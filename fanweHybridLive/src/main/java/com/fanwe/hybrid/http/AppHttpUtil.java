package com.fanwe.hybrid.http;

import android.text.TextUtils;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.utils.SDCookieFormater;
import com.fanwe.library.adapter.http.SDHttpUtil;
import com.fanwe.library.adapter.http.callback.SDRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDFileBody;
import com.fanwe.library.adapter.http.model.SDMultiFile;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDCookieManager;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.dao.UserModelDao;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AppHttpUtil extends SDHttpUtil {

    private static AppHttpUtil mInstance;

    private AppHttpUtil() {

    }

    public static AppHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (AppHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new AppHttpUtil();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected SDRequestHandler postImpl(SDRequestParams params, final SDRequestCallback callback) {
        callback.notifyStart();
        if (callback instanceof AppRequestCallback) {
            AppRequestCallback appCallback = (AppRequestCallback) callback;
            if (appCallback.isCache) {
                return null;
            }
        }
        Cancelable cancelable = x.http().post(parseRequestParams(params), new CommonCallback<String>() {
            private SDResponse response = new SDResponse();

            @Override
            public void onCancelled(CancelledException e) {
                callback.notifyCancel(response);
            }

            @Override
            public void onError(Throwable t, boolean b) {
                response.setThrowable(t);
                callback.notifyError(response);
            }

            @Override
            public void onFinished() {
                callback.notifyFinish(response);
            }

            @Override
            public void onSuccess(String result) {
                response.setResult(result);
                callback.notifySuccess(response);
            }
        });

        AppRequestHandler requestHandler = new AppRequestHandler(cancelable);
        callback.setRequestHandler(requestHandler);
        return requestHandler;
    }

    @Override
    protected SDRequestHandler getImpl(SDRequestParams params, final SDRequestCallback callback) {
        callback.notifyStart();
        if (callback instanceof AppRequestCallback) {
            AppRequestCallback appCallback = (AppRequestCallback) callback;
            if (appCallback.isCache) {
                return null;
            }
        }
        Cancelable cancelable = x.http().get(parseRequestParams(params), new CommonCallback<String>() {
            private SDResponse response = new SDResponse();

            @Override
            public void onCancelled(CancelledException e) {
                callback.notifyCancel(response);
            }

            @Override
            public void onError(Throwable t, boolean b) {
                response.setThrowable(t);
                callback.notifyError(response);
            }

            @Override
            public void onFinished() {
                callback.notifyFinish(response);
            }

            @Override
            public void onSuccess(String result) {
                response.setResult(result);
                callback.notifySuccess(response);
            }
        });

        AppRequestHandler requestHandler = new AppRequestHandler(cancelable);
        callback.setRequestHandler(requestHandler);
        return requestHandler;
    }

    public RequestParams parseRequestParams(SDRequestParams params) {
        String ctl = String.valueOf(params.getCtl());
        String act = String.valueOf(params.getAct());
        String url = params.getUrl();

        if (ApkConstant.SERVER_URL_INIT_URL.equals(url)) {

        } else {
            url = AppRuntimeWorker.getJavaApiUrl(ctl, act);
            LogUtil.i(url);
//            url = AppRuntimeWorker.getApiUrl(ctl, act);
        }
        RequestParams request = new RequestParams(url);
//        request.addHeader("Cookie", "VVNFUklORk96aF9DTg%3D%3D=NDEsMTQ4OTY2NDgwMDgyNyw2NWY1NGU0NjdhNzRiYmViZTVjNDRkMDNmMmQ4MDY4NCw%3D; JSESSIONID=26E4D03852FDF9DA8C2BB4AABC9BC824");

        // 使用cookie
        request.setUseCookie(true);
        printUrl(params);
//        initCookie();
        initHttpCookie();

        Map<String, Object> data = params.getData();
        if (!data.isEmpty()) {
            for (Entry<String, Object> item : data.entrySet()) {
                String key = item.getKey();
                Object value = item.getValue();

                if (value != null) {
                    request.addBodyParameter(key,  String.valueOf(value));
                }
            }
        }

        Map<String, SDFileBody> dataFile = params.getDataFile();
        if (!dataFile.isEmpty()) {
            request.setMultipart(true);
            for (Entry<String, SDFileBody> item : dataFile.entrySet()) {
                SDFileBody fileBody = item.getValue();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        List<SDMultiFile> listFile = params.getDataMultiFile();
        if (!listFile.isEmpty()) {
            request.setMultipart(true);
            for (SDMultiFile item : listFile) {
                SDFileBody fileBody = item.getFileBody();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        return request;
    }

    private void initHttpCookie() {
        try {
            URI uri = new URI(ApkConstant.SERVER_URL_API);
            List<HttpCookie> listCookie = DbCookieStore.INSTANCE.get(uri);
            if(listCookie != null && listCookie.size() > 0){
                return;
            }
            listCookie = UserModelDao.queryCookie();
            if (listCookie == null || listCookie.size() <= 0) {
                String cookie = SDCookieManager.getInstance().getCookie(ApkConstant.SERVER_URL_API);
                if (!TextUtils.isEmpty(cookie)) {
                    return;
                }
                listCookie = HttpCookie.parse(cookie);
                if (listCookie == null || listCookie.size() <= 0) {
                    return;
                }
            }
            for (HttpCookie item : listCookie) {
                DbCookieStore.INSTANCE.add(uri, item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("put webview cookie to http error:" + e.toString());
        }
    }

    private void initCookie() {
        String cookie = SDConfig.getInstance().getString(R.string.config_webview_cookie, "");
        if (!TextUtils.isEmpty(cookie)) {
            SDToast.showToast("添加了cookie：" + cookie);
            SDCookieFormater formater = new SDCookieFormater(cookie);
            Map<String, String> mapCookie = formater.format();
            if (!mapCookie.isEmpty()) {
                for (Entry<String, String> item : mapCookie.entrySet()) {
                    HttpCookie bcc = new HttpCookie(item.getKey(), item.getValue());

                    LogUtil.e("cookie = " + item.getKey() + item.getValue());
                    try {
                        URI uri = new URI(ApkConstant.SERVER_URL_API);
                        DbCookieStore.INSTANCE.remove(uri, bcc);
                        DbCookieStore.INSTANCE.add(uri, bcc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e("put webview cookie to http error:" + e.toString());
                    }
                }
            }
        }
    }

    private void printUrl(SDRequestParams params) {
        if (ApkConstant.DEBUG) {
            if (params != null) {
                LogUtil.i(params.parseToUrl());
            }
        }
    }
}
