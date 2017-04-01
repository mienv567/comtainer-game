package com.fanwe.live.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSearchHistoryAdapter;
import com.fanwe.live.model.LiveSearchHistoryVo;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */
public abstract class LiveAddFriendActivity extends BaseTitleActivity implements TextView.OnEditorActionListener {

    @ViewInject(R.id.etSearch)
    private EditText etSearch;

    @ViewInject(R.id.rvHistoryRecord)
    private RecyclerView rvHistoryRecord;

    private LinkedList<LiveSearchHistoryVo> mSearchInfoList = new LinkedList<>();

    private LiveSearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_add_friend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mTitle.setMiddleTextTop(getString(R.string.add_friend));

        etSearch.setOnEditorActionListener(this);
        rvHistoryRecord.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mSearchHistoryAdapter = new LiveSearchHistoryAdapter(mSearchInfoList);
        rvHistoryRecord.setAdapter(mSearchHistoryAdapter);

        findViewById(R.id.tvCancel).setOnClickListener(this);
    }

    protected void showSearchHistoryList(ArrayList<LiveSearchHistoryVo> list) {
        if (list == null || list.size() <= 0) {
            SDToast.showToast("没有验证信息");
            return;
        }
        mSearchInfoList.clear();
        mSearchInfoList.addAll(list);
        mSearchHistoryAdapter.notifyDataSetChanged();
    }

    protected void addVerifyInfoList(LiveSearchHistoryVo vo) {
        mSearchInfoList.addFirst(vo);
        mSearchHistoryAdapter.notifyItemInserted(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvCancel:
                etSearch.setText("");
                break;
        }
    }
}
