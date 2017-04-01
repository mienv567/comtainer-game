package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.PlayerRankModel;

import java.util.List;

public class LiveFansRankAdapter extends SDSimpleAdapter<PlayerRankModel> {

    private Activity mActivity;

    public LiveFansRankAdapter(List<PlayerRankModel> listModel, Activity activity) {
        super(listModel, activity);
        mActivity = activity;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_fans_rank;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, PlayerRankModel model) {
        TextView tv_rank_number = get(R.id.tv_rank_number, convertView);
        ImageView iv_user_head = get(R.id.iv_user_head, convertView);
        ImageView iv_crown = get(R.id.iv_crown, convertView);
        TextView tv_player_name = get(R.id.tv_player_name, convertView);
        TextView tv_user_level = get(R.id.tv_user_level, convertView);
        TextView tv_score = get(R.id.tv_contribution_score, convertView);
        ImageView iv_rank_trend = get(R.id.iv_rank_trend, convertView);

        SDViewBinder.setTextView(tv_rank_number, mActivity.getString(R.string.rank_number, position + 1));
        Glide.with(mActivity)
                .load(model.userInfo.thumbHeadImage)
                .into(iv_user_head);
        SDViewBinder.setTextView(tv_player_name, model.userInfo.nickName);
        SDViewBinder.setTextView(tv_user_level, "LV" + model.userInfo.level);
        SDViewBinder.setTextView(tv_score, "贡献 " + model.score);
        switch (position) {
            case 0:
                tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_top1);
                SDViewUtil.show(iv_crown);
                tv_user_level.setBackgroundResource(R.drawable.shape_rec_corner_3dp_top1);
                tv_score.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_contribution_top1, 0, 0, 0);
                break;
            case 1:
                tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_top2);
                SDViewUtil.hide(iv_crown);
                tv_user_level.setBackgroundResource(R.drawable.shape_rec_corner_3dp_top2);
                tv_score.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_contribution_top2, 0, 0, 0);
                break;
            case 2:
                tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_top3);
                SDViewUtil.hide(iv_crown);
                tv_user_level.setBackgroundResource(R.drawable.shape_rec_corner_3dp_top3);
                tv_score.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_contribution_top3, 0, 0, 0);
                break;
            default:
                tv_rank_number.setBackgroundResource(R.drawable.ic_live_rank_bg_others);
                SDViewUtil.hide(iv_crown);
                tv_user_level.setBackgroundResource(R.drawable.shape_rec_corner_3dp_others);
                tv_score.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_contribution_other, 0, 0, 0);
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
