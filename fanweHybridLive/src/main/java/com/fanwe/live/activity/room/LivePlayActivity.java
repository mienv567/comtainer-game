package com.fanwe.live.activity.room;

import android.os.Bundle;

import com.fanwe.live.control.RTMPPlayerManager;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by Administrator on 2016/8/7.
 */
public class LivePlayActivity extends LiveLayoutViewerActivity implements RTMPPlayerManager.PlayerListener
{

    private TXCloudVideoView videoView;
    protected RTMPPlayerManager player;

    protected boolean isPauseMode = false;

    public void setVideoView(TXCloudVideoView videoView)
    {
        this.videoView = videoView;
        player.setPlayerView(videoView);
        player.setRenderModeFill();
        player.setRenderRotationPortrait();
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        player = new RTMPPlayerManager(getApplication());
        player.setPlayerListener(this);

        super.init(savedInstanceState);
    }

    @Override
    public void onPlayBegin(int event, Bundle param)
    {

    }

    @Override
    public void onPlayProgress(int event, Bundle param, int total, int progress)
    {

    }

    @Override
    public void onPlayEnd(int event, Bundle param)
    {

    }

    @Override
    public void onPlayLoading(int event, Bundle param)
    {

    }

    public boolean isPlaying()
    {
        if (player != null)
        {
            return player.isPlaying();
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (videoView != null)
        {
            videoView.onResume();
        }
        if (isPauseMode)
        {
            //暂停模式不处理
        } else
        {
            player.resume();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (videoView != null)
        {
            videoView.onPause();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (videoView != null)
        {
            videoView.onStop();
        }
        player.pause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (videoView != null)
        {
            videoView.onDestroy();
        }
        player.stopPlay();
    }
}
