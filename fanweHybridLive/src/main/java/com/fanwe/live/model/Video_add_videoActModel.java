package com.fanwe.live.model;

public class Video_add_videoActModel extends App_get_videoActModel {
    private static final long serialVersionUID = 1L;

    private int roomId;
    private String private_share;
    private String share_type; // 分享类型
    private RoomShareModel share;
    private int video_type; // 0-腾讯云互动直播;1-腾讯云直播
    private String play_url;


    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }


    public int getVideo_type() {
        return video_type;
    }

    public void setVideo_type(int video_type) {
        this.video_type = video_type;
    }

    public String getPrivate_share() {
        return private_share;
    }

    public void setPrivate_share(String private_share) {
        this.private_share = private_share;
    }

    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public RoomShareModel getShare() {
        return share;
    }

    public void setShare(RoomShareModel share) {
        this.share = share;
    }

}
