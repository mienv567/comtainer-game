package com.fanwe.library.utils;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.fanwe.library.listener.SDSizeListener;

public class SDListViewPositionHandler
{

    private AbsListView listView;
    private SDViewSizeListener viewSizeHandler = new SDViewSizeListener();

    public void handle(AbsListView listView)
    {
        this.listView = listView;
        viewSizeHandler.listen(listView, viewSizeListener);
    }

    private SDSizeListener<View> viewSizeListener = new SDSizeListener<View>()
    {
        @Override
        public void onWidthChanged(int newWidth, int oldWidth, int differ, View view)
        {

        }

        @Override
        public void onHeightChanged(int newHeight, int oldHeight, int differ, View view)
        {
            if (oldHeight <= 0)
            {
                return;
            }
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
            {
                return;
            }
            int totalCount = listAdapter.getCount();
            if (totalCount <= 0)
            {
                return;
            }

            if (differ < 0)
            {
                // item需要向上滚动
                scroll(differ, newHeight);
            } else
            {

            }
        }
    };

    private void scroll(int differ, int newHeight)
    {
        int differAbs = Math.abs(differ);
        if (differAbs > newHeight)
        {
            // 需要滚动多次
            int time = differAbs / newHeight;
            for (int i = 0; i < time; i++)
            {
                SDViewUtil.scrollListBy(differ, listView);
            }

            int left = differAbs % newHeight;
            if (left > 0)
            {
                if (differ < 0)
                {
                    left = -left;
                }
                SDViewUtil.scrollListBy(left, listView);
            }
        } else
        {
            SDViewUtil.scrollListBy(differ, listView);
        }
    }

}
