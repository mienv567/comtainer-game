package com.fanwe.live.view;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveVideoActivity;

/**
 * Created by android on 2017/3/14.
 */
public class LiveHMenuView implements View.OnClickListener {

    private FrameLayout mMenuContainer;

    private LiveHUpMenu mLiveHUpMenu;

    private LiveHRightMenu mLiveHRightMenu;

    private LiveHBottomMenu mLiveHBottomMenu;

    private int mRole;

    public LiveHMenuView(FrameLayout menuContainer, int role) {
        mMenuContainer = menuContainer;
        mRole = role;
        mLiveHRightMenu = new LiveHRightMenu(mMenuContainer.getContext(), role);
        if (mRole == LiveVideoActivity.ROLE_VIEWER) {
            mLiveHBottomMenu = new LiveHBottomMenu(menuContainer.getContext());
        }
        mLiveHUpMenu = new LiveHUpMenu(mMenuContainer.getContext());
    }

    public void show() {
        mMenuContainer.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        SDViewUtil.addView(mMenuContainer, mLiveHRightMenu.getRootView(), params);
        showTopOrBottomMenu();
        mMenuContainer.setOnClickListener(this);
    }

    public void hide() {
        mMenuContainer.setOnClickListener(null);
        mMenuContainer.removeAllViews();
        mMenuContainer.setVisibility(View.GONE);
    }

    public void hideAllMenu() {
        hideTopOrBottomMenu();
        mMenuContainer.removeView(mLiveHRightMenu.getRootView());
    }

    private void showTopOrBottomMenu() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if (mRole == LiveVideoActivity.ROLE_CREATRE) {
            params.gravity = Gravity.TOP;
            SDViewUtil.addView(mMenuContainer, mLiveHUpMenu.getRootView(), params);
        } else if (mRole == LiveVideoActivity.ROLE_VIEWER) {
            params.gravity = Gravity.BOTTOM;
            SDViewUtil.addView(mMenuContainer, mLiveHBottomMenu.getRootView(), params);

            mLiveHUpMenu.getRootView().findViewById(R.id.iv_mute).setVisibility(View.INVISIBLE);
            mLiveHUpMenu.getRootView().findViewById(R.id.iv_turn).setVisibility(View.INVISIBLE);
            mLiveHUpMenu.getRootView().findViewById(R.id.iv_flash).setVisibility(View.INVISIBLE);
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP;
            SDViewUtil.addView(mMenuContainer, mLiveHUpMenu.getRootView(), params);
        }
    }

    public void hideTopOrBottomMenu() {
        if (mLiveHBottomMenu != null && mLiveHBottomMenu.isShown()) {
            mMenuContainer.removeView(mLiveHBottomMenu.getRootView());
        }
        if (mLiveHUpMenu != null && mLiveHUpMenu.isShown()) {
            mMenuContainer.removeView(mLiveHUpMenu.getRootView());
        }
    }

    /**
     * 点击空白区域
     * 反转所有菜单的显示状态
     * 当右侧面板已经打开的时候不能反转
     */
    private void toggle() {
        if (mLiveHRightMenu.isShown()) {
            return;
        }
        if (mLiveHRightMenu.getRootView().isShown()) {
            hideAllMenu();
        } else {
            show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flMenuContainer:
                toggle();
                break;
        }
    }

    /**
     * 当右侧面板是开启的时候
     * 后退应该先隐藏面板
     * 并且把上下菜单栏显示
     *
     * @return true 消费后退事件 false 不消费后退事件
     */
    public boolean onBackPressed() {
        if (mLiveHRightMenu.isShown()) {
            mLiveHRightMenu.hidePanel();
            showTopOrBottomMenu();
            return true;
        }
        return false;
    }

    public void switchUpMenuSelectState(View view) {
        mLiveHUpMenu.switchUpMenuSelectState(view);
    }
}
