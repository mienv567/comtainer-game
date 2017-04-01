package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant;

public class CustomMsgLRSRule extends CustomMsg
{
    private String desc;
    public CustomMsgLRSRule(){
        super();
        setType(LiveConstant.CustomMsgType.MSG_LRS_RULE);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
