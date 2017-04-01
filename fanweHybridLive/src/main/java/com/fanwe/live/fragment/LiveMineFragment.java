package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.LiveUserHomeReplayActivity;
import com.fanwe.live.activity.LiveUserPhotoActivity;
import com.fanwe.live.activity.LiveUserSettingActivity;
import com.fanwe.live.activity.MyTaskActivity;
import com.fanwe.live.activity.PropsListActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by kevin.liu on 2017/3/27.
 * 个人中心
 */

public class LiveMineFragment extends BaseFragment {

    @ViewInject(R.id.tv_video_count)
    TextView mTvVideoCount;
    @ViewInject(R.id.tv_setting)
    TextView mTvSetting;
    @ViewInject(R.id.iv_head_image)
    CircleImageView mIvHeadImage;
    @ViewInject(R.id.tv_user_name)
    TextView mTvUserName;
    @ViewInject(R.id.tv_fans_count)
    TextView mTvFansCount;
    @ViewInject(R.id.tv_like_count)
    TextView mTvLikeCount;
    @ViewInject(R.id.tv_diamonds_count)
    TextView mTvDiamondsCount;
    @ViewInject(R.id.tv_msg)
    TextView mTvMsg;
    @ViewInject(R.id.tv_my_task)
    TextView mTvMyTask;
    @ViewInject(R.id.tv_vip)
    TextView mTvVip;
    @ViewInject(R.id.tv_recharge)
    TextView mTvRecharge;
    @ViewInject(R.id.tv_my_live)
    TextView mTvMyLive;
    @ViewInject(R.id.tv_props_list)
    TextView mTvPropsList;
    @ViewInject(R.id.tv_rank_list)
    TextView mTvRankList;
    @ViewInject(R.id.tv_my_account)
    TextView mTvMyAccount;

    private UserModel mUserModel;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_mine;
    }

    @Override
    protected void init() {
        register();
        initUserInfo();
    }

    private void register() {
        mTvSetting.setOnClickListener(this);
        mIvHeadImage.setOnClickListener(this);
        mTvMsg.setOnClickListener(this);
        mTvMyTask.setOnClickListener(this);
        mTvVip.setOnClickListener(this);
        mTvRecharge.setOnClickListener(this);
        mTvMyLive.setOnClickListener(this);
        mTvPropsList.setOnClickListener(this);
        mTvRankList.setOnClickListener(this);
        mTvMyAccount.setOnClickListener(this);
    }

    private void initUserInfo() {
        mUserModel = UserModelDao.query();
        SDViewBinder.setTextView(mTvVideoCount, mUserModel.getVideoCount() + "");
        SDViewBinder.setImageView(getActivity(), mUserModel.getHeadImage(), mIvHeadImage, R.drawable.ic_default_head);
        SDViewBinder.setTextView(mTvUserName, mUserModel.getNickName());
        SDViewBinder.setTextView(mTvFansCount, mUserModel.getFansCount() + "");
        SDViewBinder.setTextView(mTvLikeCount, mUserModel.getLikesCount() + "");
        SDViewBinder.setTextView(mTvDiamondsCount, mUserModel.getDiamonds() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                clickSetting();
                break;
            case R.id.iv_head_image:
                clickHead();
                break;
            case R.id.tv_msg:
                clickMsg();
                break;
            case R.id.tv_my_task:
                clickMyTask();
                break;
            case R.id.tv_vip:
                clickVIP();
                break;
            case R.id.tv_recharge:
                clickRecharge();
                break;
            case R.id.tv_my_live:
                clickMyLive();
                break;
            case R.id.tv_props_list:
                clickPropsList();
                break;
            case R.id.tv_rank_list:
                clickRankList();
                break;
            case R.id.tv_my_account:
                clickMyAccount();
                break;
        }
    }

    /**
     * 设置
     */
    private void clickSetting() {
        Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 消息
     */
    private void clickMsg() {

    }

    /**
     * 我的任务
     */
    private void clickMyTask() {
        Intent intent = new Intent(getActivity(), MyTaskActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * vip
     */
    private void clickVIP() {
    }

    /**
     * 充值
     */
    private void clickRecharge() {
        Intent intent = new Intent(getActivity(), LiveRechargeActivity.class);
        getActivity().startActivity(intent);
    }

    private void clickMyLive() {
        Intent intent = new Intent(getActivity(), LiveUserHomeReplayActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 物品清单
     */
    private void clickPropsList() {
        Intent intent = new Intent(getActivity(), PropsListActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 排行榜
     */
    private void clickRankList() {

    }

    /**
     * 我的账户
     */
    private void clickMyAccount() {
        Intent intent = new Intent(getActivity(), LiveUserEditActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 点击头像
     */
    private void clickHead() {
        if (mUserModel != null) {
            Intent intent = new Intent(getActivity(), LiveUserPhotoActivity.class);
            intent.putExtra(LiveUserPhotoActivity.EXTRA_USER_IMG_URL, mUserModel.getHeadImage());
            getActivity().startActivity(intent);
        }
    }
}
