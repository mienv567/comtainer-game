package com.fanwe.live.activity.room;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.looper.impl.SDWaitRunner;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTryRunner;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.appview.FireWorkImageView;
import com.fanwe.live.appview.room.RoomMsgView;
import com.fanwe.live.appview.room.RoomTipsMsgView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dialog.LRSResultDialog;
import com.fanwe.live.dialog.LiveCreaterFinishDialog;
import com.fanwe.live.dialog.LiveCreaterReceiveInviteVideoDialog;
import com.fanwe.live.dialog.LiveSetCameraBeautyDialogQN;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.dialog.MissionResultDialog;
import com.fanwe.live.event.EEnableCameraComplete;
import com.fanwe.live.event.EEnterOrExitRoomTimeout;
import com.fanwe.live.event.EEnterRoomComplete;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ELRSChannelChange;
import com.fanwe.live.event.ELRSGameStateChange;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EOnClickSmallVideo;
import com.fanwe.live.event.ERoomHasCameraVideo;
import com.fanwe.live.event.ERoomNoCameraVideo;
import com.fanwe.live.event.ESurfaceCreated;
import com.fanwe.live.event.ETouchSWNickName;
import com.fanwe.live.listeners.OnMarqueeListener;
import com.fanwe.live.model.App_get_tokenActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgAcceptVideo;
import com.fanwe.live.model.custommsg.CustomMsgCreaterStopVideo;
import com.fanwe.live.model.custommsg.CustomMsgInviteVideo;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.fanwe.live.model.custommsg.CustomMsgMissionResult;
import com.fanwe.live.model.custommsg.CustomMsgOnlineNumber;
import com.fanwe.live.model.custommsg.CustomMsgRejectVideo;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.utils.LiveUtils;
import com.fanwe.live.utils.StringUtils;
import com.fanwe.live.view.LiveHRightMenu;
import com.fanwe.live.view.LiveViewManager;
import com.fanwe.live.view.MsgPanel;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.sunday.eventbus.SDEventManager;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class LiveLandscapeCreatorActivity extends LiveLayoutCreaterActivity {

    /*-------------------- 横屏直播新UI增加变量 start -------------------------*/
    private LiveViewManager mLiveViewManager;
    private MsgPanel mMsgPanel;
    public static final int ROLE_VIEWER = 0;
    public static final int ROLE_CREATRE = 1;

    private int viewerNumber;

    @ViewInject(R.id.tv_deadline)
    private TextView mTvDeadline;

    @ViewInject(R.id.tv_room_id)
    private TextView mTvRoomId;

    @ViewInject(R.id.tv_watch_number)
    private TextView mTvWatchNumber;

    @ViewInject(R.id.tv_danmu_connect_status)
    private TextView tv_danmu_connect_status;
    @ViewInject(R.id.room_msg_view_creator)
    private RoomMsgView room_msg_view_creator;
    @ViewInject(R.id.tv_expand_danmu)
    private TextView tv_expand_danmu;
    @ViewInject(R.id.mtv_marque)
    private RoomTipsMsgView tipsMsgView;
    @ViewInject(R.id.iv_fire_work)
    private FireWorkImageView iv_fire_work;
    /*-------------------- 横屏直播新UI增加变量 end -------------------------*/

    private SDTryRunner createGroupTryer = new SDTryRunner();
    protected boolean isCreaterLeaveByCall = false;

    // 七牛相关
    private RTCVideoWindow mRemoteWindowA;
    //    private RTCVideoWindow mRemoteWindowB;
    //    private RTCVideoWindow mRemoteWindowC;

    // 七牛连麦相关
    private boolean mIsConferenceStarted = false;
    //    private TextView tv_state;
    private String tooken = "";
    protected List<String> names;
    protected TextView userANickName;
    protected TextView userCNickName;
    protected TextView userBNickName;

    // 房间信息
    public static final String EXTRA_ROOM_INFO = "roomInfo";
    // 是否开启闪光灯
    private boolean mLightOn;
    private MissionResultDialog dialog;
    private int lastX;
    private int lastY;
    private boolean isClick;
    private long startTime;
    private long endTime;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("互动直播间-主播");
        MobclickAgent.onResume(this);
        mIsActivityPaused = false;
        /**
         * Step 10: You must start capture before conference or streaming
         * You will receive `Ready` state callback when capture started success
         */
        mRTCStreamingManager.startCapture();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("互动直播间-主播");
        MobclickAgent.onPause(this);
        //        方便调试
        mIsActivityPaused = true;
        /**
         * Step 11: You must stop capture, stop conference, stop streaming when activity paused
         */
        mRTCStreamingManager.stopCapture();
        stopPublishStreaming();
        destroyIM();
    }

    @Override
    protected void onDestroy() {
        sdkEnableMic(false);
        sdkDisableCamera();
        super.onDestroy();
    }

    //    private StreamStatusCallback mStreamStatusCallback = new StreamStatusCallback() {
    //        @Override
    //        public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    String stat = "bitrate: " + streamStatus.totalAVBitrate / 1024 + " kbps"
    //                            + "\naudio: " + streamStatus.audioFps + " fps"
    //                            + "\nvideo: " + streamStatus.videoFps + " fps";
    //                    mStatTextView.setText(stat);
    //                }
    //            });
    //        }
    //    };


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

    @Override
    protected int onCreateContentView() {
        return R.layout.activity_live_landscape_creator;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    public void onEventMainThread(ETouchSWNickName event) {
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(this, event.getIdentifier());
        dialog.show();
    }

    private DisplayMetrics dm = new DisplayMetrics();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (!AppRuntimeWorker.isContextStarted()) {
            finish();
            return;
        }
        //
        //        if (getRoomId() <= 0) {
        //            SDToast.showToast(getString(R.string.room_id_empty));
        //            finish();
        //            return;
        //        }

        // 七牛sdk
        publishAddr = getIntent().getStringExtra(EXTRA_PUBLISHADDR); // 获取推流地址

        setCreaterId(strUserId);
        initLayout(getWindow().getDecorView());
        initIM();

        int millis = 9 * 3600 * 1000 + 21 * 60 * 1000 + 15 * 1000;
        mTvDeadline.setText(StringUtils.formatDeadline(millis));
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (isLandscape && dm.widthPixels < dm.heightPixels) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /*---------------------横屏新UI start------------------------*/
        FrameLayout flMenuContainer = (FrameLayout) findViewById(R.id.flMenuContainer);
        mLiveViewManager = new LiveViewManager(null, flMenuContainer,
                getSupportFragmentManager(), mRole);
        mLiveViewManager.showH();

        tv_expand_danmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!room_msg_view_creator.isShown()) {
                    tv_expand_danmu.setText(R.string.pull_back_danmu);
                    tv_expand_danmu.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_arrow_down_gray, 0, 0, 0);
                    room_msg_view_creator.setVisibility(View.VISIBLE);
                } else {
                    tv_expand_danmu.setText(R.string.expand_danmu);
                    tv_expand_danmu.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_arrow_up_gray, 0, 0, 0);
                    room_msg_view_creator.setVisibility(View.GONE);
                }
            }
        });

        mMsgPanel = new MsgPanel(flMenuContainer);

        mTvRoomId.setText("房间号: 加载中...");
        mTvWatchNumber.setText("加载中...");

        final int duration = getResources().getInteger(R.integer.fire_work_total_duration);
        tipsMsgView.setListener(new OnMarqueeListener() {
                                    @Override
                                    public void onMarqueeStart(int status) {
                                        if (status == 1) {
                                            iv_fire_work.explode(duration);
                                        }
                                    }

                                    @Override
                                    public void onMarquessEnd() {
                                        iv_fire_work.stopAndGone();
                                    }
                                }

        );

        /*-----------------------横屏新UI start------------------------*/

        userANickName = (TextView) findViewById(R.id.tv_userA);
        userCNickName = (TextView) findViewById(R.id.tv_userC);
        userBNickName = (TextView) findViewById(R.id.tv_userB);

        // 七牛相关
        /**
         * Step 6: create the remote windows
         */
        View remoteViewA = findViewById(R.id.RemoteWindowA);
        mRemoteWindowA = new RTCVideoWindow(remoteViewA, (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewA));
        //        mRemoteWindowB = new RTCVideoWindow(findViewById(R.id.RemoteWindowB), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewB));
        //        mRemoteWindowC = new RTCVideoWindow(findViewById(R.id.RemoteWindowC), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewC));

        /**
         * Step 7: configure the mix stream position and size (only anchor)
         */
        if (mRole == RTC_ROLE_ANCHOR) {
            // set mix overlay params with absolute value
            // the w & h of remote window equals with or smaller than the vice anchor can reduce cpu consumption
            //            if (isLandscape) {
            //                mRemoteWindowA.setAbsolutetMixOverlayRect(options.getVideoEncodingWidth() - 320, 100, 320, 240);
            //                //                mRemoteWindowB.setAbsolutetMixOverlayRect(0, 100, 320, 240);
            //                //                mRemoteWindowC.setAbsolutetMixOverlayRect(320, 100, 320, 240);
            //            } else {
            //                mRemoteWindowA.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 100, 240, 320);
            //                //                mRemoteWindowB.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 420, 240, 320);
            //                //                mRemoteWindowC.setAbsolutetMixOverlayRect(options.getVideoEncodingHeight() - 240, 740, 240, 320);
            //            }

            // set mix overlay params with relative value
            mRemoteWindowA.setRelativeMixOverlayRect(0.8f, 0.5f, 0.2f, 0.5f);
            //            mRemoteWindowB.setRelativeMixOverlayRect(0.7f, 0.45f, 0.3f, 0.25f);
            //            mRemoteWindowC.setRelativeMixOverlayRect(0.7f, 0.7f, 0.3f, 0.25f);
        }

        /**
         * Step 8: add the remote windows
         */
        mRTCStreamingManager.addRemoteWindow(mRemoteWindowA);
        //        mRTCStreamingManager.addRemoteWindow(mRemoteWindowB);
        //        mRTCStreamingManager.addRemoteWindow(mRemoteWindowC);

        /**
         * Step 9: do prepare, anchor should config streaming profile first
         */
        if (mRole == RTC_ROLE_ANCHOR) {
            //            mStatTextView = (TextView) findViewById(R.id.tv_stattextview);
            mStatusTextView = (TextView) findViewById(R.id.tv_statustextview);

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

            //            mRTCStreamingManager.prepare(cameraStreamingSetting, mMicrophoneStreamingSetting, watermarksetting, mStreamingProfile); // 加水印的
            mRTCStreamingManager.prepare(cameraStreamingSetting, mMicrophoneStreamingSetting, mStreamingProfile); // 没有加水印的
        } else {
            mRTCStreamingManager.prepare(cameraStreamingSetting, null);
        }

        IConferenceUserEventListerner userEventListerner = new IConferenceUserEventListerner() {

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

        remoteViewA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        isClick = false;//当按下的时候设置isclick为false，具体原因看后边的讲解

                        startTime = System.currentTimeMillis();
                        System.out.println("执行顺序down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("执行顺序move");

                        isClick = true;//当按钮被移动的时候设置isclick为true
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > dm.widthPixels) {
                            right = dm.widthPixels;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > dm.heightPixels) {
                            bottom = dm.heightPixels;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = System.currentTimeMillis();
                        //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                        if ((endTime - startTime) > 0.1 * 1000L) {
                            isClick = true;
                        } else {
                            isClick = false;
                        }
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = v.getLeft();
                        params.topMargin = v.getTop();
                        params.setMargins(v.getLeft(), v.getTop(), 0, 0);
                        v.setLayoutParams(params);
                        System.out.println("执行顺序up");
                        break;
                }
                return isClick;
            }
        });
    }

    public void setCreaterTime(long time) {
        roomInfoView.setCreaterTime(LiveUtils.formatShowTime(time));
    }

    @Override
    protected void initIM() {
        super.initIM();
        if (isClosedBack()) {
            createRoom();
        } else {
            EEnterRoomComplete event = new EEnterRoomComplete();
            event.result = 1;
            event.roomId = getRoomId();
            event.isCreater = true;
            SDEventManager.post(event);
            //            createGroup(String.valueOf(getRoomId()), new TIMValueCallBack<String>() {
            //                @Override
            //                public void onError(int code, String desc) {
            //                    dealCreateGroupError(code, desc);
            //                }
            //
            //                @Override
            //                public void onSuccess(String s) {
            ////                    createRoom();
            //                    //        // 七牛临时, 主动去请求房间信息
            //                    EEnterRoomComplete event = new EEnterRoomComplete();
            //                    event.result = 1;
            //                    event.roomId = getRoomId();
            //                    event.isCreater = true;
            //                    SDEventManager.post(event);
            //
            //                }
            //            });
        }
    }

    protected void dealCreateGroupError(int code, String desc) {
        boolean result = createGroupTryer.tryRunDelayed(new Runnable() {
            @Override
            public void run() {
                initIM();
            }
        }, 3000);

        if (!result) {
            showCreateGroupErrorDialog(code, desc);
        }
    }

    protected void showCreateGroupErrorDialog(int code, String desc) {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent(getString(R.string.create_im_group_fail)).setTextCancel(null).setTextConfirm(getString(R.string.confirm));
        dialog.setCancelable(false);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
                requestUpdateLiveStateFail();
                exitRoom();
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                dialog.dismiss();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    private void createRoom() {
        //        liveSdk.enterRoom(getRoomId(), true);
    }

    public void onEventMainThread(EEnterRoomComplete event) {
        if (event.roomId != getRoomId()) {
            return;
        }
        //        if (liveSdk.getRoom() != null) {
        LogUtil.i("create room success");
        tv_danmu_connect_status.setText(R.string.connect_danmu_succeed);
        tv_danmu_connect_status.setVisibility(View.VISIBLE);
        LiveInformation.getInstance().enterRoom(getRoomId(), getGroupId(), getCreaterId());
        // 七牛临时，不用avsdk，可以把这段给去掉
        //            liveSdk.initAvUILayer(getApplication(), findViewById(android.R.id.content), getCreaterId());
        requestRoomInfo();
        //            // 录制
        //            TIMAvManager.RecordParam rp = newRecordParam();
        //            rp.setFilename(String.valueOf(getRoomId()));
        //            startRecord(rp, null);

        //            // 旁路直播
        //            TIMAvManager.StreamParam sp = newStreamParam();
        //            sp.setChannelName(String.valueOf(getRoomId()));
        //            sp.setChannelDescr("Android");
        //            sp.setEncode(TIMAvManager.StreamEncode.HLS_AND_RTMP);
        //            startPushStream(sp, new TIMValueCallBack<TIMAvManager.StreamRes>() {
        //
        //                @Override
        //                public void onSuccess(TIMAvManager.StreamRes res) {
        //                    requestUpdateLiveStateSuccess(String.valueOf(streamChannelIdBt));
        //                }
        //
        //                @Override
        //                public void onError(int code, String desc) {
        //                    requestUpdateLiveStateSuccess(null);
        //                }
        //            });
        //        } else {
        //            LogUtil.i("create room error:" + event.result);
        //            dealEnterRoomFail(event);
        //        }
    }

    public void onEventMainThread(ESurfaceCreated event) {
        sdkEnableFrontCamera();
    }

    public void onEventMainThread(EEnableCameraComplete event) {
        //        if (LiveUtils.isResultOk(event.result)) {
        //            if (event.enable) {
        //                if (isMuteMode) {
        //
        //                } else {
        //                    sdkEnableMic(true);
        //                }
        //                liveSdk.setLocalHasVideo(true);
        //                liveSdk.enableBeauty(true);
        //            }
        //        }
    }

    /**
     * 右侧面板打开的时候
     * 上下菜单栏需要隐藏
     *
     * @param menu
     */
    public void onEventMainThread(LiveHRightMenu menu) {
        if (menu.isShown()) {
            mLiveViewManager.hideMenu();
        } else {
            mLiveViewManager.showMenu();
        }
    }

    public void onEventMainThread(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            /*case R.id.iv_barrage:
                if (mRole == ROLE_CREATRE) {
                    mMsgPanel.toggle();
                } else if (mRole == ROLE_VIEWER) {
                    if (EventBus.getDefault().isRegistered(roomPopMsgView)) {
                        roomPopMsgView.hide();
                        EventBus.getDefault().unregister(roomPopMsgView);
                    } else {
                        roomPopMsgView.show();
                        EventBus.getDefault().register(roomPopMsgView);
                    }
                }
                mLiveViewManager.switchUpMenuSelectState(v);
                break;*/
            case R.id.iv_mute:
                // 设置静音推流
                isMuteMode = !isMuteMode;
                mRTCStreamingManager.mute(isMuteMode);
                mLiveViewManager.switchUpMenuSelectState(v);
                SDToast.showToast("静音推流已" + (isMuteMode ? "打开" : "关闭"));
                break;
            case R.id.iv_turn:
                if (mCurrentCamFacingIndex == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    if (mLightOn) {
                        mLightOn = false;
                        mRTCStreamingManager.turnLightOff();
                    }
                }
                qnSwitchCamera();
                mLiveViewManager.switchUpMenuSelectState(v);
                MobclickAgent.onEvent(LiveLandscapeCreatorActivity.this, "live_reversal");
                break;
            case R.id.iv_flash:
                if (mCurrentCamFacingIndex == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mLightOn = !mLightOn;
                    if (mLightOn) {
                        mRTCStreamingManager.turnLightOn();
                    } else {
                        mRTCStreamingManager.turnLightOff();
                    }
                    mLiveViewManager.switchUpMenuSelectState(v);
                } else {
                    SDToast.showToast(getString(R.string.front_camera_not_support_flashlight));
                }
                MobclickAgent.onEvent(LiveLandscapeCreatorActivity.this, "live_flash");
                break;
        }
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);
        mTvRoomId.setText(getString(R.string.room_id_is, actModel.getRoomId()));
        if (isClosedBack()) {
            final String groupId = actModel.getGroupId();
            joinGroup(groupId, new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    SDToast.showToast(getString(R.string.anchor_join_im_fail) + ":" + i + ":" + s);
                }

                @Override
                public void onSuccess() {
                    sendCreaterComebackMsg(groupId, null);
                    SDToast.showToast(getString(R.string.anchor_join_im) + ":" + groupId + getString(R.string.success));
                }
            });
        }
    }

    private void dealEnterRoomFail(final EEnterRoomComplete event) {
        //        boolean trySuccess = createRoomTryer.tryRunDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                LogUtil.i("try create room:" + createRoomTryer.getTryCount() + "," + event.result);
        //                createRoom();
        //            }
        //        }, 3000);
        //
        //        if (!trySuccess) {
        //            showCreateRoomErrorDialog(event.result);
        //        }
    }

    protected void showCreateRoomErrorDialog(int code) {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent(getString(R.string.create_live_fail)).setTextCancel(null).setTextConfirm(getString(R.string.confirm));
        dialog.setCancelable(false);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
                requestUpdateLiveStateFail();
                exitRoom();
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                dialog.dismiss();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    @Override
    protected void onMsgStopLive(MsgModel msg) {
        super.onMsgStopLive(msg);
        exitRoom();
    }

    private void exitRoom() {
        //        stopMusic();
        // 七牛临时,为了调试方便退出房间不消化im群组
        //destroyIM();
        //        if (liveSdk.getRoom() != null) {
        //            stopPushStream(null);

        // 狼人杀
        /*if (LRSManager.getInstance().mIsGaming && LRSManager.getInstance().mIsJoinGame) {
            CommonInterface.requestLRSOut(getRoomId(), null);
        }
        LRSManager.getInstance().reSet();*/


        // 七牛sdk
        stopConference(); // 停止连麦
        stopPublishStreaming(); // 七牛停止推流
        mRTCStreamingManager.stopCapture(); // 停止预览

        //        // 停止录制
        //        if (isRecording) {
        ////                tryStopRecord();
        //        } else {
        exitSdkRoom();
        //        }
        //        } else {
        //            finish();
        //        }
    }

    @Override
    protected void destroyIM() {
        super.destroyIM();

        final String groupId = getGroupId();
        sendCreaterQuitMsg(groupId, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                IMHelper.deleteLocalMessageGroup(groupId, null);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.deleteLocalMessageGroup(groupId, null);
            }
        });
        LRSManager.getInstance().quiteGroup();
    }

    /**
     * 结尾需要调用sdk的退出房间
     */
    private void tryStopRecord() {
        stopRecord(new TIMValueCallBack<List<String>>() {

            @Override
            public void onSuccess(List<String> list) {
                exitSdkRoom();
            }

            @Override
            public void onError(int code, String desc) {
                exitSdkRoom();
            }
        });

        new SDWaitRunner().condition(new SDWaitRunner.RunnableCondition() {

            @Override
            public boolean canRun() {
                return !isInStopRecord;
            }
        }).timeout(new Runnable() {

            @Override
            public void run() {
                SDToast.showToast(getString(R.string.end_recording_time_out));
                //                exitSdkRoom();
            }
        }).startWait();
    }

    private void exitSdkRoom() {
        //  liveSdk.exitRoom();
        addLiveFinish();
        roomInfoView.isFinishShow();
    }

    @Override
    protected void onBackground() {
        createrLeave();
        super.onBackground();
    }

    private void createrLeave() {
        if (!isCreaterLeave) {
            isCreaterLeave = true;
            requestUpdateLiveStateLeave();
            sdkDisableCamera();
            sdkEnableMic(false);
            sendCreaterLeaveMsg(getGroupId(), null);
        }
    }

    @Override
    protected void onResumeFromBackground() {
        createrComeback();
        super.onResumeFromBackground();
    }

    private void createrComeback() {
        if (isCreaterLeave) {
            isCreaterLeave = false;
            requestUpdateLiveStateComeback(null);
            sdkEnableLastCamera();
            if (isMuteMode) {

            } else {
                sdkEnableMic(true);
            }
            sendCreaterComebackMsg(getGroupId(), null);
        }
    }


    public void onEventMainThread(final EOnClickSmallVideo event) {
        //        LiveSmallVideoInfoCreaterDialog dialog = new LiveSmallVideoInfoCreaterDialog(this, event);
        //        dialog.show();
        LiveCreaterFinishDialog dialog = new LiveCreaterFinishDialog(this);
        dialog.setTextGravity(Gravity.CENTER);
        dialog.setTextContent(getString(R.string.if_close_current_small_video) + "？");
        dialog.setTextCancel(getString(R.string.no));
        dialog.setTextConfirm(getString(R.string.yes));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                IMHelper.sendMsgC2C(event.identifier, new CustomMsgCreaterStopVideo(), null);
                //                QavsdkControl.getInstance().setRemoteHasVideo(false, event.identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {

            }
        });
        dialog.show();
    }

    @Override
    protected void baseInit() {
        super.baseInit();
    }

    @Override
    protected void onMsgInviteVideo(MsgModel msg) {
        super.onMsgInviteVideo(msg);

        CustomMsgInviteVideo inviteMsg = msg.getCustomMsgInviteVideo();
        Log.i("invite", "creater onMsgInviteVideo 有人请求连麦" + inviteMsg.getSender().getUserId());
        if (LRSManager.getInstance().mIsGaming) {//如果正在游戏中 判断只能让玩家连麦
            // 如果主播没有进入直播间,就先进入直播间,否者声音合成不了
            Log.i("invite", "baseInit 主播进入连麦直播间---");
            startConferenceInternal();
            if (LRSManager.getInstance().userIsGamer(inviteMsg.getSender().getUserId())) {
                closeAllLianMai();// 关闭之前所有的连麦
                Log.i("invite", "游戏中接受某人请求");
                //                SDToast.showToast("creater onMsgInviteVideo");
                IMHelper.sendMsgC2C(inviteMsg.getSender().getUserId(), new CustomMsgAcceptVideo(), null);
            } else {
                Log.i("invite", "游戏中拒绝非游戏用户" + inviteMsg.getSender().getUserId() + "连麦请求");
                IMHelper.sendMsgC2C(inviteMsg.getSender().getUserId(), new CustomMsgRejectVideo(), null);
            }
        } else {
            Log.i("invite", "普通连麦");
            showReceiveInviteVideoDialog(inviteMsg);
        }
    }

    // 接受到连麦信息时,是否应该显示接受对话弹窗,如果连麦数到达了连麦上线不处理该消息
    private void showReceiveInviteVideoDialog(final CustomMsgInviteVideo msg) {

        // 七牛sdk
        if (mIsConferenceStarted) {
            /*if (null != names) {
                int videoCount = names.size();
                if (videoCount >= (LiveConstant.VIDEO_VIEW_MAX - 1)) {
                    IMHelper.sendMsgC2C(msg.getSender().getUserId(), new CustomMsgRejectVideo(), null);
                    return;
                }
            }*/
            int videoCount = mRTCStreamingManager.getParticipantsCount();
            if (videoCount >= LiveConstant.VIDEO_VIEW_MAX) {
                IMHelper.sendMsgC2C(msg.getSender().getUserId(), new CustomMsgRejectVideo(), null);
                return;
            }
        }
        LiveCreaterReceiveInviteVideoDialog inviteVideoDialog = new LiveCreaterReceiveInviteVideoDialog(this, msg);
        int padding = (int) (150 * dm.density);
        inviteVideoDialog.paddingLeft(padding);
        inviteVideoDialog.paddingRight(padding);
        inviteVideoDialog.setClickListener(new LiveCreaterReceiveInviteVideoDialog.ClickListener() {
            @Override
            public void onClickAccept() {
                startConference();
                SDHandlerManager.getBackgroundHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IMHelper.sendMsgC2C(msg.getSender().getUserId(), new CustomMsgAcceptVideo(), null);
                    }
                }, 100);

            }

            @Override
            public void onClickReject() {
                IMHelper.sendMsgC2C(msg.getSender().getUserId(), new CustomMsgRejectVideo(), null);
            }
        });
        inviteVideoDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                IMHelper.sendMsgC2C(msg.getSender().getUserId(), new CustomMsgRejectVideo(), null);
            }
        });
        inviteVideoDialog.show();

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
                startConferenceInternal();
            }
        });
        return true;
    }


    /**
     * 真正连麦执行的方法
     *
     * @return
     */
    private boolean startConferenceInternal() {
        Log.i("invite", "主播进入连麦房间---");
        CommonInterface.requestToken(getRoomId(), new AppRequestCallback<App_get_tokenActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    tooken = actModel.getRoomToken();
                    if (null == tooken || "".equals(tooken)) {
                        SDToast.showToast(getString(R.string.can_not_get_room_info), Toast.LENGTH_SHORT);
                        return;
                    }
                    mRTCStreamingManager.startConference(strUserId, getRoomId() + "", tooken, new RTCStartConferenceCallback() {
                        @Override
                        public void onStartConferenceSuccess() {
                            mIsConferenceStarted = true;
                            Log.i("invite", "主播进入连麦房间success--");
                            bindConferenceView();

                            /**
                             * Because `startConference` is called in child thread
                             * So we should check if the activity paused.
                             */
                            if (mIsActivityPaused) {
                                stopConference();
                            }


                        }

                        @Override
                        public void onStartConferenceFailed(final int errorCode) {
                            SDToast.showToast(getString(R.string.failed_to_start_conference) + errorCode, Toast.LENGTH_SHORT);
                            stopConference();
                            bindConferenceView();
                            Log.i("invite", "主播进入连麦房间 Failed --errorCode = " + errorCode);
                        }
                    });


                }
            }
        });


        return true;
    }

    /**
     * 停止连麦
     *
     * @return
     */
    private boolean stopConference() {
        if (!mIsConferenceStarted) {
            return true;
        }
        mRTCStreamingManager.stopConference();
        mIsConferenceStarted = false;
        //        SDToast.showToast(getString(R.string.stop_conference), Toast.LENGTH_SHORT);
        //        SDHandlerManager.getMainHandler().post(new Runnable() {
        //            @Override
        //            public void run() {
        //                tv_state.setText("CREATE");
        //            }
        //        });
        return true;
    }


    public void onEventMainThread(ERoomHasCameraVideo event) {
        for (String id : event.arrId) {
            if (!id.equals(getCreaterId())) {
                CommonInterface.requestStartLianmai(getRoomId(), id, null);
            }
        }
    }

    public void onEventMainThread(ERoomNoCameraVideo event) {
        for (String id : event.arrId) {
            if (!id.equals(getCreaterId())) {
                CommonInterface.requestStopLianmai(getRoomId(), id, null);
            }
        }
    }

    @Override
    protected void onMsgLRS(MsgModel msg) {
        CustomMsgLRS msg1 = msg.getCustomMsgLRS();
        switch (msg1.getStep()) {
            case 1://提示报名游戏
                LRSManager.getInstance().showSignBottomDialog(this, getRoomId(), true);
                break;
            case 2://提示可以开始游戏
                if (LRSManager.getInstance().mIsCreaterOutGame) {
                    return;
                }
                if (msg1.getNum() >= msg1.getDown_num() || LRSManager.getInstance().mIsShowingEnter) {
                    LRSManager.getInstance().showEnterBottomDialog(this, msg1.getNum(), msg1.getDown_num(), getRoomId());
                }
                break;
            case 3:
                LRSManager.getInstance().dismiss();
                //                SDToast.showToast("开始游戏超时");
                break;
            case 4://游戏开始
                LRSManager.getInstance().mIsGaming = true;
                closeAllLianMai();
                if (LRSManager.getInstance().getGameUsers().size() <= 0) {
                    LRSManager.getInstance().setGameUsers(msg1.getMembers());
                }
                LRSManager.getInstance().mGameGroupId = msg1.getGame_group_id();
                LRSManager.getInstance().mWolfGroupId = msg1.getWolf_group_id();
                roomLRSUserView.setVisibility(View.VISIBLE);
                //注意：主播创建的游戏频道和狼人频道  所以不需要手动进入频道了
                roomGameMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
                roomGameWolfMsgView.setGroupId(LRSManager.getInstance().mWolfGroupId);
                roomGameProgressMsgView.setGroupId(LRSManager.getInstance().mGameGroupId);
                roomGameProgressMsgView.setIsForGameProgress(true);
                roomGameRuleMsgView.initRule(msg1.getRule());
                SDEventManager.post(new ELRSGameStateChange(true));
                roomCreaterBottomView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        roomCreaterBottomView.performChannelClick();
                    }
                }, 1000);
                startConference();
                break;
            case 12://游戏结束
                LRSResultDialog dialog = new LRSResultDialog(this, msg1.getWin());
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        closeAllLianMai();
                        LRSManager.getInstance().quiteGroup();
                        LRSManager.getInstance().reSet();
                        roomCreaterBottomView.reSet();
                        roomLRSUserView.cleanData();
                        roomGameProgressMsgView.dissmissVoteDialog();
                        hideGameRelativeView();
                        cleanLRSMsgView();
                        SDEventManager.post(new ELRSGameStateChange(false));
                    }
                });
                break;
        }
    }

    private void hideGameRelativeView() {
        SDViewUtil.show(roomMsgView);
        SDViewUtil.hide(roomGameMsgView);
        SDViewUtil.hide(roomGameWolfMsgView);
        SDViewUtil.hide(roomGameProgressMsgView);
        SDViewUtil.hide(roomGameRuleMsgView);
        SDViewUtil.hide(roomLRSUserView);
    }

    private void cleanLRSMsgView() {
        roomGameMsgView.clearRoomMsg();
        roomGameWolfMsgView.clearRoomMsg();
        roomGameProgressMsgView.clearRoomMsg();
        roomGameRuleMsgView.clearRoomMsg();
    }

    /**
     * 把所有连麦的人都踢下线
     */
    private void closeAllLianMai() {
        //        AVUIControl avuiControl = liveSdk.getAVUIControl();
        //        if(avuiControl != null){
        //            if(avuiControl.id_view != null && avuiControl.id_view.size() > 0){
        //                for(Map.Entry<Integer,String> entry : avuiControl.id_view.entrySet()){
        //                    if(!entry.getValue().equals(getCreaterId())){
        //                        IMHelper.sendMsgC2C(entry.getValue(), new CustomMsgCreaterStopVideo(), null);
        //                        QavsdkControl.getInstance().setRemoteHasVideo(false, entry.getValue(), AVView.VIDEO_SRC_TYPE_CAMERA);
        //                    }
        //                }
        //            }
        //        }
        Log.i("invite", "主播踢人.....");
        names = mRTCStreamingManager.getParticipants();
        if (null != names && names.size() >= 1) {
            int counts = names.size();
            for (int i = 0; i < counts; i++) {
                boolean result = mRTCStreamingManager.kickoutUser(names.get(i));
                Log.i("invite", "主播提走连麦观众userid =" + names.get(i) + " result=" + (result == true ? "true" : "false"));
            }
        }
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
    protected void onMsgUpdateViewerNumber(CustomMsgOnlineNumber onlineNumber) {
        List<UserModel> viewerList = onlineNumber.getViewerList();
        viewerNumber = viewerList == null ? 0 : viewerList.size();
        mTvWatchNumber.setText(String.valueOf(viewerNumber));
    }

    @Override
    public void onEventMainThread(EUnLogin event) {
        exitRoom();
    }

    @Override
    public void onEventMainThread(EImOnForceOffline event) {
        exitRoom();
    }

    @Override
    public void onEventMainThread(EOnCallStateChanged event) {
        switch (event.state) {
            case TelephonyManager.CALL_STATE_RINGING:
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (isCreaterLeave) {
                    isCreaterLeaveByCall = false;
                } else {
                    isCreaterLeaveByCall = true;
                }
                createrLeave();
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (isCreaterLeaveByCall) {
                    createrComeback();
                    isCreaterLeaveByCall = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnect(TANetWorkUtil.netType type) {
        if (type == TANetWorkUtil.netType.mobile) {
            SDDialogConfirm dialog = new SDDialogConfirm(this);
            dialog.setTextContent(getString(R.string.current_network_is_not_wifi_if_continue) + "？").setTextCancel(getString(R.string.no)).setTextConfirm(getString(R.string.yes)).setmListener(new SDDialogCustom.SDDialogCustomListener() {

                @Override
                public void onDismiss(SDDialogCustom dialog) {
                }

                @Override
                public void onClickConfirm(View v, SDDialogCustom dialog) {
                }

                @Override
                public void onClickCancel(View v, SDDialogCustom dialog) {
                    exitRoom();
                }
            }).show();
        }
        super.onConnect(type);
    }

    public void onEventMainThread(EEnterOrExitRoomTimeout event) {
        SDToast.showToast(getString(R.string.operate_time_out));
        exitRoom();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    protected void onClickBottomClose(View v) {
        super.onClickBottomClose(v);
        showExitDialog();
    }

    private void showExitDialog() {
        //        if (liveSdk.getRoom() == null) {
        //            finish();
        //        } else {
        if (getRoomInfo() != null && getRoomInfo().isAuctioning()) {
            showNormalExitDialog();
            // showExitActionDialog();
        } else {
            showNormalExitDialog();
        }
        //        }
    }

    private void showNormalExitDialog() {
        LiveCreaterFinishDialog dialog = new LiveCreaterFinishDialog(this);
        int padding = (int) (135 * dm.density);
        dialog.paddingLeft(padding);
        dialog.paddingRight(padding);
        dialog.setTextContent(String.format(getString(R.string.if_ensure_finish_live), viewerNumber));
        dialog.setTextCancel(getString(R.string.ensure_finish_live));
        dialog.setTextConfirm(getString(R.string.continue_live));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {

            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
                exitRoom();
            }
        });
        dialog.show();
    }

    private void showExitActionDialog() {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("您发起的竞拍暂未结束，不能关闭直播");
        dialog.setTextConfirm(null);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
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

    @Override
    protected void showSetBeautyDialog() {
        //        七牛sdk

        // 1. 获取当前的数值
        CameraStreamingSetting.FaceBeautySetting faceBeautySetting = cameraStreamingSetting.getFaceBeautySetting();
        //
        //      float beautyLevel = (int) (faceBeautySetting.beautyLevel * 100);
        //      float redden = faceBeautySetting.redden;
        //      float whiten = faceBeautySetting.whiten;
        // 2. 设置dialog展示数值

        final LiveSetCameraBeautyDialogQN dialog = new LiveSetCameraBeautyDialogQN(this, faceBeautySetting.beautyLevel, faceBeautySetting.whiten, faceBeautySetting.redden);

        // 3. 设置监听
        dialog.setOnBeautyBuffingSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float buffing = progress / 100f;
                dialog.setBeautyLevel(buffing);
                CameraStreamingSetting.FaceBeautySetting newFaceBeautySetting = new CameraStreamingSetting.FaceBeautySetting(dialog.getBeautyLevel(), dialog.getWhiten(), dialog.getRedden());
                mRTCStreamingManager.updateFaceBeautySetting(newFaceBeautySetting);
                cameraStreamingSetting.setFaceBeautySetting(newFaceBeautySetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialog.setOnBeautyWhiteningSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float whitening = progress / 100f;
                dialog.setWhiten(whitening);
                CameraStreamingSetting.FaceBeautySetting newFaceBeautySetting = new CameraStreamingSetting.FaceBeautySetting(dialog.getBeautyLevel(), dialog.getWhiten(), dialog.getRedden());
                mRTCStreamingManager.updateFaceBeautySetting(newFaceBeautySetting);
                cameraStreamingSetting.setFaceBeautySetting(newFaceBeautySetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialog.setOnBeautyRubbySeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rubby = progress / 100f;
                dialog.setRedden(rubby);
                CameraStreamingSetting.FaceBeautySetting newFaceBeautySetting = new CameraStreamingSetting.FaceBeautySetting(dialog.getBeautyLevel(), dialog.getWhiten(), dialog.getRedden());
                mRTCStreamingManager.updateFaceBeautySetting(newFaceBeautySetting);
                cameraStreamingSetting.setFaceBeautySetting(newFaceBeautySetting);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        dialog.show();
    }

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

        // 七牛sdk,改用七牛后想调用闪关灯只能自己手动调用了
        if (mCurrentCamFacingIndex == Camera.CameraInfo.CAMERA_FACING_BACK) {
            if (enable) {
                mRTCStreamingManager.turnLightOn();
            } else {
                mRTCStreamingManager.turnLightOff();
            }
        } else {
            SDToast.showToast(getString(R.string.front_camera_not_support_flashlight));
        }

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
        //        liveSdk.onResume();
    }

    @Override
    protected void sdkOnPause() {
        //        liveSdk.onPause();
    }

    @Override
    protected void sdkOnStop() {

    }

    @Override
    protected void sdkOnDestroy() {
        //        liveSdk.onDestroy();
    }

    //    踢人

    public void onClickKickoutUserA(View v) {
        //        SDToast.showToast("onClickKickoutUserA");
        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewA);
        RefreshChat();
        bindConferenceView();
    }

    private void RefreshChat() {
        //        int participantsCount = mRTCStreamingManager.getParticipantsCount();
        //        if (participantsCount < 1) {
        //            stopConference();
        //        }
    }

    public void onClickKickoutUserB(View v) {
        //        SDToast.showToast("onClickKickoutUserA");
        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewB);
        RefreshChat();
        bindConferenceView();
    }

    public void onClickKickoutUserC(View v) {
        //        SDToast.showToast("onClickKickoutUserA");
        mRTCStreamingManager.kickoutUser(R.id.RemoteGLSurfaceViewC);
        RefreshChat();
        bindConferenceView();
    }

    /**
     * 点击远程连麦窗口A
     *
     * @param v
     */
    public void onClickRemoteWindowA(View v) {
        SDToast.showToast("onClickRemoteWindowA");
        FrameLayout window = (FrameLayout) v;
        if (window.getChildAt(0).getId() == qn_video_glview.getId()) {
            mRTCStreamingManager.switchRenderView(qn_video_glview, mRemoteWindowA.getGLSurfaceView());
        } else {
            mRTCStreamingManager.switchRenderView(mRemoteWindowA.getGLSurfaceView(), qn_video_glview);
        }
        bindConferenceView();
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
        //        bindConferenceView();

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
        //        bindConferenceView();

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
        //        names = mRTCStreamingManager.getParticipants();
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

    /*------------------主播弹幕窗口-------------------*/

    @Override
    public void onEventMainThread(EImOnNewMessages event) {
        super.onEventMainThread(event);
        if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_MISSION_RESULT) {
            CustomMsgMissionResult result = event.msg.getCustomMsgMissionResult();
            //            result = new CustomMsgMissionResult();
            //            result.setStatus((int) (System.currentTimeMillis() % 2));
            //            result.setMissionScore((int) (System.currentTimeMillis() % 10) + 1);
            //            result.setMedalNum((int) (System.currentTimeMillis() % 5) + 1);
            if (dialog == null) {
                dialog = new MissionResultDialog(this, result);
            } else {
                dialog.setResult(result);
            }
        }
    }

    /**
     * 获取当前正在了连麦的用户id
     */
    @Override
    public String getCurrentGuestId() {
        List<String> participants = mRTCStreamingManager.getParticipants();
        if (participants == null || participants.size() == 0) {
            return null;
        }
        return participants.get(0);
    }
}
