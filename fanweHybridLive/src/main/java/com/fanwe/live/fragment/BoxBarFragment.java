package com.fanwe.live.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.BoxBarAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.MultipleItem;
import com.fanwe.live.model.item.FamilyDataItem;
import com.fanwe.live.model.item.HeaderItem;
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
    @ViewInject(R.id.tv_right)
    TextView mTvRight;
    @ViewInject(R.id.recyclerView_box_bar)
    SDProgressPullToRefreshRecyclerView mRecyclerViewBoxBar;

    private int mCurrPage = 1;
    private boolean isRefresh = true;

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
        mRecyclerViewBoxBar.setRefreshing();
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

        requestData();
    }

    private void refresh() {
        mData.clear();
        isRefresh = true;
        mCurrPage = 1;
        requestData();
    }

    private void loadMore() {
        isRefresh = false;
        mCurrPage++;
        requestData();
    }

    private void requestData() {
        if (isRefresh) {
            requestTopicVideo();
        }
        requestFamilyList();
    }

    private void requestTopicVideo() {
        mData.add(0, new HeaderItem("热门话题", R.drawable.ic_hot_topic, true));
        mData.add(1, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
        mData.add(2, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
        mData.add(3, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
        mData.add(4, new MultipleItem(MultipleItem.POPULAR_VIDEO, 1));
        mBoxBarAdapter.setNewData(mData);
    }

    private void requestFamilyList() {
        CommonInterface.queryFamilyList(mCurrPage, PAGE_SIZE, new AppRequestCallback<List<FamilyDataItem>>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk() && actModel != null) {
                    if (isRefresh) {
                        mData.add(new HeaderItem("箱吧", R.drawable.ic_box_bar_small, false));
                    }
                    handleData(actModel);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                if (mRecyclerViewBoxBar.isRefreshing()) {
                    mRecyclerViewBoxBar.onRefreshComplete();
                }
            }
        });
    }

    private void handleData(List<FamilyDataItem> actModel) {
        if (actModel.size() > 0) {
            mBoxBarAdapter.addData(new ArrayList<MultipleItem>(actModel));
        } else {
            SDToast.showToast(getString(R.string.no_more_data));
        }
    }
}
