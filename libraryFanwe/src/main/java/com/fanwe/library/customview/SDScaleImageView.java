package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 用com.fanwe.library.view.SDFitImageView替代
 */
@Deprecated
public class SDScaleImageView extends ImageView
{

    private boolean isFirstVisible = true;

    public SDScaleImageView(Context context)
    {
        super(context);
        init();
    }

    public SDScaleImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public SDScaleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE)
        {
            if (isFirstVisible)
            {
                isFirstVisible = false;
                SDHandlerManager.getMainHandler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SDViewUtil.updateImageViewSize(SDScaleImageView.this, getDrawable());
                    }
                }, 100);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        SDViewUtil.updateImageViewSize(this, this.getDrawable());
    }
}
