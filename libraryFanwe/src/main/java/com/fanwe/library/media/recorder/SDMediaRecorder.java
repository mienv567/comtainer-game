package com.fanwe.library.media.recorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.text.TextUtils;

import com.fanwe.library.utils.SDCountDownTimer;
import com.fanwe.library.utils.SDFileUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/7/15.
 */
public class SDMediaRecorder
{

    private static final long INTERVAL_TIME = 1000;
    private static final long MIN_RECORD_TIME = 1000;
    private static final String DIR_NAME = "record";

    private static SDMediaRecorder instance;
    private Context context;
    private MediaRecorder recorder;
    private State state = State.Idle;
    private File dirFile;
    private boolean isInit;

    private File recordFile;
    private long startTime;

    private SDCountDownTimer timer = new SDCountDownTimer();
    private long maxRecordTime;

    private SDMediaRecorderListener listener;
    private SDCountDownTimer.SDCountDownTimerListener timerListener;

    private SDMediaRecorder()
    {
    }

    public static SDMediaRecorder getInstance()
    {
        if (instance == null)
        {
            instance = new SDMediaRecorder();
        }
        return instance;
    }

    public MediaRecorder getRecorder()
    {
        return recorder;
    }

    /**
     * 初始化录音器
     *
     * @param context
     */
    public void init(Context context)
    {
        if (!isInit)
        {
            if (recorder != null)
            {
                release();
            }

            this.context = context;
            dirFile = SDFileUtil.getCacheDir(context, DIR_NAME);

            recorder = new MediaRecorder();
            recorder.setOnErrorListener(onErrorListener);
            recorder.setOnInfoListener(onInfoListener);
            state = State.Idle;
            isInit = true;
        }
    }

    public void registerTimerListener(SDCountDownTimer.SDCountDownTimerListener timerListener)
    {
        this.timerListener = timerListener;
    }

    public void unregisterTimerListener(SDCountDownTimer.SDCountDownTimerListener timerListener)
    {
        if (timerListener != null)
        {
            if (this.timerListener == timerListener)
            {
                this.timerListener = null;
            }
        }
    }

    public void setMaxRecordTime(long maxRecordTime)
    {
        this.maxRecordTime = maxRecordTime;
    }

    public void deleteAllFile()
    {
        SDFileUtil.deleteFileOrDir(dirFile);
    }

    public Context getContext()
    {
        return context;
    }

    public State getState()
    {
        return state;
    }

    public File getRecordFile()
    {
        return recordFile;
    }

    public File getDirFile()
    {
        return dirFile;
    }

    public File getFile(String fileName)
    {
        File file = new File(dirFile, fileName);
        return file;
    }

    private MediaRecorder.OnInfoListener onInfoListener = new MediaRecorder.OnInfoListener()
    {

        @Override
        public void onInfo(MediaRecorder mr, int what, int extra)
        {
            notifyInfo(mr, what, extra);
        }
    };

    private MediaRecorder.OnErrorListener onErrorListener = new MediaRecorder.OnErrorListener()
    {
        @Override
        public void onError(MediaRecorder mr, int what, int extra)
        {
            resetData();
            stopRecorder();
            notifyError(mr, what, extra);
        }
    };

    public void registerListener(SDMediaRecorderListener listener)
    {
        this.listener = listener;
    }

    public void unregisterListener(SDMediaRecorderListener listener)
    {
        if (listener != null)
        {
            if (this.listener == listener)
            {
                this.listener = null;
            }
        }
    }

    private void setState(State state)
    {
        this.state = state;
    }

    /**
     * 开始录音
     *
     * @param path 录音文件保存路径，如果为空的话，会用录音器内部的路径规则生成录音文件
     */
    public void start(String path)
    {
        switch (state)
        {
            case Idle:
                startRecorder(path);
                break;
            case Recording:

                break;
            case Stopped:
                startRecorder(path);
                break;
            case Released:

                break;
            default:
                break;
        }
    }

    /**
     * 停止录音
     */
    public void stop()
    {
        switch (state)
        {
            case Idle:

                break;
            case Recording:
                stopRecorder();
                break;
            case Stopped:

                break;
            case Released:

                break;
            default:
                break;
        }
    }

    /**
     * 释放资源，一般在录音界面关闭的时候调用，调用后如果想系继续使用的话需要手动调用init(context)方法初始化
     */
    public void release()
    {
        switch (state)
        {
            case Idle:

                break;
            case Recording:
                stop();
                release();
                break;
            case Stopped:
                releaseRecorder();
                break;
            case Released:

                break;
            default:
                break;
        }
    }

    private void startRecorder(String path)
    {
        try
        {
            setState(State.Recording);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            if (TextUtils.isEmpty(path))
            {
                recordFile = SDFileUtil.createDefaultFileUnderDir(dirFile);
                path = recordFile.getAbsolutePath();
            } else
            {
                recordFile = new File(path);
            }

            recorder.setOutputFile(path);
            recorder.prepare();
            recorder.start();
            startTime = System.currentTimeMillis();

            startTimer();
            notifyRecording();
        } catch (Exception e)
        {
            notifyException(e);
        }
    }

    private void startTimer()
    {
        if (timerListener != null && maxRecordTime >= MIN_RECORD_TIME)
        {
            timer.start(maxRecordTime, INTERVAL_TIME, timerListener);
        }
    }

    private void stopTimer()
    {
        timer.stop();
    }

    private void stopRecorder()
    {
        try
        {
            setState(State.Stopped);
            recorder.stop();
            recorder.reset();

            stopTimer();
            notifyStopped();

            resetData();
        } catch (Exception e)
        {
            notifyException(e);
        }
    }

    private void resetData()
    {
        recordFile = null;
        startTime = 0;
    }

    private void releaseRecorder()
    {
        setState(State.Released);
        recorder.release();

        stopTimer();
        notifyReleased();
        isInit = false;
    }

    private void notifyRecording()
    {
        if (listener != null)
        {
            listener.onRecording();
        }
    }

    protected void notifyStopped()
    {
        if (listener != null)
        {
            long duration = 0;
            if (startTime > 0)
            {
                duration = System.currentTimeMillis() - startTime;
            }
            listener.onStopped(recordFile, duration);
        }
    }

    private void notifyReleased()
    {
        if (listener != null)
        {
            listener.onReleased();
        }
    }

    protected void notifyError(MediaRecorder mr, int what, int extra)
    {
        if (listener != null)
        {
            listener.onError(mr, what, extra);
        }
    }

    protected void notifyException(Exception e)
    {
        recorder.reset();
        setState(State.Idle);
        resetData();
        stopTimer();
        if (listener != null)
        {
            listener.onException(e);
        }
    }

    protected void notifyInfo(MediaRecorder mr, int what, int extra)
    {
        if (listener != null)
        {
            listener.onInfo(mr, what, extra);
        }
    }


    public enum State
    {
        /**
         * 已经释放资源
         */
        Released,
        /**
         * 空闲
         */
        Idle,
        /**
         * 录音中
         */
        Recording,
        /**
         * 重置状态
         */
        Stopped;
    }

}
