package com.fanwe.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SDTabText extends SDSelectViewAuto
{

	public TextView mTv_title;

	public SDTabText(Context context)
	{
		super(context);
		init();
	}

	public SDTabText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	@Override
	protected void init()
	{
		setContentView(R.layout.view_tab_text);
		mTv_title = (TextView) findViewById(R.id.tv_title);
		addAutoView(mTv_title);

		setDefaultConfig();
		onNormal();
		super.init();
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(mTv_title).setTextColorNormalResId(R.color.gray);
		getViewConfig(mTv_title).setTextColorSelected(mLibraryConfig.getMainColor());
		super.setDefaultConfig();
	}

	public void setTextSizeTitleSp(int textSizeSp)
	{
		SDViewUtil.setTextSizeSp(mTv_title, textSizeSp);
	}

	public void setTextTitle(CharSequence content)
	{
		SDViewBinder.setTextView(mTv_title, content);
	}

}
