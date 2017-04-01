package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 *  房间热度
 */
public class AppRoomHotScoreActModel extends BaseActModel{
    private AppRoomHotScoreDataActModel data;

    public AppRoomHotScoreDataActModel getData() {
        return data;
    }

    public void setData(AppRoomHotScoreDataActModel data) {
        this.data = data;
    }
}
