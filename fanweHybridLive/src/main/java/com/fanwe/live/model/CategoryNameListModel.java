package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/11 0011 11:27
 */
public class CategoryNameListModel extends BaseActModel{
    private List<CategoryNameModel> category_name_list;

    public List<CategoryNameModel> getCategory_name_list() {
        return category_name_list;
    }

    public void setCategory_name_list(List<CategoryNameModel> category_name_list) {
        this.category_name_list = category_name_list;
    }
}
