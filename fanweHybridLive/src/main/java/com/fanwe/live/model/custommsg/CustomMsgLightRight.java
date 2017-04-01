package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgLightRight extends CustomMsg
{
    private String imageName;
    private int showMsg;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public void setShowMsg(int showMsg)
    {
        this.showMsg = showMsg;
    }

    public int getShowMsg()
    {
        return showMsg;
    }

    public CustomMsgLightRight()
    {
        super();
        setType(CustomMsgType.MSG_CUSTOM_LIGHT);
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

}
