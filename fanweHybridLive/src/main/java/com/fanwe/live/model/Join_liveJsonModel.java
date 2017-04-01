package com.fanwe.live.model;


public class Join_liveJsonModel
{

	private int type;// 0:热门;1:最新;2:关注 [当room_id=0时有效，随机返回一个type类型下的直播]
	private int room_id;// 直播间id
	private String group_id;// 聊天组id
	private String user_id; // 主播id
	private String head_image; // 主播头像

	public String getGroup_id()
	{
		return group_id;
	}

	public void setGroup_id(String group_id)
	{
		this.group_id = group_id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getHead_image()
	{
		return head_image;
	}

	public void setHead_image(String head_image)
	{
		this.head_image = head_image;
	}

	public int getRoom_id()
	{
		return room_id;
	}

	public void setRoom_id(int room_id)
	{
		this.room_id = room_id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}
