package com.qy.ysys.yishengyishi.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qy.ysys.yishengyishi.AppConfig;
import com.qy.ysys.yishengyishi.presenters.viewinterface.IRegisterView;
import com.qy.ysys.yishengyishi.utils.HandlerManager;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.MainActivity;
import com.qy.ysys.yishengyishi.views.RegisterFillActivity;

/**
 * Created by tony.chen on 2017/1/4.
 */

public class RegisterHelper extends Presenter {
    private Context mContext = null;
    private IRegisterView mRegisterView = null;

    private Runnable jumpMainActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
    };    private Runnable jumpRegisterFillActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(mContext, RegisterFillActivity.class);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
    };

    @Override
    public void onDestory() {
        Context mContext = null;
        IRegisterView mRegisterView = null;
    }

    public RegisterHelper(Context context, IRegisterView registerView) {
        mContext = context;
        mRegisterView = registerView;
    }

    public void loginIn() {
//        ToastUtil.showToast("loginin....");
        // 请求网络已经注册用户信息,直接跳到主界面,没有则跳转到完善信息页
        String userName = (String) SPUtils.getParam(AppConfig.USERINFO, "");
        if ("".equals(userName)) {
            HandlerManager.getMainHamdler().post(jumpRegisterFillActivity);
        } else {
            HandlerManager.getMainHamdler().post(jumpMainActivity);
        }
    }

    public void clickTvAgreement() {
        ToastUtil.showToast("你点击查看协议");
    }


}
