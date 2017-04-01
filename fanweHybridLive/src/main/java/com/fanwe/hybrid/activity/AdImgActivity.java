package com.fanwe.hybrid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-2-23 上午11:35:31 类说明
 */
public class AdImgActivity extends BaseActivity implements OnClickListener
{
	public static final String EXTRA_URL = "extra_url";

	@ViewInject(R.id.iv_ad_img)
	private ImageView mIvAdImg;
	@ViewInject(R.id.btn_skip)
	private Button mBtnSkip;

	private String mImgUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_ad_img);
		x.view().inject(this);
		init();
	}

	private void init()
	{
		initIntent();
		register();
		initView();
	}

	private void initIntent()
	{
		mImgUrl = getIntent().getExtras().getString(EXTRA_URL);
	}

	private void register()
	{
		mIvAdImg.setOnClickListener(this);
		mBtnSkip.setOnClickListener(this);
	}

	private void initView()
	{
		if (!TextUtils.isEmpty(mImgUrl))
		{
			SDViewBinder.setImageView(AdImgActivity.this,mIvAdImg, mImgUrl);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.iv_ad_img:
			clickIvAdImg();
			break;
		case R.id.btn_skip:
			clickBtnSkip();
			break;
		}

	}

	private void clickBtnSkip()
	{
		clickIvAdImg();
	}

	private void clickIvAdImg()
	{
		InitActModel model = InitActModelDao.query();
		if (model != null)
		{
			String http = model.getAd_http();
			int ad_open = model.getAd_open();
			if (ad_open == 1)
			{
				Intent intent_open_type = new Intent(this, AppWebViewActivity.class);
				intent_open_type.putExtra(AppWebViewActivity.EXTRA_URL, http);
				intent_open_type.putExtra(AppWebViewActivity.EXTRA_FINISH_TO_MAIN, true);
				startActivity(intent_open_type);
				finish();
			} else
			{
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra(MainActivity.EXTRA_URL, http);
				startActivity(intent);
				finish();
			}
		}

	}
}
