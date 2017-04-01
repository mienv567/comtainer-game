package com.fanwe.live;

import android.text.TextUtils;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.ConversationUnreadMessageModel;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.model.custommsg.TIMMsgModel;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.util.List;

public class IMHelper
{

    public static TIMConversation getConversationC2C(String id)
    {
        TIMConversation conversation = null;
        if (!TextUtils.isEmpty(id))
        {
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, id);
        }
        return conversation;
    }

    public static TIMConversation getConversationGroup(String id)
    {
        TIMConversation conversation = null;
        if (!TextUtils.isEmpty(id))
        {
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, id);
        }
        return conversation;
    }

    public static void applyJoinGroup(String groupId, String reason, final TIMCallBack callback)
    {
        if (TextUtils.isEmpty(groupId))
        {
            return;
        }

        TIMGroupManager.getInstance().applyJoinGroup(groupId, reason, new TIMCallBack()
        {
            @Override
            public void onSuccess()
            {
                if (callback != null)
                {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }
        });
    }

    public static void quitGroup(String groupId, final TIMCallBack callback)
    {
        if (TextUtils.isEmpty(groupId))
        {
            return;
        }
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack()
        {

            @Override
            public void onSuccess()
            {
                if (callback != null)
                {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }
        });
    }

    public static void deleteLocalMessageGroup(String id, final TIMCallBack callback)
    {
        if (TextUtils.isEmpty(id))
        {
            return;
        }
        getConversationGroup(id).deleteLocalMessage(new TIMCallBack()
        {

            @Override
            public void onSuccess()
            {
                if (callback != null)
                {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }
        });
    }

    public static void postMsgLocal(CustomMsg customMsg, String conversationId)
    {
        MsgModel msg = customMsg.parseToMsgModel();

        postMsgLocal(msg, conversationId);
    }

    private static void postMsgLocal(MsgModel msg, String conversationId)
    {
        if (msg != null)
        {
            msg.setLocalPost(true);
            msg.setSelf(true);
            msg.setConversationPeer(conversationId);

            EImOnNewMessages event = new EImOnNewMessages();
            event.msg = msg;
            SDEventManager.post(event);
        }
    }

    public static void sendMsgGroup(String id, CustomMsg customMsg, final TIMValueCallBack<TIMMessage> callback)
    {
        if (TextUtils.isEmpty(id))
        {
            return;
        }
        TIMConversation conversation = getConversationGroup(id);
        TIMMessage tMsg = customMsg.parseToTIMMessage();
        conversation.sendMessage(tMsg, new TIMValueCallBack<TIMMessage>()
        {

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                if (callback != null)
                {
                    callback.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                LogUtil.i("sendMsgGroup error:" + code + "," + desc);
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }
        });
    }

    public static TIMMessage sendMsgC2C(String id, CustomMsg customMsg, final TIMValueCallBack<TIMMessage> callback)
    {
        if (TextUtils.isEmpty(id))
        {
            return null;
        }
        TIMConversation conversation = getConversationC2C(id);
        TIMMessage tMsg = customMsg.parseToTIMMessage();
        conversation.sendMessage(tMsg, new TIMValueCallBack<TIMMessage>()
        {

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                if (callback != null)
                {
                    callback.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                LogUtil.i("sendMsgC2C error:" + code + "," + desc);
                if (callback != null)
                {
                    callback.onError(code, desc);
                }
            }
        });
        return tMsg;
    }

    public static TotalConversationUnreadMessageModel getC2CTotalUnreadMessageModel()
    {
        TotalConversationUnreadMessageModel totalUnreadMessageModel = new TotalConversationUnreadMessageModel();

        UserModel user = UserModelDao.query();
        if (user == null)
        {
            return totalUnreadMessageModel;
        }

        long totalUnreadNum = 0;
        long cnt = TIMManager.getInstance().getConversationCount();
        for (long i = 0; i < cnt; ++i)
        {
            TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            TIMConversationType type = conversation.getType();
            if (type == TIMConversationType.C2C)
            {
                // 自己对自己发的消息过滤
                if (!conversation.getPeer().equals(user.getUserId()))
                {
                    long unreadnum = conversation.getUnreadMessageNum();
                    if (unreadnum > 0)
                    {
                        List<TIMMessage> list = conversation.getLastMsgs(1);
                        if (list != null && list.size() > 0)
                        {
                            TIMMessage msg = list.get(0);
                            MsgModel msgModel = new TIMMsgModel(msg);
                            if (msgModel.isPrivateMsg())
                            {
                                ConversationUnreadMessageModel unreadMessageModel = new ConversationUnreadMessageModel();
                                unreadMessageModel.setPeer(conversation.getPeer());
                                unreadMessageModel.setUnreadnum(unreadnum);
                                totalUnreadMessageModel.hashConver.put(conversation.getPeer(), unreadMessageModel);

                                totalUnreadNum = totalUnreadNum + unreadnum;
                            }
                        }
                    }
                }
            }
        }

        totalUnreadMessageModel.setTotalUnreadNum(totalUnreadNum);
        return totalUnreadMessageModel;
    }

    public static void setSingleC2CReadMessage(String peer)
    {
        setSingleC2CReadMessage(peer, true);
    }

    public static void setSingleC2CReadMessage(String peer, boolean isSendRefreshEvent)
    {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, peer);
        conversation.setReadMessage();
        if (isSendRefreshEvent)
        {
            postERefreshMsgUnReaded(true);
        }
    }

    public static void setAllC2CReadMessage()
    {
        setAllC2CReadMessage(true);
    }

    public static void setAllC2CReadMessage(boolean isSendRefreshEvent)
    {
        long cnt = TIMManager.getInstance().getConversationCount();
        for (long i = 0; i < cnt; ++i)
        {
            TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            TIMConversationType type = conversation.getType();
            if (type == TIMConversationType.C2C)
            {
                conversation.setReadMessage();
            }
        }
        if (isSendRefreshEvent)
        {
            postERefreshMsgUnReaded(true);
        }
    }

    public static void postERefreshMsgUnReaded()
    {
        postERefreshMsgUnReaded(false);
    }

    public static void postERefreshMsgUnReaded(boolean isFromSetLocalReade)
    {
        ERefreshMsgUnReaded event = new ERefreshMsgUnReaded();
        event.model = IMHelper.getC2CTotalUnreadMessageModel();
        event.isFromSetLocalReaded = isFromSetLocalReade;
        SDEventManager.post(event);
    }
}
