package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.library.R.drawable;

public class SDGestureItemView extends ImageView
{

	private boolean mIsHighLight = false;
	private Rect mRect = new Rect();

	public SDGestureItemView(Context context)
	{
		this(context, null);
	}

	public SDGestureItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
	}

	public boolean isTouchedView(int x, int y)
	{
		Rect rect = getViewsRect();
		return rect.contains(x, y);
	}

	public Rect getViewsRect()
	{
		return mRect;
	}

	public int getMiddleX()
	{
		Rect rect = getViewsRect();
		return (rect.left + rect.right) / 2;
	}

	public int getMiddleY()
	{
		Rect rect = getViewsRect();
		return (rect.top + rect.bottom) / 2;
	}

	public void setHighLightState(boolean highLight)
	{
		// TODO 根据状态改变图片
		if (highLight)
		{
			if (!mIsHighLight)
			{
				this.setImageResource(drawable.ic_gesture_node_highlighted);
			}
		} else
		{
			if (mIsHighLight)
			{
				this.setImageResource(drawable.ic_gesture_node_normal);
			}

		}
		mIsHighLight = highLight;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		int[] location = new int[2];
		getLocationInWindow(location);
		mRect.top = location[1];
		mRect.left = location[0];
		mRect.bottom = location[1] + getHeight();
		mRect.right = location[0] + getWidth();
		super.onLayout(changed, left, top, right, bottom);
	}
}
