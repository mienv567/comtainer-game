package com.fanwe.live.dialog;

import android.content.Context;
import android.view.View;

import com.fanwe.hybrid.dialog.DialogViewHolder;
import com.fanwe.hybrid.dialog.XDialog;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsgMissionResult;


public class MissionResultDialog implements View.OnClickListener {

    private CustomMsgMissionResult result;
    private XDialog mDialog;
    private Context mContext;

    public MissionResultDialog(Context context, CustomMsgMissionResult result) {
        mContext = context;
        this.result = result;
        init();
    }

    public void setResult(CustomMsgMissionResult result) {
        this.result = result;
        init();
    }

    private void init() {
        if (result.getStatus() == 1) {
            mDialog = new XDialog(mContext, R.layout.dialog_mission_results_win) {
                @Override
                protected void convert(DialogViewHolder holder) {
                    holder.setText(R.id.tv_player_gain_score, "+" + result.getMissionScore());
                    holder.setText(R.id.tv_honor_num, "×" + result.getMedalNum());
                    holder.setText(R.id.tv_assistant, result.getGuestName() + " 获得");
                    holder.setOnClick(R.id.btn_ok, MissionResultDialog.this);
                }
            };
        } else {
            mDialog = new XDialog(mContext, R.layout.dialog_mission_results_lose) {
                @Override
                protected void convert(DialogViewHolder holder) {
                    holder.setText(R.id.tv_player_gain_score, "+" + result.getMissionScore());
                    holder.setText(R.id.tv_honor_num, "×" + result.getMedalNum());
                    holder.setText(R.id.tv_honor, result.getGuestName() + " 获得");
                    holder.setOnClick(R.id.btn_ok, MissionResultDialog.this);
                    holder.setOnClick(R.id.btn_deny, MissionResultDialog.this);
                }
            };
        }
        mDialog.setCanceledOnTouchOutside(true)
                .showDialog(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (result.getStatus() != 1) {
                    SDToast.showToast("向服务器发请求扣除魅力值获得积分");
                }
                mDialog.dismiss();
                break;
            case R.id.btn_deny:
                mDialog.dismiss();
                break;
        }
    }
}
