package com.fanwe.library.utils;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class SDViewPositionListener
{

    private View view;
    private int x;
    private int y;

    private ViewPositionListener listener;

    public void setListener(ViewPositionListener listener)
    {
        this.listener = listener;
    }

    private void reset()
    {
        x = 0;
        y = 0;
    }

    public void listen(View view, ViewPositionListener listener)
    {
        this.view = view;
        this.listener = listener;

        if (view != null)
        {
            reset();
            view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    process();
                }
            });
        }
    }

    private void process()
    {
        int oldX = x;
        int oldY = y;

        int[] location = getLocationOnScreen(view);
        int newX = location[0];
        int newY = location[1];

        if (newX != oldX)
        {
            x = newX;
            if (listener != null)
            {
                listener.onXChanged(newX, oldX, newX - oldX);
            }
        }

        if (newY != oldY)
        {
            y = newY;
            if (listener != null)
            {
                listener.onYChanged(newY, oldY, newY - oldY);
            }
        }
    }

    private int[] getLocationOnScreen(View v)
    {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    public interface ViewPositionListener
    {
        void onYChanged(int newY, int oldY, int differ);

        void onXChanged(int newX, int oldX, int differ);
    }

}
