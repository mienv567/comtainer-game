package com.fanwe.live.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.BoxBarAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.FamilyDataListModel;
import com.fanwe.live.model.MultipleItem;
import com.fanwe.live.view.SDProgressPullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 箱吧
 */
public class BoxBarFragment extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    @ViewInject(R.id.iv_left)
    ImageView mIvLeft;
    @ViewInject(R.id.tv_title)
    TextView mTvTitle;
    @ViewInject(R.id.tv_right)
    TextView mTvRight;
    @ViewInject(R.id.recyclerView_box_bar)
    SDProgressPullToRefreshRecyclerView mRecyclerViewBoxBar;

    private int mCurrPage = 1;
    private boolean isLoadMore;

    private BoxBarAdapter mBoxBarAdapter;
    private List<MultipleItem> mData = new ArrayList<>();

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_box_bar;
    }

    @Override
    protected void init() {
        register();
        initRecyclerView();
    }

    private void register() {
        mIvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mRecyclerViewBoxBar.setMode(PullToRefreshBase.Mode.BOTH);
        mRecyclerViewBoxBar.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SDRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                loadMore();
            }
        });

        mRecyclerViewBoxBar.getRefreshableView().setGridVertical(2);
        mBoxBarAdapter = new BoxBarAdapter(getActivity(), mData);
        mBoxBarAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return mData.get(position).getSpanSize();
            }
        });

        mRecyclerViewBoxBar.getRefreshableView().setAdapter(mBoxBarAdapter);
    }

    private void refresh() {
        mData.clear();
        isLoadMore = false;
        mCurrPage = 1;
        requestData();
    }

    private void loadMore() {
        isLoadMore = true;
        mCurrPage++;
    }

    private void requestData() {
        if (!isLoadMore) {
            // TODO: 2017/4/5 如果是下拉刷新, 刷新顶部热门视频, 否则只刷新动态列表
            mData.add(0, new MultipleItem(MultipleItem.HEAD, 2, true));
            mData.add(1, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
            mData.add(2, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
            mData.add(3, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
            mData.add(4, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
            mBoxBarAdapter.setNewData(mData);
        }
        CommonInterface.queryFamilyList(mCurrPage, PAGE_SIZE, new AppRequestCallback<List<FamilyDataListModel>>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                mRecyclerViewBoxBar.onRefreshComplete();
                if (baseActModel.isOk()) {
                    handleData(actModel);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                mRecyclerViewBoxBar.onRefreshComplete();
            }
        });
    }

    private void handleData(List<FamilyDataListModel> actModel) {

    }
}
