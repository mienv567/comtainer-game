package com.qy.ysys.yishengyishi.views;

import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class AddShuoShuoActivity extends BaseTitleActivity {

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_addshuoshuo;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("说说");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setRightTitle("发表");
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onClickRight() {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
