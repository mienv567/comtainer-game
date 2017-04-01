package com.fanwe.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class SDBroadcastReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String tagString = SDBroadcastUtil.getTagStringFromIntent(intent);
		onReceive(context, intent, tagString);
	}

	public abstract void onReceive(Context context, Intent intent, String tagString);
}