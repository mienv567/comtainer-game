package com.qy.ysys.yishengyishi.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


/**
 * Created by TonyChen on 2016/12/31.
 */

public class HandlerManager {
    private HandlerManager() {
    }

    private final static Object lock = new Object();

    private final static HandlerThread thread = new HandlerThread("SECOND_HANDLER_THREAD");

    private final static android.os.Handler mainHandler = new android.os.Handler(Looper.getMainLooper());
    private static Handler backgroundHandler;

    private static boolean isBackgroundHandlerStart = false;

    public static Handler getMainHamdler() {
        return mainHandler;
    }

    public final static Handler getBackgroundHandler() {
        startBackgroundHandler();
        return backgroundHandler;
    }

    /**
     * 开始后台线程
     */
    public final static void startBackgroundHandler() {
        synchronized (lock) {
            if (!isBackgroundHandlerStart) {
                thread.start();
                backgroundHandler = new Handler(thread.getLooper());
                isBackgroundHandlerStart = true;
            }
        }
    }

    /**
     * 结束后台线程
     */
    public final static void stopBackgroundHandler() {
        synchronized (lock) {
            if (isBackgroundHandlerStart) {
                thread.quit();
                isBackgroundHandlerStart = false;
            }
        }
    }
}
