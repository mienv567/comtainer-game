package com.fanwe.live.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.FansRankAdapter;
import com.fanwe.live.appview.NoScrollListView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PlayerRankModel;
import com.fanwe.live.model.RankModel;
import com.fanwe.live.view.SDProgressPullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class FansRankFragment extends BaseFragment {

    public static final String RANK_TYPE = "rank_type";
    public static final int PAGE_SIZE = 15;

    @ViewInject(R.id.pull_to_refresh_view)
    private SDProgressPullToRefreshScrollView pull_to_refresh_view;

    @ViewInject(R.id.iv_player_head_top1)
    CircleImageView iv_player_head_top1;
    @ViewInject(R.id.tv_player_name_top1)
    TextView tv_player_name_top1;
    @ViewInject(R.id.tv_score_top1)
    TextView tv_score_top1;

    @ViewInject(R.id.iv_player_head_top2)
    CircleImageView iv_player_head_top2;
    @ViewInject(R.id.tv_player_name_top2)
    TextView tv_player_name_top2;
    @ViewInject(R.id.tv_score_top2)
    TextView tv_score_top2;

    @ViewInject(R.id.iv_player_head_top3)
    CircleImageView iv_player_head_top3;
    @ViewInject(R.id.tv_player_name_top3)
    TextView tv_player_name_top3;
    @ViewInject(R.id.tv_score_top3)
    TextView tv_score_top3;

    @ViewInject(R.id.lv_players_rank)
    NoScrollListView lv_players_rank;

    private FansRankAdapter mRankAdapter;
    private List<PlayerRankModel> mSubList = new ArrayList<>();

    private int rankType;
    private int mCurrPage;
    private boolean isRefresh;

    public static FansRankFragment newInstance(int type) {
        FansRankFragment fragment = new FansRankFragment();
        Bundle args = new Bundle();
        args.putInt(RANK_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateContentView() {
        rankType = getArguments().getInt(RANK_TYPE);
        if (rankType == 0) {
            return R.layout.fragment_fans_contribution_rank;
        } else {
            return R.layout.fragment_fans_honor_rank;
        }
    }

    @Override
    protected void init() {
        mRankAdapter = new FansRankAdapter(mSubList, getActivity(), rankType);
        lv_players_rank.setAdapter(mRankAdapter);
        refresh();
        pull_to_refresh_view.setMode(PullToRefreshBase.Mode.BOTH);
        pull_to_refresh_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                loadMore();
            }
        });
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
        switch (rankType) {
            case 0:
                requestContributionList();
                break;
            case 1:
                requestMedalList();
                break;
        }
    }

    private void requestContributionList() {
        CommonInterface.requestContributionList(mCurrPage, PAGE_SIZE, requestCallback);
    }

    private void requestMedalList() {
        CommonInterface.requestMedalList(mCurrPage, PAGE_SIZE, requestCallback);
    }

    AppRequestCallback<RankModel> requestCallback = new AppRequestCallback<RankModel>() {
        @Override
        protected void onSuccess(SDResponse sdResponse) {
            if (rootModel.isOk()) {
            handleData(actModel);}
            pull_to_refresh_view.onRefreshComplete();
        }

        @Override
        protected void onError(SDResponse resp) {
            super.onError(resp);
            pull_to_refresh_view.onRefreshComplete();
        }
    };

    private void handleData(RankModel actModel) {
            List<PlayerRankModel> models = actModel.rankingList;
            if (models == null || models.size() == 0) {
                return;
            }
            if (isRefresh) {
                handleTop3Player(models.get(0), iv_player_head_top1, tv_player_name_top1, tv_score_top1);
                if (models.size() > 1) {
                    handleTop3Player(models.get(1), iv_player_head_top2, tv_player_name_top2, tv_score_top2);
                } else if (models.size() > 2) {
                    handleTop3Player(models.get(2), iv_player_head_top3, tv_player_name_top3, tv_score_top3);
                } else if (models.size() > 3) {
                    mSubList = models.subList(3, models.size());
                    mRankAdapter.setData(mSubList);
                }
            } else {
                mSubList.addAll(models);
                mRankAdapter.setData(mSubList);
            }
    }

    /**
     * 处理前三名数据
     *
     * @param rankModel 排名数据
     * @param imageView 头像
     * @param nickName  昵称
     * @param score     得分
     */
    private void handleTop3Player(PlayerRankModel rankModel, ImageView imageView,
                                  TextView nickName, TextView score) {
        Glide.with(getActivity())
                .load(rankModel.userInfo.thumbHeadImage)
                .into(imageView);
        SDViewBinder.setTextView(nickName, rankModel.userInfo.nickName);
        if (rankType == 0) {
            SDViewBinder.setTextView(score, getString(R.string.contribution_per_dray, rankModel.score));
        } else {
            SDViewBinder.setTextView(score, String.valueOf(rankModel.score));
        }
    }

}
