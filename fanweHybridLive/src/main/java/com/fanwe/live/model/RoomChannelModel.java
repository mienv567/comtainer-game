package com.fanwe.live.model;

import java.io.Serializable;

/**
 * description:活动房间频道实体
 *
 * @author: LZG
 */
public class RoomChannelModel implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String rtmp_play_url;
    private int is_default;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRtmp_play_url() {
        return rtmp_play_url;
    }

    public void setRtmp_play_url(String rtmp_play_url) {
        this.rtmp_play_url = rtmp_play_url;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
