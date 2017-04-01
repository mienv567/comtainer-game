package com.fanwe.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SDActivityUtil
{

	// startActivity
	public static void startActivity(Activity activity, Class<?> clazz)
	{
		Intent intent = new Intent(activity, clazz);
		startActivity(activity, intent);
	}

	public static void startActivity(Fragment fragment, Class<?> clazz)
	{
		Intent intent = new Intent(fragment.getActivity(), clazz);
		startActivity(fragment, intent);
	}

	public static void startActivity(Activity activity, Intent intent)
	{
		if (activity != null && intent != null)
		{
			try
			{
				activity.startActivity(intent);
			} catch (Exception e)
			{
				SDToast.showToast("启动失败:" + e.toString());
			}
		}
	}

	public static void startActivity(Fragment fragment, Intent intent)
	{
		if (fragment != null && intent != null)
		{
			try
			{
				fragment.startActivity(intent);
			} catch (Exception e)
			{
				SDToast.showToast("启动失败:" + e.toString());
			}
		}
	}

	// startActivityForResult
	public static void startActivityForResult(Activity activity, int requestCode, Class<?> clazz)
	{
		Intent intent = new Intent(activity, clazz);
		startActivityForResult(activity, requestCode, intent);
	}

	public static void startActivityForResult(Fragment fragment, int requestCode, Class<?> clazz)
	{
		Intent intent = new Intent(fragment.getActivity(), clazz);
		startActivityForResult(fragment, requestCode, intent);
	}

	public static void startActivityForResult(Activity activity, int requestCode, Intent intent)
	{
		if (activity != null && intent != null)
		{
			try
			{
				activity.startActivityForResult(intent, requestCode);
			} catch (Exception e)
			{
				SDToast.showToast("启动失败:" + e.toString());
			}
		}
	}

	public static void startActivityForResult(Fragment fragment, int requestCode, Intent intent)
	{
		if (fragment != null && intent != null)
		{
			try
			{
				fragment.startActivityForResult(intent, requestCode);
			} catch (Exception e)
			{
				SDToast.showToast("启动失败:" + e.toString());
			}
		}
	}

}
