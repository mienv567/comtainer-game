package com.fanwe.live;

import java.util.LinkedHashMap;
import java.util.Map;

public class SDDataKeeper
{

	private static SDDataKeeper instance;

	private Map<String, Object> mapData = new LinkedHashMap<String, Object>();

	public static SDDataKeeper getInstance()
	{
		if (instance == null)
		{
			instance = new SDDataKeeper();
		}
		return instance;
	}

	private String createKey(Class<?> clazz, String key)
	{
		String result = null;
		if (clazz != null)
		{
			if (key == null)
			{
				key = "";
			}
			result = clazz.getName() + key;
		}
		return result;
	}

	public void put(Class<?> clazz, String key, Object value)
	{
		String realKey = createKey(clazz, key);
		if (realKey != null)
		{
			mapData.put(realKey, value);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<?> clazz, String key)
	{
		T t = null;
		String realKey = createKey(clazz, key);
		if (realKey != null)
		{
			try
			{
				Object value = mapData.remove(realKey);
				t = (T) value;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return t;
	}

	public void clear()
	{
		mapData.clear();
	}

}
