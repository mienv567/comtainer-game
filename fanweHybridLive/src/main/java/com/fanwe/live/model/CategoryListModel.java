package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * 主界面视频分类
 * 作者：tracy.lee on 2017/1/11 0011 11:28
 */
public class CategoryListModel extends BaseActModel{
    private List<CategoryModel> category_list;
    private HeadCategoryModel head_category;

    public List<CategoryModel> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(List<CategoryModel> category_list) {
        this.category_list = category_list;
    }

    public HeadCategoryModel getHead_category() {
        return head_category;
    }

    public void setHead_category(HeadCategoryModel head_category) {
        this.head_category = head_category;
    }
}
