package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * 连麦tooken
 */
public class App_get_tokenActModel extends BaseActModel
{

	private static final long serialVersionUID = 1L;

	public String getRoomToken() {
		return roomToken;
	}

	public void setRoomToken(String roomToken) {
		this.roomToken = roomToken;
	}

	private String roomToken;


}
