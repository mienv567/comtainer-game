package com.sunday.eventbus;

import de.greenrobot.event.EventBus;

public class SDEventManager
{

	public static final int TAG_INT_EMPTY = -999;

	/**
	 * 注册观察者
	 * 
	 * @param subscriber
	 */
	public static void register(Object subscriber)
	{
		EventBus.getDefault().register(subscriber);
	}

	/**
	 * 取消观察者
	 * 
	 * @param subscriber
	 */
	public static void unregister(Object subscriber)
	{
		EventBus.getDefault().unregister(subscriber);
	}

	/**
	 * 发送事件
	 * 
	 * @param event
	 */
	public static void post(Object event)
	{
		EventBus.getDefault().post(event);
	}

	/**
	 * 调用此方法发送事件，观察者必须实现SDEventObserver接口
	 * 
	 * @param tagString
	 */
	@Deprecated
	public static void post(int tagInt)
	{
		post(new SDBaseEvent(null, tagInt));
	}

	/**
	 * 调用此方法发送事件，观察者必须实现SDEventObserver接口
	 * 
	 * @param tagString
	 */
	public static void post(String tagString)
	{
		post(new SDBaseEvent(null, tagString));
	}

	/**
	 * 调用此方法发送事件，观察者必须实现SDEventObserver接口
	 * 
	 * @param tagString
	 */
	@Deprecated
	public static void post(Object data, int tagInt)
	{
		post(new SDBaseEvent(data, tagInt));
	}

	/**
	 * 调用此方法发送事件，观察者必须实现SDEventObserver接口
	 * 
	 * @param tagString
	 */
	public static void post(Object data, String tagString)
	{
		post(new SDBaseEvent(data, tagString));
	}

	/**
	 * 调用此方法发送事件，观察者必须实现SDEventObserver接口
	 * 
	 * @param tagString
	 */
	@Deprecated
	public static void post(Object data, int tagInt, String tagString)
	{
		post(new SDBaseEvent(data, tagInt, tagString));
	}

}
