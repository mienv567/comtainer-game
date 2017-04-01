package com.fanwe.live.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.activity.PlayerInfoActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PlayerIntrodutionModel;
import com.fanwe.live.view.SDProgressPullToRefreshRecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.liu on 2017/3/22.
 */

public class LiveGuideFragment extends BaseFragment {

    @ViewInject(R.id.iv_left)
    ImageView iv_left;

    @ViewInject(R.id.tv_title)
    TextView tv_title;

    @ViewInject(R.id.tv_right)
    TextView tv_right;

    @ViewInject(R.id.list_guide)
    SDProgressPullToRefreshRecyclerView list_guide;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_guide;
    }

    @Override
    protected void init() {
        tv_title.setText("指引");
        tv_right.setVisibility(View.GONE);
        iv_left.setVisibility(View.GONE);
        List<String> data = new ArrayList();
        for (int i = 0; i < 10; i++) {
            data.add("111");
        }
        BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_guide, data) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        };
        View view = View.inflate(getActivity(), R.layout.layout_task_guide_header, null);
        adapter.addHeaderView(view);
        list_guide.getRefreshableView().setLinearVertical();
        list_guide.getRefreshableView().setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PlayerInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });

        CommonInterface.requestPlayerList(new AppRequestCallback<List<PlayerIntrodutionModel>>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                Log.d("LiveGuideFragment", "onSuccess: ");
            }
        });
    }
    
}
