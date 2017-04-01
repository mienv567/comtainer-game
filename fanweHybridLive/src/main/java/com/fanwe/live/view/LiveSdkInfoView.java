package com.fanwe.live.view;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.live.R;

public class LiveSdkInfoView extends LinearLayout
{
	@ViewInject(R.id.tv_quality)
	private TextView tv_quality;

	private SDLooper looper = new SDSimpleLooper();

	public LiveSdkInfoView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public LiveSdkInfoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public LiveSdkInfoView(Context context)
	{
		super(context);
		init();
	}

	protected void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_live_sdk_info, this, true);
		x.view().inject(this, this);

//		looper.start(new Runnable()
//		{
//
//			@Override
//			public void run()
//			{
//				String quality = QavsdkControl.getInstance().getQualityTips();
//
//				tv_quality.setText(quality);
//			}
//		});
	}

	@Override
	protected void onDetachedFromWindow()
	{
		looper.stop();
		super.onDetachedFromWindow();
	}

}
