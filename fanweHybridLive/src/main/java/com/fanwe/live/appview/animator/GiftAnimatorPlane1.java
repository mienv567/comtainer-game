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
 * Created by Administrator on 2016/8/8.
 */
public class GiftAnimatorPlane1 extends GiftAnimatorView
{

    private View fl_animator;

    private ImageView iv_screw1;
    private ImageView iv_screw2;
    private ImageView iv_screw3;
    private ImageView iv_screw4;

    public GiftAnimatorPlane1(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GiftAnimatorPlane1(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GiftAnimatorPlane1(Context context)
    {
        super(context);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_gift_animator_plane1;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        fl_animator = find(R.id.fl_animator);
        iv_screw1 = find(R.id.iv_screw1);
        iv_screw2 = find(R.id.iv_screw2);
        iv_screw3 = find(R.id.iv_screw3);
        iv_screw4 = find(R.id.iv_screw4);
        tv_gift_desc = find(R.id.tv_gift_desc);
    }

    @Override
    protected void createAnimator()
    {
        //螺旋桨
        SDAnim animScrew1 = SDAnim.from(iv_screw1).setRotation().setDuration(500);
        SDAnim animScrew2 = animScrew1.clone().setTarget(iv_screw2);
        SDAnim animScrew3 = animScrew1.clone().setTarget(iv_screw3);
        SDAnim animScrew4 = animScrew1.clone().setTarget(iv_screw4);

        //飞机
        int planeX1 = SDAnimationUtil.getXRightOut(fl_animator);
        int planeX2 = SDAnimationUtil.getXCenterCenter(fl_animator);
        int planeX3 = SDAnimationUtil.getXLeftOut(fl_animator);

        int planeY1 = SDAnimationUtil.getYTopOut(fl_animator);
        int planeY2 = SDAnimationUtil.getYCenterCenter(fl_animator);
        int planeY3 = SDAnimationUtil.getYBottomOut(fl_animator);

        SDAnim animX1 = SDAnim.from(fl_animator).setX(planeX1, planeX2).setDecelerate().setDuration(2000);
        SDAnim animY1 = animX1.clone().setY(planeY1, planeY2);

        ValueAnimator animStop = SDAnim.stop(1500);

        SDAnim animX2 = SDAnim.from(fl_animator).setX(planeX2, planeX3).setDuration(2000).setAccelerate();
        SDAnim animY2 = animX2.clone().setY(planeY2, planeY3).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(fl_animator);
                notifyAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                notifyAnimationEnd(animation);
            }
        });

        getAnimatorSet().playTogether(animScrew1.get(), animScrew2.get(), animScrew3.get(), animScrew4.get());
        getAnimatorSet().play(animScrew1.get()).with(animX1.get()).with(animY1.get());
        getAnimatorSet().play(animStop).after(animX1.get());
        getAnimatorSet().play(animX2.get()).with(animY2.get()).after(animStop);
    }

    @Override
    protected void resetView()
    {
        SDViewUtil.invisible(fl_animator);
        SDViewUtil.resetView(fl_animator);
    }
}
