package com.fanwe.library.collection;

import java.util.LinkedList;

public class SDLinkedList<E> extends LinkedList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean offer(E o)
	{
		if (o != null)
		{
			return super.offer(o);
		} else
		{
			return false;
		}
	}

	@Override
	public boolean offerFirst(E e)
	{
		if (e != null)
		{
			return super.offerFirst(e);
		} else
		{
			return false;
		}
	}

	@Override
	public boolean offerLast(E e)
	{
		if (e != null)
		{
			return super.offerLast(e);
		} else
		{
			return false;
		}
	}

}
