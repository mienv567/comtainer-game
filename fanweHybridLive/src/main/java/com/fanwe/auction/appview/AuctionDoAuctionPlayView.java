package com.fanwe.auction.appview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.library.animator.SDAnim;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ILiveGiftView;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.model.UserModel;

/**
 * Created by shibx on 2016/9/3.
 */
public class AuctionDoAuctionPlayView extends LinearLayout implements ILiveGiftView<CustomMsgAuctionOffer> {

    private static final long DURATION_IN = 300;
    private static final long DURATION_OUT = 200;
    private static final long DURATION_DELAY = 3000;
    private static final long DURATION_PER_NUMBER = 600;

    private ImageView iv_head_image;
    private TextView tv_nickname;
    private TextView tv_content;
    private ImageView iv_gift;

    private View view_gift_number;
    private TextView tv_gift_number;

    /**
     * 是否正在播放中
     */
    private boolean isPlaying = false;
    /**
     * 是否正在播放数字
     */
    private boolean isPlayingNumber = false;
    /**
     * 是否正在延迟
     */
    private boolean isPlayingDelay = false;

    private Animator animatorIn;
    private Animator animatorNumber;
    private Animator animatorOut;

    private CustomMsgAuctionOffer msg;

    /**
     * 竞拍次数
     */
    private int auctionNumber;

    public AuctionDoAuctionPlayView(Context context) {
        super(context);
        init();
    }

    public AuctionDoAuctionPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AuctionDoAuctionPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AuctionDoAuctionPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_auction_play, this, true);

        iv_head_image = (ImageView) findViewById(R.id.iv_head_image);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_gift = (ImageView) findViewById(R.id.iv_gift);

        view_gift_number = findViewById(R.id.ll_gift_number);
        tv_gift_number = (TextView) findViewById(R.id.tv_gift_number);

        iv_head_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickHeadImage();
            }
        });
        SDViewUtil.invisible(this);
    }


    @Override
    public CustomMsgAuctionOffer getMsg() {

        return msg;
    }

    @Override
    public void setMsg(CustomMsgAuctionOffer msg) {
        msg.setTaked(true);
        if (!msg.equals(this.msg)) {
            bindData(msg);
        }
        this.msg = msg;
        auctionNumber = msg.getPai_sort();
    }

    @Override
    public boolean containsMsg(CustomMsgAuctionOffer msg)
    {
        return msg.equals(this.msg);
    }

    @Override
    public boolean playMsg(CustomMsgAuctionOffer msg) {
        boolean play = false;
        if (isPlaying) {
            if (isPlayingDelay) {
                if (this.msg.equals(msg)) {
                    setMsg(msg);
                    playNumber();
                    play = true;
                }
            }
        } else {
            setMsg(msg);
            playIn();
            play = true;
        }
        return play;
    }

    @Override
    public void bindData(CustomMsgAuctionOffer msg) {
        if (msg != null) {
            UserModel user = msg.getUser();
            if (user != null) {
                SDViewBinder.setTextView(tv_nickname, user.getNickName());
                SDViewBinder.setImageView(getContext(),user.getHeadImage(), iv_head_image);
                SDViewBinder.setTextView(tv_content, "参与1次出价");
                SDViewBinder.setImageViewResource(iv_gift,R.drawable.icon ,false);
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 是否可以播放新的消息，(空闲，或者处于延迟状态)
     *
     */
    @Override
    public boolean canPlay() {
        return (!isPlaying || (isPlaying && isPlayingDelay));
    }

    @Override
    public void playIn() {
        if (isPlaying) {
            return;
        }

        isPlaying = true;
        if (animatorIn == null) {
            SDAnim animIn = SDAnim.from(this).setX(-SDViewUtil.getViewWidth(this), 0)
                    .setDuration(DURATION_IN)
                    .setAccelerate()
                    .addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            SDViewUtil.show(AuctionDoAuctionPlayView.this);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            playNumber();
                        }
                    });
            animatorIn = animIn.get();
        }
        animatorIn.start();
    }

    @Override
    public void playNumber() {

        if (isPlayingNumber) {
            return;
        }

        isPlayingNumber = true;
        removeDelayRunnable();
        if (animatorNumber == null)
        {
            AnimatorSet animatorSetNumber = new AnimatorSet();

            float[] values = new float[]{2.0f, 1.0f, 0.2f, 0.6f, 1.0f, 1.2f, 1.0f};

            SDAnim animScaleX = SDAnim.from(view_gift_number).setScaleX(values).setDuration(DURATION_PER_NUMBER).setDecelerate();
            SDAnim animScaleY = animScaleX.clone().setScaleY(values);

            animatorSetNumber.playTogether(animScaleX.get(), animScaleY.get());
            animatorSetNumber.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    updateNumber();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    auctionNumber++;
                    updateNumber();
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    // 数字播放完毕，开启延迟
                    isPlayingNumber = false;
                    startDelayRunnable();
                }
            });
            animatorNumber = animatorSetNumber;
        }
        animatorNumber.start();
    }

    private void updateNumber() {
        tv_gift_number.setText("X" + auctionNumber);
    }

    private void startDelayRunnable() {
        SDHandlerManager.getMainHandler().postDelayed(delayRunnable, DURATION_DELAY);
        isPlayingDelay = true;
    }

    private void removeDelayRunnable() {
        SDHandlerManager.getMainHandler().removeCallbacks(delayRunnable);
        isPlayingDelay = false;
    }

    private Runnable delayRunnable = new Runnable() {

        @Override
        public void run()
        {
            isPlayingDelay = false;
            playOut();
        }
    };

    @Override
    public void playOut() {
        if (animatorOut == null) {
            SDAnim animY = SDAnim.from(this).setY(0, -SDViewUtil.getViewHeight(this));
            SDAnim animAlpha = SDAnim.from(this).setAlpha(1.0f, 0.0f);

            AnimatorSet animatorSetOut = new AnimatorSet();
            animatorSetOut.playTogether(animY.get(), animAlpha.get());
            animatorSetOut.setDuration(DURATION_OUT);
            animatorSetOut.setInterpolator(new DecelerateInterpolator());
            animatorSetOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    SDViewUtil.invisible(AuctionDoAuctionPlayView.this);
                    SDViewUtil.resetView(AuctionDoAuctionPlayView.this);
                    isPlaying = false;
                }
            });
            animatorOut = animatorSetOut;
        }
        animatorOut.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimator(animatorIn);
        stopAnimator(animatorOut);
        super.onDetachedFromWindow();
    }

    private void stopAnimator(Animator animator) {
        if (animator != null) {
            animator.cancel();
            animator.removeAllListeners();
        }
    }

    protected void clickHeadImage() {
        LiveUserInfoDialog dialog = new LiveUserInfoDialog((Activity) getContext(), msg.getSender().getUserId());
        dialog.show();
    }
}
