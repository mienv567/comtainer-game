package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SDImageCheckBox extends SDViewBase
{

	public ImageView mIvImage;
	private SDCheckBoxListener mListener;

	public void setmCanToggle(boolean canToggle)
	{
		if (canToggle)
		{
			this.setOnClickListener(mOnclickClickListener);
		} else
		{
			this.setOnClickListener(null);
		}
	}

	public SDImageCheckBox(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	private void initView()
	{
		mIvImage = new ImageView(getContext());
		this.addView(mIvImage, getLayoutParamsWW());
		this.setGravity(Gravity.CENTER);

		setCheckState(false);
		this.setOnClickListener(mOnclickClickListener);
	}

	private View.OnClickListener mOnclickClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			toggleSelected();
			if (mListener != null)
			{
				mListener.onChecked(mSelected);
			}
		}
	};

	public void setCheckState(boolean isChecked)
	{
		if (isChecked)
		{
			onSelected();
		} else
		{
			onNormal();
		}
	}

	@Override
	public void onNormal()
	{
		setImageViewNormal(mIvImage);
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setImageViewSelected(mIvImage);
		super.onSelected();
	}

	// ---------------------setter getter

	public void setmListener(SDCheckBoxListener mListener)
	{
		this.mListener = mListener;
	}

	public interface SDCheckBoxListener
	{
		public void onChecked(boolean isChecked);
	}

}
