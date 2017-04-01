package com.qy.ysys.yishengyishi.views.customviews;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * Created by tony.chen on 2016/12/30.
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    private View titleView = null;
    protected View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
//         设置系统状态栏颜色
        setStatusBar();

//         设置contentview,尽量通过覆写
        customSetContentView(savedInstanceState);
        ButterKnife.bind(this);
//         初始化view
        initView(mContentView);

//         view初始化完毕
        afterOnCreate();

    }

    /**
     * 在oncreate()调用前会调用该方法
     *
     * @param savedInstanceState
     */
    protected void beforeOnCreate(Bundle savedInstanceState) {
    }

    /**
     * 覆写该方法,该方法将在oncreate()执行完initview执行
     */
    protected void afterOnCreate() {
    }

    /**
     * 方法在setContentView以后执行,用来初始化view
     */
    protected void initView(View contentView) {

    }

    /**
     * 设置activity的content内容
     *
     * @param savedInstanceState
     */
    private void customSetContentView(Bundle savedInstanceState) {
        int layoutId = setContentViewByLayoutID();
        if (layoutId != 0) {
            setContentView(layoutId);
        } else {
            throw new RuntimeException("You must overwrite the method setContentViewByLayoutID(),and return LayoutID!");
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentView = getLayoutInflater().inflate(layoutResID, null);
        setContentView(mContentView);
    }

    @Override
    public void setContentView(View view) {
        if (shouldAddTitle()) {
            titleView = setTitleViewByView();
            if (titleView == null) {
                int resId = setTitleViewByResId();
                if (resId != 0) {
                    titleView = LayoutInflater.from(this).inflate(resId, null);
                }
            }
            if (null == titleView) {
                throw new RuntimeException("当前activity需要设置标题,请覆写setTitleViewByView()或者覆写()以完成标题栏设置,如果不想设置标题栏请覆写shouldAddTitle()并返回false!");
            }
            LinearLayout linAll = new LinearLayout(this);
            linAll.setOrientation(LinearLayout.VERTICAL);
            linAll.addView(titleView, createTitleViewLayoutParams());
            linAll.addView(view, createContentViewLayoutParams());
            linAll.setFitsSystemWindows(true);
            super.setContentView(linAll);
        } else {
            super.setContentView(view);
        }
    }

    protected LinearLayout.LayoutParams createTitleViewLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected LinearLayout.LayoutParams createContentViewLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    protected boolean shouldAddTitle() {
        return false;
    }

    /**
     * 覆写该方法,返回需要设置activity的布局id,以设置activity的标题栏
     *
     * @return
     */
    protected int setTitleViewByResId() {
        return 0;
    }

    /**
     * 覆写该方法,返回需要设置标题栏的view,以设置activity的标题栏
     *
     * @return
     */
    protected View setTitleViewByView() {
        return null;
    }

    protected abstract int setContentViewByLayoutID();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置系统状态栏颜色
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorStatuBar));
    }

    public static void showToast(String msg) {

    }


}
