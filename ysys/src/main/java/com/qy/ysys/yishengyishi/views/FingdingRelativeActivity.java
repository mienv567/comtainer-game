package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.HandlerManager;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class FingdingRelativeActivity extends BaseTitleActivity {
    private TextView tv_finding;
    private Runnable jumpMainActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(FingdingRelativeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_fingding_relative;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("寻找情人");
        titleView.setLeftTitle("跳过");
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                HandlerManager.getMainHamdler().post(jumpMainActivity);
            }

            @Override
            public void onClickRight() {

            }
        });
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tv_finding = (TextView) findViewById(R.id.tv_finding);
        tv_finding.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_finding) {
//            ToastUtil.showToast("finding ...");
            HandlerManager.getMainHamdler().postDelayed(jumpMainActivity, 0);
            finish();
        }
    }
}
