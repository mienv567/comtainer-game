package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.UserFollowPushModelAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.fanwe.live.view.SDSlidingButton;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2016/7/12.
 */
public class LivePushManageActivity extends BaseTitleActivity {

    @ViewInject(R.id.list)
    protected SDProgressPullToRefreshListView list;

    private SDSlidingButton sl_btn;
    protected List<UserModel> listModel = new ArrayList<UserModel>();
    protected UserFollowPushModelAdapter adapter;
//    private List<Integer> listModel = new ArrayList<>();
//    private LivePushManageAdapter adapter;
    protected int page = 1;
    protected App_focus_follow_ActModel app_my_focusActModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_push_manage);
        init();
    }

    private void init() {
        initTitle();
        initHeader();

        bindAdapter();
        initSDSlidingButton();
        register();

    }

    private void register()
    {
        list.setMode(PullToRefreshBase.Mode.BOTH);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshViewer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreViewer();
            }
        });
        //list.setRefreshing();
    }


    protected void refreshViewer()
    {
        page = 1;
        request(false);
    }

    protected void request(final boolean isLoadMore)
    {
        CommonInterface.requestUser_follow(page, "", new AppRequestCallback<App_focus_follow_ActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    app_my_focusActModel = actModel;
                    SDViewUtil.updateAdapterByList(listModel, actModel.getRelationshipList(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                list.onRefreshComplete();
            }
        });
    }

    private void loadMoreViewer()
    {
        if (app_my_focusActModel != null)
        {
            if (app_my_focusActModel.getHas_next() == 1)
            {
                page++;
                request(true);
            } else
            {
                SDToast.showToast(getString(R.string.no_more_data));
                list.onRefreshComplete();
            }
        } else
        {
            refreshViewer();
        }
    }

    private void bindAdapter()
    {
        adapter = new UserFollowPushModelAdapter(listModel, this);
        list.setAdapter(adapter);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.push_manage));
    }


    private void initHeader() {
//        View headerView = LayoutInflater.from(this).inflate(R.layout.list_header_push_manage, null);
        sl_btn = (SDSlidingButton) findViewById(R.id.sl_btn);
//        lv_push_list.addHeaderView(headerView);
    }

    private void initSDSlidingButton() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            if (user.getIsRemind() == 1) {
                sl_btn.setSelected(true);
                list.setVisibility(View.VISIBLE);
            } else {
                sl_btn.setSelected(false);
                list.setVisibility(View.GONE);
            }
        }
        sl_btn.setSelectedChangeListener(new SDSlidingButton.SelectedChangeListener() {
            @Override
            public void onSelectedChange(SDSlidingButton view, boolean selected) {
                if (selected) {
                    CommonInterface.requestSet_push(1, null);
                    list.setVisibility(View.VISIBLE);
                } else {
                    CommonInterface.requestSet_push(0, null);
                    list.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("推送管理界面");
        MobclickAgent.onResume(this);
        refreshViewer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("推送管理界面");
        MobclickAgent.onPause(this);
    }
}
