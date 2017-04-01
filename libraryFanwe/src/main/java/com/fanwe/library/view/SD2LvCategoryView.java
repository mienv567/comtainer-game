package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.SD2LvPoper.DataOperater;
import com.fanwe.library.view.SD2LvPoper.NotifyDirecter;
import com.fanwe.library.view.SD2LvPoper.OnItemSelectedListener;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SD2LvCategoryView<L, M> extends SDSelectViewAuto
{

	public ImageView mIvLeft;
	public ImageView mIvRight;
	public TextView mTvTitle;

	private SD2LvPoper<L, M> mPoper = new SD2LvPoper<L, M>();
	private SD2LvCategoryViewListener mListener;

	public void setNotifyDirecter(NotifyDirecter notifyDirecter)
	{
		mPoper.setNotifyDirecter(notifyDirecter);
	}

	public void setListener(SD2LvCategoryViewListener listener)
	{
		this.mListener = listener;
	}

	public void setAdapterLeft(SDAdapter<L> adapter)
	{
		mPoper.setAdapterLeft(adapter);
	}

	public void setAdapterRight(SDAdapter<M> adapter)
	{
		mPoper.setAdapterRight(adapter);
	}

	public SDAdapter<L> getAdapterLeft()
	{
		return mPoper.getAdapterLeft();
	}

	public SDAdapter<M> getAdapterRight()
	{
		return mPoper.getAdapterRight();
	}

	public void setTitle(String title)
	{
		SDViewBinder.setTextView(mTvTitle, title);
	}

	public SD2LvCategoryView(Context context)
	{
		this(context, null);
	}

	public SD2LvCategoryView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	@Override
	protected void init()
	{
		setContentView(R.layout.view_category);
		mTvTitle = (TextView) findViewById(R.id.view_category_tab_tv_title);
		mIvLeft = (ImageView) findViewById(R.id.view_category_tab_iv_left);
		mIvRight = (ImageView) findViewById(R.id.view_category_tab_iv_right);
		addAutoView(this, mTvTitle, mIvLeft, mIvRight);

		setDefaultConfig();
		initPoper();
		setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				toggleSelected();
			}
		});
		onNormal();
	}

	@Override
	public void setDefaultConfig()
	{
		getViewConfig(mTvTitle).setTextColorNormal(Color.GRAY).setTextColorSelected(mLibraryConfig.getMainColor());
		super.setDefaultConfig();
	}

	public void setDataOperater(DataOperater dataOperater)
	{
		mPoper.setDataOperater(dataOperater);
	}

	public SD2LvPoper<L, M> getPoper()
	{
		return mPoper;
	}

	private void initPoper()
	{

		setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				setStateNormal();
			}
		});

		mPoper.setListenerOnItemSelected(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected_left(int position, boolean notify, boolean notifyDirectly)
			{
				if (notifyDirectly)
				{
					updateTitleBySelectedItemlLeft();
				}

				if (notify)
				{
					if (mListener != null)
					{
						mListener.onItemSelected_left(position, notifyDirectly);
					}
				}
			}

			@Override
			public void onItemSelected_right(int positionLeft, int positionRight, boolean notify)
			{
				dismissPop();
				updateTitleBySelectedItemRight();

				if (notify)
				{
					if (mListener != null)
					{
						mListener.onItemSelected_right(positionLeft, positionRight);
					}
				}
			}
		});
	}

	public void setOnDismissListener(OnDismissListener onDismissListener)
	{
		mPoper.setOnDismissListener(onDismissListener);
	}

	public void updateTitleBySelectedItemRight()
	{
		M model = getSelectedItemRight();
		if (model != null)
		{
			setTitle(model.toString());
		}
	}

	public void updateTitleBySelectedItemlLeft()
	{
		L model = getSelectedItemLeft();
		if (model != null)
		{
			setTitle(model.toString());
		}
	}

	public L getSelectedItemLeft()
	{
		return mPoper.getSelectedItemLeft();
	}

	public M getSelectedItemRight()
	{
		return mPoper.getSelectedItemRight();
	}

	public void setListViewIdLeft(int lvId)
	{
		mPoper.setListViewIdLeft(lvId);
	}

	public void setListViewIdRight(int lvId)
	{
		mPoper.setListViewIdRight(lvId);
	}

	public void setPopView(int resId)
	{
		mPoper.setContentView(resId);
	}

	public void setSelected(int indexLeft, int indexRight, boolean notify)
	{
		mPoper.setSelected(indexLeft, indexRight, notify);
	}

	public void showPop()
	{
		mPoper.showAsDropDown(this, 0, 0);
	}

	public void dismissPop()
	{
		mPoper.dismiss();
	}

	public void bindData()
	{
		mPoper.bindData();
	}

	@Override
	public void onNormal()
	{
		dismissPop();
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		showPop();
		super.onSelected();
	}

	public interface SD2LvCategoryViewListener
	{
		public void onItemSelected_left(int position, boolean notifyDirectly);

		public void onItemSelected_right(int positionLeft, int positionRight);
	}

}
