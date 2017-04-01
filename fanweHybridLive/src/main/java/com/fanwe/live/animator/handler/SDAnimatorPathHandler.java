package com.fanwe.live.animator.handler;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

import com.fanwe.live.animator.PositionModel;
import com.fanwe.live.animator.SDAnimator;
import com.fanwe.live.animator.SDAnimatorPart;
import com.fanwe.live.animator.SDAnimatorPath;

public abstract class SDAnimatorPathHandler
{
	protected View view;
	protected SDAnimatorPath model;

	protected PositionModel start;
	protected PositionModel end;

	public SDAnimatorPathHandler(View view)
	{
		super();
		this.view = view;
	}

	public List<Animator> parse(SDAnimatorPath model)
	{
		this.model = model;
		List<Animator> listAnimator = null;
		if (model != null)
		{
			initPosition();
			if (start != null && end != null)
			{
				listAnimator = createAnimation();
			}
		}
		return listAnimator;
	}

	protected abstract void initPosition();

	protected List<Animator> createAnimation()
	{
		List<Animator> listResult = null;

		List<SDAnimatorPart> listPart = model.getList_part();
		if (listPart != null && !listPart.isEmpty())
		{
			listResult = new ArrayList<Animator>();

			PositionModel last = new PositionModel(start.getX(), start.getY());
			int totalX = end.getX() - start.getX();
			int totalY = end.getY() - start.getY();
			SDAnimatorPartHandler handler = new SDAnimatorPartHandler(view);
			for (int i = 0; i < listPart.size(); i++)
			{
				SDAnimatorPart part = listPart.get(i);
				if (part != null)
				{
					// -----start 为part中的平移动画设置起始和终点位置
					List<SDAnimator> listAnimator = part.getList_anim();
					if (listAnimator != null && !listAnimator.isEmpty())
					{
						for (int j = 0; j < listAnimator.size(); j++)
						{
							SDAnimator animator = listAnimator.get(j);
							if (animator.getType() == SDAnimator.TYPE_TRANSLATE)
							{
								int startX = last.getX();
								int startY = last.getY();
								int deltaX = (int) (totalX * animator.getPercent());
								int deltaY = (int) (totalY * animator.getPercent());
								int endX = startX + deltaX;
								int endY = startY + deltaY;

								if (i == listPart.size() - 1)
								{
									endX = end.getX();
									endY = end.getY();
								}

								PositionModel st = new PositionModel(startX, startY);
								PositionModel ed = new PositionModel(endX, endY);

								animator.setStartPosition(st);
								animator.setEndPosition(ed);

								last.setX(endX);
								last.setY(endY);
								break;
							}
						}
					}
					// -----end 为part中的平移动画设置起始和终点位置
					AnimatorSet partSet = handler.parse(part);
					if (partSet != null)
					{
						listResult.add(partSet);
					}
				}
			}
		}
		return listResult;
	}
}
