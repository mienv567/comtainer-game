package com.fanwe.library.customview;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.fanwe.library.utils.SDResourcesUtil;

public class SDViewBaseConfig implements Cloneable
{

	private int mTextColorNormal;
	private int mTextColorNormalResId;

	private int mTextColorSelected;
	private int mTextColorSelectedResId;

	private int mTextColorPressed;
	private int mTextColorPressedResId;

	private int mImageNormalResId;
	private int mImagePressedResId;
	private int mImageSelectedResId;

	private Drawable mBackgroundNormal;
	private int mBackgroundNormalResId;
	private int mBackgroundColorNormal;
	private int mBackgroundColorNormalResId;

	private Drawable mBackgroundPressed;
	private int mBackgroundPressedResId;
	private int mBackgroundColorPressed;
	private int mBackgroundColorPressedResId;

	private Drawable mBackgroundSelected;
	private int mBackgroundSelectedResId;
	private int mBackgroundColorSelected;
	private int mBackgroundColorSelectedResId;

	@Override
	public SDViewBaseConfig clone()
	{
		try
		{
			return (SDViewBaseConfig) super.clone();
		} catch (Exception e)
		{
			return null;
		}
	}

	// ----------------------setter getter

	public int getmTextColorNormal()
	{
		return mTextColorNormal;
	}

	public void setmTextColorNormal(int textColorNormal)
	{
		this.mTextColorNormal = textColorNormal;
	}

	public int getmTextColorNormalResId()
	{
		return mTextColorNormalResId;
	}

	//
	public void setmTextColorNormalResId(int textColorNormalResId)
	{
		this.mTextColorNormalResId = textColorNormalResId;
		this.mTextColorNormal = SDResourcesUtil.getColor(textColorNormalResId);
	}

	public int getmTextColorSelected()
	{
		return mTextColorSelected;
	}

	public void setmTextColorSelected(int textColorSelected)
	{
		this.mTextColorSelected = textColorSelected;
	}

	public int getmTextColorSelectedResId()
	{
		return mTextColorSelectedResId;
	}

	//
	public void setmTextColorSelectedResId(int textColorSelectedResId)
	{
		this.mTextColorSelectedResId = textColorSelectedResId;
		this.mTextColorSelected = SDResourcesUtil.getColor(textColorSelectedResId);
	}

	public int getmTextColorPressed()
	{
		return mTextColorPressed;
	}

	public void setmTextColorPressed(int textColorPressed)
	{
		this.mTextColorPressed = textColorPressed;
	}

	public int getmTextColorPressedResId()
	{
		return mTextColorPressedResId;
	}

	//
	public void setmTextColorPressedResId(int textColorPressedResId)
	{
		this.mTextColorPressedResId = textColorPressedResId;
		this.mTextColorPressed = SDResourcesUtil.getColor(textColorPressedResId);
	}

	public int getmImageNormalResId()
	{
		return mImageNormalResId;
	}

	public void setmImageNormalResId(int imageNormalResId)
	{
		this.mImageNormalResId = imageNormalResId;
	}

	public int getmImagePressedResId()
	{
		return mImagePressedResId;
	}

	public void setmImagePressedResId(int imagePressedResId)
	{
		this.mImagePressedResId = imagePressedResId;
	}

	public int getmImageSelectedResId()
	{
		return mImageSelectedResId;
	}

	public void setmImageSelectedResId(int imageSelectedResId)
	{
		this.mImageSelectedResId = imageSelectedResId;
	}

	public Drawable getmBackgroundNormal()
	{
		return mBackgroundNormal;
	}

	public void setmBackgroundNormal(Drawable backgroundNormal)
	{
		this.mBackgroundNormal = backgroundNormal;
	}

	public int getmBackgroundNormalResId()
	{
		return mBackgroundNormalResId;
	}

	//
	public void setmBackgroundNormalResId(int backgroundNormalResId)
	{
		this.mBackgroundNormalResId = backgroundNormalResId;
		this.mBackgroundNormal = SDResourcesUtil.getDrawable(backgroundNormalResId);
	}

	public int getmBackgroundColorNormal()
	{
		return mBackgroundColorNormal;
	}

	//
	public void setmBackgroundColorNormal(int backgroundColorNormal)
	{
		this.mBackgroundColorNormal = backgroundColorNormal;
		this.mBackgroundNormal = new ColorDrawable(backgroundColorNormal);
	}

	public int getmBackgroundColorNormalResId()
	{
		return mBackgroundColorNormalResId;
	}

	//
	public void setmBackgroundColorNormalResId(int backgroundColorNormalResId)
	{
		this.mBackgroundColorNormalResId = backgroundColorNormalResId;
		this.mBackgroundNormal = new ColorDrawable(SDResourcesUtil.getColor(backgroundColorNormalResId));
	}

	public Drawable getmBackgroundPressed()
	{
		return mBackgroundPressed;
	}

	public void setmBackgroundPressed(Drawable backgroundPressed)
	{
		this.mBackgroundPressed = backgroundPressed;
	}

	public int getmBackgroundPressedResId()
	{
		return mBackgroundPressedResId;
	}

	//
	public void setmBackgroundPressedResId(int backgroundPressedResId)
	{
		this.mBackgroundPressedResId = backgroundPressedResId;
		this.mBackgroundPressed = SDResourcesUtil.getDrawable(backgroundPressedResId);
	}

	public int getmBackgroundColorPressed()
	{
		return mBackgroundColorPressed;
	}

	//
	public void setmBackgroundColorPressed(int backgroundColorPressed)
	{
		this.mBackgroundColorPressed = backgroundColorPressed;
		this.mBackgroundPressed = new ColorDrawable(backgroundColorPressed);
	}

	public int getmBackgroundColorPressedResId()
	{
		return mBackgroundColorPressedResId;
	}

	//
	public void setmBackgroundColorPressedResId(int backgroundColorPressedResId)
	{
		this.mBackgroundColorPressedResId = backgroundColorPressedResId;
		this.mBackgroundPressed = new ColorDrawable(SDResourcesUtil.getColor(backgroundColorPressedResId));
	}

	public Drawable getmBackgroundSelected()
	{
		return mBackgroundSelected;
	}

	public void setmBackgroundSelected(Drawable backgroundSelected)
	{
		this.mBackgroundSelected = backgroundSelected;
	}

	public int getmBackgroundSelectedResId()
	{
		return mBackgroundSelectedResId;
	}

	//
	public void setmBackgroundSelectedResId(int backgroundSelectedResId)
	{
		this.mBackgroundSelectedResId = backgroundSelectedResId;
		this.mBackgroundSelected = SDResourcesUtil.getDrawable(backgroundSelectedResId);
	}

	public int getmBackgroundColorSelected()
	{
		return mBackgroundColorSelected;
	}

	//
	public void setmBackgroundColorSelected(int backgroundColorSelected)
	{
		this.mBackgroundColorSelected = backgroundColorSelected;
		this.mBackgroundSelected = new ColorDrawable(backgroundColorSelected);
	}

	public int getmBackgroundColorSelectedResId()
	{
		return mBackgroundColorSelectedResId;
	}

	public void setmBackgroundColorSelectedResId(int backgroundColorSelectedResId)
	{
		this.mBackgroundColorSelectedResId = backgroundColorSelectedResId;
		this.mBackgroundSelected = new ColorDrawable(SDResourcesUtil.getColor(backgroundColorSelectedResId));
	}

}
