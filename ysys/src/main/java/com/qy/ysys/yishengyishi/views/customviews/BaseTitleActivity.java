package com.qy.ysys.yishengyishi.views.customviews;

import android.view.View;

/**
 * Created by TonyChen on 2016/12/31.
 */

public abstract class BaseTitleActivity extends BaseActivity {
    protected View titleView = null;

    public ITitleView getTitleView() {
        return (ITitleView) titleView;
    }

    @Override
    protected boolean shouldAddTitle() {
        return true;
    }

    @Override
    protected View setTitleViewByView() {
        titleView = new CustomTitleView(this);
        return (View) titleView;
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        initTitleBar(getTitleView());
    }

    protected abstract void initTitleBar(ITitleView titleView);

}
