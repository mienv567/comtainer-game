package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.model.item.MemoirInfoItem;

/**
 * 回忆录信息
 * 作者：tracy.lee on 2017/1/22 0022 10:49
 */
public class MemoirInfoModel extends BaseModel {
    private MemoirInfoItem returnObj;

    public MemoirInfoItem getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(MemoirInfoItem returnObj) {
        this.returnObj = returnObj;
    }
}