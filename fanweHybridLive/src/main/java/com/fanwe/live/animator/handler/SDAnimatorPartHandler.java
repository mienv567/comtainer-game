package com.fanwe.live.animator.handler;

import java.util.List;

import android.animation.AnimatorSet;
import android.view.View;

import com.fanwe.live.animator.SDAnimator;
import com.fanwe.live.animator.SDAnimatorPart;

public class SDAnimatorPartHandler
{
	private View view;
	private SDAnimatorPart model;

	public SDAnimatorPartHandler(View view)
	{
		super();
		this.view = view;
	}

	public final AnimatorSet parse(SDAnimatorPart model)
	{
		this.model = model;
		return createAnimation(model);
	}

	protected AnimatorSet createAnimation(SDAnimatorPart model)
	{
		AnimatorSet partSet = null;
		if (model != null)
		{
			List<SDAnimator> list = model.getList_anim();
			if (list != null && !list.isEmpty())
			{
				partSet = new AnimatorSet();
				SDAnimatorHandler handler = new SDAnimatorHandler(view);
				for (SDAnimator item : list)
				{
					AnimatorSet animatorSet = handler.parse(item);
					if (animatorSet != null)
					{
						partSet.playTogether(animatorSet);
					}
				}
			}
		}
		return partSet;
	}

}
