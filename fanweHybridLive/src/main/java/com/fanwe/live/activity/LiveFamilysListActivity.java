package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveFamilysListAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_family_createActModel;
import com.fanwe.live.model.App_family_listActModel;
import com.fanwe.live.model.App_family_listItemActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.LiveSongSearchView;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索加入家族列表
 * Created by Administrator on 2016/9/24.
 */

public class LiveFamilysListActivity extends BaseTitleActivity implements LiveSongSearchView.SearchViewListener
{

    @ViewInject(R.id.sv_song)
    private LiveSongSearchView sv_song;
    @ViewInject(R.id.lv_search_result)
    private SDProgressPullToRefreshListView listView;

    private LiveFamilysListAdapter adapter;
    private List<App_family_listItemActModel> listModel;

    private int has_next;
    private int page = 1;
    private String keyword = "";
    private SDRequestHandler mHandler = null;
    private UserModel dao = UserModelDao.query();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_familys_list);
        initView();
    }

    private void initView()
    {
        initTitle();
        sv_song.setSearchViewListener(this);
        sv_song.getEtInput().setHint("请输入您想要加入的家族");
//        sv_song.btnSearch.setText("取消");

        listModel = new ArrayList<>();
        adapter = new LiveFamilysListAdapter(listModel, this);
        listView.setAdapter(adapter);
        initPullToRefresh();

        famOnSearch("");

//        sv_song.btnSearch.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                finish();
//            }
//        });

        /**
         * 申请加入家族
         */
        adapter.setClickJoinListener(new SDAdapter.ItemClickListener<App_family_listItemActModel>()
        {
            @Override
            public void onClick(int position, App_family_listItemActModel item, View view)
            {
                int family_id = item.getFamily_id();
                applyJoinFamily(family_id);
            }
        });
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("家族列表");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
        mTitle.setOnClickListener(this);
    }

    private void initPullToRefresh()
    {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
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

    protected SDRequestHandler requestData(final boolean isLoadMore)
    {

        if (isLoadMore)
        {
            if (has_next == 1)
            {
                page++;
            } else
            {
                listView.onRefreshComplete();
                SDToast.showToast("没有更多数据了");
                return null;
            }
        } else
        {
            page = 1;
        }
        return CommonInterface.requestApplyJoinFamilyList(dao.getFamilyId(),keyword, page, new AppRequestCallback<App_family_listActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    has_next = actModel.getPage().getHas_next();
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                listView.onRefreshComplete();
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    public SDRequestHandler search(String keyword)
    {
        this.keyword = keyword;
        return requestData(false);
    }

    @Override
    public void onRefreshAutoComplete(String text)
    {
        famOnSearch(text);
    }

    @Override
    public void onSearch(String text)
    {
        famOnSearch(text);
    }

    private void famOnSearch(String keyWord)
    {
//        if(!TextUtils.isEmpty(keyWord))
//        {
//            if(TextUtils.equals(keyWord,keyword))
//            {
////                SDToast.showToast("查找相同字符串");
//                return ;
//            }
            if(mHandler != null)
            {
                mHandler.cancel();
            }
            mHandler = search(keyWord);
//        }else
//        {
////            SDToast.showToast("查找空字符串");
//        }
    }

    /**
     * 申请加入家族
     * @param id
     */
    private void applyJoinFamily(int id)
    {
        CommonInterface.requestApplyJoinFamily(id, new AppRequestCallback<App_family_createActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.getStatus() == 1)
                    SDToast.showToast(actModel.getError().toString());
            }
        });
    }

}
