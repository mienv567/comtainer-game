package com.qy.ysys.yishengyishi.model;

import java.util.List;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelShowFamilyTree {
//    {
//        “code”: ”0000”,
//        ”message”: ”“,
//        ”returnObj”: [
//
//        ]

    public ModelShowFamilyTree() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    //    }

    private String code;
    private String message;

    public List<ModelTreeNodes> getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(List<ModelTreeNodes> returnObj) {
        this.returnObj = returnObj;
    }

    private List<ModelTreeNodes> returnObj;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
