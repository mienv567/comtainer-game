package com.fanwe.live.activity.room;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.activity.info.LiveInfo;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.fanwe.live.model.custommsg.CustomMsgCreaterQuit;
import com.fanwe.live.model.custommsg.CustomMsgOnlineNumber;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.model.custommsg.MsgModel;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.av.TIMAvManager;
import com.umeng.socialize.UMShareListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播间基类
 * <p/>
 * Created by Administrator on 2016/8/4.
 */
public class LiveActivity extends BaseActivity implements LiveInfo {
    /**
     * 房间id(int)
     */
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    /**
     * 讨论组id(String)
     */
    public static final String EXTRA_GROUP_ID = "extra_group_id";
    /**
     * 主播identifier(String)
     */
    public static final String EXTRA_CREATER_ID = "extra_creater_id";

    /**
     * 话题id(int)
     */
    public static final String EXTRA_TOPIC_ID = "extra_topic_id";

    // 七牛sdk相关

    // 直播间内的角色
    public static final String EXTRA_RTC_ROLE = "extra_rtc_role";
    public static final int RTC_ROLE_ANCHOR = 0x01; // 主播
    public static final int RTC_ROLE_VICE_ANCHOR = 0x02; // 连麦观众
    public static final int RTC_ROLE_AUDIENCE = 0x03; // 普通观众
    public static final String EXTRA_SWCODEC = "swcodec";
    public static final String EXTRA_ORIENTATION = "orientation";

    public int mCurrentCamFacingIndex; // 当前摄像头id
    public boolean isSwCodec;
    public boolean isLandscape;
    private CameraStreamingSetting.CAMERA_FACING_ID facingId;

    @Override
    protected void onResume() {
        super.onResume();
//        sdkOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sdkOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sdkOnStop();
    }

    @Override
    protected void onDestroy() {
        sdkOnDestroy();
        super.onDestroy();
    }

    public int getmRole() {
        return mRole;
    }

    public void setmRole(int mRole) {
        this.mRole = mRole;
    }

    /**
     * 在当前房间里的角色:主播/连麦观众/普通观众
     */
    protected int mRole; // 进入推流房间有两种角色,主播,连麦观众

    /**
     * 房间id
     */
    private int roomId;
    /**
     * 群聊id
     */
    private String strGroupId;
    /**
     * 主播id
     */
    private String strCreaterId;
    /**
     * 当前用户id
     */
    protected String strUserId;
    /**
     * 话题id
     */
    private int topicId = 0;    //录制
    protected boolean isRecording = false;
    protected boolean isInStopRecord = false;

    //旁路直播
    protected boolean isPushing = false;
    protected boolean isInStopPush = false;

    //im
    private boolean isJoinImSuccess = false;
    private boolean canSendViewerJoinMsg = false;
    private boolean canSendViewerQuitMsg = false;

    protected Long streamChannelId;
    protected BigInteger streamChannelIdBt;
    protected List<String> listRecord;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏

        roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        strGroupId = getIntent().getStringExtra(EXTRA_GROUP_ID);
        strCreaterId = getIntent().getStringExtra(EXTRA_CREATER_ID);
        topicId = getIntent().getIntExtra(EXTRA_TOPIC_ID, 0);
        strUserId = UserModelDao.query().getUserId();

        // 七牛相关
        mRole = getIntent().getIntExtra(EXTRA_RTC_ROLE, RTC_ROLE_AUDIENCE);
        isSwCodec = getIntent().getBooleanExtra(EXTRA_SWCODEC, true);
        isLandscape = getIntent().getBooleanExtra(EXTRA_ORIENTATION, false);

//        setRequestedOrientation(isLandscape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CameraStreamingSetting.CAMERA_FACING_ID facingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = facingId.ordinal();

    }

    /**
     * 七牛 获取摄像头的id
     *
     * @return
     */
    protected static CameraStreamingSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏

    }

    @Override
    public String getGroupId() {
        if (strGroupId == null) {
            strGroupId = "";
        }
        return strGroupId;
    }

    public void setGroupId(String groupId) {
        this.strGroupId = groupId;
    }

    @Override
    public String getCreaterId() {
        if (strCreaterId == null) {
            strCreaterId = "";
        }
        return strCreaterId;
    }

    public void setCreaterId(String createrId) {
        this.strCreaterId = createrId;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    @Override
    public List<String> getListRecord() {
        return listRecord;
    }

    @Override
    public App_get_videoActModel getRoomInfo() {
        //子类实现
        return null;
    }

    @Override
    public boolean isPrivate() {
        //子类实现
        return false;
    }

    @Override
    public boolean isCreater() {
        //子类实现
        return false;
    }

    @Override
    public void openShare(UMShareListener listener) {
        //子类实现
    }

    @Override
    public void openSendMsg(String content) {
        // 子类实现
    }

    @Override
    public String getCurrentGuestId() {
        //子类实现
        return null;
    }

    /**
     * 狼人杀相关事件
     *
     * @param msg
     */
    protected void onMsgLRS(MsgModel msg) {

    }

    public void onEventMainThread(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (event.msg.isLocalPost()) {

        } else {
            if (peer.equals(getGroupId())) {
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_RED_ENVELOPE) {
                    onMsgRedEnvelope(event.msg);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_END_VIDEO) {
                    LogUtil.e("LiveActivity接受到LiveConstant.CustomMsgType.MSG_END_VIDEO");
                    onMsgEndVideo(event.msg);
                } else if (event.msg.isAuctionMsg()) {
                    onMsgAuction(event.msg);
                } else if (LiveConstant.CustomMsgType.MSG_ONLINE_NUM == event.msg.getCustomMsgType()) {
                    CustomMsgOnlineNumber customMsgOnlineNumber = event.msg.getCustomMsgOnlineNumber();
                    onMsgUpdateViewerNumber(customMsgOnlineNumber);
                }
            } else {
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_STOP_LIVE) {
                    onMsgStopLive(event.msg);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_INVITE_VIDEO) {
                    onMsgInviteVideo(event.msg);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_ACCEPT_VIDEO) {
                    onMsgAcceptVideo(event.msg);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_REJECT_VIDEO) {
                    onMsgRejectVideo(event.msg);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_CREATER_STOP_VIDEO) {
                    onMsgStopVideo(event.msg);
                }

                if (event.msg.isPrivateMsg()) {
                    onMsgPrivate(event.msg);
                }
            }
        }
        if (LiveConstant.CustomMsgType.MSG_LRS == event.msg.getCustomMsgType()) {
            onMsgLRS(event.msg);
        }
    }

    /**
     * 更新在线观众数量
     */
    protected void onMsgUpdateViewerNumber(CustomMsgOnlineNumber onlineNumber) {
        //子类实现
    }

    /**
     * 跑马灯
     *
     * @param msg
     */
    private void onMsgMarquee(MsgModel msg) {

    }

    /**
     * 退出登录
     *
     * @param event
     */
    public void onEventMainThread(EUnLogin event) {
        //子类实现
    }

    /**
     * 帐号异地登录
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event) {
        //子类实现
    }

    /**
     * 电话监听
     *
     * @param event
     */
    public void onEventMainThread(EOnCallStateChanged event) {
        //子类实现
    }


    /**
     * 关闭直播，主播和观众都要退出房间
     *
     * @param msg
     */
    protected void onMsgStopLive(MsgModel msg) {
        //子类实现
    }

    /**
     * 连麦请求
     *
     * @param msg
     */
    protected void onMsgInviteVideo(MsgModel msg) {
        //子类实现
    }

    /**
     * 接受连麦
     *
     * @param msg
     */
    protected void onMsgAcceptVideo(MsgModel msg) {
        //子类实现
    }

    /**
     * 拒绝连麦
     *
     * @param msg
     */
    protected void onMsgRejectVideo(MsgModel msg) {
        //子类实现
    }

    /**
     * 结束连麦
     *
     * @param msg
     */
    protected void onMsgStopVideo(MsgModel msg) {
        //子类实现
    }

    /**
     * 直播结束
     *
     * @param msg
     */
    protected void onMsgEndVideo(MsgModel msg) {
        //子类实现
    }

    /**
     * 私聊消息
     *
     * @param msg
     */
    protected void onMsgPrivate(MsgModel msg) {
        //子类实现
    }

    /**
     * 红包消息
     *
     * @param msg
     */
    protected void onMsgRedEnvelope(MsgModel msg) {
        //子类实现
    }

    protected void onMsgAuction(MsgModel msg) {
        //子类实现
    }

    protected void initIM() {
        //子类实现
    }

    protected void destroyIM() {
        //子类实现
    }

    public boolean isJoinImSuccess() {
        return isJoinImSuccess;
    }

    /**
     * 创建聊天组
     *
     * @param groupName
     * @param listener
     */
    protected void createGroup(String groupName, final TIMValueCallBack<String> listener) {
        if (!isJoinImSuccess) {
            TIMGroupManager.getInstance().createAVChatroomGroup(groupName, new TIMValueCallBack<String>() {

                @Override
                public void onSuccess(String groupId) {
                    LogUtil.i("create im success");
                    strGroupId = groupId;
                    isJoinImSuccess = true;
                    if (listener != null) {
                        listener.onSuccess(groupId);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtil.i("create im error");
                    isJoinImSuccess = false;
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 加入聊天组
     *
     * @param groupId
     * @param listener
     */
    protected void joinGroup(final String groupId, final TIMCallBack listener) {
        if (!isJoinImSuccess) {
            IMHelper.applyJoinGroup(groupId, "申请加入", new TIMCallBack() {

                @Override
                public void onSuccess() {
                    LogUtil.i("join im success:" + groupId);
                    strGroupId = groupId;
                    isJoinImSuccess = true;
                    canSendViewerJoinMsg = true;
                    canSendViewerQuitMsg = true;
                    if (listener != null) {
                        listener.onSuccess();
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtil.i("join im error:" + groupId + "," + code + "," + desc);
                    isJoinImSuccess = false;
                    canSendViewerJoinMsg = false;
                    canSendViewerQuitMsg = false;
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    protected void clearGroupId() {
        strGroupId = "";
    }

    /**
     * 退出聊天组
     *
     * @param groupId
     * @param listener
     */
    protected void quitGroup(final String groupId, final TIMCallBack listener) {
        if (isJoinImSuccess) {
            isJoinImSuccess = false;
            IMHelper.quitGroup(groupId, new TIMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtil.i("quit im success");
                    isJoinImSuccess = false;
                    if (listener != null) {
                        listener.onSuccess();
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtil.i("quit im error");
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 发送加入消息
     *
     * @param groupId
     * @param listener
     */
    protected void sendViewerJoinMsg(final String groupId, final TIMValueCallBack<TIMMessage> listener) {
        if (canSendViewerJoinMsg) {
            canSendViewerJoinMsg = false;
            final CustomMsgViewerJoin msg = new CustomMsgViewerJoin();
            IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 发送退出消息
     */
    protected void sendViewerQuitMsg(final String groupId, final TIMValueCallBack<TIMMessage> listener) {
        if (canSendViewerQuitMsg) {
            canSendViewerQuitMsg = false;
            CustomMsgViewerQuit msg = new CustomMsgViewerQuit();
            IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    LogUtil.e("send quit group success");
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtil.e("send quit group error");
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 发送主播离开消息
     *
     * @param groupId
     * @param listener
     */
    protected void sendCreaterLeaveMsg(String groupId, final TIMValueCallBack<TIMMessage> listener) {
        if (isJoinImSuccess) {
            IMHelper.sendMsgGroup(groupId, new CustomMsgCreaterLeave(), new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 发送主播回来消息
     *
     * @param groupId
     * @param listener
     */
    protected void sendCreaterComebackMsg(String groupId, final TIMValueCallBack<TIMMessage> listener) {
        if (isJoinImSuccess) {
            IMHelper.sendMsgGroup(groupId, new CustomMsgCreaterComeback(), new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    /**
     * 发送主播退出消息
     *
     * @param groupId
     * @param listener
     */
    protected void sendCreaterQuitMsg(final String groupId, final TIMValueCallBack<TIMMessage> listener) {
        if (isJoinImSuccess) {
            isJoinImSuccess = false;
            IMHelper.sendMsgGroup(groupId, new CustomMsgCreaterQuit(), new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    LogUtil.e("send quit group success");
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtil.e("send quit group error");
                    if (listener != null) {
                        listener.onError(code, desc);
                    }
                }
            });
        }
    }

    protected TIMAvManager.RecordParam newRecordParam() {
        TIMAvManager.RecordParam recordParam = TIMAvManager.getInstance().new RecordParam();
        return recordParam;
    }

    protected TIMAvManager.StreamParam newStreamParam() {
        TIMAvManager.StreamParam streamParam = TIMAvManager.getInstance().new StreamParam();
        return streamParam;
    }

    protected TIMAvManager.RoomInfo newRoomInfo() {
        TIMAvManager.RoomInfo roomInfo = TIMAvManager.getInstance().new RoomInfo();
        roomInfo.setRelationId(roomId);
        roomInfo.setRoomId(roomId);
        return roomInfo;
    }

    /**
     * 开始录制
     */
    protected void startRecord(TIMAvManager.RecordParam param, final TIMCallBack listener) {
        if (isRecording) {
            return;
        }

        TIMAvManager.getInstance().requestMultiVideoRecorderStart(newRoomInfo(), param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.i("record start error");
                if (listener != null) {
                    listener.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                LogUtil.i("record start success");
                isRecording = true;
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
    }

    /**
     * 结束录制
     */
    protected void stopRecord(final TIMValueCallBack<List<String>> listener) {
        try {
            if (isRecording) {
                isInStopRecord = true;
                TIMAvManager.RoomInfo roomInfo = newRoomInfo();

                TIMAvManager.getInstance().requestMultiVideoRecorderStop(roomInfo, new TIMValueCallBack<List<String>>() {
                    @Override
                    public void onError(int i, String s) {
                        LogUtil.e("record stop error");
                        isInStopRecord = false;
                        if (listener != null) {
                            listener.onError(i, s);
                        }
                    }

                    @Override
                    public void onSuccess(List<String> files) {
                        LogUtil.e("record stop success");
                        isInStopRecord = false;
                        isRecording = false;
                        listRecord = files;
                        if (listener != null) {
                            listener.onSuccess(files);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始旁路直播
     */
    protected void startPushStream(TIMAvManager.StreamParam param, final TIMValueCallBack<TIMAvManager.StreamRes> listener) {
        if (isPushing) {
            return;
        }
        TIMAvManager.getInstance().requestMultiVideoStreamerStart(newRoomInfo(), param, new TIMValueCallBack<TIMAvManager.StreamRes>() {

            @Override
            public void onSuccess(TIMAvManager.StreamRes res) {
                LogUtil.i("push start success");
                isPushing = true;
                streamChannelId = res.getChnlId();
                streamChannelIdBt = BigInteger.valueOf(streamChannelId);
                if (streamChannelId < 0) {
                    streamChannelIdBt = streamChannelIdBt.add(BigInteger.ZERO.flipBit(64));
                }

                if (listener != null) {
                    listener.onSuccess(res);
                }
            }

            @Override
            public void onError(int code, String desc) {
                LogUtil.i("push start error");
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 结束旁路直播
     */
    protected void stopPushStream(final TIMCallBack listener) {
        try {
            if (isPushing) {
                isInStopPush = true;
                TIMAvManager.RoomInfo roomInfo = newRoomInfo();

                List<Long> list = new ArrayList<Long>();
                list.add(streamChannelId);

                TIMAvManager.getInstance().requestMultiVideoStreamerStop(roomInfo, list, new TIMCallBack() {

                    @Override
                    public void onSuccess() {
                        LogUtil.e("push stop success");
                        isInStopPush = false;
                        isPushing = false;
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtil.e("push stop error");
                        isInStopPush = false;
                        if (listener != null) {
                            listener.onError(code, desc);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //sdk
    protected void sdkEnableFlash(boolean enable) {
        //子类实现
    }

    protected void sdkEnableAudioDataVolume(boolean enable) {
        //子类实现
    }

    protected void sdkEnableMic(boolean enable) {
        //子类实现
    }

    protected void sdkEnableBackCamera() {
        //子类实现
    }

    protected void sdkEnableLastCamera() {
        //子类实现
    }

    protected void sdkEnableFrontCamera() {
        //子类实现
    }

    protected void sdkDisableCamera() {
        //子类实现
    }

    protected void sdkSwitchCamera() {
        //子类实现
    }

    protected void sdkEnableBeauty(boolean enable) {
        //子类实现
    }

    protected void sdkOnPause() {
        //子类实现
    }

    protected void sdkOnResume() {
        //子类实现
    }

    protected void sdkOnStop() {
        //子类实现
    }

    protected void sdkOnDestroy() {
        //子类实现
    }
}
