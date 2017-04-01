package com.fanwe.library.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.fanwe.library.SDLibrary;

/**
 * 广播工具类
 * 
 * @author Administrator
 * @date 2016-5-6 上午10:09:37
 */
public class SDBroadcastUtil
{

	public static final String ACTION_BASE = "com.fanwe.action.base";
	public static final String BASE_KEY_STRING = "base_key_string";

	public static Application getApplication()
	{
		return SDLibrary.getInstance().getApplication();
	}

	public static BroadcastReceiver registerReceiver(BroadcastReceiver receiver)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_BASE);
		return registerReceiver(receiver, filter);
	}

	public static BroadcastReceiver registerReceiver(BroadcastReceiver receiver, IntentFilter filter)
	{
		if (receiver != null)
		{
			getApplication().registerReceiver(receiver, filter);
		}
		return receiver;
	}

	public static void unRegisterReceiver(BroadcastReceiver receiver)
	{
		if (receiver != null)
		{
			getApplication().unregisterReceiver(receiver);
		}
	}

	public static void sendBroadcast(Intent intent)
	{
		getApplication().sendBroadcast(intent);
	}

	public static void sendBroadcast(String tagString)
	{
		sendBroadcast(null, tagString);
	}

	public static void sendBroadcast(Intent intent, String tagString)
	{
		getApplication().sendBroadcast(wrapperIntent(intent, tagString));
	}

	public static Intent wrapperIntent(Intent intent, String tagString)
	{
		if (intent == null)
		{
			intent = new Intent();
		}
		intent.setAction(ACTION_BASE);
		intent.putExtra(BASE_KEY_STRING, tagString);
		return intent;
	}

	public static String getTagStringFromIntent(Intent intent)
	{
		String tagString = null;
		if (intent != null)
		{
			tagString = intent.getStringExtra(BASE_KEY_STRING);
		}
		return tagString;
	}

}
