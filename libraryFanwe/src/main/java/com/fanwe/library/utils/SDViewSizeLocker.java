package com.fanwe.library.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/8/30.
 */
public class SDViewSizeLocker
{

    private View view;

    private int width;
    private int height;
    private float weight;

    private boolean hasLockWidth;
    private boolean hasLockHeight;


    public SDViewSizeLocker(View view)
    {
        this.view = view;
    }

    public void setView(View view)
    {
        this.view = view;
    }

    private ViewGroup.LayoutParams getLayoutParams()
    {
        return view.getLayoutParams();
    }

    private void setLayoutParams(ViewGroup.LayoutParams params)
    {
        view.setLayoutParams(params);
    }

    public boolean hasLockHeight()
    {
        return hasLockHeight;
    }

    public boolean hasLockWidth()
    {
        return hasLockWidth;
    }

    // lock
    public void lockWidth()
    {
        lockWidth(view.getWidth());
    }

    public void lockWidth(int lockWidth)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null)
        {
            if (hasLockWidth)
            {
                //如果已经锁了，直接赋值
                params.width = lockWidth;
            } else
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    weight = lParams.weight;
                    lParams.weight = 0;
                }

                width = params.width;
                params.width = lockWidth;
                hasLockWidth = true;
            }
            setLayoutParams(params);
        }
    }

    public void unlockWidth()
    {
        if (hasLockWidth)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = weight;
                }
                params.width = width;
                setLayoutParams(params);
            }
            hasLockWidth = false;
        }
    }

    public void lockHeight()
    {
        lockHeight(view.getHeight());
    }

    public void lockHeight(int lockHeight)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null)
        {
            if (hasLockHeight)
            {
                //如果已经锁了，直接赋值
                params.height = lockHeight;
            } else
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    weight = lParams.weight;
                    lParams.weight = 0;
                }

                height = params.height;
                params.height = lockHeight;
                hasLockHeight = true;
            }
            setLayoutParams(params);
        }
    }

    public void unlockHeight()
    {
        if (hasLockHeight)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = weight;
                }
                params.height = height;
                setLayoutParams(params);
            }
            hasLockHeight = false;
        }
    }

}
