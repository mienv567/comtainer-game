package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午4:46:21 类说明
 */
public class LiveFollowActivity extends LiveFocusFollowBaseActivity
{
	public static final String TAG = "LiveFollowActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init()
	{
		super.init();
		initTitle();
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop(getString(R.string.follow_user));
		UserModel user = UserModelDao.query();
		if (user.getUserId().equals(getIntentUserId()))
		{
			mTitle.initRightItem(1);
			mTitle.getItemRight(0).setImageLeft(R.drawable.ic_add_friend);
		}
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		Intent intent = null;
		switch (index)
		{
		case 0:
			intent = new Intent(this, LiveSearchUserActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void request(final boolean isLoadMore)
	{
		CommonInterface.requestUser_follow(page, to_user_id, new AppRequestCallback<App_focus_follow_ActModel>()
		{
			@Override
			protected void onSuccess(SDResponse resp)
			{
				if (actModel.getStatus() == 1)
				{
					app_my_focusActModel = actModel;
					SDViewUtil.updateAdapterByList(listModel, actModel.getRelationshipList(), adapter, isLoadMore);
				}
			}

			@Override
			protected void onFinish(SDResponse resp)
			{
				super.onFinish(resp);
				list.onRefreshComplete();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("关注的人界面");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("关注的人界面");
		MobclickAgent.onPause(this);
	}
}
