package com.fanwe.live.animator;

import java.util.ArrayList;
import java.util.List;

public class SDAnimatorPart
{
	private List<SDAnimator> list_anim;

	// add
	public void addAnimator(SDAnimator animator)
	{
		if (animator != null)
		{
			if (list_anim == null)
			{
				list_anim = new ArrayList<SDAnimator>();
			}
			list_anim.add(animator);
		}
	}

	public List<SDAnimator> getList_anim()
	{
		return list_anim;
	}

	public void setList_anim(List<SDAnimator> list_anim)
	{
		this.list_anim = list_anim;
	}

}
