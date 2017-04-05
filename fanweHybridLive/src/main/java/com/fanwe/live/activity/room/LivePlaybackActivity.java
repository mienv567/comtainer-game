package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomPlayControlView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_get_review_ActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.view.SDVerticalScollView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.TIMCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LivePlaybackActivity extends LiveLayoutViewerActivity {
    /**
     * 主播identifier(String)
     */
    public static final String EXTRA_PLAYBACK_URL = "extra_playback_url";
    public static final String EXTRA_REVIEW_ID = "reviewId";
//    private TXCloudVideoView view_video;

    private RoomPlayControlView roomPlayControlView;
    private FrameLayout fl_live_play_control;
    private String mReviewId;
    private int seekValue;
    private int totalTime;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_playback_new;
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        roomInfoView.setPlayback(true);

        fl_live_play_control = (FrameLayout) view.findViewById(R.id.fl_live_play_control);
        addLivePlayControl();
        requestRoomInfo();
    }

    private void addLivePlayControl() {
        roomPlayControlView = new RoomPlayControlView(this);
        roomPlayControlView.setClickListener(controlClickListener);
        roomPlayControlView.setOnSeekBarChangeListener(controlSeekBarListener);
        replaceView(fl_live_play_control, roomPlayControlView);

        updatePlayButton();
        updateDuration(0, 0);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        view_video = (TXCloudVideoView) findViewById(R.id.view_video);
//        setVideoView(view_video);
//        mVideoView = (PLVideoView) findViewById(R.id.VideoView);
        mReviewId = getIntent().getStringExtra(EXTRA_REVIEW_ID);
        Log.i("invite", "到回播了");

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
                roomViewerBottomView.hideSendGiftView();
            }
        });
    }

    @Override
    protected void destroyIM() {
        super.destroyIM();
        quitGroup(getGroupId(), null);
    }

    @Override
    protected SDRequestHandler requestRoomInfo() {
        mReviewId = getIntent().getStringExtra(EXTRA_REVIEW_ID);
        return CommonInterface.requestReview(mReviewId, new AppRequestCallback<App_get_review_ActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                // 如果是回看视频那么就不要显示活动的相关信息
                App_get_videoActModel nullModel = new App_get_videoActModel();
                nullModel.setCate_id(0);
                nullModel.setResource(null);
                nullModel.setGroupId(actModel.getGroupId());
//                HeartLayout.mapNameUrl.clear();
                //                actModel.getResource().setLogoUrl("");
                //                actModel.getResource().setFloat_pic_url(null);
                setRoomInfo(nullModel);
                SDViewBinder.setTextView(scollView.getTv_user_number_left(), String.valueOf(actModel.getPodcast().getUserId()));
                if (rootModel.isOk()) {
                    onSuccessRequestRoomInfo(nullModel);
                } else {
                    onErrorRequestRoomInfo(nullModel);
                }
                mVideoView.setVideoPath(actModel.getReviewUrl());
                mVideoView.start();
                mHandler.postDelayed(updateSeekBarRun, 1000);
                updatePlayButton();
            }
        });
    }

    Runnable updateSeekBarRun = new Runnable() {
        @Override
        public void run() {
            if (totalTime == 0) {
                totalTime = (int) (mVideoView.getDuration() / 1000);
                roomPlayControlView.setMax(totalTime);
            }
            int currentPosition = (int) (mVideoView.getCurrentPosition() / 1000);
            roomPlayControlView.setProgress(currentPosition);
            updateDuration(totalTime, currentPosition);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);

//        if (actModel.getHas_video_control() == 1) {
        SDViewUtil.show(roomPlayControlView);
//        } else {
//            SDViewUtil.hide(roomPlayControlView);
//        }
//
//        if (actModel.getIs_del_vod() == 1) {
//            SDToast.showToast(getString(R.string.video_is_removed));
//            exit();
//            return;
//        } else {
        initIM();
//            String playUrl = actModel.getPlay_url();
//            if (!TextUtils.isEmpty(playUrl)) {
//                prePlay(playUrl, TXLivePlayer.PLAY_TYPE_VOD_MP4);
//            } else {
//                SDToast.showToast(getString(R.string.video_is_removed));
//            }
//        }
    }

    @Override
    protected void onErrorRequestRoomInfo(App_get_videoActModel actModel) {
        super.onErrorRequestRoomInfo(actModel);
        exit();
    }

    private SeekBar.OnSeekBarChangeListener controlSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        private boolean needResume = false;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mVideoView.getDuration() > 0) {
                seekValue = seekBar.getProgress();
                if (needResume) {
                    needResume = false;
                    mVideoView.start();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (mVideoView.getDuration() > 0) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    needResume = true;
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mVideoView.getDuration() > 0 && fromUser) {
                updateDuration(seekBar.getMax(), progress);
                mVideoView.seekTo(progress * 1000);
            }
        }
    };

    private RoomPlayControlView.ClickListener controlClickListener = new RoomPlayControlView.ClickListener() {
        @Override
        public void onClickPlayVideo(View v) {
            clickPlayVideo();
        }
    };

    protected void clickPlayVideo() {
        if(mVideoView.isPlaying()) {
            mVideoView.pause();
        }else {
            mVideoView.start();
        }
        updatePlayButton();
    }

    private void updatePlayButton() {
        if (mVideoView.isPlaying()) {
            roomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_pause_video);
        } else {
            roomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_play_video);
        }
    }

    private void updateDuration(int total, int progress) {
        StringBuilder sb = new StringBuilder();
        sb.append(SDDateUtil.getTotalMinutesFormat(progress * 1000)).append(":").append(SDDateUtil.getDuringSecondsFormat(progress * 1000))
                .append("/").append(SDDateUtil.getTotalMinutesFormat(total * 1000)).append(":").append(SDDateUtil.getDuringSecondsFormat(total * 1000));

        roomPlayControlView.setTextDuration(sb.toString());
    }

    @Override
    protected void initSDVerticalScollView(SDVerticalScollView scollView) {
        scollView.setEnableVerticalScroll(false);
        super.initSDVerticalScollView(scollView);
        scollView.setEnableHorizontalScroll(false);
    }

    @Override
    protected void onClickBottomClose(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    protected void exit() {
        destroyIM();
        finish();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("回播直播间-观众");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("回播直播间-观众");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void exitRoom(boolean isNeedFinish) {
        exit();
    }
}
