package com.fanwe.live.appview;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.live.R;
import com.fanwe.live.adapter.GridItemDecoration;
import com.fanwe.live.adapter.LiveLikeRecyclerAdapter;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LiveLikeView extends BaseAppView {

    @ViewInject(R.id.recyclerView_like)
    private RecyclerView recyclerView_like;
    @ViewInject(R.id.tv_available_like)
    private TextView tv_available_like;
    @ViewInject(R.id.tv_send_like)
    private TextView tv_send_like;

    private List<LiveLikeModel> likeModels;
    private LiveLikeRecyclerAdapter mAdapter;
    private UserModel user;

    public LiveLikeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveLikeView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.layout_live_send_like;
    }

    @Override
    protected void baseConstructorInit() {
        likeModels = new ArrayList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        HorizontalPageLayoutManager layoutManager = new HorizontalPageLayoutManager(2, 4);
        recyclerView_like.setLayoutManager(layoutManager);
        recyclerView_like.addItemDecoration(new GridItemDecoration(getContext(), 1,
                getResources().getColor(R.color.division)));
        scrollHelper.setUpRecycleView(recyclerView_like);
        scrollHelper.updateLayoutManger();
        ((DefaultItemAnimator) recyclerView_like.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new LiveLikeRecyclerAdapter(getActivity());
        recyclerView_like.setAdapter(mAdapter);
    }

    public void setData(List<LiveLikeModel> likeModels) {
        this.likeModels = likeModels;
        mAdapter.setData(likeModels);
    }

    public void updateAvailableLike(int likesCount) {
        tv_available_like.setText("" + likesCount + "");
    }

    public void SetUserModel(UserModel result) {
        user = result;
    }
}
