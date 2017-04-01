package com.fanwe.library.customview;

import android.graphics.drawable.Drawable;

import com.fanwe.library.utils.SDResourcesUtil;

public class SDViewAttr implements Cloneable
{

	private int mTextColorNormal;
	private int mTextColorNormalResId;

	private int mTextColorSelected;
	private int mTextColorSelectedResId;

	private int mTextColorPressed;
	private int mTextColorPressedResId;

	private Drawable mBackgroundDrawableNormal;
	private int mBackgroundDrawableNormalResId;
	private int mBackgroundColorNormalResId;
	private int mBackgroundColorNormal;

	private Drawable mBackgroundDrawablePressed;
	private int mBackgroundDrawablePressedResId;
	private int mBackgroundColorPressedResId;
	private int mBackgroundColorPressed;

	private Drawable mBackgroundDrawableSelected;
	private int mBackgroundDrawableSelectedResId;
	private int mBackgroundColorSelectedResId;
	private int mBackgroundColorSelected;

	private int mImageNormalResId;
	private int mImagePressedResId;
	private int mImageSelectedResId;

	private int mStrokeColor;
	private int mStrokeColorResId;

	private int mStrokeWidth;
	private int mStrokeWidthResId;

	private int mCornerRadius;
	private int mCornerRadiusResId;

	// ----------------------setter getter

	public int getmTextColorNormal()
	{
		return mTextColorNormal;
	}

	public int getmStrokeWidthResId()
	{
		return mStrokeWidthResId;
	}

	// 需要转换
	public void setmStrokeWidthResId(int mStrokeWidthResId)
	{
		this.mStrokeWidthResId = mStrokeWidthResId;
		setmStrokeWidth(SDResourcesUtil.getDimensionPixelSize(mStrokeWidthResId));
	}

	public int getmCornerRadiusResId()
	{
		return mCornerRadiusResId;
	}

	// 需要转换
	public void setmCornerRadiusResId(int mCornerRadiusResId)
	{
		this.mCornerRadiusResId = mCornerRadiusResId;
		setmCornerRadius(SDResourcesUtil.getDimensionPixelSize(mCornerRadiusResId));
	}

	public int getmBackgroundColorNormalResId()
	{
		return mBackgroundColorNormalResId;
	}

	// 需要转换
	public void setmBackgroundColorNormalResId(int mBackgroundColorNormalResId)
	{
		this.mBackgroundColorNormalResId = mBackgroundColorNormalResId;
		setmBackgroundColorNormal(SDResourcesUtil.getColor(mBackgroundColorNormalResId));
	}

	public int getmBackgroundColorNormal()
	{
		return mBackgroundColorNormal;
	}

	public void setmBackgroundColorNormal(int mBackgroundColorNormal)
	{
		this.mBackgroundColorNormal = mBackgroundColorNormal;
	}

	public int getmBackgroundColorPressedResId()
	{
		return mBackgroundColorPressedResId;
	}

	// 需要转换
	public void setmBackgroundColorPressedResId(int mBackgroundColorPressedResId)
	{
		this.mBackgroundColorPressedResId = mBackgroundColorPressedResId;
		setmBackgroundColorPressed(SDResourcesUtil.getColor(mBackgroundColorPressedResId));
	}

	public int getmBackgroundColorPressed()
	{
		return mBackgroundColorPressed;
	}

	public void setmBackgroundColorPressed(int mBackgroundColorPressed)
	{
		this.mBackgroundColorPressed = mBackgroundColorPressed;
	}

	public int getmBackgroundColorSelectedResId()
	{
		return mBackgroundColorSelectedResId;
	}

	// 需要转换
	public void setmBackgroundColorSelectedResId(int mBackgroundColorSelectedResId)
	{
		this.mBackgroundColorSelectedResId = mBackgroundColorSelectedResId;
		setmBackgroundColorSelected(SDResourcesUtil.getColor(mBackgroundColorSelectedResId));
	}

	public int getmBackgroundColorSelected()
	{
		return mBackgroundColorSelected;
	}

	public void setmBackgroundColorSelected(int mBackgroundColorSelected)
	{
		this.mBackgroundColorSelected = mBackgroundColorSelected;
	}

	public int getmCornerRadius()
	{
		return mCornerRadius;
	}

	public void setmCornerRadius(int mCornerRadius)
	{
		this.mCornerRadius = mCornerRadius;
	}

	public int getmImageNormalResId()
	{
		return mImageNormalResId;
	}

	public void setmImageNormalResId(int mImageNormalResId)
	{
		this.mImageNormalResId = mImageNormalResId;
	}

	public int getmImagePressedResId()
	{
		return mImagePressedResId;
	}

	public void setmImagePressedResId(int mImagePressedResId)
	{
		this.mImagePressedResId = mImagePressedResId;
	}

	public int getmImageSelectedResId()
	{
		return mImageSelectedResId;
	}

	public void setmImageSelectedResId(int mImageSelectedResId)
	{
		this.mImageSelectedResId = mImageSelectedResId;
	}

	public int getmTextColorPressed()
	{
		return mTextColorPressed;
	}

	public void setmTextColorPressed(int mTextColorPressed)
	{
		this.mTextColorPressed = mTextColorPressed;
	}

	public int getmTextColorPressedResId()
	{
		return mTextColorPressedResId;
	}

	// 需要转换
	public void setmTextColorPressedResId(int mTextColorPressedResId)
	{
		this.mTextColorPressedResId = mTextColorPressedResId;
		setmTextColorPressed(SDResourcesUtil.getColor(mTextColorPressedResId));
	}

	public void setmTextColorNormal(int mTextColorNormal)
	{
		this.mTextColorNormal = mTextColorNormal;
	}

	public int getmTextColorSelected()
	{
		return mTextColorSelected;
	}

	public void setmTextColorSelected(int mTextColorSelected)
	{
		this.mTextColorSelected = mTextColorSelected;
	}

	public int getmTextColorNormalResId()
	{
		return mTextColorNormalResId;
	}

	// 需要转换
	public void setmTextColorNormalResId(int mTextColorNormalResId)
	{
		this.mTextColorNormalResId = mTextColorNormalResId;
		setmTextColorNormal(SDResourcesUtil.getColor(mTextColorNormalResId));
	}

	public int getmTextColorSelectedResId()
	{
		return mTextColorSelectedResId;
	}

	// 需要转换
	public void setmTextColorSelectedResId(int mTextColorSelectedResId)
	{
		this.mTextColorSelectedResId = mTextColorSelectedResId;
		setmTextColorSelected(SDResourcesUtil.getColor(mTextColorSelectedResId));
	}

	public Drawable getmBackgroundDrawableNormal()
	{
		return mBackgroundDrawableNormal;
	}

	public void setmBackgroundDrawableNormal(Drawable mBackgroundDrawableNormal)
	{
		this.mBackgroundDrawableNormal = mBackgroundDrawableNormal;
	}

	public Drawable getmBackgroundDrawablePressed()
	{
		return mBackgroundDrawablePressed;
	}

	public void setmBackgroundDrawablePressed(Drawable mBackgroundDrawablePressed)
	{
		this.mBackgroundDrawablePressed = mBackgroundDrawablePressed;
	}

	public Drawable getmBackgroundDrawableSelected()
	{
		return mBackgroundDrawableSelected;
	}

	public void setmBackgroundDrawableSelected(Drawable mBackgroundDrawableSelected)
	{
		this.mBackgroundDrawableSelected = mBackgroundDrawableSelected;
	}

	public int getmBackgroundDrawableNormalResId()
	{
		return mBackgroundDrawableNormalResId;
	}

	public void setmBackgroundDrawableNormalResId(int mBackgroundDrawableNormalResId)
	{
		this.mBackgroundDrawableNormalResId = mBackgroundDrawableNormalResId;
	}

	public int getmBackgroundDrawablePressedResId()
	{
		return mBackgroundDrawablePressedResId;
	}

	public void setmBackgroundDrawablePressedResId(int mBackgroundDrawablePressedResId)
	{
		this.mBackgroundDrawablePressedResId = mBackgroundDrawablePressedResId;
	}

	public int getmBackgroundDrawableSelectedResId()
	{
		return mBackgroundDrawableSelectedResId;
	}

	public void setmBackgroundDrawableSelectedResId(int mBackgroundDrawableSelectedResId)
	{
		this.mBackgroundDrawableSelectedResId = mBackgroundDrawableSelectedResId;
	}

	public int getmStrokeColor()
	{
		return mStrokeColor;
	}

	public void setmStrokeColor(int mStrokeColor)
	{
		this.mStrokeColor = mStrokeColor;
	}

	public int getmStrokeColorResId()
	{
		return mStrokeColorResId;
	}

	// 需要转换
	public void setmStrokeColorResId(int mStrokeColorResId)
	{
		this.mStrokeColorResId = mStrokeColorResId;
		setmStrokeColor(SDResourcesUtil.getColor(mStrokeColorResId));
	}

	public int getmStrokeWidth()
	{
		return mStrokeWidth;
	}

	public void setmStrokeWidth(int mStrokeWidth)
	{
		this.mStrokeWidth = mStrokeWidth;
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		} catch (Exception e)
		{
			return null;
		}
	}

}
