package com.fanwe.library.gesture;

import android.content.Context;
import android.widget.Scroller;

public class SDScroller extends Scroller
{

    public static final int DEFAULT_DURATION = 250;

    public SDScroller(Context context)
    {
        super(context);
    }

    // scroll
    public void startScrollX(int startX, int dx)
    {
        startScrollX(startX, dx, DEFAULT_DURATION);
    }

    public void startScrollX(int startX, int dx, int duration)
    {
        startScroll(startX, 0, dx, 0, duration);
    }

    public void startScrollY(int startY, int dy)
    {
        startScrollY(startY, dy, DEFAULT_DURATION);
    }

    public void startScrollY(int startY, int dy, int duration)
    {
        super.startScroll(0, startY, 0, dy, duration);
    }

    // scrollTo
    public void startScrollToX(int startX, int endX)
    {
        startScrollToX(startX, endX, DEFAULT_DURATION);
    }

    public void startScrollToX(int startX, int endX, int duration)
    {
        startScrollTo(startX, 0, endX, 0, duration);
    }

    public void startScrollToY(int startY, int endY)
    {
        startScrollToY(startY, endY, DEFAULT_DURATION);
    }

    public void startScrollToY(int startY, int endY, int duration)
    {
        startScrollTo(0, startY, 0, endY, duration);
    }

    public void startScrollTo(int startX, int startY, int endX, int endY)
    {
        startScrollTo(startX, startY, endX, endY, DEFAULT_DURATION);
    }

    public void startScrollTo(int startX, int startY, int endX, int endY, int duration)
    {
        int dx = 0;
        int dy = 0;

        dx = endX - startX;
        dy = endY - startY;

        startScroll(startX, startY, dx, dy, duration);
    }

}
