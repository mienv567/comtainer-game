package com.fanwe.live.model.custommsg;

import com.fanwe.live.model.UserModel;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface ILiveGiftMsg
{
    UserModel getMsgSender();

    void setShowNum(int showNum);

    int getShowNum();

    boolean isTaked();

    void setTaked(boolean taked);

    /**
     * 是否可以被播放
     *
     * @return
     */
    boolean canPlay();

    /**
     * 是否需要叠加
     *
     * @return
     */
    boolean needPlus();

    /**
     * 是否是本地计算叠加的模式
     *
     * @return
     */
    boolean isPlusMode();

}
