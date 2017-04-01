package com.fanwe.library.looper.impl;

import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.SDTimeouter;

public class SDSimpleTimeoutLooper implements SDLooper, SDTimeouter
{
	private SDLooper looper = new SDSimpleLooper();
	private SDTimeouter timeouter = new SDSimpleTimeouter();
	private Runnable runnable;

	@Override
	public boolean isRunning()
	{
		return looper.isRunning();
	}

	@Override
	public SDSimpleTimeoutLooper start(Runnable runnable)
	{
		start(0, DEFAULT_PERIOD, runnable);
		return this;
	}

	@Override
	public SDSimpleTimeoutLooper start(long period, Runnable runnable)
	{
		start(0, DEFAULT_PERIOD, runnable);
		return this;
	}

	@Override
	public SDSimpleTimeoutLooper start(long delay, long period, Runnable runnable)
	{
		this.runnable = runnable;
		looper.start(delay, period, realRunnable);
		return this;
	}

	private Runnable realRunnable = new Runnable()
	{

		@Override
		public void run()
		{
			if (runnable != null)
			{
				if (timeouter.isTimeout())
				{
					timeouter.runTimeout();
					stop();
				} else
				{
					runnable.run();
				}
			} else
			{
				stop();
			}
		}
	};

	@Override
	public SDSimpleTimeoutLooper stop()
	{
		looper.stop();
		return this;
	}

	@Override
	public long getTimeout()
	{
		return timeouter.getTimeout();
	}

	@Override
	public boolean isTimeout()
	{
		return timeouter.isTimeout();
	}

	@Override
	public Runnable getTimeoutRunnable()
	{
		return timeouter.getTimeoutRunnable();
	}

	@Override
	public SDSimpleTimeoutLooper timeout(Runnable timeoutRunnable)
	{
		timeouter.timeout(timeoutRunnable);
		return this;
	}

	@Override
	public SDSimpleTimeoutLooper runTimeout()
	{
		timeouter.runTimeout();
		return this;
	}

	@Override
	public SDSimpleTimeoutLooper setTimeout(long timeout)
	{
		timeouter.setTimeout(timeout);
		return this;
	}

	@Override
	public SDSimpleTimeoutLooper stopTimeout()
	{
		timeouter.stopTimeout();
		return this;
	}

}
