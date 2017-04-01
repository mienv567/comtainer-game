package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.event.ECreateCategoryChoose;
import com.fanwe.live.model.CategoryNameModel;
import com.sunday.eventbus.SDEventManager;

/**
 * 选择主题列表
 */
public class CategoryRecyclerAdapter extends SDSimpleRecyclerAdapter<CategoryNameModel> {
    public CategoryRecyclerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_choose_category;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, final CategoryNameModel model) {
        TextView tv_name = holder.get(R.id.tv_name);

        SDViewBinder.setTextView(tv_name, model.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDEventManager.post(new ECreateCategoryChoose(model.getCategory_id(), model.getCategory_name()));
            }
        });
    }
}
