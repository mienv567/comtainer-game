package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgTipsMsg extends CustomMsg {

    public int getNum() {
        return num;
    }

    /**
     * 跑马灯，状态，-1，任务失败 0-收到任务，1任务完成
     */
    public int getStatus() {
        return status;
    }

    /**
     * 跑马灯，状态，-1，任务失败 0-收到任务，1任务完成
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 跑马灯，状态，-1，任务失败 0-收到任务，1任务完成
     */
    private int status;


    public void setNum(int num) {
        this.num = num;
    }

    private int num; // 弹出多少次
    private String desc; // 弹幕消息


    public CustomMsgTipsMsg() {
        super();
        setType(CustomMsgType.MSG_TIPS_MSG);
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
