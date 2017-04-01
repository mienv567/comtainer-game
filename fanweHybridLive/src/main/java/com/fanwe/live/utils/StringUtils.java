package com.fanwe.live.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yong.zhang on 2017/3/24 0024.
 */

public class StringUtils {

    public static CharSequence formatDeadline(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM:SS");
        String title = "选手任务倒计时:";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(title);
        String time = timeFormat.format(date);
        builder.append(time);
        builder.setSpan(new ForegroundColorSpan(Color.WHITE),
                0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW),
                title.length(), title.length() + time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static String formatMoney(long count) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%03d", count / 1000 / 1000));
        builder.append(",");
        builder.append(String.format("%03d", count / 1000 % 1000));
        builder.append(",");
        builder.append(String.format("%03d", count % 1000));
        return builder.toString();
    }

    public static CharSequence formatAccountBalance(int goldCoin, int silverCoin, boolean isLandscape) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String label;
        if(isLandscape){
            label = "  金币  ";
        } else {
            label = "余额:    金币  ";
        }
        builder.append(label);
        String gold = String.valueOf(goldCoin);
        builder.append(gold);
        String desc = "    银币  ";
        builder.append(desc);
        String silver = String.valueOf(silverCoin);
        builder.append(silver);
        builder.setSpan(new ForegroundColorSpan(Color.WHITE),
                0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW),
                label.length(), label.length() + gold.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.WHITE),
                label.length() + gold.length(), label.length() + gold.length() + desc.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW),
                label.length() + gold.length() + desc.length(),
                label.length() + gold.length() + desc.length() + silver.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static CharSequence formatLikeNumber(int number) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String label = "今日可点赞次数:  ";
        builder.append(label);
        String strNumber = String.valueOf(number);
        builder.append(strNumber);
        String unit = " 次";
        builder.append(unit);
        builder.setSpan(new ForegroundColorSpan(Color.WHITE),
                0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW),
                label.length(), label.length() + strNumber.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW),
                label.length() + strNumber.length(),
                label.length() + strNumber.length() + unit.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}
