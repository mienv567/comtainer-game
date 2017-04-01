package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
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
public class SDTabCorner extends SDViewBase
{

	public TextView mTvTitle;
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

	public SDTabCorner(Context context)
	{
		super(context);
		init();
	}

	public SDTabCorner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setContentView(R.layout.view_tab_corner);
		mTvTitle = (TextView) findViewById(R.id.tv_title);

		setGravity(Gravity.CENTER);

		setDefaultConfig();
		onNormal();
	}

	public void setDefaultConfig()
	{
		getViewConfig(mTvTitle).setmTextColorNormal(mLibraryConfig.getMainColor());
		getViewConfig(mTvTitle).setmTextColorSelected(Color.WHITE);
		getViewConfig(this).setmBackgroundNormal(createDefaultBackgroundNormal());
		getViewConfig(this).setmBackgroundSelected(createDefaultBackgroundSelected());

		resetBackgroudDrawableByPosition();
	}

	public void reverseDefaultConfig()
	{
		getViewConfig(mTvTitle).setmTextColorNormal(Color.WHITE);
		getViewConfig(mTvTitle).setmTextColorSelected(mLibraryConfig.getMainColor());
		getViewConfig(this).setmBackgroundNormal(createDefaultBackgroundNormalReverse());
		getViewConfig(this).setmBackgroundSelected(createDefaultBackgroundSelectedReverse());

		resetBackgroudDrawableByPosition();
	}

	public void resetBackgroudDrawableByPosition()
	{
		Drawable drawableN = getViewConfig(this).getmBackgroundNormal();
		Drawable drawableS = getViewConfig(this).getmBackgroundSelected();
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
		mTvTitle.setTextColor(getViewConfig(mTvTitle).getmTextColorNormal());
		SDViewUtil.setBackgroundDrawable(this, getViewConfig(this).getmBackgroundNormal());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mTvTitle.setTextColor(getViewConfig(mTvTitle).getmTextColorSelected());
		SDViewUtil.setBackgroundDrawable(this, getViewConfig(this).getmBackgroundSelected());
		super.onSelected();
	}

	private SDDrawable createDefaultBackgroundNormal()
	{
		SDDrawable drawableNormal = new SDDrawable();
		drawableNormal.color(SDResourcesUtil.getColor(R.color.white));
		drawableNormal.strokeColor(mLibraryConfig.getMainColor());
		drawableNormal.strokeWidthAll(mLibraryConfig.getStrokeWidth());
		drawableNormal.cornerAll(mLibraryConfig.getCornerRadius());
		return drawableNormal;
	}

	private SDDrawable createDefaultBackgroundSelected()
	{
		SDDrawable drawableSelected = new SDDrawable();
		drawableSelected.color(mLibraryConfig.getMainColor());
		drawableSelected.strokeColor(mLibraryConfig.getMainColor());
		drawableSelected.strokeWidthAll(mLibraryConfig.getStrokeWidth());
		drawableSelected.cornerAll(mLibraryConfig.getCornerRadius());
		return drawableSelected;
	}

	private SDDrawable createDefaultBackgroundNormalReverse()
	{
		SDDrawable drawableNormal = new SDDrawable();
		drawableNormal.color(mLibraryConfig.getMainColor());
		drawableNormal.strokeColor(SDResourcesUtil.getColor(R.color.white));
		drawableNormal.strokeWidthAll(mLibraryConfig.getStrokeWidth());
		drawableNormal.cornerAll(mLibraryConfig.getCornerRadius());
		return drawableNormal;
	}

	private SDDrawable createDefaultBackgroundSelectedReverse()
	{
		SDDrawable drawableSelected = new SDDrawable();
		drawableSelected.color(SDResourcesUtil.getColor(R.color.white));
		drawableSelected.strokeColor(SDResourcesUtil.getColor(R.color.white));
		drawableSelected.strokeWidthAll(mLibraryConfig.getStrokeWidth());
		drawableSelected.cornerAll(mLibraryConfig.getCornerRadius());
		return drawableSelected;
	}

	public enum EnumTabPosition
	{
		FIRST, MIDDLE, LAST, SINGLE;
	}

}
