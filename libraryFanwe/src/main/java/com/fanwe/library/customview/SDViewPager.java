package com.fanwe.library.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.library.model.Recter;

import java.util.ArrayList;
import java.util.List;

public class SDViewPager extends ViewPager
{

    private MeasureMode measureMode = MeasureMode.NORMAL;
    private List<Recter> listIgnoreRect = new ArrayList<>();
    private boolean isLocked = false;

    public void setMeasureMode(MeasureMode measureMode)
    {
        if (measureMode != null)
        {
            this.measureMode = measureMode;
            requestLayout();
        }
    }

    public SDViewPager(Context context)
    {
        this(context, null);
    }

    public SDViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private boolean isTouchIgnoreRect(MotionEvent ev)
    {
        for (Recter rect : listIgnoreRect)
        {
            if (rect.getRect().contains((int) ev.getRawX(), (int) ev.getRawY()))
            {
                return true;
            }
        }
        return false;
    }

    public void addIgnoreRecter(Recter recter)
    {
        if (recter != null)
        {
            if (!listIgnoreRect.contains(recter))
            {
                listIgnoreRect.add(recter);
            }
        }
    }

    public void removeIgnoreRecter(Recter recter)
    {
        if (recter != null)
        {
            listIgnoreRect.remove(recter);
        }
    }

    public void clearIgnoreRect()
    {
        listIgnoreRect.clear();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (!isLocked)
        {
            try
            {
                if (isTouchIgnoreRect(ev))
                {
                    return false;
                } else
                {
                    return super.onInterceptTouchEvent(ev);
                }
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isLocked)
        {
            if (isTouchIgnoreRect(event))
            {
                return false;
            } else
            {
                return super.onTouchEvent(event);
            }
        }
        return false;
    }

    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }

    public boolean isLocked()
    {
        return isLocked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        switch (measureMode)
        {
            case NORMAL:
                measureNormal(widthMeasureSpec, heightMeasureSpec);
                break;
            case FIRST_CHILD:
                measureByFirstHeight(widthMeasureSpec, heightMeasureSpec);
                break;
            case MAX_CHILD:
                measureByMaxHeight(widthMeasureSpec, heightMeasureSpec);
                break;
            default:
                measureNormal(widthMeasureSpec, heightMeasureSpec);
                break;
        }
    }

    protected void measureNormal(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void measureByMaxHeight(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
            {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void measureByFirstHeight(int widthMeasureSpec, int heightMeasureSpec)
    {
        View view = getChildAt(0);
        if (view != null)
        {
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    protected int measureHeight(int measureSpec, View view)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        } else
        {
            if (view != null)
            {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public enum MeasureMode
    {
        /**
         * 正常模式
         */
        NORMAL,
        /**
         * 按最大页面的高度来设置高度
         */
        MAX_CHILD,
        /**
         * 按第一个页面的高度来设置高度
         */
        FIRST_CHILD;
    }

}
