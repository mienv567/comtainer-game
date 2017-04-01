package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveMainSmallSingle;
import com.fanwe.live.model.CategoryModel;

import java.util.List;

/**
 * 主页小图片适配holder
 */
public class LiveMainSmallItemHolder extends SDRecyclerViewHolder
{

    public LiveMainSmallItemHolder(View itemView, SDRecyclerAdapter<List<CategoryModel>> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    public final void bindData(int position, Object itemModel)
    {
        List<CategoryModel> list = (List<CategoryModel>)itemModel;
        ItemLiveMainSmallSingle item0 = find(R.id.item_view0);
        ItemLiveMainSmallSingle item1 = find(R.id.item_view1);
        item0.setModel(SDCollectionUtil.get(list, 0));
        item1.setModel(SDCollectionUtil.get(list, 1));
        item0.setOnClickListener(item0);
        item1.setOnClickListener(item1);
    }


}
