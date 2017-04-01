package com.qy.ysys.yishengyishi.model;

import java.util.List;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelAddNodeCallBack {
    private String code;
    private String message;

    public ModelAddNodeCallBack() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ModelTreeNode> getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(List<ModelTreeNode> returnObj) {
        this.returnObj = returnObj;
    }

    private List<ModelTreeNode> returnObj;

}
