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
import com.fanwe.library.view.SDLvPoper.OnItemSelectedListener;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SDLvCategoryView<T> extends SDSelectViewAuto
{

	public ImageView mIvLeft;
	public ImageView mIvRight;
	public TextView mTvTitle;

	private SDLvPoper<T> mPoper = new SDLvPoper<T>();
	private SDLvCategoryViewListener mListener;

	public void setListener(SDLvCategoryViewListener listener)
	{
		this.mListener = listener;
	}

	public void setAdapter(SDAdapter<T> adapter)
	{
		mPoper.setAdapter(adapter);
	}

	public void setTitle(String title)
	{
		SDViewBinder.setTextView(mTvTitle, title);
	}

	public SDLvCategoryView(Context context)
	{
		this(context, null);
	}

	public SDLvCategoryView(Context context, AttributeSet attrs)
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

	public SDLvPoper<T> getPoper()
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
			public void onItemSelected(int position, boolean notify)
			{
				dismissPop();
				updateTitleBySelectedItem();
				if (notify)
				{
					if (mListener != null)
					{
						mListener.onItemSelected(position);
					}
				}
			}
		});
	}

	public void setOnDismissListener(OnDismissListener onDismissListener)
	{
		mPoper.setOnDismissListener(onDismissListener);
	}

	public void updateTitleBySelectedItem()
	{
		T model = getSelectedItem();
		if (model != null)
		{
			setTitle(model.toString());
		}
	}

	public void setListViewId(int lvId)
	{
		mPoper.setListViewId(lvId);
	}

	public void setPopView(int resId)
	{
		mPoper.setContentView(resId);
	}

	public void setSelected(int index, boolean notify)
	{
		mPoper.setSelected(index, notify);
	}

	public T getSelectedItem()
	{
		return mPoper.getSelectedItem();
	}

	public void showPop()
	{
		mPoper.showAsDropDown(this, 0, 0);
	}

	public void dismissPop()
	{
		mPoper.dismiss();
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

	public interface SDLvCategoryViewListener
	{
		public void onItemSelected(int position);
	}

}
