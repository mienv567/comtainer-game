package com.fanwe.live.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-3 上午10:53:50 类说明
 */
public class LiveContLocalFragment extends LiveContBaseFragment
{
	public static final String TAG = "LiveContLocalFragment";
	public static final String EXTRA_ROOM_ID = "extra_room_id";
	public static final String EXTRA_ACTTICKET_URL = "extraactticketurl"; // 用户点击战斗力值排行跳转页面
	@ViewInject(R.id.ll_do_not_contribute)
	private LinearLayout ll_do_not_contribute;

	private ImageView civ_head_me;
	private TextView tv_me;
	private TextView tv_me_number;

	protected int room_id;

	@Override
	protected void init()
	{
		getIntentExtra();
		super.init();
	}

	private void getIntentExtra()
	{
		room_id = getActivity().getIntent().getExtras().getInt(EXTRA_ROOM_ID);
	}

	@Override
	protected void register()
	{
		super.register();

		SDViewUtil.hide(ll_do_not_contribute);

		View view = getActivity().getLayoutInflater().inflate(R.layout.frag_cont_head, null);
		civ_head_me = (ImageView) view.findViewById(R.id.civ_head_me);
		tv_me = (TextView) view.findViewById(R.id.tv_me);
		tv_me_number = (TextView) view.findViewById(R.id.tv_me_number);

		view.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (app_ContActModel != null)
				{
					UserModel user = app_ContActModel.getUserInfo();
					if (user != null)
					{
						String userid = user.getUserId();
						if (!TextUtils.isEmpty(userid))
						{
							Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
							intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUserId());
							startActivity(intent);
						} else
						{
//							SDToast.showToast("userid为空");
						}
					}
				}
			}
		});

		list.getRefreshableView().addHeaderView(view);
	}

	@Override
	protected void requestCont(final boolean isLoadMore)
	{
		CommonInterface.requestCont(room_id, "", page, new AppRequestCallback<App_ContActModel>()
		{
			@Override
			protected void onSuccess(SDResponse resp)
			{
				if (rootModel.getStatus() == 1)
				{
					app_ContActModel = actModel;
					bindheadData(actModel);
					SDViewUtil.updateAdapterByList(listModel, actModel.getCuserList(), adapter, isLoadMore);
				}
			}

			private void bindheadData(App_ContActModel actModel)
			{
				if (actModel != null)
				{
					UserModel user = actModel.getUserInfo();
					if (user != null)
					{
						SDViewBinder.setImageView(getActivity(),user.getHeadImage(), civ_head_me,R.drawable.ic_default_head);
						SDViewBinder.setTextView(tv_me, user.getNickName());
					}
					SDViewBinder.setTextView(tv_me_number, actModel.getTotalNum() + "");
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
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("本场直播贡献排行界面");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("本场直播贡献排行界面");
	}
}
