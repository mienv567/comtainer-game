package com.fanwe.live.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.fragment.LiveContTotalFragment;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-12 下午8:33:19 类说明
 */
public class LiveMySelfContActivity extends BaseTitleActivity
{
	@ViewInject(R.id.ll_content)
	private LinearLayout ll_content;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myself_cont);
		x.view().inject(this);
		init();
	}

	private void init()
	{
		initTitle();
		addFragment();
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop(AppRuntimeWorker.getTicketName() + getString(R.string.contribution_list));
	}

	private void addFragment()
	{
		/**
		 * 总贡献排行
		 */
		getSDFragmentManager().toggle(R.id.ll_content, null, LiveContTotalFragment.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
