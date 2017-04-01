package com.qy.ysys.yishengyishi.views;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.presenters.SplashHelper;
import com.qy.ysys.yishengyishi.presenters.viewinterface.ISplashView;
import com.qy.ysys.yishengyishi.views.customviews.BaseActivity;

/**
 * Created by tony.chen on 2017/1/3.
 */

public class SplashActivity extends BaseActivity implements ISplashView {
    private SplashHelper splashHelper = null;
    private ImageView iv_splash;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashHelper.onDestory();
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_splash;
    }

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        splashHelper = new SplashHelper(this, this);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
    }

    @Override
    protected void afterOnCreate() {
        splashHelper.showSplashImange();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        splashHelper.onBackPressed();
    }

    @Override
    public void showSplashImange() {
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv_splash, "alpha", 0.2f, 1f);
        oa.setDuration(1000);
    }

    @Override
    public void onClick(View v) {

    }
}
