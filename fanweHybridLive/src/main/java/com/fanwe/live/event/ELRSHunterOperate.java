package com.fanwe.live.event;

import com.fanwe.live.model.custommsg.CustomMsgLRS;

/**
 * 狼人杀 - 猎人行动通知
 */
public class ELRSHunterOperate {
    public static final int NOT_HUNTE = 1;
    public static final int HUNTE = 2;
    private int step;//1-表示 不操作 2 射杀一个人
    private CustomMsgLRS mMsg;
    public ELRSHunterOperate(int step,CustomMsgLRS msg){
        this.step = step;
        mMsg = msg;
    }

    public CustomMsgLRS getMsg(){
        return mMsg;
    }

    public int getStep(){
        return this.step;
    }
}
