package com.fanwe.live.appview.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.animator.SDAnim;
import com.fanwe.library.listener.SDSimpleAnimatorListener;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsgGift;

/**
 * Created by Administrator on 2016/8/10.
 */
public class GifAnimatorCar1 extends GiftAnimatorView
{
    public GifAnimatorCar1(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public GifAnimatorCar1(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GifAnimatorCar1(Context context)
    {
        super(context);
    }

    private View fl_down_car;
    private ImageView iv_down_car_front_tyre;
    private ImageView iv_down_car_back_tyre;

    private View fl_up_car;
    private ImageView iv_up_car_front_tyre;
    private ImageView iv_up_car_back_tyre;

    private AnimatorSet animDownCar;
    private AnimatorSet animDownTyre;
    private ObjectAnimator animDownFrontTyre;
    private ObjectAnimator animDownBackTyre;

    private AnimatorSet animUpCar;
    private AnimatorSet animUpTyre;
    private ObjectAnimator animUpFrontTyre;
    private ObjectAnimator animUpBackTyre;

    private TextView tv_gift_desc_car_up;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_gift_animator_car1;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        fl_down_car = find(R.id.fl_car_down);
        iv_down_car_front_tyre = find(R.id.iv_car_down_front_tyre);
        iv_down_car_back_tyre = find(R.id.iv_car_down_back_tyre);

        fl_up_car = find(R.id.fl_car_up);
        iv_up_car_front_tyre = find(R.id.iv_car_up_front_tyre);
        iv_up_car_back_tyre = find(R.id.iv_car_up_back_tyre);

        tv_gift_desc_car_up = find(R.id.tv_gift_desc_car_up);
        tv_gift_desc = find(R.id.tv_gift_desc);

    }

    @Override
    public void setMsg(CustomMsgGift msg)
    {
        super.setMsg(msg);
        SDViewBinder.setTextView(tv_gift_desc_car_up, msg.getTop_title());
    }

    @Override
    protected void createAnimator()
    {
        //轮胎下来
        final SDAnim animDownFrontTyre = SDAnim.from(iv_down_car_front_tyre).setRotationReverse().setDuration(1000);
        final SDAnim animDownBackTyre = animDownFrontTyre.clone().setTarget(iv_down_car_back_tyre);

        //汽车下来
        int carDownX1 = SDAnimationUtil.getXRightOut(fl_down_car);
        int carDownX2 = SDAnimationUtil.getXCenterCenter(fl_down_car);
        int carDownX3 = SDAnimationUtil.getXLeftOut(fl_down_car);

        int carDownY1 = SDAnimationUtil.getYTopOut(fl_down_car);
        int carDownY2 = SDAnimationUtil.getYCenterCenter(fl_down_car);
        int carDownY3 = SDAnimationUtil.getYBottomOut(fl_down_car);

        SDAnim animCarDownX1 = SDAnim.from(fl_down_car).setX(carDownX1, carDownX2).setDuration(2000).setDecelerate();
        SDAnim animCarDownY1 = animCarDownX1.clone().setY(carDownY1, carDownY2).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                notifyAnimationStart(animation);
            }
        });
        getAnimatorSet().play(animCarDownX1.get()).with(animCarDownY1.get())
                .with(animDownFrontTyre.get()).with(animDownBackTyre.get());

        ValueAnimator animCarDownStop = SDAnim.stop(1500);
        getAnimatorSet().play(animCarDownStop).after(animCarDownX1.get());

        SDAnim animCarDownX2 = SDAnim.from(fl_down_car).setX(carDownX2, carDownX3).setDuration(2000).setAccelerate();
        SDAnim animCarDownY2 = animCarDownX2.clone().setY(carDownY2, carDownY3).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                animDownFrontTyre.get().cancel();
                animDownBackTyre.get().cancel();
                SDViewUtil.invisible(fl_down_car);
            }
        });
        getAnimatorSet().play(animCarDownX2.get()).with(animCarDownY2.get())
                .after(animCarDownStop);

        //轮胎上去
        ValueAnimator animCarUpStartDelay = SDAnim.stop(500);
        getAnimatorSet().play(animCarUpStartDelay).after(animCarDownX2.get());

        final SDAnim animUpFrontTyre = SDAnim.from(iv_up_car_front_tyre).setRotation().setDuration(1000);
        final SDAnim animUpBackTyre = animUpFrontTyre.clone().setTarget(iv_up_car_back_tyre);

        //汽车上去
        int carUpX1 = SDAnimationUtil.getXLeftOut(fl_up_car);
        int carUpX2 = SDAnimationUtil.getXCenterCenter(fl_up_car);
        int carUpX3 = SDAnimationUtil.getXRightOut(fl_up_car);

        int carUpY1 = SDAnimationUtil.getYBottomOut(fl_up_car);
        int carUpY2 = SDAnimationUtil.getYCenterCenter(fl_up_car);
        int carUpY3 = SDAnimationUtil.getYTopOut(fl_up_car);

        SDAnim animCarUpX1 = SDAnim.from(fl_up_car).setX(carUpX1, carUpX2).setDuration(2000).setDecelerate();
        SDAnim animCarUpY1 = animCarUpX1.clone().setY(carUpY1, carUpY2);
        getAnimatorSet().play(animCarUpX1.get()).with(animCarUpY1.get())
                .with(animUpFrontTyre.get()).with(animUpBackTyre.get())
                .after(animCarUpStartDelay);

        ValueAnimator animCarUpStop = SDAnim.stop(1500);
        getAnimatorSet().play(animCarUpStop).after(animCarUpX1.get());

        SDAnim animCarUpX2 = SDAnim.from(fl_up_car).setX(carUpX2, carUpX3).setDuration(2000).setAccelerate();
        SDAnim animCarUpY2 = animCarUpX2.clone().setY(carUpY2, carUpY3).addListener(new SDSimpleAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                animUpFrontTyre.get().cancel();
                animUpBackTyre.get().cancel();
                SDViewUtil.invisible(fl_up_car);
                notifyAnimationEnd(animation);
            }
        });
        getAnimatorSet().play(animCarUpX2.get()).with(animCarUpY2.get()).after(animCarUpStop);
    }

    @Override
    protected void resetView()
    {
        SDViewUtil.invisible(fl_down_car);
        SDViewUtil.resetView(fl_down_car);

        SDViewUtil.invisible(fl_up_car);
        SDViewUtil.resetView(fl_up_car);
    }


}
