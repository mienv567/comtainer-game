package com.qy.ysys.yishengyishi.model;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelFitInfo {
//    {
//        “code”: ”0000”,
//        ”message”: ”“,
//        ”returnObj”: null
//    }

    public ModelFitInfo() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(String returnObj) {
        this.returnObj = returnObj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String code ;
    private String message;
    private String returnObj;

}
