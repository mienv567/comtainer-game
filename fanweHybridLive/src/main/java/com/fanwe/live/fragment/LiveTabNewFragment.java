package com.fanwe.live.fragment;

import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabNewAdapter;
import com.fanwe.live.appview.LiveTabNewHeaderView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.custommsg.CustomMsgLiveStopped;
import com.fanwe.live.model.custommsg.MsgModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 最新直播列表
 *
 * @author Administrator
 * @date 2016-7-2 上午11:27:49
 */
public class LiveTabNewFragment extends LiveTabBaseFragment
{

    @ViewInject(R.id.lv_content)
    protected PullToRefreshListView lv_content;

    protected List<LiveRoomModel> listModel = new ArrayList<LiveRoomModel>();
    protected LiveTabNewAdapter adapter;

    protected LiveTabNewHeaderView headerView;

    protected int page;
    protected int has_next;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_tab_new;
    }

    @Override
    protected void init()
    {
        super.init();

        addHeaderView();
        setAdapter();
        initPullToRefresh();
    }

    protected void addHeaderView()
    {
        headerView = new LiveTabNewHeaderView(getActivity());
        lv_content.getRefreshableView().addHeaderView(headerView);
    }

    protected void setAdapter()
    {
        adapter = new LiveTabNewAdapter(new ArrayList<List<LiveRoomModel>>(), getActivity());
        lv_content.setAdapter(adapter);
    }

    protected void initPullToRefresh()
    {
        lv_content.setMode(Mode.BOTH);
        lv_content.setOnRefreshListener(new OnRefreshListener2<ListView>()
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
        requestData(false);
    }

    @Override
    public void onResume()
    {
        requestData(false);
        super.onResume();
        MobclickAgent.onPageStart("直播-最新列表");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("直播-最新列表");
    }

    @Override
    protected void onLoopRun()
    {
        requestData(false);
    }

    protected void requestData(final boolean isLoadMore)
    {
        startLoopRunnable();
        if (isLoadMore)
        {
            if (has_next == 1)
            {
                page++;
            } else
            {
                lv_content.onRefreshComplete();
                SDToast.showToast(SDResourcesUtil.getString(R.string.no_more_data));
                return;
            }
        } else
        {
            page = 1;
        }

        CommonInterface.requestNewVideo(page, new AppRequestCallback<Index_new_videoActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.isOk())
                {
                    has_next = actModel.getHas_next();

                    synchronized (LiveTabNewFragment.this)
                    {
                        SDViewUtil.updateList(listModel, actModel.getList(), isLoadMore);
                        adapter.updateData(SDCollectionUtil.splitList(listModel, 3));
                    }

                    headerView.setListLiveTopicModel(actModel.getCate_top());
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                lv_content.onRefreshComplete();
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop()
    {
        lv_content.getRefreshableView().setSelection(0);
    }

    @Override
    protected void onMsgLiveStopped(final MsgModel msgModel)
    {
        SDHandlerManager.getBackgroundHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (LiveTabNewFragment.this)
                {
                    if (SDCollectionUtil.isEmpty(listModel))
                    {
                        return;
                    }
                    CustomMsgLiveStopped customMsg = msgModel.getCustomMsgLiveStopped();
                    if (customMsg != null)
                    {
                        int roomId = customMsg.getRoom_id();
                        Iterator<LiveRoomModel> it = listModel.iterator();
                        while (it.hasNext())
                        {
                            LiveRoomModel item = it.next();
                            if (roomId == item.getRoomId())
                            {
                                it.remove();
                                SDHandlerManager.getMainHandler().post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        adapter.updateData(SDCollectionUtil.splitList(listModel, 3));
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            }
        });
        super.onMsgLiveStopped(msgModel);
    }
}
