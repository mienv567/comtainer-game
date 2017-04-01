package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

public class App_followActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int relationship; // 1-已关注，0-未关注

	public int getRelationship()
	{
		return relationship;
	}

	public void setRelationship(int relationship)
	{
		this.relationship = relationship;
	}

}
