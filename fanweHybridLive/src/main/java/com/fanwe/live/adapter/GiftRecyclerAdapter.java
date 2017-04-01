package com.fanwe.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveGiftModel;

import java.util.ArrayList;
import java.util.List;

public class GiftRecyclerAdapter extends RecyclerView.Adapter<GiftRecyclerAdapter.GiftViewHolder> {

    private Context mContext;
    private List<LiveGiftModel> mGiftModels;
    private int lastSelectPosition;

    public GiftRecyclerAdapter(Context context) {
        mContext = context;
        mGiftModels = new ArrayList<>();
    }

    public void setGiftModels(List<LiveGiftModel> giftModels) {
        mGiftModels = giftModels;
        notifyDataSetChanged();
    }

    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GiftViewHolder holder, int position) {
        final LiveGiftModel model = mGiftModels.get(position);
        if (model.isSelected()) {
            holder.ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
            if (TextUtils.isEmpty(model.getPreviewUrl())) {
                SDViewBinder.setImageView(mContext, model.getIcon(), holder.iv_gift);
            } else {
                Glide.with(mContext)
                        .load(model.getPreviewUrl()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_gift);
            }
            lastSelectPosition = position;
        } else {
            SDViewBinder.setImageView(mContext, model.getIcon(), holder.iv_gift);
            holder.ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
            if (model.getIs_much() == 1) {
                SDViewUtil.show(holder.iv_much);
            } else {
                SDViewUtil.hide(holder.iv_much);
            }
        }
        if (model.getSuperimposedType() == 1) {
            if (model.getPropCount() > 0) {
                SDViewBinder.setTextView(holder.tv_gift_num, String.valueOf(model.getPropCount()));
                holder.tv_gift_num.setVisibility(View.VISIBLE);
            } else {
                holder.tv_gift_num.setVisibility(View.GONE);
            }
        } else {
            holder.tv_gift_num.setVisibility(View.GONE);
        }
        SDViewBinder.setTextView(holder.tv_price, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(holder.tv_score, model.getScoreFromat());
        holder.ll_live.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lastSelectPosition == holder.getAdapterPosition()) {
                    model.setSelected(!model.isSelected());
                } else {
                    mGiftModels.get(lastSelectPosition).setSelected(false);
                    notifyItemChanged(lastSelectPosition, "refresh");
                    model.setSelected(true);
                }
                notifyItemChanged(holder.getAdapterPosition(), "refresh");
            }
        });
    }

    @Override
    public void onBindViewHolder(GiftViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Log.d("GiftRecyclerAdapter", "onBindViewHolder: position=" + position);
            final LiveGiftModel model = mGiftModels.get(position);
            if (model.isSelected()){
                holder.ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
            } else {
                holder.ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGiftModels.size();
    }

    static class GiftViewHolder extends RecyclerView.ViewHolder {

        View ll_live;
        ImageView iv_gift;
        ImageView iv_much;
        TextView tv_price;
        TextView tv_score;
        TextView tv_gift_num;

        public GiftViewHolder(View itemView) {
            super(itemView);
            ll_live = itemView.findViewById(R.id.ll_live_gift);
            iv_gift = (ImageView) itemView.findViewById(R.id.iv_gift);
            iv_much = (ImageView) itemView.findViewById(R.id.iv_much);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_gift_num = (TextView) itemView.findViewById(R.id.iv_gift_num);
        }
    }
}
