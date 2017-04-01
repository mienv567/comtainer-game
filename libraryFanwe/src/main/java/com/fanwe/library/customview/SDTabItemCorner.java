package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 用com.fanwe.library.view.SDTabCornerText替代
 * 
 * @author Administrator
 * 
 */
@Deprecated
public class SDTabItemCorner extends SDViewBase<SDTabItemCorner.Config>
{

	public TextView mTvTitle;
	public TextView mTvNumber;
	private EnumTabPosition mPosition = EnumTabPosition.SINGLE;

	// ------------------get set
	public EnumTabPosition getmPosition()
	{
		return mPosition;
	}

	public void setmPosition(EnumTabPosition mPosition)
	{
		this.mPosition = mPosition;
		resetBackgroudDrawableByPosition();
	}

	public SDTabItemCorner(Context context)
	{
		super(context);
		init();
	}

	public SDTabItemCorner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{

		LayoutInflater.from(getContext()).inflate(R.layout.view_tab_item_corner, this, true);
		setGravity(Gravity.CENTER);
		mTvTitle = (TextView) findViewById(R.id.tv_title);

		setmPosition(EnumTabPosition.SINGLE);
		onNormal();
	}

	public void setDefaultConfig()
	{
		mConfig.setDefaultConfig();
		resetBackgroudDrawableByPosition();
	}

	public void reverseDefaultConfig()
	{
		mConfig.setDefaultConfgReverse();
		resetBackgroudDrawableByPosition();
	}

	public void resetBackgroudDrawableByPosition()
	{
		Drawable drawableN = mConfig.getmBackgroundDrawableNormal();
		Drawable drawableS = mConfig.getmBackgroundDrawableSelected();
		if (!(drawableN instanceof SDDrawable) || !(drawableS instanceof SDDrawable))
		{
			return;
		}

		SDDrawable drawableNormal = (SDDrawable) drawableN;
		SDDrawable drawableSelected = (SDDrawable) drawableS;

		switch (mPosition)
		{
		case FIRST:
			drawableNormal.strokeWidthRight(0).cornerTopRight(0).cornerBottomRight(0);
			drawableSelected.strokeWidthRight(0).cornerTopRight(0).cornerBottomRight(0);
			break;
		case MIDDLE:
			drawableNormal.strokeWidthRight(0).cornerAll(0);
			drawableSelected.strokeWidthRight(0).cornerAll(0);
			break;
		case LAST:
			drawableNormal.cornerTopLeft(0).cornerBottomLeft(0);
			drawableSelected.cornerTopLeft(0).cornerBottomLeft(0);
			break;
		case SINGLE:

			break;

		default:
			break;
		}
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
		mTvTitle.setTextColor(mConfig.getmTextColorTitleNormal());
		SDViewUtil.setBackgroundDrawable(this, mConfig.getmBackgroundDrawableNormal());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mTvTitle.setTextColor(mConfig.getmTextColorTitleSelected());
		SDViewUtil.setBackgroundDrawable(this, mConfig.getmBackgroundDrawableSelected());
		super.onSelected();
	}

	public static class Config extends SDViewConfig
	{

		private int mTextColorTitleNormal;
		private int mTextColorTitleSelected;

		private Drawable mBackgroundDrawableNormal;
		private Drawable mBackgroundDrawableSelected;

		@Override
		public void setDefaultConfig()
		{
			mTextColorTitleNormal = mLibraryConfig.getMainColor();
			mTextColorTitleSelected = SDResourcesUtil.getColor(R.color.white);

			mBackgroundDrawableNormal = createDefaultBackgroundNormal();
			mBackgroundDrawableSelected = createDefaultBackgroundSelected();
		}

		public void setDefaultConfgReverse()
		{
			mTextColorTitleNormal = SDResourcesUtil.getColor(R.color.white);
			mTextColorTitleSelected = mLibraryConfig.getMainColor();

			mBackgroundDrawableNormal = createDefaultBackgroundNormalReverse();
			mBackgroundDrawableSelected = createDefaultBackgroundSelectedReverse();
		}

		public SDDrawable createDefaultBackgroundNormal()
		{
			SDDrawable drawableNormal = new SDDrawable();
			drawableNormal.color(SDResourcesUtil.getColor(R.color.white));
			drawableNormal.strokeColor(mLibraryConfig.getMainColor());
			drawableNormal.strokeWidthAll(mLibraryConfig.getStrokeWidth());
			drawableNormal.cornerAll(mLibraryConfig.getCornerRadius());
			return drawableNormal;
		}

		public SDDrawable createDefaultBackgroundNormalReverse()
		{
			SDDrawable drawableNormal = new SDDrawable();
			drawableNormal.color(mLibraryConfig.getMainColor());
			drawableNormal.strokeColor(SDResourcesUtil.getColor(R.color.white));
			drawableNormal.strokeWidthAll(mLibraryConfig.getStrokeWidth());
			drawableNormal.cornerAll(mLibraryConfig.getCornerRadius());
			return drawableNormal;
		}

		public SDDrawable createDefaultBackgroundSelected()
		{
			SDDrawable drawableSelected = new SDDrawable();
			drawableSelected.color(mLibraryConfig.getMainColor());
			drawableSelected.strokeColor(mLibraryConfig.getMainColor());
			drawableSelected.strokeWidthAll(mLibraryConfig.getStrokeWidth());
			drawableSelected.cornerAll(mLibraryConfig.getCornerRadius());
			return drawableSelected;
		}

		public SDDrawable createDefaultBackgroundSelectedReverse()
		{
			SDDrawable drawableSelected = new SDDrawable();
			drawableSelected.color(SDResourcesUtil.getColor(R.color.white));
			drawableSelected.strokeColor(SDResourcesUtil.getColor(R.color.white));
			drawableSelected.strokeWidthAll(mLibraryConfig.getStrokeWidth());
			drawableSelected.cornerAll(mLibraryConfig.getCornerRadius());
			return drawableSelected;
		}

		public int getmTextColorTitleNormal()
		{
			return mTextColorTitleNormal;
		}

		public void setmTextColorTitleNormal(int mTextColorTitleNormal)
		{
			this.mTextColorTitleNormal = mTextColorTitleNormal;
		}

		public int getmTextColorTitleSelected()
		{
			return mTextColorTitleSelected;
		}

		public void setmTextColorTitleSelected(int mTextColorTitleSelected)
		{
			this.mTextColorTitleSelected = mTextColorTitleSelected;
		}

		public Drawable getmBackgroundDrawableNormal()
		{
			return mBackgroundDrawableNormal;
		}

		public void setmBackgroundDrawableNormal(Drawable mBackgroundDrawableNormal)
		{
			this.mBackgroundDrawableNormal = mBackgroundDrawableNormal;
		}

		public Drawable getmBackgroundDrawableSelected()
		{
			return mBackgroundDrawableSelected;
		}

		public void setmBackgroundDrawableSelected(Drawable mBackgroundDrawableSelected)
		{
			this.mBackgroundDrawableSelected = mBackgroundDrawableSelected;
		}

	}

	public enum EnumTabPosition
	{
		FIRST, MIDDLE, LAST, SINGLE;
	}

}
