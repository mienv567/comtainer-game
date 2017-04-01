package com.fanwe.live.adapter.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveCategoryRecyclerAdapter;
import com.fanwe.live.appview.ItemLiveCategorySingle;
import com.fanwe.live.model.LiveRoomModel;

import java.util.List;

/**
 * column头部
 */
public  class LiveCategoryItemHolder extends SDRecyclerViewHolder
{

    private LinearLayout ll_root;

    public LiveCategoryItemHolder(View itemView, SDRecyclerAdapter<List<LiveRoomModel>> adapter, int type)
    {
        super(itemView, adapter);
        ll_root = find(R.id.ll_root);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT );
        switch(type){
            case LiveCategoryRecyclerAdapter.ITEM_VIEW_TYPE_ITEM_START:
                ll_root.setBackgroundDrawable(getAdapter().getActivity().getResources().getDrawable(R.drawable.layer_white_corner_5dp_start));
                break;
            case LiveCategoryRecyclerAdapter.ITEM_VIEW_TYPE_ITEM_END:
                ll_root.setBackgroundDrawable(getAdapter().getActivity().getResources().getDrawable(R.drawable.layer_white_corner_5dp_end));
                params.bottomMargin = SDViewUtil.dp2px(10);
                params.leftMargin = SDViewUtil.dp2px(5);
                params.rightMargin = SDViewUtil.dp2px(5);
                ll_root.setLayoutParams(params);
                break;
            case LiveCategoryRecyclerAdapter.ITEM_VIEW_TYPE_ITEM_ONE:
                ll_root.setBackgroundDrawable(getAdapter().getActivity().getResources().getDrawable(R.drawable.layer_white_corner_5dp));
                params.bottomMargin = SDViewUtil.dp2px(10);
                params.leftMargin = SDViewUtil.dp2px(5);
                params.rightMargin = SDViewUtil.dp2px(5);
                ll_root.setLayoutParams(params);
                break;
            default:
                ll_root.setBackgroundColor(getAdapter().getActivity().getResources().getColor(R.color.white));
                break;
        }
    }

    @Override
    public final void bindData(int position, Object itemModel)
    {
        List<LiveRoomModel> list = (List<LiveRoomModel>)itemModel;
        ItemLiveCategorySingle item0 = find(R.id.item_view0);
        ItemLiveCategorySingle item1 = find(R.id.item_view1);
        item0.setModel(SDCollectionUtil.get(list, 0));
        item1.setModel(SDCollectionUtil.get(list, 1));
        item0.setOnClickListener(item0);
        item1.setOnClickListener(item1);
    }

}
