package com.fanwe.library.utils;

import android.media.MediaRecorder;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDHandlerLooper;

/**
 * Created by Administrator on 2016/7/18.
 */
public class SDMediaRecorderVolumer
{
    private SDLooper looper = new SDHandlerLooper(SDHandlerManager.getMainHandler());

    public void listen(final MediaRecorder mr, final MediaRecorderVolumeListener listener)
    {
        if (mr != null)
        {
            looper.start(100, new Runnable()
            {
                @Override
                public void run()
                {
                    if (listener != null)
                    {
                        int amplitude = mr.getMaxAmplitude();
                        int db = parseAmplitudeToDb(amplitude);
                        listener.onVolume(db, amplitude);
                    }
                }
            });
        }
    }

    private int parseAmplitudeToDb(int amplitude)
    {
        double db = 0;
        if (amplitude > 0)
        {
            db = 20 * Math.log10(amplitude);
        }
        return (int) db;
    }

    public void stop()
    {
        looper.stop();
    }

    public interface MediaRecorderVolumeListener
    {
        void onVolume(int db, int amplitude);
    }

}
