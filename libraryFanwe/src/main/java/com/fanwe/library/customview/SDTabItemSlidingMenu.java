package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;

public class SDTabItemSlidingMenu extends SDViewBase
{

	public View mView = null;
	public ImageView mIvTitle = null;
	public TextView mTvTitle = null;

	public SDTabItemSlidingMenu(Context context)
	{
		super(context);
		init();
	}

	public SDTabItemSlidingMenu(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mView = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_item_sld_menu, null);
		mIvTitle = (ImageView) mView.findViewById(R.id.view_simple_menu_item_img_title);
		mTvTitle = (TextView) mView.findViewById(R.id.view_simple_menu_item_txt_title);

		this.addView(mView, getLayoutParamsMW());
	}

	public void setTextTitle(String text)
	{
		if (text != null)
		{
			mTvTitle.setText(text);
		}
	}

	@Override
	public void onNormal()
	{
		setImageViewNormal(mIvTitle);
		setTextColorNormal(mTvTitle);
		setViewBackgroundNormal(mView);
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setImageViewSelected(mIvTitle);
		setTextColorSelected(mTvTitle);
		setViewBackgroundSelected(mView);
		super.onSelected();
	}

}
