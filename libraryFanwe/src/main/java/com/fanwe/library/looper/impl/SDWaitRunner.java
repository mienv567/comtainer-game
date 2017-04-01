package com.fanwe.library.looper.impl;

import com.fanwe.library.looper.SDTimeouter;

public class SDWaitRunner implements SDTimeouter
{
    public static final long DEFAULT_TIMEOUT = 10 * 1000;
    public static final long DEFAULT_PERIOD = 300;

    private SDSimpleTimeoutLooper looper = new SDSimpleTimeoutLooper();
    private RunnableCondition runnableCondition;
    private Runnable runnable;

    public SDWaitRunner run(Runnable runnable)
    {
        this.runnable = runnable;
        return this;
    }

    public SDWaitRunner condition(RunnableCondition runnableCondition)
    {
        this.runnableCondition = runnableCondition;
        return this;
    }

    public SDWaitRunner startWait()
    {
        startWait(DEFAULT_PERIOD);
        return this;
    }

    public SDWaitRunner startWait(long period)
    {
        if (getTimeout() <= 0)
        {
            // 设置一个默认超时，以免一直等待，如果确实需要一直等待，在等待方法执行之后调用stopTimeout()即可
            setTimeout(DEFAULT_TIMEOUT);
        }
        looper.start(period, loopRunnable);
        return this;
    }

    private Runnable loopRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            if (runnableCondition != null)
            {
                if (runnableCondition.canRun())
                {
                    runRunnable();
                    stopWait();
                } else
                {
                    // wait...
                }
            } else
            {
                stopWait();
            }
        }
    };

    private void runRunnable()
    {
        if (runnable != null)
        {
            runnable.run();
        }
    }

    public SDWaitRunner stopWait()
    {
        looper.stop();
        return this;
    }

    @Override
    public long getTimeout()
    {
        return looper.getTimeout();
    }

    @Override
    public boolean isTimeout()
    {
        return looper.isTimeout();
    }

    @Override
    public Runnable getTimeoutRunnable()
    {
        return looper.getTimeoutRunnable();
    }

    @Override
    public SDWaitRunner timeout(Runnable timeoutRunnable)
    {
        looper.timeout(timeoutRunnable);
        return this;
    }

    @Override
    public SDWaitRunner runTimeout()
    {
        looper.runTimeout();
        return this;
    }

    @Override
    public SDWaitRunner setTimeout(long timeout)
    {
        looper.setTimeout(timeout);
        return this;
    }

    @Override
    public SDWaitRunner stopTimeout()
    {
        looper.stopTimeout();
        return this;
    }

    public interface RunnableCondition
    {
        boolean canRun();
    }

}
