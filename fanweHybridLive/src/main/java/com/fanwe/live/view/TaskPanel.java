package com.fanwe.live.view;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveVideoActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveTaskDetailDialog;
import com.fanwe.live.model.AppTaskModel;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */

public class TaskPanel implements View.OnClickListener {

    private View mRootView;

    private TextView tvTask;

    private TextView tvReceive;

    private ProgressBar pbTask;

    private int mRole;

    private AppTaskModel mAppTaskModel;

    private LiveTaskDetailDialog mDetailDialog;

    public TaskPanel(Context context, int role) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.panel_live_h_task, null);
        tvReceive = (TextView) mRootView.findViewById(R.id.tv_receive);
        tvReceive.setOnClickListener(this);
        pbTask = (ProgressBar) mRootView.findViewById(R.id.pbTask);
        if (mRole == LiveVideoActivity.ROLE_CREATRE) {
            tvReceive.setText("领取");
        } else {
            tvReceive.setText("详情");
        }
        tvTask = (TextView) mRootView.findViewById(R.id.tv_task);
        tvTask.setOnClickListener(this);
        this.mRole = role;
    }

    public View getRootView() {
        return mRootView;
    }

    public boolean isShown() {
        return mRootView.isShown();
    }

    public void setAppTaskModel(final AppTaskModel model) {
        this.mAppTaskModel = model;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setText();
            return;
        }
        tvTask.post(new Runnable() {
            @Override
            public void run() {
                setText();
            }
        });
    }

    private void setText() {
        if (mAppTaskModel == null || mAppTaskModel.getStatus() != 1) {
            tvTask.setText("任务暂未指派！");
            tvReceive.setVisibility(View.INVISIBLE);
            return;
        }
        tvReceive.setVisibility(View.VISIBLE);
        if (mAppTaskModel == null || mAppTaskModel.missionInfo == null
                || TextUtils.isEmpty(mAppTaskModel.missionInfo.title)) {
            tvTask.setText("");
        } else {
            tvTask.setText(mAppTaskModel.missionInfo.title);
        }
        updateTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_task:
            case R.id.tv_receive:
                if (mAppTaskModel == null || mAppTaskModel.missionProcess == null || mAppTaskModel.missionInfo == null) {
                    SDToast.showToast("未获取到任务信息");
                    return;
                } else {
                    showTaskDetail((View) v.getParent().getParent());
                }
                break;
        }
    }

    private void showTaskDetail(View v) {
        String tip = "知道了";
        if (mAppTaskModel.missionProcess.status == 0 && mRole == LiveVideoActivity.ROLE_CREATRE) {
            tip = "点击领取任务";
        } else if (mAppTaskModel.missionProcess.status == 1 && mRole == LiveVideoActivity.ROLE_VIEWER) {
            tip = "点击完成任务";
        }
        if (mDetailDialog == null) {
            mDetailDialog = new LiveTaskDetailDialog(mRootView.getContext());
        }
        mDetailDialog.setAction(tip);
        mDetailDialog.setContent(mAppTaskModel.missionInfo.description);
        mDetailDialog.setOnClickListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAppTaskModel.missionProcess.status == 0
                        && mRole == LiveVideoActivity.ROLE_CREATRE) {
                    SDViewUtil.removeViewFromParent(mRootView);
                    receiveTask();
                } else if (mAppTaskModel.missionProcess.status == 1
                        && mRole == LiveVideoActivity.ROLE_VIEWER) {
                    EventBus.getDefault().post(LiveVideoActivity.CODE_LIANMAI);
                }
            }
        });
        mDetailDialog.showLeft(v);
    }

    private void receiveTask() {
        CommonInterface.requestReceiveTask(mAppTaskModel.missionInfo.missionId, new AppRequestCallback<String>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                /**
                 *{"code":"0000","message":"操作成功","returnObj":{"startTime":"2017-03-16 21:16:55","processId":4,"status":1,"userId":41,"currentMissionId":10,"missionGroupId":2,"currentMissionTitle":"玩个鸟"}}
                 */
                mAppTaskModel.missionProcess.status = 1;
                updateTask();
            }
        });
    }

    private void setButton() {
        if (mRole == LiveVideoActivity.ROLE_VIEWER) {
            tvReceive.setText("详情");
            tvReceive.setVisibility(View.VISIBLE);
            return;
        }
        if (mAppTaskModel.missionProcess.status == 0) {
            tvReceive.setText("领取");
            tvReceive.setVisibility(View.VISIBLE);
        } else if (mAppTaskModel.missionProcess.status == 1) {
            tvReceive.setText("进行中");
            tvReceive.setVisibility(View.VISIBLE);
        } else if (mAppTaskModel.missionProcess.status == -1) {
            tvReceive.setText("已完成");
            tvReceive.setVisibility(View.VISIBLE);
        }
    }

    private void updateTask() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setButton();
            return;
        }
        tvReceive.post(new Runnable() {
            @Override
            public void run() {
                setButton();
            }
        });
    }

    public void showTaskPanel(View v) {
        addView(v);
        tvReceive.setVisibility(View.VISIBLE);
        tvTask.setVisibility(View.VISIBLE);
        pbTask.setVisibility(View.INVISIBLE);
    }

    public void loadTaskPanel(View v) {
        addView(v);
        tvReceive.setVisibility(View.INVISIBLE);
        tvTask.setVisibility(View.INVISIBLE);
        pbTask.setVisibility(View.VISIBLE);
    }

    private void addView(View v) {
        if (!mRootView.isShown()) {
            SDViewUtil.addView(v, mRootView);
        }
    }
}
