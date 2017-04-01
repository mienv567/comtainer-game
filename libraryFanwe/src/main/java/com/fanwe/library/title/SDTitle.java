package com.fanwe.library.title;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.fanwe.library.R;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.title.TitleItem.EnumShowType;
import com.fanwe.library.utils.SDViewUtil;

public class SDTitle extends LinearLayout implements OnClickListener
{
	private static final int MAX_ITEM_COUNT = 2;

	public View mView;
	public LinearLayout mLlLeft;
	public LinearLayout mLlMiddle;
	public LinearLayout mLlRight;
	private SDTitleListener mListener;
	private SDTitleMoreClickListener mListenerMoreClick;

	private TitleItem mItemLeft;
	private TitleItem mItemMiddle;
	private List<TitleItem> mListTitleItem = new ArrayList<TitleItem>();
	private List<TitleItem> mListTitleItemRight = new ArrayList<TitleItem>();
	private List<TitleItem> mListTitleItemRightMore = new ArrayList<TitleItem>();
	private SDLibraryConfig mConfig = SDLibrary.getInstance().getConfig();

	private int mWidthAvailable;
	private int mMaxItemRightCount;
	private int mIndexMore;
	private int mWidthMllRight;
	private int mWidthMllMiddle;

	private TitleItem mItemMore;
	private EnumTitleMode mMode = EnumTitleMode.AUTOMATIC;
	/** 为以后改变测量方法做准备，目前只有一种默认测量方法 */
	private EnumTitleMeasureMode mModeMeasure = EnumTitleMeasureMode.DEFAULT;

	public SDTitleMoreClickListener getmListenerMoreClick()
	{
		return mListenerMoreClick;
	}

	public void setmListenerMoreClick(SDTitleMoreClickListener mListenerMoreClick)
	{
		this.mListenerMoreClick = mListenerMoreClick;
	}

	public int getmMaxItemRightCount()
	{
		return mMaxItemRightCount;
	}

	public void setmMaxItemRightCount(int mMaxItemRightCount)
	{
		if (mMaxItemRightCount > 0)
		{
			this.mMaxItemRightCount = mMaxItemRightCount;
			mIndexMore = mMaxItemRightCount - 1;
			if (mMode == EnumTitleMode.COUNT) // 如果是COUNT模式，调用doneRight方法
			{
				doneRight();
			}
		}
	}

	public EnumTitleMeasureMode getmModeMeasure()
	{
		return mModeMeasure;
	}

	public void setmModeMeasure(EnumTitleMeasureMode mModeMeasure)
	{
		if (mModeMeasure != null)
		{
			this.mModeMeasure = mModeMeasure;
			doneRight();
		}
	}

	public EnumTitleMode getmMode()
	{
		return mMode;
	}

	public void setmMode(EnumTitleMode mMode)
	{
		if (mMode != null)
		{
			this.mMode = mMode;
			doneRight();
		}
	}

	public TitleItem getmItemLeft()
	{
		return mItemLeft;
	}

	public TitleItem getmItemMiddle()
	{
		return mItemMiddle;
	}

	public TitleItem getmItemMore()
	{
		return mItemMore;
	}

	public SDTitleListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDTitleListener mListener)
	{
		this.mListener = mListener;
	}

	public SDTitle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SDTitle(Context context)
	{
		this(context, null);
	}

	private void init()
	{
		mView = LayoutInflater.from(getContext()).inflate(R.layout.title, null);
		setHeightTitle(LayoutParams.MATCH_PARENT);

		mLlLeft = (LinearLayout) mView.findViewById(R.id.title_ll_left);
		mLlMiddle = (LinearLayout) mView.findViewById(R.id.title_ll_middle);
		mLlRight = (LinearLayout) mView.findViewById(R.id.title_ll_right);
		createSimpleTitle();
		setmMaxItemRightCount(MAX_ITEM_COUNT);
		setmListenerMoreClick(new SDDefaultMoreClickListener());
		addLayoutListener();
		setDefaultConfig();
	}

	private void addLayoutListener()
	{
		mLlMiddle.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				int newWidth = mLlMiddle.getWidth();
				if (mWidthMllMiddle != newWidth)
				{
					mWidthMllMiddle = newWidth;
					doneRight();
				}
			}
		});
		mLlRight.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				int newWidth = mLlRight.getWidth();
				if (mWidthMllRight != newWidth)
				{
					mWidthMllRight = newWidth;
					doneRight();
				}
			}
		});
	}

	public SDTitle removeAllItems()
	{
		mLlLeft.removeAllViews();
		mLlMiddle.removeAllViews();
		removeAllRightItems();
		return this;
	}

	public SDTitle removeAllRightItems()
	{
		mLlRight.removeAllViews();
		mListTitleItem.clear();
		mListTitleItemRight.clear();
		mListTitleItemRightMore.clear();
		return this;
	}

	public SDTitle createSimpleTitle()
	{
		removeAllItems();
		addItemLeftDefault();
		addItemMiddleDefault();
		return this;
	}

	public TitleItem addItemLeftDefault()
	{
		mItemLeft = new TitleItem(getContext());
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		mLlLeft.addView(mItemLeft, params);
		LinearLayout llView = (LinearLayout) mItemLeft.mView;
		llView.setPadding(SDViewUtil.dp2px(10), 0, SDViewUtil.dp2px(10), 0);

		SDViewUtil.setBackgroundDrawable(mItemLeft, getSelectorDrawable());
		mItemLeft.setOnClickListener(this);
		mItemLeft.setAllViewsVisibility(View.GONE);
		return mItemLeft;
	}

	public TitleItem addItemMiddleDefault()
	{
		mItemMiddle = new TitleItem(getContext());
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		mLlMiddle.addView(mItemMiddle, params);

		mItemMiddle.setOnClickListener(this);
		mItemMiddle.setAllViewsVisibility(View.GONE);
		return mItemMiddle;
	}

	public TitleItemConfig addItemRight()
	{
		TitleItemConfig config = new TitleItemConfig();
		config.setOnClickListener(this);

		TitleItem tlRight = new TitleItem(getContext());
		tlRight.setOnClickListener(this);
		tlRight.setTag(config);
		tlRight.setmConfig(config);
		SDViewUtil.setBackgroundDrawable(tlRight, getSelectorDrawable());

		mListTitleItem.add(tlRight);
		// 这句很重要，为了引起该布局状态变化造成回调后调用doneRight方法，可以省去手动调用doneRight方法
		mLlRight.addView(tlRight);
		return config;
	}

	public SDTitle removeItemRight(TitleItem item)
	{
		if (item != null && mListTitleItem.contains(item))
		{
			mListTitleItem.remove(item);
			doneRight();
		}
		return this;
	}

	public SDTitle removeItemRight(int index)
	{
		removeItemRight(getItemRight(index));
		return this;
	}

	private TitleItem getItemRight(int index)
	{
		if (index >= 0 && index < mListTitleItem.size())
		{
			return mListTitleItem.get(index);
		}
		return null;
	}

	public TitleItemConfig getItemRightConfig(int index)
	{
		TitleItem item = getItemRight(index);
		if (item != null)
		{
			return item.getmConfig();
		}
		return null;
	}

	public int indexOfItemRight(TitleItem model)
	{
		if (model != null)
		{
			return mListTitleItem.indexOf(model);
		}
		return -1;
	}

	public int indexOfItemRightConfig(TitleItemConfig config)
	{
		if (config != null)
		{
			return indexOfItemRight(config.getTitleItem());
		}
		return -1;
	}

	/**
	 * 所有设置完成，根据设置改变视图
	 */
	public SDTitle doneRight()
	{
		findMaxItemRightCount();
		splitList();
		addRightViews();
		return this;
	}

	/**
	 * 添加右边的按钮，包括可能添加更多按钮
	 */
	private void addRightViews()
	{
		mLlRight.removeAllViews();
		for (TitleItem item : mListTitleItemRight)
		{
			addItemToRight(item);
		}
		if (mItemMore != null)
		{
			addItemToRight(mItemMore);
		}
	}

	private void addItemToRight(TitleItem item)
	{
		item.mIsAddToMore = false;
		if (item.mHasViewVisible)
		{
			mLlRight.addView(item);
		}
	}

	private void findMaxItemRightCount()
	{
		switch (mMode)
		{
		case AUTOMATIC:
			findMaxItemRightCountByModeAutomatic();
			break;
		case COUNT:
			//
			break;

		default:
			break;
		}

	}

	private void findMaxItemRightCountByModeAutomatic()
	{
		mWidthAvailable = getAvailableWidth();
		if (mWidthAvailable > 0)
		{
			int currentWidth = 0;
			int size = mListTitleItem.size();
			TitleItem item = null;
			for (int i = 0; i < size; i++)
			{
				item = mListTitleItem.get(i);
				currentWidth += SDViewUtil.getViewWidth(item);
				setmMaxItemRightCount(i + 1);
				if (currentWidth > mWidthAvailable)
				{
					setmMaxItemRightCount(i);
					break;
				}
			}
			if (mMaxItemRightCount == 0)
			{
				TitleItem moreItem = createMoreTitleItem();
				int moreWidth = SDViewUtil.getViewWidth(moreItem);
				if (moreWidth <= mWidthAvailable)
				{
					setmMaxItemRightCount(1);
				} else
				{
					int leftWidth = mWidthAvailable - moreWidth;
					SDViewUtil.setViewWidth(moreItem, leftWidth);
				}
			}
		}
		Log.i(getClass().getName(), "mWidthAvailable:" + mWidthAvailable + ",mMaxItemCount:" + mMaxItemRightCount + ",mIndexMore:" + mIndexMore);
	}

	private int getAvailableWidth()
	{
		int availableWidth = 0;
		switch (mModeMeasure)
		{
		case DEFAULT:
			availableWidth = (SDViewUtil.getViewWidth(this) - SDViewUtil.getViewWidth(mLlMiddle)) / 2;
			break;
		default:
			break;
		}
		return availableWidth;
	}

	private void splitList()
	{
		mListTitleItemRight.clear();
		mListTitleItemRightMore.clear();
		mItemMore = null;
		int size = mListTitleItem.size();
		if (size > 0)
		{
			if (size <= mMaxItemRightCount)
			{
				mListTitleItemRight.addAll(mListTitleItem);
			} else
			{
				TitleItem item = null;
				for (int i = 0; i < size; i++)
				{
					item = mListTitleItem.get(i);
					if (i < mIndexMore)
					{
						mListTitleItemRight.add(item);
					} else if (i == mIndexMore)
					{
						// TODO 添加一个更多的按钮
						createMoreTitleItem();

						// 更多里面要添加该项
						mListTitleItemRightMore.add(item);
					} else
					{
						mListTitleItemRightMore.add(item);
					}
				}
			}
		}
	}

	private TitleItem createMoreTitleItem()
	{
		TitleItemConfig moreModel = addItemRight();
		moreModel.setImageLeftResId(R.drawable.ic_more_vertical).setShowType(EnumShowType.ICON);
		mItemMore = getItemRight(mListTitleItem.size() - 1);
		mListTitleItem.remove(mItemMore);
		return mItemMore;
	}

	// ---------------------setCustomView
	public SDTitle setCustomViewLeft(View view, LinearLayout.LayoutParams params)
	{
		mLlLeft.removeAllViews();
		mLlLeft.addView(view, params);
		return this;
	}

	public SDTitle setCustomViewMiddle(View view, LinearLayout.LayoutParams params)
	{
		mLlMiddle.removeAllViews();
		mLlMiddle.addView(view, params);
		return this;
	}

	public SDTitle setCustomViewRight(View view, LinearLayout.LayoutParams params)
	{
		removeAllRightItems();
		mLlRight.addView(view, params);
		return this;
	}

	public SDTitle setHeightTitle(int height)
	{
		this.removeAllViews();
		this.addView(mView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height, 1));
		return this;
	}

	private void setDefaultConfig()
	{
		setBackgroundColor(mColorCreater.getColorTitle());
		setHeightTitle(mConfig.getTitleHeight());
	}

	private Drawable getSelectorDrawable()
	{
		SDDrawable none = new SDDrawable();
		none.color(mColorCreater.getColorNormal());

		SDDrawable pressed = new SDDrawable();
		pressed.color(mColorCreater.getColorPress());

		return SDDrawable.getStateListDrawable(none, null, null, pressed);
	}

	public void updateTitleColor()
	{
		setBackgroundColor(mColorCreater.getColorTitle());
		if (mItemLeft != null)
		{
			SDViewUtil.setBackgroundDrawable(mItemLeft, getSelectorDrawable());
			for (TitleItem item : mListTitleItem)
			{
				SDViewUtil.setBackgroundDrawable(item, getSelectorDrawable());
			}
		}
	}

	private SDTitleColorCreater mColorCreater = new SDTitleColorCreater()
	{

		@Override
		public int getColorPress()
		{
			return mConfig.getTitleColorPressed();
		}

		@Override
		public int getColorNormal()
		{
			return mConfig.getTitleColor();
		}

		@Override
		public int getColorTitle()
		{
			return mConfig.getTitleColor();
		}
	};

	public void setmColorCreater(SDTitleColorCreater colorCreater)
	{
		if (colorCreater != null)
		{
			this.mColorCreater = colorCreater;
			updateTitleColor();
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == mItemLeft)
		{
			if (mListener != null)
			{
				mListener.onLeftClick_SDTitleListener(mItemLeft);
			}
		} else if (v == mItemMiddle)
		{
			if (mListener != null)
			{
				mListener.onMiddleClick_SDTitleListener(mItemMiddle);
			}
		} else if (v == mItemMore)
		{
			clickMore(mItemMore);
		} else
		{
			if (mListener != null)
			{
				Object tag = v.getTag();
				if (tag != null && tag instanceof TitleItemConfig)
				{
					TitleItemConfig config = (TitleItemConfig) tag;
					int index = indexOfItemRightConfig(config);
					mListener.onRightClick_SDTitleListener(config, index, v);
				}
			}
			disMissMore();
		}
	}

	private void clickMore(TitleItem item)
	{
		if (mListenerMoreClick != null)
		{
			mListenerMoreClick.onMoreClick(item, mListTitleItemRightMore, this);
		}
	}

	public SDTitle showMore()
	{
		if (mListenerMoreClick != null)
		{
			mListenerMoreClick.showMore();
		}
		return this;
	}

	public SDTitle disMissMore()
	{
		if (mListenerMoreClick != null)
		{
			mListenerMoreClick.disMissMore();
		}
		return this;
	}

	// --------------------friendly method

	public TitleItemConfig addItemRight_TEXT(String text)
	{
		return addItemRight().setTextBot(text).setShowType(EnumShowType.TEXT);
	}

	public TitleItemConfig addItemRight_ICON(int resId)
	{
		return addItemRight().setImageLeftResId(resId).setShowType(EnumShowType.ICON);
	}

	public TitleItemConfig addItemRight_TEXT_WITH_BACKGROUND(int backgroundTextResId, String text)
	{
		return addItemRight().setBackgroundTextResId(backgroundTextResId).setTextBot(text).setShowType(EnumShowType.TEXT_WITH_BACKGROUND);
	}

	public SDTitle setMiddleTextTop(String text)
	{
		mItemMiddle.setTextTop(text);
		return this;
	}

	public SDTitle setMiddleTextBot(String text)
	{
		mItemMiddle.setTextBot(text);
		return this;
	}

	public SDTitle setMiddleImageLeft(int resId)
	{
		mItemMiddle.setImageLeft(resId);
		return this;
	}

	public SDTitle setMiddleImageRight(int resId)
	{
		mItemMiddle.setImageRight(resId);
		return this;
	}

	public SDTitle setMiddleBackgroundText(int resId)
	{
		mItemMiddle.setBackgroundText(resId);
		return this;
	}

	public SDTitle setLeftTextTop(String text)
	{
		mItemLeft.setTextTop(text);
		return this;
	}

	public SDTitle setLeftTextBot(String text)
	{
		mItemLeft.setTextBot(text);
		return this;
	}

	public SDTitle setLeftImageLeft(int resId)
	{
		mItemLeft.setImageLeft(resId);
		return this;
	}

	public SDTitle setLeftImageRight(int resId)
	{
		mItemLeft.setImageRight(resId);
		return this;
	}

	public SDTitle setLeftBackgroundText(int resId)
	{
		mItemLeft.setBackgroundText(resId);
		return this;
	}

	public interface SDTitleListener
	{
		public void onLeftClick_SDTitleListener(TitleItem item);

		public void onMiddleClick_SDTitleListener(TitleItem item);

		public void onRightClick_SDTitleListener(TitleItemConfig config, int index, View view);
	}

	public interface SDTitleMoreClickListener
	{
		public void onMoreClick(TitleItem itemMore, List<TitleItem> listItems, SDTitle title);

		public void showMore();

		public void disMissMore();
	}

	public interface SDTitleColorCreater
	{
		public int getColorTitle();

		public int getColorNormal();

		public int getColorPress();
	}

	public enum EnumTitleMode
	{
		/** (默认模式)自动模式，右边的按钮数量会根据测量结果显示 */
		AUTOMATIC,
		/** 指定右边的数量个数模式，只有超过右边的数量的时候才会用popwindow显示 */
		COUNT;
	}

	public enum EnumTitleMeasureMode
	{
		DEFAULT,
	}

}
