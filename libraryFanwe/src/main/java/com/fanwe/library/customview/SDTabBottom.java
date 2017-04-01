package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;

/**
 * 用com.fanwe.library.view.SDTabMenu替代
 * 
 * @author Administrator
 * 
 */
@Deprecated
public class SDTabBottom extends SDViewBase
{

	public ImageView mIvTitle;
	public TextView mTvTitle;
	public TextView mTvNumbr;

	public SDTabBottom(Context context)
	{
		super(context);
	}

	public SDTabBottom(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onInit()
	{
		setContentView(R.layout.view_tab_bottom);
		mIvTitle = (ImageView) findViewById(R.id.iv_title);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvNumbr = (TextView) findViewById(R.id.tv_number);

		setTextTitleNumber(null);
		setDefaultConfig();
		onNormal();
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(mTvTitle).setmTextColorNormal(Color.GRAY);
		getViewConfig(mTvTitle).setmTextColorSelected(mLibraryConfig.getMainColor());
	}

	public void setTextTitleNumber(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvNumbr, content);
	}

	public void setTextTitle(String content)
	{
		SDViewBinder.setTextViewsVisibility(mTvTitle, content);
	}

	public void setImageTitle(int resId)
	{
		SDViewBinder.setImageViewsVisibility(mIvTitle, resId);
	}

	public void setBackgroundTextTitleNumber(int resId)
	{
		mTvNumbr.setBackgroundResource(resId);
	}

	public void setTextSizeTitleSp(int sizeSp)
	{
		setTextSizeSp(mTvTitle, sizeSp);
	}

	public void setTextSizeNumberSp(int sizeSp)
	{
		setTextSizeSp(mTvNumbr, sizeSp);
	}

	// ----------------------states

	@Override
	public void onNormal()
	{
		mIvTitle.setImageResource(getViewConfig(mIvTitle).getmImageNormalResId());
		mTvTitle.setTextColor(getViewConfig(mTvTitle).getmTextColorNormal());

		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mIvTitle.setImageResource(getViewConfig(mIvTitle).getmImageSelectedResId());
		mTvTitle.setTextColor(getViewConfig(mTvTitle).getmTextColorSelected());

		super.onSelected();
	}

}
