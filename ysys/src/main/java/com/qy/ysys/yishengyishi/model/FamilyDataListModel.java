package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.model.item.FamilyDataItem;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/19 0019 21:47
 */
public class FamilyDataListModel extends BaseModel{
    private List<FamilyDataItem> returnObj;

    public List<FamilyDataItem> getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(List<FamilyDataItem> returnObj) {
        this.returnObj = returnObj;
    }
}
