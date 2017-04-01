package com.fanwe.library.listener;

import android.view.View;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface SDItemClickListener<T>
{
    boolean onClick(int position, T item, View view);
}
