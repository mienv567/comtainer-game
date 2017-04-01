package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.qy.ysys.yishengyishi.utils.UiUtils;

/**
 * Created by tony.chen on 2017/1/6.
 */

public class SlidingButton extends RelativeLayout
{
    private static final long DUR_MAX = 200;

    private View viewNormal;
    private View viewSelected;
    private View viewHandle;

    private GestureDetector gestureDetector;
    private ScrollGestureHelper scrollHelper;

    private MarginLayoutParams marginParams;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;

    private boolean onUpVelocityFling;

    private boolean selected;
    private boolean runtimeSelected;
    private SelectedChangeListener selectedChangeListener;

    public void setSelectedChangeListener(SelectedChangeListener selectedChangeListener)
    {
        this.selectedChangeListener = selectedChangeListener;
    }

    public void setOnUpVelocityFling(boolean onUpVelocityFling)
    {
        this.onUpVelocityFling = onUpVelocityFling;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        setSelected(selected, true);
    }

    public void setSelected(boolean selected, boolean smooth)
    {
        this.runtimeSelected = selected;

        if (marginParams != null)
        {
            if (selected)
            {
                scrollToRight(smooth);
            } else
            {
                scrollToLeft(smooth);
            }
        }
    }

    public SlidingButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public SlidingButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SlidingButton(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        gestureDetector = new GestureDetector(getContext(), onGestureListener);
        scrollHelper = new ScrollGestureHelper(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            setViewNormal(getChildAt(0));
            setViewSelected(getChildAt(1));
            setViewHandle(getChildAt(2));
            setSelected(runtimeSelected, false);
        }
    }

    public void setViewNormal(View viewNormal)
    {
        this.viewNormal = viewNormal;
    }

    public void setViewSelected(View viewSelected)
    {
        this.viewSelected = viewSelected;
    }

    public void setViewHandle(View viewHandle)
    {
        if (this.viewHandle != viewHandle)
        {
            this.viewHandle = viewHandle;
            marginParams = UiUtils.getViewMarginLayoutParams(viewHandle);
            if (marginParams != null)
            {
                marginBottom = marginParams.bottomMargin;
                marginLeft = marginParams.leftMargin;
                marginRight = marginParams.rightMargin;
                marginTop = marginParams.topMargin;
            }
        }
    }

    public int getMaxMargin()
    {
        return getWidth() - viewHandle.getWidth() - marginRight;
    }

    private void updateLeftMargin(int margin)
    {
        marginParams.leftMargin = margin;
        viewHandle.setLayoutParams(marginParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            onActionUp();
        }
        if (result)
        {
            return true;
        } else
        {
            return super.onTouchEvent(event);
        }
    }

    protected void onActionUp()
    {
        if (scrollHelper.isHorizontalScroll())
        {
            if (onUpVelocityFling)
            {
                float velocityX = scrollHelper.getVelocityTracker().getXVelocity();
                float minVelocity = scrollHelper.getViewConfiguration().getScaledMinimumFlingVelocity();
                if (Math.abs(velocityX) > minVelocity * 15)
                {
                    if (velocityX > 0)
                    {
                        scrollToRight(true);
                    } else
                    {
                        scrollToLeft(true);
                    }
                } else
                {
                    dealScrollDirection(true);
                }
            } else
            {
                dealScrollDirection(true);
            }
            scrollHelper.cancel();
        }
    }

    private void dealScrollDirection(boolean smooth)
    {
        if (marginParams.leftMargin < getMaxMargin() / 2)
        {
            // 在左半部
            scrollToLeft(smooth);
        } else
        {
            // 在右半部
            scrollToRight(smooth);
        }
    }

    private void scrollToLeft(boolean smooth)
    {
        runtimeSelected = false;
        if (smooth)
        {
            int dur = GestureHelper.getDurationPercent(marginParams.leftMargin, marginLeft, getMaxMargin(), DUR_MAX);
            scrollHelper.getMyScroller().startScrollToX(marginParams.leftMargin, marginLeft, dur);
            postInvalidate();
        } else
        {
            updateLeftMargin(marginLeft);
            updateSelected();
        }
    }

    private void scrollToRight(boolean smooth)
    {
        runtimeSelected = true;
        if (smooth)
        {
            int dur = GestureHelper.getDurationPercent(marginParams.leftMargin, getMaxMargin(), getMaxMargin(), DUR_MAX);
            scrollHelper.getMyScroller().startScrollToX(marginParams.leftMargin, getMaxMargin(), dur);
            postInvalidate();
        } else
        {
            updateLeftMargin(getMaxMargin());
            updateSelected();
        }
    }

    @Override
    public void computeScroll()
    {
        if (scrollHelper.getMyScroller().computeScrollOffset())
        {
            if (scrollHelper.getMyScroller().isFinished())
            {
                updateSelected();
            } else
            {
                updateLeftMargin(scrollHelper.getMyScroller().getCurrX());
            }
        }
        postInvalidate();
    }

    protected void updateSelected()
    {
        if (runtimeSelected != selected)
        {
            selected = runtimeSelected;
            notifyListener();
        }
        updateVisibility();
    }

    private void updateVisibility()
    {
        if (selected)
        {
            UiUtils.show(viewSelected);
            UiUtils.hide(viewNormal);
        } else
        {
            UiUtils.show(viewNormal);
            UiUtils.hide(viewSelected);
        }
    }

    private void notifyListener()
    {
        if (selectedChangeListener != null)
        {
            selectedChangeListener.onSelectedChange(this, selected);
        }
    }

    private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
    {

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            boolean result = false;

            scrollHelper.onScroll(e1, e2, distanceX, distanceY);

            if (scrollHelper.isHorizontalScroll())
            {
                result = true;

                float left = marginParams.leftMargin - distanceX;
                if (left < marginLeft)
                {
                    left = marginLeft;
                } else
                {
                    if (left + viewHandle.getWidth() > getWidth() - marginRight)
                    {
                        left = getWidth() - viewHandle.getWidth() - marginRight;
                    }
                }
                updateLeftMargin((int) left);
            }

            return result;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            return false;
        }

        public boolean onSingleTapUp(MotionEvent e)
        {
            setSelected(!selected);
            return false;
        }

        public boolean onDown(MotionEvent e)
        {
            return true;
        }
    };

    public interface SelectedChangeListener
    {
        void onSelectedChange(SlidingButton view, boolean selected);
    }

}

