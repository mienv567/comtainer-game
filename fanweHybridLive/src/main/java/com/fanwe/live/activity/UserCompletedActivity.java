package com.fanwe.live.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.hybrid.model.UserTaskModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.adapter.UserCompletedTaskAdapter;
import com.fanwe.live.common.CommonInterface;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class UserCompletedActivity extends BaseTitleActivity {
    private static final int HAS_COMPLETED_TASK = 1;
    @ViewInject(R.id.list_view)
    private ListView list_view;

    private UserCompletedTaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_completed);
        initTitle();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData(){
        CommonInterface.requestMyTask(HAS_COMPLETED_TASK, new AppRequestCallback<UserTaskModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk()) {
                    mAdapter.updateData(actModel.getMain_task());
                }
            }
        });
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.completed_task));
    }

    private void initListView(){
        mAdapter = new UserCompletedTaskAdapter(new ArrayList<UserTaskItem>(),this);
        list_view.setAdapter(mAdapter);
    }

    private List<UserTaskItem> initData(){
        List<UserTaskItem> list = new ArrayList<>();
        UserTaskItem item0 = new UserTaskItem();
        item0.setTask_id("0");
        item0.setTask_title("面子工程");
        List<String> experience0 = new ArrayList<>();
        experience0.add("有头有脸的勋章");
        experience0.add("50经验");
        item0.setTask_experience(experience0);
        item0.setTask_type(0);
        item0.setTask_state(0);
        UserTaskItem item1 = new UserTaskItem();
        item1.setTask_id("0");
        item1.setTask_title("领取勋章");
        List<String> experience1 = new ArrayList<>();
        experience1.add("安卓达人徽章");
        experience1.add("5经验值");
        item1.setTask_experience(experience1);
        item1.setTask_type(0);
        item1.setTask_state(0);
        UserTaskItem item2 = new UserTaskItem();
        item2.setTask_id("0");
        item2.setTask_title("每日签到");
        List<String> experience2 = new ArrayList<>();
        experience2.add("每日领币");
        experience2.add("满签抽奖");
        item2.setTask_experience(experience2);
        item2.setTask_type(0);
        item2.setTask_state(0);
        list.add(item0);
        list.add(item1);
        list.add(item2);
        return list;
    }
}
