package com.fanwe.library.listener;

import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface SDIterateListener<T>
{
    /**
     * 返回true，结束遍历
     *
     * @param i
     * @param item
     * @param it
     * @return
     */
    boolean next(int i, T item, Iterator<T> it);
}
