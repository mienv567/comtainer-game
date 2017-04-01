package com.fanwe.library.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.listener.SDItemClickListener;
import com.fanwe.library.listener.SDItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
public abstract class SDRecyclerAdapter<T> extends RecyclerView.Adapter<SDRecyclerViewHolder> implements ISDAdapter<T>
{

    private Activity activity;
    private List<T> listModel = new ArrayList<>();

    private SDSelectManager<T> selectManager = new SDSelectManager<>();
    private SDSelectManager.SelecteStateListener<T> selectStateListener;
    private SDItemClickListener<T> itemClickListener;
    private SDItemLongClickListener<T> itemLongClickListener;

    public void setSelectStateListener(SDSelectManager.SelecteStateListener<T> selectStateListener)
    {
        this.selectStateListener = selectStateListener;
    }

    public void setItemClickListener(SDItemClickListener<T> itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(SDItemLongClickListener<T> itemLongClickListener)
    {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void notifyItemClickListener(int position, T item, View view)
    {
        if (itemClickListener != null)
        {
            itemClickListener.onClick(position, item, view);
        }
    }

    public void notifyItemLongClickListener(int position, T item, View view)
    {
        if (itemLongClickListener != null)
        {
            itemLongClickListener.onLongClick(position, item, view);
        }
    }

    public SDSelectManager<T> getSelectManager()
    {
        return selectManager;
    }

    public SDRecyclerAdapter(Activity activity)
    {
        super();
        this.activity = activity;
        initSDSelectManager();
    }

    @Override
    public Activity getActivity()
    {
        return activity;
    }

    @Override
    public View inflate(int resource, ViewGroup root)
    {
        return inflate(resource, root, false);
    }

    @Override
    public View inflate(int resource, ViewGroup root, boolean attachToRoot)
    {
        return getActivity().getLayoutInflater().inflate(resource, root, attachToRoot);
    }

    @Override
    public int indexOf(T model)
    {
        return listModel.indexOf(model);
    }

    @Override
    public void setData(List<T> list)
    {
        if (list != null)
        {
            this.listModel = list;
        } else
        {
            this.listModel.clear();
        }
        selectManager.setItems(listModel);
        selectManager.synchronizedSelected();
    }

    @Override
    public void updateData(List<T> list)
    {
        setData(list);
        notifyDataSetChanged();
    }

    @Override
    public void clearData()
    {
        updateData(null);
    }

    @Override
    public List<T> getData()
    {
        return listModel;
    }

    @Override
    public T getItemData(int position)
    {
        if (isPositionLegal(position))
        {
            return listModel.get(position);
        }
        return null;
    }

    @Override
    public void appendData(T model)
    {
        if (model != null)
        {
            listModel.add(model);
            selectManager.synchronizedSelected(model);
            notifyItemInserted(listModel.size() - 1);
        }
    }

    @Override
    public void appendData(List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
            int positionStart = listModel.size() - 1;
            int itemCount = list.size();

            listModel.addAll(list);
            selectManager.synchronizedSelected(list);
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    public void removeData(T model)
    {
        if (model != null)
        {
            int position = listModel.indexOf(model);
            removeData(position);
        }
    }

    @Override
    public void removeData(int position)
    {
        if (isPositionLegal(position))
        {
            selectManager.setSelected(position, false);
            listModel.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void insertData(int position, T model)
    {
        if (model != null)
        {
            listModel.add(position, model);
            selectManager.synchronizedSelected(position);
            notifyItemInserted(position);
        }
    }

    @Override
    public void insertData(int position, List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
            int positionStart = position;
            int itemCount = list.size();

            listModel.addAll(position, list);
            selectManager.synchronizedSelected(list);
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    public void updateData(int position, T model)
    {
        if (model != null && isPositionLegal(position))
        {
            listModel.set(position, model);
            selectManager.synchronizedSelected(model);
            notifyItemChanged(position);
        }
    }

    @Override
    public void updateData(int position)
    {
        if (isPositionLegal(position))
        {
            notifyItemChanged(position);
        }
    }

    @Override
    public boolean isPositionLegal(int position)
    {
        if (position >= 0 && position < listModel.size())
        {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount()
    {
        if (listModel != null)
        {
            return listModel.size();
        }
        return 0;
    }

    @Override
    public SDRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public final void onBindViewHolder(SDRecyclerViewHolder holder, int position)
    {
        bindData(holder, position, getItemData(position));
    }

    /**
     * 重写此方法绑定数据
     *
     * @param holder
     * @param position
     * @param model
     */
    public void bindData(SDRecyclerViewHolder holder, int position, T model)
    {
        holder.bindData(position, model);
    }

    private void initSDSelectManager()
    {
        selectManager.setMode(SDSelectManager.Mode.SINGLE);
        selectManager.setListener(new SDSelectManager.SDSelectManagerListener<T>()
        {
            @Override
            public void onNormal(int index, T item)
            {
                onNormalItem(index, item);
                if (selectStateListener != null)
                {
                    selectStateListener.onNormal(index, item);
                }
            }

            @Override
            public void onSelected(int index, T item)
            {
                onSelectedItem(index, item);
                if (selectStateListener != null)
                {
                    selectStateListener.onSelected(index, item);
                }
            }
        });
    }

    /**
     * item被置为正常状态的时候回调
     *
     * @param position
     * @param item
     */
    protected void onNormalItem(int position, T item)
    {
        if (item instanceof SDSelectManager.SDSelectable)
        {
            SDSelectManager.SDSelectable selectable = (SDSelectManager.SDSelectable) item;
            selectable.setSelected(false);
        }
        updateData(position);
    }

    /**
     * item被置为选中状态的时候回调
     *
     * @param position
     * @param item
     */
    protected void onSelectedItem(int position, T item)
    {
        if (item instanceof SDSelectManager.SDSelectable)
        {
            SDSelectManager.SDSelectable selectable = (SDSelectManager.SDSelectable) item;
            selectable.setSelected(true);
        }
        updateData(position);
    }

}
