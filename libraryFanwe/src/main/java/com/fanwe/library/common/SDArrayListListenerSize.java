package com.fanwe.library.common;

import java.util.ArrayList;
import java.util.Collection;

public class SDArrayListListenerSize<E> extends ArrayList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int mSizeOld;

	private SDArrayListListener mListener;

	// -----------------get set
	public SDArrayListListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDArrayListListener mListener)
	{
		this.mListener = mListener;
	}

	// --------------------------add
	@Override
	public boolean add(E object)
	{
		beforeAdd();
		boolean rs = super.add(object);
		afterAdd();
		return rs;
	}

	@Override
	public void add(int index, E object)
	{
		beforeAdd();
		super.add(index, object);
		afterAdd();
	}

	@Override
	public boolean addAll(Collection<? extends E> collection)
	{

		beforeAdd();
		boolean rs = super.addAll(collection);
		afterAdd();
		return rs;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		beforeAdd();
		boolean rs = super.addAll(index, collection);
		afterAdd();
		return rs;
	}

	protected void beforeAdd()
	{
		mSizeOld = size();
	}

	protected void afterAdd()
	{
		maybeSizeChange();
	}

	// --------------------------------remove

	@Override
	public E remove(int index)
	{
		beforeRemove();
		E e = super.remove(index);
		afterRemove();
		return e;
	}

	@Override
	public boolean remove(Object object)
	{
		beforeRemove();
		boolean rs = super.remove(object);
		afterRemove();
		return rs;
	}

	@Override
	public boolean removeAll(Collection<?> collection)
	{
		beforeRemove();
		boolean rs = super.removeAll(collection);
		afterRemove();
		return rs;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex)
	{
		beforeRemove();
		super.removeRange(fromIndex, toIndex);
		afterRemove();
	}

	private void beforeRemove()
	{
		mSizeOld = size();
	}

	private void afterRemove()
	{
		maybeSizeChange();
	}

	// -----------------clear
	@Override
	public void clear()
	{
		beforeRemove();
		super.clear();
		afterRemove();
	}

	// ----------------maybe

	private void maybeSizeChange()
	{
		if (mSizeOld != size())
		{
			notifyOnSizeChange();
		}
	}

	// ------------------notify

	protected void notifyOnSizeChange()
	{
		if (mListener != null)
		{
			mListener.onSizeChange(mSizeOld, size());
		}
	}

	public interface SDArrayListListener
	{
		public void onSizeChange(int sizeOld, int sizeNew);
	}

}
