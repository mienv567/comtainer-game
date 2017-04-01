package com.sunday.eventbus;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;
import de.greenrobot.event.EventBus;

public class SDEventBusFactory
{

	private static Map<String, EventBus> mMapEventBus = new HashMap<String, EventBus>();

	public static EventBus get(String key)
	{
		EventBus eventBus = null;
		if (!TextUtils.isEmpty(key))
		{
			eventBus = mMapEventBus.get(key);
			if (eventBus == null)
			{
				eventBus = new EventBus();
				mMapEventBus.put(key, eventBus);
			}
		}
		return eventBus;
	}

	public static EventBus remove(String key)
	{
		return mMapEventBus.remove(key);
	}

	public static void clear()
	{
		mMapEventBus.clear();
	}

}
