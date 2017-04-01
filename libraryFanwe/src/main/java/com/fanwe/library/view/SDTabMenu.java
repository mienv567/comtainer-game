package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SDTabMenu extends SDSelectViewAuto
{
	public ImageView mIvTitle;
	public TextView mTvTitle;
	public TextView mTvNumbr;

	public SDTabMenu(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SDTabMenu(Context context)
	{
		super(context);
		init();
	}

	@Override
	protected void init()
	{
		setContentView(R.layout.view_tab_menu);
		mIvTitle = (ImageView) findViewById(R.id.iv_title);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvNumbr = (TextView) findViewById(R.id.tv_number);
		addAutoView(mIvTitle, mTvNumbr, mTvTitle);
		
		setDefaultConfig();	
		onNormal();
		setTextTitleNumber(null);
		
		super.init();
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(mTvTitle).setTextColorNormal(Color.GRAY);
		getViewConfig(mTvTitle).setTextColorSelected(mLibraryConfig.getMainColor());
		super.setDefaultConfig();
	}

	public void setTextTitleNumber(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvNumbr, content);
	}

	public void setTextTitle(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvTitle, content);
	}

	public void setBackgroundTextTitleNumber(int resId)
	{
		mTvNumbr.setBackgroundResource(resId);
	}

}
