package com.fanwe.live.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.adapter.LiveVPropsRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.BaseGoodsModel;
import com.fanwe.live.model.GoodsDetailModel;
import com.fanwe.live.model.GoodsListModel;
import com.fanwe.live.model.GoodsTypeModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LivePropsFragment extends BaseFragment {

    private LiveActivity mActivity;

    /*@ViewInject(R.id.expandable_list)
    private ExpandableListView expandable_list;
    @ViewInject(R.id.tv_empty_view)
    private TextView tv_empty_view;*/
    @ViewInject(R.id.recyclerView_props)
    private RecyclerView recyclerView;
    private LiveVPropsRecyclerAdapter livePropsAdapter;
    private List<BaseGoodsModel> goodsList = new ArrayList<>();

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_live_props;
    }

    @Override
    protected void init() {
        super.init();
        mActivity = (LiveActivity) getActivity();
        livePropsAdapter = new LiveVPropsRecyclerAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        //        expandable_list.setAdapter(livePropsAdapter);
        //        expandable_list.setEmptyView(tv_empty_view);
        recyclerView.setAdapter(livePropsAdapter);
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
