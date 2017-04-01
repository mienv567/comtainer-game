package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDHandlerLooper;

/**
 * Created by Administrator on 2016/7/15.
 */
public abstract class RoomLooperBackgroundView<T> extends RoomLooperView<T>
{
    public RoomLooperBackgroundView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomLooperBackgroundView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomLooperBackgroundView(Context context)
    {
        super(context);
    }

    @Override
    protected SDLooper createLooper()
    {
        return new SDHandlerLooper(SDHandlerManager.getBackgroundHandler());
    }
}
