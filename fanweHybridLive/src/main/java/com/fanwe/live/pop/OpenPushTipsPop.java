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

/**
 * Created by Administrator on 2016/7/5.
 */
public class OpenPushTipsPop extends PopupWindow{

    private View contentView;
    private TextView textView;
    public OpenPushTipsPop(Activity context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = layoutInflater.inflate(R.layout.pop_open_push, null);
        setContentView(contentView);
        textView = (TextView)contentView.findViewById(R.id.tv_pop_share_tips);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        this.setBackgroundDrawable(dw);

        setOutsideTouchable(true);
        setFocusable(false);
    }

    public void setTips(String tips) {
        textView.setText(tips);
    }

    public void showPopTips(String shareTips,View parent,int top,int left) {
        if(isShowing()) {
            dismiss();
        }
        setTips(shareTips);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        showAtLocation(parent, Gravity.BOTTOM, location[0] - SDViewUtil.getScreenWidth() / 2 + this.getWidth() / 2 - left, location[1] + top);
//        SDViewUtil.showPopTopAndLeft(this, parent, top, left);
    }

    public void showPopTips(String shareTips,View parent) {
        if(isShowing()) {
            dismiss();
        }
        setTips(shareTips);
        SDViewUtil.showPopTopAndLeft(this, parent, 0, SDViewUtil.dp2px(42));
    }
}
