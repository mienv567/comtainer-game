package com.qy.ysys.yishengyishi.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.UserModel;
import com.qy.ysys.yishengyishi.presenters.viewinterface.ISplashView;
import com.qy.ysys.yishengyishi.utils.HandlerManager;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.MainActivity;
import com.qy.ysys.yishengyishi.views.MobileRegisterViewActivity;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tony.chen on 2017/1/3.
 */

public class SplashHelper extends Presenter {
    private ISplashView splashImpl = null;
    private Context context = null;
    private long lastBackPressedTime = 0;


    private Runnable jumpMainActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    };
    private Runnable jumpRegisterActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(context, MobileRegisterViewActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    };

    public SplashHelper(Context context, ISplashView splashImpl) {
        this.context = context;
        this.splashImpl = splashImpl;
    }


    public void showSplashImange() {
        splashImpl.showSplashImange();

        RequestInterface.getUserInfo(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.body() != null){
                    if (response.body().isNotLogin()) {
                        HandlerManager.getMainHamdler().post(jumpRegisterActivity);
                    } else {
                        AppImpl.setCurrentUser(response.body().getReturnObj());
                        HandlerManager.getMainHamdler().post(jumpMainActivity);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                ToastUtil.showToast(t.getMessage());
            }
        });
    }


    public void onBackPressed() {
//        long currentBackPressedTime = System.currentTimeMillis();
//        if (currentBackPressedTime - lastBackPressedTime <= 1000) {
//            // 通知系统,退出程序
//            HandlerManager.getMainHamdler().removeCallbacks(jumpMainActivity);
//            AppImpl.getApplication().exitApp();
//        } else {
//            lastBackPressedTime = currentBackPressedTime;
//            ToastUtil.showToast("连续点击两次退出应用");
//        }

    }

    @Override
    public void onDestory() {
        ISplashView splashImpl = null;
        Context context = null;
    }
}
