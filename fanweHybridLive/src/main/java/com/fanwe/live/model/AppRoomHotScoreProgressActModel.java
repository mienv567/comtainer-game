package com.fanwe.live.model;


public class AppRoomHotScoreProgressActModel {
    private int person_precent;//人气进度条
    private int mala_precent;//麻辣度进度条
    private int share_precent;//被分享数进度条
    private int light_precent;//点亮进度条
    private int level_precent;//等级进度条
    private int hot_gift;//热度值
    private int hot_gift_max;//热度最大值
    public int getLevel_precent() {
        return level_precent;
    }

    public void setLevel_precent(int level_precent) {
        this.level_precent = level_precent;
    }

    public int getLight_precent() {
        return light_precent;
    }

    public void setLight_precent(int light_precent) {
        this.light_precent = light_precent;
    }

    public int getMala_precent() {
        return mala_precent;
    }

    public void setMala_precent(int mala_precent) {
        this.mala_precent = mala_precent;
    }

    public int getPerson_precent() {
        return person_precent;
    }

    public void setPerson_precent(int person_precent) {
        this.person_precent = person_precent;
    }

    public int getShare_precent() {
        return share_precent;
    }

    public void setShare_precent(int share_precent) {
        this.share_precent = share_precent;
    }

    public int getHot_gift() {
        return hot_gift;
    }

    public void setHot_gift(int hot_gift) {
        this.hot_gift = hot_gift;
    }

    public int getHot_gift_max() {
        return hot_gift_max;
    }

    public void setHot_gift_max(int hot_gift_max) {
        this.hot_gift_max = hot_gift_max;
    }
}
