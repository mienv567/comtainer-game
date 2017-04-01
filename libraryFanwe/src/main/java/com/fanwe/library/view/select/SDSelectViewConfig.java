package com.fanwe.library.view.select;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.fanwe.library.utils.SDResourcesUtil;

public class SDSelectViewConfig implements Cloneable
{

	public static final int EMPTY_VALUE = -Integer.MAX_VALUE;

	//
	private int mTextColorNormal = EMPTY_VALUE;
	private int mTextColorSelected = EMPTY_VALUE;

	private int mTextColorNormalResId;
	private int mTextColorSelectedResId;

	//
	private int mTextSizeNormal = EMPTY_VALUE;
	private int mTextSizeSelected = EMPTY_VALUE;

	//
	private int mImageNormalResId = EMPTY_VALUE;
	private int mImageSelectedResId = EMPTY_VALUE;

	//
	private float mAlphaNormal = EMPTY_VALUE;
	private float mAlphaSelected = EMPTY_VALUE;

	//
	private Drawable mBackgroundNormal;
	private Drawable mBackgroundSelected;

	private int mBackgroundNormalResId;
	private int mBackgroundSelectedResId;

	private int mBackgroundColorNormal;
	private int mBackgroundColorSelected;

	private int mBackgroundColorNormalResId;
	private int mBackgroundColorSelectedResId;

	@Override
	public SDSelectViewConfig clone()
	{
		try
		{
			return (SDSelectViewConfig) super.clone();
		} catch (Exception e)
		{
			return null;
		}
	}

	// ----------------------setter getter

	public int getTextColorNormal()
	{
		return mTextColorNormal;
	}

	public float getAlphaNormal()
	{
		return mAlphaNormal;
	}

	public SDSelectViewConfig setAlphaNormal(float alphaNormal)
	{
		this.mAlphaNormal = alphaNormal;
		return this;
	}

	public float getAlphaSelected()
	{
		return mAlphaSelected;
	}

	public SDSelectViewConfig setAlphaSelected(float alphaSelected)
	{
		this.mAlphaSelected = alphaSelected;
		return this;
	}

	public int getTextSizeNormal()
	{
		return mTextSizeNormal;
	}

	public SDSelectViewConfig setTextSizeNormal(int textSizeNormal)
	{
		this.mTextSizeNormal = textSizeNormal;
		return this;
	}

	public int getTextSizeSelected()
	{
		return mTextSizeSelected;
	}

	public SDSelectViewConfig setTextSizeSelected(int textSizeSelected)
	{
		this.mTextSizeSelected = textSizeSelected;
		return this;
	}

	public SDSelectViewConfig setTextColorNormal(int textColorNormal)
	{
		this.mTextColorNormal = textColorNormal;
		return this;
	}

	public int getmTextColorNormalResId()
	{
		return mTextColorNormalResId;
	}

	//
	public SDSelectViewConfig setTextColorNormalResId(int textColorNormalResId)
	{
		this.mTextColorNormalResId = textColorNormalResId;
		this.mTextColorNormal = SDResourcesUtil.getColor(textColorNormalResId);
		return this;
	}

	public int getTextColorSelected()
	{
		return mTextColorSelected;
	}

	public SDSelectViewConfig setTextColorSelected(int textColorSelected)
	{
		this.mTextColorSelected = textColorSelected;
		return this;
	}

	public int getTextColorSelectedResId()
	{
		return mTextColorSelectedResId;
	}

	//
	public SDSelectViewConfig setTextColorSelectedResId(int textColorSelectedResId)
	{
		this.mTextColorSelectedResId = textColorSelectedResId;
		this.mTextColorSelected = SDResourcesUtil.getColor(textColorSelectedResId);
		return this;
	}

	public int getImageNormalResId()
	{
		return mImageNormalResId;
	}

	public SDSelectViewConfig setImageNormalResId(int imageNormalResId)
	{
		this.mImageNormalResId = imageNormalResId;
		return this;
	}

	public int getImageSelectedResId()
	{
		return mImageSelectedResId;
	}

	public SDSelectViewConfig setImageSelectedResId(int imageSelectedResId)
	{
		this.mImageSelectedResId = imageSelectedResId;
		return this;
	}

	public Drawable getBackgroundNormal()
	{
		return mBackgroundNormal;
	}

	public SDSelectViewConfig setBackgroundNormal(Drawable backgroundNormal)
	{
		this.mBackgroundNormal = backgroundNormal;
		return this;
	}

	public int getBackgroundNormalResId()
	{
		return mBackgroundNormalResId;
	}

	//
	public SDSelectViewConfig setBackgroundNormalResId(int backgroundNormalResId)
	{
		this.mBackgroundNormalResId = backgroundNormalResId;
		this.mBackgroundNormal = SDResourcesUtil.getDrawable(backgroundNormalResId);
		return this;
	}

	public int getBackgroundColorNormal()
	{
		return mBackgroundColorNormal;
	}

	//
	public SDSelectViewConfig setBackgroundColorNormal(int backgroundColorNormal)
	{
		this.mBackgroundColorNormal = backgroundColorNormal;
		this.mBackgroundNormal = new ColorDrawable(backgroundColorNormal);
		return this;
	}

	public int getBackgroundColorNormalResId()
	{
		return mBackgroundColorNormalResId;
	}

	//
	public SDSelectViewConfig setBackgroundColorNormalResId(int backgroundColorNormalResId)
	{
		this.mBackgroundColorNormalResId = backgroundColorNormalResId;
		this.mBackgroundNormal = new ColorDrawable(SDResourcesUtil.getColor(backgroundColorNormalResId));
		return this;
	}

	public Drawable getBackgroundSelected()
	{
		return mBackgroundSelected;
	}

	public SDSelectViewConfig setBackgroundSelected(Drawable backgroundSelected)
	{
		this.mBackgroundSelected = backgroundSelected;
		return this;
	}

	public int getBackgroundSelectedResId()
	{
		return mBackgroundSelectedResId;
	}

	//
	public SDSelectViewConfig setBackgroundSelectedResId(int backgroundSelectedResId)
	{
		this.mBackgroundSelectedResId = backgroundSelectedResId;
		this.mBackgroundSelected = SDResourcesUtil.getDrawable(backgroundSelectedResId);
		return this;
	}

	public int getBackgroundColorSelected()
	{
		return mBackgroundColorSelected;
	}

	//
	public SDSelectViewConfig setBackgroundColorSelected(int backgroundColorSelected)
	{
		this.mBackgroundColorSelected = backgroundColorSelected;
		this.mBackgroundSelected = new ColorDrawable(backgroundColorSelected);
		return this;
	}

	public int getBackgroundColorSelectedResId()
	{
		return mBackgroundColorSelectedResId;
	}

	public SDSelectViewConfig setBackgroundColorSelectedResId(int backgroundColorSelectedResId)
	{
		this.mBackgroundColorSelectedResId = backgroundColorSelectedResId;
		this.mBackgroundSelected = new ColorDrawable(SDResourcesUtil.getColor(backgroundColorSelectedResId));
		return this;
	}

}
