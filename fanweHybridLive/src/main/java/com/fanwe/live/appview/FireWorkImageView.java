package com.fanwe.live.appview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.R;

/**
 * 用来做爆炸效果的imageView，为了方便使用把逻辑放在了view内部
 * Created by kevin.liu on 2017/3/22.
 */
public class FireWorkImageView extends ImageView {
    private AnimationDrawable fireWorkAnim;
    private boolean isDoingAnima;
    private Runnable explodeRun;

    public FireWorkImageView(Context context) {
        super(context);
    }

    public FireWorkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FireWorkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 执行爆炸动画，播放完后自动gone掉
     *
     * @param duration 动画播放的持续时间
     */
    public void explode(int duration) {
        if (isDoingAnima) {
            removeCallbacks(explodeRun);
            postDelayed(explodeRun, duration);
            return;
        }
        setVisibility(VISIBLE);
        if (fireWorkAnim == null) {
            setBackgroundResource(R.drawable.anim_fire_work);
            fireWorkAnim = (AnimationDrawable) getBackground();
        }
        isDoingAnima = true;
        fireWorkAnim.start();
        explodeRun = new Runnable() {
            @Override
            public void run() {
                try {

                    fireWorkAnim.stop();
                    setVisibility(GONE);
                    isDoingAnima = false;
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                }
            }
        };
        postDelayed(explodeRun, duration);
    }

    public void stopAndGone() {
        if (fireWorkAnim != null && explodeRun != null && isDoingAnima) {
            explodeRun.run();
            removeCallbacks(explodeRun);
        }
    }
}
