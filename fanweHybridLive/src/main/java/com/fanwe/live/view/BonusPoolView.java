package com.fanwe.live.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 刘焕宇 on 17/3/25.
 * QQ：310719413
 * Email：freshmboy@126.com
 */
public class BonusPoolView extends TextView {

    private int mWidth;

    private List<Integer> numberHeights = new ArrayList<>(); // 随时变动的数字Y坐标
    private List<Integer> numberXCoors = new ArrayList<>(); // 数字的X坐标（不变）
    private List<Integer> splitXCoors = new ArrayList<>(); // 符号的X坐标（不变）
    private List<Integer> targetNumber = new ArrayList<>(); // 目标数字（最后要停住的数字，不变）
    private List<Integer> numbers = new ArrayList<>(); // 随时变动的数据 从0-9
    private int mHeight;
    private String mText;
    private boolean hasMeasured;
    private int mLength;
    private int backGroundColor = Color.TRANSPARENT;
    private Timer mTimer;
    private int dSpeed; // 数字移动速度的最小差值 ds
    private int mPeriod = 30; // 间隔多久post一次重绘任务
    private int mBaseSpeed;// 数字移动速度最小值 s

    public BonusPoolView(Context context) {
        super(context);
        setTimer();
    }

    public BonusPoolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTimer();
    }

    public BonusPoolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTimer();
    }


    public void setTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        }, mPeriod, mPeriod);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, -getPaint().getFontMetrics().bottom);
        canvas.drawColor(backGroundColor);
        calculate();
        drawText(canvas);
    }

    private void calculate() {
        if (!hasMeasured) {
            hasMeasured = true;
            // 初始化各种数字
            initParams();
        } else {
            boolean stop = true;
            for (int i = 0; i < mLength; i++) {
                int h1 = numberHeights.get(i) + mBaseSpeed + Math.min(i * dSpeed, mHeight / 5);
                if (h1 > mHeight) {
                    h1 = h1 - mHeight;
                    if (targetNumber.get(i).equals(numbers.get(i))) {
//                        numberHeights.set(i, (int) (mHeight - getPaint().getFontMetrics().bottom));
                        numberHeights.set(i, mHeight);
                        continue;
                    }
                    numbers.set(i, (numbers.get(i) + 1) % 10);
                }
                numberHeights.set(i, h1);
                stop = false;
            }
            if (stop) {
                mTimer.cancel();
            }
        }
    }

    private void drawText(Canvas canvas) {
        TextPaint paint = getPaint();
        for (int i = 0; i < mLength; i++) {
            int x = numberXCoors.get(i);
            int y = numberHeights.get(i);
            canvas.drawText(numbers.get(i) + "", x, y, paint);
            canvas.drawText((numbers.get(i) + 9) % 10 + "", x, y + mHeight, paint);
        }
        for (Integer splitXCoor : splitXCoors) {
            canvas.drawText(",", splitXCoor, mHeight, paint);
        }

    }

    private void initParams() {
        mText = getText().toString();
        int totalLength = mText.length();
        mWidth = getWidth();
        mHeight = getHeight();
        mLength = 0;
        for (int i = 0; i < totalLength; i++) {
            char c = mText.charAt(i);
            if (c >= '0' && c <= '9') {
                numberXCoors.add(i == 0 ? 0 : (int) getPaint().measureText(mText.substring(0, i)));
                numberHeights.add(0);
                numbers.add(0);
                targetNumber.add(c - 48);
                mLength++;
            } else {
                splitXCoors.add((int) getPaint().measureText(mText.substring(0, i)));
            }
        }
        try {
            backGroundColor = ((ColorDrawable) getBackground()).getColor();
        } catch (Exception e) {
        }

        dSpeed = Math.max(mHeight / 50, 1);
        mBaseSpeed = mHeight * mPeriod / 500;
    }


}
