package com.fanwe.live.event;

/**
 * 狼人杀频道切换
 */
public class ELRSChannelChange {

    private int mChannelIndex;//0-游戏公频道 1-狼人频道 2-游戏进程
    private String mCurrentGroupId; //当前所在群组  主要用来控制发送消息
    private boolean mIsLiveGroup = true; //是否为直播群组

    public ELRSChannelChange(String groupId,boolean isLiveGroup){
        mCurrentGroupId = groupId;
        mIsLiveGroup = isLiveGroup;
    }

    public String getCurrentGroupId(){
        return mCurrentGroupId;
    }

    public boolean  getIsLiveGroup(){
        return mIsLiveGroup;
    }

    public void setChannelIndex(int index){
        mChannelIndex = index;
    }

    public int getChannelIndex(){
        return mChannelIndex;
    }

}
