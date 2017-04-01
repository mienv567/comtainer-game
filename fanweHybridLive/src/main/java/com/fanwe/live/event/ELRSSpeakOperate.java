package com.fanwe.live.event;

/**
 * 狼人杀 - 发遗言  轮流说话得到的事件
 */
public class ELRSSpeakOperate {
    private String userId;

    public ELRSSpeakOperate(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return this.userId;
    }
}
