package com.fanwe.live.activity.chat;

import android.os.AsyncTask;
import android.os.Bundle;

import com.fanwe.live.model.LiveVerifyInfoVo;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/30 0030.
 */

public class LiveVerifyFriendEventActivity extends LiveVerifyFriendActivity {

    private int size = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                requestVerifyInfo(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void requestVerifyInfo(final boolean fresh) {
        final ArrayList<LiveVerifyInfoVo> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            LiveVerifyInfoVo item = new LiveVerifyInfoVo();
            item.name = "好友名称" + index;
            list.add(item);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fresh) {
                    freshVerifyInfoList(list);
                } else {
                    showVerifyInfoList(list);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        size++;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                requestVerifyInfo(true);
            }
        });
    }

    public void onEventMainThread(final LiveVerifyInfoVo data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                reuqestAcceptFriend(data);
            }
        });
    }

    private void reuqestAcceptFriend(final LiveVerifyInfoVo data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                acceptFriendSuccess(data);
            }
        });
    }
}
