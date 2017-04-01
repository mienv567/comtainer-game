package com.fanwe.live.animator;

import java.util.ArrayList;
import java.util.List;

public class SDAnimatorPath
{

	private List<SDAnimatorPart> list_part;

	// add
	public void addPart(SDAnimatorPart animatorPart)
	{
		if (animatorPart != null)
		{
			if (list_part == null)
			{
				list_part = new ArrayList<SDAnimatorPart>();
			}
			list_part.add(animatorPart);
		}
	}

	public List<SDAnimatorPart> getList_part()
	{
		return list_part;
	}

	public void setList_part(List<SDAnimatorPart> list_part)
	{
		this.list_part = list_part;
	}

}
