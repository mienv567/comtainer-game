package com.fanwe.library.looper.impl;

import com.fanwe.library.looper.SDTimeouter;

public class SDSimpleTimeouter implements SDTimeouter
{

	private Runnable timeoutRunnable;
	private long timeout;
	private long startTime;

	@Override
	public Runnable getTimeoutRunnable()
	{
		return timeoutRunnable;
	}

	@Override
	public long getTimeout()
	{
		return timeout;
	}

	@Override
	public boolean isTimeout()
	{
		boolean result = false;
		if (timeout > 0)
		{
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime >= timeout)
			{
				// 超时
				result = true;
			}
		}
		return result;
	}

	@Override
	public SDTimeouter timeout(Runnable timeoutRunnable)
	{
		this.timeoutRunnable = timeoutRunnable;
		return this;
	}

	@Override
	public SDTimeouter setTimeout(long timeout)
	{
		this.timeout = timeout;
		this.startTime = System.currentTimeMillis();
		return this;
	}

	@Override
	public SDTimeouter stopTimeout()
	{
		this.timeout = 0;
		return this;
	}

	@Override
	public SDTimeouter runTimeout()
	{
		if (timeoutRunnable != null)
		{
			timeoutRunnable.run();
		}
		return this;
	}

}
