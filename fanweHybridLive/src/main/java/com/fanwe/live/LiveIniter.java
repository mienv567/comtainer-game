package com.fanwe.live;

import android.app.Application;
import android.media.MediaPlayer;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.media.player.SDMediaPlayer;
import com.fanwe.library.media.player.SDMediaPlayerListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EImOnConnected;
import com.fanwe.live.event.EImOnDisconnected;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EImOnMemberInfoUpdate;
import com.fanwe.live.event.EImOnMemberUpdate;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.EImOnRefresh;
import com.fanwe.live.event.ESDMediaPlayerStateChanged;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.model.custommsg.TIMMsgModel;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMConnListener;
import com.tencent.TIMConversation;
import com.tencent.TIMGroupMemberUpdateListener;
import com.tencent.TIMGroupTipsElemMemberInfo;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMLogListener;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMRefreshListener;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;
import com.tencent.bugly.imsdk.crashreport.CrashReport;

import java.util.List;

import tencent.tls.platform.TLSHelper;

public class LiveIniter {

    public void init(Application app) {
        CrashReport.initCrashReport(app, String.valueOf(LiveConstant.APP_ID_TENCENT_LIVE), true);

        // 初始化七牛sdk
        /**
         * Step 1: init sdk, you can also move this to MainActivity.onCreate
         */
        RTCMediaStreamingManager.init(app.getApplicationContext());

        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {

            /**
             * 将接收到的IM消息发送到程序的内部各个view中进行相应的显示和处理
             * @param msgModel
             */
            private void postNewMessage(MsgModel msgModel) {
                EImOnNewMessages event = new EImOnNewMessages();
                event.msg = msgModel;
                SDEventManager.post(event);

                if (msgModel.isPrivateMsg()) {
                    IMHelper.postERefreshMsgUnReaded();
                }
            }

            @Override
            public boolean onNewMessages(final List<TIMMessage> listMessage) {
                if (!SDCollectionUtil.isEmpty(listMessage)) {
                    SDHandlerManager.getBackgroundHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            for (TIMMessage msg : listMessage) {
                                boolean post = true;
                                final TIMMsgModel msgModel = new TIMMsgModel(msg);

                                boolean needDownloadSound = msgModel.checkSoundFile(new TIMValueCallBack<String>() {
                                    @Override
                                    public void onError(int i, String s) {
                                    }

                                    @Override
                                    public void onSuccess(String path) {
                                        msgModel.getCustomMsgPrivateVoice().setPath(path);
                                        postNewMessage(msgModel);
                                    }
                                });
                                if (needDownloadSound) {
                                    post = false;
                                }


                                if (post) {
                                    postNewMessage(msgModel);
                                }
                            }
                        }
                    });
                }
                return false;
            }
        });
        TIMManager.getInstance().setConnectionListener(new TIMConnListener() {

            @Override
            public void onWifiNeedAuth(String arg0) {
            }

            @Override
            public void onDisconnected(int code, String desc) {
                EImOnDisconnected event = new EImOnDisconnected();
                event.code = code;
                event.desc = desc;
                SDEventManager.post(event);
            }

            @Override
            public void onConnected() {
                SDEventManager.post(new EImOnConnected());
            }
        });
        TIMManager.getInstance().setLogListener(new TIMLogListener() {

            @Override
            public void log(int level, String tag, String msg) {

            }
        });
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {

            @Override
            public void onForceOffline() {
                // 被踢下线
                AppRuntimeWorker.logoutIm(false);
                SDEventManager.post(new EImOnForceOffline());
            }

            @Override
            public void onUserSigExpired() {
                // userSig过期
                AppRuntimeWorker.logoutIm(false);
                CommonInterface.requestUsersig(null);
            }
        });
        TIMManager.getInstance().setRefreshListener(new TIMRefreshListener() {

            @Override
            public void onRefresh() {
                // 默认登陆后会异步获取离线消息以及同步资料数据（如果有开启ImSDK存储，可参见关系链资料章节），同步完成后会通过onRefresh回调通知更新界面，用户得到这个消息时，可以刷新界面，比如会话列表的未读等等：
                SDEventManager.post(new EImOnRefresh());
            }

            @Override
            public void onRefreshConversation(List<TIMConversation> list) {

            }
        });
        TIMManager.getInstance().setGroupMemberUpdateListener(new TIMGroupMemberUpdateListener() {

            @Override
            public void onMemberUpdate(String groupId, TIMGroupTipsType type, List<String> listMember) {
                // 群成员变更通知，包括加入群，退出群，踢出群，设置管理员等， 根据回调中的type参数确定变更通知类型
                EImOnMemberUpdate event = new EImOnMemberUpdate();
                event.groupId = groupId;
                event.type = type;
                event.listMember = listMember;
                SDEventManager.post(event);
            }

            @Override
            public void onMemberInfoUpdate(String groupId, List<TIMGroupTipsElemMemberInfo> listMemberInfos) {
                // 群成员资料变更， 如被设置禁言
                EImOnMemberInfoUpdate event = new EImOnMemberInfoUpdate();
                event.groupId = groupId;
                event.listMemberInfos = listMemberInfos;
                SDEventManager.post(event);
            }
        });
        TIMManager.getInstance().setLogPrintEnable(true);
        TIMManager.getInstance().init(app);
        TIMManager.getInstance().setEnv(LiveConstant.LIVE_ENVIRONMENT);
        TLSHelper.getInstance().init(app, LiveConstant.APP_ID_TENCENT_LIVE);
//        QavsdkControl.getInstance().init(app);


        SDMediaPlayer.getInstance().setListener(new SDMediaPlayerListener() {
            private void postStateEvent() {
                SDEventManager.post(new ESDMediaPlayerStateChanged(SDMediaPlayer.getInstance().getState()));
            }

            @Override
            public void onReleased() {
                postStateEvent();
            }

            @Override
            public void onReset() {
                postStateEvent();
            }

            @Override
            public void onInitialized() {
                postStateEvent();
            }

            @Override
            public void onPreparing() {
                postStateEvent();
            }

            @Override
            public void onPrepared() {
                postStateEvent();
            }

            @Override
            public void onPlaying() {
                postStateEvent();
            }

            @Override
            public void onPaused() {
                postStateEvent();
            }

            @Override
            public void onCompletion() {
                postStateEvent();
            }

            @Override
            public void onStopped() {
                postStateEvent();
            }

            @Override
            public void onError(MediaPlayer mp, int what, int extra) {
            }

            @Override
            public void onException(Exception e) {
            }
        });
    }

}
