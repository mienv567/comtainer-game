package com.fanwe.live.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;


public class PassMicTipsPop extends PopupWindow{

    private View contentView;
    public PassMicTipsPop(Activity context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = layoutInflater.inflate(R.layout.pop_pass_mic, null);
        setContentView(contentView);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        this.setBackgroundDrawable(dw);

        setOutsideTouchable(false);
        setFocusable(false);
    }



    public void showPopTips(View parent,int top,int left) {
        if(isShowing()) {
            dismiss();
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        showAtLocation(parent, Gravity.BOTTOM, location[0] - SDViewUtil.getScreenWidth() / 2 + this.getWidth() / 2 - left,
                SDViewUtil.getScreenHeight() - location[1] + top);
    }

}
