package com.fanwe.live.model.custommsg;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.library.utils.SDObjectCache;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;

public class CustomMsg
{
    private int type;
    private UserModel sender;
    private String deviceType;

    public CustomMsg()
    {
        type = CustomMsgType.MSG_NONE;
        deviceType = "Android";
        sender = SDObjectCache.get(UserModel.class);
        if (sender == null)
        {
            sender = UserModelDao.query();
        }
        if (sender == null)
        {
            LogUtil.i("sender is null--------------------------------------");
        }
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public TIMMessage parseToTIMMessage()
    {
        TIMMessage msg = null;

        try
        {
            String json = SDJsonUtil.object2Json(this);
            byte[] bytes = json.getBytes("UTF-8");

            TIMCustomElem elemCustom = new TIMCustomElem();
            elemCustom.setData(bytes);

            msg = new TIMMessage();
            msg.addElement(elemCustom);
            LogUtil.i("send json:" + json);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return msg;
    }

    public final MsgModel parseToMsgModel()
    {
        TIMMessage timMessage = parseToTIMMessage();
        MsgModel msgModel = new TIMMsgModel(timMessage);
        return msgModel;
    }

    public UserModel getSender()
    {
        return sender;
    }

    public void setSender(UserModel sender)
    {
        this.sender = sender;
    }

    /**
     * 返回用于会话列表中展示的内容
     *
     * @return
     */
    public String getConversationDesc()
    {
        return "";
    }

}
