package com.fanwe.library.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SDTabCorner extends SDSelectViewAuto
{

	private TabPosition mPosition = TabPosition.SINGLE;

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

	@Override
	protected void init()
	{
		addAutoView(this);
		setDefaultConfig();
		onNormal();
		super.init();
	}

	public TabPosition getPosition()
	{
		return mPosition;
	}

	public void setPosition(TabPosition position)
	{
		if (position != null)
		{
			this.mPosition = position;
			resetBackgroudDrawableByPosition();
		}
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(this).setBackgroundNormal(createDefaultBackgroundNormal());
		getViewConfig(this).setBackgroundSelected(createDefaultBackgroundSelected());

		resetBackgroudDrawableByPosition();
		super.setDefaultConfig();
	}

	@Override
	public void reverseDefaultConfig()
	{
		getViewConfig(this).setBackgroundNormal(createDefaultBackgroundNormalReverse());
		getViewConfig(this).setBackgroundSelected(createDefaultBackgroundSelectedReverse());

		resetBackgroudDrawableByPosition();
		super.reverseDefaultConfig();
	}

	public void resetBackgroudDrawableByPosition()
	{
		Drawable drawableN = getViewConfig(this).getBackgroundNormal();
		Drawable drawableS = getViewConfig(this).getBackgroundSelected();
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

	public enum TabPosition
	{
		FIRST, MIDDLE, LAST, SINGLE;
	}

}
