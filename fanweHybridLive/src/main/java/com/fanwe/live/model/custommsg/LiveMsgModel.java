package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.LiveMsgType;

/**
 * 直播间聊天组的item实体
 * 
 * @author Administrator
 * @date 2016-5-18 上午11:03:21
 */
public class LiveMsgModel
{
	private int type = LiveMsgType.MSG_TEXT;
	private MsgModel msg;

	public LiveMsgModel(int type)
	{
		super();
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public MsgModel getMsg()
	{
		return msg;
	}

	public void setMsg(MsgModel msg)
	{
		this.msg = msg;
	}

}
