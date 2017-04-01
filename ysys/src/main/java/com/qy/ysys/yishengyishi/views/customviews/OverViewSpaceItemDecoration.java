package com.qy.ysys.yishengyishi.views.customviews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class OverViewSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public OverViewSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
        outRect.right = space;
        outRect.right = space / 2;
        outRect.left = space / 2;
        if (parent.getChildLayoutPosition(view) > 3) {
            outRect.top = 0;
        }
    }
}
