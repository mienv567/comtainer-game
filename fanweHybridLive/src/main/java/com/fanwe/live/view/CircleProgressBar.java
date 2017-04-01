package com.fanwe.live.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.hybrid.app.App;
import com.fanwe.live.R;
import com.fanwe.live.utils.UiUtils;


/**
 * description:
 *
 * @version Vprivate int0 <描述当前版本功能>
 * @author: Tony
 * email:chenchenyanrong@163.com
 * @date: 2016; //-09; //-29 10:46
 */
public class CircleProgressBar extends View {
    private int mWidth;
    private int mHeight;
    private int centerPointX; //中心点x坐标
    private int centerPointY; //中心点y坐标
    private OneHundredHitListener hitListener;
    private StrokeListener strokeListener;

    public void setHitListener(OneHundredHitListener listener) {
        this.hitListener = listener;
    }

    public void setStrokeListener(StrokeListener listener) {
        this.strokeListener = listener;
    }

    private int strokeWidth; //-描边宽度,有默认值
    private int strokeColor; //-描边的颜色,有默认值

    public int getStrokeProgress() {
        return strokeProgress;
    }

    private int strokeProgress; //描边的进度

    private int innerCircleRadius; //-内院半径,有默认值
    private int innerCircleColor; //-内院的颜色,有默认值

    private float hitCountSizeTextSize; // 视图中间显示的字体大小
    private int hitCountSizeTextColor; // 字体颜色

    public int getHitCount() {
        return hitCount;
    }

    private int hitCount; // 显示的点击数

    private int outterCircleRadius; //-外圆半径,内圆半径+描边宽度即为外圆半径
    private int outterCircleColor; //-外圆的颜色,与内圆/描边的颜色不一致突显层次感

    private int circleArcRadius; //-进度圆弧的半径,是外圆半径一致即可
    private int circleArcColor;  //-进度圆弧的颜色,有默认值,颜色值与其他颜色不一致突显层次感
    private int circleArcProgress; //进度圆弧的进度

    private int defaultHitCountSizeTextSize = UiUtils.sp2px(App.getApplication().getApplicationContext(), 10);
    private float defaultZoomRatio = 1.5f;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 绘图画笔

    public CircleProgressBar(Context context) {
        super(context);
    }


    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);  //消除锯齿
        mPaint.setStrokeCap(Paint.Cap.BUTT); // 设置笔触类型
        mPaint.setStyle(Paint.Style.FILL); //设置空心

        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBar,
                0, 0);

        try {
            strokeWidth = ta.getInteger(R.styleable.CircleProgressBar_strokeWidth, UiUtils.dip2px(App.getApplication().getApplicationContext(), 2));
            strokeColor = ta.getColor(R.styleable.CircleProgressBar_strokeColor,  Color.parseColor("#fa7d81"));
            strokeProgress = ta.getInteger(R.styleable.CircleProgressBar_strokeProgress, 100);
            if (strokeProgress < 0) {
                strokeProgress = 100;
            }
            if (strokeProgress > 100) {
                strokeProgress = strokeProgress % 100;
            }

            innerCircleColor = ta.getColor(R.styleable.CircleProgressBar_innerCircleColor, Color.parseColor("#666666"));

            outterCircleColor = ta.getColor(R.styleable.CircleProgressBar_innerCircleColor, Color.parseColor("#666666"));

            hitCount = ta.getInteger(R.styleable.CircleProgressBar_hitCount, 0);
            hitCountSizeTextSize = ta.getDimension(R.styleable.CircleProgressBar_hitCountSizeTextSize, defaultHitCountSizeTextSize);
            hitCountSizeTextColor = ta.getColor(R.styleable.CircleProgressBar_hitCountSizeTextColor, Color.parseColor("#FFFFFF"));

            circleArcColor = ta.getColor(R.styleable.CircleProgressBar_circleArcColor, Color.parseColor("#f78e7b"));
            circleArcProgress = ta.getInteger(R.styleable.CircleProgressBar_circleArcProgress, 0);
            if (circleArcProgress < 0) {
                circleArcProgress = 0;
            }
            if (circleArcProgress > 100) {
                circleArcProgress = circleArcProgress % 100;
            }

        } finally {
            ta.recycle();
        }

    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setCircleArcProgress(int circleArcProgress) {
        this.circleArcProgress = circleArcProgress;
    }

    public void setCircleArcColor(int circleArcColor) {
        this.circleArcColor = circleArcColor;
    }

    public void setCircleArcRadius(int circleArcRadius) {
        this.circleArcRadius = circleArcRadius;
    }

    public void setHitCountSizeTextColor(int hitCountSizeTextColor) {
        this.hitCountSizeTextColor = hitCountSizeTextColor;
    }

    public void setHitCountSizeTextSize(float hitCountSizeTextSize) {
        this.hitCountSizeTextSize = hitCountSizeTextSize;
    }

    public void setInnerCircleColor(int innerCircleColor) {
        this.innerCircleColor = innerCircleColor;
    }

    public void setInnerCircleRadius(int innerCircleRadius) {
        this.innerCircleRadius = innerCircleRadius;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        if (mWidth <= 0 || mHeight <= 0) {
//            throw new RuntimeException(CircleProgressBar.class
//                    .getSimpleName() + "view的长或宽不能少于等于0");
            return;
        }
        // 外圆沾满整个view的宽高,如果长宽不相等则外圆直径为较短边的边长
        if (mWidth >= mHeight) {
            outterCircleRadius = mHeight / 2;
        } else {
            outterCircleRadius = mWidth / 2;
        }
        innerCircleRadius = outterCircleRadius - strokeWidth; //内院半径等于外圆半径减去描边的厚度
        centerPointX = outterCircleRadius;
        centerPointY = outterCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.reset();
        //绘图顺序:外圆->内圆->进度圆弧->描边与文字显示
        // 1. 画外圆
        mPaint.setColor(outterCircleColor);
        mPaint.setAlpha(64);
        canvas.drawCircle(centerPointX, centerPointY, outterCircleRadius - strokeWidth, mPaint);

        // 2. 画内圆
        mPaint.setColor(innerCircleColor);
        mPaint.setAlpha(192);
        canvas.drawCircle(centerPointX, centerPointY, innerCircleRadius - 2 * strokeWidth, mPaint);

        // 3. 画圆弧
        mPaint.setAlpha(255);
        mPaint.setColor(circleArcColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawArc(new RectF(strokeWidth, strokeWidth, outterCircleRadius * 2 - strokeWidth, outterCircleRadius * 2 - strokeWidth), 90 - 1.8f * circleArcProgress, 3.6f * circleArcProgress, false, mPaint);

        // 4. 画描边
        mPaint.setColor(strokeColor);
        mPaint.setAlpha(192);
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 设置笔触类型
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setStrokeWidth(2 * strokeWidth); //设置圆环的宽度
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawArc(new RectF(2 * strokeWidth, 2 * strokeWidth, outterCircleRadius * 2 - strokeWidth * 2, outterCircleRadius * 2 - strokeWidth * 2), -90, 3.6f * strokeProgress, false, mPaint);

        // 5. 显示图案
        if (hitCount > 0) {
            mPaint.reset();
            mPaint.setColor(hitCountSizeTextColor);
            mPaint.setTextSize(hitCountSizeTextSize);
            mPaint.setAntiAlias(true);  //消除锯齿
            float textWidth = mPaint.measureText("X" + hitCount);
            canvas.drawText("X" + hitCount, outterCircleRadius - textWidth / 2, outterCircleRadius + hitCountSizeTextSize / 2 - strokeWidth, mPaint);
        }
    }

    public void hit() {
        hitCount++;
        if (hitCount == 99) {
            if (null != hitListener) {
                hitListener.HitOneHundred();
            }
            hitCount = 0;
        }
        strokeProgress = 100;
        circleArcProgress = hitCount;
        postInvalidate();
        this.animate().cancel();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "hitCountSizeTextSize", defaultHitCountSizeTextSize * defaultZoomRatio, defaultHitCountSizeTextSize);
        animator.setDuration(1300);
        animator.start();
    }




    public void setStrokeProgress(int strokeProgress) {
        this.strokeProgress = strokeProgress;
    }

    public void setStrokeProgressValue(int strokeProgress) {
        this.strokeProgress = strokeProgress;
        if (strokeProgress <= 0) {
            this.strokeProgress = 100;
            if (null != strokeListener) {
                strokeListener.strokeFull();
            }
            this.setVisibility(View.INVISIBLE);
        }

            invalidate();

    }

    public void reset() {
        hitCount = 0;
        strokeProgress = 100;
        circleArcProgress = 0;
        invalidate();

    }

    public interface OneHundredHitListener {
        public void HitOneHundred();
    }

    public interface StrokeListener {
        public void strokeFull();
    }
}
