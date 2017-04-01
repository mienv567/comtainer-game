package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTryRunner;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.dialog.LiveSetCameraBeautyDialog;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.custommsg.MsgModel;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 推流直播主播界面
 */
public class LivePushCreaterActivity extends LiveLayoutCreaterActivity implements ITXLivePushListener
{

    private TXCloudVideoView videoView;
    private TXLivePusher livePusher;
    private TXLivePushConfig liveConfig;

    protected boolean isCreaterLeaveByCall = false;
    /**
     * 是否已经启动推流
     */
    private boolean isVideoPublish;
    private SDTryRunner createGroupTryer = new SDTryRunner();

    @Override
    protected int onCreateContentView()
    {
        return R.layout.act_live_push_creater;
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);

        if (getRoomId() <= 0)
        {
            SDToast.showToast("房间id为空");
            finish();
            return;
        }

        videoView = find(R.id.view_video);

        livePusher = new TXLivePusher(this);

        initLivePush();

        setCreaterId(strUserId);
        initLayout(getWindow().getDecorView());

        initIM();
    }

    @Override
    protected void initIM()
    {
        super.initIM();
        if (isClosedBack())
        {
            requestRoomInfo();
        } else
        {
            createGroup(String.valueOf(getRoomId()), new TIMValueCallBack<String>()
            {
                @Override
                public void onError(int code, String desc)
                {
                    dealCreateGroupError(code, desc);
                }

                @Override
                public void onSuccess(String s)
                {
                    requestUpdateLiveStateSuccess(null);
                    requestRoomInfo();
                }
            });
        }
    }


    protected void dealCreateGroupError(int code, String desc)
    {
        boolean result = createGroupTryer.tryRunDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                initIM();
            }
        }, 3000);

        if (!result)
        {
            showCreateGroupErrorDialog(code, desc);
        }
    }

    protected void showCreateGroupErrorDialog(int code, String desc)
    {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("创建聊天组失败，请退出重试").setTextCancel(null).setTextConfirm("确定");
        dialog.setCancelable(false);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener()
        {

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
                requestUpdateLiveStateFail();
                exitRoom(false);
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                dialog.dismiss();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
            }
        });
        dialog.show();
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel)
    {
        super.onSuccessRequestRoomInfo(actModel);
        if (isClosedBack())
        {
            final String groupId = actModel.getGroupId();
            joinGroup(groupId, new TIMCallBack()
            {
                @Override
                public void onError(int i, String s)
                {

                }

                @Override
                public void onSuccess()
                {
                    sendCreaterComebackMsg(groupId, null);
                }
            });
        }

        startPush(actModel.getPush_rtmp());
    }

    /**
     * 初始化推流设置
     */
    protected void initLivePush()
    {
        liveConfig = new TXLivePushConfig();

        liveConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
        //自适应码率
        liveConfig.setAutoAdjustBitrate(true);
        liveConfig.setMaxVideoBitrate(1000);
        liveConfig.setMinVideoBitrate(500);
        liveConfig.setVideoBitrate(700);
        liveConfig.setTouchFocus(false);

        livePusher.setConfig(liveConfig);

        sdkEnableBeauty(true);
    }

    /**
     * 开始推流
     */
    protected void startPush(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            SDToast.showToast("推流地址为空");
            return;
        }

        livePusher.setPushListener(this);
        livePusher.startCameraPreview(videoView);
        livePusher.startPusher(url);
        livePusher.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);

        isVideoPublish = true;
    }

    /**
     * 停止推流
     */
    protected void stopPush()
    {
        livePusher.stopCameraPreview(true);
        livePusher.setPushListener(null);
        livePusher.stopPusher();

        isVideoPublish = false;
    }

    /**
     * 暂停推流
     */
    protected void pausePush()
    {
        videoView.onPause();

        if (isVideoPublish)
        {
            livePusher.stopCameraPreview(false);
            livePusher.pausePusher();
        }
    }

    /**
     * 恢复推流
     */
    protected void resumePush()
    {
        videoView.onResume();

        if (isVideoPublish)
        {
            livePusher.resumePusher();
            livePusher.startCameraPreview(videoView);
        }
    }

    @Override
    public void onPushEvent(int event, Bundle bundle)
    {
        String msg = bundle.getString(TXLiveConstants.EVT_DESCRIPTION);
        LogUtil.i("event:" + event + "," + msg);
    }

    @Override
    public void onNetStatus(Bundle bundle)
    {

    }

    protected String getNetStatusString(Bundle status)
    {
        String str = String.format("%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT) + "|" + status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP));
        return str;
    }

    @Override
    protected void onMsgStopLive(MsgModel msg)
    {
        super.onMsgStopLive(msg);
        exitRoom(false);
    }

    protected void exitRoom(boolean addLiveFinish)
    {
        stopPush();
        stopMusic();
        destroyIM();

        if (addLiveFinish)
        {
            addLiveFinish();
        } else
        {
            finish();
        }
    }

    @Override
    protected void destroyIM()
    {
        super.destroyIM();

        final String groupId = getGroupId();
        sendCreaterQuitMsg(groupId, new TIMValueCallBack<TIMMessage>()
        {
            @Override
            public void onError(int i, String s)
            {
                IMHelper.deleteLocalMessageGroup(groupId, null);
            }

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                IMHelper.deleteLocalMessageGroup(groupId, null);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopPush();
        videoView.onDestroy();
    }

    @Override
    protected void onBackground()
    {
        createrLeave();
        super.onBackground();
    }

    private void createrLeave()
    {
        if (!isCreaterLeave)
        {
            isCreaterLeave = true;
            requestUpdateLiveStateLeave();
            pausePush();
            sendCreaterLeaveMsg(getGroupId(), null);
        }
    }

    @Override
    protected void onResumeFromBackground()
    {
        createrComeback();
        super.onResumeFromBackground();
    }

    private void createrComeback()
    {
        if (isCreaterLeave)
        {
            isCreaterLeave = false;
            requestUpdateLiveStateComeback(null);
            resumePush();

            sdkEnableMic(!isMuteMode);
            sendCreaterComebackMsg(getGroupId(), null);
        }
    }

    @Override
    public void onEventMainThread(EUnLogin event)
    {
        exitRoom(false);
    }

    @Override
    public void onEventMainThread(EImOnForceOffline event)
    {
        exitRoom(false);
    }

    @Override
    public void onEventMainThread(EOnCallStateChanged event)
    {
        switch (event.state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (isCreaterLeave)
                {
                    isCreaterLeaveByCall = false;
                } else
                {
                    isCreaterLeaveByCall = true;
                }
                createrLeave();
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (isCreaterLeaveByCall)
                {
                    createrComeback();
                    isCreaterLeaveByCall = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnect(TANetWorkUtil.netType type)
    {
        if (type == TANetWorkUtil.netType.mobile)
        {
            SDDialogConfirm dialog = new SDDialogConfirm(this);
            dialog.setTextContent("当前处于数据网络下，会耗费较多流量，是否继续？").setTextCancel("否").setTextConfirm("是").setmListener(new SDDialogCustom.SDDialogCustomListener()
            {

                @Override
                public void onDismiss(SDDialogCustom dialog)
                {
                }

                @Override
                public void onClickConfirm(View v, SDDialogCustom dialog)
                {
                }

                @Override
                public void onClickCancel(View v, SDDialogCustom dialog)
                {
                    exitRoom(true);
                }
            }).show();
        }
        super.onConnect(type);
    }

    @Override
    public void onBackPressed()
    {
        showExitDialog();
    }

    @Override
    protected void onClickBottomClose(View v)
    {
        super.onClickBottomClose(v);
        showExitDialog();
    }

    private void showExitDialog()
    {
        showNormalExitDialog();
    }

    private void showNormalExitDialog()
    {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("确定要结束直播吗？");
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener()
        {

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                exitRoom(true);
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
            }
        });
        dialog.show();
    }

    @Override
    protected void showSetBeautyDialog()
    {
        LiveSetCameraBeautyDialog dialog = new LiveSetCameraBeautyDialog(this);
        int progress = AppRuntimeWorker.getBeautyProgress();
        dialog.setBeautyProgress(progress);
        dialog.setOnBeautySeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int realValue = AppRuntimeWorker.getRealBeautyProgressPush(progress);
                livePusher.setBeautyFilter(realValue, 0);
                AppRuntimeWorker.setBeautyProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        dialog.show();
    }

    @Override
    protected void sdkDisableCamera()
    {
        pausePush();
    }

    @Override
    protected void sdkEnableAudioDataVolume(boolean enable)
    {

    }

    @Override
    protected void sdkEnableBackCamera()
    {
    }

    @Override
    protected void sdkEnableFlash(boolean enable)
    {
        livePusher.turnOnFlashLight(enable);
    }

    @Override
    protected void sdkEnableFrontCamera()
    {
        resumePush();
    }

    @Override
    protected void sdkEnableLastCamera()
    {
        resumePush();
    }

    @Override
    protected void sdkEnableMic(boolean enable)
    {
        livePusher.setMute(!enable);
    }

    @Override
    protected void sdkSwitchCamera()
    {
        livePusher.switchCamera();
    }

    @Override
    protected void sdkEnableBeauty(boolean enable)
    {
        if (enable)
        {
            int progress = AppRuntimeWorker.getBeautyProgress();
            int realValue = AppRuntimeWorker.getRealBeautyProgressPush(progress);
            livePusher.setBeautyFilter(realValue, 0);
        } else
        {
            livePusher.setBeautyFilter(0, 0);
        }
    }

    @Override
    protected void sdkOnResume()
    {
    }

    @Override
    protected void sdkOnPause()
    {
    }

    @Override
    protected void sdkOnStop()
    {
    }

    @Override
    protected void sdkOnDestroy()
    {
    }

}
