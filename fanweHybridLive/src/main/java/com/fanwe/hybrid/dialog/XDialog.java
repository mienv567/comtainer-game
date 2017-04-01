package com.fanwe.hybrid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fanwe.live.R;

/**
 * Created by Yuan on 2017/3/26.
 * 邮箱：44004606@qq.com
 */

public abstract class XDialog {

    private Dialog mDialog;
    private Window mWindow;

    public XDialog(Context context, int layoutId) {
        DialogViewHolder holder = DialogViewHolder.get(context, layoutId);
        View rootView = holder.getConvertView();
        mDialog = new Dialog(context, R.style.dialog);

        mDialog.setContentView(rootView);
        mWindow = mDialog.getWindow();
        convert(holder);
    }

    protected abstract void convert(DialogViewHolder holder);

    /**
     * 显示dialog
     */
    public XDialog showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        return this;
    }

    /**
     * 是否显示默认动画(淡入淡出)
     */
    public XDialog showDialog(boolean isAnimation) {
        if (isAnimation) {
            mWindow.setWindowAnimations(R.style.dialog_scale_anim);
        }
        mDialog.show();
        return this;
    }

    /**
     * 设置一个自定义的弹出动画
     */
    public XDialog setCustomAnimation(@StyleRes int style) {
        mWindow.setWindowAnimations(style);
        return this;
    }

    /**
     * 设置dialog背景亮度, 值为0~1, 1.0表示全黑  0.0表示全透明
     */
    public XDialog setBackgroundLight(float light) {
        if (light < 0 || light > 1) {
            return this;
        }
        WindowManager.LayoutParams attributes = mWindow.getAttributes();
        attributes.dimAmount = light;
        mWindow.setAttributes(attributes);
        return this;
    }

    /**
     * 屏幕顶部弹出
     */
    public XDialog fromTop() {
        mWindow.setWindowAnimations(R.style.window_top_in_top_out);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        return this;
    }

    /**
     * 底部弹出, 底部消失, 显示在底部
     */
    public XDialog fromBottom() {
        mWindow.setWindowAnimations(R.style.window_bottom_in_bottom_out);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        return this;
    }

    /**
     * 屏幕左边弹出, 左边消失
     */
    public XDialog fromLeft() {
        mWindow.setWindowAnimations(R.style.window_left_in_left_out);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setGravity(Gravity.CENTER | Gravity.START);
        return this;
    }

    /**
     * 屏幕右边弹出, 右边消失
     */
    public XDialog fromRight() {
        mWindow.setWindowAnimations(R.style.window_right_in_right_out);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setGravity(Gravity.END);
        return this;
    }

    /**
     * 全屏显示
     */
    public XDialog fullscreen() {
        WindowManager.LayoutParams wl = mWindow.getAttributes();
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 全屏宽度
     */
    public XDialog fullWidth() {
        WindowManager.LayoutParams wl = mWindow.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 全屏高度
     */
    public XDialog fullHeight() {
        WindowManager.LayoutParams wl = mWindow.getAttributes();
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 自定义宽高
     */
    public XDialog setWidthAndHeight(int width, int height) {
        WindowManager.LayoutParams wl = mWindow.getAttributes();
        wl.width = width;
        wl.height = height;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * cancel dialog
     */
    public void cancel() {
        dismiss();
    }

    /**
     * dismiss dialog
     */
    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * dialog消失监听
     */
    public XDialog setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
        return this;
    }

    /**
     * dialog取消监听
     */
    public XDialog setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
        return this;
    }

    /**
     * 是否可取消
     */
    public XDialog setCancelable(boolean cancel) {
        mDialog.setCancelable(cancel);
        return this;
    }

    /**
     * 是否可以点击外部消失
     */
    public XDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
}
