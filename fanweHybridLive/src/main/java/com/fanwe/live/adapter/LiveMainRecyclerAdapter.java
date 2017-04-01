package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.adapter.viewholder.LiveMainBigItemHolder;
import com.fanwe.live.adapter.viewholder.LiveMainSmallItemHolder;
import com.fanwe.live.model.CategoryDetailModel;
import com.fanwe.live.model.CategoryModel;
import com.fanwe.live.model.LiveRoomModel;

import java.util.List;

/**
 * 主界面adapter
 */
public class LiveMainRecyclerAdapter extends SDRecyclerAdapter<LiveRoomModel> {
    public LiveMainRecyclerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public SDRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SDRecyclerViewHolder viewHolder = null;
        viewHolder = new LiveMainNewItemHolder(inflate(R.layout.item_live_main_new, parent), this);
        return viewHolder;
    }
}
