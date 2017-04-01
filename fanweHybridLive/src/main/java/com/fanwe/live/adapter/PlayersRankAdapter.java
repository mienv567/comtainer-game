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

public class PlayersRankAdapter extends SDSimpleAdapter<PlayerRankModel> {

    private Activity mActivity;

    public PlayersRankAdapter(List<PlayerRankModel> listModel, Activity activity) {
        super(listModel, activity);
        mActivity = activity;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_players_rank;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, PlayerRankModel model) {
        TextView tv_rank_number = get(R.id.tv_rank_number, convertView);
        ImageView iv_player_head = get(R.id.iv_player_head, convertView);
        TextView tv_player_name = get(R.id.tv_player_name, convertView);
        ImageView iv_rank_trend = get(R.id.iv_rank_trend, convertView);
        tv_rank_number.setText(mActivity.getString(R.string.rank_number, position + 4));
        Glide.with(mActivity)
                .load(model.userInfo.thumbHeadImage)
                .into(iv_player_head);
        SDViewBinder.setTextView(tv_player_name, model.userInfo.nickName);
        if (position < 2) {
            tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_orange);
        } else {
            tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_gray);
        }
        switch (model.isUp) {
            case 1:
                iv_rank_trend.setBackgroundResource(R.drawable.ic_live_rank_up);
                break;
            case -1:
                iv_rank_trend.setBackgroundResource(R.drawable.ic_live_rank_down);
                break;
            default:
                iv_rank_trend.setBackgroundResource(R.drawable.ic_live_rank_unchanged);
        }
    }
}
