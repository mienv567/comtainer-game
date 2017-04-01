package com.fanwe.live.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveVideoActivity;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */
public class LiveHBottomMenu implements View.OnClickListener, TextView.OnEditorActionListener {

    private ViewGroup mRootView;

    private EditText etInputMsg;
    private final ImageView iv_barrage;

    public LiveHBottomMenu(Context context) {
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_live_h_bottom_menu, null);
        iv_barrage = (ImageView) mRootView.findViewById(R.id.iv_barrage);
        iv_barrage.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_gift).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_lianmai).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_charge).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_donate).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_more).setOnClickListener(this);
        etInputMsg = (EditText) mRootView.findViewById(R.id.et_msg);
        etInputMsg.setOnEditorActionListener(this);
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gift:
                EventBus.getDefault().post(13);
                break;
            case R.id.iv_barrage://弹幕
                EventBus.getDefault().post(LiveVideoActivity.CODE_BARRAGE);
                iv_barrage.setSelected(!iv_barrage.isSelected());
                break;
            case R.id.tv_charge://充值
                EventBus.getDefault().post(LiveVideoActivity.CODE_CHARGE);
                break;
            case R.id.tv_donate://送礼
                EventBus.getDefault().post(LiveVideoActivity.CODE_DONATE);
                break;
            case R.id.iv_more://更多
                EventBus.getDefault().post(LiveVideoActivity.CODE_MORE);
                break;
            case R.id.iv_back://返回
                EventBus.getDefault().post(LiveVideoActivity.CODE_BACK);
                break;
            case R.id.iv_lianmai://返回
                EventBus.getDefault().post(LiveVideoActivity.CODE_LIANMAI);
                break;
        }
    }

    public boolean isShown() {
        if (mRootView == null || !mRootView.isShown()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_SEND:
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                sendMsg();
                hideSoftInput();
                break;
        }
        return false;
    }

    private void sendMsg() {
        String content = etInputMsg.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            SDToast.showToast("发送消息不能为空");
            return;
        }
        etInputMsg.setText("");
        CustomMsgPopMsg msg = new CustomMsgPopMsg();
        msg.setDesc(content);
        EventBus.getDefault().post(msg);
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) etInputMsg.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInputMsg.getWindowToken(), 0);
    }
}
