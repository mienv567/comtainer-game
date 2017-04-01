package com.fanwe.library.title;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawableManager;
import com.fanwe.library.popupwindow.SDPopupWindow;
import com.fanwe.library.title.SDTitle.SDTitleMoreClickListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

public class SDDefaultMoreClickListener implements SDTitleMoreClickListener
{

	protected PopupWindow mPop;
	protected LinearLayout mLlMoreItem;
	protected TitleItem mItemMore;
	protected SDTitle mTitle;
	protected List<TitleItem> mListItemsRightMore;
	protected SDDrawableManager mDrawableManager = new SDDrawableManager();

	protected PopupWindow createPopMore()
	{
		if (mPop != null)
		{
			return mPop;
		}
		PopupWindow pop = new SDPopupWindow();
		pop.setWidth(SDViewUtil.getScreenWidth() / 2);
		View popView = SDViewUtil.inflate(R.layout.pop_title_more, null);
		mLlMoreItem = (LinearLayout) popView.findViewById(R.id.pop_title_more_ll_items);
		pop.setContentView(popView);
		return pop;
	}

	@Override
	public void onMoreClick(TitleItem itemMore, List<TitleItem> listItems, SDTitle title)
	{
		this.mTitle = title;
		this.mItemMore = itemMore;
		this.mListItemsRightMore = listItems;
		showMore();
	}

	@Override
	public void showMore()
	{
		mPop = createPopMore();
		if (mItemMore != null && mPop != null)
		{
			addRightViewsMore();
			mPop.showAsDropDown(mItemMore, 0, 0);
		}
	}

	@Override
	public void disMissMore()
	{
		if (mPop != null)
		{
			mPop.dismiss();
		}
	}

	private void addRightViewsMore()
	{
		beforeAddRightViewsMore();
		if (mListItemsRightMore != null)
		{
			for (TitleItem item : mListItemsRightMore)
			{
				addItemToRightMore(item);
			}
		}
	}

	private void addItemToRightMore(TitleItem item)
	{
		item.mIsAddToMore = true;
		if (item.mHasViewVisible)
		{
			View view = getItemView(item.getmConfig());
			if (view != null)
			{
				view.setTag(item.getmConfig());
				view.setOnClickListener(item.getmConfig().getOnClickListener());
				addItemView(view);
			}
		}
	}

	protected void beforeAddRightViewsMore()
	{
		if (mLlMoreItem != null)
		{
			mLlMoreItem.removeAllViews();
		}
	}

	/**
	 * 添加view，可以被重写
	 * 
	 * @param view
	 */
	protected void addItemView(View view)
	{
		if (mLlMoreItem != null)
		{
			mLlMoreItem.addView(view);
		}
	}

	/**
	 * 根据TitleItemConfig 生成view ，可以被重写
	 * 
	 * @param config
	 * @return
	 */
	protected View getItemView(TitleItemConfig config)
	{
		View view = SDViewUtil.inflate(R.layout.item_title_more, null);
		ImageView ivLeft = (ImageView) view.findViewById(R.id.item_title_more_iv_title);
		TextView tvTitle = (TextView) view.findViewById(R.id.item_title_more_tv_title);

		int imageResId = config.getImageLeftResId();
		if (imageResId == 0)
		{
			ivLeft.setImageBitmap(null);
			ivLeft.setVisibility(View.GONE);
		} else
		{
			ivLeft.setImageResource(imageResId);
			ivLeft.setVisibility(View.VISIBLE);
		}
		SDViewBinder.setTextView(tvTitle, config.getTextBot());
		SDViewUtil.setBackgroundDrawable(view, mDrawableManager.getSelectorMainColor(false));
		return view;
	}

}
