package com.fanwe.hybrid.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.fanwe.hybrid.common.ImageLoaderManager;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.event.EExitApp;
import com.fanwe.hybrid.event.EJsLogout;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.hybrid.push.PushRunnable;
import com.fanwe.hybrid.umeng.UmengPushManager;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.media.recorder.SDMediaRecorder;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDObjectCache;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveIniter;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.event.EUserLogout;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.AIDUtil;
import com.fanwe.live.utils.ChannelUtil;
import com.fanwe.live.utils.FileUtils;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qy.ysys.yishengyishi.AppImpl;
import com.sunday.eventbus.SDEventManager;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;
import com.umeng.analytics.MobclickAgent;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.SubscriberExceptionEvent;

public class App extends Application implements TANetChangeObserver {
    private static App instance;
    private PushRunnable pushRunnable;
    public static boolean mShowSignRedPoint = false;
    public static boolean mShowTaskRedPoint = false;

    public static App getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        final App app = this;

        // 为了把一生一世的App也持有application
        AppImpl.setApplication(this);
        Logger.init(getString(com.qy.ysys.yishengyishi.R.string.appname))                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(0);              // default 0

        initServerAddress();
        new Runnable() {
            @Override
            public void run() {

                //runtimeData
                SDObjectCache.put(UserModelDao.query());

                SDEventManager.register(app);
                TANetworkStateReceiver.registerNetworkStateReceiver(app);
                TANetworkStateReceiver.registerObserver(app);
                ImageLoaderManager.initImageLoader();
                x.Ext.init(app);
                //fanwe library
                SDLibrary.getInstance().init(app);
                SDLibraryConfig config = new SDLibraryConfig();
                config.setMainColor(getResources().getColor(R.color.main_color));
                config.setMainColorPress(getResources().getColor(R.color.main_color_press));
                config.setTitleColor(getResources().getColor(R.color.bg_main));
                config.setTitleColorPressed(getResources().getColor(R.color.bg_title_bar_pressed));
                config.setTitleTextColor(getResources().getColor(R.color.text_title_bar));
                config.setTitleHeight(getResources().getDimensionPixelOffset(R.dimen.height_title_bar));
                config.setStrokeColor(getResources().getColor(R.color.stroke));
                config.setStrokeWidth(SDViewUtil.dp2px(1));
                config.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.corner));
                config.setGrayPressColor(getResources().getColor(R.color.gray_press));
                SDLibrary.getInstance().setConfig(config);

                UmengPushManager.init(app);
                initUmengAnalytics(app);
                createCacheDir();
                SDTencentMapManager.getInstance().init(app);
                new LiveIniter().init(app);
                initSystemListener();
                SDMediaRecorder.getInstance().init(app);
                LogUtil.isDebug = ApkConstant.DEBUG;

                ShareSDK.initSDK(app);

                //init vk
                vkAccessTokenTracker.startTracking();
                VKSdk.initialize(app);
                //init kakao
                KakaoSDK.init(new KakaoSDKAdapter());
            }
        }.run();
    }

    /**
     * 搞一下服务器地址
     */
    private void initServerAddress() {
        SharedPreferences sp = getSharedPreferences(ApkConstant.SERVER_SP, MODE_PRIVATE);
        int index = sp.getInt(ApkConstant.SERVER_SP_SELECT, -1);
        if (index != -1) {
            try {
                Properties prop = new Properties();
                InputStream fs = this.getAssets().open("serverAddress.properties");
                prop.load(fs);
                String server = prop.getProperty("server" + index);
                if(!TextUtils.isEmpty(server)) {
                    ApkConstant.SERVER_URL_DOMAIN = server;
                    ApkConstant.SERVER_URL_API = ApkConstant.SERVER_URL_SCHEMES + ApkConstant.SERVER_URL_DOMAIN + ApkConstant.SERVER_URL_PATH_API;;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCacheDir() {
        String rootDir = AIDUtil.getRootDir();
        FileUtils.createDir(rootDir, true);
    }

    private void initUmengAnalytics(Context app) {
        MobclickAgent.setScenarioType(app, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(app, app.getString(R.string.umeng_appkey), ChannelUtil.getChannel(app));
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(false);
    }

    private void initSystemListener() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                EOnCallStateChanged event = new EOnCallStateChanged();
                event.state = state;
                event.incomingNumber = incomingNumber;
                SDEventManager.post(event);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public boolean isPushStartActivity(Class<?> clazz) {
        boolean result = false;
        if (pushRunnable != null) {
            result = pushRunnable.getStartActivity() == clazz;
        }
        return result;
    }

    public void setPushRunnable(PushRunnable pushRunnable) {
        this.pushRunnable = pushRunnable;
    }

    public PushRunnable getPushRunnable() {
        return pushRunnable;
    }

    public void startPushRunnable() {
        if (pushRunnable != null) {
            pushRunnable.run();
            pushRunnable = null;
        }
    }


    public void exitApp(boolean isBackground) {
        MobclickAgent.onKillProcess(this);
        AppRuntimeWorker.logoutIm(true);
        //AppRuntimeWorker.stopContext();
        SDActivityManager.getInstance().finishAllActivity();
        EExitApp event = new EExitApp();
        SDEventManager.post(event);
        RTCMediaStreamingManager.deinit();
        if (!isBackground) {
            System.exit(0);
        }
    }

    /**
     * 退出登录
     *
     * @param post
     */
    public void logout(boolean post) {
        UserModelDao.delete();
        AppRuntimeWorker.setUsersig(null);
        AppRuntimeWorker.logoutIm(true);
        CommonInterface.requestLogout(null);
        CommonInterface.requestInit(null);

        Intent intent = new Intent(this, LiveLoginActivity.class);
        SDActivityManager.getInstance().getLastActivity().startActivity(intent);

//        SDActivityManager.getInstance().finishAllActivity();
//        Intent intent = new Intent(this, LiveLoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);

        if (post) {
            EUserLogout event = new EUserLogout();
            SDEventManager.post(event);
        }
    }

    /**
     * 退出登录
     *
     * @param event
     */
    public void onEventMainThread(EJsLogout event) {
        logout(true);
    }

    public void onEventMainThread(EUserLoginSuccess event) {
        AppRuntimeWorker.setUsersig(null);
        CommonInterface.requestMyUserInfoJava(new AppRequestCallback<UserModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    CommonInterface.requestUsersig(null);
                }
            }
        });
    }

    public void onEventMainThread(SubscriberExceptionEvent event) {
        event.throwable.printStackTrace();
    }

    @Override
    public void onTerminate() {
        SDEventManager.unregister(instance);
        TANetworkStateReceiver.unRegisterNetworkStateReceiver(instance);
        TANetworkStateReceiver.removeRegisterObserver(instance);
        SDHandlerManager.stopBackgroundHandler();
        SDMediaRecorder.getInstance().release();
        super.onTerminate();
    }

    @Override
    public void onConnect(netType type) {

    }

    @Override
    public void onDisConnect() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(instance, LiveLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_TALK};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return instance;
                }
            };
        }
    }
}
