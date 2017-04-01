package com.fanwe.live.model.custommsg;

import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.LiveConstant.LiveMsgType;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.tencent.TIMMessage;

public abstract class MsgModel
{

    /**
     * 私聊消息类型
     */
    private int privateMsgType = LiveConstant.PrivateMsgType.MSG_TEXT_LEFT;
    /**
     * 直播间消息列表类型
     */
    private int liveMsgType = LiveMsgType.MSG_TEXT;

    //
    private int customMsgType = CustomMsgType.MSG_NONE;
    private CustomMsg customMsg;
    private CustomMsgText customMsgText;
    private CustomMsgGift customMsgGift;
    private CustomMsgPopMsg customMsgPopMsg;
    private CustomMsgTipsMsg customMsgTipsMsg;
    private CustomMsgCreaterQuit customMsgCreaterExitRoom;
    private CustomMsgForbidSendMsg customMsgForbidSendMsg;
    private CustomMsgViewerJoin customMsgViewerJoin;
    private CustomMsgViewerQuit customMsgViewerQuit;
    private CustomMsgEndVideo customMsgEndVideo;
    private CustomMsgRedEnvelope customMsgRedEnvelope;
    private CustomMsgLiveMsg customMsgLiveMsg;
    private CustomMsgCreaterLeave customMsgCreaterLeave;
    private CustomMsgCreaterComeback customMsgCreaterComeback;
    private CustomMsgLight customMsgLight;
    private CustomMsgLightRight customMsgLightRight;
    private CustomMsgInviteVideo customMsgInviteVideo;
    private CustomMsgAcceptVideo customMsgAcceptVideo;
    private CustomMsgRejectVideo customMsgRejectVideo;
    private CustomMsgCreaterStopVideo customMsgCreaterStopVideo;
    private CustomMsgStopLive customMsgStopLive;
    private CustomMsgPrivateText customMsgPrivateText;
    private CustomMsgLiveStopped customMsgLiveStopped;
    private CustomMsgPrivateVoice customMsgPrivateVoice;
    private CustomMsgPrivateImage customMsgPrivateImage;
    private CustomMsgPrivateGift customMsgPrivateGift;
    private CustomMsgAuctionSuccess customMsgAuctionSuccess;
    private CustomMsgAuctionFail customMsgAuctionFail;
    private CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay;
    private CustomMsgAuctionOffer customMsgAuctionOffer;
    private CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess;
    private CustomMsgAuctionCreateSuccess customMsgAuctionCreateSuccess;
    private CustomMsgShare customMsgShare;
    private CustomMsgStar customMsgStar;
    private CustomMsgChangeChannel customMsgChangeChannel;
    private CustomMsgRedPoint customMsgRedPoint;
    private CustomMsgLRS customMsgLRS;
    private CustomMsgLRSProgress customMsgLRSProgress;
    private CustomMsgLRSRule customMsgLRSRule;
    private CustomMsgOnlineNumber customMsgOnlineNumber;
    private CustomMsgMissionResult customMsgMissionResult;
    /**
     * true-本地通过EventBus直接发送的消息
     */
    private boolean isLocalPost = false;
    /**
     * 是否自己发送的
     */
    private boolean isSelf = false;
    /**
     * 会话的对方id或者群组Id
     */
    private String conversationtPeer;
    /**
     * 消息在服务端生成的时间戳
     */
    private long timestamp;
    /**
     * 消息在服务端生成的时间格式化
     */
    private String timestampFormat;
    private MsgStatus status = MsgStatus.Invalid;

    public CustomMsgOnlineNumber getCustomMsgOnlineNumber() {
        return customMsgOnlineNumber;
    }

    public void setCustomMsgOnlineNumber(CustomMsgOnlineNumber customMsgOnlineNumber){
        this.customMsgOnlineNumber = customMsgOnlineNumber;
        setCustomMsg(customMsgOnlineNumber);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_ONLINE_NUM);
    }

    public CustomMsgMissionResult getCustomMsgMissionResult() {
        return customMsgMissionResult;
    }

    public void setCustomMsgMissionResult(CustomMsgMissionResult customMsgMissionResult){
        this.customMsgMissionResult = customMsgMissionResult;
        setCustomMsg(customMsgMissionResult);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_Marquee);
    }

    /**
     * 是否是直播间列表显示的消息
     *
     * @return
     */
    public boolean isLiveMsg()
    {
        boolean result = false;
        switch (customMsgType)
        {
            case CustomMsgType.MSG_TEXT:
            case CustomMsgType.MSG_GIFT:
            case CustomMsgType.MSG_POP_MSG:
            case CustomMsgType.MSG_FORBID_SEND_MSG:
            case CustomMsgType.MSG_VIEWER_JOIN:
            case CustomMsgType.MSG_LIVE_MSG:
            case CustomMsgType.MSG_RED_ENVELOPE:
            case CustomMsgType.MSG_CREATER_LEAVE:
            case CustomMsgType.MSG_CREATER_COME_BACK:
            case CustomMsgType.MSG_LIGHT:
            case CustomMsgType.MSG_AUCTION_OFFER:
            case CustomMsgType.MSG_AUCTION_SUCCESS:
            case CustomMsgType.MSG_AUCTION_PAY_SUCCESS:
            case CustomMsgType.MSG_AUCTION_FAIL:
            case CustomMsgType.MSG_AUCTION_NOTIFY_PAY:
            case CustomMsgType.MSG_AUCTION_CREATE_SUCCESS:
            case CustomMsgType.MSG_SHARE:
            case CustomMsgType.MSG_STAR:
            case CustomMsgType.MSG_LRS_PROGRESS:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 是否是私聊消息
     *
     * @return
     */
    public boolean isPrivateMsg()
    {
        boolean result = false;
        switch (customMsgType)
        {
            case CustomMsgType.MSG_PRIVATE_TEXT:
            case CustomMsgType.MSG_PRIVATE_VOICE:
            case CustomMsgType.MSG_PRIVATE_IMAGE:
            case CustomMsgType.MSG_PRIVATE_GIFT:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 是否是竞拍消息
     *
     * @return
     */
    public boolean isAuctionMsg()
    {
        boolean result = false;
        switch (customMsgType)
        {
            case CustomMsgType.MSG_AUCTION_SUCCESS:
            case CustomMsgType.MSG_AUCTION_NOTIFY_PAY:
            case CustomMsgType.MSG_AUCTION_FAIL:
            case CustomMsgType.MSG_AUCTION_OFFER:
            case CustomMsgType.MSG_AUCTION_PAY_SUCCESS:
            case CustomMsgType.MSG_AUCTION_CREATE_SUCCESS:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 重写，用于删除本地缓存的消息
     */
    public abstract void remove();

    //==============================================竞拍自定义消息start
    public CustomMsgAuctionNotifyPay getCustomMsgAuctionNotifyPay()
    {
        return customMsgAuctionNotifyPay;
    }

    public void setCustomMsgAuctionNotifyPay(CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay)
    {
        this.customMsgAuctionNotifyPay = customMsgAuctionNotifyPay;
        setCustomMsg(customMsgAuctionNotifyPay);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_NOTIFY_PAY);
    }

    public CustomMsgAuctionFail getCustomMsgAuctionFail()
    {
        return customMsgAuctionFail;
    }

    public void setCustomMsgAuctionFail(CustomMsgAuctionFail customMsgAuctionFail)
    {
        this.customMsgAuctionFail = customMsgAuctionFail;
        setCustomMsg(customMsgAuctionFail);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_FAIL);
    }

    public CustomMsgAuctionSuccess getCustomMsgAuctionSuccess()
    {
        return customMsgAuctionSuccess;
    }

    public void setCustomMsgAuctionSuccess(CustomMsgAuctionSuccess customMsgAuctionSuccess)
    {
        this.customMsgAuctionSuccess = customMsgAuctionSuccess;
        setCustomMsg(customMsgAuctionSuccess);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_SUCCESS);
    }

    public CustomMsgAuctionCreateSuccess getCustomMsgAuctionCreateSuccess()
    {
        return customMsgAuctionCreateSuccess;
    }

    public void setCustomMsgAuctionCreateSuccess(CustomMsgAuctionCreateSuccess customMsgAuctionCreateSuccess)
    {
        this.customMsgAuctionCreateSuccess = customMsgAuctionCreateSuccess;
        setCustomMsg(customMsgAuctionCreateSuccess);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_CREATE_SUCCESS);
    }

    public CustomMsgAuctionPaySuccess getCustomMsgAuctionPaySuccess()
    {
        return customMsgAuctionPaySuccess;
    }

    public void setCustomMsgAuctionPaySuccess(CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess)
    {
        this.customMsgAuctionPaySuccess = customMsgAuctionPaySuccess;
        setCustomMsg(customMsgAuctionPaySuccess);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_PAY_SUCCESS);
    }
    //==============================================竞拍自定义消息end

    public CustomMsgAuctionOffer getCustomMsgAuctionOffer()
    {
        return customMsgAuctionOffer;
    }

    public void setCustomMsgAuctionOffer(CustomMsgAuctionOffer customMsgAuctionOffer)
    {
        this.customMsgAuctionOffer = customMsgAuctionOffer;
        setCustomMsg(customMsgAuctionOffer);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_AUCTION_OFFER);
    }

    public CustomMsgPrivateGift getCustomMsgPrivateGift()
    {
        return customMsgPrivateGift;
    }

    public void setCustomMsgPrivateGift(CustomMsgPrivateGift customMsgPrivateGift)
    {
        this.customMsgPrivateGift = customMsgPrivateGift;
        setCustomMsg(customMsgPrivateGift);

        // 私聊消息类型
        if (isSelf())
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_GIFT_RIGHT);
        } else
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_GIFT_LEFT);
        }
    }

    public CustomMsgPrivateImage getCustomMsgPrivateImage()
    {
        return customMsgPrivateImage;
    }

    public void setCustomMsgPrivateImage(CustomMsgPrivateImage customMsgPrivateImage)
    {
        this.customMsgPrivateImage = customMsgPrivateImage;
        setCustomMsg(customMsgPrivateImage);

        // 私聊消息类型
        if (isSelf())
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_IMAGE_RIGHT);
        } else
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_IMAGE_LEFT);
        }
    }

    public CustomMsgPrivateVoice getCustomMsgPrivateVoice()
    {
        return customMsgPrivateVoice;
    }

    public void setCustomMsgPrivateVoice(CustomMsgPrivateVoice customMsgPrivateVoice)
    {
        this.customMsgPrivateVoice = customMsgPrivateVoice;
        setCustomMsg(customMsgPrivateVoice);

        // 私聊消息类型
        if (isSelf())
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_VOICE_RIGHT);
        } else
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_VOICE_LEFT);
        }
    }

    public CustomMsgLiveStopped getCustomMsgLiveStopped()
    {
        return customMsgLiveStopped;
    }

    public void setCustomMsgLiveStopped(CustomMsgLiveStopped customMsgLiveStopped)
    {
        this.customMsgLiveStopped = customMsgLiveStopped;
        setCustomMsg(customMsgLiveStopped);
    }

    public CustomMsgGift getCustomMsgGift()
    {
        return customMsgGift;
    }

    public void setCustomMsgGift(CustomMsgGift customMsgGift)
    {
        this.customMsgGift = customMsgGift;
        setCustomMsg(customMsgGift);

        // 直播间消息列表类型
        UserModel user = UserModelDao.query();
        if (user != null && customMsgGift.getToUserId().equals(user.getUserId()))
        {
            setLiveMsgType(LiveMsgType.MSG_GIFT_CREATER);
        } else
        {
            setLiveMsgType(LiveMsgType.MSG_GIFT_VIEWER);
        }
    }

    public CustomMsgText getCustomMsgText()
    {
        return customMsgText;
    }

    public void setCustomMsgText(CustomMsgText customMsgText)
    {
        this.customMsgText = customMsgText;
        setCustomMsg(customMsgText);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_TEXT);
    }

    public CustomMsgCreaterQuit getCustomMsgCreaterExitRoom()
    {
        return customMsgCreaterExitRoom;
    }

    public void setCustomMsgCreaterExitRoom(CustomMsgCreaterQuit customMsgCreaterExitRoom)
    {
        this.customMsgCreaterExitRoom = customMsgCreaterExitRoom;
        setCustomMsg(customMsgCreaterExitRoom);
    }

    public CustomMsgViewerJoin getCustomMsgViewerJoin()
    {
        return customMsgViewerJoin;
    }

    public void setCustomMsgViewerJoin(CustomMsgViewerJoin customMsgViewerJoin)
    {
        this.customMsgViewerJoin = customMsgViewerJoin;
        setCustomMsg(customMsgViewerJoin);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_VIEWER_JOIN);
    }

    public CustomMsgViewerQuit getCustomMsgViewerQuit()
    {
        return customMsgViewerQuit;
    }

    public void setCustomMsgViewerQuit(CustomMsgViewerQuit customMsgViewerQuit)
    {
        this.customMsgViewerQuit = customMsgViewerQuit;
        setCustomMsg(customMsgViewerQuit);
    }

    public int getCustomMsgType()
    {
        return customMsgType;
    }

    public void setCustomMsgType(int customMsgType)
    {
        this.customMsgType = customMsgType;
    }

    public CustomMsgForbidSendMsg getCustomMsgForbidSendMsg()
    {
        return customMsgForbidSendMsg;
    }

    public void setCustomMsgForbidSendMsg(CustomMsgForbidSendMsg customMsgForbidSendMsg)
    {
        this.customMsgForbidSendMsg = customMsgForbidSendMsg;
        setCustomMsg(customMsgForbidSendMsg);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_FORBID_SEND_MSG);
    }

    public CustomMsgPopMsg getCustomMsgPopMsg()
    {
        return customMsgPopMsg;
    }

    public void setCustomMsgPopMsg(CustomMsgPopMsg customMsgPopMsg)
    {
        this.customMsgPopMsg = customMsgPopMsg;
        setCustomMsg(customMsgPopMsg);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_POP_MSG);
    }

    public CustomMsgTipsMsg getCustomMsgTipsMsg()
    {
        return customMsgTipsMsg;
    }

    public void setCustomMsgTipsMsg(CustomMsgTipsMsg customMsgTipsMsg)
    {
        this.customMsgTipsMsg = customMsgTipsMsg;
        setCustomMsg(customMsgTipsMsg);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_TIP_MSG);
    }

    public CustomMsgEndVideo getCustomMsgEndVideo()
    {
        return customMsgEndVideo;
    }

    public void setCustomMsgEndVideo(CustomMsgEndVideo customMsgEndVideo)
    {
        this.customMsgEndVideo = customMsgEndVideo;
        setCustomMsg(customMsgEndVideo);
    }

    public boolean isLocalPost()
    {
        return isLocalPost;
    }

    public void setLocalPost(boolean isLocalPost)
    {
        this.isLocalPost = isLocalPost;
    }

    public CustomMsgRedEnvelope getCustomMsgRedEnvelope()
    {
        return customMsgRedEnvelope;
    }

    public void setCustomMsgRedEnvelope(CustomMsgRedEnvelope customMsgRedEnvelope)
    {
        this.customMsgRedEnvelope = customMsgRedEnvelope;
        setCustomMsg(customMsgRedEnvelope);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_RED_ENVELOPE);
    }

    public CustomMsgLiveMsg getCustomMsgLiveMsg()
    {
        return customMsgLiveMsg;
    }

    public void setCustomMsgLiveMsg(CustomMsgLiveMsg customMsgLiveMsg)
    {
        this.customMsgLiveMsg = customMsgLiveMsg;
        setCustomMsg(customMsgLiveMsg);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_LIVE_MSG);
    }

    public CustomMsgShare getCustomMsgShare(){
        return customMsgShare;
    }

    public void setCustomMsgShare(CustomMsgShare customMsgShare){
        this.customMsgShare = customMsgShare;
        setCustomMsg(customMsgShare);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_SHARE);
    }

    public CustomMsgChangeChannel getCustomMsgChangeChannel(){
        return customMsgChangeChannel;
    }

    public void setCustomMsgChangechannel(CustomMsgChangeChannel customMsgChangeChannel){
        this.customMsgChangeChannel = customMsgChangeChannel;
        setCustomMsg(customMsgChangeChannel);
    }

    public CustomMsgStar getCustomMsgStar(){
        return customMsgStar;
    }

    public void setCustomMsgStar(CustomMsgStar customMsgStar){
        this.customMsgStar = customMsgStar;
        setCustomMsg(customMsgStar);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_STAR);
    }

    public CustomMsgCreaterLeave getCustomMsgCreaterLeave()
    {
        return customMsgCreaterLeave;
    }

    public void setCustomMsgCreaterLeave(CustomMsgCreaterLeave customMsgCreaterLeave)
    {
        this.customMsgCreaterLeave = customMsgCreaterLeave;
        setCustomMsg(customMsgCreaterLeave);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_CREATER_LEAVE);
    }

    public CustomMsgCreaterComeback getCustomMsgCreaterComeback()
    {
        return customMsgCreaterComeback;
    }

    public void setCustomMsgCreaterComeback(CustomMsgCreaterComeback customMsgCreaterComeback)
    {
        this.customMsgCreaterComeback = customMsgCreaterComeback;
        setCustomMsg(customMsgCreaterComeback);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_CREATER_COME_BACK);
    }

    public CustomMsgLight getCustomMsgLight()
    {
        return customMsgLight;
    }

    public void setCustomMsgLight(CustomMsgLight customMsgLight)
    {
        this.customMsgLight = customMsgLight;
        setCustomMsg(customMsgLight);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_LIGHT);
    }

    public CustomMsgLightRight getCustomMsgLightRight()
    {
        return customMsgLightRight;
    }

    public void setCustomMsgLightRight(CustomMsgLightRight customMsgLightRight)
    {
        this.customMsgLightRight = customMsgLightRight;
        setCustomMsg(customMsgLightRight);

        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_CUSTOM_LIGHT);
    }

    public CustomMsgRejectVideo getCustomMsgRejectVideo()
    {
        return customMsgRejectVideo;
    }

    public void setCustomMsgRejectVideo(CustomMsgRejectVideo customMsgRejectVideo)
    {
        this.customMsgRejectVideo = customMsgRejectVideo;
        setCustomMsg(customMsgRejectVideo);
    }

    public CustomMsgAcceptVideo getCustomMsgAcceptVideo()
    {
        return customMsgAcceptVideo;
    }

    public void setCustomMsgAcceptVideo(CustomMsgAcceptVideo customMsgAcceptVideo)
    {
        this.customMsgAcceptVideo = customMsgAcceptVideo;
        setCustomMsg(customMsgAcceptVideo);
    }

    public CustomMsgInviteVideo getCustomMsgInviteVideo()
    {
        return customMsgInviteVideo;
    }

    public void setCustomMsgInviteVideo(CustomMsgInviteVideo customMsgInviteVideo)
    {
        this.customMsgInviteVideo = customMsgInviteVideo;
        setCustomMsg(customMsgInviteVideo);
    }

    public CustomMsgCreaterStopVideo getCustomMsgCreaterStopVideo()
    {
        return customMsgCreaterStopVideo;
    }

    public void setCustomMsgCreaterStopVideo(CustomMsgCreaterStopVideo customMsgCreaterStopVideo)
    {
        this.customMsgCreaterStopVideo = customMsgCreaterStopVideo;
        setCustomMsg(customMsgCreaterStopVideo);
    }

    public CustomMsgStopLive getCustomMsgStopLive()
    {
        return customMsgStopLive;
    }

    public void setCustomMsgStopLive(CustomMsgStopLive customMsgStopLive)
    {
        this.customMsgStopLive = customMsgStopLive;
        setCustomMsg(customMsgStopLive);
    }

    public CustomMsgPrivateText getCustomMsgPrivateText()
    {
        return customMsgPrivateText;
    }

    public void setCustomMsgPrivateText(CustomMsgPrivateText customMsgPrivateText)
    {
        this.customMsgPrivateText = customMsgPrivateText;
        setCustomMsg(customMsgPrivateText);

        // 私聊消息类型
        if (isSelf())
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_TEXT_RIGHT);
        } else
        {
            setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_TEXT_LEFT);
        }
    }

    public CustomMsgRedPoint getCustomMsgRedPoint(){
        return this.customMsgRedPoint;
    }

    public void setCustomMsgRedPoint(CustomMsgRedPoint customMsgRedPoint){
        this.customMsgRedPoint = customMsgRedPoint;
        setCustomMsg(customMsgRedPoint);
    }

    public CustomMsgLRS getCustomMsgLRS(){
        return this.customMsgLRS;
    }

    public void setCustomMsgLRS(CustomMsgLRS customMsgLRS){
        this.customMsgLRS = customMsgLRS;
        setCustomMsg(customMsgLRS);
    }

    public CustomMsgLRSProgress getCustomMsgLRSProgress(){
        return this.customMsgLRSProgress;
    }

    public void setCustomMsgLRSProgress(CustomMsgLRSProgress customMsgLRSProgress){
        this.customMsgLRSProgress = customMsgLRSProgress;
        setCustomMsg(customMsgLRSProgress);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_LRS_PROGRESS);
    }

    public CustomMsgLRSRule getCustomMsgLRSRule(){
        return this.customMsgLRSRule;
    }

    public void setCustomMsgLRSRule(CustomMsgLRSRule customMsgLRSRule){
        this.customMsgLRSRule = customMsgLRSRule;
        setCustomMsg(customMsgLRSRule);
        // 直播间消息列表类型
        setLiveMsgType(LiveMsgType.MSG_LRS_RULE);
    }

    public CustomMsg getCustomMsg()
    {
        return customMsg;
    }

    public void setCustomMsg(CustomMsg customMsg)
    {
        this.customMsg = customMsg;
        if (customMsg != null)
        {
            int type = customMsg.getType();
            setCustomMsgType(type);
        }
    }

    public boolean isSelf()
    {
        return isSelf;
    }

    public void setSelf(boolean isSelf)
    {
        this.isSelf = isSelf;
    }

    public String getConversationPeer()
    {
        if (conversationtPeer == null)
        {
            conversationtPeer = "";
        }
        return conversationtPeer;
    }

    public void setConversationPeer(String conversationtPeer)
    {
        this.conversationtPeer = conversationtPeer;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;

        String format = SDDateUtil.formatDuringFrom(timestamp * 1000);
        setTimestampFormat(format);
    }

    public String getTimestampFormat()
    {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat)
    {
        this.timestampFormat = timestampFormat;
    }

    public MsgStatus getStatus()
    {
        return status;
    }

    public void setStatus(MsgStatus status)
    {
        this.status = status;
    }

    public int getLiveMsgType()
    {
        return liveMsgType;
    }

    public void setLiveMsgType(int liveMsgType)
    {
        this.liveMsgType = liveMsgType;
    }

    public int getPrivateMsgType()
    {
        return privateMsgType;
    }

    public void setPrivateMsgType(int privateMsgType)
    {
        this.privateMsgType = privateMsgType;
    }

    // 腾讯im相关方法

    /**
     * 解析腾讯的消息实体
     *
     * @param timMessage
     */
    public abstract void setTimMessage(TIMMessage timMessage);

}
