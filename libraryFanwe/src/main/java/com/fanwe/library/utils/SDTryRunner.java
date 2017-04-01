package com.fanwe.library.utils;

import com.fanwe.library.common.SDHandlerManager;

/**
 * Created by Administrator on 2016/8/19.
 */
public class SDTryRunner
{
    private static final int DEFAULT_TRY_COUNT = 3;

    private int tryCount = DEFAULT_TRY_COUNT;
    private int currentCount = 0;

    /**
     * 设置重试次数
     *
     * @param tryCount
     * @return
     */
    public SDTryRunner setTryCount(int tryCount)
    {
        this.tryCount = tryCount;
        return this;
    }

    public int getTryCount()
    {
        return tryCount;
    }

    public int getCurrentCount()
    {
        return currentCount;
    }

    public boolean tryRun(Runnable runnable)
    {
        return tryRunDelayed(runnable, 0);
    }

    /**
     * 返回false：超过重试次数
     *
     * @param delay 延迟多久执行
     * @return
     */
    public boolean tryRunDelayed(Runnable runnable, long delay)
    {
        currentCount++;
        if (currentCount > tryCount)
        {
            //超过最大重试次数，不处理
            return false;
        } else
        {
            if (delay > 0)
            {
                SDHandlerManager.getMainHandler().postDelayed(runnable, delay);

            } else
            {
                runnable.run();
            }
            return true;
        }
    }
}
