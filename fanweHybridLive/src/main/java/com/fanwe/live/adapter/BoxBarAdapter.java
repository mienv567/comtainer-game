package com.fanwe.live.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.model.MultipleItem;

import java.util.List;

/**
 * Created by cheng.yuan on 2017/4/5.
 */

public class BoxBarAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    private Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BoxBarAdapter(Context context, List<MultipleItem> data) {
        super(data);
        mContext = context;
        addItemType(MultipleItem.HEAD, R.layout.item_multiple_head);
        addItemType(MultipleItem.POPULAR_VIDEO, R.layout.item_popular_video);
        addItemType(MultipleItem.BOX_BAR, R.layout.item_box_bar);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.HEAD:
                break;
            case MultipleItem.POPULAR_VIDEO:
                break;
            case MultipleItem.BOX_BAR:
                break;
        }
    }
}
