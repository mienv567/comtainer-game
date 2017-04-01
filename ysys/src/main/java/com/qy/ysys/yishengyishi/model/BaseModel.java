package com.qy.ysys.yishengyishi.model;

import com.qy.ysys.yishengyishi.ResponseCode;

/**
 * 作者：tracy.lee on 2017/1/18 0018 19:40
 */
public class BaseModel {
    private String code;
    private String message;

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


    public boolean isNotLogin(){
        return code.equals(ResponseCode.USER_NOT_LOGIN_ERROR);
    }

    public boolean isSuccess(){
        return code.equals(ResponseCode.SUCCESS);
    }
}
