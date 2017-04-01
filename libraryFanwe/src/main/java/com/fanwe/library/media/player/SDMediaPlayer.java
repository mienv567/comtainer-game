package com.fanwe.library.media.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;

public class SDMediaPlayer
{
    private static SDMediaPlayer instance;

    private MediaPlayer player;
    private State state = State.Idle;
    private String audioFilePath;
    private String audioUrl;
    private SDMediaPlayerListener listener;

    private SDMediaPlayer()
    {
        init();
    }

    public static SDMediaPlayer getInstance()
    {
        if (instance == null)
        {
            instance = new SDMediaPlayer();
        }
        return instance;
    }

    /**
     * 初始化录音器，调用release()后如果想要继续使用录音器，要调用此方法初始化
     */
    public void init()
    {
        if (player != null)
        {
            release();
        }
        player = new MediaPlayer();
        player.setOnErrorListener(mListenerOnError);
        player.setOnPreparedListener(mListenerOnPrepared);
        player.setOnCompletionListener(mListenerOnCompletion);
    }

    public State getState()
    {
        return state;
    }

    public void setListener(SDMediaPlayerListener listener)
    {
        this.listener = listener;
    }

    public String getAudioFilePath()
    {
        return audioFilePath;
    }

    public String getAudioUrl()
    {
        return audioUrl;
    }

    public void playAudioRaw(int resId, Context context)
    {
        reset();
        try
        {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            setState(State.Initialized);
            notifyInitialized();
            start();
        } catch (Exception e)
        {
            notifyException(e);
        }
    }

    public void playAudioUrl(String url)
    {
        reset();
        try
        {
            player.setDataSource(url);
            this.audioUrl = url;
            setState(State.Initialized);
            notifyInitialized();
            start();
        } catch (Exception e)
        {
            notifyException(e);
        }
    }

    public void playAudioFile(String path)
    {
        reset();
        try
        {
            player.setDataSource(path);
            this.audioFilePath = path;
            setState(State.Initialized);
            notifyInitialized();
            start();
        } catch (Exception e)
        {
            notifyException(e);
        }
    }

    public void performPlayAudioFile(String path)
    {
        if (!TextUtils.isEmpty(path))
        {
            if (isPlayingAudioFile(path))
            {
                stop();
            } else
            {
                playAudioFile(path);
            }
        }
    }

    public void performPlayAudioUrl(String url)
    {
        if (!TextUtils.isEmpty(url))
        {
            if (isPlayingAudioUrl(url))
            {
                stop();
            } else
            {
                playAudioUrl(url);
            }
        }
    }

    public boolean isPlayingAudioFile(String path)
    {
        boolean result = false;
        if (isPlaying() && !TextUtils.isEmpty(path))
        {
            result = path.equals(audioFilePath);
        }
        return result;
    }

    public boolean isPlayingAudioUrl(String url)
    {
        boolean result = false;
        if (isPlaying() && url != null)
        {
            result = url.equals(audioUrl);
        }
        return result;
    }

    public boolean isPlaying()
    {
        return state == State.Playing;
    }

    public boolean isPause()
    {
        return state == State.Paused;
    }

    /**
     * 开始播放
     */
    public void start()
    {
        switch (state)
        {
            case Idle:

                break;
            case Initialized:
                prepareAsyncPlayer();
                break;
            case Preparing:

                break;
            case Prepared:
                startPlayer();
                break;
            case Playing:

                break;
            case Paused:
                startPlayer();
                break;
            case Completed:
                startPlayer();
                break;
            case Stopped:
                prepareAsyncPlayer();
                break;

            default:
                break;
        }
    }

    /**
     * 暂停播放
     */
    public void pause()
    {
        switch (state)
        {
            case Idle:

                break;
            case Initialized:

                break;
            case Preparing:

                break;
            case Prepared:

                break;
            case Playing:
                pausePlayer();
                break;
            case Paused:

                break;
            case Completed:

                break;
            case Stopped:

                break;

            default:
                break;
        }
    }

    /**
     * 停止播放
     */
    public void stop()
    {
        switch (state)
        {
            case Idle:

                break;
            case Initialized:

                break;
            case Preparing:

                break;
            case Prepared:
                stopPlayer();
                break;
            case Playing:
                stopPlayer();
                break;
            case Paused:
                stopPlayer();
                break;
            case Completed:
                stopPlayer();
                break;
            case Stopped:

                break;

            default:
                break;
        }
    }

    /**
     * 重置播放器，一般用于关闭播放界面的时候调用
     */
    public void reset()
    {
        stop();
        resetPlayer();
    }

    /**
     * 释放播放器，用于不再需要播放器的时候调用，调用此方法后，需要手动调用init()方法初始化后才可以使用
     */
    public void release()
    {
        stop();
        releasePlayer();
    }


    private void setState(State state)
    {
        this.state = state;
    }

    private void prepareAsyncPlayer()
    {
        setState(State.Preparing);
        notifyPreparing();
        player.prepareAsync();
    }

    private void startPlayer()
    {
        setState(State.Playing);
        notifyPlaying();
        player.start();
    }

    private void pausePlayer()
    {
        setState(State.Paused);
        notifyPaused();
        player.pause();
    }

    private void stopPlayer()
    {
        setState(State.Stopped);
        notifyStopped();
        player.stop();
    }

    private void resetPlayer()
    {
        setState(State.Idle);
        notifyReset();
        player.reset();
    }

    private void releasePlayer()
    {
        setState(State.Released);
        notifyReleased();
        player.release();
    }

    // notify
    protected void notifyPreparing()
    {
        if (listener != null)
        {
            listener.onPreparing();
        }
    }

    protected void notifyPrepared()
    {
        if (listener != null)
        {
            listener.onPrepared();
        }
    }

    protected void notifyPlaying()
    {
        if (listener != null)
        {
            listener.onPlaying();
        }
    }

    protected void notifyPaused()
    {
        if (listener != null)
        {
            listener.onPaused();
        }
    }

    protected void notifyCompletion()
    {
        if (listener != null)
        {
            listener.onCompletion();
        }
    }

    protected void notifyStopped()
    {
        if (listener != null)
        {
            listener.onStopped();
        }
    }

    protected void notifyReset()
    {
        if (listener != null)
        {
            listener.onReset();
        }
    }

    protected void notifyInitialized()
    {
        if (listener != null)
        {
            listener.onInitialized();
        }
    }

    protected void notifyReleased()
    {
        if (listener != null)
        {
            listener.onReleased();
        }
    }

    protected void notifyError(MediaPlayer mp, int what, int extra)
    {
        if (listener != null)
        {
            listener.onError(mp, what, extra);
        }
    }

    protected void notifyException(Exception e)
    {
        if (listener != null)
        {
            listener.onException(e);
        }
    }

    // listener
    private OnErrorListener mListenerOnError = new OnErrorListener()
    {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra)
        {
            resetPlayer();
            notifyError(mp, what, extra);
            return true;
        }
    };

    private OnPreparedListener mListenerOnPrepared = new OnPreparedListener()
    {

        @Override
        public void onPrepared(MediaPlayer mp)
        {
            setState(State.Prepared);
            notifyPrepared();
            start();
        }
    };

    private OnCompletionListener mListenerOnCompletion = new OnCompletionListener()
    {

        @Override
        public void onCompletion(MediaPlayer mp)
        {
            setState(State.Completed);
            notifyCompletion();
        }
    };

    public enum State
    {
        /**
         * 已经释放资源
         */
        Released,
        /**
         * 空闲，还没设置dataSource
         */
        Idle,
        /**
         * 已经设置dataSource，还未播放
         */
        Initialized,
        /**
         * 准备中
         */
        Preparing,
        /**
         * 准备完毕
         */
        Prepared,
        /**
         * 已经启动播放
         */
        Playing,
        /**
         * 已经暂停播放
         */
        Paused,
        /**
         * 已经播放完毕
         */
        Completed,
        /**
         * 调用stop方法后的状态
         */
        Stopped;
    }

}
