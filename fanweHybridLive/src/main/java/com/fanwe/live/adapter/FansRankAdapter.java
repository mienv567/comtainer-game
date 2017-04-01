package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.PlayerRankModel;

import java.util.List;

public class FansRankAdapter extends SDSimpleAdapter<PlayerRankModel> {

    private Activity mActivity;
    private int type;

    public FansRankAdapter(List<PlayerRankModel> listModel, Activity activity, int type) {
        super(listModel, activity);
        mActivity = activity;
        this.type = type;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_fans_rank;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, PlayerRankModel model) {
        TextView tv_rank_number = get(R.id.tv_rank_number, convertView);
        ImageView iv_player_head = get(R.id.iv_player_head, convertView);
        TextView tv_player_name = get(R.id.tv_player_name, convertView);
        TextView tv_rank_type = get(R.id.tv_rank_type, convertView);
        TextView tv_total_number = get(R.id.tv_total_number, convertView);
        SDViewBinder.setTextView(tv_rank_number, mActivity.getString(R.string.rank_number, position + 4));
        if (position < 2) {
            tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_orange);
        } else {
            tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_gray);
        }
        Glide.with(mActivity)
                .load(model.userInfo.thumbHeadImage)
                .into(iv_player_head);
        SDViewBinder.setTextView(tv_player_name, model.userInfo.nickName);
        if (type == 0) {
            tv_rank_type.setText("日贡献");
        } else if (type == 1) {
            tv_rank_type.setBackgroundResource(R.drawable.ic_live_honor_normal);
        }
        SDViewBinder.setTextView(tv_total_number, String.valueOf(model.score));
    }
}
