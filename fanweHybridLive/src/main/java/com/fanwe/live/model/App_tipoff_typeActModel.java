package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-26 下午6:08:00 类说明
 */
@SuppressWarnings("serial")
public class App_tipoff_typeActModel extends BaseActModel
{
	private List<App_tipoff_typeModel> tipoffTypes;

	public List<App_tipoff_typeModel> getTipoffTypes()
	{
		return tipoffTypes;
	}

	public void setTipoffTypes(List<App_tipoff_typeModel> tipoffTypes)
	{
		this.tipoffTypes = tipoffTypes;
	}

}
