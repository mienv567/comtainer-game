package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 用com.fanwe.library.view.SDTabImage替代
 * 
 * @author Administrator
 * 
 */
@Deprecated
public class SDTabImage extends SDViewBase<SDTabImage.Config>
{

	public ImageView mIvTitle;

	public SDTabImage(Context context)
	{
		this(context, null);
	}

	public SDTabImage(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setGravity(Gravity.CENTER);
		mIvTitle = new ImageView(getContext());
		addView(mIvTitle, getLayoutParamsWW());
		onNormal();
	}

	@Override
	public void onNormal()
	{
		mIvTitle.setImageResource(mConfig.getmImageNormalResId());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mIvTitle.setImageResource(mConfig.getmImageSelectedResId());
		super.onSelected();
	}

	public static class Config extends SDViewConfig
	{
		private int mImageNormalResId;
		private int mImageSelectedResId;

		@Override
		public Config clone()
		{
			return (Config) super.clone();
		}

		@Override
		public void setDefaultConfig()
		{

		}

		public int getmImageNormalResId()
		{
			return mImageNormalResId;
		}

		public void setmImageNormalResId(int mImageNormalResId)
		{
			this.mImageNormalResId = mImageNormalResId;
		}

		public int getmImageSelectedResId()
		{
			return mImageSelectedResId;
		}

		public void setmImageSelectedResId(int mImageSelectedResId)
		{
			this.mImageSelectedResId = mImageSelectedResId;
		}

	}

}
