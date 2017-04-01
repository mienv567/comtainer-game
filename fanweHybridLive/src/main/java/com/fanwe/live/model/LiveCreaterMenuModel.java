package com.fanwe.live.model;

import com.fanwe.library.common.SDSelectManager.SDSelectable;

public class LiveCreaterMenuModel implements SDSelectable
{

	private int imageResIdNormal;
	private int imageResIdSelected;
	private int textColorResIdNormal;
	private int textColorResIdSelected;
	private String textNormal;
	private String textSelected;

	private boolean enable = true;

	private boolean selected;

	public int getTextColorResIdNormal()
	{
		return textColorResIdNormal;
	}

	public void setTextColorResIdNormal(int textColorResIdNormal)
	{
		this.textColorResIdNormal = textColorResIdNormal;
	}

	public int getTextColorResIdSelected()
	{
		return textColorResIdSelected;
	}

	public void setTextColorResIdSelected(int textColorResIdSelected)
	{
		this.textColorResIdSelected = textColorResIdSelected;
	}

	@Override
	public boolean isSelected()
	{
		return selected;
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public int getImageResIdNormal()
	{
		return imageResIdNormal;
	}

	public void setImageResIdNormal(int imageResIdNormal)
	{
		this.imageResIdNormal = imageResIdNormal;
	}

	public int getImageResIdSelected()
	{
		return imageResIdSelected;
	}

	public void setImageResIdSelected(int imageResIdSelected)
	{
		this.imageResIdSelected = imageResIdSelected;
	}

	public String getTextNormal()
	{
		return textNormal;
	}

	public void setTextNormal(String textNormal)
	{
		this.textNormal = textNormal;
	}

	public String getTextSelected()
	{
		return textSelected;
	}

	public void setTextSelected(String textSelected)
	{
		this.textSelected = textSelected;
	}

	public boolean isEnable()
	{
		return enable;
	}

	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}

}
