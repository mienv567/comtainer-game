package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

public class UserInviteRecordActivity extends BaseActivity {
    public static final String EXTRA_INVITE_USER_NUM = "extra_invite_user_num";
    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.tv_invite_num)
    private TextView tv_invite_num;
    private int mInvitedNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_user_invite_record);
        initIntent();
        initListener();
        initData();
    }

    private void initIntent(){
        Intent intent = getIntent();
        mInvitedNum = intent.getIntExtra(EXTRA_INVITE_USER_NUM,0);
    }

    private void initData(){
        tv_invite_num.setText(Html.fromHtml(String.format(getResources().getString(R.string.user_already_invited), mInvitedNum)));
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
