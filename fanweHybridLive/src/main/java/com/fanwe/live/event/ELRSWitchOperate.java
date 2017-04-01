package com.fanwe.live.event;

import com.fanwe.live.model.custommsg.CustomMsgLRS;

/**
 * 狼人杀 - 女巫行动通知
 */
public class ELRSWitchOperate {
    public static final int DONE_SAVE = 1;
    public static final int SURE_POISON = 2;
    public static final int DONE_POISON = 3;
    private int step;//1-表示 救人步骤完成 不区分是救了还是没救  2-表示确定毒某人 3-完成毒人操作
    private boolean save; // false 表示没救，true表示救了
    private CustomMsgLRS mMsg;
    public ELRSWitchOperate(int step, CustomMsgLRS msg){
        this.step = step;
        mMsg = msg;
    }

    public int getStep(){
        return this.step;
    }

    public CustomMsgLRS getMsg(){
        return mMsg;
    }

    public void setSave(boolean save){
        this.save = save;
    }

    public boolean getSave(){
        return this.save;
    }
}
