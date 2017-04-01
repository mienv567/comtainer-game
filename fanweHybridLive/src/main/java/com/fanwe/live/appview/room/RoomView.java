package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.activity.info.LiveInfo;
import com.fanwe.live.appview.BaseAppView;

/**
 * Created by Administrator on 2016/8/4.
 */
public class RoomView extends BaseAppView
{
    private boolean isPlayback;

    public RoomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomView(Context context)
    {
        super(context);
    }


    public void setPlayback(boolean playback)
    {
        isPlayback = playback;
    }

    public boolean isPlayback()
    {
        return isPlayback;
    }

    public LiveInfo getLiveInfo()
    {
        if (getActivity() instanceof LiveInfo)
        {
            return (LiveInfo) getActivity();
        } else
        {
            return null;
        }
    }
}
