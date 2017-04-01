package com.fanwe.library.view;

import java.util.List;

import com.fanwe.library.utils.SDListenerManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SDSizeChangeRelativeLayout extends RelativeLayout
{

	private SDListenerManager<OnSizeChangedListener> observerManager = new SDListenerManager<OnSizeChangedListener>();

	public SDListenerManager<OnSizeChangedListener> getObserverManager()
	{
		return observerManager;
	}

	public SDSizeChangeRelativeLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public SDSizeChangeRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SDSizeChangeRelativeLayout(Context context)
	{
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		List<OnSizeChangedListener> list = observerManager.getListeners();
		for (OnSizeChangedListener item : list)
		{
			item.onSizeChanged(w, h, oldw, oldh, w - oldw, h - oldh, this);
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public interface OnSizeChangedListener
	{
		void onSizeChanged(int w, int h, int oldw, int oldh, int differW, int differH, View view);
	}

}
