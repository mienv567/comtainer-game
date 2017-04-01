package com.fanwe.live.model;


public class AppRoomHotScoreDataActModel{
    private long total_score; //房间总分
    private long prev; //上级的值
    private long next; //下级的值
    private String current_name;//热度名称
    private AppRoomHotScoreProgressActModel progress_bar;

    public long getTotal_score() {
        return total_score;
    }

    public void setTotal_score(long total_score) {
        this.total_score = total_score;
    }

    public AppRoomHotScoreProgressActModel getProgress_bar() {
        return progress_bar;
    }

    public void setProgress_bar(AppRoomHotScoreProgressActModel progress_bar) {
        this.progress_bar = progress_bar;
    }

    public String getCurrent_name() {
        return current_name;
    }

    public void setCurrent_name(String current_name) {
        this.current_name = current_name;
    }

    public long getNext() {
        return next;
    }

    public void setNext(long next) {
        this.next = next;
    }

    public long getPrev() {
        return prev;
    }

    public void setPrev(long prev) {
        this.prev = prev;
    }
}
