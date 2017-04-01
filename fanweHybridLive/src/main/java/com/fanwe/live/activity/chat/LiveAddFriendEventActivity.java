package com.fanwe.live.activity.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.fanwe.live.model.LiveSearchHistoryVo;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */

public class LiveAddFriendEventActivity extends LiveAddFriendActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                requestSearchHistory();
            }
        });
    }

    private void requestSearchHistory() {

    }

    @Override
    public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        requesSearchFriend(v.toString());
                    }
                });
                break;
        }
        return false;
    }

    private void requesSearchFriend(final String content) {

    }

    public void onEventMainThread(LiveSearchHistoryVo vo) {

    }
}
