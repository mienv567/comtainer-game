package com.qy.ysys.yishengyishi.views.customviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by tony.chen on 2016/12/30.
 */

public abstract class BaseTitleFragment extends BaseFragment {


    protected boolean shouldShowTitle() {
        return true;
    }

    protected View titleView = null;

    public ITitleView getTitleView() {
        return (ITitleView) titleView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = setFragmentContentView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,contentView);
        initView(contentView);
        if (shouldShowTitle()) {
            titleView = setTitleViewByView();
            if (titleView == null) {
                int resId = setTitleViewByResId();
                if (resId != 0) {
                    titleView = LayoutInflater.from(getActivity()).inflate(resId, null);
                }
            }
            if (null == titleView) {
                throw new RuntimeException("当前fragment需要设置标题,请覆写setTitleViewByView()或者覆写()以完成标题栏设置,如果不想设置标题栏请覆写shouldAddTitle()并返回false!");
            }

            initTitleBar(getTitleView());
            LinearLayout linAll = new LinearLayout(getActivity());
            linAll.setOrientation(LinearLayout.VERTICAL);
            linAll.addView(titleView, createTitleViewLayoutParams());
            linAll.addView(contentView, createContentViewLayoutParams());
            linAll.setFitsSystemWindows(true);

            return linAll;
        }
        return contentView;
    }

    protected void initView(View contentView) {
    }

    protected abstract void initTitleBar(ITitleView titleView);

    protected View setTitleViewByView() {
        return null;
    }

    protected int setTitleViewByResId() {
        return 0;
    }

    protected LinearLayout.LayoutParams createTitleViewLayoutParams() {

        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected LinearLayout.LayoutParams createContentViewLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
