package com.fanwe.live.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.PlayersRankAdapter;
import com.fanwe.live.appview.NoScrollListView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PlayerRankModel;
import com.fanwe.live.model.RankModel;
import com.fanwe.live.view.SDProgressPullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class PlayerRankFragment extends BaseFragment {

    public static final String RANK_TYPE = "rank_type";
    public static final int PAGE_SIZE = 15;

    @ViewInject(R.id.pull_to_refresh_view)
    private SDProgressPullToRefreshScrollView pull_to_refresh_view;
    @ViewInject(R.id.iv_player_head_top1)
    CircleImageView iv_player_head_top1;
    @ViewInject(R.id.tv_player_name_top1)
    TextView tv_player_name_top1;
    @ViewInject(R.id.iv_player_head_top2)
    CircleImageView iv_player_head_top2;
    @ViewInject(R.id.tv_player_name_top2)
    TextView tv_player_name_top2;
    @ViewInject(R.id.iv_player_head_top3)
    CircleImageView iv_player_head_top3;
    @ViewInject(R.id.tv_player_name_top3)
    TextView tv_player_name_top3;
    @ViewInject(R.id.lv_players_rank)
    NoScrollListView lv_players_rank;

    private int mCurrPage;
    private boolean isRefresh;
    private PlayersRankAdapter mRankAdapter;
    private List<PlayerRankModel> mSubList = new ArrayList<>();

    public static PlayerRankFragment newInstance(int type) {
        PlayerRankFragment fragment = new PlayerRankFragment();
        Bundle args = new Bundle();
        args.putInt(RANK_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_player_rank;
    }

    @Override
    protected void init() {
        mRankAdapter = new PlayersRankAdapter(mSubList, getActivity());
        lv_players_rank.setAdapter(mRankAdapter);
        refresh();
        pull_to_refresh_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
        Bundle bundle = this.getArguments();
        int type = bundle.getInt(RANK_TYPE);
        switch (type) {
            case 0:
                requestCharmList();
                break;
            case 1:
                requestLikesList();
                break;
            case 2:
                requestOutMissionList();
                break;
            case 3:
                requestInsideMissionList();
                break;
        }
    }

    private void requestCharmList() {
        CommonInterface.requestCharmList(mCurrPage, PAGE_SIZE, requestCallback);
    }

    private void requestLikesList() {
        CommonInterface.requestLikesList(mCurrPage, PAGE_SIZE, requestCallback);
    }

    private void requestOutMissionList() {
        CommonInterface.requestOutMissionList(mCurrPage, PAGE_SIZE, requestCallback);
    }

    private void requestInsideMissionList() {
        CommonInterface.requestInsideMissionList(mCurrPage, PAGE_SIZE, requestCallback);
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
                handleTop3Player(models.get(0), iv_player_head_top1, tv_player_name_top1);
                if (models.size() > 1) {
                    handleTop3Player(models.get(1), iv_player_head_top2, tv_player_name_top2);
                } else if (models.size() > 2) {
                    handleTop3Player(models.get(2), iv_player_head_top3, tv_player_name_top3);
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
     * @param textView  昵称
     */
    private void handleTop3Player(PlayerRankModel rankModel, ImageView imageView, TextView textView) {
        Glide.with(getActivity())
                .load(rankModel.userInfo.thumbHeadImage)
                .into(imageView);
        textView.setText(rankModel.userInfo.nickName);
    }
}
