package com.fanwe.live.activity.info;

import com.fanwe.live.model.App_get_videoActModel;
import com.umeng.socialize.UMShareListener;

import java.util.List;

public interface LiveInfo
{
    /**
     * 获得直播间聊天组id
     */
    String getGroupId();

    /**
     * 获得直播间主播id
     */
    String getCreaterId();

    /**
     * 获得直播间id
     */
    int getRoomId();

    App_get_videoActModel getRoomInfo();

    /**
     * 是否私密直播
     *
     * @return
     */
    boolean isPrivate();

    /**
     * 是否是主播
     *
     * @return
     */
    boolean isCreater();

    /**
     * 获得录制的视频
     *
     * @return
     */
    List<String> getListRecord();

    /**
     * 打开分享
     *
     * @param listener
     */
    void openShare(UMShareListener listener);

    /**
     * 打开直播间输入框
     *
     * @param content
     */
    void openSendMsg(String content);

    String getCurrentGuestId();
}
