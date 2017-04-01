package com.fanwe.library.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 选择管理器
 *
 * @param <T>
 * @author Administrator
 */
public class SDSelectManager<T>
{
    private List<T> listItem = new ArrayList<T>();
    private int currentIndex = -1;
    private int lastIndex = -1;
    private Map<Integer, T> mapSelectedIndexItem = new LinkedHashMap<Integer, T>();
    private Mode mode = Mode.SINGLE_MUST_ONE_SELECTED;
    private boolean enable = true;

    private SDSelectManagerListener<T> mListener;
    private ReSelectListener<T> reSelectListener;

    public void setReSelectListener(ReSelectListener<T> reSelectListener)
    {
        this.reSelectListener = reSelectListener;
    }

    /**
     * 设置是否开启管理功能
     *
     * @param enable
     */
    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    /**
     * 是否开启了管理功能，默认true，开启
     *
     * @return
     */
    public boolean isEnable()
    {
        return enable;
    }

    public int getLastIndex()
    {
        return lastIndex;
    }

    public Mode getMode()
    {
        return mode;
    }

    /**
     * 设置选中模式
     *
     * @param mode
     */
    public void setMode(Mode mode)
    {
        if (mode != null)
        {
            clearSelected();
            this.mode = mode;
        }
    }

    /**
     * 是否单选模式
     *
     * @return
     */
    public boolean isSingleMode()
    {
        boolean single = false;
        switch (mode)
        {
            case SINGLE:
            case SINGLE_MUST_ONE_SELECTED:
                single = true;
                break;
            case MULTI:
            case MULTI_MUST_ONE_SELECTED:
                single = false;
                break;
            default:
                break;
        }
        return single;
    }

    /**
     * 项是否被选中
     *
     * @param item
     * @return
     */
    public boolean isSelected(T item)
    {
        int index = indexOfItem(item);
        return isSelected(index);
    }

    /**
     * 项是否被选中
     *
     * @param index
     * @return
     */
    public boolean isSelected(int index)
    {
        boolean selected = false;
        if (index >= 0)
        {
            switch (mode)
            {
                case SINGLE:
                case SINGLE_MUST_ONE_SELECTED:
                    if (index == currentIndex)
                    {
                        selected = true;
                    }
                    break;
                case MULTI:
                case MULTI_MUST_ONE_SELECTED:
                    if (mapSelectedIndexItem.containsKey(index))
                    {
                        selected = true;
                    }
                    break;

                default:
                    break;
            }
        }
        return selected;
    }

    public void synchronizedSelected()
    {
        synchronizedSelected(listItem);
    }

    public void synchronizedSelected(List<T> items)
    {
        if (items != null)
        {
            for (T item : items)
            {
                synchronizedSelected(item);
            }
        }
    }

    public void synchronizedSelected(T item)
    {
        synchronizedSelected(indexOfItem(item));
    }

    public void synchronizedSelected(int index)
    {
        T model = getItem(index);
        if (model instanceof SDSelectManager.SDSelectable)
        {
            SDSelectManager.SDSelectable sModel = (SDSelectManager.SDSelectable) model;
            setSelected(index, sModel.isSelected());
        }
    }

    /**
     * 获得选中项的位置(单选模式)
     *
     * @return
     */
    public int getSelectedIndex()
    {
        return currentIndex;
    }

    /**
     * 获得选中项(单选模式)
     *
     * @return
     */
    public T getSelectedItem()
    {
        return getItem(currentIndex);
    }

    /**
     * 获得选中项的位置(多选模式)
     *
     * @return
     */
    public List<Integer> getSelectedIndexs()
    {
        List<Integer> listIndex = new ArrayList<Integer>();
        if (!mapSelectedIndexItem.isEmpty())
        {
            for (Entry<Integer, T> item : mapSelectedIndexItem.entrySet())
            {
                listIndex.add(item.getKey());
            }
        }
        return listIndex;
    }

    /**
     * 获得选中项(多选模式)
     *
     * @return
     */
    public List<T> getSelectedItems()
    {
        List<T> listItem = new ArrayList<T>();
        if (!mapSelectedIndexItem.isEmpty())
        {
            for (Entry<Integer, T> item : mapSelectedIndexItem.entrySet())
            {
                listItem.add(item.getValue());
            }
        }
        return listItem;
    }

    /**
     * 设置监听对象
     *
     * @param listener
     */
    public void setListener(SDSelectManagerListener<T> listener)
    {
        this.mListener = listener;
    }

    public SDSelectManagerListener<T> getListener()
    {
        return mListener;
    }

    public void setItems(T[] items)
    {
        List<T> listItem = new ArrayList<T>();
        if (items != null && items.length > 0)
        {
            for (int i = 0; i < items.length; i++)
            {
                listItem.add(items[i]);
            }
        }
        setItems(listItem);
    }

    public void setItems(List<T> items)
    {
        if (items != null)
        {
            this.listItem = items;
        } else
        {
            listItem.clear();
        }
        resetIndex();
        initItems(listItem);
    }

    public void appendItems(List<T> items, boolean addAll)
    {
        if (items != null && !items.isEmpty())
        {
            if (!listItem.isEmpty())
            {
                if (addAll)
                {
                    listItem.addAll(items);
                }
                initItems(items);
            } else
            {
                setItems(items);
            }
        }
    }

    public void appendItem(T item, boolean add)
    {
        if (!listItem.isEmpty() && item != null)
        {
            if (add)
            {
                listItem.add(item);
            }
            initItem(indexOfItem(item), item);
        }
    }

    private void initItems(List<T> items)
    {
        if (items != null && !items.isEmpty())
        {
            T item = null;
            int index = 0;
            for (int i = 0; i < items.size(); i++)
            {
                item = items.get(i);
                index = indexOfItem(item);
                initItem(index, item);
            }
        }
    }

    /**
     * 设置数据后会遍历调用此方法对每个数据进行初始化
     *
     * @param index
     * @param item
     */
    protected void initItem(int index, T item)
    {

    }

    private void resetIndex()
    {
        switch (mode)
        {
            case SINGLE:
            case SINGLE_MUST_ONE_SELECTED:
                currentIndex = -1;
                break;
            case MULTI:
            case MULTI_MUST_ONE_SELECTED:
                mapSelectedIndexItem.clear();
                break;

            default:
                break;
        }

    }

    private boolean isIndexLegal(int index)
    {
        if (index >= 0 && index < listItem.size())
        {
            return true;
        }
        return false;
    }

    /**
     * 设置最后一次选中的位置选中(单选模式)
     */
    public void selectLastIndex()
    {
        setSelected(lastIndex, true);
    }

    /**
     * 选中全部(多选模式)
     */
    public void selectAll()
    {
        for (int i = 0; i < listItem.size(); i++)
        {
            setSelected(i, true);
        }
    }

    /**
     * 模拟点击该项
     *
     * @param index
     */
    public void performClick(int index)
    {
        if (isIndexLegal(index))
        {
            boolean selected = isSelected(index);
            setSelected(index, !selected);
        }
    }

    /**
     * 模拟点击该项
     *
     * @param item
     */
    public void performClick(T item)
    {
        performClick(indexOfItem(item));
    }

    /**
     * 设置该项的选中状态
     *
     * @param item
     * @param selected
     */
    public void setSelected(T item, boolean selected)
    {
        int index = indexOfItem(item);
        setSelected(index, selected);
    }

    /**
     * 设置该位置的选中状态
     *
     * @param index
     * @param selected
     */
    public void setSelected(int index, boolean selected)
    {
        if (!enable)
        {
            return;
        }

        if (!isIndexLegal(index))
        {
            return;
        }

        switch (mode)
        {
            case SINGLE_MUST_ONE_SELECTED:
                if (selected)
                {
                    if (currentIndex == index)
                    {

                    } else
                    {
                        int tempCurrentIndex = currentIndex;
                        currentIndex = index;

                        normalItem(tempCurrentIndex);
                        selectItem(currentIndex);

                        lastIndex = currentIndex;
                    }
                } else
                {
                    if (currentIndex == index)
                    {
                        if (reSelectListener != null)
                        {
                            reSelectListener.onSelected(currentIndex, getItem(currentIndex));
                        }
                    } else
                    {

                    }
                }
                break;
            case SINGLE:
                if (selected)
                {
                    if (currentIndex == index)
                    {

                    } else
                    {
                        int tempCurrentIndex = currentIndex;
                        currentIndex = index;

                        normalItem(tempCurrentIndex);
                        selectItem(currentIndex);

                        lastIndex = currentIndex;
                    }
                } else
                {
                    if (currentIndex == index)
                    {
                        int tempCurrentIndex = currentIndex;
                        currentIndex = -1;

                        normalItem(tempCurrentIndex);
                    } else
                    {

                    }
                }
                break;
            case MULTI_MUST_ONE_SELECTED:
                if (selected)
                {
                    if (mapSelectedIndexItem.containsKey(index))
                    {

                    } else
                    {
                        mapSelectedIndexItem.put(index, getItem(index));
                        selectItem(index);
                    }
                } else
                {
                    if (mapSelectedIndexItem.containsKey(index))
                    {
                        if (mapSelectedIndexItem.size() == 1)
                        {

                        } else
                        {
                            mapSelectedIndexItem.remove(index);
                            normalItem(index);
                        }
                    } else
                    {

                    }
                }
                break;
            case MULTI:
                if (selected)
                {
                    if (mapSelectedIndexItem.containsKey(index))
                    {

                    } else
                    {
                        mapSelectedIndexItem.put(index, getItem(index));
                        selectItem(index);
                    }
                } else
                {
                    if (mapSelectedIndexItem.containsKey(index))
                    {
                        mapSelectedIndexItem.remove(index);
                        normalItem(index);
                    } else
                    {

                    }
                }
                break;

            default:
                break;
        }
    }

    private void normalItem(int index)
    {
        if (isIndexLegal(index))
        {
            notifyNormal(index, getItem(index));
        }
    }

    private void selectItem(int index)
    {
        if (isIndexLegal(index))
        {
            notifySelected(index, getItem(index));
        }
    }

    protected void notifyNormal(int index, T item)
    {
        if (mListener != null)
        {
            mListener.onNormal(index, item);
        }
    }

    protected void notifySelected(int index, T item)
    {
        if (mListener != null)
        {
            mListener.onSelected(index, item);
        }
    }

    /**
     * 获得该位置的item
     *
     * @param index
     * @return
     */
    public T getItem(int index)
    {
        T item = null;
        if (isIndexLegal(index))
        {
            item = listItem.get(index);
        }
        return item;
    }

    /**
     * 返回item的位置
     *
     * @param item
     * @return
     */
    public int indexOfItem(T item)
    {
        int index = -1;
        if (item != null)
        {
            index = listItem.indexOf(item);
        }
        return index;
    }

    /**
     * 清除选中
     */
    public void clearSelected()
    {
        switch (mode)
        {
            case SINGLE:
            case SINGLE_MUST_ONE_SELECTED:
                if (currentIndex >= 0)
                {
                    int tempCurrentIndex = currentIndex;
                    resetIndex();
                    normalItem(tempCurrentIndex);
                }
                break;
            case MULTI:
            case MULTI_MUST_ONE_SELECTED:
                List<Integer> listIndexs = getSelectedIndexs();
                if (listIndexs != null)
                {
                    for (Integer index : listIndexs)
                    {
                        mapSelectedIndexItem.remove(index);
                        normalItem(index);
                    }
                    resetIndex();
                }
                break;
            default:
                break;
        }
    }

    public interface SDSelectManagerListener<T>
    {
        void onNormal(int index, T item);

        void onSelected(int index, T item);
    }

    public interface ReSelectListener<T>
    {
        void onSelected(int index, T item);
    }

    public enum Mode
    {
        /**
         * 单选，必须选中一项
         */
        SINGLE_MUST_ONE_SELECTED,
        /**
         * 单选，可以一项都没选中
         */
        SINGLE,
        /**
         * 多选，必须选中一项
         */
        MULTI_MUST_ONE_SELECTED,
        /**
         * 多选，可以一项都没选中
         */
        MULTI;
    }

    public interface SDSelectable
    {
        public boolean isSelected();

        public void setSelected(boolean selected);
    }

    public interface SelecteStateListener<T>
    {
        public void onNormal(int position, T item);

        public void onSelected(int position, T item);
    }
}
