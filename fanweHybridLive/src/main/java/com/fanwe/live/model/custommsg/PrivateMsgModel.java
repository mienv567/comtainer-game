package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.LiveConstant.PrivateMsgType;

/**
 * 私聊item实体
 *
 * @author Administrator
 * @date 2016-6-23 上午9:17:51
 */
public class PrivateMsgModel
{

    private int type = PrivateMsgType.MSG_TEXT_LEFT;
    private MsgModel msg;

    public PrivateMsgModel(MsgModel msg)
    {
        super();
        this.msg = msg;
        if (msg != null)
        {
            switch (msg.getCustomMsgType())
            {
                case CustomMsgType.MSG_PRIVATE_TEXT:
                    if (msg.isSelf())
                    {
                        setType(PrivateMsgType.MSG_TEXT_RIGHT);
                    } else
                    {
                        setType(PrivateMsgType.MSG_TEXT_LEFT);
                    }
                    break;
                case CustomMsgType.MSG_PRIVATE_VOICE:
                    if (msg.isSelf())
                    {
                        setType(PrivateMsgType.MSG_VOICE_RIGHT);
                    } else
                    {
                        setType(PrivateMsgType.MSG_VOICE_LEFT);
                    }
                    break;

                case CustomMsgType.MSG_PRIVATE_IMAGE:
                    if (msg.isSelf())
                    {
                        setType(PrivateMsgType.MSG_IMAGE_RIGHT);
                    } else
                    {
                        setType(PrivateMsgType.MSG_IMAGE_LEFT);
                    }
                    break;
                case CustomMsgType.MSG_PRIVATE_GIFT:
                    if (msg.isSelf())
                    {
                        setType(PrivateMsgType.MSG_GIFT_RIGHT);
                    } else
                    {
                        setType(PrivateMsgType.MSG_GIFT_LEFT);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public PrivateMsgModel(CustomMsg customMsg)
    {
        this(customMsg.parseToMsgModel());
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public MsgModel getMsg()
    {
        return msg;
    }

    public void setMsg(MsgModel msg)
    {
        this.msg = msg;
    }

}
