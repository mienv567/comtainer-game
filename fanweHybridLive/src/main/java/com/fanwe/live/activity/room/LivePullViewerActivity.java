package com.fanwe.live.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.appview.room.RoomChannelView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.RTMPPlayerManager;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.RoomChannelModel;
import com.fanwe.live.model.custommsg.CustomMsgChangeChannel;
import com.fanwe.live.view.SDVerticalScollView;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 观众 - 拉流看直播
 */
public class LivePullViewerActivity extends LiveLayoutViewerActivity implements RTMPPlayerManager.PlayerListener {
    private TXCloudVideoView videoView;

    protected RTMPPlayerManager player;
    private RoomChannelView mRoomChannelView;
    private List<RoomChannelModel> mModels = new ArrayList<>();
    private FrameLayout fl_live_channel_view;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_pull_viewer;
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        fl_live_channel_view = (FrameLayout) view.findViewById(R.id.fl_live_channel);
        showLoadingVideo();
        addRoomChannelView();
        requestRoomInfo();
    }
//
//    //腾讯sdk
//    public void setVideoView(TXCloudVideoView videoView) {
//        this.videoView = videoView;
//        player.setPlayerView(videoView);
//        player.setRenderModeFill();
//        player.setRenderRotationPortrait();
//    }

    private void addRoomChannelView() {
        mRoomChannelView = new RoomChannelView(this);
        mRoomChannelView.setClickListener(channelClickListener);
        replaceView(fl_live_channel_view, mRoomChannelView);
    }

    private RoomChannelView.ClickListener channelClickListener = new RoomChannelView.ClickListener() {

        @Override
        public void onClickChannel(View v) {
            showLoadingVideo();
            mRoomChannelView.selectChannel(v.getId());
            if (videoView != null) {
                videoView.onStop();
            }
            player.pause();
            prePlay(mRoomChannelView.getRTMPUrlById(v.getId()), TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        }
    };

    private void doChangeChannel(String url) {
        showLoadingVideo();
        mRoomChannelView.selectChannel(url);
        if (videoView != null) {
            videoView.onStop();
        }
        player.pause();
        prePlay(url, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        player = new RTMPPlayerManager(getApplication());
        player.setPlayerListener(this);
        super.init(savedInstanceState);
//        videoView = (TXCloudVideoView) findViewById(R.id.view_video);

//        setVideoView(videoView);
    }

    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                sendViewerJoinMsg(groupId, null);
            }
        });

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
    }

    @Override
    protected SDRequestHandler requestRoomInfo() {


        return CommonInterface.requestRoomInfo(getRoomId(), 0, type, 1, sex, cate_id, city, strPrivateKey, null, new AppRequestCallback<App_get_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                setRoomInfo(actModel);
                SDViewBinder.setTextView(scollView.getTv_user_number_left(), String.valueOf(getRoomInfo().getPodcast().getUserId()));
                if (rootModel.isOk()) {
                    onSuccessRequestRoomInfo(actModel);
                } else {
                    onErrorRequestRoomInfo(actModel);
                }
            }
        });
    }

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);

        initIM();
        mModels = actModel.getStream_url_list();
        mRoomChannelView.setRoomChannelList(mModels);
        prePlay(mModels.get(0).getRtmp_play_url(), TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        if (actModel.getType() == 0) {
            SDViewUtil.show(fl_live_channel_view);
        } else {
            SDViewUtil.hide(fl_live_channel_view);
        }
//        if (actModel.getIs_del_vod() == 1) {
//            SDToast.showToast("视频已删除");
//            exit();
//            return;
//        } else {
//            initIM();
//            String playUrl = actModel.getPlayUrl();
//            if (!TextUtils.isEmpty(playUrl)) {
//                prePlay(playUrl, TXLivePlayer.PLAY_TYPE_VOD_MP4);
//            } else {
//                SDToast.showToast("视频已删除.");
//            }
//        }
    }

    @Override
    protected void onErrorRequestRoomInfo(App_get_videoActModel actModel) {
        super.onErrorRequestRoomInfo(actModel);
        exit();
    }


    private void prePlay(String url, int playType) {
        if (isEmpty(url)) {
            SDToast.showToast(getString(R.string.url_empty));
            return;
        }

//        if (!player.validatePlayType(playType)) {
//            SDToast.showToast("playType非法:" + playType);
//            return;
//        }

        player.setUrl(url);
        player.setPlayType(playType);

        clickPlayVideo();
    }


    @Override
    public void onPlayBegin(int event, Bundle param) {
        LogUtil.i("开始播放拉流视频……");
        hideLoadingVideo();
    }

    @Override
    public void onPlayProgress(int event, Bundle param, int total, int progress) {

    }

    @Override
    public void onPlayEnd(int event, Bundle param) {
        LogUtil.i("拉流视频播放结束……");
        SDToast.showToast(getString(R.string.live_is_over));
        exit();
    }

    @Override
    public void onPlayLoading(int event, Bundle param) {
        LogUtil.i("正在拉取视频流……");
    }

    protected void clickPlayVideo() {
        player.performPlay();
    }


    @Override
    protected void initSDVerticalScollView(SDVerticalScollView scollView) {
        scollView.setEnableVerticalScroll(false);
        super.initSDVerticalScollView(scollView);
    }

    @Override
    protected void onClickBottomClose(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    protected void exit() {
        destroyIM();
        Intent intent = new Intent(this, LiveMainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.onResume();
        }
        player.resume();
        MobclickAgent.onPageStart("拉流直播间-观众");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.onPause();
        }
        MobclickAgent.onPageEnd("拉流直播间-观众");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView != null) {
            videoView.onStop();
        }
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
        }
        player.stopPlay();
    }


    private void showExitDialog() {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent(getString(R.string.ensure_exist));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                exit();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
            }
        });
        dialog.show();
    }

    public void onEventMainThread(EImOnNewMessages event) {
        try {
            String peer = event.msg.getConversationPeer();
            if (peer.equals(getGroupId())) {
                if (LiveConstant.CustomMsgType.MSG_CHANGE_CHANNEL == event.msg.getCustomMsgType()) {
                    String url = ((CustomMsgChangeChannel) event.msg.getCustomMsg()).getPlay_url();
                    changeChannel(url);
                }
            }
        } catch (Exception e) {
            SDToast.showToast(e.toString());
        }
    }

    private void changeChannel(String url) {
        if (mModels != null && mModels.size() > 0) {
            for (RoomChannelModel model : mModels) {
                if (model.getRtmp_play_url().equals(url)) {
                    doChangeChannel(url);
                }
            }
        }
    }
}
