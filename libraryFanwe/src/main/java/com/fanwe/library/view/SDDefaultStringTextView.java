package com.fanwe.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.utils.SDOtherUtil;

/**
 * 显示默认字符串的TextView
 */
public abstract class SDDefaultStringTextView extends TextView
{
    public static final String BEFORE = "before";
    public static final String AFTER = "after";

    public SDDefaultStringTextView(Context context)
    {
        super(context);
        init();
    }

    public SDDefaultStringTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDDefaultStringTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        String defaultText = getDefaultText();
        String text = getText().toString();

        String result = SDOtherUtil.buildDefaultString(defaultText, text, BEFORE, AFTER);

        setText(result);
    }

    protected abstract String getDefaultText();

}
