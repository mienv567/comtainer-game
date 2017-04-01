package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.model.item.MemoirListItem;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/22 0022 10:37
 */
public class MemoirListModel extends  BaseModel {
    private List<MemoirListItem> returnObj;

    public List<MemoirListItem> getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(List<MemoirListItem> returnObj) {
        this.returnObj = returnObj;
    }
}
