package com.fanwe.live.activity.room;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.appview.FireWorkImageView;
import com.fanwe.live.appview.room.RoomGiftGifView;
import com.fanwe.live.appview.room.RoomGiftPlayView;
import com.fanwe.live.appview.room.RoomInfoView;
import com.fanwe.live.appview.room.RoomPopMsgView;
import com.fanwe.live.appview.room.RoomSendGiftView;
import com.fanwe.live.appview.room.RoomTipsMsgView;
import com.fanwe.live.appview.room.RoomViewerFinishView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveInviteVideoDialog;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.dialog.MissionResultDialog;
import com.fanwe.live.event.EEnterRoomComplete;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ERoomInfoReady;
import com.fanwe.live.listeners.OnMarqueeListener;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_get_tokenActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgMissionResult;
import com.fanwe.live.model.custommsg.CustomMsgOnlineNumber;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.utils.LogUtils;
import com.fanwe.live.utils.StringUtils;
import com.fanwe.live.view.LiveHRightMenu;
import com.fanwe.live.view.LiveViewManager;
import com.fanwe.live.view.MsgPanel;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
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
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.TRUE;

/**
 * Created by yong.zhang on 2017/3/14 0014.
 */
public class LiveVideoActivity extends LiveLayoutActivity implements View.OnClickListener {

    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_LIVE_TYPE = "extra_live_type";
    public static final String EXTRA_PRIVATE_KEY = "extra_private_key";
    public static final String EXTRA_SEX = "extra_sex";

    /**
     * 直播的类型0-热门;1-最新
     */
    protected int type;
    /**
     * 私密直播的key
     */
    protected String strPrivateKey;
    /**
     * 性别0-全部，1-男，2-女
     */
    protected int sex;
    /**
     * 话题id
     */
    protected int cate_id;
    /**
     * 城市
     */
    protected String city;

    private LiveViewManager mLiveViewManager;

    public static final int ROLE_VIEWER = 0;

    public static final int ROLE_CREATRE = 1;


    public static final int CODE_BACK = 0;

    public static final int CODE_BARRAGE = 1;
    // 静音推流
    public static final int CODE_MUTE = 2;

    public static final int CODE_TURN = 3;

    public static final int CODE_FLASH = 4;
    // 连麦
    public static final int CODE_LIANMAI = 10;

    public static final int CODE_CHARGE = 11;

    public static final int CODE_DONATE = 12;

    public static final int CODE_MORE = 13;

    private MsgPanel mMsgPanel;

    @ViewInject(R.id.btnChangeOrientation)
    private ImageView btnChangeOrientation;

    @ViewInject(R.id.tv_deadline)
    private TextView mTvDeadline;

    @ViewInject(R.id.tv_room_id)
    private TextView mTvRoomId;

    @ViewInject(R.id.tv_watch_number)
    private TextView mTvWatchNumber;

    @ViewInject(R.id.fl_live_send_gift_fullscreen)
    private FrameLayout mFlSendGift;

    @ViewInject(R.id.iv_fire_work)
    private FireWorkImageView iv_fire_work;

    @ViewInject(R.id.mtv_marque)
    private RoomTipsMsgView tipsMsgView;
    //    @ViewInject(R.id.mtv_marque)
    //    private MarqueTextView mtv_marque;
    private int times;
    private RoomSendGiftView mRoomSendGiftView;
    private boolean isNeedShowFinish;
    private RoomViewerFinishView roomViewerFinishView;
    private int viewerNumber;
    private SDDialogConfirm dialog;

    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    // You must `StartConference` after `Ready`
                    showToast(getString(R.string.ready), Toast.LENGTH_SHORT);
                    requestToken();
                    break;
                case CONNECT_FAIL:
                    showToast(getString(R.string.failed_to_connect_rtc_server), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case VIDEO_PUBLISH_FAILED:
                case AUDIO_PUBLISH_FAILED:
                    showToast(getString(R.string.failed_to_publish_av_to_rtc) + extra, Toast.LENGTH_SHORT);
                    finish();
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    showToast(getString(R.string.success_publish_video_to_rtc), Toast.LENGTH_SHORT);
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    showToast(getString(R.string.success_publish_audio_to_rtc), Toast.LENGTH_SHORT);
                    break;
                case USER_JOINED_AGAIN:
                    showToast(getString(R.string.user_join_other_where), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case USER_KICKOUT_BY_HOST:
                    showToast(getString(R.string.user_kickout_by_host), Toast.LENGTH_SHORT);
                    //finish();
                    stopConference();
                    break;
                case OPEN_CAMERA_FAIL:
                    showToast(getString(R.string.failed_open_camera), Toast.LENGTH_SHORT);
                    stopConference();
                    break;
                case AUDIO_RECORDING_FAIL:
                    //                    showToast(getString(R.string.failed_open_microphone), Toast.LENGTH_SHORT);
                    LogUtil.e(getString(R.string.failed_open_microphone));
                    break;
                default:
                    return;
            }
        }
    };
    // 烟花持续时间
    private int FIRE_WORK_DURATION;//= getResources().getInteger(R.integer.fire_work_total_duration);
    private MissionResultDialog mMissionResultDialog;
    private long joinTimestamp = System.currentTimeMillis();
    private int lastX;
    private int lastY;
    private boolean isClick;
    private long startTime;
    private long endTime;

    private void showToast(final String text, final int duration) {
        if (mIsActivityPaused) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                Toast.makeText(getBaseContext(), text, duration).show();
            }
        });
    }

    private RTCUserEventListener mRTCUserEventListener = new RTCUserEventListener() {
        @Override
        public void onUserJoinConference(String remoteUserId) {
            LogUtils.logI("onUserJoinConference: " + remoteUserId);
        }

        @Override
        public void onUserLeaveConference(String remoteUserId) {
            LogUtils.logI("onUserLeaveConference: " + remoteUserId);
        }
    };

    private RTCRemoteWindowEventListener mRTCRemoteWindowEventListener = new RTCRemoteWindowEventListener() {
        @Override
        public void onRemoteWindowAttached(RTCVideoWindow window, String remoteUserId) {
            LogUtils.logI("onRemoteWindowAttached: " + remoteUserId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pausePlayer();
                }
            });
        }

        @Override
        public void onRemoteWindowDetached(RTCVideoWindow window, String remoteUserId) {
            LogUtils.logI("onRemoteWindowDetached: " + remoteUserId);
        }
    };

    /**
     * "code":"0000","message":"操作成功","returnObj":{"liveList":[
     * {
     * "robotNum":0,"createTime":null,"sort":null,"roomId":1,
     * "rtmpPlayUrl":"rtmp://pili-live-rtmp.qiankeep.com/mala/3",
     * "onlineStatus":1,"monitorTime":"2017-03-17 11:38:23","endTime":null
     * ,"pushUrl":"rtmp://pili-publish.qiankeep.com/mala/3?e=1489735325576&token=ZUdTZzOrAMrgTac4e2I-w_F2_NsMCU_IrLeE580r:kmZryiJtaaf8L7gLO6F4NvoxyhM=",
     * "city":"深圳市","isDelete":null,"isHorizontal":1,"watchNumber":0,
     * "userId":43,"province":"广东省","maxWatchNumber":0,"isAborted":null,
     * "virtualWatchNumber":0,"privateKey":null,"isNew":1,"categoryId":2,
     * "beginTime":"2017-03-17 11:38:10","isHot":1,"voteNumber":0,"liveIn":1,
     * "groupId":"@TGS#aLRV2ZNEE","roomType":3,"topicId":102,"tipoffCount":null,
     * "shareType":null}],"liveNum":1
     * }
     * }
     */


    private void initPlayerSetting(PLVideoView player) {
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        player.setAVOptions(options);
        player.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);

        // Set some listeners
        player.setOnInfoListener(mOnInfoListener);
        player.setOnCompletionListener(mOnCompletionListener);
        player.setOnErrorListener(mOnErrorListener);
    }

    private void initCaptureSetting(GLSurfaceView preview, FrameLayout window, GLSurfaceView remote) {
        /**
         * Step 1: init sdk, you can also move this to MainActivity.onCreate
         */
        RTCMediaStreamingManager.init(getApplicationContext());

        /**
         * Step 2: find & init views
         */

        /**
         * Step 3: config camera settings
         */
        CameraStreamingSetting.CAMERA_FACING_ID facingId = chooseCameraFacingId();
        if (mCameraStreamingSetting == null) {
            mCameraStreamingSetting = new CameraStreamingSetting();
        }
        mCameraStreamingSetting.setCameraFacingId(facingId)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true) // Using sdk built in face beauty algorithm
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)) // sdk built in face beauty settings
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off
        mCurrentCamFacingIndex = facingId.ordinal();

        /**
         * Step 4: create streaming manager and set listeners
         */
        if (mRTCStreamingManager == null) {
            mRTCStreamingManager = new RTCMediaStreamingManager(getApplicationContext(),
                    preview, AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);
            mRTCStreamingManager.prepare(mCameraStreamingSetting, null);
        }
        mRTCStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCStreamingManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
        mRTCStreamingManager.setUserEventListener(mRTCUserEventListener);
        mRTCStreamingManager.setDebugLoggingEnabled(false);
        mRTCStreamingManager.mute(true);

        /**
         * Step 5: set conference options
         */
        RTCConferenceOptions confOptions = new RTCConferenceOptions();
        // vice anchor can use a smaller size
        // RATIO_4_3 & VIDEO_ENCODING_SIZE_HEIGHT_240 means the output size is 320 x 240
        // 4:3 looks better in the mix frame
        confOptions.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_4_3);
        confOptions.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_240);
        // vice anchor can use a higher conference bitrate for better image quality
        confOptions.setVideoBitrateRange(300 * 1000, 800 * 1000);
        // 20 fps is enough
        confOptions.setVideoEncodingFps(20);
        confOptions.setHWCodecEnabled(false);
        mRTCStreamingManager.setConferenceOptions(confOptions);

        /**
         * Step 6: create the remote windows
         */
        if (mRTCVideoWindow == null) {
            mRTCVideoWindow = new RTCVideoWindow(window, remote);

            /**
             * Step 8: add the remote windows
             */
            mRTCStreamingManager.addRemoteWindow(mRTCVideoWindow);
        }

        /**
         * Step 9: do prepare, anchor should config streaming profile first
         */
        StreamingProfile mStreamingProfile = new StreamingProfile();
        mStreamingProfile.setEncodingOrientation(
                isLandscape() ? StreamingProfile.ENCODING_ORIENTATION.LAND : StreamingProfile.ENCODING_ORIENTATION.PORT);
        mRTCStreamingManager.setStreamingProfile(mStreamingProfile);
    }


    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            LogUtils.logI(what + "::" + extra);
            return false;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            showToastTips("Play Completed !");
            finish();
        }
    };

    private void showToastTips(final String tips) {
        if (mIsActivityPaused) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                Toast.makeText(getBaseContext(), tips, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    showToastTips("Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToastTips("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToastTips("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            if (isNeedReconnect) {
                //                sendReconnectMessage();
            } else {
                finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    private void changeOrientation() {
        int orientation = getRequestedOrientation();
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            btnChangeOrientation.setImageResource(R.drawable.ic_enter_fullscreen);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            btnChangeOrientation.setImageResource(R.drawable.ic_exit_fullscreen);
        }
    }

    private void addVerticalView() {
        mLiveViewManager.showV();

        int margin = (int) (10 * dm.density);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvWatchNumber.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, rlVideoContainer.getId());
        params.addRule(ALIGN_PARENT_RIGHT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.bottomMargin = margin;
        params.leftMargin = margin;
        params.topMargin = params.rightMargin = 0;
        mTvWatchNumber.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) mTvRoomId.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_BOTTOM, rlVideoContainer.getId());
        params.addRule(RelativeLayout.RIGHT_OF, mTvWatchNumber.getId());
        params.addRule(RelativeLayout.LEFT_OF, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        params.bottomMargin = margin;
        params.leftMargin = margin;
        params.topMargin = params.rightMargin = 0;
        mTvRoomId.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) mTvDeadline.getLayoutParams();
        params.addRule(ALIGN_PARENT_RIGHT, TRUE);
        params.addRule(RelativeLayout.LEFT_OF, 0);
        mTvDeadline.setLayoutParams(params);
        params.rightMargin = margin;
        params.topMargin = margin;
        params.bottomMargin = params.leftMargin = 0;

        int millis = 9 * 3600 * 1000 + 21 * 60 * 1000 + 15 * 1000;
        mTvDeadline.setText(StringUtils.formatDeadline(millis));

        SDViewUtil.removeViewFromParent(roomPopMsgView);

        findViewById(R.id.iv_back_vertical).setVisibility(View.VISIBLE);
    }

    private void addHorizonalView() {
        mLiveViewManager.showH();

        int margin = (int) (10 * dm.density);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvWatchNumber.getLayoutParams();
        params.addRule(ALIGN_PARENT_RIGHT, TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, 0);
        params.rightMargin = margin;
        params.topMargin = margin;
        params.leftMargin = params.bottomMargin = 0;
        mTvWatchNumber.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) mTvRoomId.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, mTvWatchNumber.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ABOVE, 0);
        params.rightMargin = margin;
        params.topMargin = margin;
        params.leftMargin = params.bottomMargin = 0;
        mTvRoomId.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) mTvDeadline.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, mTvRoomId.getId());
        params.addRule(ALIGN_PARENT_RIGHT, 0);
        mTvDeadline.setLayoutParams(params);
        params.rightMargin = margin;
        params.topMargin = margin;
        params.leftMargin = params.bottomMargin = 0;

        int millis = 9 * 3600 * 1000 + 21 * 60 * 1000 + 15 * 1000;
        mTvDeadline.setText(StringUtils.formatDeadline(millis));

        replaceView((ViewGroup) findViewById(R.id.fl_live_pop_msg_new), roomPopMsgView);

        findViewById(R.id.iv_back_vertical).setVisibility(View.INVISIBLE);
    }

    public void resizeScreen() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (isLandscape()) {
            attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void resizePlayer(View v) {
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (isLandscape()) {
            params.width = dm.widthPixels;
            params.height = dm.heightPixels;
            v.setLayoutParams(params);
            Log.e("zy_log", params.width + "/" + params.height + "::" + dm.widthPixels + "/" + dm.heightPixels);

            addHorizonalView();

            btnChangeOrientation.setVisibility(View.INVISIBLE);
            btnChangeOrientation.setOnClickListener(null);
        } else {
            params.width = dm.widthPixels;
            params.height = params.width * dm.widthPixels / dm.heightPixels;
            v.setLayoutParams(params);
            Log.e("zy_log", params.width + "/" + params.height + "::" + dm.widthPixels + "/" + dm.heightPixels);

            addVerticalView();

            btnChangeOrientation.setVisibility(View.VISIBLE);
            btnChangeOrientation.setOnClickListener(this);
        }
    }

    private boolean isLandscape() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public void onClickRemoteWindow(View v) {
        int id = mCameraWindow.getChildAt(0).getId();
        if (id == mCameraPreview.getId()) {//最小窗口为预览,准备切换到最大
            //预览切换到最大
            mCameraWindow.removeView(mCameraPreview);
            mRemoteWindow.removeView(mRemoteView);

            mCameraWindow.addView(mRemoteView, 0);
            mRemoteWindow.addView(mCameraPreview, 0);

            mRemoteView.setZOrderMediaOverlay(true);
        } else if (id == mRemoteView.getId()) {//最小窗口为主播，准备切换到最大
            //预览窗口切换到最小
            mCameraWindow.removeView(mRemoteView);
            mRemoteWindow.removeView(mCameraPreview);
            mCameraWindow.addView(mCameraPreview, 0);
            mRemoteWindow.addView(mRemoteView, 0);

            mCameraPreview.setZOrderMediaOverlay(true);
        }
    }

    public void onClickCloseWindow(View v) {
        stopConference();
        startPlayer();
    }


    /**
     * OnCompletionListener.onCompletion(PLMediaPlayer plMediaPlayer)
     *
     * @param plMediaPlayer
     */
    public void onEventMainThread(PLMediaPlayer plMediaPlayer) {
        finish();
    }

    /**
     * 横屏发送消息弹幕显示，竖屏发送消息列表显示
     *
     * @param msg
     */
    public void onEventMainThread(final CustomMsgPopMsg msg) {
        IMHelper.sendMsgGroup(getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                SDToast.showToast("发送消息失败,请稍候再试");
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
            }
        });


        //        AppRequestParams params = CommonInterface.requestPopMsgParams(getRoomId(), msg.getDesc(), getGroupId());
        //        AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_msgActModel>() {
        //            @Override
        //            protected void onSuccess(SDResponse resp) {
        //                LogUtils.logI("" + resp);
        //            }
        //
        //            @Override
        //            protected void onError(SDResponse resp) {
        //                LogUtils.logI("" + resp);
        //            }
        //        });
        //        if (mRole == ROLE_VIEWER) {
        //            int orientation = getRequestedOrientation();
        //            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        //            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        //            }
        //        } else if (mRole == ROLE_CREATRE) {
        //
        //        }
    }

    public void onEventMainThread(Integer what) {
        LogUtils.logI(String.valueOf(what));
        switch (what) {
            case CODE_BACK:
                onBackPressed();
                break;
            case CODE_BARRAGE:
                if (EventBus.getDefault().isRegistered(roomPopMsgView)) {
                    roomPopMsgView.hide();
                    EventBus.getDefault().unregister(roomPopMsgView);
                } else {
                    roomPopMsgView.show();
                    EventBus.getDefault().register(roomPopMsgView);
                }
                break;
            case CODE_CHARGE://充值
                showRechargeDialog();
                break;
            case CODE_DONATE://送礼
                showGiftView();
                mLiveViewManager.hideMenu();
                break;
            case CODE_MORE://更多

                break;
            case CODE_LIANMAI:
                if (mRTCStreamingManager.isConferenceStarted()) {
                    stopConference();
                } else {
                    showInviteVideoDialog();
                }
                break;
        }
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);
        SDEventManager.post(new ERoomInfoReady());
        mTvRoomId.setText(getString(R.string.room_id_is, getRoomId()));
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

    public void onEventMainThread(EEnterRoomComplete event) {
        if (event.roomId != getRoomId()) {
            return;
        }
        LiveInformation.getInstance().enterRoom(getRoomId(), getGroupId(), getCreaterId());
    }


    @Override
    public void onEventMainThread(EImOnNewMessages event) {
        super.onEventMainThread(event);
        if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_MISSION_RESULT) {
            CustomMsgMissionResult result = event.msg.getCustomMsgMissionResult();
            if (mMissionResultDialog == null) {
                mMissionResultDialog = new MissionResultDialog(this, result);
            } else {
                mMissionResultDialog.setResult(result);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mLiveViewManager.onBackPressed()) {
            return;
        }
        if (mMsgPanel.isShown()) {
            mMsgPanel.hide();
            return;
        }
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }

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

    @Override
    public void onClick(View v) {
        LogUtils.logI("--" + v);
        switch (v.getId()) {
            case R.id.btnChangeOrientation:
                changeOrientation();
                break;
            case R.id.iv_back_vertical:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onMsgUpdateViewerNumber(CustomMsgOnlineNumber onlineNumber) {
        List<UserModel> viewerList = onlineNumber.getViewerList();
        mTvWatchNumber.setText(String.valueOf(viewerList == null ? 0 : viewerList.size()));
    }

    /**
     * 接受连麦
     *
     * @param msg
     */
    @Override
    protected void onMsgAcceptVideo(MsgModel msg) {
        if (!mIsConferenceStarted) {
            inviteVideoDialog.dismiss();
            startConference();
        }
    }

    /**
     * 拒绝连麦
     *
     * @param msg
     */
    protected void onMsgRejectVideo(MsgModel msg) {
        dismissInviteVideoDialog();
        stopConference();
    }

    /**
     * 结束连麦
     *
     * @param msg
     */
    protected void onMsgStopVideo(MsgModel msg) {
        stopConference();
    }

    /**
     * 直播结束
     *
     * @param msg
     */
    protected void onMsgEndVideo(MsgModel msg) {
        super.onMsgEndVideo(msg);
        isNeedShowFinish = true;
        viewerNumber = msg.getCustomMsgEndVideo().getShow_num();
        exitRoom(true);
    }

    @Override
    protected void onMsgStopLive(MsgModel msg) {
        super.onMsgStopLive(msg);
        isNeedShowFinish = true;
        exitRoom(true);
    }

    /**
     * 退出房间
     */
    protected void exitRoom(boolean isNeedFinish) {
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
            finish();
        }
    }


    @Override
    protected void addLiveFinish() {
        if (getRoomInfo() != null) {
            removeView(roomViewerFinishView);
            roomViewerFinishView = new RoomViewerFinishView(this);
            int status = getRoomInfo().getStatus();
            if (status == 1) {
                roomViewerFinishView.setHasFollow(roomInfoView.getHasFollow());
            } else if (status == 2) {
                viewerNumber = getRoomInfo().getShow_num();
                roomViewerFinishView.setHasFollow(getRoomInfo().getHas_focus());
            }
            roomViewerFinishView.setViewerNumber(viewerNumber);
            addView(roomViewerFinishView);
        }
    }

    protected void showInviteVideoDialog() {
        dismissInviteVideoDialog();
        inviteVideoDialog = new LiveInviteVideoDialog(this);
        if (isLandscape()) {
            int padding = (int) (150 * dm.density);
            inviteVideoDialog.paddingLeft(padding);
            inviteVideoDialog.paddingRight(padding);
        }
        inviteVideoDialog.show();
    }

    public void dismissInviteVideoDialog() {
        if (inviteVideoDialog != null) {
            inviteVideoDialog.dismiss();
        }
    }

    private void showRechargeDialog() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(this);
        dialog.showCenter();
    }

    /**
     * 显示送礼的窗口
     */
    private void showGiftView() {
        if (mRoomSendGiftView == null) {
            mRoomSendGiftView = new RoomSendGiftView(this);
            mRoomSendGiftView.requestData(this);
            mRoomSendGiftView.bindUserData();
            MobclickAgent.onEvent(this, "live_gift");
            SDViewUtil.replaceView(mFlSendGift, mRoomSendGiftView);
        }
        mRoomSendGiftView.show(true);
    }

    private static final String TEST_ROOM_NAME = "1001";

    private static final String TEST_TOKEN = "QzdCUKE0lXmIJsvJ_yQJTeIsJYeK6liEdWAn9JuU:FAFA4PsZ0FX16JAmPxv1tluxg8A=:eyJyb29tX25hbWUiOiIxMDAxIiwidXNlcl9pZCI6ImFzZGFzZCIsInBlcm0iOiJhZG1pbiIsImV4cGlyZV9hdCI6MTc4NTYwMDAwMDAwMH0=";

    private static final String TEST_VIDEO_PATH = "rtmp://pili-live-rtmp.ps.qiniucdn.com/NIU7PS/malatv_test";

    private void startPlayer() {
        mVideoView.getSurfaceView().setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

    private void pausePlayer() {
        mVideoView.pause();
        mVideoView.stopPlayback();
        mVideoView.setVisibility(View.GONE);
        mVideoView.getSurfaceView().setVisibility(View.GONE);
    }

    private boolean startConference() {
        if (mRTCStreamingManager.isConferenceStarted()) {
            return true;
        }
        mCameraWindow.setVisibility(View.VISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mRTCStreamingManager.startCapture();
        return true;
    }

    private void requestToken() {
        if (!TextUtils.isEmpty(token)) {
            startConferenceInternal();
            return;
        }
        CommonInterface.requestToken(getRoomId(), new AppRequestCallback<App_get_tokenActModel>() {

                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (rootModel.isOk()) {
                            token = actModel.getRoomToken();
                            if (TextUtils.isEmpty(token)) {
                                SDToast.showToast(getString(R.string.can_not_get_room_info), Toast.LENGTH_SHORT);
                                return;
                            }
                            startConferenceInternal();
                        }
                    }
                }

        );
    }

    private boolean startConferenceInternal() {
        mRTCStreamingManager.startConference(strUserId, String.valueOf(getRoomId()), token, new RTCStartConferenceCallback() {
            @Override
            public void onStartConferenceSuccess() {
                showToast(getString(R.string.start_conference), Toast.LENGTH_SHORT);
                mIsConferenceStarted = true;
                /**
                 * Because `startConference` is called in child thread
                 * So we should check if the activity paused.
                 */
                if (mIsActivityPaused) {
                    stopConference();
                }
            }

            @Override
            public void onStartConferenceFailed(int errorCode) {
                mIsConferenceStarted = false;
                showToast(getString(R.string.failed_to_start_conference) + errorCode, Toast.LENGTH_SHORT);
            }
        });
        return true;
    }

    private boolean stopConference() {
        mRTCStreamingManager.stopCapture();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startPlayer();
                mCameraWindow.setVisibility(View.INVISIBLE);
                mCameraPreview.setVisibility(View.INVISIBLE);
            }
        });
        if (!mRTCStreamingManager.isConferenceStarted()) {
            return true;
        }
        mRTCStreamingManager.stopConference();
        mIsConferenceStarted = false;
        showToast(getString(R.string.stop_conference), Toast.LENGTH_SHORT);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        resizeScreen();
        resizePlayer(rlVideoContainer);

        int id = mCameraWindow.getChildAt(0).getId();
        if (id == mCameraPreview.getId()) {//当前最小窗口为预览窗口
            mCameraWindow.removeView(mCameraPreview);
            mCameraWindow.addView(mCameraPreview, 0);

            mRemoteWindow.removeView(mRemoteView);
            mRemoteWindow.addView(mRemoteView, 0);

            mCameraPreview.setZOrderMediaOverlay(true);
        } else if (id == mRemoteView.getId()) {//当前最小窗口为主播

            mRemoteView.setZOrderMediaOverlay(true);
        }

        RelativeLayout.LayoutParams remoteParams = new RelativeLayout.LayoutParams(
                SDViewUtil.dp2px(120), SDViewUtil.dp2px(160));
        remoteParams.addRule(ALIGN_PARENT_RIGHT);
        remoteParams.addRule(ALIGN_PARENT_BOTTOM);
        remoteParams.bottomMargin = SDViewUtil.dp2px(100);
        mCameraWindow.setLayoutParams(remoteParams);

        initCaptureSetting(mCameraPreview, mRemoteWindow, mRemoteView);
    }

    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                requestRoomInfo();
            }

            @Override
            public void onSuccess() {
                sendViewerJoinMsg(groupId, null);
                requestRoomInfo();
            }
        });
    }

    @Override
    protected SDRequestHandler requestRoomInfo() {
        return CommonInterface.requestRoomInfo(getRoomId(), 0, type, 1, sex, cate_id, city, strPrivateKey, null, new AppRequestCallback<App_get_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                setRoomInfo(actModel);
                //SDViewBinder.setTextView(scollView.getTv_user_number_left(), String.valueOf(getRoomInfo().getPodcast().getUserId()));
                if (rootModel.isOk()) {
                    onSuccessRequestRoomInfo(actModel);
                } else {
                    onErrorRequestRoomInfo(actModel);
                }
            }
        });
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_video;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        //        initLayout(getWindow().getDecorView());

        type = getIntent().getIntExtra(EXTRA_LIVE_TYPE, 0);
        strPrivateKey = getIntent().getStringExtra(EXTRA_PRIVATE_KEY);
        sex = getIntent().getIntExtra(EXTRA_SEX, 0);
        city = getIntent().getStringExtra(EXTRA_CITY);

        mRole = ROLE_VIEWER;
        FrameLayout flTabContainer = (FrameLayout) findViewById(R.id.flTabContainer);
        FrameLayout flMenuContainer = (FrameLayout) findViewById(R.id.flMenuContainer);
        mLiveViewManager = new LiveViewManager(flTabContainer, flMenuContainer,
                getSupportFragmentManager(), mRole);
        mMsgPanel = new MsgPanel(flMenuContainer);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        findViewById(R.id.view_vertical_scroll).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_room_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_lrs_user_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_lrs_reminder).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_room_star).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_gift_play).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_tips_msg).setVisibility(View.INVISIBLE);
        FIRE_WORK_DURATION = getResources().getInteger(R.integer.fire_work_total_duration);
        tipsMsgView.setListener(new OnMarqueeListener() {
                                    @Override
                                    public void onMarqueeStart(int status) {
                                        if (status == 1) {
                                            iv_fire_work.explode(FIRE_WORK_DURATION);
                                        }
                                    }

                                    @Override
                                    public void onMarquessEnd() {
                                        iv_fire_work.stopAndGone();
                                    }
                                }

        );

        findViewById(R.id.fl_live_viewer_join_room).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_msg).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_msg_game).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_msg_game_wolf).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_msg_game_progress).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_msg_game_rule).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_send_msg).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_bottom_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_heart).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_heart_right).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_gift_gif).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_auction_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.fl_live_auction_rank_list).setVisibility(View.INVISIBLE);

        mVideoView = (PLVideoView) findViewById(R.id.VideoView);
        mLoadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(mLoadingView);

        initPlayerSetting(mVideoView);

        mCameraWindow = (FrameLayout) findViewById(R.id.CameraWindow);
        mCameraPreview = (GLSurfaceView) findViewById(R.id.CameraPreview);

        mRemoteWindow = (FrameLayout) findViewById(R.id.RemoteWindow);
        mRemoteView = (GLSurfaceView) findViewById(R.id.RemoteView);

        initCaptureSetting(mCameraPreview, mRemoteWindow, mRemoteView);

        rlVideoContainer = findViewById(R.id.rlContainer);

        FrameLayout flGiftPlay = (FrameLayout) findViewById(R.id.fl_live_gift_play_new);
        roomGiftPlayView = new RoomGiftPlayView(this);
        replaceView(flGiftPlay, roomGiftPlayView);
        roomGiftGifView = new RoomGiftGifView(this);
        replaceView((ViewGroup) findViewById(R.id.fl_live_gift_gif_new), roomGiftGifView);

        roomPopMsgView = new RoomPopMsgView(this);

        resizePlayer(rlVideoContainer);

        mVideoPath = getIntent().getStringExtra(LiveViewerActivity.EXTRA_VIDEOPATH);

        //        strUserId = String.valueOf(new Random().nextInt(100));
        //        mVideoPath = TEST_VIDEO_PATH;
        //        setRoomId(Integer.valueOf(TEST_ROOM_NAME));
        //        token = TEST_TOKEN;
        // 房间信息
        roomInfoView = new RoomInfoView(this);

        mTvRoomId.setText("房间号: 加载中...");
        mTvWatchNumber.setText("加载中...");

        initIM();

        findViewById(R.id.iv_back_vertical).setOnClickListener(this);
        mCameraWindow.setOnTouchListener(new View.OnTouchListener() {
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
                        //当从点击到弹起小于0.1的时候,则判断为点击,如果超过则不响应点击事件
                        if ((endTime - startTime) > 0.1 * 1000L) {
                            isClick = true;
                        } else {
                            isClick = false;
                        }
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                SDViewUtil.dp2px(120), SDViewUtil.dp2px(160));
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

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        if (mRTCStreamingManager.isConferenceStarted()) {
            mRTCStreamingManager.startCapture();
            return;
        }
        startPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActivityPaused = true;
        pausePlayer();
        /**
         * Step 11: You must stop capture, stop conference, stop streaming when activity paused
         */
        mRTCStreamingManager.stopCapture();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopConference();
        mRTCStreamingManager.destroy();
        RTCMediaStreamingManager.deinit();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (EventBus.getDefault().isRegistered(roomPopMsgView)) {
            EventBus.getDefault().unregister(roomPopMsgView);
        }
    }

    private CameraStreamingSetting mCameraStreamingSetting;

    private volatile boolean mIsConferenceStarted = false;

    private boolean mIsActivityPaused = true;

    private View mLoadingView;

    private PLVideoView mVideoView;

    private DisplayMetrics dm = new DisplayMetrics();

    private FrameLayout mCameraWindow;

    private GLSurfaceView mCameraPreview;

    private FrameLayout mRemoteWindow;

    private GLSurfaceView mRemoteView;

    private RTCVideoWindow mRTCVideoWindow;

    private int mCurrentCamFacingIndex;

    private RTCMediaStreamingManager mRTCStreamingManager;

    private View rlVideoContainer;

    private String mVideoPath;

    private String token;

    protected LiveInviteVideoDialog inviteVideoDialog;

    //    private FrameLayout flGiftPlay;

}
