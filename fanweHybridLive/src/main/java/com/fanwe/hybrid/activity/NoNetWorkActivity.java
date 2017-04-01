package com.fanwe.hybrid.activity;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.live.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-4-13 下午2:37:29 类说明
 */
public class NoNetWorkActivity extends BaseActivity
{
	private TextView mTvReload;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_no_network);
		mIsExitApp = true;
		mTvReload = (TextView) findViewById(R.id.tv_reload);
		mTvReload.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String domain = ApkConstant.SERVER_URL_SHOW_ANIM;
				Intent intent = new Intent();
				intent.putExtra(MainActivity.EXTRA_URL, domain);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	/**
	 * 返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (mIsExitApp)
			{
				exitApp();
			} else
			{
				finish();
			}
		}
		return true;
	}
}
