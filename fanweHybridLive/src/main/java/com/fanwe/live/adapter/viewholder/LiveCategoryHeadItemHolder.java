package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveCategoryHeadSingle;
import com.fanwe.live.model.LiveRoomModel;

import java.util.List;

/**
 * column头部
 */
public  class LiveCategoryHeadItemHolder extends SDRecyclerViewHolder
{

    public LiveCategoryHeadItemHolder(View itemView, SDRecyclerAdapter<List<LiveRoomModel>> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    public final void bindData(int position, Object itemModel)
    {
        List<LiveRoomModel> list = (List<LiveRoomModel>)itemModel;
        ItemLiveCategoryHeadSingle item0 = find(R.id.item_view0);
        item0.setModel(SDCollectionUtil.get(list, 0));
        item0.setOnClickListener(item0);
    }


}
