package com.fanwe.live.http;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.live.utils.ClassUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

/**
 * Created by yong.zhang on 2017/3/22 0022.
 */
public abstract class HttpCallback<T> implements Callback.CommonCallback<String> {

    public final static String MSG_UNKNOWN = "未知错误";

    public final static int CODE_UNKNOWN = -1;

    public final static int CODE_JSON = -2;

    public final static String KEY_CODE = "code";

    public final static String KEY_MSG = "message";

    public final static String KEY_DATA = "returnObj";

    public final static String SEPARATOR = "::";

    @Override
    public final void onSuccess(String response) {
        LogUtil.i(getClass().toString() + SEPARATOR + response);
        if (TextUtils.isEmpty(response)) {
            onError(CODE_UNKNOWN, MSG_UNKNOWN);
            return;
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (obj == null) {
            onError(CODE_JSON, response);
            return;
        }
        int code = obj.optInt(KEY_CODE);
        String message = obj.optString(KEY_MSG);
        String returnObj = obj.optString(KEY_DATA);
        T t = null;
        if (!TextUtils.isEmpty(returnObj)) {
            Gson gson = new Gson();
            try {
                t = gson.fromJson(returnObj, ClassUtils.getType(getClass(), 0));
            } catch (JsonSyntaxException e) {
                LogUtil.e(Log.getStackTraceString(e));
            }
        }
        onSuccess(code, message, t);
        LogUtil.i(code + SEPARATOR + message);
    }

    public void onError(int code, String message) {
        onError(new HttpUnkownError(code, message), true);
        LogUtil.i(code + SEPARATOR + message);
    }

    @Override
    public void onFinished() {

    }

    /**
     * 服务器返回的数据解析成功，不代表业务上请求成功
     */
    public abstract void onSuccess(int code, String msg, T result);
}
