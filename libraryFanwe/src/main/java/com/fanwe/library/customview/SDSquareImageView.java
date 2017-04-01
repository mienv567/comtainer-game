package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewUtil;

public class SDSquareImageView extends ImageView
{

	public SDSquareImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SDSquareImageView(Context context)
	{
		super(context);
		init();
	}

	public SDSquareImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		int width = getMeasuredWidth();
		SDViewUtil.setViewHeight(this, width);
		super.onDraw(canvas);
	}

}
