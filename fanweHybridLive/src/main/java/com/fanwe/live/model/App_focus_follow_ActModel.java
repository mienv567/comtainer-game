package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-12 下午6:30:19 类说明
 */
@SuppressWarnings("serial")
public class App_focus_follow_ActModel extends BaseActModel
{
	private List<UserModel> relationshipList;
	private int has_next;
	private int page;

	public List<UserModel> getRelationshipList()
	{
		return relationshipList;
	}

	public void setRelationshipList(List<UserModel> relationshipList)
	{
		this.relationshipList = relationshipList;
	}

	public int getHas_next()
	{
		return has_next;
	}

	public void setHas_next(int has_next)
	{
		this.has_next = has_next;
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
