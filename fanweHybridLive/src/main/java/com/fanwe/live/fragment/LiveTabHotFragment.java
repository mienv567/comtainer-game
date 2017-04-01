package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabHotAdapter;
import com.fanwe.live.appview.LiveTabHotHeaderView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveSelectLiveDialog;
import com.fanwe.live.event.EReSelectTabLive;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveBannerModel;
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
 * 热门直播列表
 *
 * @author Administrator
 * @date 2016-7-2 上午11:28:04
 */
public class LiveTabHotFragment extends LiveTabBaseFragment
{

    /**
     * 话题的id(int)
     */
    public static final String EXTRA_TOPIC_ID = "extra_topic_id";

    @ViewInject(R.id.lv_content)
    protected PullToRefreshListView lv_content;

    protected LiveTabHotHeaderView headerView;

    protected List<LiveRoomModel> listModel = new ArrayList<LiveRoomModel>();
    protected LiveTabHotAdapter adapter;

    protected int page;
    protected int has_next;
    protected int topicId;
    protected int sex;
    protected String city;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_tab_hot;
    }

    @Override
    protected void init()
    {
        super.init();

        topicId = getArguments().getInt(EXTRA_TOPIC_ID);

        addHeaderView();
        setAdapter();
        updateParams();
        initPullToRefresh();
    }

    protected void setAdapter()
    {
        adapter = new LiveTabHotAdapter(listModel, getActivity());
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
        lv_content.setRefreshing();
    }

    protected void addHeaderView()
    {
        headerView = new LiveTabHotHeaderView(getActivity());
        headerView.setBannerClickListener(new SDAdapter.ItemClickListener<LiveBannerModel>()
        {
            @Override
            public void onClick(int position, LiveBannerModel item, View view)
            {
                Intent intent = item.parseType(getActivity());
                if (intent != null)
                {
                    startActivity(intent);
                }
            }
        });
        lv_content.getRefreshableView().addHeaderView(headerView);
    }

    protected void updateParams()
    {
        sex = SDConfig.getInstance().getInt(R.string.config_live_select_sex, 0);
        city = SDConfig.getInstance().getString(R.string.config_live_select_city, "");
    }


    @Override
    public void onResume()
    {
        requestData(false);
        super.onResume();
        MobclickAgent.onPageStart("直播-热门列表");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("直播-热门列表");
    }

    public void onEventMainThread(EReSelectTabLive event)
    {
        if (event.index == 1)
        {
            final LiveSelectLiveDialog selectDialog = new LiveSelectLiveDialog(getActivity());
            selectDialog.showBottom();
        }
    }

    public void onEventMainThread(ESelectLiveFinish event)
    {
        updateParams();
        requestData(false);
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

        CommonInterface.requestIndex(page, sex, topicId, city, new AppRequestCallback<Index_indexActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    has_next = actModel.getHas_next();

                    headerView.setListLiveBannerModel(actModel.getBanner());
                    headerView.setTopicInfoModel(actModel.getCate());

                    synchronized (LiveTabHotFragment.this)
                    {
                        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                    }
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
                synchronized (LiveTabHotFragment.this)
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
                                        adapter.updateData(listModel);
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
