package com.fanwe.live.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.live.R;
import com.fanwe.live.adapter.UserTaskAdapter;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务fragment
 */
public class UserTaskFragment extends BaseFragment {

    @ViewInject(R.id.expandable_list)
    private ExpandableListView expandable_list;
    private UserTaskAdapter mAdapter;
    private List<UserTaskItem> mTasks;
    private View mView;
    public UserTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_user_task;
    }

    @Override
    protected void init() {
        super.init();
        initExpandableList();
    }

    private void initExpandableList() {
        mAdapter = new UserTaskAdapter(getActivity());
        mAdapter.setList(new ArrayList<UserTaskItem>());
        expandable_list.setAdapter(mAdapter);
        expandable_list.setGroupIndicator(null);
    }

    public void setData(List<UserTaskItem> tasks){
        mTasks = tasks;
        mAdapter.setList(mTasks);
        mAdapter.notifyDataSetChanged();
        expandableAll();
    }

    private void expandableAll() {
        int groupCount = expandable_list.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandable_list.expandGroup(i);
        }
    }

}
