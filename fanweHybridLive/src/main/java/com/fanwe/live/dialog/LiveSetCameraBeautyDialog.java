package com.fanwe.live.dialog;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fanwe.live.R;

public class LiveSetCameraBeautyDialog extends LiveBaseDialog
{

    private SeekBar sb_beauty;
    private TextView tv_percent;
    private OnSeekBarChangeListener onBeautySeekBarChangeListener;

    public LiveSetCameraBeautyDialog(Activity activity)
    {
        super(activity);
        init();
    }

    /**
     * 设置美颜百分比监听
     *
     * @param onBeautySeekBarChangeListener
     */
    public void setOnBeautySeekBarChangeListener(OnSeekBarChangeListener onBeautySeekBarChangeListener)
    {
        this.onBeautySeekBarChangeListener = onBeautySeekBarChangeListener;
    }

    /**
     * 设置美颜百分比0-100
     *
     * @param progress
     */
    public void setBeautyProgress(int progress)
    {
        if (sb_beauty != null)
        {
            sb_beauty.setProgress(progress);
            updatePercent(progress);
        }
    }

    private void init()
    {
        setContentView(R.layout.dialog_set_camera_beauty);
        setCanceledOnTouchOutside(true);

        sb_beauty = (SeekBar) getContentView().findViewById(R.id.sb_beauty);
        tv_percent = (TextView) getContentView().findViewById(R.id.tv_percent);

        sb_beauty.setMax(100);
        sb_beauty.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (onBeautySeekBarChangeListener != null)
                {
                    onBeautySeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                if (onBeautySeekBarChangeListener != null)
                {
                    onBeautySeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (onBeautySeekBarChangeListener != null)
                {
                    onBeautySeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
                updatePercent(progress);
            }
        });
    }

    private void updatePercent(int percent)
    {
        tv_percent.setText(String.valueOf(percent + "%"));
    }

}
