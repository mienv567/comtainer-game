package com.fanwe.live.model.item;

import android.support.annotation.DrawableRes;

import com.fanwe.live.model.MultipleItem;

/**
 * Created by Yuan on 2017/4/5.
 * 邮箱：44004606@qq.com
 */

public class HeaderItem extends MultipleItem {

    private String title;
    private int resId;
    private boolean isMore;

    public HeaderItem(String title, @DrawableRes int resId, boolean isMore) {
        super(MultipleItem.HEAD, 2);
        this.title = title;
        this.resId = resId;
        this.isMore = isMore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
}
