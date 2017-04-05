package com.fanwe.live.activity.room;

import android.content.Intent;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.dialog.AuctionCreateAuctionDialog;
import com.fanwe.auction.event.ECreateAuctionSuccess;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.umeng.UmengSocialManager;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.listener.SDNetStateListener;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveSongChooseActivity;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.appview.room.RoomBottomView;
import com.fanwe.live.appview.room.RoomCreaterBottomView;
import com.fanwe.live.appview.room.RoomCreaterFinishView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveCreaterFinishDialog;
import com.fanwe.live.event.ECreateLiveSuccess;
import com.fanwe.live.event.ELRSSpeakPass;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_video_cstatusActModel;
import com.fanwe.live.model.LiveCreaterMenuModel;
import com.fanwe.live.model.RoomShareModel;
import com.fanwe.live.pop.LiveCreaterMenuPop;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCUserEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.net.URISyntaxException;

import static com.fanwe.library.utils.SDToast.showToast;

/**
 * 直播间主播布局和部分逻辑实现
 * <p/>
 * Created by Administrator on 2016/8/4.
 */
public class LiveLayoutCreaterActivity extends LiveLayoutActivity {
    /**
     * 1：主播界面被强制关闭后回来(int)
     */
    public static final String EXTRA_IS_CLOSED_BACK = "EXTRA_IS_CLOSED_BACK";
    /**
     * 1：主播界面被强制关闭后回来
     */
    protected int isClosedBack;

    /**
     * 主播是否离开
     */
    protected boolean isCreaterLeave = false;

    /**
     * 是否静音模式
     */
    protected boolean isMuteMode = false;

    protected RoomCreaterBottomView roomCreaterBottomView;
    //    protected RoomPlayMusicView roomPlayMusicView;
    private RoomCreaterFinishView roomCreaterFinishView;

    //    private ViewGroup fl_live_play_music; // 播放音乐
    protected LiveCreaterMenuPop popMenu;

    private AuctionCreateAuctionDialog dialogCreateAuction;

    // 七牛相关
    public static final String EXTRA_PUBLISHADDR = "EXTRA_PUBLISHADDR";
    private static final int MESSAGE_ID_RECONNECTING = 0x01;
    protected String publishAddr;
    protected RTCConferenceOptions options;
    protected RTCMediaStreamingManager mRTCStreamingManager; // 推拉流管理
    protected CameraStreamingSetting cameraStreamingSetting; // 摄像头设置配置
    protected StreamingProfile mStreamingProfile;
    //    protected WatermarkSetting watermarksetting; // 七牛支持视频流加水印,但是我们的程序没有要求
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;

    protected boolean mIsPublishStreamStarted = false;
    protected boolean mIsInReadyState = false;

    //    protected TextView mStatTextView; // 显示推流的实时数据
    protected TextView mStatusTextView;
    protected boolean isConferenceReady; //  判断连麦窗是否就绪

    /**
     * 开始推流
     *
     * @return
     */
    protected boolean startPublishStreaming() {
        if (mIsPublishStreamStarted) {
            return true;
        }
        if (!mIsInReadyState) {
            showToast(getString(R.string.stream_state_not_ready), Toast.LENGTH_SHORT);
            return false;
        }
        return startPublishStreamingInternal();
    }

    /**
     * 开始推流
     *
     * @return
     */
    private boolean startPublishStreamingInternal() {

        if (publishAddr == null) {
            showToast(getString(R.string.can_not_get_room_info_push_address) + " !", Toast.LENGTH_SHORT);
            return false;
        }
        //
        //        try {
        //            if (StreamUtils.IS_USING_STREAMING_JSON) {
        //                mStreamingProfile.setStream(new StreamingProfile.Stream(new JSONObject(publishAddr)));
        //            } else {
        //                mStreamingProfile.setPublishUrl(publishAddr);
        //            }
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //            dismissProgressDialog();
        //            showToast("无效的推流地址 !", Toast.LENGTH_SHORT);
        //            return false;
        //        } catch (URISyntaxException e) {
        //            e.printStackTrace();
        //            dismissProgressDialog();
        //            showToast("无效的推流地址 !", Toast.LENGTH_SHORT);
        //            return false;
        //        }

        // 七牛支持json或者url的推流格式,为了方便统一使用url
        try {
            mStreamingProfile.setPublishUrl(publishAddr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            dismissProgressDialog();
            showToast(getString(R.string.invalid_push_address), Toast.LENGTH_SHORT);
            return false;
        }

        mRTCStreamingManager.setStreamingProfile(mStreamingProfile);
        if (!mRTCStreamingManager.startStreaming()) {
            showToast(getString(R.string.failed_to_start_streaming), Toast.LENGTH_SHORT);
            return false;
        }
        //        showToast(getString(R.string.start_streaming), Toast.LENGTH_SHORT);
        mIsPublishStreamStarted = true;
        /**
         * Because `startPublishStreaming` need a long time in some weak network
         * So we should check if the activity paused.
         */
        if (mIsActivityPaused) {
            stopPublishStreaming();
        }
        return true;
    }

    /**
     * 停止推流
     *
     * @return
     */
    protected boolean stopPublishStreaming() {
        if (!mIsPublishStreamStarted) {
            return true;
        }
        mRTCStreamingManager.stopStreaming();
        mIsPublishStreamStarted = false;
        //        showToast(getString(R.string.stop_streaming), Toast.LENGTH_SHORT);
        return false;
    }


    /**
     * 流状态监听
     */
    protected StreamingStateChangedListener mStreamingStateChangedListener = new StreamingStateChangedListener() {
        @Override
        public void onStateChanged(final StreamingState state, Object o) {
            switch (state) {
                case PREPARING:
                    //                    setStatusText(getString(R.string.preparing));
                    LogUtil.e("onStateChanged state:" + "preparing");
                    break;
                case READY:
                    mIsInReadyState = true;
                    //                    setStatusText("推流准备就绪");
                    LogUtil.e("onStateChanged state:" + "ready");
                    startPublishStreaming();
                    break;
                case CONNECTING:
                    LogUtil.e("onStateChanged state:" + "connecting");
                    break;
                case STREAMING:
                    //                    setStatusText(getString(R.string.streaming));
                    LogUtil.e("onStateChanged state:" + "streaming");
                    break;
                case SHUTDOWN:
                    mIsInReadyState = true;
                    //                    setStatusText(getString(R.string.ready));
                    LogUtil.e("onStateChanged state:" + "shutdown");
                    break;
                case UNKNOWN:
                    LogUtil.e("onStateChanged state:" + "unknown");
                    break;
                case SENDING_BUFFER_EMPTY:
                    LogUtil.e("onStateChanged state:" + "sending buffer empty");
                    break;
                case SENDING_BUFFER_FULL:
                    LogUtil.e("onStateChanged state:" + "sending buffer full");
                    break;
                case OPEN_CAMERA_FAIL:
                    LogUtil.e("onStateChanged state:" + "open camera failed");
                    showToast(getString(R.string.failed_open_camera), Toast.LENGTH_SHORT);
                    break;
                case AUDIO_RECORDING_FAIL:
                    LogUtil.e("onStateChanged state:" + "audio recording failed");
                    LogUtil.e(getString(R.string.failed_open_microphone));
                    break;
                case IOERROR:
                    /**
                     * Network-connection is unavailable when `startStreaming`.
                     * You can do reconnecting or just finish the streaming
                     */
                    LogUtil.e("onStateChanged state:" + "io error");
                    showToast(getString(R.string.io_error), Toast.LENGTH_SHORT);
                    sendReconnectMessage();
                    stopPublishStreaming();
                    break;
                case DISCONNECTED:
                    /**
                     * Network-connection is broken after `startStreaming`.
                     * You can do reconnecting in `onRestartStreamingHandled`
                     */
                    LogUtil.e("onStateChanged state:" + "disconnected");
                    //                    setStatusText(getString(R.string.disconnected));
                    // we will process this state in `onRestartStreamingHandled`
                    break;
            }
        }
    };

    /**
     * 发送重连消息
     */
    private void sendReconnectMessage() {
        showToast(getString(R.string.is_reconnecting), Toast.LENGTH_SHORT);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 2000);
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING || mIsActivityPaused || !mIsPublishStreamStarted) {
                return;
            }
            SDOtherUtil.checkNet(LiveLayoutCreaterActivity.this, new SDNetStateListener() {
                @Override
                public void onWifi() {
                    LogUtil.e("do reconnecting ...");
                    mRTCStreamingManager.startStreaming();
                }

                @Override
                public void onNone() {
                    sendReconnectMessage();
                    return;
                }

                @Override
                public void onMobile() {
                    LogUtil.e("当前使用移动网络,用户钱多直接推流 do reconnecting ...");
                    mRTCStreamingManager.startStreaming();
                }
            });


        }
    };


    //    protected void setStatusText(final String status) {
    //        runOnUiThread(new Runnable() {
    //            @Override
    //            public void run() {
    //                mStatusTextView.setText(status);
    //            }
    //        });
    //    }


    /**
     * 连麦窗状态
     */
    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    // You must `StartConference` after `Ready`
                    isConferenceReady = true;
                    //                    SDToast.showToast("RTCConferenceStateChangedListener" + getString(R.string.ready), Toast.LENGTH_SHORT);
                    break;
                case CONNECT_FAIL:
                    isConferenceReady = false;
                    SDToast.showToast(getString(R.string.failed_to_connect_rtc_server), Toast.LENGTH_SHORT);
                    //                    finish();
                    break;
                case VIDEO_PUBLISH_FAILED:
                    isConferenceReady = false;

                case AUDIO_PUBLISH_FAILED:
                    isConferenceReady = false;

                    SDToast.showToast(getString(R.string.failed_to_publish_av_to_rtc) + extra, Toast.LENGTH_SHORT);
                    finish();
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    Log.i("invite", "Creater 成功加入到连麦房间");
                    //                    SDToast.showToast(getString(R.string.success_publish_video_to_rtc), Toast.LENGTH_SHORT);
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    //                    SDToast.showToast(getString(R.string.success_publish_audio_to_rtc), Toast.LENGTH_SHORT);
                    break;
                case USER_JOINED_AGAIN:
                    isConferenceReady = false;
                    SDToast.showToast(getString(R.string.user_join_other_where), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case USER_KICKOUT_BY_HOST:
                    //                    SDToast.showToast(getString(R.string.user_kickout_by_host), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case OPEN_CAMERA_FAIL:
                    isConferenceReady = false;

                    SDToast.showToast(getString(R.string.failed_open_camera), Toast.LENGTH_SHORT);
                    break;
                case AUDIO_RECORDING_FAIL:
                    isConferenceReady = false;
                    LogUtil.e(getString(R.string.failed_open_microphone));
                    break;
                default:
                    return;
            }
        }
    };

    /**
     * 用户进入退出直播间方法回调
     */
    private RTCUserEventListener mRTCUserEventListener = new RTCUserEventListener() {
        @Override
        public void onUserJoinConference(String remoteUserId) {
            Log.i("invite", "onUserJoinConference: " + remoteUserId);
            LogUtil.e("onUserJoinConference: " + remoteUserId);
            if (null != userEventListerner) {
                userEventListerner.onUserJoinConference(remoteUserId);
            }
        }

        @Override
        public void onUserLeaveConference(String remoteUserId) {
            Log.i("invite", "onUserLeaveConference: " + remoteUserId);
            LogUtil.e("onUserLeaveConference: " + remoteUserId);
            if (null != userEventListerner) {
                userEventListerner.onUserLeaveConference(remoteUserId);
            }
        }
    };


    private RTCRemoteWindowEventListener mRTCRemoteWindowEventListener = new RTCRemoteWindowEventListener() {
        @Override
        public void onRemoteWindowAttached(RTCVideoWindow window, String remoteUserId) {
            LogUtil.e("onRemoteWindowAttached: " + remoteUserId);
        }

        @Override
        public void onRemoteWindowDetached(RTCVideoWindow window, String remoteUserId) {
            LogUtil.e("onRemoteWindowDetached: " + remoteUserId);
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        isClosedBack = getIntent().getIntExtra(EXTRA_IS_CLOSED_BACK, 0);

        // 七牛相关
        RTCMediaStreamingManager.init(getApplication());
        /**
         * Step 3: config camera settings
         */
        cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraFacingId(chooseCameraFacingId())
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true) // Using sdk built in face beauty algorithm
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)) // sdk built in face beauty settings
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off

    }


    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        //        fl_live_play_music = (ViewGroup) view.findViewById(R.id.fl_live_play_music);

        //播放音乐
        addLivePlayMusic();

        // 七牛相关
        afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        if (null != afl) {
            afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        }
        qn_video_glview = (GLSurfaceView) view.findViewById(R.id.qn_video_glview); // 七牛摄像头预览view


        /**
         * Step 4: create streaming manager and set listeners
         */
        AVCodecType codecType = isSwCodec ? AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC : AVCodecType.HW_VIDEO_YUV_AS_INPUT_WITH_HW_AUDIO_CODEC;
        mRTCStreamingManager = new RTCMediaStreamingManager(getApplicationContext(), afl, qn_video_glview, codecType);
        mRTCStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCStreamingManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
        mRTCStreamingManager.setUserEventListener(mRTCUserEventListener);
        mRTCStreamingManager.setDebugLoggingEnabled(false);

        /**
         * Step 5: set conference options
         */
        options = new RTCConferenceOptions();
        if (mRole == RTC_ROLE_ANCHOR) {
            // anchor should use a bigger size, must equals to `StreamProfile.setPreferredVideoEncodingSize` or `StreamProfile.setEncodingSizeLevel`
            // RATIO_16_9 & VIDEO_ENCODING_SIZE_HEIGHT_480 means the output size is 848 x 480
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
            // anchor can use a smaller conference bitrate in order to reserve enough bandwidth for rtmp streaming
            options.setVideoBitrateRange(100 * 1000, 300 * 1000);
            // 20 fps is enough
            options.setVideoEncodingFps(20);
        } else {
            // vice anchor can use a smaller size
            // RATIO_4_3 & VIDEO_ENCODING_SIZE_HEIGHT_240 means the output size is 320 x 240
            // 4:3 looks better in the mix frame
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_4_3);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_240);
            // vice anchor can use a higher conference bitrate for better image quality
            options.setVideoBitrateRange(300 * 1000, 800 * 1000);
            // 20 fps is enough
            options.setVideoEncodingFps(20);
        }
        options.setHWCodecEnabled(!isSwCodec);
        mRTCStreamingManager.setConferenceOptions(options);
    }


    @Override
    protected void onDestroy() {
        if (popMenu != null) {
            SDEventManager.unregister(popMenu);
        }
        super.onDestroy();
    }

    /**
     * 添加播放音乐
     */
    protected void addLivePlayMusic() {
        //        roomPlayMusicView = new RoomPlayMusicView(this);
        //        replaceView(fl_live_play_music, roomPlayMusicView);
    }

    @Override
    protected void addLiveBottomMenu() {
        if (roomCreaterBottomView == null) {
            roomCreaterBottomView = new RoomCreaterBottomView(this);
            roomCreaterBottomView.setClickListener(bottomClickListener);
        }
        replaceView(fl_live_bottom_menu, roomCreaterBottomView);
    }

    @Override
    protected RoomBottomView getBottomView() {
        return roomCreaterBottomView;
    }

    /**
     * 底部菜单点击监听
     */
    private RoomCreaterBottomView.ClickListener bottomClickListener = new RoomCreaterBottomView.ClickListener() {
        @Override
        public void onClickBottomOpenSend(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomOpenSend(v);
        }

        @Override
        public void onClickBottomPodcastOrder(View v) {
            LiveLayoutCreaterActivity.this.onCLickBottomPodcastOrder(v);
        }

        @Override
        public void onCLickBottomCreateAuction(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomCreateAuction(v);
        }

        @Override
        public void onClickBottomMsg(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomMsg(v);
        }

        @Override
        public void onClickBottomMusic(View v) {
            //            七牛临时,没有接音乐相关的api,现阶段点击音乐不执行任何操作
            //            LiveLayoutCreaterActivity.this.onClickBottomMusic(v);
            //            MobclickAgent.onEvent(LiveLayoutCreaterActivity.this, "live_music");
        }

        @Override
        public void onClickBottomLrs(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomLrs(v);
        }

        @Override
        public void onClickBottomLrsChannel(View v) {

        }

        @Override
        public void onClickBottomGamePass(View v) {
            //过麦 - 主播不需要取消连麦  只需要通知到服务器即可
            CommonInterface.requestLRSPassSpeak(getRoomId(), null);
            SDEventManager.post(new ELRSSpeakPass());
        }

        @Override
        public void onClickBottomGameOut(View v) {
            //退出游戏
            LiveCreaterFinishDialog dialog = new LiveCreaterFinishDialog(LiveLayoutCreaterActivity.this);
            dialog.setTextGravity(Gravity.CENTER);
            dialog.setTextContent(getString(R.string.if_ensure_out_game));
            dialog.setTextCancel(getString(R.string.no));
            dialog.setTextConfirm(getString(R.string.yes));
            dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

                @Override
                public void onDismiss(SDDialogCustom dialog) {
                }

                @Override
                public void onClickConfirm(View v, SDDialogCustom dialog) {
                    CommonInterface.requestLRSOut(getRoomId(), null);
                }

                @Override
                public void onClickCancel(View v, SDDialogCustom dialog) {

                }
            });
            dialog.show();
        }

        @Override
        public void onClickBottomGameOpenClose(View v, boolean isShow) {
            //控制玩家信息以及倒计时的显示
            roomLRSUserView.showOrHide(isShow);
            roomInfoView.showOrHideGameTime(isShow);
        }

        @Override
        public void onClickBottomMore(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomMore(v);
        }

        @Override
        public void onClickBottomClose(View v) {
            LiveLayoutCreaterActivity.this.onClickBottomClose(v);
        }

        @Override
        public void onClickChannelPublic(View v) {

        }

        @Override
        public void onClickChannelWolf(View v) {

        }

        @Override
        public void onClickChannelProgress(View v) {

        }

        @Override
        public void onClickChannelRule(View v) {

        }
    };

    //星店订单
    protected void onCLickBottomPodcastOrder(View v) {
        InitActModel initmodel = InitActModelDao.query();
        if (initmodel != null) {
            App_InitH5Model h5Model = initmodel.getH5_url();
            if (h5Model != null) {
                String url_podcast_order = h5Model.getUrl_podcast_order();
                if (!TextUtils.isEmpty(url_podcast_order)) {
                    Intent intent = new Intent(this, LiveWebViewActivity.class);
                    intent.putExtra(LiveWebViewActivity.EXTRA_URL, url_podcast_order);
                    startActivity(intent);
                } else {
                    SDToast.showToast(getString(R.string.url_empty));
                }
            }
        }
    }


    /**
     * 创建竞拍
     *
     * @param v
     */
    protected void onClickBottomCreateAuction(View v) {
        //调用接口，判断创建竞拍是否可用
        showProgressDialog("");
        AuctionCommonInterface.requestCreateAuctionAuthority(new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                //                if(rootModel.isOk()) {
                //                    showCreateAuctionDialog();
                //                } else {
                //                    showErrorMsg(actModel.getError());
                //                }
                showCreateAuctionDialog();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
                if (resp.getThrowable() != null) {
                    SDToast.showToast(resp.getThrowable().toString());
                }

            }
        });

    }

    private void showErrorMsg(String errorMsg) {
        new SDDialogConfirm(this).setTextContent(errorMsg).setTextGravity(Gravity.CENTER).setTextConfirm("确定").setTextCancel("").setmListener(new SDDialogCustom.SDDialogCustomListener() {
            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {

            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                dialog.dismiss();
            }

            @Override
            public void onDismiss(SDDialogCustom dialog) {

            }
        }).show();
    }

    private void showCreateAuctionDialog() {
        if (dialogCreateAuction == null) {
            dialogCreateAuction = new AuctionCreateAuctionDialog(this, AppRuntimeWorker.getPai_virtual_btn(), AppRuntimeWorker.getPai_real_btn());
        }
        dialogCreateAuction.showBottom();
    }

    /**
     * 点击音乐
     *
     * @param v
     */
    protected void onClickBottomMusic(View v) {
        Intent intent = new Intent(LiveLayoutCreaterActivity.this, LiveSongChooseActivity.class);
        startActivity(intent);
    }

    /**
     * 点击更多
     *
     * @param v
     */
    protected void onClickBottomMore(View v) {
        if (popMenu == null) {
            popMenu = new LiveCreaterMenuPop(this);
            SDEventManager.register(popMenu);
            popMenu.setWidth((int) ((float) SDViewUtil.getScreenWidth() * 0.25));
            popMenu.setListenerItemClick(new SDAdapter.ItemClickListener<LiveCreaterMenuModel>() {
                @Override
                public void onClick(int position, LiveCreaterMenuModel item, View view) {
                    switch (position) {
                        case 0: // 分享
                            openShare(null);
                            break;
                        case 1: // 开关闪关灯
                            if (mCurrentCamFacingIndex != Camera.CameraInfo.CAMERA_FACING_BACK) {
                                SDToast.showToast(getString(R.string.front_camera_not_support_flashlight));
                                return;
                            }
                            item.setSelected(!item.isSelected());
                            sdkEnableFlash(item.isSelected());
                            MobclickAgent.onEvent(LiveLayoutCreaterActivity.this, "live_flash");
                            break;
                        case 2: // 切换摄像头
                            item.setSelected(!item.isSelected());
                            //                            sdkSwitchCamera(); // avsdk
                            //                            七牛sdk
                            // 先判断当前是否开启了后置闪光灯
                            if (mCurrentCamFacingIndex == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                if (popMenu.modelflash.isSelected()) {
                                    popMenu.modelflash.setSelected(false);
                                    mRTCStreamingManager.turnLightOff();
                                }
                            }
                            qnSwitchCamera();
                            MobclickAgent.onEvent(LiveLayoutCreaterActivity.this, "live_reversal");
                            break;
                        case 3: // 开关麦克风
                            item.setSelected(!item.isSelected());
                            isMuteMode = !item.isSelected();
                            mRTCStreamingManager.mute(isMuteMode);
                            MobclickAgent.onEvent(LiveLayoutCreaterActivity.this, "live_mic");
                            break;
                        case 4: // 调节美颜
                            item.setSelected(!item.isSelected());
                            boolean isEditMode = AppRuntimeWorker.isBeautyEditMode();
                            if (isEditMode) {
                                showSetBeautyDialog();
                            } else {
                                sdkEnableBeauty(item.isSelected());
                            }
                            MobclickAgent.onEvent(LiveLayoutCreaterActivity.this, "live_beauty");
                            break;

                        default:
                            break;
                    }
                    popMenu.getAdapter().notifyDataSetChanged();
                    popMenu.dismiss();
                }
            });
        }

        SDViewUtil.showPopTop(popMenu, roomCreaterBottomView.getViewBottomMore(), 0);

        SDViewUtil.getViewSize(popMenu.getContentView(), true, 100, new SDViewUtil.SDViewSizeListener() {

            @Override
            public void onResult(View view) {
                SDAnimationUtil.translateInBottom(popMenu.getContentView()).setDuration(200).start();
            }
        });
    }

    /**
     * 七牛sdk切换摄像头逻辑
     */
    protected void qnSwitchCamera() {
        mRTCStreamingManager.turnLightOff();
        mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
        CameraStreamingSetting.CAMERA_FACING_ID facingId;
        if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        }
        mRTCStreamingManager.switchCamera(facingId);
    }

    /**
     * 显示美颜设置窗口
     */
    protected void showSetBeautyDialog() {
        //子类实现
    }

    @Override
    protected SDRequestHandler requestRoomInfo() {
        //        return CommonInterface.requestRoomInfo(getRoomId(), 0, 0, 0, 0, 0, null, null, null, new AppRequestCallback<App_get_videoActModel>() {
        //            @Override
        //            protected void onSuccess(SDResponse resp) {
        //                setRoomInfo(actModel);
        //                if (rootModel.isOk()) {
        //                    onSuccessRequestRoomInfo(actModel);
        //                } else {
        //                    onErrorRequestRoomInfo(actModel);
        //                }
        //            }
        //        });
        try {
            App_get_videoActModel actModel = (App_get_videoActModel) getIntent().getSerializableExtra(LiveCreaterActivity.EXTRA_ROOM_INFO);
            //// TODO: 2017/2/25 暂时跳过这两步，以后再说
            setRoomInfo(actModel);
            onSuccessRequestRoomInfo(actModel);
        } catch (Exception e) {
            LogUtil.e(e + "");
            LogUtil.e("未能获取到房间信息");
        }

        return null;
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);
        String shareType = actModel.getShare_type();
        if (!TextUtils.isEmpty(shareType)) {
            RoomShareModel share = actModel.getShare();
            if (share != null) {
                String title = share.getShareTitle();
                String content = share.getShareContent();
                String imageUrl = share.getShareImageUrl();
                String clickUrl = share.getShareUrl();

                // 弹出分享页面
                if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN.toString())) {
                    UmengSocialManager.shareWeixin(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN_CIRCLE.toString())) {
                    UmengSocialManager.shareWeixinCircle(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QQ.toString())) {
                    UmengSocialManager.shareQQ(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QZONE.toString())) {
                    UmengSocialManager.shareQzone(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.SINA.toString())) {
                    UmengSocialManager.shareSina(title, content, imageUrl, clickUrl, this, null);
                }
            }
        }

        if (actModel.getPai_id() > 0) {
            requestPaiUserGetVideo(actModel.getPai_id());
        }
    }

    @Override
    protected void addLiveFinish() {
        removeView(roomCreaterFinishView);
        roomCreaterFinishView = new RoomCreaterFinishView(this);
        roomCreaterFinishView.setHeadImgUrl(roomInfoView.getHeadImgUrl());
        addView(roomCreaterFinishView);
    }

    protected boolean isClosedBack() {
        return isClosedBack == 1;
    }

    /**
     * 设置房间状态为失败
     */
    protected void requestUpdateLiveStateFail() {
        CommonInterface.requestStartLive(getRoomId(), getGroupId(), null, 0, null);
    }

    /**
     * 设置房间状态为成功
     *
     * @param channelId 推流频道id
     */
    protected void requestUpdateLiveStateSuccess(String channelId) {
        CommonInterface.requestStartLive(getRoomId(), getGroupId(), channelId, 1, null);

        ECreateLiveSuccess event = new ECreateLiveSuccess();
        SDEventManager.post(event);
    }

    /**
     * 设置房间状态主播离开
     */
    protected void requestUpdateLiveStateLeave() {
        CommonInterface.requestStartLive(getRoomId(), getGroupId(), null, 2, null);
    }

    /**
     * 设置房间状态主播回来
     *
     * @param listener
     */
    protected void requestUpdateLiveStateComeback(AppRequestCallback<App_video_cstatusActModel> listener) {
        CommonInterface.requestStartLive(getRoomId(), getGroupId(), null, 3, listener);
    }

    protected void stopMusic() {
        //        if (roomPlayMusicView != null) {
        //            roomPlayMusicView.stopMusic();
        //        }
    }

    @Override
    public boolean isCreater() {
        return true;
    }

    //主播创建竞拍成功后接收事件
    public void onEventMainThread(ECreateAuctionSuccess event) {
        int id = SDTypeParseUtil.getInt(event.pai_id);
        if (id > 0) {
            requestPaiUserGetVideo(id);
        } else {
            SDToast.showToast("主播创建竞拍后pai_id为0");
        }
    }

    //直播间请求拍卖详情
    protected void requestPaiUserGetVideo(int pai_id) {
        AuctionCommonInterface.requestPaiUserGetVideo(pai_id, new AppRequestCallback<App_pai_user_get_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    bindData();
                }
            }

            private void bindData() {
                addLiveAuctionInfo(actModel);
                roomCreaterBottomView.bindData(actModel);
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

}
