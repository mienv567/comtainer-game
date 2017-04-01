package com.sunday.eventbus;

public interface SDEventObserver
{

	public void onEvent(SDBaseEvent event);

	public void onEventMainThread(SDBaseEvent event);

	public void onEventBackgroundThread(SDBaseEvent event);

	public void onEventAsync(SDBaseEvent event);
}
