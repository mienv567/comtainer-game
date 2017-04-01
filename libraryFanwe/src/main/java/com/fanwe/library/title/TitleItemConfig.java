package com.fanwe.library.title;

import android.view.View;
import android.view.View.OnClickListener;

import com.fanwe.library.title.TitleItem.EnumShowType;
import com.fanwe.library.utils.SDResourcesUtil;

public class TitleItemConfig
{
	private int imageLeftResId;
	private int imageRightResId;
	private int textTopResId;
	private int textBotResId;
	private int backgroundTextResId;
	private String textTop;
	private String textBot;
	private EnumShowType showType;
	private TitleItem titleItem;
	private int visibility = View.VISIBLE;
	private OnClickListener onClickListener;

	public TitleItemConfig create()
	{
		create(false);
		return this;
	}

	public TitleItemConfig create(boolean showTypeChange)
	{
		if (titleItem != null)
		{
			titleItem.create(showTypeChange);
		}
		return this;
	}

	public boolean isAddToMore()
	{
		if (titleItem != null)
		{
			return titleItem.mIsAddToMore;
		}
		return false;
	}

	// -----------------get set

	public int getVisibility()
	{
		return visibility;
	}

	public OnClickListener getOnClickListener()
	{
		return onClickListener;
	}

	public TitleItemConfig setOnClickListener(OnClickListener onClickListener)
	{
		this.onClickListener = onClickListener;
		create();
		return this;
	}

	public TitleItemConfig setVisibility(int visibility)
	{
		this.visibility = visibility;
		create();
		return this;
	}

	public TitleItem getTitleItem()
	{
		return titleItem;
	}

	public TitleItemConfig setTitleItem(TitleItem titleItem)
	{
		this.titleItem = titleItem;
		create();
		return this;
	}

	public int getBackgroundTextResId()
	{
		return backgroundTextResId;
	}

	public TitleItemConfig setBackgroundTextResId(int backgroundTextResId)
	{
		this.backgroundTextResId = backgroundTextResId;
		create();
		return this;
	}

	public EnumShowType getShowType()
	{
		return showType;
	}

	public TitleItemConfig setShowType(EnumShowType showType)
	{
		this.showType = showType;
		create(true);
		return this;
	}

	public int getImageLeftResId()
	{
		return imageLeftResId;
	}

	public TitleItemConfig setImageLeftResId(int imageLeftResId)
	{
		this.imageLeftResId = imageLeftResId;
		create();
		return this;
	}

	public int getImageRightResId()
	{
		return imageRightResId;
	}

	public TitleItemConfig setImageRightResId(int imageRightResId)
	{
		this.imageRightResId = imageRightResId;
		create();
		return this;
	}

	public int getTextTopResId()
	{
		return textTopResId;
	}

	public TitleItemConfig setTextTopResId(int textTopResId)
	{
		this.textTopResId = textTopResId;
		setTextTop(SDResourcesUtil.getString(textTopResId));
		return this;
	}

	public int getTextBotResId()
	{
		return textBotResId;
	}

	public TitleItemConfig setTextBotResId(int textBotResId)
	{
		this.textBotResId = textBotResId;
		setTextBot(SDResourcesUtil.getString(textBotResId));
		return this;
	}

	public String getTextTop()
	{
		return textTop;
	}

	public TitleItemConfig setTextTop(String textTop)
	{
		this.textTop = textTop;
		create();
		return this;
	}

	public String getTextBot()
	{
		return textBot;
	}

	public TitleItemConfig setTextBot(String textBot)
	{
		this.textBot = textBot;
		create();
		return this;
	}

}
