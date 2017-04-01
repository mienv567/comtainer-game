package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-6 下午1:58:07 类说明
 */
@SuppressWarnings("serial")
public class App_ContActModel extends BaseActModel
{
	private int page;
	private int has_next;
	private int totalNum;
	private UserModel userInfo;
	private List<App_ContModel> cuserList;

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getHas_next()
	{
		return has_next;
	}

	public void setHas_next(int has_next)
	{
		this.has_next = has_next;
	}

	public int getTotalNum()
	{
		return totalNum;
	}

	public void setTotalNum(int totalNum)
	{
		this.totalNum = totalNum;
	}

	public UserModel getUserInfo()
	{
		return userInfo;
	}

	public void setUserInfo(UserModel userInfo)
	{
		this.userInfo = userInfo;
	}

	public List<App_ContModel> getCuserList()
	{
		return cuserList;
	}

	public void setCuserList(List<App_ContModel> cuserList)
	{
		this.cuserList = cuserList;
	}

}
