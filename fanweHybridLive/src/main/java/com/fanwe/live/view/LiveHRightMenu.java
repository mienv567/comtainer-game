package com.fanwe.live.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomPropsView;
import com.fanwe.live.appview.room.RoomView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.AppTaskModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LogUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */
public class LiveHRightMenu extends RoomView implements View.OnClickListener {

    private TaskPanel mTaskPanel;

    private RoomPropsView mGiftPanel;

    private OrderPanel mOrderPanel;

    private View mRootView;

    private FrameLayout flPanelContainer;

    private int mRole;
    private final TextView tv_task;
    private final TextView tv_gift;
    private final TextView tv_share;
    private final TextView tv_order;

    public LiveHRightMenu(Context context, int role) {
        super(context);
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_live_h_right_menu, null);
        tv_task = (TextView) mRootView.findViewById(R.id.tv_task);
        tv_gift = (TextView) mRootView.findViewById(R.id.tv_gift);
        tv_share = (TextView) mRootView.findViewById(R.id.tv_share);
        tv_order = (TextView) mRootView.findViewById(R.id.tv_order);
        tv_task.setOnClickListener(this);
        tv_gift.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_order.setOnClickListener(this);
        this.mRole = role;
        flPanelContainer = (FrameLayout) mRootView.findViewById(R.id.fl_panel_container_r);
    }

    public View getRootView() {
        return mRootView;
    }

    private void showTaskPanel(View v) {
        if (mTaskPanel == null) {
            mTaskPanel = new TaskPanel(v.getContext(), mRole);
        }
        mTaskPanel.showTaskPanel(v);
    }

    private void toggleTaskPanel(View v) {
        if (mTaskPanel != null && mTaskPanel.isShown()) {
            hidePanel();
            return;
        }
        if (isShown()) {
            hidePanel();
        }
        requestCurrentTask(v);
    }

    private void showGiftPanel(View v) {
        if (mGiftPanel == null) {
            mGiftPanel = new RoomPropsView(v.getContext());
        }
        if (!mGiftPanel.isVisible()) {
            mGiftPanel.setVisibility(View.VISIBLE);
        }
        //EventBus.getDefault().post(LiveVideoActivity.CODE_LIANMAI);
        SDViewUtil.addView(flPanelContainer, mGiftPanel);
    }

    private void toggleGiftPanel(View v) {
        if (mGiftPanel != null && mGiftPanel.isShown()) {
            hidePanel();
            return;
        }
        if (isShown()) {
            hidePanel();
        }
        showGiftPanel(v);
    }

    private void showOrderPanel(View v) {
        if (mOrderPanel == null) {
            mOrderPanel = new OrderPanel(v.getContext(), mRole);
        }
        //        我在这里调出连麦
        SDViewUtil.addView(flPanelContainer, mOrderPanel.getRootView());
    }

    private void toggleOrderPanel(View v) {
        if (mOrderPanel != null && mOrderPanel.isShown()) {
            hidePanel();
            return;
        }
        if (isShown()) {
            hidePanel();
        }
        showOrderPanel(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_task:
                toggleTaskPanel(flPanelContainer);
                break;
            case R.id.tv_gift:
                toggleGiftPanel(v);
                break;
            case R.id.tv_share:
                hidePanel();
                EventBus.getDefault().post(12);
                break;
            case R.id.tv_order:
                //toggleOrderPanel(v);
                hidePanel();
                break;
        }
        switchSelectState(v);
        //发送反转上下菜单栏当前状态的消息
        EventBus.getDefault().post(LiveHRightMenu.this);
    }

    public boolean isShown() {
        if (flPanelContainer.getChildCount() <= 0) {
            return false;
        }
        return true;
    }

    public void hidePanel() {
        flPanelContainer.removeAllViews();
    }

    private void loadTaskPanel(View v){
        if (mTaskPanel == null) {
            mTaskPanel = new TaskPanel(v.getContext(), mRole);
        }
        mTaskPanel.loadTaskPanel(v);
    }

    private void requestCurrentTask(final View v) {
        loadTaskPanel(v);
        UserModel userInfo = UserModelDao.query();
        LogUtils.logI(userInfo.toString());
        CommonInterface.requestCurrentTask(null, getLiveInfo().getCreaterId(), new AppRequestCallback<AppTaskModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                /**{"code":"0000","message":"操作成功！","returnObj":{
                 "missionInfo":{
                 "missionId":10,"groupId":2,"title":"呵呵",
                 "description":"呵呵","limitTime":null,
                 "score":2,"preMissionId":null,"type":1,
                 "medal":2
                 },
                 "missionProcess":{
                 "startTime":"2017-03-16 06:56:12","status":0,
                 "processId":4,"userId":41,"currentMissionId":10,
                 "missionGroupId":2,"currentMissionTitle":"玩个鸟"
                 }
                 }
                 }*/
                if (actModel.isOk()) {
                    showTaskPanel(v);
                    mTaskPanel.setAppTaskModel(actModel);
                } else {
                    hidePanel();
                    SDToast.showToast("暂未指派任务");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                hidePanel();
                SDToast.showToast("网络异常，请稍候再试");
            }
        });
    }

    public void switchSelectState(View view) {
        if (!view.isSelected()) {
            tv_task.setSelected(false);
            tv_gift.setSelected(false);
            tv_share.setSelected(false);
            tv_order.setSelected(false);
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
    }
}
