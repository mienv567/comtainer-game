package com.fanwe.library.media.player;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface SDMediaPlayerListener
{
    void onReleased();

    void onReset();

    void onInitialized();

    void onPreparing();

    void onPrepared();

    void onPlaying();

    void onPaused();

    void onCompletion();

    void onStopped();

    void onError(MediaPlayer mp, int what, int extra);

    void onException(Exception e);
}
