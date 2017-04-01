package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

public class SDTabCornerText extends SDTabCorner
{

	public TextView mTvTitle;

	public SDTabCornerText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SDTabCornerText(Context context)
	{
		super(context);
	}

	@Override
	protected void init()
	{
		setContentView(R.layout.view_tab_corner_text);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		addAutoView(mTvTitle);

		super.init();
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(mTvTitle).setTextColorNormal(mLibraryConfig.getMainColor()).setTextColorSelected(Color.WHITE);
		super.setDefaultConfig();
	}

	@Override
	public void reverseDefaultConfig()
	{
		getViewConfig(mTvTitle).setTextColorNormal(Color.WHITE).setTextColorSelected(mLibraryConfig.getMainColor());
		super.reverseDefaultConfig();
	}

	public void setTextTitle(String content)
	{
		SDViewBinder.setTextView(mTvTitle, content);
	}

	public void setTextSizeTitle(float sizeSp)
	{
		SDViewUtil.setTextSizeSp(mTvTitle, sizeSp);
	}

}
