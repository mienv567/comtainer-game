package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-31 上午11:16:03 类说明
 */
@SuppressWarnings("serial")
public class App_user_adminActModel extends BaseActModel
{
	private List<App_user_adminModel> manageList;
	private int max_num;
	private int cur_num;

	public List<App_user_adminModel> getManageList()
	{
		return manageList;
	}

	public void setManageList(List<App_user_adminModel> list)
	{
		this.manageList = list;
	}

	public int getMax_num()
	{
		return max_num;
	}

	public void setMax_num(int max_num)
	{
		this.max_num = max_num;
	}

	public int getCur_num()
	{
		return cur_num;
	}

	public void setCur_num(int cur_num)
	{
		this.cur_num = cur_num;
	}

}
