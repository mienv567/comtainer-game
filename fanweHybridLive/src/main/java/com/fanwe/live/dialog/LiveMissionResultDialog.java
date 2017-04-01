package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsgMissionResult;

/**
 * Created by kevin.liu on 2017/3/23.
 */

public class LiveMissionResultDialog extends SDDialogBase {

    private CustomMsgMissionResult result; //任务结果

    private TextView tv_player_gain_score;
    private TextView tv_honor_num;
    private TextView tv_assistant;


    public LiveMissionResultDialog(Activity activity, CustomMsgMissionResult result) {
        super(activity);
        getWindow().setDimAmount(0);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        this.result = result;
        init();
    }

    protected void init() {
        if (result.getStatus() == 1) {
            setContentView(R.layout.dialog_mission_results_win);
            tv_player_gain_score = (TextView) findViewById(R.id.tv_player_gain_score);
            tv_honor_num = (TextView) findViewById(R.id.tv_honor_num);
            tv_assistant = (TextView) findViewById(R.id.tv_assistant);
            tv_player_gain_score.setText("+" + result.getMissionScore());
            tv_honor_num.setText("X" + result.getMedalNum());
            tv_assistant.setText(result.getGuestName() + " 获得");
        } else {
            setContentView(R.layout.dialog_mission_results_lose);
            findViewById(R.id.btn_deny).setOnClickListener(this);
        }
        findViewById(R.id.btn_ok).setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (result.getStatus() != 1) {
                    SDToast.showToast("向服务器发请求扣除魅力值获得积分");
                }
                dismiss();
                break;
            case R.id.btn_deny:
                dismiss();
                break;
        }
    }

    public void setResult(CustomMsgMissionResult result) {
        this.result = result;
        init();
    }
}
