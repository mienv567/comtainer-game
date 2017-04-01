package com.fanwe.live.event;

/**
 * 开播的时候 选择主题
 * 作者：tracy.lee on 2017/1/14 0014 15:28
 */
public class ECreateCategoryChoose {
    private String mCategoryId;
    private String mCategoryName;
    public ECreateCategoryChoose(String categoryId,String categoryName){
        mCategoryId = categoryId;
        mCategoryName = categoryName;
    }

    public String getmCategoryId() {
        return mCategoryId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }
}
