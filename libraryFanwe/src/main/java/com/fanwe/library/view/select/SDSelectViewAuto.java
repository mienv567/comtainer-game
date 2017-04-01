package com.fanwe.library.view.select;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;

public abstract class SDSelectViewAuto extends SDSelectView
{

	private HashSet<View> mSetAutoView = new HashSet<View>();

	public SDSelectViewAuto(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SDSelectViewAuto(Context context)
	{
		super(context);
	}

	protected void addAutoView(View... views)
	{
		if (views != null && views.length > 0)
		{
			for (View view : views)
			{
				mSetAutoView.add(view);
			}
		}
	}

	protected void removeAutoView(View... views)
	{
		if (views != null && views.length > 0)
		{
			for (View view : views)
			{
				mSetAutoView.remove(view);
			}
		}
	}

	@Override
	protected void setContentView(int resId)
	{
		mSetAutoView.clear();
		super.setContentView(resId);
	}

	@Override
	protected void setContentView(View view)
	{
		mSetAutoView.clear();
		super.setContentView(view);
	}

	@Override
	protected void setContentView(View view, android.view.ViewGroup.LayoutParams params)
	{
		mSetAutoView.clear();
		super.setContentView(view, params);
	}

	private void normalAutoViews()
	{
		for (View view : mSetAutoView)
		{
			normalView_background(view);
			normalView_alpha(view);
			if (view instanceof TextView)
			{
				TextView tv = (TextView) view;
				normalTextView_textColor(tv);
				normalTextView_textSize(tv);
			} else if (view instanceof ImageView)
			{
				ImageView iv = (ImageView) view;
				normalImageView_image(iv);
			}
		}
	}

	private void selectAutoViews()
	{
		for (View view : mSetAutoView)
		{
			selectView_background(view);
			selectView_alpha(view);
			if (view instanceof TextView)
			{
				TextView tv = (TextView) view;
				selectTextView_textColor(tv);
				selectTextView_textSize(tv);
			} else if (view instanceof ImageView)
			{
				ImageView iv = (ImageView) view;
				selectImageView_image(iv);
			}
		}
	}

	@Override
	public void onNormal()
	{
		normalAutoViews();
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		selectAutoViews();
		super.onSelected();
	}

}
