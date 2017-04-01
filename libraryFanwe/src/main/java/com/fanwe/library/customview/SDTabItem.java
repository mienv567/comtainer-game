package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

@Deprecated
public class SDTabItem extends SDViewBase
{

	public TextView mTvTitle = null;

	private int mWidth = 0;
	private int mHeight = 0;

	public int getmWidth()
	{
		return mWidth;
	}

	public void setmWidth(int mWidth)
	{
		this.mWidth = mWidth;
		if (mWidth > 0)
		{
			ViewGroup.LayoutParams params = mTvTitle.getLayoutParams();
			params.width = mWidth;
			mTvTitle.setLayoutParams(params);
		}
	}

	public int getmHeight()
	{
		return mHeight;

	}

	public void setmHeight(int mHeight)
	{
		this.mHeight = mHeight;
		if (mHeight > 0)
		{
			ViewGroup.LayoutParams params = mTvTitle.getLayoutParams();
			params.height = mHeight;
			mTvTitle.setLayoutParams(params);
		}
	}

	public SDTabItem(Context context)
	{
		super(context);
		init();
	}

	public SDTabItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mTvTitle = new TextView(getContext());
		mTvTitle.setGravity(Gravity.CENTER);

		this.addView(mTvTitle, getLayoutParamsWW());
		this.setGravity(Gravity.CENTER);

		onNormal();
	}

	public void setTabName(String name)
	{
		if (name != null)
		{
			mTvTitle.setText(name);
		}
	}

	public void setTabTextSizeSp(float textSize)
	{
		setTextSizeSp(mTvTitle, textSize);
	}

	@Override
	public void onNormal()
	{
		setTextColorNormal(mTvTitle);
		setViewBackgroundNormal(this);
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setTextColorSelected(mTvTitle);
		setViewBackgroundSelected(this);
		super.onSelected();
	}

}
