package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.UiUtils;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class Timeline extends View {
    private Paint mPaint;
    private int radius;
    private int lineWidth;
    private  int spance;

    public Timeline(Context context) {
        super(context);
        initView(context);

    }

    public Timeline(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Timeline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        spance =UiUtils.dip2px(2);
        radius = UiUtils.dip2px(4);
        lineWidth = UiUtils.dip2px(2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.orange));
        canvas.drawCircle(radius+spance, radius+spance, radius, mPaint);
        mPaint.setStrokeWidth(spance);
        canvas.drawLine(spance+radius, radius+spance+radius, radius+radius, getMeasuredHeight(), mPaint);
        super.onDraw(canvas);
    }
}
