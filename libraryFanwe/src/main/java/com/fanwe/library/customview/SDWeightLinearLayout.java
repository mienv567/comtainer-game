package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewUtil;

public class SDWeightLinearLayout extends LinearLayout
{

    public SDWeightLinearLayout(Context context)
    {
        this(context, null);
    }

    public SDWeightLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
    }

    public void startWeightView()
    {
        calculateWidth(this, new CalculateWidthListener()
        {

            @Override
            public void onResult(int width0, int width1, int widthTotal, int widthLeft, ViewGroup parent)
            {
                if (width0 > widthLeft && widthLeft > 0)
                {
                    // 空间不够
                    SDViewUtil.setViewWidthWeightContent(parent.getChildAt(0), 1);
                } else
                {
                    SDViewUtil.setViewWidthWrapContent(parent.getChildAt(0));
                }
            }
        });
    }

    public static void calculateWidth(final ViewGroup parent, final CalculateWidthListener listener)
    {
        View child0 = parent.getChildAt(0);
        View child1 = parent.getChildAt(1);

        int width0 = SDViewUtil.getViewWidth(child0);
        int width1 = SDViewUtil.getViewWidth(child1);
        int widthParent = parent.getWidth();

        int widthParentAvalible = widthParent - parent.getPaddingLeft() - parent.getPaddingRight();
        int width1All = width1;
        MarginLayoutParams params1 = SDViewUtil.getViewMarginLayoutParams(child1);
        if (params1 != null)
        {
            width1All = width1All + params1.leftMargin + params1.rightMargin;
        }

        int widthLeft = widthParentAvalible - width1All;

        MarginLayoutParams params0 = SDViewUtil.getViewMarginLayoutParams(child0);
        if (params0 != null)
        {
            widthLeft = widthLeft - params0.leftMargin - params0.rightMargin;
        }

        if (listener != null)
        {
            listener.onResult(width0, width1, widthParent, widthLeft, parent);
        }
    }

    public void startWeightView(long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        SDHandlerManager.getMainHandler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                startWeightView();
            }
        }, delay);
    }

    public interface CalculateWidthListener
    {
        public void onResult(int width0, int width1, int widthParent, int widthLeft, ViewGroup parent);
    }

}
