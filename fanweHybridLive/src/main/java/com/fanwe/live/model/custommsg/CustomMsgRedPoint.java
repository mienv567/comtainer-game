package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgRedPoint extends CustomMsg
{

    private int has_task_not_get ;// 值为1 表示存在没有领取奖励的任务  0 表示没有可领取的任务

    public CustomMsgRedPoint()
    {
        super();
        setType(CustomMsgType.MSG_RED_POINT);
    }

    public int getHas_task_not_get() {
        return has_task_not_get;
    }

    public void setHas_task_not_get(int has_task_not_get) {
        this.has_task_not_get = has_task_not_get;
    }


}
