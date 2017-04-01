package com.fanwe.auction.appview.room;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * Created by shibx on 2016/8/18.
 */
public class RoomTimerTextView extends TextView
{

    private SDLooper mHandler;//竞拍倒计时

    private TimerRunnable mRunnable;

    private long mil;//毫秒值

    public RoomTimerTextView(Context context)
    {
        super(context);
    }

    public RoomTimerTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomTimerTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoomTimerTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTime(int type, long mil)
    {
        //0 出局 1待付款 2排队中 3超时出局 4 付款完成
        if (type == 0)
        {
            setText("出局");
            return;
        } else if (type == 2)
        {
            setText("排队中");
            SDViewUtil.setTextViewColorResId(this, R.color.main_color);
            return;
        } else if (type == 3)
        {
            setText("超时未付款");
            SDViewUtil.setTextViewColorResId(this, R.color.main_color);
            return;
        } else if (type == 4)
        {
            setText("付款完成");
            return;
        }
        if (mil == 0)
        {
            return;
        }
        this.mil = mil;
        if (mHandler == null)
        {
            mHandler = new SDSimpleLooper();
        }
        if (mRunnable == null)
        {
            mRunnable = new TimerRunnable();
        }
        SDViewUtil.setTextViewColorResId(this, R.color.yellow);
        mHandler.start(1000, mRunnable);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (mHandler != null)
        {
            mHandler.stop();
        }
    }

    private class TimerRunnable implements Runnable
    {

        @Override
        public void run()
        {
            if (mil >= 0)
            {
                long hour = SDDateUtil.getDuringHours(mil * 1000);
                long min = SDDateUtil.getDuringMinutes(mil * 1000);
                long sec = SDDateUtil.getDuringSeconds(mil * 1000);
                setText(Long.toString(hour) + "小时" + Long.toString(min) + "分钟" + Long.toString(sec) + "秒 内需付款");
                mil--;
            } else
            {
                mHandler.stop();
            }
        }
    }

}
