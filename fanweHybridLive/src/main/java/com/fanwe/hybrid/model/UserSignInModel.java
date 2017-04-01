package com.fanwe.hybrid.model;

import java.util.List;

/**
 * 用户签到数据
 */
public class UserSignInModel extends BaseActModel{
    private List<UserSignInItem> list;
    private int sign_count;//当前周期最高连续签到数量
    private int sign_total_count;//当前周期总共签到数量
    private String sign_rule;//签到规则
    private String sign_word;//签到规则
    public List<UserSignInItem> getList() {
        return list;
    }

    public void setList(List<UserSignInItem> list) {
        this.list = list;
    }

    public String getSign_rule() {
        return sign_rule;
    }

    public void setSign_rule(String sign_rule) {
        this.sign_rule = sign_rule;
    }

    public int getSign_count() {
        return sign_count;
    }

    public void setSign_count(int sign_count) {
        this.sign_count = sign_count;
    }

    public int getSign_total_count() {
        return sign_total_count;
    }

    public void setSign_total_count(int sign_total_count) {
        this.sign_total_count = sign_total_count;
    }

    public String getSign_word() {
        return sign_word;
    }

    public void setSign_word(String sign_word) {
        this.sign_word = sign_word;
    }
}
