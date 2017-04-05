package com.fanwe.live.fragment;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.adapter.LiveFansRankAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PlayerRankModel;
import com.fanwe.live.model.RankModel;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LiveFansRankFragment extends BaseFragment {

    public static final String RANK_TYPE = "rank_type";
    public static final int PAGE_SIZE = 10;

    @ViewInject(R.id.lv_rank_list)
    SDProgressPullToRefreshListView lv_rank_list;
    @ViewInject(R.id.tv_empty_view)
    TextView tv_empty_view;

    private LiveFansRankAdapter mRankAdapter;
    private List<PlayerRankModel> mModels = new ArrayList<>();

    private int rankType;
    private int mCurrPage;
    private boolean isRefresh;
    private String mCreaterId;

    public static LiveFansRankFragment newInstance(int type) {
        LiveFansRankFragment fragment = new LiveFansRankFragment();
        Bundle args = new Bundle();
        args.putInt(RANK_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_fans_rank;
    }

    @Override
    protected void init() {
        mCreaterId = ((LiveActivity) getActivity()).getCreaterId();
        rankType = getArguments().getInt(RANK_TYPE);

        mRankAdapter = new LiveFansRankAdapter(mModels, getActivity());
        lv_rank_list.setAdapter(mRankAdapter);
        lv_rank_list.setEmptyView(tv_empty_view);

        lv_rank_list.setMode(PullToRefreshBase.Mode.BOTH);
        lv_rank_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                loadMore();
            }
        });

        refresh();
    }

    private void refresh() {
        isRefresh = true;
        mCurrPage = 0;
        requestRankData();
    }

    private void loadMore() {
        isRefresh = false;
        mCurrPage++;
        requestRankData();
    }

    private void requestRankData() {
        CommonInterface.requestRoomControbutionList(mCurrPage, PAGE_SIZE, mCreaterId, rankType,
                new AppRequestCallback<RankModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {

                        if (rootModel.isOk()) {
                            handleData(actModel);
                            lv_rank_list.onRefreshComplete();
                        }
                    }
                });
    }

    private void handleData(RankModel actModel) {
        List<PlayerRankModel> models = actModel.rankingList;
        if (models == null || models.size() == 0) {
            return;
        }
        if (isRefresh) {
            mRankAdapter.setData(mModels);
        } else {
            mModels.addAll(models);
            mRankAdapter.setData(mModels);
        }
    }

}
