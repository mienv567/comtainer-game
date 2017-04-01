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
public class SDTabItemImage extends SDViewBase<SDTabItemImage.Config>
{

	public ImageView mIvTitle = null;

	public SDTabItemImage(Context context)
	{
		this(context, null);
	}

	// -------------------get set

	public SDTabItemImage(Context context, AttributeSet attrs)
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
		mIvTitle.setImageResource(mAttr.getmImageNormalResId());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mIvTitle.setImageResource(mAttr.getmImageSelectedResId());
		super.onSelected();
	}

	public static class Config extends SDViewConfig
	{
		private int mImageNormalResId;
		private int mImageSelectedResId;

		@Override
		public void setDefaultConfig()
		{
			// TODO Auto-generated method stub

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
