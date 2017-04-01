package com.fanwe.library.listener;

import android.view.View;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface SDItemLongClickListener<T>
{
    boolean onLongClick(int position, T item, View view);
}
