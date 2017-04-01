package com.fanwe.hybrid.model;


public class UserSignInReward {
    private String desc; //奖励说明
    private int num; // 奖励数量
    private String url; // 要展示图片的url

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
