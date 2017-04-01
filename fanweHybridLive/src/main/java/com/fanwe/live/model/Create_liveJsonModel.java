package com.fanwe.live.model;

public class Create_liveJsonModel
{

	private String title;// 直播间标题
	private int cate_id;// 直播间分类id
	private int location_switch; // 1-上传当前城市名称
	private int is_private; // 1-私密直播
	private String share_type;// 分享类型（weixin,weixin_circle,qq,qzone,sina）,当这个参数有值时，会返回share分享内容

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getCate_id()
	{
		return cate_id;
	}

	public void setCate_id(int cate_id)
	{
		this.cate_id = cate_id;
	}

	public int getLocation_switch()
	{
		return location_switch;
	}

	public void setLocation_switch(int location_switch)
	{
		this.location_switch = location_switch;
	}

	public String getShare_type()
	{
		return share_type;
	}

	public void setShare_type(String share_type)
	{
		this.share_type = share_type;
	}

	public int getIs_private()
	{
		return is_private;
	}

	public void setIs_private(int is_private)
	{
		this.is_private = is_private;
	}

}
