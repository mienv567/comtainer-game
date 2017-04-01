package com.fanwe.live.appview.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.model.custommsg.CustomMsgGift;

/**
 * Created by Administrator on 2016/8/8.
 */
public abstract class GiftAnimatorView extends BaseAppView
{
    private CustomMsgGift msg;
    private boolean isPlaying;
    private boolean isCreate;
    private boolean needPlay;
    private AnimatorSet animatorSet;
    private Animator.AnimatorListener animatorListener;

    protected TextView tv_gift_desc;

    public GiftAnimatorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GiftAnimatorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GiftAnimatorView(Context context)
    {
        super(context);
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        animatorSet = new AnimatorSet();
    }

    public void setAnimatorSet(AnimatorSet animatorSet)
    {
        this.animatorSet = animatorSet;
    }

    public AnimatorSet getAnimatorSet()
    {
        return animatorSet;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener)
    {
        this.animatorListener = animatorListener;
    }

    public void setMsg(CustomMsgGift msg)
    {
        this.msg = msg;
        if (tv_gift_desc != null)
        {
            SDViewBinder.setTextView(tv_gift_desc, msg.getTop_title());
        }
    }

    public CustomMsgGift getMsg()
    {
        return msg;
    }

    protected void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public final boolean isPlaying()
    {
        return isPlaying;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        tryCreateAnimator();
    }

    private void tryCreateAnimator()
    {
        if (!isCreate)
        {
            isCreate = true;
            createAnimator();
            if (needPlay)
            {
                needPlay = false;
                getAnimatorSet().start();
            }
        }
    }

    protected abstract void createAnimator();

    protected abstract void resetView();

    public void play()
    {
        if (!isPlaying)
        {
            isPlaying = true;
            if (isCreate)
            {
                getAnimatorSet().start();
            } else
            {
                needPlay = true;
            }
        }
    }

    protected void notifyAnimationStart(Animator animation)
    {
        setPlaying(true);
        if (animatorListener != null)
        {
            animatorListener.onAnimationStart(animation);
        }
    }

    protected void notifyAnimationEnd(Animator animation)
    {
        setPlaying(false);
        resetView();
        if (animatorListener != null)
        {
            animatorListener.onAnimationEnd(animation);
        }
    }

    protected void notifyAnimationCancel(Animator animation)
    {
        setPlaying(false);
        if (animatorListener != null)
        {
            animatorListener.onAnimationCancel(animation);
        }
    }

    protected void notifyAnimationRepeat(Animator animation)
    {
        if (animatorListener != null)
        {
            animatorListener.onAnimationRepeat(animation);
        }
    }
}
