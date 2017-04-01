package com.qy.ysys.yishengyishi.model.item;

/**
 * 作者：tracy.lee on 2017/1/22 0022 18:58
 */
public class UserInfo {
    public static final int IS_INVITED = 1;
    public static final int IS_NOT_INVITED = 0;
    private int id;
    private String phone;
    private String name;
    private int gender;// 0 - 男 1 - 女
    private String createTime;
    private int isInvitate;//0-表示没有填写过邀请码 1-表示填写过邀请码
    private String invitationCode;//填写的邀请码
    private String invitationTime;//填写邀请码时间
    private int fromUserId;//邀请码对应用户id
    public UserInfo(){

    }
    public UserInfo(String name, int gender) {
        this.name = name;
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getInvitationTime() {
        return invitationTime;
    }

    public void setInvitationTime(String invitationTime) {
        this.invitationTime = invitationTime;
    }

    public int getIsInvitate() {
        return isInvitate;
    }

    public void setIsInvitate(int isInvitate) {
        this.isInvitate = isInvitate;
    }
}
