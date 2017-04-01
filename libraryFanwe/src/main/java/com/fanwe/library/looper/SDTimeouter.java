package com.fanwe.library.looper;

public interface SDTimeouter
{
	long getTimeout();

	boolean isTimeout();

	Runnable getTimeoutRunnable();

	SDTimeouter timeout(Runnable timeoutRunnable);

	SDTimeouter runTimeout();

	SDTimeouter setTimeout(long timeout);

	SDTimeouter stopTimeout();
}