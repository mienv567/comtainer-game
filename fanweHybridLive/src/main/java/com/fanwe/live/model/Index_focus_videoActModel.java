package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.ArrayList;
import java.util.List;

public class Index_focus_videoActModel extends BaseActModel
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<LiveRoomModel> list = new ArrayList<>();
    private List<LivePlaybackModel> playback = new ArrayList<>();
    private List<UserModel> recommend_follow_list = new ArrayList<>();
    public List<LiveRoomModel> getList()
    {
        return list;
    }

    public void setList(List<LiveRoomModel> list)
    {
        this.list = list;
    }

    public List<LivePlaybackModel> getPlayback()
    {
        return playback;
    }

    public void setPlayback(List<LivePlaybackModel> playback)
    {
        this.playback = playback;
    }

    public List<UserModel> getRecommend_follow_list() {
        return recommend_follow_list;
    }

    public void setRecommend_follow_list(List<UserModel> recommend_follow_list) {
        this.recommend_follow_list = recommend_follow_list;
    }
}
