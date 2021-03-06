package com.fanwe.live.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by cheng.yuan on 2017/4/5.
 */

public class MultipleItem implements MultiItemEntity {

    public static final int HEAD = 0;
    public static final int POPULAR_VIDEO = 1;
    public static final int BOX_BAR = 2;

    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
