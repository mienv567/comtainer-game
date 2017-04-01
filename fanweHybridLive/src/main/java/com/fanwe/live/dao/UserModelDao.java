package com.fanwe.live.dao;

import android.text.TextUtils;
import android.webkit.CookieManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.JsonDbModelDao;
import com.fanwe.hybrid.db.DbManagerX;
import com.fanwe.hybrid.model.JsonDbModel;
import com.fanwe.hybrid.utils.JsonUtil;
import com.fanwe.hybrid.utils.SDCookieFormater;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.utils.AESUtil;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDObjectCache;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.UserModel;
import com.franmontiel.persistentcookiejar.persistence.SerializableCookie;
import com.sunday.eventbus.SDEventManager;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.cookie.DbCookieStore;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

public class UserModelDao {

    private static final String COOKIES = "cookies";

    // 刘焕宇加的 将cookies存在数据库中，并且增加了相应查询和删除
    public static boolean insertOrUpdateCookie(List<HttpCookie> cookies) {
        JsonDbModel jsonDbModel = new JsonDbModel();
        jsonDbModel.setKey(COOKIES);
        jsonDbModel.setValue(JSON.toJSONString(cookies));
        try {
            DbManagerX.getDb().save(jsonDbModel);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertOrUpdate(UserModel model) {
        boolean result = false;
        SDObjectCache.put(model);
        result = JsonDbModelDao.getInstance().insertOrUpdate(model, true);
        EUpdateUserInfo event = new EUpdateUserInfo();
        event.user = model;
        SDEventManager.post(event);
        return result;

    }

    public static UserModel query() {
        return JsonDbModelDao.getInstance().query(UserModel.class, true);
    }


    // 获取cookies并且放到malatv的请求头中
    public static List<HttpCookie> queryCookie() {
        List<JsonDbModel> listJsonDbModel = null;
        List<HttpCookie> cookies = new ArrayList<>();
        try {
            listJsonDbModel = DbManagerX.getDb().selector(JsonDbModel.class).where("key", "=", COOKIES).findAll();
            if (listJsonDbModel != null && listJsonDbModel.size() == 1) {
                JsonDbModel jsonDbModel = listJsonDbModel.get(0);
                String cookie = jsonDbModel.getValue();
                if (!TextUtils.isEmpty(cookie)) {
                    JSONArray array = JSON.parseArray(cookie);
                    for (int i = 0; i < array.size(); i++) {
                        String name = array.getJSONObject(i).getString("name");
                        String value = array.getJSONObject(i).getString("value");
                        try {
                            Class c = Class.forName("java.net.HttpCookie");
                            Constructor constructor = c.getConstructor(String.class, String.class);
                            constructor.setAccessible(true);
                            HttpCookie hc = (HttpCookie) constructor.newInstance(name, value);
                            URI uri = new URI(ApkConstant.SERVER_URL_API);
                            int end = ApkConstant.SERVER_URL_DOMAIN.indexOf(":");
                            if (end > 0) {
                                hc.setDomain(ApkConstant.SERVER_URL_DOMAIN.substring(0, end));
                            } else {
                                hc.setDomain(ApkConstant.SERVER_URL_DOMAIN);
                            }
                            hc.setPath("/");
                            DbCookieStore.INSTANCE.remove(uri, hc);
                            DbCookieStore.INSTANCE.add(uri, hc);
                            cookies.add(hc);
                        } catch (Exception e) {
                            LogUtil.e(e.getMessage());
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cookies;
    }


    public static void delete() {
        SDObjectCache.remove(UserModel.class);
        JsonDbModelDao.getInstance().delete(UserModel.class);
        try {
            DbManagerX.getDb().delete(JsonDbModel.class, WhereBuilder.b("key", "=", COOKIES));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
