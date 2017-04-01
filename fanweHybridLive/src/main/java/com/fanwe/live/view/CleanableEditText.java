package com.fanwe.live.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.fanwe.live.R;


/**
 * 自定义带删除按钮的EditText
 */
public class CleanableEditText extends EditText implements OnFocusChangeListener,
        TextWatcher {
    /**
     * EditText右侧的删除按钮
     */
    private Drawable mClearDrawable;
    /**
     * 是否获取焦点
     */
    private boolean hasFocus;

    private Drawable disableLeftDrawable;
    private Drawable enableLeftDrawable;
    private boolean isChangeLeftDrawable;


    private View relevanceView;
    private int textLength;

    public CleanableEditText(Context context) {
        super(context);
        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CleanableEditText);
        enableLeftDrawable = ta.getDrawable(R.styleable.CleanableEditText_enableLeftIcon);
        isChangeLeftDrawable = ta.getBoolean(R.styleable.CleanableEditText_isChangeLeftIcon, false);
        ta.recycle();

        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        disableLeftDrawable = getCompoundDrawables()[0];
        if (!isChangeLeftDrawable) {
            enableLeftDrawable = disableLeftDrawable;
        }
        // 默认设置隐藏图标
        setClearIconVisible(false, false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /*
     * @说明：isInnerWidth, isInnerHeight为true，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX()获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight()获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth: getWidth() - getTotalPaddingRight()计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight: distance 删除图标顶部边缘到控件顶部边缘的距离
     * distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight())
                        && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候， 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0, true);
        } else {
            setClearIconVisible(false, false);
        }
    }

    /**
     * 设置是否显示删除图标
     */
    private void setClearIconVisible(boolean visible, boolean isLeftChange) {
        Drawable right = visible ? mClearDrawable : null;
        Drawable left = isLeftChange ? enableLeftDrawable : disableLeftDrawable;

        setCompoundDrawablesWithIntrinsicBounds(left, getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0, true);

            if (relevanceView != null && s.length() >= textLength) {
                relevanceView.setEnabled(true);
            } else if (relevanceView != null && s.length() < textLength) {
                relevanceView.setEnabled(false);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置跟编辑框有相关性的view，作用在于判断如果输入了内容，是否更改view的状态
     */
    public void setRelevanceView(View view, int textLength) {
        if (textLength <= 0) {
            return;
        }
        relevanceView = view;
        this.textLength = textLength;
    }
}
