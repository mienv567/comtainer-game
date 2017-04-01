package com.fanwe.library.utils;

import com.alibaba.fastjson.JSON;

public class SDJsonUtil
{
	private SDJsonUtil()
	{
	}

	public static <T> T json2Object(String json, Class<T> clazz)
	{
		return JSON.parseObject(json, clazz);
	}

	public static String object2Json(Object obj)
	{
		return JSON.toJSONString(obj);
	}
}
