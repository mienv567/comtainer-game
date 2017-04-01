package com.fanwe.library.view;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

import com.fanwe.library.R;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.popupwindow.SDPopupWindow;
import com.fanwe.library.utils.SDToast;

public class SD2LvPoper<L, M>
{

	private SDPopupWindow mPop;
	private ListView mLvLeft;
	private ListView mLvRight;
	private SDAdapter<L> mAdapterLeft;
	private SDAdapter<M> mAdapterRight;

	private int mResIdLvLeft;
	private int mResIdLvRight;

	/** 当前左边选中位置 */
	private int mLeftIndexCurrent = -1;
	/** 上一次左边选中位置 */
	private int mLeftIndexOld = -1;

	/** 右边选中的时候左边选中的位置 */
	private int mLeftIndexWhenRightSelected = -1;
	/** 当前右边选中的位置 */
	private int mRightIndexCurrent = -1;

	private OnItemSelectedListener mListenerOnItemSelected;
	private DataOperater mDataOperater;
	private NotifyDirecter mNotifyDirecter;

	public void setNotifyDirecter(NotifyDirecter notifyDirecter)
	{
		if (notifyDirecter != null)
		{
			this.mNotifyDirecter = notifyDirecter;
		}
	}

	public void setDataOperater(DataOperater dataOperater)
	{
		if (dataOperater != null)
		{
			this.mDataOperater = dataOperater;
		}
	}

	public void setListenerOnItemSelected(OnItemSelectedListener listenerOnItemSelected)
	{
		this.mListenerOnItemSelected = listenerOnItemSelected;
	}

	public SD2LvPoper()
	{
		init();
	}

	private void init()
	{
		mPop = new SDPopupWindow();
		setContentView(R.layout.pop_two_lv);
		setListViewIdLeft(R.id.lv_left);
		setListViewIdRight(R.id.lv_right);
		initDefaultDataOperater();
		initDefaultNotifyDirecter();
	}

	private void initDefaultNotifyDirecter()
	{
		mNotifyDirecter = new NotifyDirecter()
		{

			@Override
			public boolean isNotifyDirectly()
			{
				return false;
			}
		};
	}

	private void initDefaultDataOperater()
	{
		mDataOperater = new DataOperater()
		{

			@Override
			public void setSelected(int positionLeft, int positionRight, boolean selected)
			{
				if (mAdapterLeft instanceof DataOperater)
				{
					DataOperater dataOperater = (DataOperater) mAdapterLeft;
					dataOperater.setSelected(positionLeft, positionRight, selected);
				}
			}

			@Override
			public Object getDataForRightOnLeftSelected(int positionLeft)
			{
				Object object = null;
				if (mAdapterLeft instanceof DataOperater)
				{
					DataOperater dataOperater = (DataOperater) mAdapterLeft;
					object = dataOperater.getDataForRightOnLeftSelected(positionLeft);
				}
				return object;
			}
		};
	}

	public void setAdapterLeft(SDAdapter<L> adapter)
	{
		if (adapter != null)
		{
			this.mAdapterLeft = adapter;
			findListViewLeft();
		}
	}

	public void setAdapterRight(SDAdapter<M> adapter)
	{
		if (adapter != null)
		{
			this.mAdapterRight = adapter;
			findListViewRight();
		}
	}

	public SDAdapter<L> getAdapterLeft()
	{
		return mAdapterLeft;
	}

	public SDAdapter<M> getAdapterRight()
	{
		return mAdapterRight;
	}

	public L getSelectedItemLeft()
	{
		return mAdapterLeft.getSelectManager().getSelectedItem();
	}

	public int getSelectedIndexLeft()
	{
		return mAdapterLeft.getSelectManager().getSelectedIndex();
	}

	public M getSelectedItemRight()
	{
		return mAdapterRight.getSelectManager().getSelectedItem();
	}

	public int getSelectedIndexRight()
	{
		return mAdapterRight.getSelectManager().getSelectedIndex();
	}

	public void setOnDismissListener(OnDismissListener onDismissListener)
	{
		mPop.setOnDismissListener(onDismissListener);
	}

	public void bindData()
	{
		resetLeftIndexCurrentAndOld();
		resetLeftRightIndexWhenLastRightSelected();
		// left
		mLvLeft.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				setSelected((int) id, -1, true);
			}
		});
		mLvLeft.setAdapter(mAdapterLeft);

		// right
		mLvRight.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				setSelected(-1, (int) id, true);
			}
		});
		mLvRight.setAdapter(mAdapterRight);
	}

	public void removeOldRightSelected()
	{
		if (mDataOperater != null)
		{
			if (mLeftIndexWhenRightSelected >= 0 && mRightIndexCurrent >= 0)
			{
				mDataOperater.setSelected(mLeftIndexWhenRightSelected, mRightIndexCurrent, false);
				if (mLeftIndexWhenRightSelected == mLeftIndexCurrent)
				{
					mAdapterRight.getSelectManager().setSelected(mRightIndexCurrent, false);
				}
			}
			resetLeftRightIndexWhenLastRightSelected();
		}
	}

	private void resetLeftRightIndexWhenLastRightSelected()
	{
		mLeftIndexWhenRightSelected = -1;
		mRightIndexCurrent = -1;
	}

	private void resetLeftIndexCurrentAndOld()
	{
		mLeftIndexCurrent = -1;
		mLeftIndexOld = -1;
	}

	public int getLeftIndexCurrent()
	{
		return mLeftIndexCurrent;
	}

	public int getLeftIndexOld()
	{
		return mLeftIndexOld;
	}

	public int getRightIndexCurrent()
	{
		return mRightIndexCurrent;
	}

	public int getLeftIndexWhenRightSelected()
	{
		return mLeftIndexWhenRightSelected;
	}

	public void setSelected(int indexLeft, int indexRight, boolean notify)
	{
		// left
		if (mAdapterLeft.isPositionLegal(indexLeft))
		{
			mLeftIndexOld = mLeftIndexCurrent;
			mLeftIndexCurrent = indexLeft;

			mAdapterLeft.getSelectManager().setSelected(mLeftIndexCurrent, true);

			if (mDataOperater != null)
			{
				List<M> listDataRight = (List<M>) mDataOperater.getDataForRightOnLeftSelected(mLeftIndexCurrent);
				mAdapterRight.updateData(listDataRight);
				// 如果更新数据后adapter会把上次的选中位置清除掉，那么如果上次有选中要让它重新选中
			}

			boolean notifyDirectly = mNotifyDirecter.isNotifyDirectly();

			if (notifyDirectly)
			{
				removeOldRightSelected();
				dismiss();
			}

			if (mListenerOnItemSelected != null)
			{
				mListenerOnItemSelected.onItemSelected_left(mLeftIndexCurrent, notify, notifyDirectly);
			}

			if (notifyDirectly)
			{
				return;
			}
		}

		// right
		if (mAdapterRight.isPositionLegal(indexRight))
		{
			// 把最后一次选中的位置还原为未选中
			removeOldRightSelected();

			mLeftIndexWhenRightSelected = mLeftIndexCurrent;
			mRightIndexCurrent = indexRight;

			mAdapterRight.getSelectManager().setSelected(mRightIndexCurrent, true);

			if (mListenerOnItemSelected != null)
			{
				mListenerOnItemSelected.onItemSelected_right(mLeftIndexCurrent, mRightIndexCurrent, notify);
			}
		}
	}

	public void dismiss()
	{
		mPop.dismiss();
	}

	public void showAsDropDown(View anchor)
	{
		mPop.showAsDropDown(anchor);
	}

	public void showAsDropDown(View anchor, int xoff, int yoff)
	{
		mPop.showAsDropDown(anchor, xoff, yoff);
	}

	public SDPopupWindow getPop()
	{
		return mPop;
	}

	public void setContentView(int resId)
	{
		mPop.setContentView(resId);
	}

	public void setContentView(View popView)
	{
		mPop.setContentView(popView);
	}

	public void setListViewIdLeft(int lvId)
	{
		this.mResIdLvLeft = lvId;
	}

	private boolean findListViewLeft()
	{
		boolean found = false;
		try
		{
			mLvLeft = (ListView) mPop.getContentView().findViewById(mResIdLvLeft);
			found = true;
		} catch (Exception e)
		{
			SDToast.showToast("未找到listview");
		}
		return found;
	}

	public void setListViewIdRight(int lvId)
	{
		this.mResIdLvRight = lvId;
	}

	private boolean findListViewRight()
	{
		boolean found = false;
		try
		{
			mLvRight = (ListView) mPop.getContentView().findViewById(mResIdLvRight);
			found = true;
		} catch (Exception e)
		{
			SDToast.showToast("未找到listview");
		}
		return found;
	}

	public View getContentView()
	{
		return mPop.getContentView();
	}

	public interface DataOperater
	{
		public Object getDataForRightOnLeftSelected(int positionLeft);

		public void setSelected(int positionLeft, int positionRight, boolean selected);
	}

	public interface OnItemSelectedListener
	{
		public void onItemSelected_left(int position, boolean notify, boolean notifyDirectly);

		public void onItemSelected_right(int positionLeft, int positionRight, boolean notify);
	}

	public interface NotifyDirecter
	{
		public boolean isNotifyDirectly();
	}

}
