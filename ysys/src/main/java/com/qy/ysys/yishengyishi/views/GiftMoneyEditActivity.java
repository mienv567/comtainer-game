package com.qy.ysys.yishengyishi.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class GiftMoneyEditActivity extends BaseTitleActivity {

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("添加礼金");
        titleView.setRightImage(R.mipmap.ic_add);
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_gift_money;
    }

    @Override
    public void onClick(View v) {

    }
}
