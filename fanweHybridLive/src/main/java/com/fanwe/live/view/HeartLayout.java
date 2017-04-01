package com.fanwe.live.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HeartLayout extends RelativeLayout {

    private static final int HEART_WIDTH = SDViewUtil.dp2px(25);
    private static final int HEART_NUM_WIDTH = SDViewUtil.dp2px(30);
    private List<Interpolator> listInterpolator = new ArrayList<Interpolator>();
    private List<String> listDrawableName = new ArrayList<String>();
    public static Map<String, Drawable> mapNameDrawable = new HashMap<String, Drawable>();
    public static Map<String, String> mapNameUrl = new HashMap<>();
    private int height;
    private int width;
    private LayoutParams heartLayoutParams;
    private Random random = new Random();
    private int drawableHeight = SDViewUtil.dp2px(47);
    private int drawableWidth = SDViewUtil.dp2px(47);

    private LayoutParams lp;

    public HeartLayout(Context context) {
        super(context);
        init(null);
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);
    }

    /**
     * 因活动房间可能需要使用特定的点亮图标,在添加HeartLayout对象前务必调用该方法,否者用户点亮时会因获取不到点亮列表而报错
     *
     * @param drawables
     */
    public void init(Map<String, Drawable> drawables) {
        mapNameDrawable.clear();
        listDrawableName.clear();
        Drawable drawable = null;
        if (null == drawables || drawables.isEmpty()) {
            listDrawableName.add("heart0");
            listDrawableName.add("heart1");
            listDrawableName.add("heart2");
            listDrawableName.add("heart3");
            listDrawableName.add("heart4");
            listDrawableName.add("heart5");
            listDrawableName.add("heart6");

            mapNameDrawable.clear();

            for (String item : listDrawableName) {
                int id = SDViewUtil.getIdentifierDrawable(item);
                if (id != 0) {
                    drawable = getResources().getDrawable(id);
                    if (drawable != null) {
                        mapNameDrawable.put(item, drawable);
                    }
                }
            }
        } else {
            Set<String> keys = drawables.keySet();
            for (String key : keys) {
                drawable = drawables.get(key);
                listDrawableName.add(key);
                if (drawable != null) {
                    mapNameDrawable.put(key, drawable);
                }
            }
        }

        //底部 并且 水平居中
        lp = new LayoutParams(HEART_NUM_WIDTH, HEART_NUM_WIDTH);
        lp.addRule(CENTER_HORIZONTAL, TRUE);//这里的TRUE 要注意 不是true
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        createHeartLayoutParams();

        listInterpolator.add(new LinearInterpolator());
        listInterpolator.add(new AccelerateInterpolator());
        listInterpolator.add(new DecelerateInterpolator());
        listInterpolator.add(new AccelerateDecelerateInterpolator());

    }

    private void createHeartLayoutParams() {
        heartLayoutParams = new LayoutParams(HEART_WIDTH, HEART_WIDTH);
        heartLayoutParams.addRule(CENTER_HORIZONTAL, TRUE);// 这里的TRUE 要注意 不是true
        heartLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public String randomImageName() {
        int index = random.nextInt(listDrawableName.size());
        String name = listDrawableName.get(index);
        return name;
    }

    public void addHeart() {
       String imageName = listDrawableName.get(new Random(System.currentTimeMillis()).nextInt(listDrawableName.size()));
        addHeart(imageName);
    }

    public void addHeart(String imageName) {
        Drawable drawable = mapNameDrawable.get(imageName);

        if (drawable != null) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(drawable);
            imageView.setLayoutParams(heartLayoutParams);
            addView(imageView);

            Animator set = getAnimator(imageView,false);
            set.addListener(new AnimEndListener(imageView));
            set.start();
        }
    }



    public void addHeart(String imageName, String number) {
        Drawable drawable = mapNameDrawable.get(imageName);

        if (drawable != null) {

            RelativeLayout rl = (RelativeLayout) View.inflate(getContext(), R.layout.item_periscopelayout_withnumber, null);

            ImageView imageView = (ImageView) rl.findViewById(R.id.iv_periscopelimage);
            //随机选一个
            imageView.setImageDrawable(drawable);

            TextView tvCount = (TextView) rl.findViewById(R.id.tv_periscopel_count);
            tvCount.setText(number);
            rl.setLayoutParams(lp);
            addView(rl);
            Animator set = getAnimator(rl,true);
            set.addListener(new AnimEndListener(rl));
            set.start();
        }
    }

    private Animator getAnimator(View target,boolean forWithCount) {
//        AnimatorSet set = getEnterAnimtor(target);
//
//        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
//
//        AnimatorSet finalSet = new AnimatorSet();
//        finalSet.playSequentially(set);
//        finalSet.playSequentially(set, bezierValueAnimator);
//        finalSet.setInterpolator(listInterpolator.get(random.nextInt(listInterpolator.size())));
//        finalSet.setTarget(target);
//        return finalSet;

        AnimatorSet set = getEnterAnimtor(target);

        Animator bezierValueAnimator = getBezierValueAnimator(target,forWithCount);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set, bezierValueAnimator);
//        finalSet.setInterpolator(listInterpolator.get(random.nextInt(listInterpolator.size())));
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
//        AnimatorSet enter = new AnimatorSet();
//        enter.setDuration(500);
//        enter.setInterpolator(new LinearInterpolator());
//        enter.playTogether(alpha, scaleX, scaleY);
//        enter.setTarget(target);
//        return enter;

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 0.7f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 0.7f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private AnimatorSet getBezierValueAnimator(View target,boolean forWithCount) {

        //初始化一个贝塞尔计算器- - 传入
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        //这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = null;
        if(forWithCount){
            animator = ValueAnimator.ofObject(evaluator, new PointF((width - drawableWidth) / 2 + SDViewUtil.dp2px(9), height - drawableHeight + SDViewUtil.dp2px(17)), new PointF(getRandomNumber(getWidth()), 0));
        }else{
            animator = ValueAnimator.ofObject(evaluator, new PointF((width - drawableWidth) / 2 + SDViewUtil.dp2px(11), height - drawableHeight + SDViewUtil.dp2px(21)), new PointF(getRandomNumber(getWidth()), 0));
        }
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        //        animator.setDuration(2000);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.7f, 1.3f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.7f, 1.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.playTogether(animator,scaleX,scaleY);
        return animatorSet;
    }

    private int getRandomNumber(int number) {
        return number == 0 ? 0 : random.nextInt(number);
    }

    /**
     * 获取中间的两个 点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        try {
            pointF.x = random.nextInt((width -drawableWidth ));//减去100 是为了控制 x轴活动范围,看效果 随意~~
            //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
            pointF.y = random.nextInt((height - drawableHeight)) / scale;
        }catch (Exception e){
            e.printStackTrace();
        }
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
        }
    }
}
