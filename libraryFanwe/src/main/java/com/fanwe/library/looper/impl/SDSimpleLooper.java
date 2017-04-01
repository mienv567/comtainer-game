package com.fanwe.library.looper.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fanwe.library.looper.SDLooper;

public class SDSimpleLooper implements SDLooper
{
	private static final int MSG_WHAT = 1990;

	private Runnable runnable;
	private long period;
	private boolean isRunning = false;
	private boolean isNeedStop = false;

	private final Handler handler = new Handler(Looper.getMainLooper())
	{
		public void handleMessage(Message msg)
		{
			if (isNeedStop)
			{

			} else
			{
				if (runnable != null)
				{
					runnable.run();
					SDSimpleLooper.this.sendMessageDelayed(period);
				} else
				{
					stop();
				}
			}
		};
	};

	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public SDLooper start(Runnable runnable)
	{
		start(0, DEFAULT_PERIOD, runnable);
		return this;
	}

	@Override
	public SDLooper start(long period, Runnable runnable)
	{
		start(0, period, runnable);
		return this;
	}

	@Override
	public SDLooper start(long delay, long period, Runnable runnable)
	{
		stop();

		this.isRunning = true;
		this.isNeedStop = false;
		this.runnable = runnable;
		this.period = period;

		if (period <= 0)
		{
			period = DEFAULT_PERIOD;
		}
		sendMessageDelayed(delay);
		return this;
	}

	private void sendMessageDelayed(long delay)
	{
		if (delay < 0)
		{
			delay = 0;
		}
		Message msg = handler.obtainMessage(MSG_WHAT);
		handler.sendMessageDelayed(msg, delay);
	}

	@Override
	public SDLooper stop()
	{
		handler.removeMessages(MSG_WHAT);
		isRunning = false;
		isNeedStop = true;
		return this;
	}

}
