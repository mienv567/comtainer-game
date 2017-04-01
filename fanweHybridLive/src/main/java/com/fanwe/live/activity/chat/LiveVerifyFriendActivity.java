package com.fanwe.live.activity.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveVerifyFriendAdapter;
import com.fanwe.live.model.LiveVerifyInfoVo;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by yong.zhang on 2017/3/30 0030.
 */
public abstract class LiveVerifyFriendActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.srlContainer)
    private SwipeRefreshLayout srlVerifyInfo;

    @ViewInject(R.id.rvContent)
    private RecyclerView rvVerifyInfo;

    private ArrayList<LiveVerifyInfoVo> mVerifyInfoList = new ArrayList<>();

    private LiveVerifyFriendAdapter mVerifyInfoAdapter;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_refresh_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mTitle.setMiddleTextTop(getString(R.string.verify));

        srlVerifyInfo.setOnRefreshListener(this);

        rvVerifyInfo.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mVerifyInfoAdapter = new LiveVerifyFriendAdapter(mVerifyInfoList);
        rvVerifyInfo.setAdapter(mVerifyInfoAdapter);
    }

    protected void showVerifyInfoList(ArrayList<LiveVerifyInfoVo> list) {
        if (list == null || list.size() <= 0) {
            SDToast.showToast("没有验证信息");
            return;
        }
        mVerifyInfoList.clear();
        mVerifyInfoList.addAll(list);
        mVerifyInfoAdapter.notifyDataSetChanged();
    }

    protected void freshVerifyInfoList(ArrayList<LiveVerifyInfoVo> list) {
        srlVerifyInfo.setRefreshing(false);
        showVerifyInfoList(list);
    }

    protected void acceptFriendSuccess(LiveVerifyInfoVo data) {
        int index = mVerifyInfoList.indexOf(data);
        mVerifyInfoList.remove(index);
        mVerifyInfoAdapter.notifyItemRemoved(index);
    }
}
