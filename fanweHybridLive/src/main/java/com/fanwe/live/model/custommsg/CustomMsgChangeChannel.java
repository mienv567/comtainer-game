package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgChangeChannel extends CustomMsg
{
	private String play_url;

	public CustomMsgChangeChannel()
	{
		super();
		setType(CustomMsgType.MSG_CHANGE_CHANNEL);
	}

	public String getPlay_url() {
		return play_url;
	}

	public void setPlay_url(String play_url) {
		this.play_url = play_url;
	}
}
