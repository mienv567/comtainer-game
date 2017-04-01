package com.fanwe.live.appview.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.animator.SDAnim;
import com.fanwe.library.listener.SDSimpleAnimatorListener;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class GiftAnimatorRocket1 extends GiftAnimatorView
{
    private View fl_rocket_root;

    private TextView tv_number;
    private ImageView iv_rocket;
    private ImageView iv_rocket_smoke;

    private int number = 3;

    public GiftAnimatorRocket1(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GiftAnimatorRocket1(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GiftAnimatorRocket1(Context context)
    {
        super(context);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_gift_animator_rocket1;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        fl_rocket_root = find(R.id.fl_rocket_root);

        tv_number = find(R.id.tv_number);
        iv_rocket = find(R.id.iv_rocket);
        iv_rocket_smoke = find(R.id.iv_rocket_smoke);
        tv_gift_desc = find(R.id.tv_gift_desc);

    }

    private void updateNumber()
    {
        tv_number.setText(String.valueOf(number));
    }

    private void resetNumber()
    {
        number = 3;
        updateNumber();
    }

    @Override
    protected void createAnimator()
    {
        //火箭淡入
        SDAnim animAlphaIn = SDAnim.from(iv_rocket).setAlpha(0, 1f).setDuration(1000).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(fl_rocket_root);
                notifyAnimationStart(animation);
            }
        });
        getAnimatorSet().play(animAlphaIn.get());

        //数字倒数
        ValueAnimator animNumStartDelay = SDAnim.stop(500);
        SDAnim animNumScaleX = SDAnim.from(tv_number).setScaleX(1f, 0f).setRepeatCount(2).setDuration(1000);
        SDAnim animNumScaleY = animNumScaleX.clone().setScaleY(1f, 0f).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(tv_number);
                updateNumber();
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                SDViewUtil.invisible(tv_number);
                resetNumber();
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
                number--;
                updateNumber();
            }
        });
        getAnimatorSet().play(animNumStartDelay).after(animAlphaIn.get());
        getAnimatorSet().play(animNumScaleX.get()).with(animNumScaleY.get()).after(animNumStartDelay);

        //火箭起飞
        int rocketY1 = 0;
        int rocketY2 = SDAnimationUtil.getYTopOut(fl_rocket_root);

        SDAnim animRocket = SDAnim.from(fl_rocket_root).setY(rocketY1, rocketY2).setDuration(3000).setAccelerate().addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.startAnimationDrawable(iv_rocket.getDrawable());
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                SDViewUtil.stopAnimationDrawable(iv_rocket.getDrawable());
            }
        });
        getAnimatorSet().play(animRocket.get()).after(animNumScaleX.get());

        //烟雾
        ValueAnimator animSmokeStartDelay = SDAnim.stop(500);
        SDAnim animSmokeAlphaIn = SDAnim.from(iv_rocket_smoke).setAlpha(0, 1f).setDuration(3000);
        SDAnim animSmokeAlphaOut = animSmokeAlphaIn.clone().setAlpha(1f, 0).setDuration(500).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                notifyAnimationEnd(animation);
            }
        });
        getAnimatorSet().play(animSmokeStartDelay).after(animNumScaleX.get());
        getAnimatorSet().play(animSmokeAlphaIn.get()).after(animSmokeStartDelay);
        getAnimatorSet().play(animSmokeAlphaOut.get()).after(animSmokeAlphaIn.get());

    }

    @Override
    protected void resetView()
    {
        resetNumber();
        SDViewUtil.invisible(fl_rocket_root);
        SDViewUtil.invisible(tv_number);
        SDViewUtil.invisible(iv_rocket_smoke);
    }
}
