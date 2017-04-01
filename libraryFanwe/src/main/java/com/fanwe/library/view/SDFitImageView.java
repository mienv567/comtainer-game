package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewUtil;

public class SDFitImageView extends ImageView
{
    public SDFitImageView(Context context)
    {
        super(context);
        init();
    }

    public SDFitImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public SDFitImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        scaleViewSize(getDrawable());
    }

    private void scaleViewSize(Drawable drawable)
    {
        if (drawable != null)
        {
            SDViewUtil.scaleViewSize(this, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }


}
