package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.activity.PlayerInfoActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PlayerIntroductionModel;
import com.fanwe.live.model.PlayerListModel;
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

    List<PlayerIntroductionModel> mModels;
    private BaseQuickAdapter mAdapter;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_guide;
    }

    @Override
    protected void init() {
        tv_title.setText("指引");
        tv_right.setVisibility(View.GONE);
        iv_left.setVisibility(View.GONE);

        mModels = new ArrayList<>();

        mAdapter = new BaseQuickAdapter<PlayerIntroductionModel,
                BaseViewHolder>(R.layout.item_guide, mModels) {

            @Override
            protected void convert(BaseViewHolder helper, PlayerIntroductionModel item) {
                ImageView playerHead = helper.getView(R.id.iv_head_image);
                SDViewBinder.setImageView(getActivity(), playerHead, item.headImage);
                helper.setText(R.id.tv_player_name, item.nickName);
                helper.setText(R.id.tv_introduce, item.introduce);
            }
        };
        View view = View.inflate(getActivity(), R.layout.layout_task_guide_header, null);
        mAdapter.addHeaderView(view);
        list_guide.getRefreshableView().setLinearVertical();
        list_guide.getRefreshableView().setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PlayerInfoActivity.class);
                intent.putExtra("player_info", (PlayerIntroductionModel) adapter.getItem(position));
                getActivity().startActivity(intent);
            }
        });

        requestPlayerList();
    }

    private void requestPlayerList() {
        CommonInterface.requestPlayerList(new AppRequestCallback<PlayerListModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk()) {
                    if (actModel.players != null) {
                        mModels = actModel.players;
                        mAdapter.setNewData(mModels);
                    }
                }
            }
        });
    }

}
