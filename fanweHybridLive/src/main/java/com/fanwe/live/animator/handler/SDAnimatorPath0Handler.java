package com.fanwe.live.animator.handler;

import android.view.View;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.animator.PositionModel;

public class SDAnimatorPath0Handler extends SDAnimatorPathHandler
{

	public SDAnimatorPath0Handler(View view)
	{
		super(view);
	}

	@Override
	protected void initPosition()
	{
		int viewX = SDViewUtil.getViewXOnScreen(view);
		int viewY = SDViewUtil.getViewYOnScreen(view);
		int viewWidth = SDViewUtil.getViewWidth(view);
		int viewHeight = SDViewUtil.getViewHeight(view);

		int sx = -viewX - viewWidth;
		int sy = -viewY - viewHeight;

		int ex = SDViewUtil.getScreenWidth();
		int ey = SDViewUtil.getScreenHeight();

		start = new PositionModel(sx, sy);
		end = new PositionModel(ex, ey);

	}

}
