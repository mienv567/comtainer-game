package com.fanwe.library.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;

/**
 * Created by Administrator on 2016/9/7.
 */
public abstract class SDSimpleRecyclerAdapter<T> extends SDRecyclerAdapter<T>
{
    public SDSimpleRecyclerAdapter(Activity activity)
    {
        super(activity);
    }

    @Override
    public SDRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        int layoutId = getLayoutId(parent, viewType);
        View itemView = inflate(layoutId, parent);
        SDRecyclerViewHolder holder = new SDRecyclerViewHolder(itemView, this)
        {
            @Override
            public void bindData(int position, Object itemModel)
            {
            }
        };
        initViewHolder(holder, viewType, parent);
        return holder;
    }

    /**
     * 初始化viewHolder，仅在创建viewHolder的时候被调用
     *
     * @param holder
     * @param viewType
     * @param parent
     */
    protected void initViewHolder(SDRecyclerViewHolder holder, int viewType, ViewGroup parent)
    {

    }

    /**
     * 返回布局id
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(ViewGroup parent, int viewType);

}
