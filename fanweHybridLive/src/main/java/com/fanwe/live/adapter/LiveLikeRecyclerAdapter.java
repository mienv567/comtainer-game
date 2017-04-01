package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;

public class LiveLikeRecyclerAdapter extends SDSimpleRecyclerAdapter<LiveLikeModel> {

    private final SDSelectManager<LiveLikeModel> mSelectManager;

    public LiveLikeRecyclerAdapter(Activity activity) {
        super(activity);
        mSelectManager = getSelectManager();
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_like;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, final int position, LiveLikeModel model) {
        View rootView = holder.get(R.id.ll_live_like);
        ImageView likeIcon = holder.get(R.id.iv_like_icon);
        TextView likeName = holder.get(R.id.tv_like_name);
        if (model.isSelected()) {
            rootView.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
        } else {
            rootView.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
        }
        Glide.with(getActivity())
                .load(model.getIcon()).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(likeIcon);
        likeName.setText(model.getName());

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectManager.performClick(position);
            }
        });
    }
}
