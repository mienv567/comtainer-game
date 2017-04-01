package com.fanwe.live.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveVideoActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */
public class LiveHUpMenu implements View.OnClickListener {

    private ViewGroup mRootView;

    private FrameLayout flPanelContainer;

    public LiveHUpMenu(Context context) {
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_live_h_up_menu, null);
        //mRootView.findViewById(R.id.iv_barrage).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_mute).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_turn).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_flash).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        flPanelContainer = (FrameLayout) mRootView.findViewById(R.id.fl_panel_container_t);
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(LiveVideoActivity.CODE_BACK);
                break;
        }
        EventBus.getDefault().post(v);
    }

    public boolean isShown() {
        if (mRootView == null || !mRootView.isShown()) {
            return false;
        }
        return true;
    }

    public void switchUpMenuSelectState(View view) {
        view.setSelected(!view.isSelected());
    }
}
