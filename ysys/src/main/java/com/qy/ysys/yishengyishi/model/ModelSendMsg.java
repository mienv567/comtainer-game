package com.qy.ysys.yishengyishi.model;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelSendMsg {
    private String code;
    private String message;
    private String returnObj;

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

    public ModelSendMsg() {
    }

    public ModelSendMsg(String code, String message, String returnObj) {
        this.code = code;
        this.message = message;
        this.returnObj = returnObj;
    }
}
