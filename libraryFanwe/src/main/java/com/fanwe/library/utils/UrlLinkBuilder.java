package com.fanwe.library.utils;

import android.text.TextUtils;

public class UrlLinkBuilder
{
	private String mUrl;
	private StringBuilder mSb;

	public UrlLinkBuilder(String url)
	{
		this.mUrl = url;
		reset();
	}

	public UrlLinkBuilder add(String key, String value)
	{
		if (!TextUtils.isEmpty(key) && value != null)
		{
			beforeAdd();
			mSb.append(key).append("=").append(value);
		}
		return this;
	}

	public String build()
	{
		return mSb.toString();
	}

	public void reset()
	{
		mSb = new StringBuilder(mUrl);
	}

	private void beforeAdd()
	{
		String currentUrl = mSb.toString();
		if (currentUrl.contains("?"))
		{
			if (currentUrl.endsWith("?"))
			{

			} else if (currentUrl.endsWith("&"))
			{

			} else
			{
				mSb.append("&");
			}
		} else
		{
			mSb.append("?");
		}
	}

}
