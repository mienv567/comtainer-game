package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant;

public class CustomMsgLRSProgress extends CustomMsg
{
    private String desc;
    private int window; //是否弹框 1 表示弹框
    public CustomMsgLRSProgress(){
        super();
        setType(LiveConstant.CustomMsgType.MSG_LRS_PROGRESS);
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
