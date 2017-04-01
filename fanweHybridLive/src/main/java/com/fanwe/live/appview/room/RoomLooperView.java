package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.looper.SDLooper;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/15.
 */
public abstract class RoomLooperView<T> extends RoomView
{
    public RoomLooperView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomLooperView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomLooperView(Context context)
    {
        super(context);
    }

    private SDLooper looper;
    protected LinkedList<T> queue = new LinkedList<T>();

    protected abstract SDLooper createLooper();

    private SDLooper getLooper()
    {
        if (looper == null)
        {
            looper = createLooper();
        }
        return looper;
    }

    protected void offerModel(T model)
    {
        if (model != null)
        {
            queue.offer(model);
        }

        if (!queue.isEmpty())
        {
            startLooper(200);
        }
    }

    protected abstract void looperWork(LinkedList<T> queue);

    private void afterLooperWork()
    {
        if (queue.isEmpty())
        {
            stopLooper();
        }
    }

    protected void startLooper(long period)
    {
        if (!getLooper().isRunning())
        {
            getLooper().start(period, new Runnable()
            {

                @Override
                public void run()
                {
                    looperWork(queue);
                    afterLooperWork();
                }
            });
        }
    }

    protected void stopLooper()
    {
        getLooper().stop();
    }

    @Override
    public void onDestroy()
    {
        stopLooper();
        super.onDestroy();
    }
}
