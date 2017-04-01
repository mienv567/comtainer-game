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
public class GiftAnimatorPlane2 extends GiftAnimatorView
{

    private View fl_cloud;
    private View ll_plane;
    private ImageView iv_plane;

    public GiftAnimatorPlane2(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GiftAnimatorPlane2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GiftAnimatorPlane2(Context context)
    {
        super(context);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_gift_animator_plane2;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        fl_cloud = find(R.id.fl_cloud);
        ll_plane = find(R.id.ll_plane);
        iv_plane = find(R.id.iv_plane);
        tv_gift_desc = find(R.id.tv_gift_desc);

    }

    @Override
    protected void createAnimator()
    {
        //云朵
        int cloudY1 = SDAnimationUtil.getYBottomOut(fl_cloud);
        int cloudY2 = SDAnimationUtil.getYTop(fl_cloud);

        SDAnim animCoudY = SDAnim.from(fl_cloud).setY(cloudY1, cloudY2).setDuration(2000).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(fl_cloud);
                notifyAnimationStart(animation);
            }
        });

        //飞机
        int planeX1 = SDAnimationUtil.getXRightOut(ll_plane);
        int planeX2 = SDAnimationUtil.getXCenterCenter(ll_plane);
        int planeX3 = SDAnimationUtil.getXLeftOut(ll_plane);

        int planeY1 = SDAnimationUtil.getYTop(ll_plane);
        int planeY2 = SDAnimationUtil.getYCenterCenter(ll_plane);
        int planeY3 = SDAnimationUtil.getYBottomOut(ll_plane);

        SDAnim animPlaneX1 = SDAnim.from(ll_plane).setX(planeX1, planeX2).setDuration(2000).setDecelerate();
        SDAnim animPlaneY1 = animPlaneX1.clone().setY(planeY1, planeY2).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                SDViewUtil.show(ll_plane);
                SDViewUtil.startAnimationDrawable(iv_plane.getDrawable());
            }
        });

        ValueAnimator animStop = SDAnim.stop(1500);

        SDAnim animPlaneX2 = SDAnim.from(ll_plane).setX(planeX2, planeX3).setDuration(2000).setAccelerate();
        SDAnim animPlaneY2 = animPlaneX2.clone().setY(planeY2, planeY3).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                notifyAnimationEnd(animation);
            }
        });

        getAnimatorSet().play(animCoudY.get());
        getAnimatorSet().play(animPlaneX1.get()).with(animPlaneY1.get()).after(animCoudY.get());
        getAnimatorSet().play(animStop).after(animPlaneX1.get());
        getAnimatorSet().play(animPlaneX2.get()).with(animPlaneY2.get()).after(animStop);
    }

    @Override
    protected void resetView()
    {
        SDViewUtil.invisible(fl_cloud);
        SDViewUtil.resetView(fl_cloud);

        SDViewUtil.invisible(ll_plane);
        SDViewUtil.resetView(ll_plane);
        SDViewUtil.stopAnimationDrawable(iv_plane.getDrawable());
    }
}
