package com.fanwe.hybrid.model;


public class UserSignInItem {
    public final static int NOT_SIGN = 0;
    public final static int HAS_SIGN = 1;
    public final static int IS_TODAY = 1;
    private String date; //日期
    private int is_sign; //0:未签到 ； 1：已签到 2 什么都不显示
    private int current; // 有这个键代表是今天

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }
}
