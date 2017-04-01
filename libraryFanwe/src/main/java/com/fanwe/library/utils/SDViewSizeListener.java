package com.fanwe.library.utils;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.fanwe.library.listener.SDSizeListener;

public class SDViewSizeListener
{

    private View view;
    private int width;
    private int height;

    private int firstHeight;
    private int firstWidth;

    private SDSizeListener<View> listener;


    /**
     * 获得view第一次测量到的高度
     *
     * @return
     */
    public int getFirstHeight()
    {
        return firstHeight;
    }

    /**
     * 获得view第一次测量到的宽度
     *
     * @return
     */
    public int getFirstWidth()
    {
        return firstWidth;
    }

    private void reset()
    {
        width = 0;
        height = 0;
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

        int newWidth = view.getWidth();
        int newHeight = view.getHeight();

        int differWidth = newWidth - oldWidth;
        int differHeight = newHeight - oldHeight;

        if (newWidth != oldWidth)
        {
            width = newWidth;

            //保存第一次测量到的高度
            if (firstWidth <= 0)
            {
                if (newWidth > 0)
                {
                    firstWidth = newWidth;
                }
            }

            if (listener != null)
            {
                listener.onWidthChanged(newWidth, oldWidth, differWidth, view);
            }
        }

        if (newHeight != oldHeight)
        {
            height = newHeight;

            //保存第一次测量到的高度
            if (firstHeight <= 0)
            {
                if (newHeight > 0)
                {
                    firstHeight = newHeight;
                }
            }

            if (listener != null)
            {
                listener.onHeightChanged(newHeight, oldHeight, differHeight, view);
            }
        }
    }
}
