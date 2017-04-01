package com.fanwe.library.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fanwe.library.model.Recter;

/**
 * Found at http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager.
 * Convenient way to temporarily disable ViewPager navigation while interacting with ImageView.
 * 
 * Julia Zudikova
 */

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 * 
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager
{

	private List<Recter> listIgnoreRect = new ArrayList<Recter>();
	private boolean isLocked;

	public HackyViewPager(Context context)
	{
		super(context);
		isLocked = false;
	}

	public HackyViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		isLocked = false;
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

	public void toggleLock()
	{
		isLocked = !isLocked;
	}

	public void setLocked(boolean isLocked)
	{
		this.isLocked = isLocked;
	}

	public boolean isLocked()
	{
		return isLocked;
	}

}
