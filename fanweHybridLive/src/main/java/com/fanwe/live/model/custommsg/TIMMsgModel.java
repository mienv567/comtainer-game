package com.fanwe.live.model.custommsg;

import android.text.TextUtils;

import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.library.media.recorder.SDMediaRecorder;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.library.utils.SDObjectCache;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.tencent.TIMCallBack;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMFaceElem;
import com.tencent.TIMFileElem;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMLocationElem;
import com.tencent.TIMMessage;
import com.tencent.TIMProfileSystemElem;
import com.tencent.TIMSNSSystemElem;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.TIMVideoElem;

import java.io.File;
import java.util.List;

public class TIMMsgModel extends MsgModel
{

    private TIMMessage timMessage;
    private TIMCustomElem timCustomElem;
    private TIMFaceElem timFaceElem;
    private TIMFileElem timFileElem;
    private TIMGroupSystemElem timGroupSystemElem;
    private TIMGroupTipsElem timGroupTipsElem;
    private TIMImageElem timImageElem;
    private TIMLocationElem timLocationElem;
    private TIMProfileSystemElem timProfileSystemElem;
    private TIMSNSSystemElem timSnsSystemElem;
    private TIMSoundElem timSoundElem;
    private TIMTextElem timTextElem;
    private TIMVideoElem timVideoElem;

    public TIMMsgModel(TIMMessage timMessage)
    {
        super();
        setTimMessage(timMessage);
    }

    @Override
    public void remove()
    {
        if (timMessage != null)
        {
            timMessage.remove();
        }
    }

    public TIMMessage getTimMessage()
    {
        return timMessage;
    }

    @Override
    public void setTimMessage(TIMMessage timMessage)
    {
        // 解析消息
        this.timMessage = timMessage;
        readElement();
        parseCustomElem();
    }

    public TIMCustomElem getTimCustomElem()
    {
        return timCustomElem;
    }

    public void setTimCustomElem(TIMCustomElem timCustomElem)
    {
        this.timCustomElem = timCustomElem;
    }

    /**
     * 将消息的elem解析出来
     */
    private void readElement()
    {
        if (timMessage != null)
        {
            switch (timMessage.status())
            {
                case SendFail:
                    setStatus(MsgStatus.SendFail);
                    break;
                case Sending:
                    setStatus(MsgStatus.Sending);
                    break;
                case SendSucc:
                    setStatus(MsgStatus.SendSuccess);
                    break;
                case HasDeleted:
                    setStatus(MsgStatus.HasDeleted);
                    break;
                default:
                    setStatus(MsgStatus.Invalid);
                    break;
            }

            setSelf(timMessage.isSelf());
            setConversationPeer(timMessage.getConversation().getPeer());
            setTimestamp(timMessage.timestamp());

            long count = timMessage.getElementCount();
            TIMElem elem = null;
            for (int i = 0; i < count; i++)
            {
                elem = timMessage.getElement(i);
                if (elem == null)
                {
                    continue;
                }
                TIMElemType elemType = elem.getType();
                switch (elemType)
                {
                    case Custom:
                        setTimCustomElem((TIMCustomElem) elem);
                        break;
                    case Face:
                        setTimFaceElem((TIMFaceElem) elem);
                        break;
                    case File:
                        setTimFileElem((TIMFileElem) elem);
                        break;
                    case GroupSystem:
                        setTimGroupSystemElem((TIMGroupSystemElem) elem);
                        break;
                    case GroupTips:
                        setTimGroupTipsElem((TIMGroupTipsElem) elem);
                        break;
                    case Image:
                        setTimImageElem((TIMImageElem) elem);
                        break;
                    case Invalid:

                        break;
                    case Location:
                        setTimLocationElem((TIMLocationElem) elem);
                        break;
                    case ProfileTips:
                        setTimProfileSystemElem((TIMProfileSystemElem) elem);
                        break;
                    case SNSTips:
                        setTimSnsSystemElem((TIMSNSSystemElem) elem);
                        break;
                    case Sound:
                        setTimSoundElem((TIMSoundElem) elem);
                        break;
                    case Text:
                        setTimTextElem((TIMTextElem) elem);
                        break;
                    case Video:
                        setTimVideoElem((TIMVideoElem) elem);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * 将TIMCustomElem解析成自定义消息
     */
    private void parseCustomElem()
    {
        if (timCustomElem != null)
        {
            CustomMsg customMsg = parseToModel(CustomMsg.class);
            if (customMsg != null)
            {
                UserModel sender = customMsg.getSender();
                if (sender != null)
                {
                    String userId = sender.getUserId();
                    if (userId != null)
                    {
                        UserModel user = SDObjectCache.get(UserModel.class);
                        if (user != null && userId.equals(user.getUserId()))
                        {
                            int level = sender.getUserLevel();
                            int oldLevel = user.getUserLevel();
                            if (level > oldLevel)
                            {
                                user.setUserLevel(level);
                                UserModelDao.insertOrUpdate(user);
                            }
                        }
                    }
                }

                switch (customMsg.getType())
                {
                    case CustomMsgType.MSG_TEXT:

                        setCustomMsgText(parseToModel(CustomMsgText.class));

                        if (timTextElem != null)
                        {
                            getCustomMsgText().setText(timTextElem.getText());
                        }
                        break;
                    case CustomMsgType.MSG_GIFT:
                        setCustomMsgGift(parseToModel(CustomMsgGift.class));
                        break;
                    case CustomMsgType.MSG_POP_MSG:
                        setCustomMsgPopMsg(parseToModel(CustomMsgPopMsg.class));

                        if (timTextElem != null)
                        {
                            getCustomMsgPopMsg().setDesc(timTextElem.getText());
                        }
                        break;
                    case CustomMsgType.MSG_TIPS_MSG:
                        setCustomMsgTipsMsg(parseToModel(CustomMsgTipsMsg.class));
                        if (timTextElem != null)
                        {
                            getCustomMsgTipsMsg().setDesc(timTextElem.getText());
                        }
                        break;
                    case CustomMsgType.MSG_CREATER_QUIT:
                        setCustomMsgCreaterExitRoom(parseToModel(CustomMsgCreaterQuit.class));
                        break;
                    case CustomMsgType.MSG_FORBID_SEND_MSG:
                        setCustomMsgForbidSendMsg(parseToModel(CustomMsgForbidSendMsg.class));
                        break;
                    case CustomMsgType.MSG_VIEWER_JOIN:
                        setCustomMsgViewerJoin(parseToModel(CustomMsgViewerJoin.class));
                        break;
                    case CustomMsgType.MSG_VIEWER_QUIT:
                        setCustomMsgViewerQuit(parseToModel(CustomMsgViewerQuit.class));
                        break;
                    case CustomMsgType.MSG_END_VIDEO:
                        setCustomMsgEndVideo(parseToModel(CustomMsgEndVideo.class));
                        break;
                    case CustomMsgType.MSG_RED_ENVELOPE:
                        setCustomMsgRedEnvelope(parseToModel(CustomMsgRedEnvelope.class));
                        break;
                    case CustomMsgType.MSG_LIVE_MSG:
                        setCustomMsgLiveMsg(parseToModel(CustomMsgLiveMsg.class));
                        break;
                    case CustomMsgType.MSG_CREATER_LEAVE:
                        setCustomMsgCreaterLeave(parseToModel(CustomMsgCreaterLeave.class));
                        break;
                    case CustomMsgType.MSG_CREATER_COME_BACK:
                        setCustomMsgCreaterComeback(parseToModel(CustomMsgCreaterComeback.class));
                        break;
                    case CustomMsgType.MSG_LIGHT:
                        setCustomMsgLight(parseToModel(CustomMsgLight.class));
                        break;
                    case CustomMsgType.MSG_CUSTOM_LIGHT:
                        setCustomMsgLightRight(parseToModel(CustomMsgLightRight.class));
                        break;
                    case CustomMsgType.MSG_SHARE:
                        setCustomMsgShare(parseToModel(CustomMsgShare.class));
                        break;
                    case CustomMsgType.MSG_STAR:
                        setCustomMsgStar(parseToModel(CustomMsgStar.class));
                        break;
                    case CustomMsgType.MSG_CHANGE_CHANNEL:
                        setCustomMsgChangechannel(parseToModel(CustomMsgChangeChannel.class));
                        break;
                    case CustomMsgType.MSG_INVITE_VIDEO:
                        setCustomMsgInviteVideo(parseToModel(CustomMsgInviteVideo.class));
                        break;
                    case CustomMsgType.MSG_ACCEPT_VIDEO:
                        setCustomMsgAcceptVideo(parseToModel(CustomMsgAcceptVideo.class));
                        break;
                    case CustomMsgType.MSG_REJECT_VIDEO:
                        setCustomMsgRejectVideo(parseToModel(CustomMsgRejectVideo.class));
                        break;
                    case CustomMsgType.MSG_CREATER_STOP_VIDEO:
                        setCustomMsgCreaterStopVideo(parseToModel(CustomMsgCreaterStopVideo.class));
                        break;
                    case CustomMsgType.MSG_STOP_LIVE:
                        setCustomMsgStopLive(parseToModel(CustomMsgStopLive.class));
                        break;
                    case CustomMsgType.MSG_LIVE_STOPPED:
                        setCustomMsgLiveStopped(parseToModel(CustomMsgLiveStopped.class));
                        break;
                    case CustomMsgType.MSG_PRIVATE_TEXT:
                        setCustomMsgPrivateText(parseToModel(CustomMsgPrivateText.class));
                        break;
                    case CustomMsgType.MSG_PRIVATE_GIFT:
                        setCustomMsgPrivateGift(parseToModel(CustomMsgPrivateGift.class));
                        break;
                    case CustomMsgType.MSG_PRIVATE_VOICE:
                        CustomMsgPrivateVoice customMsgPrivateVoice = parseToModel(CustomMsgPrivateVoice.class);
                        if (customMsgPrivateVoice != null && timSoundElem != null)
                        {
                            String uuid = timSoundElem.getUuid();

                            if (!TextUtils.isEmpty(uuid))
                            {
                                File file = SDMediaRecorder.getInstance().getFile(uuid);
                                if (file.exists())
                                {
                                    String path = file.getAbsolutePath();
                                    customMsgPrivateVoice.setPath(path);
                                }
                            } else
                            {
                                // 主动post解析
                            }
                            setCustomMsgPrivateVoice(customMsgPrivateVoice);
                        }
                        break;
                    case CustomMsgType.MSG_PRIVATE_IMAGE:
                        CustomMsgPrivateImage customMsgPrivateImage = parseToModel(CustomMsgPrivateImage.class);
                        if (customMsgPrivateImage != null && timImageElem != null)
                        {
                            List<TIMImage> listImage = timImageElem.getImageList();
                            if (!SDCollectionUtil.isEmpty(listImage))
                            {
                                TIMImage image = listImage.get(0);
                                String url = image.getUrl();
                                customMsgPrivateImage.setUrl(url);
                            }
                            setCustomMsgPrivateImage(customMsgPrivateImage);
                        }
                        break;
                    case CustomMsgType.MSG_AUCTION_SUCCESS:
                        setCustomMsgAuctionSuccess(parseToModel(CustomMsgAuctionSuccess.class));
                        break;
                    case CustomMsgType.MSG_AUCTION_FAIL:
                        setCustomMsgAuctionFail(parseToModel(CustomMsgAuctionFail.class));
                        break;
                    case CustomMsgType.MSG_AUCTION_NOTIFY_PAY:
                        setCustomMsgAuctionNotifyPay(parseToModel(CustomMsgAuctionNotifyPay.class));
                        break;
                    case CustomMsgType.MSG_AUCTION_OFFER:
                        setCustomMsgAuctionOffer(parseToModel(CustomMsgAuctionOffer.class));
                        break;
                    case CustomMsgType.MSG_AUCTION_PAY_SUCCESS:
                        setCustomMsgAuctionPaySuccess(parseToModel(CustomMsgAuctionPaySuccess.class));
                        break;
                    case CustomMsgType.MSG_AUCTION_CREATE_SUCCESS:
                        setCustomMsgAuctionCreateSuccess(parseToModel(CustomMsgAuctionCreateSuccess.class));
                        break;
                    case CustomMsgType.MSG_RED_POINT:
                        setCustomMsgRedPoint(parseToModel(CustomMsgRedPoint.class));
                        break;
                    case CustomMsgType.MSG_LRS:
                        setCustomMsgLRS(parseToModel(CustomMsgLRS.class));
                        break;
                    case CustomMsgType.MSG_LRS_PROGRESS:
                        setCustomMsgLRSProgress(parseToModel(CustomMsgLRSProgress.class));
                        break;
                    case CustomMsgType.MSG_LRS_RULE:
                        setCustomMsgLRSRule(parseToModel(CustomMsgLRSRule.class));
                        break;
                    case CustomMsgType.MSG_ONLINE_NUM:
                        setCustomMsgOnlineNumber(parseToModel(CustomMsgOnlineNumber.class));
                        break;
                    case CustomMsgType.MSG_MISSION_RESULT:
                        setCustomMsgMissionResult(parseToModel(CustomMsgMissionResult.class));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 检测声音文件是否存在
     *
     * @param listener
     * @return true-本地不存在缓存，需要下载
     */
    public boolean checkSoundFile(final TIMValueCallBack<String> listener)
    {
        boolean needDownload = false;
        if (getCustomMsgType() == LiveConstant.CustomMsgType.MSG_PRIVATE_VOICE)
        {
            if (timSoundElem != null)
            {
                String uuid = timSoundElem.getUuid();
                if (!TextUtils.isEmpty(uuid))
                {
                    File file = SDMediaRecorder.getInstance().getFile(uuid);
                    if (file != null && !file.exists())
                    {
                        // 需要下载文件
                        needDownload = true;
                        final String path = file.getAbsolutePath();
                        timSoundElem.getSoundToFile(path, new TIMCallBack()
                        {
                            @Override
                            public void onError(int i, String s)
                            {
                                if (listener != null)
                                {
                                    listener.onError(i, s);
                                }
                            }

                            @Override
                            public void onSuccess()
                            {
                                if (listener != null)
                                {
                                    listener.onSuccess(path);
                                }
                            }
                        });
                    }
                }
            }
        }
        return needDownload;
    }

    public TIMFaceElem getTimFaceElem()
    {
        return timFaceElem;
    }

    public void setTimFaceElem(TIMFaceElem timFaceElem)
    {
        this.timFaceElem = timFaceElem;
    }

    public TIMFileElem getTimFileElem()
    {
        return timFileElem;
    }

    public void setTimFileElem(TIMFileElem timFileElem)
    {
        this.timFileElem = timFileElem;
    }

    public TIMGroupSystemElem getTimGroupSystemElem()
    {
        return timGroupSystemElem;
    }

    public void setTimGroupSystemElem(TIMGroupSystemElem timGroupSystemElem)
    {
        this.timGroupSystemElem = timGroupSystemElem;
    }

    public TIMGroupTipsElem getTimGroupTipsElem()
    {
        return timGroupTipsElem;
    }

    public void setTimGroupTipsElem(TIMGroupTipsElem timGroupTipsElem)
    {
        this.timGroupTipsElem = timGroupTipsElem;
    }

    public TIMImageElem getTimImageElem()
    {
        return timImageElem;
    }

    public void setTimImageElem(TIMImageElem timImageElem)
    {
        this.timImageElem = timImageElem;
    }

    public TIMLocationElem getTimLocationElem()
    {
        return timLocationElem;
    }

    public void setTimLocationElem(TIMLocationElem timLocationElem)
    {
        this.timLocationElem = timLocationElem;
    }

    public TIMProfileSystemElem getTimProfileSystemElem()
    {
        return timProfileSystemElem;
    }

    public void setTimProfileSystemElem(TIMProfileSystemElem timProfileSystemElem)
    {
        this.timProfileSystemElem = timProfileSystemElem;
    }

    public TIMSNSSystemElem getTimSnsSystemElem()
    {
        return timSnsSystemElem;
    }

    public void setTimSnsSystemElem(TIMSNSSystemElem timSnsSystemElem)
    {
        this.timSnsSystemElem = timSnsSystemElem;
    }

    public TIMSoundElem getTimSoundElem()
    {
        return timSoundElem;
    }

    public void setTimSoundElem(TIMSoundElem timSoundElem)
    {
        this.timSoundElem = timSoundElem;
    }

    public TIMTextElem getTimTextElem()
    {
        return timTextElem;
    }

    public void setTimTextElem(TIMTextElem timTextElem)
    {
        this.timTextElem = timTextElem;
    }

    public TIMVideoElem getTimVideoElem()
    {
        return timVideoElem;
    }

    public void setTimVideoElem(TIMVideoElem timVideoElem)
    {
        this.timVideoElem = timVideoElem;
    }

    public <T extends CustomMsg> T parseToModel(Class<T> clazz)
    {
        T model = null;
        try
        {
            String json = new String(timCustomElem.getData(), "UTF-8");
            LogUtil.i(json);
            model = SDJsonUtil.json2Object(json, clazz);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return model;
    }
}
