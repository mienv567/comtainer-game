package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveBlacklistAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Settings_black_listActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_set_blackActModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class LiveBlackListActivity extends BaseTitleActivity
{

    @ViewInject(R.id.lv_black_list_content)
    private PullToRefreshListView lv_content;

    @ViewInject(R.id.view_empty)
    private View view_empty;

    @ViewInject(R.id.tv_empty_text)
    private TextView tv_empty_text;

    private LiveBlacklistAdapter adapter;
    private int page;
    private int has_next;
    private List<UserModel> listModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_black_list);
        initView();
    }

    private void initView()
    {
        initTitle();

        tv_empty_text.setText("");

        adapter = new LiveBlacklistAdapter(listModel, this);
        adapter.setItemClickListener(new SDAdapter.ItemClickListener<UserModel>()
        {
            @Override
            public void onClick(int position, UserModel item, View view)
            {
                Intent intent = new Intent(LiveBlackListActivity.this, LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUserId());
                startActivity(intent);
            }
        });
        adapter.setItemLongClickListener(new SDAdapter.ItemLongClickListener<UserModel>()
        {
            @Override
            public void onClick(int position, UserModel item, View view)
            {
                showOperateMenu(item);
            }
        });

        lv_content.setAdapter(adapter);

        initPullToRefresh();
    }

    private void initPullToRefresh()
    {
        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
        {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                requestData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                requestData(true);
            }
        });
    }

    private void showOperateMenu(final UserModel user)
    {
        SDDialogMenu dialog = new SDDialogMenu(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setItems(new String[]{getString(R.string.cancel_black)});
        dialog.setmListener(new SDDialogMenu.SDDialogMenuListener()
        {
            @Override
            public void onCancelClick(View v, SDDialogMenu dialog)
            {
            }

            @Override
            public void onDismiss(SDDialogMenu dialog)
            {
            }

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog)
            {
                switch (index)
                {
                    case 0:
                        clickCancelBlackUser(user);
                        break;

                    default:
                        break;
                }
            }
        });
        dialog.showBottom();
    }

    private void clickCancelBlackUser(UserModel user)
    {
        if (user != null)
        {
            CommonInterface.requestSet_black(user.getUserId(),user, new AppRequestCallback<User_set_blackActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (rootModel.isOk())
                    {
                        requestData(false);
                    }
                }
            });
        }
    }

    private void requestData(final boolean isLoadMore)
    {
        if (isLoadMore)
        {
            if (has_next == 1)
            {
                page++;
            } else
            {
                lv_content.onRefreshComplete();
                SDToast.showToast(getString(R.string.no_more_data));
                return;
            }
        } else
        {
            page = 1;
        }

        CommonInterface.requestBlackList(page, new AppRequestCallback<Settings_black_listActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.isOk())
                {
                    has_next = actModel.getHas_next();

                    SDViewUtil.updateAdapterByList(listModel, actModel.getUser(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                lv_content.onRefreshComplete();
                SDViewUtil.toggleEmptyViewByList(listModel, view_empty);
                super.onFinish(resp);
            }
        });
    }


    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.black_list));
    }

    @Override
    protected void onResume()
    {
        requestData(false);
        super.onResume();
        MobclickAgent.onPageStart("黑名单界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("黑名单界面");
        MobclickAgent.onPause(this);
    }
}
