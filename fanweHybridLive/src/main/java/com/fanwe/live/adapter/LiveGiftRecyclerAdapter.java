package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveGiftModel;

public class LiveGiftRecyclerAdapter extends SDSimpleRecyclerAdapter<LiveGiftModel> {


    private final SDSelectManager<LiveGiftModel> mSelectManager;

    public LiveGiftRecyclerAdapter(Activity activity) {
        super(activity);
        mSelectManager = getSelectManager();
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_gift;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, final int position, final LiveGiftModel model) {
        View ll_live = holder.get(R.id.ll_live_gift);
        ImageView iv_gift = holder.get(R.id.iv_gift);
        ImageView iv_much = holder.get(R.id.iv_much);
        TextView tv_price = holder.get(R.id.tv_price);
        TextView tv_score = holder.get(R.id.tv_score);
        TextView tv_gift_num = holder.get(R.id.iv_gift_num);
        if (model.isSelected()) {
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
            if (TextUtils.isEmpty(model.getPreviewUrl())) {
                SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            } else {
                Glide.with(getActivity())
                        .load(model.getPreviewUrl()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_gift);
            }
        } else {
            SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
            if (model.getIs_much() == 1) {
                SDViewUtil.show(iv_much);
            } else {
                SDViewUtil.hide(iv_much);
            }
        }
        if (model.getSuperimposedType() == 1) {
            if (model.getPropCount() > 0) {
                SDViewBinder.setTextView(tv_gift_num, String.valueOf(model.getPropCount()));
                tv_gift_num.setVisibility(View.VISIBLE);
            } else {
                tv_gift_num.setVisibility(View.GONE);
            }
        } else {
            tv_gift_num.setVisibility(View.GONE);
        }
        SDViewBinder.setTextView(tv_price, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_score, model.getScoreFromat());
        ll_live.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSelectManager.performClick(position);
            }
        });
    }
}
