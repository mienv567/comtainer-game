package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.views.FeedBackActivity;
import com.qy.ysys.yishengyishi.views.GiftMoneyOverviewActivity;
import com.qy.ysys.yishengyishi.views.InvitationActivity;
import com.qy.ysys.yishengyishi.views.MyInfoActivity;
import com.qy.ysys.yishengyishi.views.SettingActivity;

import butterknife.BindView;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class FragMe extends BaseTitleFragment implements View.OnClickListener {

    @BindView(R2.id.ll_myinfo)
    View ll_myinfo;
    @BindView(R2.id.ll_giftbook)
    View ll_giftbook;
    @BindView(R2.id.ll_me_setting)
    View ll_setting;
    @BindView(R2.id.ll_me_invitecode)
    View ll_invitecode;
    @BindView(R2.id.ll_me_feedback)
    View ll_feedback;
    @BindView(R2.id.tv_user_name)
    TextView mUserName;
    private UserInfo mUserInfo;
    @Nullable
    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_me, null);
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText(getResources().getString(R.string.menu_me) + "的");
        titleView.setRightImage(R.mipmap.ic_my_task);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected View setTitleViewByView() {
        return new CustomTitleView(getActivity());
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        initUser();
        ll_myinfo.setOnClickListener(this);
        ll_giftbook.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_invitecode.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
    }

    private void initUser(){
        mUserInfo = AppImpl.getCurrentUser();
        if(mUserInfo != null) {
            mUserName.setText(mUserInfo.getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ll_myinfo) {
            Intent intent = new Intent(getActivity(), MyInfoActivity.class);
            startActivity(intent);
        } else if (v == ll_giftbook) {
            Intent intent = new Intent(getActivity(), GiftMoneyOverviewActivity.class);
            startActivity(intent);
        } else if (v == ll_setting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        } else if (v == ll_invitecode) {
            Intent intent = new Intent(getActivity(), InvitationActivity.class);
            startActivity(intent);
        } else if (v == ll_feedback) {
            Intent intent = new Intent(getActivity(), FeedBackActivity .class);
            startActivity(intent);
        }
    }
}
