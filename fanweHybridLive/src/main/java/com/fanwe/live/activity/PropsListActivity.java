package com.fanwe.live.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveVPropsRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.BaseGoodsModel;
import com.fanwe.live.model.GoodsDetailModel;
import com.fanwe.live.model.GoodsListModel;
import com.fanwe.live.model.GoodsTypeModel;
import com.fanwe.live.view.SDProgressPullToRefreshRecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class PropsListActivity extends BaseTitleActivity {

    @ViewInject(R.id.recyclerView_props)
    SDProgressPullToRefreshRecyclerView recyclerView_props;
    private LiveVPropsRecyclerAdapter livePropsAdapter;
    private List<BaseGoodsModel> goodsList = new ArrayList<>();

    @Override
    protected int onCreateContentView() {
        return R.layout.activity_props_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mTitle.setMiddleTextTop(getString(R.string.props_list));
        livePropsAdapter = new LiveVPropsRecyclerAdapter(this);
        recyclerView_props.getRefreshableView().setLayoutManager(new LinearLayoutManager(mActivity));
        //        expandable_list.setAdapter(livePropsAdapter);
        //        expandable_list.setEmptyView(tv_empty_view);
        recyclerView_props.getRefreshableView().setAdapter(livePropsAdapter);
        requestGoods();
    }

    private void requestGoods() {
        CommonInterface.requestGoodsList(new AppRequestCallback<GoodsListModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    for (GoodsTypeModel typeModel : actModel.getTypes()) {
                        typeModel.isTitle =true;
                        goodsList.add(typeModel);
                        for (GoodsDetailModel detailModel : typeModel.getGoods()) {
                            detailModel.isTitle =false;
                            goodsList.add(detailModel);
                        }
                    }
                    livePropsAdapter.updateData(goodsList);
                }
            }
        });
    }

}
