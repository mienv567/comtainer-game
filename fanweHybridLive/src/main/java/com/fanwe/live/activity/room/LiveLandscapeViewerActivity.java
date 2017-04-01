package com.fanwe.live.activity.room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LRSResultDialog;
import com.fanwe.live.dialog.LiveSmallVideoInfoCreaterDialog;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.event.EEnterOrExitRoomTimeout;
import com.fanwe.live.event.EEnterRoomComplete;
import com.fanwe.live.event.EGLVideoViewVisibilityChange;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.ELRSChannelChange;
import com.fanwe.live.event.ELRSGameStateChange;
import com.fanwe.live.event.ELRSQuiteLianMai;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EOnClickSmallVideo;
import com.fanwe.live.event.ERequestViewComplete;
import com.fanwe.live.event.EScrollChangeRoomComplete;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_get_tokenActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.LRSGameModel;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.fanwe.live.model.custommsg.MsgModel;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCUserEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.sunday.eventbus.SDEventManager;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import static com.fanwe.library.utils.SDToast.showToast;

public class LiveLandscapeViewerActivity extends LiveLayoutViewerActivity {

    private static final long DELAY_JOIN_ROOM = 1000;

    // 七牛互动相关
    protected RTCConferenceOptions options;
    protected RTCMediaStreamingManager mRTCStreamingManager; // 推拉流管理
    protected RTCMediaStreamingManager mRTCAudioStreamingManager; // 音频连麦Manager
    protected CameraStreamingSetting cameraStreamingSetting; // 摄像头设置配置
    protected StreamingProfile mStreamingProfile;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;

    protected boolean mIsInReadyState = false;
    private RTCVideoWindow mRemoteWindowA;
    private RTCVideoWindow mRemoteWindowB;
    private RTCVideoWindow mRemoteWindowC;

    // 七牛sdk相关
    protected GLSurfaceView qn_video_glview; // 七牛视频预览
    protected AspectFrameLayout afl;
    protected boolean mIsActivityPaused = true;
    protected List<String> names;
    private boolean isConferenceReady = false;

    // 七牛纯音频连麦
    private String tooken = "";
    private RTCConferenceOptions audioOptions;
    private MicrophoneStreamingSetting audioSetting;
    private boolean mIsAudioConferenceStarted = false;
    private int isManage = 0;

    /**
     * 横竖屏切换按钮
     */
    Button btnChangeOrientation;

    /**
     * 初始化纯音频连麦相关对象
     */
    protected void initAudioConference() {
        mRTCAudioStreamingManager = new RTCMediaStreamingManager(this, AVCodecType.SW_AUDIO_CODEC);
        mRTCAudioStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCAudioStreamingManager.setUserEventListener(mRTCUserEventListener);
        mRTCAudioStreamingManager.setDebugLoggingEnabled(false);

        audioOptions = new RTCConferenceOptions();
        audioOptions.setHWCodecEnabled(false);
        mRTCAudioStreamingManager.setConferenceOptions(audioOptions);

        audioSetting = new MicrophoneStreamingSetting();
        audioSetting.setBluetoothSCOEnabled(false);

        mRTCAudioStreamingManager.prepare(audioSetting);
        Log.i("invite", "initAudioConference ready");
    }

    /**
     * 观众端纯音频连麦
     */
    protected void startAudioConference() {
        if (mIsAudioConferenceStarted) {
            Log.i("invite", "已经在语音连麦了,直接返回...");
            return;
        }
        CommonInterface.requestToken(getRoomId(), new AppRequestCallback<App_get_tokenActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.isOk()) {
                            tooken = actModel.getRoomToken();
                            if (null == tooken || "".equals(tooken)) {
                                showToast(getString(R.string.can_not_get_room_info), Toast.LENGTH_SHORT);
                                return;
                            }
                            mRTCAudioStreamingManager.startConference(strUserId, getRoomId() + "", tooken, new RTCStartConferenceCallback() {

                                        @Override
                                        public void onStartConferenceSuccess() {
                                            mIsAudioConferenceStarted = true;
                                            Log.i("invite", "纯音频连麦成功开始...");

                                        }

                                        @Override
                                        public void onStartConferenceFailed(int errorCode) {
                                            mIsAudioConferenceStarted = false;
                                            Log.i("invite", "音频连麦错误代码:" + errorCode);
                                            SDHandlerManager.getMainHandler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mRTCAudioStreamingManager.stopConference();
                                                }
                                            });
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }

    /**
     * 停止纯音频连麦
     */
    protected void stopAudioConference() {
        if (mIsAudioConferenceStarted) {
            mRTCAudioStreamingManager.stopConference();
            mRTCAudioStreamingManager.stopCapture();
            mIsAudioConferenceStarted = false;
            Log.i("invite", "纯音频连麦结束");
        }

    }

    /**
     * 销毁纯音频连麦对象
     */
    protected void destroyAudioConference() {
        stopAudioConference();
        if (null != mRTCAudioStreamingManager) {
            mRTCAudioStreamingManager.destroy();
        }
    }


    /**
     * 连麦窗状态
     */
    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    isConferenceReady = true;
                    Log.i("invite", getString(R.string.ready));
                    LogUtil.e("RTCConferenceStateChangedListener" + getString(R.string.ready));
                    break;
                case CONNECT_FAIL:
                    showToast(getString(R.string.failed_to_connect_rtc_server), Toast.LENGTH_SHORT);
                    Log.i("invite", getString(R.string.failed_to_connect_rtc_server));
                    break;
                case VIDEO_PUBLISH_FAILED:
                case AUDIO_PUBLISH_FAILED:
                    isConferenceReady = false;
                    showToast(getString(R.string.failed_to_publish_av_to_rtc) + extra, Toast.LENGTH_SHORT);
                    //                    finish();
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    Log.i("invite", getString(R.string.success_publish_video_to_rtc));
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    Log.i("invite", getString(R.string.success_publish_audio_to_rtc));
                    break;
                case USER_JOINED_AGAIN:
                    showToast(getString(R.string.user_join_other_where), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case USER_KICKOUT_BY_HOST:
                    isConferenceReady = false;
                    Log.i("invite", getString(R.string.user_kickout_by_host));
                    //                    if (LRSManager.getInstance().mIsGaming) {
                    stopAudioConference();
                    //                    } else {
                    stopConference();
                    //                    }
                    break;
                case OPEN_CAMERA_FAIL:
                    isConferenceReady = false;
                    showToast(getString(R.string.failed_open_camera), Toast.LENGTH_SHORT);
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
            LogUtil.e("onUserJoinConference: " + remoteUserId);
            bindConferenceView();

        }

        @Override
        public void onUserLeaveConference(String remoteUserId) {
            LogUtil.e("onUserLeaveConference: " + remoteUserId);
            bindConferenceView();
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

    //    private StreamStatusCallback mStreamStatusCallback = new StreamStatusCallback() {
    //        @Override
    //        public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    String stat = "bitrate: " + streamStatus.totalAVBitrate / 1024 + " kbps"
    //                            + "\naudio: " + streamStatus.audioFps + " fps"
    //                            + "\nvideo: " + streamStatus.videoFps + " fps";
    //                }
    //            });
    //        }
    //    };

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
                    //                    SDToast.showToast("推流准备就绪");
                    LogUtil.e("onStateChanged state:" + "ready");
                    //                    startPublishStreaming();

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
                    //                    stopPublishStreaming();
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

    private StreamingSessionListener mStreamingSessionListener = new StreamingSessionListener() {
        @Override
        public boolean onRecordAudioFailedHandled(int code) {
            return false;
        }

        /**
         * When the network-connection is broken, StreamingState#DISCONNECTED will notified first,
         * and then invoked this method if the environment of restart streaming is ready.
         *
         * @return true means you handled the event; otherwise, given up and then StreamingState#SHUTDOWN
         * will be notified.
         */
        @Override
        public boolean onRestartStreamingHandled(int code) {
            LogUtil.e("onRestartStreamingHandled, reconnect ...");
            return mRTCStreamingManager.startStreaming();
        }

        @Override
        public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
            return null;
        }
    };
    private SDDialogConfirm dialog;
    private RelativeLayout.LayoutParams layoutParams; // videoview的布局参数

    private void initViceAnchor() {
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

        /**
         * Step 6: create the remote windows
         */
        mRemoteWindowA = new RTCVideoWindow(findViewById(R.id.RemoteWindowA), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewA));
        mRemoteWindowB = new RTCVideoWindow(findViewById(R.id.RemoteWindowB), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewB));
        mRemoteWindowC = new RTCVideoWindow(findViewById(R.id.RemoteWindowC), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewC));

        /**
         * Step 7: configure the mix stream position and size (only anchor)
         */
        if (mRole == RTC_ROLE_ANCHOR) {
            // set mix overlay params with absolute value
            // the w & h of remote window equals with or smaller than the vice anchor can reduce cpu consumption
            if (isLandscape) {
                mRemoteWindowA.setAbsolutetMixOverlayRect(options.getVideoEncodingWidth() - 320, 100, 320, 240);
                mRemoteWindowB.setAbsolutetMixOverlayRect(0, 100, 320, 240);
                mRemoteWindowC.setAbsolutetMixOverlayRect(320, 100, 320, 240);
            } else {
                mRemoteWindowA.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 100, 240, 320);
                mRemoteWindowB.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 420, 240, 320);
                mRemoteWindowC.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 740, 240, 320);
            }

            // set mix overlay params with relative value
            // windowA.setRelativeMixOverlayRect(0.65f, 0.2f, 0.3f, 0.3f);
            // windowB.setRelativeMixOverlayRect(0.65f, 0.5f, 0.3f, 0.3f);
        }

        /**
         * Step 8: add the remote windows
         */
        mRTCStreamingManager.addRemoteWindow(mRemoteWindowA);
        mRTCStreamingManager.addRemoteWindow(mRemoteWindowB);
        mRTCStreamingManager.addRemoteWindow(mRemoteWindowC);

        /**
         * Step 9: do prepare, anchor should config streaming profile first
         */
        if (mRole == RTC_ROLE_ANCHOR) {
            //            mRTCStreamingManager.setStreamStatusCallback(mStreamStatusCallback);
            mRTCStreamingManager.setStreamingStateListener(mStreamingStateChangedListener);
            mRTCStreamingManager.setStreamingSessionListener(mStreamingSessionListener);

            mStreamingProfile = new StreamingProfile();
            mStreamingProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM1)
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                    .setPreferredVideoEncodingSize(options.getVideoEncodingWidth(), options.getVideoEncodingHeight());

            if (isLandscape) {
                mStreamingProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.LAND);
            } else {
                mStreamingProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT);
            }

            //            watermarksetting = new WatermarkSetting(this);
            //            watermarksetting.setResourceId(R.drawable.qiniu_logo)
            //                    .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
            //                    .setAlpha(100)
            //                    .setCustomPosition(0.5f, 0.5f);


            mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
            mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

            mRTCStreamingManager.prepare(cameraStreamingSetting, mMicrophoneStreamingSetting, mStreamingProfile); // 没有加水印的
        } else {
            mRTCStreamingManager.prepare(cameraStreamingSetting, null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAudioConference();
        if (null != mVideoView) {
            mVideoView.stopPlayback();
        }
        if (null != mRTCAudioStreamingManager) {
            mRTCAudioStreamingManager.stopStreaming();
            mRTCAudioStreamingManager.stopCapture();
            mRTCAudioStreamingManager.destroy();
        }

        if (null != mRTCStreamingManager) {
            mRTCStreamingManager.stopStreaming();
            mRTCStreamingManager.stopCapture();
            mRTCStreamingManager.destroy();
        }
    }

    @Override
    protected void onClickBottomInviteVideo(View v) {
        if (!mIsConferenceStarted) {
            CommonInterface.requestToken(getRoomId(), new AppRequestCallback<App_get_tokenActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.isOk()) {
                        tooken = actModel.getRoomToken();
                        if (null == tooken || "".equals(tooken)) {
                            showToast(getString(R.string.can_not_get_room_info), Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                    showInviteVideoDialog();
                }
            });
        } else {
            SDDialogMenu dialog = new SDDialogMenu(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setItems(new String[]{getString(R.string.close_invite_video)});
            dialog.setmListener(new SDDialogMenu.SDDialogMenuListener() {
                @Override
                public void onItemClick(View v, int index, SDDialogMenu dialog) {
                    switch (index) {
                        case 0:
                            onClickCloseVideo();
                            break;

                        default:
                            break;
                    }
                }

                @Override
                public void onDismiss(SDDialogMenu dialog) {
                }

                @Override
                public void onCancelClick(View v, SDDialogMenu dialog) {
                }
            });
            dialog.showBottom();
        }
    }

    /**
     * 开始连麦
     *
     * @return
     */

    private boolean startConference() {
        if (mIsConferenceStarted) {
            return true;
        }

        SDHandlerManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mRTCAudioStreamingManager.stopCapture();
                mRTCStreamingManager.startCapture();
                hideLoadingVideo();
                mVideoView.pause();
//                shouldShowPlayer(false);
                startConferenceInternal();
            }
        });

        return true;
    }

    /**
     * 是否要显示videoview播放器的view
     *
     * @param shouldShow
     */
    private void shouldShowPlayer(boolean shouldShow) {
        layoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        if (shouldShow) {
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;

        } else {
            layoutParams.width = 0;
            layoutParams.height = 0;
        }
        SDHandlerManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mVideoView.setLayoutParams(layoutParams);
            }
        });

    }

    /**
     * 真正连麦执行的方法
     *
     * @return
     */
    private boolean startConferenceInternal() {
        /*CommonInterface.requestToken(getRoomId(), new AppRequestCallback<App_get_tokenActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.isOk()) {
                            tooken = actModel.getRoomToken();
                            if (null == tooken || "".equals(tooken)) {
                                SDToast.showToast(getString(R.string.can_not_get_room_info), Toast.LENGTH_SHORT);
                                return;
                            }
                            mRTCStreamingManager.startConference(strUserId, getRoomId() + "", tooken, new RTCStartConferenceCallback() {
                                        private boolean shouldIgnore = false;

                                        @Override
                                        public void onStartConferenceSuccess() {

                                            mIsConferenceStarted = true;


                                            *//**
         * Because `startConference` is called in child thread
         * So we should check if the activity paused.
         *//*
                                            if (mIsActivityPaused) {
                                                stopConference();
                                            }
                                            bindConferenceView();
                                        }

                                        @Override
                                        public void onStartConferenceFailed(int errorCode) {
                                            SDToast.showToast(getString(R.string.failed_to_start_conference) + errorCode, Toast.LENGTH_SHORT);
                                            bindConferenceView();
                                            SDHandlerManager.getMainHandler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showLoadingVideo();
                                                    mRTCStreamingManager.stopConference();
                                                    mVideoView.setVideoPath(mVideoPath);
                                                    mVideoView.start();
                                                    shouldShowPlayer(true);
                                                }
                                            });
                                            if (!shouldIgnore) {
                                                shouldIgnore = true;
                                            }
                                        }
                                    }
                            );
                        }
                    }
                }

        );*/
        mRTCStreamingManager.startConference(strUserId, getRoomId() + "", tooken, new RTCStartConferenceCallback() {
                    private boolean shouldIgnore = false;

                    @Override
                    public void onStartConferenceSuccess() {

                        mIsConferenceStarted = true;


                        /**
                         * Because `startConference` is called in child thread
                         * So we should check if the activity paused.
                         */
                        if (mIsActivityPaused) {
                            stopConference();
                        }
                        bindConferenceView();
                    }

                    @Override
                    public void onStartConferenceFailed(int errorCode) {
                        showToast(getString(R.string.failed_to_start_conference) + errorCode, Toast.LENGTH_SHORT);
                        bindConferenceView();
                        SDHandlerManager.getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                showLoadingVideo();
                                mRTCStreamingManager.stopConference();
                                mVideoView.setVideoPath(mVideoPath);
                                mVideoView.start();
//                                shouldShowPlayer(true);
                            }
                        });
                        if (!shouldIgnore) {
                            shouldIgnore = true;
                        }
                    }
                }
        );
        return true;
    }

    /**
     * 停止连麦
     *
     * @return
     */
    protected boolean stopConference() {
        if (!mIsConferenceStarted) {
            return true;
        }
        if (LRSManager.getInstance().mIsGaming) {
            stopAudioConference();
        } else {
            SDHandlerManager.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    showLoadingVideo();
                    mRTCStreamingManager.stopConference();
                    //                    mRTCStreamingManager.stopCapture();
                    mVideoView.setVideoPath(mVideoPath);
                    mVideoView.start();
//                    shouldShowPlayer(true);
                    mIsConferenceStarted = false;
                }
            });
        }
        return true;
    }


    /**
     * 是否正在延迟加入房间
     */
    protected boolean isInDelayJoinRoom = false;
    private SDRequestHandler requestHandler;
    private long joinTimestamp = System.currentTimeMillis();
    private DisplayMetrics dm = new DisplayMetrics();


    @Override
    protected int onCreateContentView() {
        return R.layout.activity_live_landscape_viewer;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (validateParams(getRoomId(), getGroupId(), getCreaterId())) {
            startJoinRoomRunnable(false);
        }
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        //        rl_player_contain = (RelativeLayout) findViewById(R.id.rl_player_contain);
        // 七牛相关
        afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        if (null != afl) {
            afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        }
        qn_video_glview = (GLSurfaceView) view.findViewById(R.id.qn_video_glview); // 七牛摄像头预览view
        initViceAnchor();

        LiveLayoutActivity.IConferenceUserEventListerner userEventListerner = new LiveLayoutActivity.IConferenceUserEventListerner() {

            @Override
            public void onUserJoinConference(String remoteUserId) {
                bindConferenceView();

            }

            @Override
            public void onUserLeaveConference(String remoteUserId) {
                bindConferenceView();
            }
        };
        setUserEventListerner(userEventListerner);

        // 方便调试
        showLoadingVideo();
        mVideoView.start();
        initIM();
        mRTCStreamingManager.startCapture();

        initAudioConference();

        btnChangeOrientation = (Button) findViewById(R.id.btnChangeOrientation);
        btnChangeOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getRequestedOrientation();
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        requestHorizentalScreen();
    }

    private void requestHorizentalScreen() {
        findViewById(R.id.qn_video_glview).setVisibility(View.GONE);

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (isLandscape) {
            ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
            params.width = dm.widthPixels;
            params.height = params.width * dm.widthPixels / dm.heightPixels;
            mVideoView.setLayoutParams(params);

            btnChangeOrientation.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams btnParams = (RelativeLayout.LayoutParams) btnChangeOrientation.getLayoutParams();
            btnParams.topMargin = (int) (params.height - 40 * dm.density);
            btnChangeOrientation.setLayoutParams(btnParams);
        } else {
            btnChangeOrientation.setVisibility(View.GONE);
        }
    }

    /**
     * 根据横竖屏状态设置VideoView的高宽
     * 横屏为铺满
     * 竖屏按照横屏的高宽比等比例缩放
     */
    private void resizePlayer() {
        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        if (isLandscape()) {
            params.width = dm.widthPixels;
            params.height = dm.heightPixels;
            mVideoView.setLayoutParams(params);

            btnChangeOrientation.setText("最小化");
        } else {
            params.width = dm.widthPixels;
            params.height = params.width * dm.widthPixels / dm.heightPixels;
            mVideoView.setLayoutParams(params);

            btnChangeOrientation.setText("最大化");
        }
    }

    private boolean isLandscape() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        resizePlayer();
    }


    //    七牛相关

    @Override
    protected void initVideoListener() {
        super.initVideoListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int oldRoomId = getRoomId();
        int newRoomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        if (newRoomId != oldRoomId) {
            setIntent(intent);
            exitRoom(false);
            init(null);
        }
        super.onNewIntent(intent);
    }

    protected boolean validateParams(int roomId, String groupId, String createrId) {
        if (roomId <= 0) {
            showToast(getString(R.string.room_id_empty));
            finish();
            return false;
        }

        if (isEmpty(groupId)) {
            showToast(getString(R.string.im_room_id_empty));
            finish();
            return false;
        }

        if (isEmpty(createrId)) {
            showToast(getString(R.string.anchor_id_empty));
            finish();
            return false;
        }
        setRoomId(roomId);
        setGroupId(groupId);
        setCreaterId(createrId);
        return true;
    }


    public void onEventMainThread(EOnClickSmallVideo event) {
        LiveSmallVideoInfoCreaterDialog dialog = new LiveSmallVideoInfoCreaterDialog(this, event);
        dialog.show();
    }

    @Override
    protected void onMsgLRS(MsgModel msg) {
        CustomMsgLRS msg1 = msg.getCustomMsgLRS();
        switch (msg1.getStep()) {
            case 1://提示报名游戏
                LRSManager.getInstance().showSignBottomDialog(this, getRoomId(), false);
                break;
            case 2://提示报名人数 并且可以退出游戏
                if (LRSManager.getInstance().mIsJoinGame) {
                    LRSManager.getInstance().showPlayerBottomDialog(this, getRoomId(), msg1.getNum());
                }
                break;
            case 3:
                LRSManager.getInstance().dismiss();
                //                SDToast.showToast("开始游戏超时");
                break;
            case 4://游戏开始
                mRTCStreamingManager.stopCapture();
                mRTCAudioStreamingManager.startCapture();
                LRSManager.getInstance().dismiss();
                LRSManager.getInstance().mIsGaming = true;
                if (LRSManager.getInstance().getGameUsers().size() <= 0) {
                    LRSManager.getInstance().setGameUsers(msg1.getMembers());
                }
                LRSManager.getInstance().mGameGroupId = msg1.getGame_group_id();
                LRSManager.getInstance().mWolfGroupId = msg1.getWolf_group_id();
                roomLRSUserView.setVisibility(View.VISIBLE);
                roomGameMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
                roomGameWolfMsgView.setGroupId(LRSManager.getInstance().mWolfGroupId);
                roomGameProgressMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
                roomGameProgressMsgView.setIsForGameProgress(true);
                roomGameRuleMsgView.initRule(msg1.getRule());
                LRSManager.getInstance().joinGroup();
                SDEventManager.post(new ELRSGameStateChange(true));
                roomViewerBottomView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        roomViewerBottomView.performChannelClick();
                    }
                }, 1000);
                SDEventManager.post(new ELRSQuiteLianMai());//关闭之前的连麦
                break;
            case 12://结束游戏
                SDEventManager.post(new ELRSQuiteLianMai());//关闭之前的连麦
                mRTCAudioStreamingManager.stopCapture();
                mRTCStreamingManager.startCapture();
                LRSResultDialog dialog = new LRSResultDialog(this, msg1.getWin());
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dealGameFinish();
                    }
                });
                break;
        }
    }

    private void dealGameFinish() {
        stopAudioConference();
        LRSManager.getInstance().quiteGroup();
        LRSManager.getInstance().reSet();
        roomViewerBottomView.reSet();
        roomGameProgressMsgView.dissmissVoteDialog();
        roomLRSUserView.cleanData();
        hideGameRelativeView();
        cleanLRSMsgView();
        SDEventManager.post(new ELRSGameStateChange(false));
    }


    public void hideAllLianMai() {
        //        AVUIControl avuiControl = liveSdk.getAVUIControl();
        //        if(avuiControl != null){
        //            if(avuiControl.mGlVideoView != null && avuiControl.mGlVideoView.length > 1){
        //                for(int i = 1; i < avuiControl.mGlVideoView.length;i++){
        //                    if(avuiControl.mGlVideoView[i] != null){
        //                        avuiControl.mGlVideoView[i].setVisibility(GLView.INVISIBLE);
        //                    }
        //                }
        //            }
        //        }
    }

    @Override
    protected void dealWolfKillGameInfo(LRSGameModel model) {
        LRSManager.getInstance().mIsGaming = true;
        if (LRSManager.getInstance().getGameUsers().size() <= 0) {
            LRSManager.getInstance().setGameUsers(model.getMembers());
        }
        roomLRSUserView.initData();
        LRSManager.getInstance().mGameGroupId = model.getGame_group_id();
        roomLRSUserView.setVisibility(View.VISIBLE);
        roomGameMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
        roomGameMsgView.initData(model.getMessage());
        roomGameProgressMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
        roomGameProgressMsgView.setIsForGameProgress(true);
        roomGameProgressMsgView.initData(model.getMessage());
        roomGameRuleMsgView.initRule(model.getRule());
        LRSManager.getInstance().joinGroup();
        ELRSGameStateChange event = new ELRSGameStateChange(true);
        event.setIsViewer(true);
        SDEventManager.post(event);
        roomViewerBottomView.postDelayed(new Runnable() {
            @Override
            public void run() {
                roomViewerBottomView.performChannelClick();
            }
        }, 1000);
        hideAllLianMai();
    }

    private void hideGameRelativeView() {
        SDViewUtil.show(roomMsgView);
        SDViewUtil.hide(roomGameMsgView);
        SDViewUtil.hide(roomGameWolfMsgView);
        SDViewUtil.hide(roomGameProgressMsgView);
        SDViewUtil.hide(roomGameRuleMsgView);
        SDViewUtil.hide(roomLRSUserView);
    }

    public void onEventMainThread(ELRSChannelChange event) {
        if (event.getIsLiveGroup()) {
            roomMsgView.setVisibility(View.VISIBLE);
            roomGameMsgView.setVisibility(View.GONE);
            roomGameWolfMsgView.setVisibility(View.GONE);
            roomGameProgressMsgView.setVisibility(View.GONE);
            roomGameRuleMsgView.setVisibility(View.GONE);
        } else {
            roomMsgView.setVisibility(View.GONE);
            switch (event.getChannelIndex()) {
                case 0:
                    roomGameMsgView.setVisibility(View.VISIBLE);
                    roomGameWolfMsgView.setVisibility(View.GONE);
                    roomGameProgressMsgView.setVisibility(View.GONE);
                    roomGameRuleMsgView.setVisibility(View.GONE);
                    break;
                case 1:
                    roomGameMsgView.setVisibility(View.GONE);
                    roomGameWolfMsgView.setVisibility(View.VISIBLE);
                    roomGameProgressMsgView.setVisibility(View.GONE);
                    roomGameRuleMsgView.setVisibility(View.GONE);
                    break;
                case 2:
                    roomGameMsgView.setVisibility(View.GONE);
                    roomGameWolfMsgView.setVisibility(View.GONE);
                    roomGameProgressMsgView.setVisibility(View.VISIBLE);
                    roomGameRuleMsgView.setVisibility(View.GONE);
                    break;
                case 3:
                    roomGameMsgView.setVisibility(View.GONE);
                    roomGameWolfMsgView.setVisibility(View.GONE);
                    roomGameProgressMsgView.setVisibility(View.GONE);
                    roomGameRuleMsgView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void initPlayer() {
        super.initPlayer();
        // 七牛临时,直接初始化
        EEnterRoomComplete event = new EEnterRoomComplete();
        event.result = 1;
        event.roomId = getRoomId();
        event.isCreater = false;
        //        SDEventManager.post(event);
        onEventMainThread(event);
    }


    /**
     * 加入房间回调
     */
    public void onEventMainThread(EEnterRoomComplete event) {
        if (event.roomId != getRoomId()) {
            return;
        }
        //        if (liveSdk.getRoom() != null)
        //        {
        LogUtil.i("join room success");
        LiveInformation.getInstance().enterRoom(getRoomId(), getGroupId(), getCreaterId());
        setCanBindShowInviteVideoView(true);
        if (isScrollChangeRoom) {
            showViewerLayout();
            EScrollChangeRoomComplete eventChangeRoomComplete = new EScrollChangeRoomComplete();
            SDEventManager.post(eventChangeRoomComplete);
        }
        //            七牛临时,下面的语句是为了初始化腾讯avsdk视频view的，现在换用七牛暂时不需要用，直接略过
        //            liveSdk.initAvUILayer(getApplication(), findViewById(android.R.id.content),getCreaterId());
        //        initIM();
        //        } else
        //        {
        //            LogUtil.i("join room error");
        //            dealEnterRoomFail(event);
        //        }
    }

    //    private void dealEnterRoomFail(final EEnterRoomComplete event)
    //    {
    //        if (10003 == event.result)
    //        {
    //            requestRoomInfo();
    //        } else
    //        {
    //            boolean trySuccess = joinRoomTryer.tryRunDelayed(new Runnable()
    //            {
    //                @Override
    //                public void run()
    //                {
    //                    LogUtil.i("try join room:" + joinRoomTryer.getTryCount() + "," + event.result);
    //                    joinSdkRoom();
    //                }
    //            }, 3000);
    //
    //            if (!trySuccess)
    //            {
    //                showJoinRoomErrorDialog(event.result);
    //            }
    //        }
    //    }

    private void showJoinRoomErrorDialog(int result) {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("进入房间失败，请退出重试").setTextCancel(null).setTextConfirm("确定");
        dialog.setCancelable(false);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
                exitRoom(true);
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    /**
     * 请求视频完成回调
     *
     * @param event
     */
    public void onEventMainThread(ERequestViewComplete event) {
        //        if (event.listId.contains(getCreaterId())) {
        //            SDHandlerManager.getMainHandler().postDelayed(new Runnable() {
        //
        //                @Override
        //                public void run() {
        ////                    hideLoadingVideo();
        ////                    liveSdk.setMirror(true,getCreaterId());
        //                }
        //            }, 100);
        //        }
    }


    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                onErrorJoinGroup(code, desc);
            }

            @Override
            public void onSuccess() {
                onSuccessJoinGroup(groupId);

            }
        });
    }

    public void onErrorJoinGroup(int code, String desc) {
        if (isScrollChangeRoom) {
            //不用请求，切换的时候已经请求
        } else {
            requestRoomInfo();
        }
    }

    protected void onSuccessJoinGroup(String groupId) {
        sendViewerJoinMsg(groupId, null);

        if (isScrollChangeRoom) {
            //不用请求，切换的时候已经请求
        } else {
            requestRoomInfo();
        }
    }

    @Override
    protected void onMsgEndVideo(MsgModel msg) {
        super.onMsgEndVideo(msg);
        // 接收到视频结束的消息,退出房间
        LogUtil.e("接收到视频结束的消息,退出房间");
        exitRoom(false);
    }

    @Override
    protected void onMsgStopLive(MsgModel msg) {
        super.onMsgStopLive(msg);
        showToast(msg.getCustomMsgStopLive().getDesc());
        exitRoom(true);
    }


    @Override
    protected void onMsgAcceptVideo(MsgModel msg) {
        super.onMsgAcceptVideo(msg);
        if (LRSManager.getInstance().mIsGaming) {
            Log.i("invite", "viewer onMsgAcceptVideo");
            if (!mIsAudioConferenceStarted) {
                startAudioConference();
                dismissInviteVideoDialog();
            }
        } else {
            if (!mIsConferenceStarted) {
                inviteVideoDialog.dismiss();
                startConference();
                Log.i("invite", "普通连麦");
            }
        }
    }

    @Override
    protected void onMsgRejectVideo(MsgModel msg) {
        super.onMsgRejectVideo(msg);
        if (isInvitingVideo()) {
            inviteVideoDialog.tv_info.setText(msg.getCustomMsgRejectVideo().getSender().getNickName() + "拒绝了你的连麦请求");
            inviteVideoDialog.startDismissRunnable(2000);
        }
    }

    @Override
    protected void onMsgStopVideo(MsgModel msg) {
        super.onMsgStopVideo(msg);
        if (!LRSManager.getInstance().mIsGaming) {
            showToast(getString(R.string.anchor_close_small_video));
        }
    }

    /**
     * 关连麦
     */
    @Override
    protected void onClickCloseVideo() {
        stopConference();
        stopAudioConference();
    }

    public void onEventMainThread(ELRSQuiteLianMai event) {
        // 如果不是轮到自己连麦,那就关闭自己就行了
        stopAudioConference();
    }

    //    /**
    //     * 切换到小屏幕窗口
    //     */
    //    private void switchToSmallVideo() {
    //        if (isBigToSmall == 1) {
    //            return;
    //        }
    //
    //        isBigToSmall = 1;
    //
    //        JoinLiveData data = new JoinLiveData();
    //        data.setRoomId(getRoomId());
    //        data.setGroupId(getGroupId());
    //        data.setCreaterId(getCreaterId());
    //
    //        LiveFloatViewerView.tryJoinRoom(data);
    //
    //        finish();
    //    }

    /**
     * 停止连麦
     */
    private void stopVideoWithCreater() {
        if (isVideoing) {
            isVideoing = false;
            stopAudioConference();
        }
    }

    /**
     * 退出房间
     *
     * @param isNeedFinish
     */
    @Override
    protected void exitRoom(boolean isNeedFinish) {
        if (LRSManager.getInstance().mIsGaming && LRSManager.getInstance().mIsJoinGame) {
            CommonInterface.requestLRSOut(getRoomId(), null);
        }
        stopVideoWithCreater();
        dealGameFinish();
        if (mIsConferenceStarted) {
            stopConference();
        }
        if (null != mVideoView) {
            mVideoView.stopPlayback();
        }
        if (null != mRTCStreamingManager) {
            mRTCStreamingManager.destroy();
        }

        destroyIM();
        if (null != dialog) {
            dialog.dismiss();
        }

        if (isNeedShowFinish) {
            addLiveFinish();
            if (null != dialog) {
                dialog.dismiss();
            }
            return;
        }

        if (isNeedFinish) {
            if (getRoomInfo() != null && getRoomInfo().isAuctioning()) {
                //                Intent intent = new Intent(this, LiveMainActivity.class);
                //                startActivity(intent);
                finish();
                // showExitActionDialog();
            } else {
                //                Intent intent = new Intent(this, LiveMainActivity.class);
                //                startActivity(intent);
                finish();
            }
        }
    }

    private void showExitActionDialog() {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("您参与的竞拍暂未结束，确定离开直播间？");
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                finish();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    @Override
    protected void destroyIM() {
        super.destroyIM();

        final String groupId = getGroupId();
        sendViewerQuitMsg(groupId, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                IMHelper.deleteLocalMessageGroup(groupId, null);
                quitGroup(groupId, null);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.deleteLocalMessageGroup(groupId, null);
                quitGroup(groupId, null);
            }
        });
        LRSManager.getInstance().quiteGroup();
    }

    @Override
    protected void onVerticalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super.onVerticalScroll(e1, e2, distanceX, distanceY);
        if (isInDelayJoinRoom) {
            startJoinRoomRunnable(true);
        }
    }

    @Override
    protected void afterVerticalScroll(boolean top) {
        super.afterVerticalScroll(top);
        if (requestHandler != null) {
            requestHandler.cancel();
        }
        exitRoom(false);
        showLoadingVideo();
        invisibleViewerLayout();
        roomMsgView.clearRoomMsg();
        roomMsgView.initLiveMsg();
        joinTimestamp = System.currentTimeMillis();
        requestHandler = requestRoomInfo();
    }

    private void cleanLRSMsgView() {
        roomGameMsgView.clearRoomMsg();
        roomGameWolfMsgView.clearRoomMsg();
        roomGameProgressMsgView.clearRoomMsg();
        roomGameRuleMsgView.clearRoomMsg();
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);
        if (isScrollChangeRoom) {
            isManage = actModel.getIsManage();
            int rId = actModel.getRoomId();
            String gId = actModel.getGroupId();
            String userId = actModel.getPodcast().getUserId();

            if (validateParams(rId, gId, userId)) {
                startJoinRoomRunnable(true);
            }
        }
    }

    @Override
    protected void onErrorRequestRoomInfo(App_get_videoActModel actModel) {
        super.onErrorRequestRoomInfo(actModel);
        if (actModel.isVideoStoped()) {
            addLiveFinish();
        } else {
            if (!isScrollChangeRoom) {
                exitRoom(true);
            }
        }
    }

    private void startJoinRoomRunnable(boolean delay) {
        if (delay) {
            isInDelayJoinRoom = true;
            SDHandlerManager.getMainHandler().removeCallbacks(joinRoomRunnable);
            SDHandlerManager.getMainHandler().postDelayed(joinRoomRunnable, DELAY_JOIN_ROOM);
        } else {
            joinRoomRunnable.run();
        }
    }

    /**
     * 加入房间runnable
     */
    private Runnable joinRoomRunnable = new Runnable() {

        @Override
        public void run() {
            isInDelayJoinRoom = false;
            joinSdkRoom();
        }
    };

    /**
     * 加入房间
     */
    protected void joinSdkRoom() {
        //        liveSdk.enterRoom(getRoomId(), false);
        LogUtil.e("joinSdkRoom" + getRoomId());
        //        mVideoView.pause();
        //        mVideoView.setVideoPath("");
        //        mVideoView.start();
    }


    @Override
    protected void afterVerticalScrollRoom(App_get_videoActModel videoActModel) {
        super.afterVerticalScrollRoom(videoActModel);
        stopConference();
        mVideoView.pause();
        mVideoView.setVideoPath(videoActModel.getPlay_url());
        LogUtil.e("vertical,newplay_url = " + videoActModel.getPlay_url());
        mVideoView.start();

    }

    public void onEventMainThread(EEnterOrExitRoomTimeout event) {
        showToast(getString(R.string.operate_time_out));
        exitRoom(true);
    }

    @Override
    public void onBackPressed() {
        if (dm.widthPixels > dm.heightPixels) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        //未关注
        if (roomInfoView.getHasFollow() == 0) {
            if (System.currentTimeMillis() - joinTimestamp >= LiveConstant.LIVE_VIEWER_FOLLOW_TIME) {
                showExitDialogForFollow();
            } else {
                showExitDialog();
            }
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void onClickBottomClose(View v) {
        buttonClose();
    }

    /**
     * 点击xx退出房间
     */
    private void buttonClose() {
        //        exitRoom(true);
        //未关注
        if (roomInfoView.getHasFollow() == 0) {
            if (System.currentTimeMillis() - joinTimestamp >= LiveConstant.LIVE_VIEWER_FOLLOW_TIME) {
                showExitDialogForFollow();
            } else {
                showExitDialog();
            }
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        if (null == dialog) {
            dialog = new SDDialogConfirm(this);
        }
        dialog.setTextContent(getString(R.string.ensure_exist));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                exitRoom(true);
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    private void showExitDialogForFollow() {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent(getString(R.string.watch_many_time_if_follow));
        dialog.setTextCancel(getString(R.string.exit));
        dialog.setTextConfirm(getString(R.string.follow));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                CommonInterface.requestFollow(getCreaterId(), new AppRequestCallback<App_followActModel>() {

                    @Override
                    protected void onSuccess(SDResponse resp) {
                    }
                });
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
                exitRoom(true);
            }
        });
        dialog.show();
    }

    public void onEventMainThread(final EGLVideoViewVisibilityChange event) {
        if (LiveConstant.CAN_MOVE_VIDEO) {
            //            if (!QavsdkControl.getInstance().getAVUIControl().isBigGlVideoView(event.view))
            //            {
            //                if (event.view.getVisibility() == GLView.VISIBLE)
            //                {
            //                    addIgnoreRect(new GLVideoViewRecter(event.view));
            //                } else
            //                {
            //                    removeIgnoreRect(new GLVideoViewRecter(event.view));
            //                }
            //            }
        }
    }

    public void onEventMainThread(EUnLogin event) {
        exitRoom(true);
    }

    public void onEventMainThread(EImOnForceOffline event) {
        exitRoom(true);
    }

    public void onEventMainThread(EOnCallStateChanged event) {
        switch (event.state) {
            case TelephonyManager.CALL_STATE_RINGING:
                sdkEnableAudioDataVolume(false);

                if (isVideoing) {
                    //                    change2Viewer(null);
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                sdkEnableAudioDataVolume(false);
                if (isVideoing) {
                    //                    change2Viewer(null);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                sdkEnableAudioDataVolume(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnect(TANetWorkUtil.netType type) {
        if (type == TANetWorkUtil.netType.mobile) {
            SDDialogConfirm dialog = new SDDialogConfirm(this);
            dialog.setTextContent(getString(R.string.current_network_is_not_wifi_if_continue)).setTextCancel(getString(R.string.no)).setTextConfirm(getString(R.string.yes)).setmListener(new SDDialogCustom.SDDialogCustomListener() {

                @Override
                public void onDismiss(SDDialogCustom dialog) {
                }

                @Override
                public void onClickConfirm(View v, SDDialogCustom dialog) {
                }

                @Override
                public void onClickCancel(View v, SDDialogCustom dialog) {
                    exitRoom(true);
                }
            }).show();
        }
        super.onConnect(type);
    }

    //    protected void change2VideoViewer(final AVRoomMulti.ChangeAVControlRoleCompleteCallback listener) {
    //        liveSdk.change2VideoViewer(new AVRoomMulti.ChangeAVControlRoleCompleteCallback()
    //        {
    //
    //            @Override
    //            public void OnComplete(int result)
    //            {
    //                if (LiveUtils.isResultOk(result))
    //                {
    //                    isVideoing = true;
    //                    sdkEnableFrontCamera();
    //                    sdkEnableMic(true);
    //                }
    //                if (listener != null)
    //                {
    //                    listener.OnComplete(result);
    //                }
    //            }
    //        });
    //    }

    //    protected void change2Viewer(final AVRoomMulti.ChangeAVControlRoleCompleteCallback listener) {
    //        liveSdk.change2Viewer(new AVRoomMulti.ChangeAVControlRoleCompleteCallback()
    //        {
    //
    //            @Override
    //            public void OnComplete(int result)
    //            {
    //                if (LiveUtils.isResultOk(result))
    //                {
    //                    isVideoing = false;
    //                    sdkDisableCamera();
    //                    sdkEnableMic(false);
    //                } else
    //                {
    //                    SDToast.showToast("关闭连麦权限失败：" + result);
    //                }
    //                if (listener != null)
    //                {
    //                    listener.OnComplete(result);
    //                }
    //            }
    //        });
    //        liveSdk.setLocalHasVideo(false);
    //    }


    @Override
    protected void sdkDisableCamera() {
        //        liveSdk.disableCamera();
    }

    @Override
    protected void sdkEnableAudioDataVolume(boolean enable) {
        //        liveSdk.enableAudioDataVolume(enable);
    }

    @Override
    protected void sdkEnableBackCamera() {
        //        liveSdk.enableBackCamera();
    }

    @Override
    protected void sdkEnableFlash(boolean enable) {
        //        liveSdk.enableFlash(enable);
    }

    @Override
    protected void sdkEnableFrontCamera() {
        //        liveSdk.enableFrontCamera();
    }

    @Override
    protected void sdkEnableLastCamera() {
        //        liveSdk.enableLastCamera();
    }

    @Override
    protected void sdkEnableMic(boolean enable) {
        //        liveSdk.enableMic(enable);
    }

    @Override
    protected void sdkSwitchCamera() {
        //        liveSdk.switchCamera();
    }

    @Override
    protected void sdkEnableBeauty(boolean enable) {
        //        liveSdk.enableBeauty(enable);
    }

    @Override
    protected void sdkOnResume() {
        //        if (isBigToSmall == 1)
        //        {
        //
        //        } else
        //        {
        //            liveSdk.onResume();
        //        }
    }

    @Override
    protected void sdkOnPause() {
        //        if (isBigToSmall == 1)
        //        {
        //
        //        } else
        //        {
        //            liveSdk.onPause();
        //        }
    }

    @Override
    protected void sdkOnStop() {

    }

    @Override
    protected void sdkOnDestroy() {
        //        if (isBigToSmall == 1) {
        //
        //        } else {
        ////            liveSdk.onDestroy();
        //        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("互动直播间-观众");
        MobclickAgent.onResume(this);
        mIsActivityPaused = false;
        //        showLoadingVideo();
        //        mVideoView.start();
        //        initIM();
        //        mRTCStreamingManager.startCapture();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        方便调试
        //        mVideoView.pause();
        //        if (null != mRTCStreamingManager) {
        //            mRTCStreamingManager.stopCapture();
        //        }
        //        if (null != mRTCAudioStreamingManager) {
        //            mRTCAudioStreamingManager.stopCapture();
        //        }
        mIsActivityPaused = true;
        MobclickAgent.onPageEnd("互动直播间-观众");
        MobclickAgent.onPause(this);
    }

    //    踢人

    public void onClickKickoutUserA(View v) {
        //        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewA);
        String uerID = mRTCStreamingManager.getParticipants().get(0);
        if (null == uerID || "".equals(uerID)) {
            return;
        }
        if (uerID.equals(UserModelDao.query().getUserId())) {
            stopConference();
        }

    }

    public void onClickKickoutUserB(View v) {
        //        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewB);
        String uerID = mRTCStreamingManager.getParticipants().get(2);
        if (null == uerID || "".equals(uerID)) {
            return;
        }
        if (uerID.equals(UserModelDao.query().getUserId())) {
            stopConference();
        }

    }

    public void onClickKickoutUserC(View v) {
        //        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewC);
        String uerID = mRTCStreamingManager.getParticipants().get(3);
        if (null == uerID || "".equals(uerID)) {
            return;
        }
        if (uerID.equals(UserModelDao.query().getUserId())) {
            stopConference();
        }

    }

    /**
     * 点击远程连麦窗口A
     *
     * @param v
     */
    public void onClickRemoteWindowA(View v) {
        //        SDToast.showToast("onClickRemoteWindowA");
        //        FrameLayout window = (FrameLayout) v;
        //        if (window.getChildAt(0).getPropId() == qn_video_glview.getPropId()) {
        //            mRTCStreamingManager.switchRenderView(qn_video_glview, mRTCVideoWindowA.getGLSurfaceView());
        //        } else {
        //            mRTCStreamingManager.switchRenderView(mRTCVideoWindowA.getGLSurfaceView(), qn_video_glview);
        //        }
    }

    /**
     * 点击连麦窗口B
     *
     * @param v
     */
    public void onClickRemoteWindowB(View v) {
        //        SDToast.showToast("onClickRemoteWindowB");
        //        FrameLayout window = (FrameLayout) v;
        //        if (window.getChildAt(0).getPropId() == qn_video_glview.getPropId()) {
        //            mRTCStreamingManager.switchRenderView(qn_video_glview, mRTCVideoWindowB.getGLSurfaceView());
        //        } else {
        //            mRTCStreamingManager.switchRenderView(mRTCVideoWindowB.getGLSurfaceView(), qn_video_glview);
        //        }

    }

    /**
     * 点击连麦窗口C
     *
     * @param v
     */
    public void onClickRemoteWindowC(View v) {
        //        SDToast.showToast("onClickRemoteWindowC");
        //        FrameLayout window = (FrameLayout) v;
        //        if (window.getChildAt(0).getPropId() == qn_video_glview.getPropId()) {
        //            mRTCStreamingManager.switchRenderView(qn_video_glview, mRTCVideoWindowC.getGLSurfaceView());
        //        } else {
        //            mRTCStreamingManager.switchRenderView(mRTCVideoWindowC.getGLSurfaceView(), qn_video_glview);
        //        }

    }

    public void onClickUserANickName(View v) {
        //        SDToast.showToast("onClickUserANickName");
        if (names == null || names.size() < 1) {
            return;
        }
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(this, names.get(0));
        dialog.show();

    }

    public void onClickUserBNickName(View v) {
        //        SDToast.showToast("onClickUserBNickName");
        if (names == null || names.size() < 2) {
            return;
        }
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(this, names.get(1));
        dialog.show();
    }

    public void onClickUserCNickName(View v) {
        //        SDToast.showToast("onClickUserCNickName");
        if (names == null || names.size() < 3) {
            return;
        }
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(this, names.get(2));
        dialog.show();
    }


    protected void bindConferenceView() {
        names = mRTCStreamingManager.getParticipants();
        //        if (null != names && names.size() >= 1) {
        //            int counts = names.size();
        //            for (int i = 0; i < counts; i++) {
        //                if (i == 0) {
        //                    SDHandlerManager.getMainHandler().post(new Runnable() {
        //                        @Override
        //                        public void run() {
        //
        //                            CommonInterface.requestUserInfo(null, names.get(0), new AppRequestCallback<App_userinfoActModel>() {
        //                                @Override
        //                                protected void onSuccess(SDResponse resp) {
        //                                    if (actModel.getStatus() == 1) {
        //                                        userANickName.setText(actModel.getUser().getNickName());
        //                                    }
        //                                }
        //                            });
        //                        }
        //                    });
        //                }
        //                if (i == 1) {
        //                    SDHandlerManager.getMainHandler().post(new Runnable() {
        //                        @Override
        //                        public void run() {
        //
        //                            CommonInterface.requestUserInfo(null, names.get(1), new AppRequestCallback<App_userinfoActModel>() {
        //                                @Override
        //                                protected void onSuccess(SDResponse resp) {
        //                                    if (actModel.getStatus() == 1) {
        //                                        userBNickName.setText(actModel.getUser().getNickName());
        //                                    }
        //                                }
        //                            });
        //                        }
        //                    });
        //                }
        //                if (i == 2) {
        //                    SDHandlerManager.getMainHandler().post(new Runnable() {
        //                        @Override
        //                        public void run() {
        //
        //                            CommonInterface.requestUserInfo(null, names.get(2), new AppRequestCallback<App_userinfoActModel>() {
        //                                @Override
        //                                protected void onSuccess(SDResponse resp) {
        //                                    if (actModel.getStatus() == 1) {
        //                                        userCNickName.setText(actModel.getUser().getNickName());
        //                                    }
        //                                }
        //                            });
        //                        }
        //                    });
        //                }
        //            }
        //        }

    }
}
