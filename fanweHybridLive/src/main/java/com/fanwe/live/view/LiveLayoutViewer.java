package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;

public class LiveLayoutViewer extends LinearLayout
{

    public LiveLayoutViewer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveLayoutViewer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveLayoutViewer(Context context)
    {
        super(context);
        init();
    }

    protected void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.include_live_layout_viewer_new, this, true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (LiveConstant.CAN_MOVE_VIDEO)
        {
//            AVUIControl control = QavsdkControl.getInstance().getAVUIControl();
//            if (control != null && control.isTouchGlVideoView(ev))
//            {
//                return true;
//            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (LiveConstant.CAN_MOVE_VIDEO)
        {
//            AVUIControl control = QavsdkControl.getInstance().getAVUIControl();
//            if (control != null && control.isTouchGlVideoView(event))
//            {
//                return false;
//            }
        }
        return super.onTouchEvent(event);
    }

}
