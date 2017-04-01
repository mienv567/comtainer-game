package com.qy.ysys.yishengyishi.views;

import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class SettingActivity extends BaseTitleActivity {

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("设置");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                SettingActivity.this.finish();
            }

            @Override
            public void onClickRight() {

            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_setting;
    }

    @Override
    public void onClick(View v) {

    }
}
