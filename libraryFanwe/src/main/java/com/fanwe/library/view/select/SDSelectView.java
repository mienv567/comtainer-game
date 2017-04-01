package com.fanwe.library.view.select;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.SDViewUtil;

public abstract class SDSelectView extends LinearLayout
{
	private boolean mSelected;
	protected SDSelectViewStateListener mListener;
	protected SDLibraryConfig mLibraryConfig = SDLibrary.getInstance().getConfig();
	private Map<View, SDSelectViewConfig> mMapViewConfig = new HashMap<View, SDSelectViewConfig>();

	public SDSelectView(Context context)
	{
		super(context);
		baseInit();
	}

	public SDSelectView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		baseInit();
	}

	private void baseInit()
	{

	}

	/**
	 * 为统一规范，重写此方法进行初始化，在类的构造方法中调用此方法进行初始化
	 */
	protected void init()
	{

	}

	/**
	 * 若需要设置默认config，为统一规范，重写此方法进行设置
	 */
	public void setDefaultConfig()
	{

	}

	/**
	 * 若需要设置默认config反转，为统一规范，重写此方法进行设置
	 */
	public void reverseDefaultConfig()
	{

	}

	/**
	 * 正常状态回调
	 */
	public void onNormal()
	{

	}

	/**
	 * 选中状态回调
	 */
	public void onSelected()
	{

	}

	/**
	 * 设置状态监听
	 * 
	 * @param listener
	 */
	public void setListenerState(SDSelectViewStateListener listener)
	{
		this.mListener = listener;
	}

	public SDSelectViewConfig getViewConfig(View view)
	{
		SDSelectViewConfig config = null;
		if (view != null)
		{
			config = mMapViewConfig.get(view);
			if (config == null)
			{
				config = new SDSelectViewConfig();
				mMapViewConfig.put(view, config);
			}
		}
		return config;
	}

	// util method

	protected SDSelectView normalImageView_image(ImageView view)
	{
		int resId = getViewConfig(view).getImageNormalResId();
		if (resId != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setImageResource(resId);
		}
		return this;
	}

	protected SDSelectView selectImageView_image(ImageView view)
	{
		int resId = getViewConfig(view).getImageSelectedResId();
		if (resId != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setImageResource(resId);
		}
		return this;
	}

	protected SDSelectView normalTextView_textColor(TextView view)
	{
		int color = getViewConfig(view).getTextColorNormal();
		if (color != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setTextColor(color);
		}
		return this;
	}

	protected SDSelectView selectTextView_textColor(TextView view)
	{
		int color = getViewConfig(view).getTextColorSelected();
		if (color != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setTextColor(color);
		}
		return this;
	}

	protected SDSelectView normalTextView_textSize(TextView view)
	{
		int size = getViewConfig(view).getTextSizeNormal();
		if (size != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
		return this;
	}

	protected SDSelectView selectTextView_textSize(TextView view)
	{
		int size = getViewConfig(view).getTextSizeSelected();
		if (size != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
		return this;
	}

	protected SDSelectView normalView_alpha(View view)
	{
		float alpha = getViewConfig(view).getAlphaNormal();
		if (alpha != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setAlpha(alpha);
		}
		return this;
	}

	protected SDSelectView selectView_alpha(View view)
	{
		float alpha = getViewConfig(view).getAlphaSelected();
		if (alpha != SDSelectViewConfig.EMPTY_VALUE)
		{
			view.setAlpha(alpha);
		}
		return this;
	}

	protected SDSelectView normalView_background(View view)
	{
		Drawable drawable = getViewConfig(view).getBackgroundNormal();
		if (drawable != null)
		{
			SDViewUtil.setBackgroundDrawable(view, drawable);
		}
		return this;
	}

	protected SDSelectView selectView_background(View view)
	{
		Drawable drawable = getViewConfig(view).getBackgroundSelected();
		if (drawable != null)
		{
			SDViewUtil.setBackgroundDrawable(view, drawable);
		}
		return this;
	}

	public void updateViewState()
	{
		if (mSelected)
		{
			onSelected();
		} else
		{
			onNormal();
		}
	}

	public void toggleSelected()
	{
		mSelected = !mSelected;
		if (mSelected)
		{
			setStateSelected();
		} else
		{
			setStateNormal();
		}
	}

	/**
	 * 设置布局
	 * 
	 * @param resId
	 */
	protected void setContentView(int resId)
	{
		removeAllViews();
		LayoutInflater.from(getContext()).inflate(resId, this, true);
	}

	/**
	 * 设置布局
	 * 
	 * @param view
	 */
	protected void setContentView(View view)
	{
		removeAllViews();
		addView(view);
	}

	/**
	 * 设置布局
	 * 
	 * @param view
	 * @param params
	 */
	protected void setContentView(View view, ViewGroup.LayoutParams params)
	{
		removeAllViews();
		addView(view, params);
	}

	public boolean isSelected()
	{
		return mSelected;
	}

	public void setStateNormal()
	{
		setStateNormal(true);
	}

	/**
	 * 设置状态为normal
	 */
	public void setStateNormal(boolean notify)
	{
		mSelected = false;
		onNormal();
		if (notify)
		{
			notifyNormal();
		}
	}

	public void setStateSelected()
	{
		setStateSelected(true);
	}

	/**
	 * 设置状态为selected
	 */
	public void setStateSelected(boolean notify)
	{
		mSelected = true;
		onSelected();
		if (notify)
		{
			notifySelected();
		}
	}

	private void notifyNormal()
	{
		if (mListener != null)
		{
			mListener.onNormal(this);
		}
	}

	private void notifySelected()
	{
		if (mListener != null)
		{
			mListener.onSelected(this);
		}
	}

	public interface SDSelectViewStateListener
	{
		public void onNormal(View v);

		public void onSelected(View v);
	}

}
