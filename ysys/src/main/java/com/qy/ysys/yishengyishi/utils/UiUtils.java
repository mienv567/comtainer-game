package com.qy.ysys.yishengyishi.utils;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.AppImpl;

/**
 * description:
 *
 * @version V1.0 <描述当前版本功能>
 * @author: Tony
 * email:chenchenyanrong@163.com
 * @date: 2016-09-26 15:35
 */
public class UiUtils {
    /**
     * 获取当前设备的显示器的显示区矩阵信息
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取当前设备的显示器的像素密度与标准屏幕(320*480)的比值
     * @param context
     * @return
     */
    public static float getDisplayDensity(Context context)
    {
        return context.getResources().getDisplayMetrics().density;
    }


    /**
     * 获取当前设备显示器的宽度的像素值
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.widthPixels;
    }

    /**
     * 获取当前设备显示器的高度的像素值
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.heightPixels;
    }

    /**
     * dip转换成px
     * @param dip
     * @return
     */
    public static int dip2px(int dip)
    {
        DisplayMetrics metrics = AppImpl.getApplication().getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * px转换成dip
     * @param px
     * @return
     */
    public static int px2dip( int px)
    {
        DisplayMetrics metrics = AppImpl.getApplication().getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }

    /**
     * px转成sp，主要用于字体
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, int px){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float scaledDensity = metrics.scaledDensity;
        return (int)(px/scaledDensity +0.5f);
    }

    /**
     * sp转换成px
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, int sp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float scaledDensity = metrics.scaledDensity;
        return (int)(sp * scaledDensity +0.5f);
    }

    public static ViewGroup.MarginLayoutParams getViewMarginLayoutParams(View view)
    {
        ViewGroup.MarginLayoutParams result = null;
        if (view != null)
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null && params instanceof ViewGroup.MarginLayoutParams)
            {
                result = (ViewGroup.MarginLayoutParams) params;
            }
        }
        return result;
    }

    public static void hide(View view)
    {
        if (view != null && view.getVisibility() != View.GONE)
        {
            view.setVisibility(View.GONE);
        }
    }

    public static void invisible(View view)
    {
        if (view != null && view.getVisibility() != View.INVISIBLE)
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void show(View view)
    {
        if (view != null && view.getVisibility() != View.VISIBLE)
        {
            view.setVisibility(View.VISIBLE);
        }
    }

}
