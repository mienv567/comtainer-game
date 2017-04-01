package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.model.item.MyInviteItem;

/**
 * 作者：tracy.lee on 2017/1/18 0018 21:24
 */
public class GetMyInviteCodeModel extends  BaseModel {
    private MyInviteItem returnObj;

    public MyInviteItem getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(MyInviteItem returnObj) {
        this.returnObj = returnObj;
    }
}
