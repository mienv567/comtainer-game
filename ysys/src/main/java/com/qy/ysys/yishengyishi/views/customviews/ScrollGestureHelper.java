package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by tony.chen on 2017/1/6.
 */


public class ScrollGestureHelper extends GestureHelper
{

    private MyScroller myScroller;
    private boolean isVerticalScroll = false;
    private boolean isHorizontalScroll = false;

    public ScrollGestureHelper(Context context)
    {
        super(context);
        myScroller = new MyScroller(context);
    }

    public MyScroller getMyScroller()
    {
        return myScroller;
    }

    private void setHorizontalScroll(boolean isHorizontalScroll)
    {
        this.isHorizontalScroll = isHorizontalScroll;
        if (isHorizontalScroll)
        {
            isVerticalScroll = false;
        }
    }

    private void setVerticalScroll(boolean isVerticalScroll)
    {
        this.isVerticalScroll = isVerticalScroll;
        if (isVerticalScroll)
        {
            isHorizontalScroll = false;
        }
    }

    public void cancel()
    {
        isHorizontalScroll = false;
        isHorizontalScroll = false;
        recyleVelocityTracker();
    }

    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        getVelocityTracker().addMovement(e2);
        getVelocityTracker().computeCurrentVelocity(1000);

        if (isHorizontalScroll || isVerticalScroll)
        {
            return;
        } else
        {
            float dx = Math.abs(e1.getRawX() - e2.getRawX());
            float dy = Math.abs(e1.getRawY() - e2.getRawY());

            if (dx > dy)
            {
                // 水平方向
                if (dx > getViewConfiguration().getScaledTouchSlop())
                {
                    setHorizontalScroll(true);
                }
            } else
            {
                // 竖直方向
                if (dy > getViewConfiguration().getScaledTouchSlop())
                {
                    setVerticalScroll(true);
                }
            }
        }
    }

    public boolean isHorizontalScroll()
    {
        return isHorizontalScroll;
    }

    public boolean isVerticalScroll()
    {
        return isVerticalScroll;
    }
}
