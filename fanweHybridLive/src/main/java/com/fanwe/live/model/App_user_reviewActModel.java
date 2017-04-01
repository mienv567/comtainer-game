package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-12 下午8:05:03 类说明
 */
@SuppressWarnings("serial")
public class App_user_reviewActModel extends BaseActModel
{
	private List<ItemApp_user_reviewModel> reviewList;
	private int has_next;
	private int count;
	private int page;

	public List<ItemApp_user_reviewModel> getReviewList()
	{
		return reviewList;
	}

	public void setReviewList(List<ItemApp_user_reviewModel> reviewList)
	{
		this.reviewList = reviewList;
	}

	public int getHas_next()
	{
		return has_next;
	}

	public void setHas_next(int has_next)
	{
		this.has_next = has_next;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

}
