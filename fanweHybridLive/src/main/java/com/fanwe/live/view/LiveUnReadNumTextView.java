package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.fanwe.live.R;

/**
 * Created by Administrator on 2016/7/21.
 */
public class LiveUnReadNumTextView extends TextView
{
    private static final int DEFAULT_MAX_NUM = 99;
    private int maxNum = DEFAULT_MAX_NUM;

    public void setMaxNum(int maxNum)
    {
        this.maxNum = maxNum;
    }

    public LiveUnReadNumTextView(Context context)
    {
        super(context);
    }

    public LiveUnReadNumTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveUnReadNumTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        super.setText(setTextNum(text.toString()), type);
    }

    private String setTextNum(String text)
    {
        int num = 0;
        try
        {
            num = Integer.valueOf(text);
            if (num >= DEFAULT_MAX_NUM)
            {
                setBackgroundResource(R.drawable.layer_red_half_round);
                return maxNum + "+";
            } else if (num >= 10)
            {
                setBackgroundResource(R.drawable.layer_red_half_round);
                return Integer.toString(num);
            } else if (num <= 0)
            {
                setBackgroundResource(R.drawable.bg_circle_red);
                setVisibility(View.INVISIBLE);
                return Integer.toString(maxNum);
            } else
            {
                setBackgroundResource(R.drawable.bg_circle_red);
                return Integer.toString(num);
            }
        } catch (Exception e)
        {
            return "0";
        }
    }


}
