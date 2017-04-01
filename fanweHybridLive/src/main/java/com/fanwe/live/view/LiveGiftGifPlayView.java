package com.fanwe.live.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.gif.GifConfigModel;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class LiveGiftGifPlayView extends LinearLayout
{

	private LinearLayout ll_all;
	private TextView tv_nickname;
	private ImageView iv_image;
	private CustomMsgGift msg;

	private GifConfigModel config;
	private GifDrawable gifDrawable;
	/** 是否处于忙碌 */
	private boolean isBusy = false;
	/** 是否正在播放gif */
	private boolean isPlaying = false;
	/** 是否加载gif成功 */
	private boolean isLoadGifSuccess = false;
	/** 是否正在加载gif */
	private boolean isLoadingGif = false;
	/** 是否按次数播放gif模式 */
	private boolean isPlayCount = false;
	private int playCount;
	private File gifFile;

	public LiveGiftGifPlayView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public LiveGiftGifPlayView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public LiveGiftGifPlayView(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.view_live_gift_gif_play, this, true);

		ll_all = (LinearLayout) findViewById(R.id.ll_all);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		SDViewUtil.invisible(this);

	}

	/**
	 * 是否正在播放gif
	 * 
	 * @return
	 */
	public boolean isPlaying()
	{
		return isPlaying;
	}

	/**
	 * 是否忙碌
	 * 
	 * @return
	 */
	public boolean isBusy()
	{
		return isBusy;
	}

	/**
	 * gif是否加载成功
	 * 
	 * @return
	 */
	public boolean isLoadGifSuccess()
	{
		return isLoadGifSuccess;
	}

	/**
	 * gif是否正在加载中
	 * 
	 * @return
	 */
	public boolean isLoadingGif()
	{
		return isLoadingGif;
	}

	/**
	 * 验证gif配置是否合法
	 * 
	 * @return
	 */
	private boolean validateConfig()
	{
		if (config == null)
		{
			return false;
		}

		if (config.getPlay_count() <= 0 && config.getDuration() <= 0)
		{
			return false;
		}

		if (config.getPlay_count() > 0)
		{
			isPlayCount = true;
			playCount = 0;
		}

		switch (config.getType())
		{
		case GifConfigModel.TYPE_GRAVITY_TOP:
			ll_all.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
			break;
		case GifConfigModel.TYPE_GRAVITY_MIDDLE:
			ll_all.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			break;
		case GifConfigModel.TYPE_GRAVITY_BOTTOM:
			ll_all.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
			break;

		default:
			break;
		}

		return true;
	}

	/**
	 * 设置gif配置
	 * 
	 * @param config
	 */
	public void setConfig(GifConfigModel config)
	{
		this.config = config;
	}

	/**
	 * 设置msg，调用此方法前要先调用setConfig方法设置gif配置
	 * 
	 * @param msg
	 * @return
	 */
	public boolean setMsg(CustomMsgGift msg)
	{
		boolean isTarget = false;
		if (msg != null)
		{
			if (validateConfig())
			{
				this.msg = msg;
				initMsg();
				bindData();
				isTarget = true;
			}
		}
		return isTarget;
	}

	private void initMsg()
	{
		isBusy = true;
		isLoadGifSuccess = false;
		isLoadingGif = true;
		final String url = config.getUrl();
		ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener()
		{
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				gifFile = ImageLoader.getInstance().getDiskCache().get(url);
				if (gifFile != null)
				{
					loadResult(true);
				} else
				{
					loadResult(false);
				}
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason)
			{
				loadResult(false);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
				loadResult(false);
			}
		});
	}

	private void loadResult(boolean success)
	{
		isLoadingGif = false;
		isLoadGifSuccess = success;
		if (isLoadGifSuccess)
		{

		} else
		{
			stopGif();
		}
	}

	public void playGif()
	{
		try
		{
			if (!isLoadGifSuccess())
			{
				return;
			}
			isPlaying = true;
			gifDrawable = new GifDrawable(gifFile);
			gifDrawable.addAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationCompleted()
				{
					if (isPlayCount)
					{
						playCount++;
						if (playCount == config.getPlay_count())
						{
							stopGif();
						}
					}
				}
			});
			iv_image.setImageDrawable(gifDrawable);

			long delay = config.getDelay_time();
			SDHandlerManager.getMainHandler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					SDViewUtil.show(LiveGiftGifPlayView.this);
					gifDrawable.start();
					if (isPlayCount)
					{
						// 次数模式
					} else
					{
						// 时长模式
						SDHandlerManager.getMainHandler().postDelayed(new Runnable()
						{

							@Override
							public void run()
							{
								stopGif();
							}
						}, config.getDuration());
					}
				}
			}, delay);
		} catch (Exception e)
		{
			stopGif();
			e.printStackTrace();
		}
	}

	private void stopGif()
	{
		if (gifDrawable != null)
		{
			gifDrawable.stop();
		}
		isBusy = false;
		isPlaying = false;
		playCount = 0;
		SDViewUtil.invisible(this);
	}

	private void bindData()
	{
		if (config.getShow_user() == 1)
		{
			SDViewUtil.show(tv_nickname);
			tv_nickname.setText(msg.getSender().getNickName());
		} else
		{
			SDViewUtil.hide(tv_nickname);
		}
	}

	@Override
	protected void onDetachedFromWindow()
	{
		stopGif();
		super.onDetachedFromWindow();
	}

}
