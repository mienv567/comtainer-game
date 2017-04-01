package com.qy.ysys.yishengyishi;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.qy.ysys.yishengyishi.model.item.UserInfo;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class AppImpl extends Application {
    public static Application mApp = null;
    public static UserInfo mCurrentUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        initApp();
    }

    public static Application getApplication() {
        return mApp;
    }

    public static void setApplication(Application application){
        mApp = application;
    }

    public void initApp() {
        mApp = this;
        initLogger();
    }

    public void initLogger() {
        Logger.init(getString(com.qy.ysys.yishengyishi.R.string.appname))                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(0);              // default 0
    }



    public static void setCurrentUser(UserInfo userModel){
        mCurrentUser = userModel;
    }

    public static UserInfo getCurrentUser(){
        return mCurrentUser;
    }

}
