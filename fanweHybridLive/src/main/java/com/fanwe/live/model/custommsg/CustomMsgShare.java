package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgShare extends CustomMsg
{
	private String desc;
	public CustomMsgShare()
	{
		super();
		setType(CustomMsgType.MSG_SHARE);
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

}
