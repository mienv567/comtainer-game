package com.fanwe.library.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.fanwe.library.listener.SDSizeListener;

public class SDWindowSizeListener
{

    private View view;
    private int width;
    private int height;
    private Rect rect = new Rect();

    private SDSizeListener<View> listener;

    private void reset()
    {
        width = 0;
        height = 0;
    }

    public void listen(Activity activity, SDSizeListener<View> listener)
    {
        listen(activity.findViewById(android.R.id.content), listener);
    }

    public void listen(View view, SDSizeListener<View> listener)
    {
        this.view = view;
        this.listener = listener;

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

    private void process()
    {
        int oldWidth = width;
        int oldHeight = height;

        view.getWindowVisibleDisplayFrame(rect);
        int newWidth = rect.width();
        int newHeight = rect.height();

        int differWidth = newWidth - oldWidth;
        int differHeight = newHeight - oldHeight;

        if (newWidth != oldWidth)
        {
            width = newWidth;
            if (listener != null)
            {
                listener.onWidthChanged(newWidth, oldWidth, differWidth, view);
            }
        }

        if (newHeight != oldHeight)
        {
            height = newHeight;
            if (listener != null)
            {
                listener.onHeightChanged(newHeight, oldHeight, differHeight, view);
            }
        }
    }
}
