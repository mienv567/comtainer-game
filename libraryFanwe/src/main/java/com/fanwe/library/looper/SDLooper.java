package com.fanwe.library.looper;

public interface SDLooper
{

	public static final long DEFAULT_PERIOD = 300;

	boolean isRunning();

	SDLooper start(Runnable runnable);

	SDLooper start(long period, Runnable runnable);

	SDLooper start(long delay, long period, Runnable runnable);

	SDLooper stop();

}