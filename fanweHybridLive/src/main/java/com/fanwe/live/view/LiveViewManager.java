package com.fanwe.live.view;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by android on 2017/3/14.
 */
public class LiveViewManager {

    private LiveVTabView mLiveVTabView;

    private LiveHMenuView mLiveHMenuView;

    private FrameLayout mTabContainer;

    private FrameLayout mMenuContainer;

    private FragmentManager mFragmentManager;

    private int mRole;

    public LiveViewManager(FrameLayout flTabContainer, FrameLayout flMenuContainer,
                           FragmentManager fragmentManager, int role) {
        mTabContainer = flTabContainer;
        mMenuContainer = flMenuContainer;
        mFragmentManager = fragmentManager;
        mRole = role;
    }

    public void showV() {
        hideH();
        if (mLiveVTabView == null) {
            mLiveVTabView = new LiveVTabView(mTabContainer, mFragmentManager);
        }
        mLiveVTabView.show();
    }

    public void hideV() {
        if (mLiveVTabView != null) {
            mLiveVTabView.hide();
        }
    }

    public void showH() {
        hideV();
        if (mLiveHMenuView == null) {
            mLiveHMenuView = new LiveHMenuView(mMenuContainer, mRole);
        }
        mLiveHMenuView.show();
    }

    public void hideH() {
        if (mLiveHMenuView != null) {
            mLiveHMenuView.hide();
        }
    }

    public void hideMenu() {
        mLiveHMenuView.hideTopOrBottomMenu();
    }

    public void showMenu() {
        mLiveHMenuView.show();
    }

    public boolean onBackPressed() {
        if (mLiveHMenuView != null && mLiveHMenuView.onBackPressed()) {
            return true;
        }
        return false;
    }

    public void switchUpMenuSelectState(View view) {
        mLiveHMenuView.switchUpMenuSelectState(view);
    }
}
