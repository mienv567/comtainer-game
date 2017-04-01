package com.fanwe.live.model;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PlayBackData
{

    private int roomId;
    private String reviewId;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    private String playbackUrl;
    public int getRoomId()
    {
        return roomId;
    }

    public PlayBackData setRoomId(int roomId)
    {
        this.roomId = roomId;
        return this;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }
}
