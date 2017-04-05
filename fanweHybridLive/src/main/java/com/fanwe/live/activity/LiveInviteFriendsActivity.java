package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveFriendsListAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_friendsActModel;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LiveInviteFriendsActivity extends BaseActivity implements OnItemClickListener{

	@ViewInject(R.id.lv_invite_friends_list)
	private SDProgressPullToRefreshListView mListView;
	@ViewInject(R.id.tv_invite_or_cancel)
	private TextView mBtnInviteOrCancel;
	@ViewInject(R.id.ll_no_friends_tips)
	private LinearLayout ll_no_friends_tips;
	
	private List<UserModel> mListUser ;
	private List<UserModel> mListSelected;
	private LiveFriendsListAdapter mAdapter;
	private int room_id;
	private int mPage = 1;
	private User_friendsActModel currentModels;//最新获取的好友
	
	/** 房间id(int) */
	public static final String EXTRA_ROOM_ID = "extra_room_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_live_invite_friends);
		initView();
		getExtraData();
		requestFriendsDatas(mPage);
	}

	private void getExtraData() {
		room_id = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
	}

	/**
	 * 获取好友数据
	 * @param page 获取页码
	 */
	private void requestFriendsDatas(int page) {
		if(page ==1) {
			mListUser.clear();
			mListSelected.clear();
		}
		CommonInterface.requestFriends(room_id, page, new AppRequestCallback<User_friendsActModel>() {
			@Override
			protected void onSuccess(SDResponse resp) {
				if(rootModel.getStatus() == 1) {
					currentModels = actModel;
					//获取好友数量大于0才更新数据
					mAdapter.appendData(actModel.getList());
				}

			}
			
			@Override
			protected void onFinish(SDResponse resp) {
				mListView.onRefreshComplete();
				super.onFinish(resp);
				if(mAdapter.getCount() > 0) {
					SDViewUtil.hide(ll_no_friends_tips);
				} else {
					SDViewUtil.show(ll_no_friends_tips);
				}
			}
		});
	}

	private void initView() {
		mBtnInviteOrCancel.setTextColor(SDDrawable.getStateListColor(SDResourcesUtil.getColor(R.color.main_color),SDResourcesUtil.getColor(R.color.white)));
		mListUser = new ArrayList<UserModel> ();
		mListSelected  = new ArrayList<UserModel> ();
		mAdapter = new LiveFriendsListAdapter(mListUser, this,LiveFriendsListAdapter.MODE_INVITE_FRIENDS);
		mListView.setOnItemClickListener(this);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mPage = 1;
				requestFriendsDatas(mPage);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(currentModels == null || currentModels.getList().size() == 0) {
					mListView.onRefreshComplete();
					return;
				}
				if(currentModels.hasNext()) {
					mPage ++;
					requestFriendsDatas(mPage);
				} else {
					SDToast.showToast(getString(R.string.no_more_data));
					mListView.onRefreshComplete();
				}
			}
		});
		mListView.setAdapter(mAdapter);
		mBtnInviteOrCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.tv_invite_or_cancel :
				requesInviteFriends();
				finish();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 邀请好友
	 */
	private void requesInviteFriends() {
		//当前未选中好友
		if(mListSelected.size() == 0) {
			return;
		}
		CommonInterface.requestPrivatePushUser(room_id,getUserIds(mListSelected),new AppRequestCallback<BaseActModel>() {
			
			@Override
			protected void onSuccess(SDResponse resp) {
				if(rootModel.isOk()) {
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long itemId) {
		boolean isSelscted = mAdapter.getSelectManager().getItem((int) itemId).isSelected();
		if(mListSelected.size() == 100 && !isSelscted) {
			SDToast.showToast(getString(R.string.choose_user_over_100));
			return;
		}
		mAdapter.getSelectManager().performClick((int) itemId);
		mListSelected = mAdapter.getSelectManager().getSelectedItems();
		updateView(mListSelected.size());
	}

	/**
	 * 更新按钮UI
	 * @param size
	 */
	private void updateView(int size) {
		switch(size) {
			//未选中
			case 0:
				changeInvite2Cancel();
				break;
			//选中
			default:
				changeCancel2Invite(size);
				break;
		}
	}

	private void changeCancel2Invite(int size) {
		mBtnInviteOrCancel.setText(getString(R.string.completed)+"("+size+")");
	}

	private void changeInvite2Cancel() {
		mBtnInviteOrCancel.setText(getString(R.string.cancel));
	}

	/**
	 * 获取接口参数所需的 user_id 字符串
	 * @param selectedList 选中好友列表
	 * @return
	 */
	private String getUserIds(List<UserModel> selectedList) {
		List<String> result = new ArrayList<String> ();
		for(UserModel model : selectedList) {
			result.add(model.getUserId());
		}
		return TextUtils.join(",", result);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("私密房间邀请好友界面");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("私密房间邀请好友界面");
		MobclickAgent.onPause(this);
	}
}
