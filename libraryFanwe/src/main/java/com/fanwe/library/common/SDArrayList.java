package com.fanwe.library.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SDArrayList<E> extends ArrayList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public E get(int index)
	{
		E obj = null;
		try
		{
			obj = super.get(index);
		} catch (Exception e)
		{
			// TODO: 不处理
		}
		return obj;
	}

	@Override
	public boolean add(E object)
	{
		if (object != null)
		{
			return super.add(object);
		} else
		{
			return false;
		}
	}

	@Override
	public void add(int index, E object)
	{
		try
		{
			if (object != null)
			{
				super.add(index, object);
			}
		} catch (Exception e)
		{
			// 不处理
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		try
		{
			return super.addAll(index, collection);
		} catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public E set(int index, E object)
	{
		try
		{
			return super.set(index, object);
		} catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public List<E> subList(int start, int end)
	{
		try
		{
			return super.subList(start, end);
		} catch (Exception e)
		{
			return null;
		}
	}

}
