package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/11 0011 11:36
 */
public class CategoryDetailModel extends BaseActModel {
    private CategoryModel category_info;
    private List<LiveRoomModel> liveList;
    private int liveNum = 0;

    public int getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }

    public CategoryModel getCategory_info() {
        return category_info;
    }

    public void setCategory_info(CategoryModel category_info) {
        this.category_info = category_info;
    }

    public List<LiveRoomModel> getLiveList() {
        return liveList;
    }

    public void setLiveList(List<LiveRoomModel> liveList) {
        this.liveList = liveList;
    }
}
