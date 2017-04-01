package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.model.UserModel;

public class CustomMsgViewerJoin extends CustomMsg
{

	public CustomMsgViewerJoin()
	{
		super();
		setType(CustomMsgType.MSG_VIEWER_JOIN);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}

		if (!(o instanceof CustomMsgViewerJoin))
		{
			return false;
		}

		CustomMsgViewerJoin model = (CustomMsgViewerJoin) o;
		UserModel user = model.getSender();
		if (user == null)
		{
			return false;
		}
		if (!user.equals(getSender()))
		{
			return false;
		}

		return true;
	}

}
