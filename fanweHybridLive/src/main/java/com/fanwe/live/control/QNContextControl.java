package com.fanwe.live.control;

import com.fanwe.library.utils.LogUtil;
import com.tencent.TIMCallBack;

/**
 * Created by tony.chen on 2016/12/18.
 */

public class QNContextControl {

    private boolean isContextStarted = false;

    public boolean isContextStarted() {
        return isContextStarted;
    }

    public QNContextControl() {
        timControl = TIMControl.getInstance();
    }

//    private static QNContextControl instance = new QNContextControl();
//
//    public static QNContextControl getInstance() {
//        return instance;
//    }

    public TIMControl getTimControl() {
        return timControl;
    }

    public void setTimControl(TIMControl timControl) {
        this.timControl = timControl;
    }

    private TIMControl timControl = null;

    /**
     * 初始化imsdk,七牛sdk,程序关于直播的初始化
     */
    public void startContext(final int appId, final String accounType, final String identifier, final String usersig) {
        timControl.loginIm(appId, accounType, identifier, usersig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.i("QNContextControl#startContext#onError");
            }

            @Override
            public void onSuccess() {
                LogUtil.i("QNContextControl#startContext#onSuccess");
                isContextStarted = true; // 本来用于记录avsdk是否正常启动的,现在七牛没这道,反正初始化了im就当初始化了context呗
            }
        });
    }

    /**
     * 每次退出app调用
     */
    public void stopContext() {

        isContextStarted = false;

    }


}
