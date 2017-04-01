package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;

/**
 * 用com.fanwe.library.view.SDTabMenu替代
 * 
 * @author js02
 * 
 */
@Deprecated
public class SDTabItemBottom extends SDViewBase
{

	public ImageView mIvTitle;
	public TextView mTvTitle;
	public TextView mTvNumbr;

	public SDTabItemBottom(Context context)
	{
		super(context);
		init();
	}

	public SDTabItemBottom(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_tab_item_bottom, this, true);
		mIvTitle = (ImageView) findViewById(R.id.view_tab_item_iv_title);
		mTvTitle = (TextView) findViewById(R.id.view_tab_item_tv_title);
		mTvNumbr = (TextView) findViewById(R.id.view_tab_item_tv_number);
		mTvNumbr.setVisibility(View.GONE);
		onNormal();
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
		setImageViewNormal(mIvTitle);
		setTextColorNormal(mTvTitle);
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setImageViewSelected(mIvTitle);
		setTextColorSelected(mTvTitle);
		super.onSelected();
	}

}
