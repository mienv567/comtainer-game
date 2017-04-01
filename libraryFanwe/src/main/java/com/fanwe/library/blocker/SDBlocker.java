package com.fanwe.library.blocker;

public class SDBlocker
{

	public static final long DEFAULT_BLOCK_TIME = 500;

	private long blockTime;
	private volatile long lastTime;

	public SDBlocker()
	{
		this(DEFAULT_BLOCK_TIME);
	}

	public SDBlocker(long blockTime)
	{
		super();
		setBlockTime(blockTime);
	}

	public long getLastTime()
	{
		return lastTime;
	}

	public void setBlockTime(long blockTime)
	{
		if (blockTime < 0)
		{
			blockTime = 0;
		}
		this.blockTime = blockTime;
	}

	public boolean block()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime < blockTime)
		{
			// 拦截掉
			return true;
		} else
		{
			lastTime = currentTime;
			return false;
		}
	}
}
