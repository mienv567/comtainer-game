package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

public class App_viewerActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<UserModel> viewerList;

	private int count;
	private int has_next;
	private int watch_number; // 观看总人数
	private List<Integer> watch_number_list; //观看人数集合
	public List<UserModel> getViewerList()
	{
		return viewerList;
	}

	public void setViewerList(List<UserModel> viewerList)
	{
		this.viewerList = viewerList;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getHas_next()
	{
		return has_next;
	}

	public void setHas_next(int has_next)
	{
		this.has_next = has_next;
	}

	public int getWatch_number()
	{
		return watch_number;
	}

	public void setWatch_number(int watch_number)
	{
		this.watch_number = watch_number;
	}

	public List<Integer> getWatch_number_list() {
		return watch_number_list;
	}

	public void setWatch_number_list(List<Integer> watch_number_list) {
		this.watch_number_list = watch_number_list;
	}
}
