package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.GetMyInviteCodeModel;
import com.qy.ysys.yishengyishi.model.SubmitInviteCodeModel;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationActivity extends BaseTitleActivity {

    @BindView(R2.id.tv_invite_code)
    TextView mInviteCode;
    @BindView(R2.id.et_invite_code)
    EditText mEInviteCode;
    @BindView(R2.id.tv_clean)
    TextView mClean;
    @BindView(R2.id.tv_confirm)
    TextView mConfirm;
    @BindView(R2.id.has_invite_code)
    TextView mHasInvitedCode;
    private UserInfo mUserInfo;
    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_invitation;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("邀请码");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                InvitationActivity.this.finish();
            }

            @Override
            public void onClickRight() {
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        initUser();
        initListener();
    }

    private void initUser(){
        mUserInfo = AppImpl.getCurrentUser();
        if(mUserInfo != null){
            if(mUserInfo.getIsInvitate() == UserInfo.IS_INVITED){
                showInviteInfo(mUserInfo.getInvitationCode());
            }
        }
    }

    private void showInviteInfo(String inviteCode){
        mClean.setVisibility(View.GONE);
        mConfirm.setVisibility(View.GONE);
        mEInviteCode.setVisibility(View.GONE);
        mHasInvitedCode.setVisibility(View.VISIBLE);
        mHasInvitedCode.setText(String.format(getString(R.string.has_invite_code),inviteCode));
    }

    private void initListener(){
        mClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEInviteCode.setText("");
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inviteCode = mEInviteCode.getText() + "".replace(" ", "");
                if (!TextUtils.isEmpty(inviteCode)) {
                    if (inviteCode.length() == 6) {
                        requestSubmit(inviteCode);
                    } else {
                        ToastUtil.showToast("邀请码不能低于6位");
                    }
                }

            }
        });
    }

    private void refreshUserModel(SubmitInviteCodeModel model){
        if(mUserInfo != null){
            mUserInfo.setIsInvitate(UserInfo.IS_INVITED);
            mUserInfo.setInvitationCode(model.getReturnObj().getInvitationCode());
            mUserInfo.setInvitationTime(model.getReturnObj().getCreateTime());
            mUserInfo.setFromUserId(model.getReturnObj().getFromUserId());
            AppImpl.setCurrentUser(mUserInfo);
        }
    }

    private void requestSubmit(String inviteCode){
        RequestInterface.submitInviteCode(inviteCode, new Callback<SubmitInviteCodeModel>() {
            @Override
            public void onResponse(Call<SubmitInviteCodeModel> call, Response<SubmitInviteCodeModel> response) {
                if (response.body().isSuccess()) {
                    refreshUserModel(response.body());
                    showInviteInfo(response.body().getReturnObj().getInvitationCode());
                } else {
                    ToastUtil.showToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SubmitInviteCodeModel> call, Throwable t) {

            }
        });
    }

    private void requestData(){
        RequestInterface.getMyInviteCode(new Callback<GetMyInviteCodeModel>() {
            @Override
            public void onResponse(Call<GetMyInviteCodeModel> call, Response<GetMyInviteCodeModel> response) {
                if (response.body().isNotLogin()) {
                    Logger.d(response.body().getCode() + response.body().getMessage());
                    SPUtils.setParam("uid", 0);
                    startActivity(new Intent(InvitationActivity.this, MobileRegisterViewActivity.class));
                } else {
                    mInviteCode.setText(response.body().getReturnObj().getInvitationCode());
                }
            }

            @Override
            public void onFailure(Call<GetMyInviteCodeModel> call, Throwable t) {
                Logger.e(t + "");
            }

        });
    }
}
