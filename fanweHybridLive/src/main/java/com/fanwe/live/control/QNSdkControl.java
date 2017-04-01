package com.fanwe.live.control;

import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;

/**
 * Created by tony.chen on 2016/12/18.
 */

public class QNSdkControl {
    private RTCMediaStreamingManager streamingManager;


    private QNSdkControl() {

    }

    private static QNSdkControl mInstance = new QNSdkControl();

    public static QNSdkControl getInstance() {
        return mInstance;
    }

    protected void init(RTCMediaStreamingManager streamingManager){
       this.streamingManager = streamingManager;
    }


    public static QNSdkControl getmInstance() {
        return mInstance;
    }

    private TIMControl timControl = TIMControl.getInstance();


    public TIMControl getTimControl() {
        return timControl;
    }

    private com.fanwe.live.control.QNContextControl QNContextControl = new QNContextControl();

    public com.fanwe.live.control.QNContextControl getQNContextControl() {
        return QNContextControl;
    }

    private QNRoomManager roomManager = new QNRoomManager(streamingManager);

    public QNRoomManager getRoomManager() {
        return roomManager;
    }


}
