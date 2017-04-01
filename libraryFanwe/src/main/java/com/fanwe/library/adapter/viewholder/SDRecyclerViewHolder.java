package com.fanwe.library.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;

/**
 * Created by Administrator on 2016/8/27.
 */
public abstract class SDRecyclerViewHolder extends RecyclerView.ViewHolder
{

    private SDRecyclerAdapter<?> adapter;
    private SparseArray<View> arrayView;

    public SDRecyclerViewHolder(View itemView, SDRecyclerAdapter<?> adapter)
    {
        super(itemView);
        this.adapter = adapter;
    }

    public SDRecyclerAdapter<?> getAdapter()
    {
        return adapter;
    }

    /**
     * 在itemView中通过id查找view
     *
     * @param id
     * @param <V>
     * @return
     */
    public <V extends View> V find(int id)
    {
        return (V) itemView.findViewById(id);
    }

    /**
     * 在itemView中通过id查找view，如果需要频繁的通过id查找view，调用此方法查找效率较高
     *
     * @param id
     * @param <V>
     * @return
     */
    public <V extends View> V get(int id)
    {
        if (arrayView == null)
        {
            arrayView = new SparseArray<>();
        }
        View childView = arrayView.get(id);
        if (childView == null)
        {
            childView = itemView.findViewById(id);
            if (childView != null)
            {
                arrayView.put(id, childView);
            }
        }
        return (V) childView;
    }

    public abstract void bindData(int position, Object itemModel);
}
