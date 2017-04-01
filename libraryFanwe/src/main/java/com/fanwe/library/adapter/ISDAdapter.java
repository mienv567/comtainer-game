package com.fanwe.library.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by L on 2016/8/28.
 */
public interface ISDAdapter<T>
{
    Activity getActivity();

    View inflate(int resource, ViewGroup root);

    View inflate(int resource, ViewGroup root, boolean attachToRoot);

    boolean isPositionLegal(int position);

    T getItemData(int position);

    int getItemCount();

    int indexOf(T model);

    void updateData(List<T> list);

    void setData(List<T> list);

    List<T> getData();

    // append
    void appendData(T model);

    void appendData(List<T> list);

    // remove
    void removeData(T model);

    void removeData(int position);

    // insert
    void insertData(int position, T model);

    void insertData(int position, List<T> list);

    // update
    void updateData(int position, T model);

    void updateData(int position);

    void clearData();

}
