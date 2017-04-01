package com.fanwe.live.control;

import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;

/**
 * Created by tony.chen on 2016/12/18.
 */

public class QNRoomManager {
    private RTCMediaStreamingManager streamingManager;

    public QNRoomManager(RTCMediaStreamingManager streamingManager) {
        this.streamingManager = streamingManager;
    }

    /**
     * 获取当前房间连麦窗的个数
     *
     * @return
     */
    public int getVideoCount() {
        return streamingManager.getParticipantsCount();
    }


}
