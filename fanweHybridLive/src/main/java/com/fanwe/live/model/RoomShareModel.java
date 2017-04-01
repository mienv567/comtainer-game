package com.fanwe.live.model;

import java.io.Serializable;

public class RoomShareModel implements Serializable
{

	private String shareTitle;
	private String shareImgUrl;
	private String shareKey;
	private String shareUrl;
	private String shareContent;

	public String getShareTitle()
	{
		return shareTitle;
	}

	public void setShareTitle(String share_title)
	{
		this.shareTitle = share_title;
	}

	public String getShareImageUrl()
	{
		return shareImgUrl;
	}

	public void setShareImageUrl(String share_imageUrl)
	{
		this.shareImgUrl = share_imageUrl;
	}

	public String getShareKey()
	{
		return shareKey;
	}

	public void setShareKey(String share_key)
	{
		this.shareKey = share_key;
	}

	public String getShareUrl()
	{
		return shareUrl;
	}

	public void setShareUrl(String share_url)
	{
		this.shareUrl = share_url;
	}

	public String getShareContent()
	{
		return shareContent;
	}

	public void setShareContent(String share_content)
	{
		this.shareContent = share_content;
	}

}
