package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.fanwe.live.R;

public class LiveLayoutViewerPlayback extends LiveLayoutViewer
{

    public LiveLayoutViewerPlayback(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveLayoutViewerPlayback(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveLayoutViewerPlayback(Context context)
    {
        super(context);
    }

    @Override
    protected void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.include_live_layout_playback_new, this, true);
    }

}
