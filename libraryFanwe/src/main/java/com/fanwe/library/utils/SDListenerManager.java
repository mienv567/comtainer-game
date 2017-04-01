package com.fanwe.library.utils;

import com.fanwe.library.listener.SDIterateListener;

import java.util.ArrayList;
import java.util.List;

public class SDListenerManager<T>
{
    private List<T> listListener = new ArrayList<T>();

    public void add(T listener)
    {
        synchronized (this)
        {
            if (listener != null)
            {
                if (!listListener.contains(listener))
                {
                    listListener.add(listener);
                }
            }
        }
    }

    public void remove(T listener)
    {
        synchronized (this)
        {
            if (listener != null)
            {
                listListener.remove(listener);
            }
        }
    }

    /**
     * 获得监听对象，调用此方法遍历的时候需要锁定当前对象
     *
     * @return
     */
    public List<T> getListeners()
    {
        return listListener;
    }

    public boolean foreach(SDIterateListener<T> listener)
    {
        synchronized (this)
        {
            return SDCollectionUtil.foreach(listListener, listener);
        }
    }

    public boolean iterate(SDIterateListener<T> listener)
    {
        synchronized (this)
        {
            return SDCollectionUtil.iterate(listListener, listener);
        }
    }

    public boolean foreachReverse(SDIterateListener<T> listener)
    {
        synchronized (this)
        {
            return SDCollectionUtil.foreachReverse(listListener, listener);
        }
    }

    public void clear()
    {
        synchronized (this)
        {
            listListener.clear();
        }
    }
}
