package com.qy.ysys.yishengyishi.views;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class MyInfoActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.frag_me_myinfo;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("我的资料");
        titleView.setRightImage(R.mipmap.ic_my_task);
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                MyInfoActivity.this.finish();
            }

            @Override
            public void onClickRight() {
                ToastUtil.showToast("功能开发中...");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
