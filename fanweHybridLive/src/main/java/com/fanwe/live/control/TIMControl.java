package com.fanwe.live.control;

import android.text.TextUtils;

import com.fanwe.library.utils.LogUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

/**
 * Created by tony.chen on 2016/12/18.
 */

public class TIMControl {

    private TIMControl() {
    }

    private static TIMControl instance = new TIMControl();

    public static TIMControl getInstance() {
        return instance;
    }


    public void loginIm(int appId, String accounType, String identifier, String usersig, final TIMCallBack callback)
    {
        TIMUser user = new TIMUser();
        user.setAccountType(accounType);
        user.setAppIdAt3rd(String.valueOf(appId));
        user.setIdentifier(identifier);
        // 发起登录请求
        TIMManager.getInstance().login(appId, user, usersig, new TIMCallBack()
        {
            @Override
            public void onError(int code, String desc)
            {
                LogUtil.i("im login error:" + code + "_" + desc);
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }

            @Override
            public void onSuccess()
            {
                LogUtil.i("im login success");
                if (callback != null)
                {
                    callback.onSuccess();
                }
            }
        });
    }


    /**
     * 退出登录
     */
    public void logoutIm() {

        String loginUser = TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(loginUser)) {
            return;
        }
        TIMManager.getInstance().logout(new TIMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.i("im logout success");

            }

            @Override
            public void onError(int code, String desc) {
                LogUtil.i("im logout error:" + code + "_" + desc);
            }
        });
    }

}
