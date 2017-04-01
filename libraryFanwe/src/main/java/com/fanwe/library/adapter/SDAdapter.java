package com.fanwe.library.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.common.SDSelectManager.Mode;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class SDAdapter<T> extends BaseAdapter
{

    protected List<T> listModel = new ArrayList<T>();
    public Activity mActivity;

    /**
     * 保存每个position的parentView
     */
    private SparseArray<ViewGroup> mapPositionParentView = new SparseArray<ViewGroup>();
    /**
     * 保存每个itemView对应的position
     */
    private Map<View, Integer> mapViewPosition = new LinkedHashMap<View, Integer>();

    private SDSelectManager<T> selectManager = new SDSelectManager<T>();
    private SDSelectManager.SelecteStateListener<T> selectStateListener;
    protected ItemClickListener<T> itemClickListener;
    protected ItemLongClickListener<T> itemLongClickListener;

    public SDAdapter(List<T> listModel, Activity activity)
    {
        selectManager.setMode(Mode.SINGLE);
        selectManager.setListener(defaultSelectListener);
        setData(listModel);
        this.mActivity = activity;
    }

    public void setSelectStateListener(SDSelectManager.SelecteStateListener<T> selectStateListener)
    {
        this.selectStateListener = selectStateListener;
    }

    public void setItemClickListener(ItemClickListener<T> itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(ItemLongClickListener<T> itemLongClickListener)
    {
        this.itemLongClickListener = itemLongClickListener;
    }

    public LayoutInflater getLayoutInflater()
    {
        return mActivity.getLayoutInflater();
    }

    public Activity getActivity()
    {
        return mActivity;
    }

    /**
     * 获得选择管理器
     *
     * @return
     */
    public SDSelectManager<T> getSelectManager()
    {
        return selectManager;
    }

    /**
     * 默认选择管理器监听
     */
    private SDSelectManagerListener<T> defaultSelectListener = new SDSelectManagerListener<T>()
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
    };

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
        updateItem(position);
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
        updateItem(position);
    }

    @Override
    public void notifyDataSetChanged()
    {
        clearViews();
        super.notifyDataSetChanged();
    }

    private void clearViews()
    {
        mapPositionParentView.clear();
        mapViewPosition.clear();
    }

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

    public void clearData()
    {
        updateData(null);
    }

    public List<T> getData()
    {
        return listModel;
    }

    public T getItemData(int position)
    {
        if (isPositionLegal(position))
        {
            return listModel.get(position);
        }
        return null;
    }

    public void appendData(T model)
    {
        if (model != null)
        {
            listModel.add(model);
            selectManager.synchronizedSelected(model);
            notifyDataSetChanged();
        }
    }

    public void appendData(List<T> list)
    {
        if (list != null && list.size() > 0)
        {
            listModel.addAll(list);
            selectManager.synchronizedSelected(list);
            notifyDataSetChanged();
        }
    }

    public void removeData(T model)
    {
        if (model != null)
        {
            int position = listModel.indexOf(model);
            removeData(position);
        }
    }

    public void removeData(int position)
    {
        if (isPositionLegal(position))
        {
            selectManager.setSelected(position, false);
            listModel.remove(position);
            notifyDataSetChanged();
        }
    }

    public void insertData(int position, T model)
    {
        if (model != null)
        {
            listModel.add(position, model);
            selectManager.synchronizedSelected(model);
            notifyDataSetChanged();
        }
    }

    public void insertData(int position, List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
            listModel.addAll(position, list);
            selectManager.synchronizedSelected(list);
            notifyDataSetChanged();
        }
    }

    public void updateData(int position, T model)
    {
        updateItem(position, model);
    }

    public void updateData(int position)
    {
        updateItem(position);
    }

    public void updateData(List<T> listModel)
    {
        setData(listModel);
        notifyDataSetChanged();
    }


    public boolean isPositionLegal(int position)
    {
        if (position >= 0 && position < listModel.size())
        {
            return true;
        } else
        {
            return false;
        }
    }

    public int getItemCount()
    {
        return getCount();
    }

    @Override
    public int getCount()
    {
        if (listModel != null)
        {
            return listModel.size();
        } else
        {
            return 0;
        }
    }

    @Override
    public T getItem(int position)
    {
        if (isPositionLegal(position))
        {
            return listModel.get(position);
        } else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        beforeOnGetView(position, convertView, parent);
        convertView = onGetView(position, convertView, parent);
        afterOnGetView(position, convertView, parent);
        return convertView;
    }

    protected void beforeOnGetView(int position, View convertView, ViewGroup parent)
    {
        mapPositionParentView.put(position, parent);
    }

    /**
     * 重写此方法，不要重写public View getView(int position, View convertView, ViewGroup
     * parent)方法
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected View onGetView(int position, View convertView, ViewGroup parent)
    {
        return convertView;
    }

    protected void afterOnGetView(final int position, final View convertView, ViewGroup parent)
    {
        putItemView(position, convertView);
    }

    private void putItemView(int position, View view)
    {
        mapViewPosition.put(view, position);
    }

    /**
     * 刷新position对应的itemView
     *
     * @param position
     */
    public void updateItem(int position)
    {
        View itemView = getItemView(position);
        if (itemView != null)
        {
            onUpdateView(position, itemView, getItemParent(position), getItem(position));
        }
    }

    /**
     * 刷新position对应的itemView，数据会根据提供的model来绑定，并替换原list中该位置的model
     *
     * @param position
     * @param model
     */
    public void updateItem(int position, T model)
    {
        if (isPositionLegal(position))
        {
            listModel.set(position, model);
            selectManager.synchronizedSelected(model);
            updateItem(position);
        }
    }

    /**
     * 刷新model对应的itemView
     *
     * @param model
     */
    public void updateItem(T model)
    {
        updateItem(indexOf(model));
    }

    /**
     * 获得该position对应的itemView
     *
     * @param position
     * @return
     */
    public View getItemView(int position)
    {
        View itemView = null;
        for (Entry<View, Integer> item : mapViewPosition.entrySet())
        {
            if (Integer.valueOf(position).equals(item.getValue()))
            {
                itemView = item.getKey();
                break;
            }
        }
        return itemView;
    }

    /**
     * 获得该position对应到parentView
     *
     * @param position
     * @return
     */
    public ViewGroup getItemParent(int position)
    {
        return mapPositionParentView.get(position);
    }

    /**
     * 若重写此方法，则应该把需要刷新的逻辑写在重写方法中，然后不调用super的方法，此方法会在调用updateItem方法刷新某一项时候触发
     *
     * @param position
     * @param convertView
     * @param parent
     * @param model
     */
    protected void onUpdateView(int position, View convertView, ViewGroup parent, T model)
    {
        getView(position, convertView, getItemParent(position));
    }

    /**
     * 移除该position对应的项
     *
     * @param position
     */
    public void removeItem(int position)
    {
        if (isPositionLegal(position))
        {
            listModel.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除该model对应的项
     *
     * @param t
     */
    public void removeItem(T t)
    {
        removeItem(indexOf(t));
    }

    public int indexOf(T t)
    {
        int index = -1;
        if (t != null)
        {
            index = listModel.indexOf(t);
        }
        return index;
    }

    public void notifyDataSetChanged(long delay)
    {
        if (delay <= 0)
        {
            notifyDataSetChanged();
        } else
        {
            SDHandlerManager.getMainHandler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    notifyDataSetChanged();
                }
            }, delay);
        }
    }

    // util method
    @SuppressWarnings("unchecked")
    public static <V extends View> V get(int id, View convertView)
    {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null)
        {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (V) childView;
    }

    @SuppressWarnings("unchecked")
    public static <V extends View> V find(int id, View convertView)
    {
        return (V) convertView.findViewById(id);
    }

    public interface ItemClickListener<T>
    {
        void onClick(int position, T item, View view);
    }

    public interface ItemLongClickListener<T>
    {
        void onClick(int position, T item, View view);
    }

}
