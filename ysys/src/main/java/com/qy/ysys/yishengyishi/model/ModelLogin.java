package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.model.item.UserInfo;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelLogin extends BaseModel{

    private UserInfo returnObj;

    public UserInfo getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(UserInfo returnObj) {
        this.returnObj = returnObj;
    }
}
