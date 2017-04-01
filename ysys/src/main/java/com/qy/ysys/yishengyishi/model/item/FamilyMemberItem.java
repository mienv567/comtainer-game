package com.qy.ysys.yishengyishi.model.item;

/**
 * 作者：tracy.lee on 2017/1/19 0019 21:43
 */
public class FamilyMemberItem {
    private int userId;
    private String name;
    private String headImage;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
