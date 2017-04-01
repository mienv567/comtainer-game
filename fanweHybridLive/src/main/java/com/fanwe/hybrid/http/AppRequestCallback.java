package com.fanwe.hybrid.http;

import android.text.TextUtils;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.JsonDbModelDao;
import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.hybrid.http.AppRequestParams.ResponseDataType;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.utils.JsonUtil;
import com.fanwe.library.adapter.http.callback.SDModelRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.live.model.App_root_model;
import com.sunday.eventbus.SDEventManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class AppRequestCallback<D> extends SDModelRequestCallback<D> {

    protected AppRequestParams requestParams;
    protected BaseActModel baseActModel;
    protected boolean isCache;

    @Override
    protected void onStartAfter() {
        SDRequestParams sp = getRequestParams();
        if (sp != null && sp instanceof AppRequestParams) {
            requestParams = (AppRequestParams) sp;
        }

        if (clazz != null) {
            D model = JsonDbModelDao.getInstance().query(clazz);
            if (model != null) {
                if (model instanceof BaseActModel) {
                    BaseActModel baseActModel = (BaseActModel) model;

                    long current = System.currentTimeMillis();
                    long endTime = baseActModel.getExpiry_after();

                    if (current <= endTime * 1000) {
                        // 缓存还没过期
                        isCache = true;
                        SDRequestHandler requestHandler = getRequestHandler();
                        if (requestHandler != null) {
                            requestHandler.cancel();
                        }

                        SDResponse resp = new SDResponse();
                        resp.setResult(JsonUtil.object2Json(model));
                        if (requestParams != null) {
                            requestParams.setResponseDataType(ResponseDataType.JSON);
                        }
                        notifySuccess(resp);
                    } else {
                        JsonDbModelDao.getInstance().delete(clazz);
                    }
                }
            }
        }
    }

    @Override
    protected void onSuccessBefore(SDResponse resp) {
        if (requestParams != null) {
            LogUtil.i("onSuccessBefore:" + requestParams.getCtl() + "," + requestParams.getAct() + "：" + resp.getResult());

            String result = resp.getResult();
            if (!TextUtils.isEmpty(result)) {
                //                if (ApkConstant.DEBUG)
                //                {
                //                    if (result.contains("false"))
                //                    {
                //                        SDToast.showToast(requestParams.getCtl() + "," + requestParams.getAct() + " false");
                //                    }
                //                }

                switch (requestParams.getResponseDataType()) {
                    case ResponseDataType.BASE64:

                        break;
                    case ResponseDataType.JSON:

                        break;
                    case ResponseDataType.AES:

                        break;

                    default:
                        break;
                }
                // 解密后的result赋值回去
//                try {
//                    result = URLDecoder.decode(result,"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                resp.setResult(result);

            }
        }


        // 调用父类方法转实体 此处废弃掉这个方法
        super.onSuccessBefore(resp);

        String result = resp.getResult();
        // 后台切换之后，返回数据的格式变了，此时先将返回的obj从rootModel中取出，再返给resp的result，这样可以保证super的onSuccessBefore可以正常处理
        App_root_model rootModel = SDJsonUtil.json2Object(result, App_root_model.class);

        if (rootModel.getReturnObj() == null) {
            if (this.clazz != null) {
                try {
                    actModel = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else
            actModel = SDJsonUtil.json2Object(rootModel.getReturnObj(), clazz);
        if ("0000".equals(rootModel.getCode())) {
            ((BaseActModel) actModel).setStatus(1);
        } else {
            ((BaseActModel) actModel).setError(rootModel.getMessage());
        }

        if (actModel != null) {
            if (actModel instanceof BaseActModel) {
                baseActModel = (BaseActModel) actModel;
                if (baseActModel.getExpiry_after() > 0) {
                    JsonDbModelDao.getInstance().insertOrUpdate(actModel);
                }
                if (requestParams != null) {
                    //                    if (requestParams.isNeedShowActInfo())
                    //                    {
                    //                        SDToast.showToast(baseActModel.getError());
                    //                    }
                    if (requestParams.isNeedCheckLoginState()) {
                        if (baseActModel.getUser_login_status() == 0) {
                            // 未登录
                            EUnLogin event = new EUnLogin();
                            SDEventManager.post(event);

                            App.getApplication().logout(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSuccessAfter(SDResponse resp) {
        if (isCache) {
            notifyFinish(resp);
        }
    }

    @Override
    protected void onError(SDResponse resp) {
        if (requestParams != null) {
            LogUtil.i("onError:" + requestParams.getCtl() + "," + requestParams.getAct() + "：" + resp.getThrowable());

            //            if (ApkConstant.DEBUG)
            //            {
            //                SDToast.showToast(requestParams.getCtl() + "," + requestParams.getAct() + " " + String.valueOf(resp.getThrowable()));
            //            }
        }
    }

    @Override
    protected void onCancel(SDResponse resp) {

    }

    @Override
    protected void onFinish(SDResponse resp) {

    }

    @Override
    protected <T> T parseActModel(String result, Class<T> clazz) {
        return null;
    }
}
