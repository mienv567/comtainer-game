package com.fanwe.live.dialog;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fanwe.live.R;

public class LiveSetCameraBeautyDialogQN extends LiveBaseDialog {
    public float getBeautyLevel() {
        return beautyLevel;
    }

    public void setBeautyLevel(float beautyLevel) {
        this.beautyLevel = beautyLevel;
        setBeautyBuffingProgress((int) (beautyLevel*100));
    }

    public float getRedden() {
        return redden;
    }

    public void setRedden(float redden) {
        this.redden = redden;
        setBeautyRuddyProgress((int) (redden*100));
    }

    public float getWhiten() {
        return whiten;
    }

    public void setWhiten(float whiten) {
        this.whiten = whiten;
        setBeautyWhiteningProgress((int) (whiten*100));
    }

    public LiveSetCameraBeautyDialogQN(Activity activity, float beautyLevel, float whiten, float redden) {
        super(activity);
        this.beautyLevel = beautyLevel;
        this.redden = redden;
        this.whiten = whiten;
        init();
    }

    private float beautyLevel;
    private float redden;
    private float whiten;

    // 磨皮
    private SeekBar sb_beauty_buffing;
    private TextView tv_percent_buffing;
    private OnSeekBarChangeListener onBeautyBuffingSeekBarChangeListener;
    // 美白
    private SeekBar sb_beauty_whitening;
    private TextView tv_percent_whitening;
    private OnSeekBarChangeListener onBeautyWhiteningSeekBarChangeListener;
    // 红润
    private SeekBar sb_beauty_ruddy;
    private TextView tv_percent_ruddy;
    private OnSeekBarChangeListener onBeautyRuddySeekBarChangeListener;

    public LiveSetCameraBeautyDialogQN(Activity activity) {
        super(activity);
        init();
    }


    public void setOnBeautyBuffingSeekBarChangeListener(OnSeekBarChangeListener onBeautySeekBarChangeListener) {
        this.onBeautyBuffingSeekBarChangeListener = onBeautySeekBarChangeListener;
    }

    public void setBeautyBuffingProgress(int progress) {
        if (sb_beauty_buffing != null) {
            sb_beauty_buffing.setProgress(progress);
            updateBuffingPercent(progress);
        }
    }

    public void setOnBeautyWhiteningSeekBarChangeListener(OnSeekBarChangeListener onBeautyWhiteningSeekBarChangeListener) {
        this.onBeautyWhiteningSeekBarChangeListener = onBeautyWhiteningSeekBarChangeListener;
    }

    public void setBeautyWhiteningProgress(int progress) {
        if (sb_beauty_whitening != null) {
            sb_beauty_whitening.setProgress(progress);
            updateWhiteningPercent(progress);
        }
    }

    public void setOnBeautyRubbySeekBarChangeListener(OnSeekBarChangeListener onBeautyBuffingSeekBarChangeListener) {
        this.onBeautyRuddySeekBarChangeListener = onBeautyBuffingSeekBarChangeListener;
    }

    public void setBeautyRuddyProgress(int progress) {
        if (sb_beauty_ruddy != null) {
            sb_beauty_ruddy.setProgress(progress);
            updateRuddyPercent(progress);
        }
    }

    private void init() {
        setContentView(R.layout.dialog_set_camera_beauty_qn);
        setCanceledOnTouchOutside(true);

        sb_beauty_buffing = (SeekBar) getContentView().findViewById(R.id.sb_beauty_buffing);
        tv_percent_buffing = (TextView) getContentView().findViewById(R.id.tv_percent_buffing);
        sb_beauty_buffing.setMax(100);
setBeautyLevel(this.beautyLevel);
        sb_beauty_buffing.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onBeautyBuffingSeekBarChangeListener != null) {
                    onBeautyBuffingSeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (onBeautyBuffingSeekBarChangeListener != null) {
                    onBeautyBuffingSeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onBeautyBuffingSeekBarChangeListener != null) {
                    onBeautyBuffingSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
                updateBuffingPercent(progress);
            }
        });


        sb_beauty_whitening = (SeekBar) getContentView().findViewById(R.id.sb_beauty_whitening);
        tv_percent_whitening = (TextView) getContentView().findViewById(R.id.tv_percent_whitening);
        sb_beauty_whitening.setMax(100);
        setBeautyWhiteningProgress((int) (this.whiten*100));
        sb_beauty_whitening.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onBeautyWhiteningSeekBarChangeListener != null) {
                    onBeautyWhiteningSeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (onBeautyWhiteningSeekBarChangeListener != null) {
                    onBeautyWhiteningSeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onBeautyWhiteningSeekBarChangeListener != null) {
                    onBeautyWhiteningSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
                updateWhiteningPercent(progress);
            }
        });


        sb_beauty_ruddy = (SeekBar) getContentView().findViewById(R.id.sb_beauty_ruddy);
        tv_percent_ruddy = (TextView) getContentView().findViewById(R.id.tv_percent_ruddy);

        sb_beauty_ruddy.setMax(100);
        setBeautyRuddyProgress((int) (this.redden*100));
        sb_beauty_ruddy.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onBeautyRuddySeekBarChangeListener != null) {
                    onBeautyRuddySeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (onBeautyRuddySeekBarChangeListener != null) {
                    onBeautyRuddySeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onBeautyRuddySeekBarChangeListener != null) {
                    onBeautyRuddySeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
                updateRuddyPercent(progress);
            }
        });
    }

    /**
     * 更新磨皮显示百分比文字
     *
     * @param percent
     */
    private void updateBuffingPercent(int percent) {
        tv_percent_buffing.setText(String.valueOf(percent + "%"));
    }

    /**
     * 更新美白显示百分比文字
     *
     * @param percent
     */
    private void updateWhiteningPercent(int percent) {
        tv_percent_whitening.setText(String.valueOf(percent + "%"));
    }

    /**
     * 更新红润显示百分比文字
     *
     * @param percent
     */
    private void updateRuddyPercent(int percent) {
        tv_percent_ruddy.setText(String.valueOf(percent + "%"));
    }

}
