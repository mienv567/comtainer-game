package com.fanwe.live.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.activity.room.LiveVideoActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.TaskOrGoodsDetailDialog;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.AppTaskModel;

import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

public class LiveTaskFragment extends BaseFragment {

    private LiveActivity mActivity;

    @ViewInject(R.id.fl_task)
    private FrameLayout fl_task;
    @ViewInject(R.id.tv_mission_title)
    private TextView tv_mission_title;
    @ViewInject(R.id.tv_no_tasks)
    private TextView tv_no_tasks;
    @ViewInject(R.id.tv_handle_task)
    private TextView tv_handle_task;
    private String missionId;
    private int status;
    private AppTaskModel.MissionInfo mMissionInfo;
    private AppTaskModel.MissionProcess mMissionProcess;

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_live_task;
    }

    @Override
    protected void init() {
        super.init();
        mActivity = (LiveActivity) getActivity();
        fl_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskDetailDialog();
            }
        });
        requestTasks();
    }

    private void showTaskDetailDialog() {
        if (mMissionInfo == null || TextUtils.isEmpty(mMissionInfo.description)) {
            EventBus.getDefault().post(LiveVideoActivity.CODE_LIANMAI);
            SDToast.showToast("暂未指派任务");
            return;
        }
        TaskOrGoodsDetailDialog taskDetailDialog = new TaskOrGoodsDetailDialog(mActivity);
        taskDetailDialog.setTitle(getString(R.string.task_detail));
        taskDetailDialog.setContent(mMissionInfo.description);
        taskDetailDialog.setConfirmString(status == 1 ?
                getString(R.string.click_to_complete) : getString(R.string.confirm));
        taskDetailDialog.setConfirmClickListener(new TaskOrGoodsDetailDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm() {
                if (status == 1) {
                    EventBus.getDefault().post(LiveVideoActivity.CODE_LIANMAI);
                }
            }
        });
        taskDetailDialog.show();
    }

    public void onEventMainThread(EImOnNewMessages event) {
        if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_TIPS_MSG) {
            requestTasks();
        }
    }

    private void requestTasks() {
        LogUtil.d("requestTasks--");
        CommonInterface.requestCurrentTask(missionId, mActivity.getCreaterId(), new AppRequestCallback<AppTaskModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                LogUtil.d(actModel.toString());
                tv_no_tasks.setVisibility(View.GONE);
                if (rootModel.isOk()) {
                    if (actModel != null) {
                        fl_task.setVisibility(View.VISIBLE);
                        mMissionInfo = actModel.missionInfo;
                        mMissionProcess = actModel.missionProcess;
                        missionId = mMissionInfo.missionId;
                        tv_mission_title.setText(mMissionProcess.currentMissionTitle);
                        updateStatus(mMissionProcess.status);
                    }
                } else {
                    fl_task.setVisibility(View.GONE);
                    tv_no_tasks.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateStatus(int status) {
        this.status = status;
        switch (status) {
            case 0:
                tv_handle_task.setText("未领取");
                tv_handle_task.setEnabled(false);
                break;
            case 1:
                tv_handle_task.setText("进行中");
                tv_handle_task.setEnabled(true);
                break;
            case -1:
                tv_handle_task.setText("已完成");
                tv_handle_task.setEnabled(false);
                break;
        }
    }
}
