package com.fanwe.library.customview;

import android.view.View;
import android.view.View.OnClickListener;

public class SDViewNavigatorManager
{

	private SDViewBase[] mItems = null;
	private int mCurrentIndex = -1;
	private int mLastIndex = -1;
	private Mode mMode = Mode.MUST_ONE_SELECT;
	private boolean mClickAble = true;

	public void setmClickAble(boolean mClickAble)
	{
		this.mClickAble = mClickAble;
	}

	public int getmLastIndex()
	{
		return mLastIndex;
	}

	public Mode getmMode()
	{
		return mMode;
	}

	public void setmMode(Mode mMode)
	{
		this.mMode = mMode;
	}

	private SDViewNavigatorManagerListener mListener = null;

	public int getSelectedIndex()
	{
		return mCurrentIndex;
	}

	public SDViewBase getSelectedView()
	{
		if (isIndexLegal(mCurrentIndex))
		{
			return mItems[mCurrentIndex];
		} else
		{
			return null;
		}
	}

	public SDViewNavigatorManagerListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDViewNavigatorManagerListener mListener)
	{
		this.mListener = mListener;
	}

	public void setItems(SDViewBase[] items)
	{
		resetIndex();
		if (items != null && items.length > 0)
		{
			mItems = items;
			for (int i = 0; i < mItems.length; i++)
			{
				mItems[i].setId(i);
				mItems[i].setOnClickListener(new SDBottomNavigatorView_OnClickListener());
				mItems[i].onNormal();
			}
		}
	}

	private void resetIndex()
	{
		mCurrentIndex = -1;
		mLastIndex = -1;
	}

	private boolean isIndexLegal(int index)
	{
		if (mItems != null && index >= 0 && index < mItems.length)
		{
			return true;
		}
		return false;
	}

	class SDBottomNavigatorView_OnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			setSelectIndex(v.getId(), v, true);
		}

	}

	public boolean setSelectIndex(int index, View v, boolean notifyListener)
	{
		if (!mClickAble)
		{
			return false;
		}

		if (!isIndexLegal(index))
		{
			return false;
		}
		switch (mMode)
		{
		case MUST_ONE_SELECT:

			break;
		case CAN_NONE_SELECT:
			if (index == mCurrentIndex)
			{
				if (mItems[index].ismSelected())
				{
					normalItem(index);
					mCurrentIndex = -1;
				} else
				{
					selectItem(index);
				}
				notifyListener(mCurrentIndex, v, notifyListener);
				return true;
			}

			break;
		default:

			break;
		}

		normalItem(mCurrentIndex);
		selectItem(index);
		notifyListener(mCurrentIndex, v, notifyListener);
		return true;
	}

	private void normalItem(int index)
	{
		if (isIndexLegal(index))
		{
			mItems[index].onNormal();
		}
	}

	private void selectItem(int index)
	{
		if (isIndexLegal(index))
		{
			mItems[index].onSelected();
			mLastIndex = mCurrentIndex;
			mCurrentIndex = index;
		}
	}

	private void notifyListener(int index, View v, boolean notifyListener)
	{
		if (mListener != null && notifyListener)
		{
			mListener.onItemClick(v, index);
		}
	}

	public boolean setSelectIndexLast(boolean notifyListener)
	{
		return setSelectIndex(mLastIndex, null, notifyListener);
	}

	public void clearSelected()
	{
		if (mItems != null)
		{
			for (int i = 0; i < mItems.length; i++)
			{
				mItems[i].onNormal();
			}
		}
		resetIndex();
	}

	public interface SDViewNavigatorManagerListener
	{
		public void onItemClick(View v, int index);
	}

	public enum Mode
	{
		MUST_ONE_SELECT, CAN_NONE_SELECT
	}
}
