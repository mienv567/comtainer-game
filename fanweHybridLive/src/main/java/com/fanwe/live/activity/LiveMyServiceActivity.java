package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveMyServiceActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_my_service);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("集装箱客服");
    }

    @OnClick(R.id.tv_feedback)
    public void onViewClicked() {
        Intent intent = new Intent(this, LiveFeedBackActivity.class);
        startActivity(intent);
    }
}
