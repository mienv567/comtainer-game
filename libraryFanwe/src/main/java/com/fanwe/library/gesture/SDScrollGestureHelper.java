package com.fanwe.library.gesture;

import android.content.Context;
import android.view.MotionEvent;

import com.fanwe.library.gesture.SDGestureHelper;

public class SDScrollGestureHelper extends SDGestureHelper
{

	private SDScroller scroller;
	private boolean isVerticalScroll = false;
	private boolean isHorizontalScroll = false;

	public SDScrollGestureHelper(Context context)
	{
		super(context);
		scroller = new SDScroller(context);
	}

	public SDScroller getScroller()
	{
		return scroller;
	}

	private void setHorizontalScroll(boolean isHorizontalScroll)
	{
		this.isHorizontalScroll = isHorizontalScroll;
		if (isHorizontalScroll)
		{
			isVerticalScroll = false;
		}
	}

	private void setVerticalScroll(boolean isVerticalScroll)
	{
		this.isVerticalScroll = isVerticalScroll;
		if (isVerticalScroll)
		{
			isHorizontalScroll = false;
		}
	}

	public void cancel()
	{
		isHorizontalScroll = false;
		isHorizontalScroll = false;
		recyleVelocityTracker();
	}

	public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		getVelocityTracker().addMovement(e2);
		getVelocityTracker().computeCurrentVelocity(1000);

		if (isHorizontalScroll || isVerticalScroll)
		{
			return;
		} else
		{
			float dx = Math.abs(e1.getRawX() - e2.getRawX());
			float dy = Math.abs(e1.getRawY() - e2.getRawY());

			if (dx > dy)
			{
				// 水平方向
				if (dx > getViewConfiguration().getScaledTouchSlop())
				{
					setHorizontalScroll(true);
				}
			} else
			{
				// 竖直方向
				if (dy > getViewConfiguration().getScaledTouchSlop())
				{
					setVerticalScroll(true);
				}
			}
		}
	}

	public boolean isHorizontalScroll()
	{
		return isHorizontalScroll;
	}

	public boolean isVerticalScroll()
	{
		return isVerticalScroll;
	}
}
