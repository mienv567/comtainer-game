package com.fanwe.live.model;

/**
 * Created by cheng.yuan on 2017/3/17.
 */

public class GoodsHistoryModel {
    /**
     * id : 24
     * historyId : 24
     * userId : 68
     * userMalaId : 63
     * userName :
     * goodsId : 6
     * status : 1
     * buyTime : 2017-03-16 19:01:32
     * endTime : null
     * goods : {"goodsId":6,"title":"stevesb5","desc":"stevesb5stevesb5stevesb5stevesb5","icon":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","iconSmall":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","charmCount":500,"likesCount":600}
     */

    private int id;
    private int historyId;
    private int userId;
    private int userMalaId;
    private String userName;
    private int goodsId;
    private int status;
    private String buyTime;
    private Object endTime;
    private GoodsDetailModel goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserMalaId() {
        return userMalaId;
    }

    public void setUserMalaId(int userMalaId) {
        this.userMalaId = userMalaId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public GoodsDetailModel getGoods() {
        return goods;
    }

    public void setGoods(GoodsDetailModel goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "GoodsHistoryModel{" +
                "id=" + id +
                ", historyId=" + historyId +
                ", userId=" + userId +
                ", userMalaId=" + userMalaId +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", status=" + status +
                ", buyTime='" + buyTime + '\'' +
                ", endTime=" + endTime +
                ", goods=" + goods +
                '}';
    }
}
