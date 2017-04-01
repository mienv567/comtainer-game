package com.fanwe.live.video;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fanwe.live.utils.LogUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/14 0014.
 */
public class VideoViewManager implements PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnErrorListener {

    private PLVideoView mPLVideoView;

    private Toast mToast = null;

    public View getVideoView() {
        return mPLVideoView;
    }

    public VideoViewManager(PLVideoView view) {
        this(view, defaultAVOptions());
    }

    public VideoViewManager(PLVideoView view, AVOptions options) {
        mPLVideoView = view;

        mPLVideoView.setAVOptions(options);
        mPLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);

        mPLVideoView.setOnInfoListener(this);
        mPLVideoView.setOnCompletionListener(this);
        mPLVideoView.setOnErrorListener(this);
    }

    public void setDisplayAspectRatio(int ratio) {
        mPLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
    }

    public void setVideoPath(String videoPath) {
        mPLVideoView.setVideoPath(videoPath);
    }

    public void changeLayoutSize(int width, int height) {
        LogUtils.logI(width + ":" + height);
        ViewGroup.LayoutParams params = mPLVideoView.getLayoutParams();
        params.width = width;
        params.height = height;
        mPLVideoView.setLayoutParams(params);
        mPLVideoView.invalidate();
    }

    public static AVOptions defaultAVOptions() {
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        return options;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int whati, int extra) {
        return false;
    }

    /**
     * 播放结束，发送结束消息给Activity
     */
    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        EventBus.getDefault().post(plMediaPlayer);
    }

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
        if (isNeedReconnect) {
            showToastTips("正在重连...");
            sendReconnectMessage();
        } else {
            notifyActivityFinish(plMediaPlayer);
        }
        // Return true means the error has been handled
        // If return false, then `onCompletion` will be called
        return true;
    }

    public void start() {
        mPLVideoView.start();
    }

    public void pause() {
        mPLVideoView.pause();
    }

    public void stopPlayback() {
        mPLVideoView.stopPlayback();
    }

    private void showToastTips(final String tips) {
        if (mPLVideoView.isActivated()) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mPLVideoView.getContext(), tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING || !mPLVideoView.isActivated()) {
                return;
            }
            mPLVideoView.start();
        }
    };

    private void sendReconnectMessage() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MESSAGE_ID_RECONNECTING, 500);
    }

    /**
     * 遇到无法重试连接的错误，发送结束消息给Activity
     */
    private void notifyActivityFinish(PLMediaPlayer plMediaPlayer) {
        EventBus.getDefault().post(plMediaPlayer);
    }
}
