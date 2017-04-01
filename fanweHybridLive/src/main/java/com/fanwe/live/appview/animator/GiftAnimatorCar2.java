package com.fanwe.live.appview.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.animator.SDAnim;
import com.fanwe.library.listener.SDSimpleAnimatorListener;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * 红色兰博基尼
 */
public class GiftAnimatorCar2 extends GiftAnimatorView
{
    public GiftAnimatorCar2(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GiftAnimatorCar2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GiftAnimatorCar2(Context context)
    {
        super(context);
    }

    private View fl_car;
    private ImageView iv_car_front_tyre;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_gift_animator_car2;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        fl_car = find(R.id.fl_car);
        iv_car_front_tyre = find(R.id.iv_car_front_tyre);
        tv_gift_desc = find(R.id.tv_gift_desc);
    }

    @Override
    protected void createAnimator()
    {

        SDAnim animFrontTyre = SDAnim.from(iv_car_front_tyre).setRotation().setDuration(1000).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.startAnimationDrawable(iv_car_front_tyre.getDrawable());
            }
        });

        int carX1 = SDAnimationUtil.getXLeftOut(fl_car);
        int carX2 = SDAnimationUtil.getXCenterCenter(fl_car);
        int carX3 = SDAnimationUtil.getXRightOut(fl_car);

        int carY1 = SDAnimationUtil.getYTopOut(fl_car);
        int carY2 = SDAnimationUtil.getYCenterCenter(fl_car);
        int carY3 = SDAnimationUtil.getYBottomOut(fl_car);

        SDAnim animCarX1 = SDAnim.from(fl_car).setX(carX1, carX2).setDuration(2000).setDecelerate();
        SDAnim animCarY1 = animCarX1.clone().setY(carY1, carY2).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(fl_car);
                notifyAnimationStart(animation);
            }
        });

        ValueAnimator animStop = SDAnim.stop(1500);

        SDAnim animCarX2 = SDAnim.from(fl_car).setX(carX2, carX3).setDuration(2000).setAccelerate();
        SDAnim animCarY2 = animCarX2.clone().setY(carY2, carY3).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                notifyAnimationEnd(animation);
            }
        });

        getAnimatorSet().playTogether(animFrontTyre.get(), animCarX1.get(), animCarY1.get());
        getAnimatorSet().play(animStop).after(animCarX1.get());
        getAnimatorSet().play(animCarX2.get()).with(animCarY2.get()).after(animStop);
    }

    @Override
    protected void resetView()
    {
        SDViewUtil.invisible(fl_car);
        SDViewUtil.resetView(fl_car);
        SDViewUtil.stopAnimationDrawable(iv_car_front_tyre.getDrawable());
    }
}
