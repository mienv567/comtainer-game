package com.fanwe.library.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class HorizontalScrollViewPageIndicator extends HorizontalScrollView implements OnPageChangeListener
{

	private ViewPager mViewPager;
	private Runnable mIconSelector;
	private LinearLayout mLlTabLayout;
	private BaseAdapter mAdapter;
	private OnTabItemSelectedListener mListenerOnTabItemSelected;
	private boolean mIsFirstSetCurrentItem = true;

	public void setListenerOnTabItemSelected(OnTabItemSelectedListener listenerOnTabItemSelected)
	{
		this.mListenerOnTabItemSelected = listenerOnTabItemSelected;
	}

	public HorizontalScrollViewPageIndicator(Context context)
	{
		this(context, null);
	}

	public HorizontalScrollViewPageIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);
		mLlTabLayout = new LinearLayout(context);
		mLlTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(mLlTabLayout);
	}

	public void setAdapter(BaseAdapter adapter)
	{
		this.mAdapter = adapter;
		mAdapter.registerDataSetObserver(new DataSetObserver()
		{
			@Override
			public void onChanged()
			{
				notifyDataSetChanged();
				super.onChanged();
			}
		});
		notifyDataSetChanged();
	}

	private void animateToTab(final int position)
	{
		final View iconView = mLlTabLayout.getChildAt(position);
		if (mIconSelector != null)
		{
			removeCallbacks(mIconSelector);
		}
		mIconSelector = new Runnable()
		{
			public void run()
			{
				final int scrollPos = iconView.getLeft() - (getWidth() - iconView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mIconSelector = null;
			}
		};
		post(mIconSelector);
	}

	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		if (mIconSelector != null)
		{
			post(mIconSelector);
		}
	}

	@Override
	public void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		if (mIconSelector != null)
		{
			removeCallbacks(mIconSelector);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
	}

	@Override
	public void onPageSelected(int position)
	{
		setCurrentItemTab(position);
	}

	public void setViewPager(ViewPager viewPager)
	{
		if (viewPager == null)
		{
			return;
		}
		if (mViewPager == viewPager)
		{
			return;
		}
		mViewPager = viewPager;
		mViewPager.addOnPageChangeListener(this);
	}

	public void notifyDataSetChanged()
	{
		mLlTabLayout.removeAllViews();
		int count = mAdapter.getCount();
		for (int i = 0; i < count; i++)
		{
			View view = mAdapter.getView(i, null, mLlTabLayout);
			view.setOnClickListener(new DefaultOnClickTabItemListener(i));
			mLlTabLayout.addView(view);
		}
		// requestLayout();
	}

	private class DefaultOnClickTabItemListener implements View.OnClickListener
	{
		private int nIndex;

		public DefaultOnClickTabItemListener(int index)
		{
			nIndex = index;
		}

		@Override
		public void onClick(View v)
		{
			setCurrentItem(nIndex);
		}
	}

	private boolean isPositionLegal(int position)
	{
		boolean legal = false;
		if (position >= 0 && mAdapter != null && position < mAdapter.getCount())
		{
			legal = true;
		}
		return legal;

	}

	public void setCurrentItem(int position)
	{
		if (!isPositionLegal(position))
		{
			return;
		}
		if (mViewPager != null)
		{
			if (mIsFirstSetCurrentItem && position == mViewPager.getCurrentItem())
			{
				mIsFirstSetCurrentItem = false;
				setCurrentItemTab(position);
			}
			mViewPager.setCurrentItem(position);
		} else
		{
			setCurrentItemTab(position);
		}
	}

	private void setCurrentItemTab(int position)
	{
		if (!isPositionLegal(position))
		{
			return;
		}
		animateToTab(position);
		if (mListenerOnTabItemSelected != null)
		{
			mListenerOnTabItemSelected.onSelected(mLlTabLayout.getChildAt(position), position);
		}
	}

	public interface OnTabItemSelectedListener
	{
		public void onSelected(View v, int index);
	}

}
