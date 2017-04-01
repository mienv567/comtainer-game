package com.fanwe.live.model;

/**
 * Created by cheng.yuan on 2017/3/16.
 */

public class GoodsDetailModel extends BaseGoodsModel {

    /**
     * id : 16
     * title : stevesb5
     * description : stevesb5stevesb5stevesb5stevesb5
     * icon : http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG
     * iconSmall : http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG
     * charmCount : 500
     * likesCount : 600
     */
    private int goodsId;
    private String title;
    private String desc;
    private String icon;
    private String iconSmall;
    private int charmCount;
    private int likesCount;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconSmall() {
        return iconSmall;
    }

    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
    }

    public int getCharmCount() {
        return charmCount;
    }

    public void setCharmCount(int charmCount) {
        this.charmCount = charmCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return "GoodsDetailModel{" +
                "goodsId=" + goodsId +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                ", iconSmall='" + iconSmall + '\'' +
                ", charmCount=" + charmCount +
                ", likesCount=" + likesCount +
                '}';
    }
}
