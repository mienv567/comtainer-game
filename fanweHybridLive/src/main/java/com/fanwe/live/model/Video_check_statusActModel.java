package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

public class Video_check_statusActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int room_id;// 直播间id
	private int live_in; // 1-正在直播; 0-直播已结束

	private String user_id;// 主播id
	private String group_id;// 聊天组id
	private String head_image;// 主播头像
	private String content;// 提示内容

	public int getRoom_id()
	{
		return room_id;
	}

	public void setRoom_id(int room_id)
	{
		this.room_id = room_id;
	}

	public int getLive_in()
	{
		return live_in;
	}

	public void setLive_in(int live_in)
	{
		this.live_in = live_in;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getGroup_id()
	{
		return group_id;
	}

	public void setGroup_id(String group_id)
	{
		this.group_id = group_id;
	}

	public String getHead_image()
	{
		return head_image;
	}

	public void setHead_image(String head_image)
	{
		this.head_image = head_image;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
