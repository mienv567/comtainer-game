package com.fanwe.library.customview;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.SDOtherUtil;

public abstract class SDViewBase<C extends SDViewConfig> extends LinearLayout
{
	protected boolean mSelected;

	protected SDViewAttr mAttr;
	protected EnumViewState mViewState = EnumViewState.NORMAL;
	protected SDViewBaseListener mListenerState;
	protected SDLibraryConfig mLibraryConfig = SDLibrary.getInstance().getConfig();
	protected C mConfig;

	private Map<View, SDViewBaseConfig> mMapViewConfig = new HashMap<View, SDViewBaseConfig>();

	public C getmConfig()
	{
		return mConfig;
	}

	public void setmConfig(C config)
	{
		if (config != null)
		{
			this.mConfig = config;
		}
	}

	public SDViewBaseConfig getViewConfig(View view)
	{
		SDViewBaseConfig config = null;
		if (view != null)
		{
			config = mMapViewConfig.get(view);
			if (config == null)
			{
				config = new SDViewBaseConfig();
				mMapViewConfig.put(view, config);
			}
		}
		return config;
	}

	public void setmListenerState(SDViewBaseListener mListenerState)
	{
		this.mListenerState = mListenerState;
	}

	public SDViewBase(Context context)
	{
		this(context, null);
	}

	public SDViewBase(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		baseInit();
	}

	private void baseInit()
	{
		mAttr = new SDViewAttr();

		createDefaultConfig();
		onInit();
	}

	protected void onInit()
	{

	}

	public void setDefaultConfig()
	{

	}

	@SuppressWarnings("unchecked")
	private void createDefaultConfig()
	{
		try
		{
			Type[] types = SDOtherUtil.getType(getClass());
			if (types != null && types.length > 0)
			{
				Class<C> clazz = (Class<C>) types[0];
				mConfig = clazz.newInstance();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public EnumViewState getmViewState()
	{
		return mViewState;
	}

	public void setmViewState(EnumViewState viewState)
	{
		if (viewState != null)
		{
			this.mViewState = viewState;
			updateViewState();
		}
	}

	public void updateViewState()
	{
		switch (mViewState)
		{
		case FOCUS:
			onFocus();
			break;
		case NORMAL:
			onNormal();
			break;
		case PRESSED:
			onPressed();
			break;
		case SELECTED:
			onSelected();
			break;

		default:
			break;
		}
	}

	public void onNormal()
	{
		setmSelected(false);
		mViewState = EnumViewState.NORMAL;
		if (mListenerState != null)
		{
			mListenerState.onNormal_SDViewBase(this);
		}
	}

	public void onSelected()
	{
		setmSelected(true);
		mViewState = EnumViewState.SELECTED;
		if (mListenerState != null)
		{
			mListenerState.onSelected_SDViewBase(this);
		}
	}

	public void toggleSelected()
	{
		if (mSelected)
		{
			onNormal();
		} else
		{
			onSelected();
		}
	}

	public void onFocus()
	{
		mViewState = EnumViewState.FOCUS;
		if (mListenerState != null)
		{
			mListenerState.onFocus_SDViewBase(this);
		}
	}

	public void onPressed()
	{
		mViewState = EnumViewState.PRESSED;
		if (mListenerState != null)
		{
			mListenerState.onPressed_SDViewBase(this);
		}
	}

	// ------------------utils method

	@SuppressWarnings("unchecked")
	protected <V extends View> V find(int id, View parentView)
	{
		return (V) parentView.findViewById(id);
	}

	protected <V extends View> V find(int id)
	{
		return find(id, this);
	}

	protected void setContentView(int resId)
	{
		LayoutInflater.from(getContext()).inflate(resId, this, true);
	}

	protected void setContentView(View view)
	{
		removeAllViews();
		addView(view);
	}

	protected void setContentView(View view, ViewGroup.LayoutParams params)
	{
		removeAllViews();
		addView(view, params);
	}

	public DisplayMetrics getDisplayMetrics()
	{
		return getContext().getResources().getDisplayMetrics();
	}

	public int getScreenWidth()
	{
		DisplayMetrics metrics = getDisplayMetrics();
		return metrics.widthPixels;
	}

	public int getScreenHeight()
	{
		DisplayMetrics metrics = getDisplayMetrics();
		return metrics.heightPixels;
	}

	public LinearLayout.LayoutParams getLayoutParamsMM()
	{
		return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	}

	public LinearLayout.LayoutParams getLayoutParamsWW()
	{
		return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	public LinearLayout.LayoutParams getLayoutParamsWM()
	{
		return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
	}

	public LinearLayout.LayoutParams getLayoutParamsMW()
	{
		return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	// -----------------base method

	public void setTextSizeSp(TextView textView, float sizeSp)
	{
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
	}

	// -------------------------imageview

	public void setImageViewNormal(ImageView... ivs)
	{
		if (ivs != null && ivs.length > 0)
		{
			for (ImageView iv : ivs)
			{
				iv.setImageResource(mAttr.getmImageNormalResId());
			}
		}
	}

	public void setImageViewSelected(ImageView... ivs)
	{
		if (ivs != null && ivs.length > 0)
		{
			for (ImageView iv : ivs)
			{
				iv.setImageResource(mAttr.getmImageSelectedResId());
			}
		}
	}

	public void setImageViewPressed(ImageView... ivs)
	{
		if (ivs != null && ivs.length > 0)
		{
			for (ImageView iv : ivs)
			{
				iv.setImageResource(mAttr.getmImagePressedResId());
			}
		}
	}

	// -------------------------textColor

	public void setTextColorNormal(TextView... tvs)
	{
		if (tvs != null && tvs.length > 0)
		{
			for (TextView tv : tvs)
			{
				tv.setTextColor(mAttr.getmTextColorNormal());
			}
		}
	}

	public void setTextColorSelected(TextView... tvs)
	{
		if (tvs != null && tvs.length > 0)
		{
			for (TextView tv : tvs)
			{
				tv.setTextColor(mAttr.getmTextColorSelected());
			}
		}
	}

	public void setTextColorPressed(TextView... tvs)
	{
		if (tvs != null && tvs.length > 0)
		{
			for (TextView tv : tvs)
			{
				tv.setTextColor(mAttr.getmTextColorPressed());
			}
		}
	}

	// ----------------------background

	@SuppressLint("NewApi")
	public void setViewBackground(View view, Drawable drawable, int resId)
	{
		if (resId != 0)
		{
			view.setBackgroundResource(resId);
		} else
		{
			if (drawable != null)
			{
				if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
				{
					view.setBackground(drawable);
				} else
				{
					view.setBackgroundDrawable(drawable);
				}
			}
		}
	}

	public void setViewBackgroundNormal(View... views)
	{
		if (views != null && views.length > 0)
		{
			for (View view : views)
			{
				setViewBackground(view, mAttr.getmBackgroundDrawableNormal(), mAttr.getmBackgroundDrawableNormalResId());
			}
		}
	}

	public void setViewBackgroundSelected(View... views)
	{
		if (views != null && views.length > 0)
		{
			for (View view : views)
			{
				setViewBackground(view, mAttr.getmBackgroundDrawableSelected(), mAttr.getmBackgroundDrawableSelectedResId());
			}
		}
	}

	public void setViewBackgroundPressed(View... views)
	{
		if (views != null && views.length > 0)
		{
			for (View view : views)
			{
				setViewBackground(view, mAttr.getmBackgroundDrawablePressed(), mAttr.getmBackgroundDrawablePressedResId());
			}
		}
	}

	// ------------------setter getter

	public SDViewAttr getmAttr()
	{
		return mAttr;
	}

	public boolean ismSelected()
	{
		return mSelected;
	}

	public void setmSelected(boolean mSelected)
	{
		this.mSelected = mSelected;
	}

	public void setmAttr(SDViewAttr mAttr)
	{
		this.mAttr = mAttr;
	}

	// --------------------------enum
	public enum EnumViewState
	{
		NORMAL, SELECTED, PRESSED, FOCUS;
	}

	public interface SDViewBaseListener
	{
		public void onNormal_SDViewBase(View v);

		public void onSelected_SDViewBase(View v);

		public void onFocus_SDViewBase(View v);

		public void onPressed_SDViewBase(View v);
	}

}
