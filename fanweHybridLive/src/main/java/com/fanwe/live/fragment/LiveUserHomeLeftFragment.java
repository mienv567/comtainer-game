package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveMySelfContActivity;
import com.fanwe.live.adapter.LiveUserHomeLeftAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-11 下午3:30:11 类说明
 */
public class LiveUserHomeLeftFragment extends LiveUserHomeBaseFragment
{
	public static final String TAG = "LiveUserHomeLeftFragment";

	@ViewInject(R.id.ll_cont)
	private LinearLayout ll_cont;

	@ViewInject(R.id.gll_info)
	private SDGridLinearLayout gll_info;

	@ViewInject(R.id.iv_first)
	private ImageView iv_first;
	@ViewInject(R.id.iv_second)
	private ImageView iv_second;
	@ViewInject(R.id.iv_third)
	private ImageView iv_third;

	private LiveUserHomeLeftAdapter adapter;

	@Override
	protected int onCreateContentView()
	{
		return R.layout.frag_user_home_left;
	}

	@Override
	protected void init()
	{
		super.init();
		request();
		register();
		bindData();
	}

	private void request() {
		String userId = app_user_homeActModel.getUser().getUserId();
		CommonInterface.requestCont(0, userId, 0, new AppRequestCallback<App_ContActModel>() {
			@Override
			protected void onSuccess(SDResponse sdResponse) {
				List<App_ContModel> cuser_list = actModel.getCuserList();
				if (cuser_list != null && cuser_list.size() > 0)
				{
					SDViewUtil.show(iv_first);
					SDViewUtil.show(iv_second);
					SDViewUtil.show(iv_third);
					if (cuser_list.size() == 1)
					{
						App_ContModel model0 = cuser_list.get(0);
						SDViewBinder.setImageView(getActivity(),iv_first, model0.getHead_image(),R.drawable.ic_default_head);
					} else if (cuser_list.size() == 2)
					{
						App_ContModel model0 = cuser_list.get(0);
						App_ContModel model1 = cuser_list.get(1);
						SDViewBinder.setImageView(getActivity(),iv_first, model0.getHead_image(),R.drawable.ic_default_head);
						SDViewBinder.setImageView(getActivity(),iv_second, model1.getHead_image(),R.drawable.ic_default_head);
					} else if (cuser_list.size() >= 3)
					{
						App_ContModel model0 = cuser_list.get(0);
						App_ContModel model1 = cuser_list.get(1);
						App_ContModel model2 = cuser_list.get(2);
						SDViewBinder.setImageView(getActivity(),iv_first, model0.getHead_image(),R.drawable.ic_default_head);
						SDViewBinder.setImageView(getActivity(),iv_second, model1.getHead_image(),R.drawable.ic_default_head);
						SDViewBinder.setImageView(getActivity(),iv_third, model2.getHead_image(),R.drawable.ic_default_head);
					}
				}

			}
		});
	}

	private void register()
	{
		ll_cont.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (app_user_homeActModel != null && app_user_homeActModel.getUser() != null)
				{
					UserModel user = app_user_homeActModel.getUser();
					Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
					intent.putExtra(LiveContTotalFragment.EXTRA_USER_ID, user.getUserId());
					startActivity(intent);
				}
			}
		});
	}

	private void bindData()
	{
		if (app_user_homeActModel == null)
		{
			return;
		}

		SDViewUtil.show(iv_first);
		SDViewUtil.show(iv_second);
		SDViewUtil.show(iv_third);

		UserModel user = app_user_homeActModel.getUser();

		if (user != null)
		{
			ArrayList<ItemUserModel> list = new ArrayList<LiveUserHomeLeftFragment.ItemUserModel>();
			Map<String, String> map = user.getItem();

			if (map != null && map.size() > 0)
			{
				for (String key : map.keySet())
				{
					ItemUserModel item = new ItemUserModel();
					item.key = key;
					item.value = map.get(key);
					list.add(item);
				}
			}

			adapter = new LiveUserHomeLeftAdapter(list, getActivity());
			gll_info.setColNumber(1);
			gll_info.setAdapter(adapter);
		}
	}

	public static class ItemUserModel
	{
		private String key;
		private String value;

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("用户主页-主页");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("用户主页-主页");
	}
}
