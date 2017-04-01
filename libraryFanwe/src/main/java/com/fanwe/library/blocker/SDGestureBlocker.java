package com.fanwe.library.blocker;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public abstract class SDGestureBlocker implements OnGestureListener
{

	public static final long DEFAULT_BLOCK_TIME = 500;

	private GestureDetector gestureDetector;
	protected Context context;
	protected SDBlocker blocker = new SDBlocker();

	public SDGestureBlocker(Context context)
	{
		this(DEFAULT_BLOCK_TIME, context);
	}

	public SDGestureBlocker(long blockTime, Context context)
	{
		this.context = context;
		blocker.setBlockTime(blockTime);
		init();
	}

	private void init()
	{
		gestureDetector = new GestureDetector(context, this);
	}

	public boolean blockEvent(MotionEvent event)
	{
		boolean result = gestureDetector.onTouchEvent(event);
		if (result)
		{
			if (MotionEvent.ACTION_DOWN != event.getAction())
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return true;
	}

}
