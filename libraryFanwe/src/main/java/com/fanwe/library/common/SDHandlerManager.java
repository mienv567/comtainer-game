package com.fanwe.library.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by Administrator on 2016/7/14.
 */
public class SDHandlerManager
{

    private final static Object lock = new Object();

    private final static HandlerThread thread = new HandlerThread("handler thread");

    private final static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static Handler backgroundHandler;

    private static boolean isBackgroundHandlerStart = false;

    public final static Handler getMainHandler()
    {
        return mainHandler;
    }

    public final static Handler getBackgroundHandler()
    {
        startBackgroundHandler();
        return backgroundHandler;
    }

    /**
     * 开始后台线程
     */
    public final static void startBackgroundHandler()
    {
        synchronized (lock)
        {
            if (!isBackgroundHandlerStart)
            {
                thread.start();
                backgroundHandler = new Handler(thread.getLooper());
                isBackgroundHandlerStart = true;
            }
        }
    }

    /**
     * 结束后台线程
     */
    public final static void stopBackgroundHandler()
    {
        synchronized (lock)
        {
            if (isBackgroundHandlerStart)
            {
                thread.quit();
                isBackgroundHandlerStart = false;
            }
        }
    }

}
