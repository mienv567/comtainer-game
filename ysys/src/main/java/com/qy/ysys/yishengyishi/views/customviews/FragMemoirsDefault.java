package com.qy.ysys.yishengyishi.views.customviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.adapter.MemoirListAdapter;
import com.qy.ysys.yishengyishi.event.ENeedRefreshMemoirs;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.MemoirListModel;
import com.qy.ysys.yishengyishi.model.item.MemoirListItem;
import com.qy.ysys.yishengyishi.widgets.SDPullToRefreshGridRecyclerView;
import com.qy.ysys.yishengyishi.widgets.SDRecyclerView;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragMemoirsDefault extends BaseTitleFragment {

    @BindView(R2.id.recyclerView)
    SDPullToRefreshGridRecyclerView recyclerView;
    private MemoirListAdapter mAdapter;
    @Override
    protected void initTitleBar(ITitleView titleView) {

    }

    @Override
    protected boolean shouldShowTitle() {
        return false;
    }

    @Override
    protected View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_memoirs_default, null);
    }

    @Override
    protected void initView(View contentView) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initRecycleView();
        recyclerView.setRefreshing(true);
    }

    private void initRecycleView(){
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SDRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                requestFirstPageInfo();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                int count = mAdapter.getItemCount() + RequestInterface.PAGE_SIZE;
                int page = (count % RequestInterface.PAGE_SIZE == 0) ? (count / RequestInterface.PAGE_SIZE) : (count / RequestInterface.PAGE_SIZE) + 1;
                RequestInterface.queryFamilyMemoirList(page, RequestInterface.PAGE_SIZE, RequestInterface.MEMOIR_FM_TYPE_DEFAULT, new Callback<MemoirListModel>() {
                    @Override
                    public void onResponse(Call<MemoirListModel> call, Response<MemoirListModel> response) {
                        recyclerView.onRefreshComplete();
                        if (response.body().isSuccess()) {
                            List<MemoirListItem> item = response.body().getReturnObj();
                            if (item != null && item.size() > 0) {
                                mAdapter.getDatas().addAll(response.body().getReturnObj());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MemoirListModel> call, Throwable t) {
                        recyclerView.onRefreshComplete();
                    }
                });
            }
        });

        mAdapter = new MemoirListAdapter(getActivity());
        recyclerView.getRefreshableView().setAdapter(mAdapter);
    }

    private void requestFirstPageInfo() {
        RequestInterface.queryFamilyMemoirList(1, RequestInterface.PAGE_SIZE, RequestInterface.MEMOIR_FM_TYPE_DEFAULT, new Callback<MemoirListModel>() {
            @Override
            public void onResponse(Call<MemoirListModel> call, Response<MemoirListModel> response) {
                recyclerView.onRefreshComplete();
                if (response.body().isSuccess()) {
                    mAdapter.setDatas(response.body().getReturnObj());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MemoirListModel> call, Throwable t) {
                recyclerView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onEventMainThread(ENeedRefreshMemoirs event){
        requestFirstPageInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
