package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.adapter.viewholder.LiveCategoryHeadItemHolder;
import com.fanwe.live.adapter.viewholder.LiveCategoryItemHolder;
import com.fanwe.live.model.LiveRoomModel;

import java.util.List;

/**
 * 直播栏目adapter
 */
public class LiveCategoryRecyclerAdapter extends SDRecyclerAdapter<List<LiveRoomModel>> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    public static final int ITEM_VIEW_TYPE_ITEM = 1;
    public static final int ITEM_VIEW_TYPE_ITEM_START = 2;
    public static final int ITEM_VIEW_TYPE_ITEM_END = 3;
    public static final int ITEM_VIEW_TYPE_ITEM_ONE = 4;//只有一行数据的情况下
    public LiveCategoryRecyclerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == 0){
            return ITEM_VIEW_TYPE_HEADER;
        }else if(getItemCount() == 2) {
            return ITEM_VIEW_TYPE_ITEM_ONE;
        }else if(position == 1){
            return ITEM_VIEW_TYPE_ITEM_START;
        }else if(position == getItemCount() -1){
            return ITEM_VIEW_TYPE_ITEM_END;
        }else{
            return ITEM_VIEW_TYPE_ITEM;
        }
    }

    @Override
    public SDRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        SDRecyclerViewHolder viewHolder = null;
        switch (viewType){
            case ITEM_VIEW_TYPE_HEADER:
                viewHolder = new LiveCategoryHeadItemHolder(inflate(R.layout.item_live_category_header, parent), this);
                break;
            case ITEM_VIEW_TYPE_ITEM:
            case ITEM_VIEW_TYPE_ITEM_START:
            case ITEM_VIEW_TYPE_ITEM_END:
            case ITEM_VIEW_TYPE_ITEM_ONE:
                viewHolder = new LiveCategoryItemHolder(inflate(R.layout.item_live_category, parent), this,viewType);
                break;
        }
        return viewHolder;
    }
}
