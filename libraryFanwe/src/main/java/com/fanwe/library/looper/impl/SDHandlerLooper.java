package com.fanwe.library.looper.impl;

import android.os.Handler;

import com.fanwe.library.looper.SDLooper;

public class SDHandlerLooper implements SDLooper
{
    private Object lock = new Object();

    private Runnable runnable;
    private long period;
    private boolean isRunning = false;
    private boolean isNeedStop = false;

    private Handler handler;

    public SDHandlerLooper(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public SDLooper start(Runnable runnable)
    {
        start(0, DEFAULT_PERIOD, runnable);
        return this;
    }

    @Override
    public SDLooper start(long period, Runnable runnable)
    {
        start(0, period, runnable);
        return this;
    }

    @Override
    public SDLooper start(long delay, long period, Runnable runnable)
    {
        stop();

        this.isRunning = true;
        this.isNeedStop = false;
        this.runnable = runnable;
        this.period = period;

        if (period <= 0)
        {
            period = DEFAULT_PERIOD;
        }
        sendMessageDelayed(delay);
        return this;
    }

    private Runnable realRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (lock)
            {
                if (isNeedStop)
                {

                } else
                {
                    if (runnable != null)
                    {
                        runnable.run();
                        sendMessageDelayed(period);
                    } else
                    {
                        stop();
                    }
                }
            }
        }
    };

    private void sendMessageDelayed(long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        handler.postDelayed(realRunnable, delay);
    }

    @Override
    public SDLooper stop()
    {
        synchronized (lock)
        {
            handler.removeCallbacks(realRunnable);
            isRunning = false;
            isNeedStop = true;
        }
        return this;
    }

}
