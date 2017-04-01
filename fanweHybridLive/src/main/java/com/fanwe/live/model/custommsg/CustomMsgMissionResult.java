package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant;

/**
 * Created by kevin.liu on 2017/3/18.
 */

public class CustomMsgMissionResult extends CustomMsg {
    private int missionScore;//任务积分值
    private String guestName;//    ：任务执行者昵称
    private int guestId;//：任务执行者id
    private int guestMalaId;//：任务执行者麻辣id
    private int medalNum;//    ：任务可获得勋章数
    private int status;// 完成状态，1成功，-1失败',

    public CustomMsgMissionResult() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_MISSION_RESULT);
    }

    public int getMissionScore() {
        return missionScore;
    }

    public void setMissionScore(int missionScore) {
        this.missionScore = missionScore;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getGuestMalaId() {
        return guestMalaId;
    }

    public void setGuestMalaId(int guestMalaId) {
        this.guestMalaId = guestMalaId;
    }

    public int getMedalNum() {
        return medalNum;
    }

    public void setMedalNum(int medalNum) {
        this.medalNum = medalNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
