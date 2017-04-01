package com.fanwe.library.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SDAnim implements Cloneable
{
    private ObjectAnimator animator = new ObjectAnimator();

    /**
     * 快捷创建对象方法
     *
     * @param target
     * @return
     */
    public static SDAnim from(View target)
    {
        SDAnim anim = new SDAnim();
        anim.setTarget(target);
        return anim;
    }

    public SDAnim setTarget(View target)
    {
        animator.setTarget(target);
        addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                View view = getTargetView();
                if (view != null)
                {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        });
        return this;
    }

    public View getTargetView()
    {
        View view = null;
        Object target = animator.getTarget();
        if (target instanceof View)
        {
            view = (View) target;
        }
        return view;
    }

    public static ValueAnimator stop(long duration)
    {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(duration);
        return anim;
    }

    public SDAnim setDuration(long duration)
    {
        animator.setDuration(duration);
        return this;
    }

    public SDAnim setRepeatCount(int count)
    {
        if (count < 0)
        {
            animator.setRepeatCount(ValueAnimator.INFINITE);
        } else
        {
            animator.setRepeatCount(count);
        }
        return this;
    }

    public SDAnim setX(float... values)
    {
        animator.setPropertyName("translationX");
        animator.setFloatValues(values);
        return this;
    }

    public SDAnim setY(float... values)
    {
        animator.setPropertyName("translationY");
        animator.setFloatValues(values);
        return this;
    }

    public SDAnim setAlpha(float... values)
    {
        animator.setPropertyName("alpha");
        animator.setFloatValues(values);
        return this;
    }

    public SDAnim setScaleX(float... values)
    {
        animator.setPropertyName("scaleX");
        animator.setFloatValues(values);
        return this;
    }

    public SDAnim setScaleY(float... values)
    {
        animator.setPropertyName("scaleY");
        animator.setFloatValues(values);
        return this;
    }

    public SDAnim setRotation(float... values)
    {
        animator.setPropertyName("rotation");
        animator.setFloatValues(values);
        setLinear();
        return this;
    }

    public SDAnim setRotation()
    {
        setRotation(0, 360).setRepeatCount(-1);
        return this;
    }

    public SDAnim setRotationReverse()
    {
        setRotation(0, -360).setRepeatCount(-1);
        return this;
    }

    public SDAnim addListener(Animator.AnimatorListener listener)
    {
        animator.addListener(listener);
        return this;
    }

    public SDAnim setInterpolator(Interpolator interpolator)
    {
        animator.setInterpolator(interpolator);
        return this;
    }

    public SDAnim setStartDelay(long delay)
    {
        animator.setStartDelay(delay);
        return this;
    }

    public SDAnim setDecelerate()
    {
        setInterpolator(new DecelerateInterpolator());
        return this;
    }

    public SDAnim setAccelerate()
    {
        setInterpolator(new AccelerateInterpolator());
        return this;
    }

    public SDAnim setAccelerateDecelerate()
    {
        setInterpolator(new AccelerateDecelerateInterpolator());
        return this;
    }

    public SDAnim setLinear()
    {
        setInterpolator(new LinearInterpolator());
        return this;
    }

    public void start()
    {
        animator.start();
    }

    public ObjectAnimator get()
    {
        return animator;
    }


    public void setAnimator(ObjectAnimator animator)
    {
        this.animator = animator;
    }

    @Override
    public SDAnim clone()
    {
        SDAnim anim = new SDAnim();
        anim.setAnimator(animator.clone());
        return anim;
    }
}
