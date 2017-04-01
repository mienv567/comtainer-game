package com.fanwe.library.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;

/**
 * 用com.fanwe.library.view.SDTabUnderline替代
 * @author Administrator
 *
 */
@Deprecated
public class SDTabUnderline extends SDViewBase<SDTabUnderline.Config>
{

	public TextView mTvTitle;
	public ImageView mIvUnderline;

	public SDTabUnderline(Context context)
	{
		this(context, null);
	}

	public SDTabUnderline(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_tab_under_line, this, true);

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mIvUnderline = (ImageView) findViewById(R.id.iv_underline);
	}

	public void setTextTitle(String title)
	{
		SDViewBinder.setTextView(mTvTitle, title);
	}

	@Override
	public void onNormal()
	{
		mTvTitle.setTextColor(mConfig.getmTextColorNormal());
		mIvUnderline.setBackgroundColor(mConfig.getmBackgroundColorUnderlineNormal());
		super.onNormal();
	}

	@Override
	public void onSelected()
	{
		mTvTitle.setTextColor(mConfig.getmTextColorSelected());
		mIvUnderline.setBackgroundColor(mConfig.getmBackgroundColorUnderlineSelected());
		super.onSelected();
	}

	public static class Config extends SDViewConfig
	{
		private int mTextColorNormal;
		private int mTextColorSelected;

		private int mBackgroundColorUnderlineNormal;
		private int mBackgroundColorUnderlineSelected;

		@Override
		public Config clone()
		{
			return (Config) super.clone();
		}

		@Override
		public void setDefaultConfig()
		{
			mTextColorNormal = SDResourcesUtil.getColor(R.color.gray);
			mTextColorSelected = mLibraryConfig.getMainColor();

			mBackgroundColorUnderlineNormal = SDResourcesUtil.getColor(R.color.transparent);
			mBackgroundColorUnderlineSelected = mLibraryConfig.getMainColor();
		}

		public int getmTextColorNormal()
		{
			return mTextColorNormal;
		}

		public void setmTextColorNormal(int mTextColorNormal)
		{
			this.mTextColorNormal = mTextColorNormal;
		}

		public int getmTextColorSelected()
		{
			return mTextColorSelected;
		}

		public void setmTextColorSelected(int mTextColorSelected)
		{
			this.mTextColorSelected = mTextColorSelected;
		}

		public int getmBackgroundColorUnderlineNormal()
		{
			return mBackgroundColorUnderlineNormal;
		}

		public void setmBackgroundColorUnderlineNormal(int mBackgroundColorUnderlineNormal)
		{
			this.mBackgroundColorUnderlineNormal = mBackgroundColorUnderlineNormal;
		}

		public int getmBackgroundColorUnderlineSelected()
		{
			return mBackgroundColorUnderlineSelected;
		}

		public void setmBackgroundColorUnderlineSelected(int mBackgroundColorUnderlineSelected)
		{
			this.mBackgroundColorUnderlineSelected = mBackgroundColorUnderlineSelected;
		}

	}

}
