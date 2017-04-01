package com.fanwe.library.title;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

public class TitleItem extends LinearLayout
{
	public View mView;
	public ImageView mIvLeft;
	public ImageView mIvRight;
	public TextView mTvTop;
	public TextView mTvBot;

	public LinearLayout mLlText;
	private Drawable mBackgroundDrawable;
	private TitleItemConfig mConfig;

	/** 是否有控件可见 */
	public boolean mHasViewVisible = false;
	public boolean mIsAddToMore = false;

	public TitleItemConfig getmConfig()
	{
		return mConfig;
	}

	public void setmConfig(TitleItemConfig mConfig)
	{
		this.mConfig = mConfig;
		if (this.mConfig != null)
		{
			this.mConfig.setTitleItem(this);
		}
	}

	public TitleItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public TitleItem(Context context)
	{
		this(context, null);
	}

	private void init()
	{
		this.setGravity(Gravity.CENTER);
		this.setLayoutParams(SDViewUtil.getLayoutParamsLinearLayoutWM());
		mView = LayoutInflater.from(getContext()).inflate(R.layout.title_item, null);
		this.addView(mView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(getResources().getColor(android.R.color.transparent));

		mIvLeft = (ImageView) findViewById(R.id.title_item_iv_left);
		mIvRight = (ImageView) findViewById(R.id.title_item_iv_right);

		mLlText = (LinearLayout) findViewById(R.id.title_item_ll_text);
		mTvTop = (TextView) findViewById(R.id.title_item_tv_top);
		mTvBot = (TextView) findViewById(R.id.title_item_tv_bot);

		setAllViewsVisibility(View.GONE);
	}

	public void setAllViewsVisibility(int visibility)
	{
		mIvLeft.setVisibility(visibility);
		mIvRight.setVisibility(visibility);

		mLlText.setVisibility(visibility);
		mTvTop.setVisibility(visibility);
		mTvBot.setVisibility(visibility);
		dealClickListener();
	}

	public TitleItem setTextTop(String text)
	{
		SDViewBinder.setTextViewsVisibility(mTvTop, text);
		dealClickListener();
		return this;
	}

	public TitleItem setTextBot(String text)
	{
		SDViewBinder.setTextViewsVisibility(mTvBot, text);
		dealClickListener();
		return this;
	}

	public TitleItem setBackgroundText(int resId)
	{
		mLlText.setBackgroundResource(resId);
		return this;
	}

	public TitleItem setImageLeft(int resId)
	{
		if (resId == 0)
		{
			mIvLeft.setVisibility(View.GONE);
		} else
		{
			mIvLeft.setVisibility(View.VISIBLE);
			mIvLeft.setImageResource(resId);
		}
		dealClickListener();
		return this;
	}

	public TitleItem setImageRight(int resId)
	{
		if (resId == 0)
		{
			mIvRight.setVisibility(View.GONE);
		} else
		{
			mIvRight.setVisibility(View.VISIBLE);
			mIvRight.setImageResource(resId);
		}
		dealClickListener();
		return this;
	}

	private void dealClickListener()
	{
		if (mIvLeft.getVisibility() == View.VISIBLE || mTvTop.getVisibility() == View.VISIBLE || mTvBot.getVisibility() == View.VISIBLE
				|| mIvRight.getVisibility() == View.VISIBLE)
		{
			if (mTvTop.getVisibility() == View.VISIBLE || mTvBot.getVisibility() == View.VISIBLE)
			{
				mLlText.setVisibility(View.VISIBLE);
			} else
			{
				mLlText.setVisibility(View.GONE);
			}
			mHasViewVisible = true;
			SDViewUtil.setBackgroundDrawable(this, mBackgroundDrawable);
		} else
		{
			mHasViewVisible = false;
			SDViewUtil.setBackgroundDrawable(this, null);
		}
	}

	public TitleItem create()
	{
		create(false);
		return this;
	}

	public TitleItem create(boolean showTypeChange)
	{
		if (mConfig != null)
		{
			setVisibility(mConfig.getVisibility());
			this.setOnClickListener(mConfig.getOnClickListener());
			EnumShowType type = mConfig.getShowType();
			if (type != null)
			{
				if (showTypeChange)
				{
					setAllViewsVisibility(View.GONE);
				}
				switch (mConfig.getShowType())
				{
				case ICON:
					setImageLeft(mConfig.getImageLeftResId());
					break;
				case TEXT:
					setTextBot(mConfig.getTextBot());
					break;
				case ICON_TEXT:
					setImageLeft(mConfig.getImageLeftResId());
					setTextBot(mConfig.getTextBot());
					break;
				case TEXT_ICON:
					setImageRight(mConfig.getImageRightResId());
					setTextBot(mConfig.getTextBot());
					break;
				case TEXT_WITH_BACKGROUND:
					this.mBackgroundDrawable = null;
					setBackgroundDrawable(null);
					setTextBot(mConfig.getTextBot());
					setBackgroundText(mConfig.getBackgroundTextResId());
					break;

				default:
					break;
				}
			}
		}
		return this;
	}

	@Override
	@Deprecated
	public void setBackgroundDrawable(Drawable background)
	{
		if (background != null)
		{
			this.mBackgroundDrawable = background;
		}
		super.setBackgroundDrawable(background);
	}

	public enum EnumShowType
	{
		/** 默认以左边的ImageView显示 */
		ICON,
		/** 根据textBot来显示 */
		TEXT,
		/** 根据textBot来显示,背景根据 */
		TEXT_WITH_BACKGROUND,
		/** 根据textBot来显示,根据iconLeftResId来显示 */
		ICON_TEXT,
		/** 根据textBot来显示,根据iconRightResId来显示 */
		TEXT_ICON;
	}
}
