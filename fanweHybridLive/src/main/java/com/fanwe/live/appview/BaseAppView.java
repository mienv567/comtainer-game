package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.view.SDAppView;

import org.xutils.x;

public class BaseAppView extends SDAppView
{
    public BaseAppView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public BaseAppView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseAppView(Context context)
    {
        super(context);
    }

    @Override
    public void setContentView(int layoutId)
    {
        super.setContentView(layoutId);
        x.view().inject(this, this);
    }
}
