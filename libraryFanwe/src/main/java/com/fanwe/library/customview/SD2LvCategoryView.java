package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.customview.SD2LvCategoryViewHelper.SD2LvCategoryViewHelperAdapterInterface;
import com.fanwe.library.customview.SD2LvCategoryViewHelper.SD2LvCategoryViewHelperListener;
import com.fanwe.library.popupwindow.SDPopupWindow;

public class SD2LvCategoryView extends SDViewBase
{

	public ImageView mIvLeft = null;
	public ImageView mIvRight = null;
	public TextView mTvTitle = null;
	public ListView mLvLeft;
	public ListView mLvRight;

	public PopupWindow mPopCategory = null;

	private SD2LvCategoryViewHelper mHelper;

	private SD2LvCategoryViewListener mListener;

	// ----------------get set

	public void setmListener(SD2LvCategoryViewListener mListener)
	{
		this.mListener = mListener;
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

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_category_tab, this, true);
		mTvTitle = (TextView) this.findViewById(R.id.view_category_tab_tv_title);
		mIvLeft = (ImageView) this.findViewById(R.id.view_category_tab_iv_left);
		mIvRight = (ImageView) this.findViewById(R.id.view_category_tab_iv_right);

		View popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_category_two_lv, null);
		mLvLeft = (ListView) popView.findViewById(R.id.pop_category_two_lv_lv_left);
		mLvRight = (ListView) popView.findViewById(R.id.pop_category_two_lv_lv_right);
		setGravity(Gravity.CENTER_VERTICAL);

		initPopwindow(popView);

		mHelper = new SD2LvCategoryViewHelper(mLvLeft, mLvRight);
		mHelper.setmListener(new SD2LvCategoryViewHelperListener()
		{

			@Override
			public void onTitleChange(String title)
			{
				mTvTitle.setText(title);
			}

			@Override
			public void onRightItemSelect(int leftIndex, int rightIndex, Object leftModel, Object rightModel)
			{
				disMissPopWindow();
				if (mListener != null)
				{
					mListener.onRightItemSelect(leftIndex, rightIndex, leftModel, rightModel);
				}
			}

			@Override
			public void onLeftItemSelect(int leftIndex, Object leftModel, boolean isNotifyDirect)
			{
				if (isNotifyDirect)
				{
					disMissPopWindow();
				}
				if (mListener != null)
				{
					mListener.onLeftItemSelect(leftIndex, leftModel, isNotifyDirect);
				}
			}
		});
	}

	public void setLeftAdapter(SD2LvCategoryViewHelperAdapterInterface adapter)
	{
		mHelper.setLeftAdapter(adapter);
	}

	public void setRightAdapter(SD2LvCategoryViewHelperAdapterInterface adapter)
	{
		mHelper.setRightAdapter(adapter);
	}

	public void setAdapterFinish()
	{
		mHelper.setAdapterFinish();
	}

	public void setSelectIndex(int left, int right)
	{
		mHelper.setSelectIndex(left, right);
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

	public void showPopWindow()
	{
		mPopCategory.showAsDropDown(this, 0, 0);
	}

	public void disMissPopWindow()
	{
		mPopCategory.dismiss();
	}

	@Override
	public void onNormal()
	{
		setViewBackgroundNormal(this);
		setTextColorNormal(mTvTitle);
		disMissPopWindow();
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		setViewBackgroundSelected(this);
		setTextColorSelected(mTvTitle);
		showPopWindow();
		super.onSelected();
	}

	public interface SD2LvCategoryViewListener
	{
		public void onRightItemSelect(int leftIndex, int rightIndex, Object leftModel, Object rightModel);

		public void onLeftItemSelect(int leftIndex, Object leftModel, boolean isNotifyDirect);
	}

}
