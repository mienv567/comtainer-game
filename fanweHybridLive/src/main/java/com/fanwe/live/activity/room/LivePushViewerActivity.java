package com.fanwe.live.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EScrollChangeRoomComplete;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.custommsg.MsgModel;
import com.pili.pldroid.player.PLMediaPlayer;
import com.sunday.eventbus.SDEventManager;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 推流直播间观众界面
 */
public class LivePushViewerActivity extends LivePlayActivity
{

    private static final long DELAY_JOIN_ROOM = 1000;

    /**
     * 是否正在延迟加入房间
     */
    protected boolean isInDelayJoinRoom = false;
    private SDRequestHandler requestHandler;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.act_live_push_viewer;
    }

    //    七牛相关

    @Override
    protected void initVideoListener() {
        super.initVideoListener();

        mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                LogUtil.e( "onInfo: " + what + ", " + extra);
                return false;
            }
        };

        mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
                boolean isNeedReconnect = false;
                LogUtil.e( "Error happened, errorCode = " + errorCode);
                switch (errorCode) {
                    case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                        SDToast.showToast("Invalid URL !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                        SDToast.showToast("404 resource not found !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                        SDToast.showToast("Connection refused !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                        SDToast.showToast("Connection timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                        SDToast.showToast("Empty playlist !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                        SDToast.showToast("Stream disconnected !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                        SDToast.showToast("Network IO Error !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                        SDToast.showToast("Unauthorized Error !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                        SDToast.showToast("Prepare timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                        SDToast.showToast("Read frame timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                        break;
                    default:
                        SDToast.showToast("unknown error !");
                        break;
                }
                // Todo pls handle the error status here, reconnect or call finish()
                if (isNeedReconnect) {
                    sendReconnectMessage();
                } else {
                    finish();
                }
                // Return true means the error has been handled
                // If return false, then `onCompletion` will be called
                return true;
            }
        };


        mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                LogUtil.e( "Play Completed !");
                SDToast.showToast("Play Completed !");
                finish();
            }
        };

    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);

        setVideoView((TXCloudVideoView) find(R.id.view_video));

        if (validateParams(getRoomId(), getGroupId(), getCreaterId()))
        {
            initIM();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        int oldRoomId = getRoomId();
        int newRoomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        if (newRoomId != oldRoomId)
        {
            setIntent(intent);
            exitRoom(false);
            init(null);
        }
        super.onNewIntent(intent);
    }

    protected boolean validateParams(int roomId, String groupId, String createrId)
    {
        if (roomId <= 0)
        {
            SDToast.showToast("房间id为空");
            finish();
            return false;
        }

        if (isEmpty(groupId))
        {
            SDToast.showToast("聊天室id为空");
            finish();
            return false;
        }

        if (isEmpty(createrId))
        {
            SDToast.showToast("主播id为空");
            finish();
            return false;
        }
        setRoomId(roomId);
        setGroupId(groupId);
        setCreaterId(createrId);

        return true;
    }

    @Override
    protected void initIM()
    {
        super.initIM();

        final String groupId = getGroupId();
        joinGroup(groupId, new TIMCallBack()
        {
            @Override
            public void onError(int code, String desc)
            {
                onErrorJoinGroup(code, desc);
            }

            @Override
            public void onSuccess()
            {
                onSuccessJoinGroup(groupId);
            }
        });
    }

    /**
     * 加入聊天组失败
     */
    public void onErrorJoinGroup(int code, String desc)
    {
        showJoinGroupErrorDialog(code, desc);
    }

    protected void showJoinGroupErrorDialog(int code, String desc)
    {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("加入聊天组失败，是否重试");
        dialog.setCancelable(false);
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener()
        {

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                initIM();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
                exitRoom(true);
            }
        });
        dialog.show();
    }

    /**
     * 加入聊天组成功
     */
    protected void onSuccessJoinGroup(String groupId)
    {
        sendViewerJoinMsg(groupId, null);

        if (isScrollChangeRoom)
        {
            //不用请求requestRoomInfo()，切换的时候已经请求
            showViewerLayout();
            EScrollChangeRoomComplete eventChangeRoomComplete = new EScrollChangeRoomComplete();
            SDEventManager.post(eventChangeRoomComplete);
        } else
        {
            requestRoomInfo();
        }
    }

    @Override
    protected void onMsgEndVideo(MsgModel msg)
    {
        super.onMsgEndVideo(msg);
        exitRoom(false);
    }

    @Override
    protected void onMsgStopLive(MsgModel msg)
    {
        super.onMsgStopLive(msg);
        SDToast.showToast(msg.getCustomMsgStopLive().getDesc());
        exitRoom(true);
    }

    /**
     * 退出房间
     *
     * @param isNeedFinish
     */
    protected void exitRoom(boolean isNeedFinish)
    {
        destroyIM();
        if (isNeedShowFinish)
        {
            addLiveFinish();
            return;
        }

        if (isNeedFinish)
        {
            finish();
        }
    }

    @Override
    protected void destroyIM()
    {
        super.destroyIM();

        final String groupId = getGroupId();
        sendViewerQuitMsg(groupId, new TIMValueCallBack<TIMMessage>()
        {
            @Override
            public void onError(int i, String s)
            {
                IMHelper.deleteLocalMessageGroup(groupId, null);
                quitGroup(groupId, null);
            }

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                IMHelper.deleteLocalMessageGroup(groupId, null);
                quitGroup(groupId, null);
            }
        });
    }

    @Override
    protected void onVerticalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        super.onVerticalScroll(e1, e2, distanceX, distanceY);
        if (isInDelayJoinRoom)
        {
            startJoinRoomRunnable(true);
        }
    }

    @Override
    protected void afterVerticalScroll(boolean top)
    {
        super.afterVerticalScroll(top);
        if (requestHandler != null)
        {
            requestHandler.cancel();
        }
        exitRoom(false);
        showLoadingVideo();
        invisibleViewerLayout();
        roomMsgView.clearRoomMsg();
        requestHandler = requestRoomInfo();
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel)
    {
        super.onSuccessRequestRoomInfo(actModel);
        if (isScrollChangeRoom)
        {
            int rId = actModel.getRoomId();
            String gId = actModel.getGroupId();
            String userId = actModel.getPodcast().getUserId();

            if (validateParams(rId, gId, userId))
            {
                startJoinRoomRunnable(true);
            }
        } else
        {
            playUrl(actModel.getPlay_url());
        }
    }

    @Override
    protected void onErrorRequestRoomInfo(App_get_videoActModel actModel)
    {
        super.onErrorRequestRoomInfo(actModel);
        if (actModel.isVideoStoped())
        {
            addLiveFinish();
        } else
        {
            if (!isScrollChangeRoom)
            {
                exitRoom(true);
            }
        }
    }

    private void startJoinRoomRunnable(boolean delay)
    {
        if (delay)
        {
            isInDelayJoinRoom = true;
            SDHandlerManager.getMainHandler().removeCallbacks(joinRoomRunnable);
            SDHandlerManager.getMainHandler().postDelayed(joinRoomRunnable, DELAY_JOIN_ROOM);
        } else
        {
            joinRoomRunnable.run();
        }
    }

    /**
     * 加入房间runnable
     */
    private Runnable joinRoomRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            isInDelayJoinRoom = false;
            initIM();

            if (getRoomInfo() != null)
            {
                String url = getRoomInfo().getPlay_url();
                playUrl(url);
            }
        }
    };

    protected void playUrl(String playUrl)
    {
        if (validatePlayUrl(playUrl))
        {
            player.setUrl(playUrl);
            player.startPlay();
        }
    }

    @Override
    public void onPlayBegin(int event, Bundle param)
    {
        hideLoadingVideo();
        super.onPlayBegin(event, param);
    }

    protected boolean validatePlayUrl(String playUrl)
    {
        if (TextUtils.isEmpty(playUrl))
        {
            SDToast.showToast("未找到直播地址");
            return false;
        }

        if (playUrl.startsWith("rtmp://"))
        {
            player.setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.endsWith(".flv"))
        {
            player.setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        } else
        {
            SDToast.showToast("播放地址不合法");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed()
    {
        showExitDialog();
    }

    @Override
    protected void onClickBottomClose(View v)
    {
        exitRoom(true);
    }

    private void showExitDialog()
    {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent("确定要退出吗？");
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


    public void onEventMainThread(EUnLogin event)
    {
        exitRoom(true);
    }

    public void onEventMainThread(EImOnForceOffline event)
    {
        exitRoom(true);
    }

    public void onEventMainThread(EOnCallStateChanged event)
    {
        switch (event.state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                sdkEnableAudioDataVolume(false);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                sdkEnableAudioDataVolume(false);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                sdkEnableAudioDataVolume(true);
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
    protected void sdkDisableCamera()
    {
    }

    @Override
    protected void sdkEnableAudioDataVolume(boolean enable)
    {
        player.setMute(!enable);
    }

    @Override
    protected void sdkEnableBackCamera()
    {
    }

    @Override
    protected void sdkEnableFlash(boolean enable)
    {
    }

    @Override
    protected void sdkEnableFrontCamera()
    {
    }

    @Override
    protected void sdkEnableLastCamera()
    {
    }

    @Override
    protected void sdkEnableMic(boolean enable)
    {
    }

    @Override
    protected void sdkSwitchCamera()
    {
    }

    @Override
    protected void sdkEnableBeauty(boolean enable)
    {
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
