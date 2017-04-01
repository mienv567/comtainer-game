package com.fanwe.live.activity;

import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;

public class MyTaskActivity extends BaseTitleActivity {

    @Override
    protected int onCreateContentView() {
        return R.layout.activity_my_task;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTitle.setMiddleTextTop(getString(R.string.my_task));
    }
}
