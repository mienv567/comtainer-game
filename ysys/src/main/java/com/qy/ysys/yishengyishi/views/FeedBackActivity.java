package com.qy.ysys.yishengyishi.views;

import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class FeedBackActivity extends BaseTitleActivity {


    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("意见反馈");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                FeedBackActivity.this.finish();
            }

            @Override
            public void onClickRight() {
            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_feedback;
    }

    @Override
    public void onClick(View v) {

    }
}
