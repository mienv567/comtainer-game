package com.fanwe.live.fragment;

import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveFamilyMembersAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_family_createActModel;
import com.fanwe.live.model.App_family_user_user_listActModel;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 家族成员列表
 * Created by Administrator on 2016/9/26.
 */

public class LiveFamilyMembersFragment extends BaseFragment
{
    @ViewInject(R.id.lv_fam_members)
    private SDProgressPullToRefreshListView lv_fam_members;

    private LiveFamilyMembersAdapter adapter;
    private List<UserModel> listModel;

    private PageModel pageModel = new PageModel();
    private int page = 1;

    private SDTabUnderline tab_live_menb;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_family_members;
    }

    @Override
    protected void init()
    {
        super.init();
        initData();
    }

    private void initData()
    {
        listModel = new ArrayList<>();
        adapter = new LiveFamilyMembersAdapter(listModel, getActivity());
        lv_fam_members.setAdapter(adapter);
        initPullToRefresh();

        /**
         * 踢出家族
         */
        adapter.setClickDelListener(new SDAdapter.ItemClickListener<UserModel>()
        {
            @Override
            public void onClick(int position, UserModel item, View view)
            {
                int user_id = Integer.parseInt(item.getUserId());
                delFamilyMember(user_id,item);
            }
        });
    }

    private void initPullToRefresh()
    {
        lv_fam_members.setMode(PullToRefreshBase.Mode.BOTH);
        lv_fam_members.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
        {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                refreshViewer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                loadMoreViewer();
            }
        });
    }

    public void refreshViewer()
    {
        page = 1;
        requestFamilyMembersList(false);
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestFamilyMembersList(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            lv_fam_members.onRefreshComplete();
        }
    }

    /**
     * 获取家族成员列表
     * @param isLoadMore
     */
    private void requestFamilyMembersList(final boolean isLoadMore)
    {
        UserModel dao = UserModelDao.query();
        CommonInterface.requestFamilyMembersList(dao.getFamilyId(),page, new AppRequestCallback<App_family_user_user_listActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.isOk())
                {
                    if (rootModel.getStatus() == 1)
                    {
                        tab_live_menb.setTextTitle("家族成员(" + actModel.getRs_count() + ")");
                        pageModel = actModel.getPage();
                        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                lv_fam_members.onRefreshComplete();
            }
        });
    }

    public void  setMembRsCount(SDTabUnderline textView)
    {
        this.tab_live_menb = textView;
    }

    /**
     * 家族成员移除
     * @param user_id
     */
    private void delFamilyMember(int user_id, final UserModel item)
    {
        CommonInterface.requestDelFamilyMember(user_id, new AppRequestCallback<App_family_createActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (rootModel.getStatus() == 1)
                {
                    SDToast.showToast("该家族成员已踢出家族");
                    adapter.removeData(item);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }
}
