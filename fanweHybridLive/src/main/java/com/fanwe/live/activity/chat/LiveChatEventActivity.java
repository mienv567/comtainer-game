package com.fanwe.live.activity.chat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.live.model.LiveChatMsgVo;

import java.util.ArrayList;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */

public class LiveChatEventActivity extends LiveChatActivity {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_SEND:
                sendMsg();
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMsgList();
    }

    private void requestMsgList() {
        ArrayList<LiveChatMsgVo> list = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            LiveChatMsgVo item = new LiveChatMsgVo();
            item.img = "http://avatar.csdn.net/A/0/F/1_weidongjian.jpg";
            item.type = index % 3 == 0 ? LiveChatMsgVo.TYPE_ME : LiveChatMsgVo.TYPE_FREIEND;
            item.msg = "XXXXXXXXXXX";
            list.add(item);
        }
        setMsgList(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTalk:

                break;
            case R.id.ivEmoji:
                showEmojiPanel(v);
                break;
            case R.id.ivMore:

                break;
        }
    }

    public void onEventMainThread(String msg) {

    }

    @Override
    public void onRefresh() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshOver();
            }
        }, 3000);
    }
}
