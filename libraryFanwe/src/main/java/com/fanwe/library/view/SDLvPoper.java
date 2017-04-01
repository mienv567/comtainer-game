package com.fanwe.library.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

import com.fanwe.library.R;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.popupwindow.SDPopupWindow;
import com.fanwe.library.utils.SDToast;

public class SDLvPoper<T>
{

	private SDPopupWindow mPop;
	private ListView mLv;
	private SDAdapter<T> mAdapter;

	private int mResIdLv;

	private OnItemSelectedListener mListenerOnItemSelected;

	public SDLvPoper()
	{
		init();
	}

	private void init()
	{
		mPop = new SDPopupWindow();
		setContentView(R.layout.pop_single_lv);
		setListViewId(R.id.lv);
	}

	public void setListenerOnItemSelected(OnItemSelectedListener listenerOnItemSelected)
	{
		this.mListenerOnItemSelected = listenerOnItemSelected;
	}

	public void setAdapter(SDAdapter<T> adapter)
	{
		if (adapter != null)
		{
			this.mAdapter = adapter;
			if (findListView())
			{
				bindData();
			}
		}
	}

	private boolean findListView()
	{
		boolean found = false;
		try
		{
			mLv = (ListView) mPop.getContentView().findViewById(mResIdLv);
			found = true;
		} catch (Exception e)
		{
			SDToast.showToast("未找到listview");
		}
		return found;
	}

	public SDAdapter<?> getAdapter()
	{
		return mAdapter;
	}

	public T getSelectedItem()
	{
		return mAdapter.getSelectManager().getSelectedItem();
	}

	public int getSelectedIndex()
	{
		return mAdapter.getSelectManager().getSelectedIndex();
	}

	public void setOnDismissListener(OnDismissListener onDismissListener)
	{
		mPop.setOnDismissListener(onDismissListener);
	}

	private void bindData()
	{
		mLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				setSelected((int) id, true);
			}
		});
		mLv.setAdapter(mAdapter);
	}

	public void setSelected(int index, boolean notify)
	{
		mAdapter.getSelectManager().setSelected(index, true);
		if (mListenerOnItemSelected != null)
		{
			mListenerOnItemSelected.onItemSelected(index, notify);
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

	public View getContentView()
	{
		return mPop.getContentView();
	}

	public void setContentView(int resId)
	{
		mPop.setContentView(resId);
	}

	public void setContentView(View popView)
	{
		mPop.setContentView(popView);
	}

	public void setListViewId(int lvId)
	{
		this.mResIdLv = lvId;
	}

	public interface OnItemSelectedListener
	{
		public void onItemSelected(int position, boolean notify);
	}

}
