package com.fanwe.library.customview;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fanwe.library.customview.SDLvCategoryViewHelper.SDLvCategoryViewHelperAdapterInterface;

public class SD2LvCategoryViewHelper
{

	private static final int DEFAULT_INDEX = 0;

	private SD2LvCategoryViewHelperAdapterInterface mAdapterHelperLeft;
	private SD2LvCategoryViewHelperAdapterInterface mAdapterHelperRight;

	public ListView mLvLeft = null;
	public ListView mLvRight = null;

	private int mCurrentIndexLeft = 0;
	private int mCurrentIndexRight = 0;

	private SD2LvCategoryViewHelperListener mListener = null;

	private int mOldLeftIndex = 0;

	/** 是否点击左边直接消失popwindow */
	private boolean mIsNeedNotifyDirect = true;

	/** 当子分类的数量小于等于这个值的时候点击左边直接消失popwindow */
	private int mNotifyDirectRightCount = 1;

	private String mTitle;

	// ----------------get set

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

	public void setmListener(SD2LvCategoryViewHelperListener mListener)
	{
		this.mListener = mListener;
	}

	public SD2LvCategoryViewHelper(ListView lvLeft, ListView lvRight)
	{
		this.mLvLeft = lvLeft;
		this.mLvRight = lvRight;
		init();
	}

	private void init()
	{
	}

	public int getRightItemCount()
	{
		return mAdapterHelperRight.getAdapter().getCount();
	}

	public int getLeftItemCount()
	{
		return mAdapterHelperLeft.getAdapter().getCount();
	}

	private boolean isIndexLegalInLeft(int index)
	{
		boolean result = false;
		if (index >= 0 && index < getLeftItemCount())
		{
			result = true;
		}
		return result;
	}

	private boolean isIndexLegalInRight(int index)
	{
		boolean result = false;
		if (index >= 0 && index < getRightItemCount())
		{
			result = true;
		}
		return result;
	}

	public void setSelectIndex(int left, int right)
	{
		if (isIndexLegalInLeft(left))
		{
			onItemClickLeft(left);
		}
		if (isIndexLegalInRight(right))
		{
			onItemClickRight(right);
		}
	}

	private OnItemClickListener mLeftListViewItemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			onItemClickLeft((int) id);
		}
	};

	private void onItemClickLeft(int position)
	{
		mAdapterHelperLeft.setPositionSelectState(mCurrentIndexLeft, false, false);
		mAdapterHelperLeft.setPositionSelectState(position, true, true);
		mCurrentIndexLeft = position;

		Object rightListModel = mAdapterHelperLeft.getRightListModelFromPosition_left(mCurrentIndexLeft);
		mAdapterHelperRight.updateRightListModel_right(rightListModel);

		// --------------------------是否需要点击左边直接回调
		boolean isNotifyDirect = false;
		if (mIsNeedNotifyDirect)
		{
			int rightCount = getRightItemCount();
			if (rightCount <= mNotifyDirectRightCount) // 点击左边直接消失popWindow
			{
				isNotifyDirect = true;
				String title = mAdapterHelperLeft.getTitleNameFromPosition(mCurrentIndexLeft);
				// ===================让右边第一项直接选中(考虑到有的用户数据右边没有子分类的时候右边只有一项"全部")，如果不需要，删除这段代码==============
				if (getRightItemCount() > 0)
				{
					mAdapterHelperRight.setPositionSelectState(0, true, true);
				}
				// ================================================================================================
				removeOldRightSelect();
				setmTitle(title);
			}
		}

		if (mListener != null)
		{
			Object leftModel = mAdapterHelperLeft.getSelectModelFromPosition(mCurrentIndexLeft);
			mListener.onLeftItemSelect(mCurrentIndexLeft, leftModel, isNotifyDirect);
		}
	}

	private OnItemClickListener mRightListViewItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			onItemClickRight((int) id);
		}
	};

	private void onItemClickRight(int position)
	{
		removeOldRightSelect();
		mAdapterHelperRight.setPositionSelectState(position, true, true);
		mCurrentIndexRight = position;
		mOldLeftIndex = mCurrentIndexLeft;

		// ================================================================
		String title = null;
		if (mCurrentIndexRight <= (mNotifyDirectRightCount - 1))
		{
			if (mIsNeedNotifyDirect)
			{
				title = mAdapterHelperLeft.getTitleNameFromPosition(mCurrentIndexLeft);
			} else
			{
				title = mAdapterHelperRight.getTitleNameFromPosition(mCurrentIndexRight);
			}
		} else
		{
			title = mAdapterHelperRight.getTitleNameFromPosition(mCurrentIndexRight);
		}
		// ================================================================
		setmTitle(title);
		if (mListener != null)
		{
			Object leftModel = mAdapterHelperLeft.getSelectModelFromPosition(mCurrentIndexLeft);
			Object rightModel = mAdapterHelperRight.getSelectModelFromPosition(mCurrentIndexRight);
			mListener.onRightItemSelect(mCurrentIndexLeft, mCurrentIndexRight, leftModel, rightModel);
		}
	}

	private void removeOldRightSelect()
	{
		mAdapterHelperLeft.setPositionSelectState_left(mOldLeftIndex, mCurrentIndexRight, false);
		mAdapterHelperLeft.setPositionSelectState_left(mCurrentIndexLeft, mCurrentIndexRight, false);
	}

	public void setLeftAdapter(SD2LvCategoryViewHelperAdapterInterface adapter)
	{
		this.mAdapterHelperLeft = adapter;
		mLvLeft.setAdapter(mAdapterHelperLeft.getAdapter());
		mLvLeft.setOnItemClickListener(mLeftListViewItemClickListener);
	}

	public void setRightAdapter(SD2LvCategoryViewHelperAdapterInterface adapter)
	{
		this.mAdapterHelperRight = adapter;
		mLvRight.setAdapter(mAdapterHelperRight.getAdapter());
		mLvRight.setOnItemClickListener(mRightListViewItemClickListener);
	}

	public void setAdapterFinish()
	{
		initTitle();
	}

	private void initTitle()
	{
		if (getLeftItemCount() <= 0)
		{
			return;
		}

		int leftDefaultIndex = mAdapterHelperLeft.getTitleIndex();
		if (leftDefaultIndex >= 0)
		{
			mCurrentIndexLeft = leftDefaultIndex;
		} else
		{
			mCurrentIndexLeft = DEFAULT_INDEX;
		}

		setmTitle(mAdapterHelperLeft.getTitleNameFromPosition(mCurrentIndexLeft));
		mAdapterHelperLeft.setPositionSelectState(mCurrentIndexLeft, true, true);

		Object rightListModel = mAdapterHelperLeft.getRightListModelFromPosition_left(mCurrentIndexLeft);
		mAdapterHelperRight.updateRightListModel_right(rightListModel);

		if (getRightItemCount() > 0)
		{
			int rightDefaultIndex = mAdapterHelperRight.getTitleIndex();
			if (rightDefaultIndex >= 0)
			{
				mCurrentIndexRight = rightDefaultIndex;
			} else
			{
				mCurrentIndexRight = DEFAULT_INDEX;
			}
			mAdapterHelperRight.setPositionSelectState(mCurrentIndexRight, true, true);
			mLvRight.setSelection(mCurrentIndexRight);
		}
		mLvLeft.setSelection(mCurrentIndexLeft);
	}

	public interface SD2LvCategoryViewHelperListener
	{
		public void onRightItemSelect(int leftIndex, int rightIndex, Object leftModel, Object rightModel);

		public void onLeftItemSelect(int leftIndex, Object leftModel, boolean isNotifyDirect);

		public void onTitleChange(String title);
	}

	public interface SD2LvCategoryViewHelperAdapterInterface extends SDLvCategoryViewHelperAdapterInterface
	{
		public Object getRightListModelFromPosition_left(int position);

		public void updateRightListModel_right(Object rightListModel);

		public void setPositionSelectState_left(int positionLeft, int positionRight, boolean select);
	}

}
