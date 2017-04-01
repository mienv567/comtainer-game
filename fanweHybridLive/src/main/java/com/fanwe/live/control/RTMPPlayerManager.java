package com.fanwe.live.control;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.fanwe.library.utils.LogUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by Administrator on 2016/7/25.
 */
public class RTMPPlayerManager
{
    private TXLivePlayer player;
    private String url;
    private int playType;

    /**
     * 总的播放时间(秒)
     */
    private int total;
    /**
     * 当前播放的进度(秒)
     */
    private int progress;

    private boolean isStarted = false;
    private boolean isPausing = false;

    private PlayerListener playerListener;

    public RTMPPlayerManager(Context context)
    {
        player = new TXLivePlayer(context);
    }

    /**
     * 全屏渲染
     */
    public void setRenderModeFill()
    {
        player.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
    }

    /**
     * 按分辨率渲染
     */
    public void setRenderModeAdjustResolution()
    {
        player.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
    }

    /**
     * 竖屏
     */
    public void setRenderRotationPortrait()
    {
        player.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    /**
     * 横屏
     */
    public void setRenderRotationLandscape()
    {
        player.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
    }

    /**
     * 设置静音
     *
     * @param mute true-静音
     */
    public void setMute(boolean mute)
    {
        player.setMute(mute);
    }

    public void setPlayerView(TXCloudVideoView playerView)
    {
        if (playerView != null)
        {
            player.setPlayerView(playerView);
        }
    }

    /**
     * 获得总的播放时间(秒)
     *
     * @return
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * 获得当前播放的进度(秒)
     *
     * @return
     */
    public int getProgress()
    {
        return progress;
    }

    public void setPlayerListener(PlayerListener playerListener)
    {
        this.playerListener = playerListener;
    }

    private ITXLivePlayListener defaultListener = new ITXLivePlayListener()
    {
        @Override
        public void onPlayEvent(int event, Bundle param)
        {
            switch (event)
            {
                case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                    LogUtil.i("PLAY_EVT_PLAY_BEGIN");
                    if (playerListener != null)
                    {
                        playerListener.onPlayBegin(event, param);
                    }
                    break;
                case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                    total = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);

                    LogUtil.i("PLAY_EVT_PLAY_PROGRESS:" + progress + "," + total);
                    if (playerListener != null)
                    {
                        playerListener.onPlayProgress(event, param, total, progress);
                    }
                    break;
                case TXLiveConstants.PLAY_EVT_PLAY_END:
                    LogUtil.i("PLAY_EVT_PLAY_END");
                    resetState();
                    if (playerListener != null)
                    {
                        playerListener.onPlayEnd(event, param);
                    }
                    break;
                case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                    LogUtil.i("PLAY_EVT_PLAY_LOADING");
                    if (playerListener != null)
                    {
                        playerListener.onPlayLoading(event, param);
                    }
                    break;

                case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                    LogUtil.i("PLAY_ERR_NET_DISCONNECT");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNetStatus(Bundle bundle)
        {

        }
    };

    public void setUrl(String url)
    {
        this.url = url;
        stopPlay();
        resetData();
    }

    /**
     * 设置播放类型 flv,mp4等。。。
     *
     * @param playType TXLivePlayer.PLAY_TYPE_XXXXXXX
     */
    public void setPlayType(int playType)
    {
        this.playType = playType;
    }

    public void performPlay()
    {
        if (isStarted)
        {
            if (!isPausing)
            {
                pause();
            } else
            {
                resume();
            }
        } else
        {
            startPlay();
        }
    }

    public void startPlay()
    {
        if (canPlay())
        {
            player.setPlayListener(defaultListener);
            player.startPlay(url, playType);
            isStarted = true;
            LogUtil.i("PlayListener register");
        }
    }

    /**
     * 暂停
     */
    public void pause()
    {
        if (isStarted)
        {
            if (!isPausing)
            {
                isPausing = true;
                player.pause();
            }
        }
    }

    /**
     * 恢复播放
     */
    public void resume()
    {
        if (isStarted)
        {
            if (isPausing)
            {
                isPausing = false;
                player.resume();
            }
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay()
    {
        if (isStarted)
        {
            player.setPlayListener(null);
            player.stopPlay(true);
            resetState();
            LogUtil.i("PlayListener set null");
        }
    }

    private void resetState()
    {
        isStarted = false;
        isPausing = false;
    }

    private void resetData()
    {
        total = 0;
        progress = 0;
    }

    public void seek(int time)
    {
        player.seek(time);
    }

    public boolean isPlaying()
    {
        return isStarted && !isPausing;
    }

    public boolean isStarted()
    {
        return isStarted;
    }

    private boolean canPlay()
    {
        if (isStarted)
        {
            return false;
        }

        if (TextUtils.isEmpty(url))
        {
            return false;
        }
        return true;
    }

    public interface PlayerListener
    {
        void onPlayBegin(int event, Bundle param);

        void onPlayProgress(int event, Bundle param, int total, int progress);

        void onPlayEnd(int event, Bundle param);

        void onPlayLoading(int event, Bundle param);
    }
}
