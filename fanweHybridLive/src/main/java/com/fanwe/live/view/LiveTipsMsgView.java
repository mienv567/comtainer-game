package com.fanwe.live.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.app.App;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.listeners.OnMarqueeListener;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgTipsMsg;
import com.fanwe.live.utils.UiUtils;

public class LiveTipsMsgView extends LinearLayout {

    public void setListener(OnMarqueeListener listener) {
        this.listener = listener;
    }

    private OnMarqueeListener listener;

    private long animation_time;// 整个走马灯效果运行一次的时间

    private HorizontalScrollView horizontalScrollView; // 跑马灯实现原理其实是Textview在HorizontalScrollView中移动

    private TextView tv_marquee; // 内容展示view
    private float screeWidth; // 屏幕宽度

    private float tvContentWidth; // 文字内容长度
    private float totalMoveLenght; // 总的要移动的距离
    private float scrollDistance; //horizontalScrollView需要移动多长的距离,因为textview显示的内容从头到未(最后时刻是满屏显示一行字),所以真是移动的距离是内容长度减去屏幕宽度
    private float timeIn; // horizontalScrollView进入屏幕的时间
    private float timemove; // textview内容滑动时间
    private float mMoveTimes; // 一共要移动多少次
    private int mTime; // 当前移动第几次
    private float scrollSpeed; // 滑动速度

    private boolean isPlaying; // 跑马灯是否正在进行

    ObjectAnimator horizontalScrollViewAnimator;
    ObjectAnimator animator;

    // textview在horizontalScrollView水平滑动的任务
    private Runnable viewScrollTask = new Runnable() {
        @Override
        public void run() {
            {
                scrollSpeed = scrollDistance / timemove;
                mTime = 1;
                horizontalScrollView.post(new DoScrollTask());
            }

        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screeWidth = UiUtils.getScreenWidth(App.getApplication().getApplicationContext());
    }

    private class DoScrollTask implements Runnable {
        private static final int DELAYEDTIME = 50; // 减轻cpu重绘压力,间隔一段时间再post一个任务去滑动horizontalScrollView

        @Override
        public void run() {
            int hasScrollDistance = (int) (scrollSpeed * mTime * DELAYEDTIME);
            horizontalScrollView.scrollTo(hasScrollDistance, 0);
            if (hasScrollDistance < scrollDistance) {
                horizontalScrollView.postDelayed(this, DELAYEDTIME);
            } else {
                horizontalScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        animator = ObjectAnimator.ofFloat(horizontalScrollView, "translationX", scrollDistance > 0 ? -screeWidth : -tvContentWidth);
                        animator.setInterpolator(new LinearInterpolator());
                        animator.setDuration((long) (timeIn * 0.5));
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animator.removeListener(this);
                                SDViewUtil.resetView(LiveTipsMsgView.this);
                                SDViewUtil.invisible(LiveTipsMsgView.this);
                                isPlaying = false;
                                ((ViewGroup) LiveTipsMsgView.this.getParent()).setBackgroundColor(Color.parseColor("#00000000"));
                                if (listener != null) {
                                    listener.onMarquessEnd();
                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.start();

                    }
                });
            }
            mTime++;
        }
    }

    private CustomMsgTipsMsg msg; // 展示内容需要从该消息中获取

    public LiveTipsMsgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTipsMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTipsMsgView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_tips_msg, this, true);

        tv_marquee = (TextView) findViewById(R.id.tv_marquee);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv);
        // 屏蔽水平滑动view的触摸时间
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        screeWidth = UiUtils.getScreenWidth(App.getApplication().getApplicationContext());
        SDViewUtil.invisible(this);

    }


    public boolean canPlay() {
        return !isPlaying;
    }

    private void setMsg(CustomMsgTipsMsg msg) {
        this.msg = msg;
    }

    public void playMsg(CustomMsgTipsMsg newMsg) {
        if (newMsg != null) {
            if (canPlay()) {
                isPlaying = true;

                setMsg(newMsg);

                SDHandlerManager.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        bindData();
                        SDHandlerManager.getMainHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playIn();
                            }
                        }, 500);
                    }
                });
            }
        }
    }

    private void bindData() {
        UserModel sender = msg.getSender();
        if (sender != null) {

            SDViewBinder.setTextView(tv_marquee, Html.fromHtml(msg.getDesc()));

        }
    }

    private void playIn() {
        animation_time = 8 * 1000;
        SDViewUtil.show(LiveTipsMsgView.this);
        ((ViewGroup) LiveTipsMsgView.this.getParent()).setBackgroundColor(Color.parseColor("#66000000"));

        horizontalScrollView.animate().translationX(screeWidth).setDuration(0).start();
        horizontalScrollView.scrollTo(0, 0);
        tv_marquee.post(new Runnable() {
            @Override
            public void run() {

                tvContentWidth = tv_marquee.getWidth();

                totalMoveLenght = tvContentWidth + screeWidth;
                scrollDistance = tvContentWidth - screeWidth;

                // 为了过长内容不会播放太快,应该根据内容来设定显示时间
                int multiple = (int) (scrollDistance / screeWidth);
                int remainder = (int) (scrollDistance % screeWidth);
                if (multiple > 0) {
                    if (remainder > 0) {
                        multiple++;
                    }
                    animation_time = animation_time * multiple;
                }


                timeIn = screeWidth / totalMoveLenght * animation_time;
                timemove = scrollDistance / totalMoveLenght * animation_time;


                horizontalScrollViewAnimator = ObjectAnimator.ofFloat(horizontalScrollView, "translationX", 0);
                horizontalScrollViewAnimator.setDuration((long) timeIn);
                horizontalScrollViewAnimator.setInterpolator(new LinearInterpolator());
                horizontalScrollViewAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        horizontalScrollViewAnimator.removeListener(this);
                        tv_marquee.post(viewScrollTask);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                horizontalScrollViewAnimator.start();

            }
        });


    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimator(animator);
        stopAnimator(horizontalScrollViewAnimator);
        super.onDetachedFromWindow();
    }

    private void stopAnimator(Animator animator) {
        if (animator != null) {
            animator.cancel();
            animator.removeAllListeners();
        }
    }

}
