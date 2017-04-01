package com.fanwe.live.adapter;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveMainBigSingle;
import com.fanwe.live.appview.ItemLiveMainNew;
import com.fanwe.live.model.CategoryDetailModel;
import com.fanwe.live.model.LiveRoomModel;

import java.util.List;

/**
 * Created by kevin.liu on 2017/3/17.
 */
public class LiveMainNewItemHolder extends SDRecyclerViewHolder {
    public LiveMainNewItemHolder(View view, SDRecyclerAdapter<LiveRoomModel> adapter) {
        super(view,adapter);
    }

    @Override
    public void bindData(int position, Object itemModel) {
        LiveRoomModel model = (LiveRoomModel) itemModel;
        final ItemLiveMainNew item0 = find(R.id.item_view0);
        item0.setModel(model);
        item0.setOnClickListener(item0);
    }
}
