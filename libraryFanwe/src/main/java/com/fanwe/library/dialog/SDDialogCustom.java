package com.fanwe.library.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDTimer;
import com.fanwe.library.utils.SDTimer.SDTimerListener;
import com.fanwe.library.utils.SDViewUtil;

public class SDDialogCustom extends SDDialogBase
{
    public TextView tv_title;
    public TextView tv_tip;
    private LinearLayout ll_content;
    public TextView tv_cancel;
    public TextView tv_confirm;

    private SDDialogCustomListener mListener;

    private SDTimer timer = new SDTimer();

    public SDDialogCustom setmListener(SDDialogCustomListener mListener)
    {
        this.mListener = mListener;
        return this;
    }

    public SDDialogCustom(Activity activity)
    {
        super(activity);
        init();
    }

    protected void init()
    {
        setContentView(R.layout.dialog_custom);
        tv_title = (TextView) findViewById(R.id.dialog_custom_tv_title);
        tv_tip = (TextView) findViewById(R.id.dialog_custom_tv_tip);
        ll_content = (LinearLayout) findViewById(R.id.dialog_custom_ll_content);
        tv_cancel = (TextView) findViewById(R.id.dialog_custom_tv_cancel);
        tv_confirm = (TextView) findViewById(R.id.dialog_custom_tv_confirm);

        SDViewUtil.setBackgroundDrawable(getContentView(), new SDDrawable().color(Color.WHITE).cornerAll(config.getCornerRadius()));
        initViewStates();
    }

    private void initViewStates()
    {
        SDViewUtil.hide(tv_title);
        SDViewUtil.hide(tv_cancel);
        SDViewUtil.hide(tv_confirm);

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        setTextColorCancel(config.getMainColor());
        setTextColorConfirm(config.getMainColor());

        setTextTitle("提示").setTextConfirm("确定").setTextCancel("取消");
    }

    public SDDialogCustom setCustomView(View view)
    {
        setCustomView(view, null);
        return this;
    }

    public SDDialogCustom setCustomView(View view, LinearLayout.LayoutParams params)
    {
        ll_content.removeAllViews();
        if (params == null)
        {
            params = SDViewUtil.getLayoutParamsLinearLayoutMM();
        }
        ll_content.addView(view, params);
        return this;
    }

    public SDDialogCustom setCustomView(int layoutId)
    {
        ll_content.removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutId, ll_content, true);
        return this;
    }

    private void changeBackground()
    {
        if (tv_cancel.getVisibility() == View.VISIBLE && tv_confirm.getVisibility() == View.VISIBLE)
        {
            SDViewUtil.setBackgroundDrawable(tv_cancel, getBackgroundBottomLeft());
            SDViewUtil.setBackgroundDrawable(tv_confirm, getBackgroundBottomRight());
        } else if (tv_cancel.getVisibility() == View.VISIBLE)
        {
            SDViewUtil.setBackgroundDrawable(tv_cancel, getBackgroundBottomSingle());
        } else if (tv_confirm.getVisibility() == View.VISIBLE)
        {
            SDViewUtil.setBackgroundDrawable(tv_confirm, getBackgroundBottomSingle());
        }
    }

    // tip
    public void showTip(String tip, long time)
    {
        if (!TextUtils.isEmpty(tip) && time > 0)
        {
            tv_tip.setText(tip);
            SDViewUtil.show(tv_tip);
            timer.startWork(time, new SDTimerListener()
            {

                @Override
                public void onWorkMain()
                {
                    tv_tip.setText("");
                    SDViewUtil.hide(tv_tip);
                }

                @Override
                public void onWork()
                {
                }
            });
        }
    }

    public void showTip(String tip)
    {
        showTip(tip, 2000);
    }

    // ---------------------------color

    public SDDialogCustom setTextColorTitle(int color)
    {
        tv_title.setTextColor(color);
        return this;
    }

    public SDDialogCustom setTextColorCancel(int color)
    {
        tv_cancel.setTextColor(color);
        return this;
    }

    public SDDialogCustom setTextColorConfirm(int color)
    {
        tv_confirm.setTextColor(color);
        return this;
    }

    // ---------------------------text
    public SDDialogCustom setTextTitle(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            SDViewUtil.hide(tv_title);
        } else
        {
            SDViewUtil.show(tv_title);
            tv_title.setText(text);
        }
        return this;
    }

    public SDDialogCustom setTextCancel(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            SDViewUtil.hide(tv_cancel);
        } else
        {
            SDViewUtil.show(tv_cancel);
            tv_cancel.setText(text);
        }
        changeBackground();
        return this;
    }

    public SDDialogCustom setTextConfirm(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            SDViewUtil.hide(tv_confirm);
        } else
        {
            SDViewUtil.show(tv_confirm);
            tv_confirm.setText(text);
        }
        changeBackground();
        return this;
    }

    @Override
    public void onClick(View v)
    {
        if (v == tv_cancel)
        {
            clickCancel(v);
        } else if (v == tv_confirm)
        {
            clickConfirm(v);
        }

    }

    private void clickCancel(View v)
    {
        if (mListener != null)
        {
            mListener.onClickCancel(v, SDDialogCustom.this);
        }
        dismissAfterClick();
    }

    private void clickConfirm(View v)
    {
        if (mListener != null)
        {
            mListener.onClickConfirm(v, SDDialogCustom.this);
        }
        dismissAfterClick();
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        if (mListener != null)
        {
            mListener.onDismiss(SDDialogCustom.this);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        timer.stopWork();
    }

    public interface SDDialogCustomListener
    {
        void onClickCancel(View v, SDDialogCustom dialog);

        void onClickConfirm(View v, SDDialogCustom dialog);

        void onDismiss(SDDialogCustom dialog);
    }
}
