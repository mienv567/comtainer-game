package com.fanwe.live.model;

import com.fanwe.live.activity.room.LiveActivity;

/**
 * 互动直播的创建数据
 */
public class CreateLiveData {

    private int roomId; // 直播间房间号
    private int isClosedBack; //1：主播界面被强制关闭后回来
    private int videoType; //0-互动直播，1-直播
    private int isHorizontal;  //是否横屏 1：是  0：否

    public int getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(int isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public App_get_videoActModel getVideoActModel() {
        return videoActModel;
    }

    public void setVideoActModel(App_get_videoActModel videoActModel) {
        this.videoActModel = videoActModel;
    }

    private App_get_videoActModel videoActModel; // 获取房间信息的bean


    private String play_url;

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }

    private String push_url; // 七牛新增,开房后需要告诉创建者推流地址


    private int rtcRole; // 进入直播间的角色

    public int getRtcrole() {
//        return LiveActivity.RTC_ROLE_ANCHOR;
        return rtcRole;
    }

    public void setRtcRole(int rtcrole) {
        this.rtcRole = rtcrole;
    }

    // 开房的时候直接获得群组id
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    private String group_id;


    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public int getIsClosedBack() {
        return isClosedBack;
    }

    public void setIsClosedBack(int isClosedBack) {
        this.isClosedBack = isClosedBack;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
