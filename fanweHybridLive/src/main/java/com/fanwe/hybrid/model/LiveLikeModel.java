package com.fanwe.hybrid.model;

import android.view.View;

import com.fanwe.library.common.SDSelectManager;

import java.lang.ref.WeakReference;

/**
 * Created by cheng.yuan on 2017/3/28.
 */

public class LiveLikeModel implements SDSelectManager.SDSelectable {


    /**
     * createTime : 2017-03-28 16:08:09
     * icon : likeProp/pkq.jpg
     * sort : 1
     * name : 皮卡丘
     * isEffect : 1
     * likeId : 1
     */

    private String createTime;
    private String icon;
    private int sort;
    private String name;
    private int isEffect;
    private int likeId;
    private WeakReference<View> hodler;

    public WeakReference<View> getHodler() {
        return hodler;
    }

    public void setHodler(WeakReference<View> hodler) {
        this.hodler = hodler;
    }

    //add
    private boolean selected;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(int isEffect) {
        this.isEffect = isEffect;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }
}
