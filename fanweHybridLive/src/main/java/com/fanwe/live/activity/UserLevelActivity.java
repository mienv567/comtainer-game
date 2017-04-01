package com.fanwe.live.activity;

import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

public class UserLevelActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_level);
        initTitle();
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.main_color);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.my_level));
        mTitle.setBackgroundColor(getResources().getColor(R.color.main_color));
    }
}
