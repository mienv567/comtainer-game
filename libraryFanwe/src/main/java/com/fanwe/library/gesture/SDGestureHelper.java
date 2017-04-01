package com.fanwe.library.gesture;

import android.content.Context;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class SDGestureHelper
{
	protected Context context;
	private ViewConfiguration viewConfiguration;
	private VelocityTracker velocityTracker;

	public SDGestureHelper(Context context)
	{
		this.context = context;
		this.viewConfiguration = ViewConfiguration.get(context);
	}

	public static int getDurationPercent(float start, float end, float maxDistance, long maxDuration)
	{
		int result = 0;

		float distance = Math.abs(end - start);
		float percent = distance / Math.abs(maxDistance);
		float duration = percent * (float) maxDuration;

		result = (int) duration;
		return result;
	}

	public VelocityTracker getVelocityTracker()
	{
		if (velocityTracker == null)
		{
			velocityTracker = VelocityTracker.obtain();
		}
		return velocityTracker;
	}

	public void recyleVelocityTracker()
	{
		if (velocityTracker != null)
		{
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	public ViewConfiguration getViewConfiguration()
	{
		return viewConfiguration;
	}

	public Context getContext()
	{
		return context;
	}

}
