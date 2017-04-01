package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;


public class LiveSetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.ll_titlebar_left)
    private LinearLayout ll_titlebar_left;
    @ViewInject(R.id.tv_get_ver_code)
    private TextView tv_get_ver_code;
    @ViewInject(R.id.tv_get_audio_code)
    private TextView tv_get_audio_code;
    @ViewInject(R.id.tv_enter)
    private TextView tv_enter;

    @Override
    protected int onCreateContentView() {
        return R.layout.activity_live_set_password;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        initListener();
    }

    private void initListener() {
        ll_titlebar_left.setOnClickListener(this);
        tv_get_ver_code.setOnClickListener(this);
        tv_get_audio_code.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_titlebar_left:
                finish();
                break;
            case R.id.tv_get_audio_code://获取语音验证码

                break;
            case R.id.tv_enter:
                break;
        }
    }
}
