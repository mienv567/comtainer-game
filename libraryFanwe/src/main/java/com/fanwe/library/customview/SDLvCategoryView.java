package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.customview.SDLvCategoryViewHelper.SDLvCategoryViewHelperAdapterInterface;
import com.fanwe.library.customview.SDLvCategoryViewHelper.SDLvCategoryViewHelperListener;
import com.fanwe.library.popupwindow.SDPopupWindow;

public class SDLvCategoryView extends SDViewBase
{

	private SDLvCategoryViewHelper mHelper;

	public ImageView mIvLeft = null;
	public ImageView mIvRight = null;
	public TextView mTvTitle = null;

	public PopupWindow mPopCategory = null;

	private SDLvCategoryViewListener mListener = null;

	// -----------------get set

	public SDLvCategoryViewListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDLvCategoryViewListener mListener)
	{
		this.mListener = mListener;
	}

	public void setTitle(String title)
	{
		if (title != null)
		{
			this.mTvTitle.setText(title);
		}
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

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_category_tab, this, true);
		mTvTitle = (TextView) this.findViewById(R.id.view_category_tab_tv_title);
		mIvLeft = (ImageView) this.findViewById(R.id.view_category_tab_iv_left);
		mIvRight = (ImageView) this.findViewById(R.id.view_category_tab_iv_right);

		View popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_category_single_lv, null);
		ListView listView = (ListView) popView.findViewById(R.id.pop_category_single_lv_lv);
		initPopwindow(popView);

		mHelper = new SDLvCategoryViewHelper(listView);
		mHelper.setmListener(new SDLvCategoryViewHelperListener()
		{

			@Override
			public void onTitleChange(String title)
			{
				mTvTitle.setText(title);
			}

			@Override
			public void onItemSelect(int index, Object model)
			{
				dismissPopwindow();
				if (mListener != null)
				{
					mListener.onItemSelect(index, model);
				}
			}
		});
	}

	private void initPopwindow(View popView)
	{
		mPopCategory = new SDPopupWindow();
		mPopCategory.setContentView(popView);
		mPopCategory.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				onNormal();
			}
		});
	}

	public void showPopwindow()
	{
		mPopCategory.showAsDropDown(this, 0, 0);
	}

	public void dismissPopwindow()
	{
		mPopCategory.dismiss();
	}

	public void setAdapter(SDLvCategoryViewHelperAdapterInterface adapter)
	{
		mHelper.setAdapter(adapter);
	}

	@Override
	public void onNormal()
	{
		setViewBackgroundNormal(this);
		setTextColorNormal(mTvTitle);
		dismissPopwindow();
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setViewBackgroundSelected(this);
		setTextColorSelected(mTvTitle);
		showPopwindow();
		super.onSelected();
	}

	public interface SDLvCategoryViewListener
	{
		public void onItemSelect(int index, Object model);
	}

}
