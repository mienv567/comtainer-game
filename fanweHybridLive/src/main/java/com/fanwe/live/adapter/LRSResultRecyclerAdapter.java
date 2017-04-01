package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.LRSUserModel;

/**
 * 狼人杀游戏结果
 */
public class LRSResultRecyclerAdapter extends SDSimpleRecyclerAdapter<LRSUserModel> {
    public LRSResultRecyclerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_game_result;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, final LRSUserModel model) {
        TextView tv_num = holder.get(R.id.tv_num);
        TextView tv_profession = holder.get(R.id.tv_profession);
        TextView tv_nickname = holder.get(R.id.tv_nickname);
        SDViewBinder.setTextView(tv_num,model.getIndex()+"");
        SDViewBinder.setTextView(tv_profession,getUserRole(model.getIdentity()));
        SDViewBinder.setTextView(tv_nickname,model.getNick_name());
    }

    private String getUserRole(int identity) {
        String result = "";
        switch (identity) {
            case LRSUserModel.GAME_ROLE_WOLF:
                result = "狼人";
                break;
            case LRSUserModel.GAME_ROLE_WITCH:
                result = "女巫";
                break;
            case LRSUserModel.GAME_ROLE_PROPHET:
                result = "预言家";
                break;
            case LRSUserModel.GAME_ROLE_HUNTER:
                result = "猎人";
                break;
            case LRSUserModel.GAME_ROLE_CIVILIAN:
                result = "平民";
                break;
        }
        return result;
    }
}
