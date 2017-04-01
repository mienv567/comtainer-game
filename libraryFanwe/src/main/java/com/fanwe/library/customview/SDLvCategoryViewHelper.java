package com.fanwe.library.customview;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class SDLvCategoryViewHelper
{
	private static final int DEFAULT_INDEX = 0;

	public ListView mLvContent = null;
	private SDLvCategoryViewHelperAdapterInterface mAdapterHelper = null;
	private int mCurrentIndex = DEFAULT_INDEX;
	private SDLvCategoryViewHelperListener mListener = null;
	private String mTitle;

	// -----------------get set

	public SDLvCategoryViewHelperListener getmListener()
	{
		return mListener;
	}

	public int getmCurrentIndex()
	{
		return mCurrentIndex;
	}

	public int getItemCount()
	{
		return mAdapterHelper.getAdapter().getCount();
	}

	public void setmCurrentIndex(int index)
	{
		if (isIndexLegal(index))
		{
			this.mCurrentIndex = index;
			mOnItemClickListener.onItemClick(mLvContent, null, mCurrentIndex + 1, mCurrentIndex);
		}
	}

	private boolean isIndexLegal(int index)
	{
		if (getItemCount() > 0 && mCurrentIndex >= 0 && mCurrentIndex < getItemCount())
		{
			return true;
		} else
		{
			return false;
		}
	}

	public String getmTitle()
	{
		return mTitle;
	}

	public void setmTitle(String mTitle)
	{
		this.mTitle = mTitle;
		if (mListener != null)
		{
			mListener.onTitleChange(mTitle);
		}
	}

	public void setmListener(SDLvCategoryViewHelperListener mListener)
	{
		this.mListener = mListener;
	}

	public SDLvCategoryViewHelper(ListView listView)
	{
		this.mLvContent = listView;
		init();
	}

	private void init()
	{
	}

	public void setAdapter(SDLvCategoryViewHelperAdapterInterface adapter)
	{
		this.mAdapterHelper = adapter;
		initTitle();
		mLvContent.setOnItemClickListener(mOnItemClickListener);
		mLvContent.setAdapter(mAdapterHelper.getAdapter());
	}

	private void initTitle()
	{
		if (getItemCount() <= 0)
		{
			return;
		}

		int titleIndex = mAdapterHelper.getTitleIndex();
		if (titleIndex >= 0)
		{
			mCurrentIndex = titleIndex;
		} else
		{
			mCurrentIndex = DEFAULT_INDEX;
		}
		mAdapterHelper.setPositionSelectState(mCurrentIndex, true, false);
		setmTitle(mAdapterHelper.getTitleNameFromPosition(mCurrentIndex));
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			mAdapterHelper.setPositionSelectState(mCurrentIndex, false, false);
			mAdapterHelper.setPositionSelectState((int) id, true, true);
			mCurrentIndex = (int) id;

			setmTitle(mAdapterHelper.getTitleNameFromPosition(mCurrentIndex));
			if (mListener != null)
			{
				mListener.onItemSelect(mCurrentIndex, mAdapterHelper.getSelectModelFromPosition(mCurrentIndex));
			}
		}
	};

	public interface SDLvCategoryViewHelperListener
	{
		public void onItemSelect(int index, Object model);

		public void onTitleChange(String title);
	}

	public interface SDLvCategoryViewHelperAdapterInterface
	{
		public void setPositionSelectState(int position, boolean select, boolean notify);

		public String getTitleNameFromPosition(int position);

		public BaseAdapter getAdapter();

		public Object getSelectModelFromPosition(int position);

		public int getTitleIndex();
	}
}
