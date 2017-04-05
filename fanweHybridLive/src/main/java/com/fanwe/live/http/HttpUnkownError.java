package com.fanwe.live.http;

/**
 * Created by yong.zhang on 2017/4/1 0001.
 */
public class HttpUnkownError extends Exception {

    private int code;

    public HttpUnkownError(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
