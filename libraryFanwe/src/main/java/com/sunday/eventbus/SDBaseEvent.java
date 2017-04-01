package com.sunday.eventbus;

public class SDBaseEvent
{
	protected int tagInt = SDEventManager.TAG_INT_EMPTY;
	protected Object data = null;
	protected String tagString = null;

	public SDBaseEvent(Object data, int tagInt)
	{
		this.tagInt = tagInt;
		this.data = data;
	}

	public SDBaseEvent(Object data, String tagString)
	{
		this.data = data;
		this.tagString = tagString;
	}

	public SDBaseEvent(Object data, int tagInt, String tagString)
	{
		this.tagInt = tagInt;
		this.data = data;
		this.tagString = tagString;
	}

	public int getTagInt()
	{
		return tagInt;
	}

	public void setTagInt(int tagInt)
	{
		this.tagInt = tagInt;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public String getTagString()
	{
		return tagString;
	}

	public void setTagString(String tagString)
	{
		this.tagString = tagString;
	}

}
