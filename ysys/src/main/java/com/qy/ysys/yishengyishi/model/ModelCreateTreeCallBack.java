package com.qy.ysys.yishengyishi.model;

import java.util.List;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelCreateTreeCallBack {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ModelTreeNodes> getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(List<ModelTreeNodes> returnObj) {
        this.returnObj = returnObj;
    }

    private String message;

    public ModelCreateTreeCallBack() {
    }

    private List<ModelTreeNodes> returnObj;
}
