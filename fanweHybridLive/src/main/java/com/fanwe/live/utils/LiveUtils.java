package com.fanwe.live.utils;

import com.facebook.device.yearclass.DeviceInfo;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;

import java.text.DecimalFormat;

public class LiveUtils
{

    public static boolean isResultOk(Object result)
    {
        boolean isResultOk = false;
        if (result != null)
        {
            int intResult = 0;
            try
            {
                intResult = (Integer) result;
            } catch (Exception e)
            {
            }
//            if (intResult == AVError.AV_OK)
//            {
            isResultOk = true;
//            }
        }
        return isResultOk;
    }

    public static int getLevelImageResId(int level)
    {
        int resId = 0;
        try
        {
            String imageName = String.valueOf("rank_" + level);
            resId = SDViewUtil.getIdentifierDrawable(imageName);
        } catch (Exception e)
        {
            resId = R.drawable.nopic_expression;
        }
        return resId;
    }

    public static int getLevelBackgroudImageResId(long currentScore,long upScore,long downScore)
    {
        double range = upScore - downScore;
        double value = currentScore - downScore;
        double percent = ((value * 100) / range);
        int bgLevel = (int)(percent / 3.33);
        if(bgLevel == 0){
            bgLevel = 1;
        }else if(bgLevel > 30){
            bgLevel = 30;
        }
        int resId = 0;
        try
        {
            String imageName = String.valueOf("bg_level_" + bgLevel);
            resId = SDViewUtil.getIdentifierDrawable(imageName);
        } catch (Exception e)
        {
            resId = R.drawable.nopic_expression;
        }
        return resId;
    }

    public static int getSexImageResId(int sex)
    {
        int resId = 0;
        switch (sex)
        {
            case 0:

                break;
            case 1:
                resId = R.drawable.ic_global_male;
                break;
            case 2:
                resId = R.drawable.ic_global_female;
                break;
            default:
                break;
        }
        return resId;
    }

    public static String getFormatNumber(int number) {
        double result ;
        DecimalFormat format = new DecimalFormat("#.00");
        if(number >= 10000) {
            result = number;
            result = result/10000;
            return format.format(result) + "万";
        }
        return String.valueOf(number);
    }


    public static String getFormatNumber(long number) {
        double result ;
        DecimalFormat format = new DecimalFormat("#.00");
        if(number >= 10000) {
            result = number;
            result = result/10000;
            return format.format(result) + "万";
        }
        return String.valueOf(number);
    }

    /**
     * 格式化秒数
     * @param showTime
     * @return
     */
    public static String formatShowTime(long showTime){
        long hour=showTime/3600;
        long minute=showTime%3600/60;
        long second=showTime%60;
        String hour_str = "";
        String minute_str = "";
        String second_str = "";
        if(hour >0 && hour< 10){
            hour_str = "0"+hour+":";
        }else if(hour>=10){
            hour_str = hour+":";
        }
        if(minute >0 && minute< 10){
            minute_str = "0"+minute+":";
        }else if(minute == 0){
            minute_str = "00"+":";
        }else if(minute>=10){
            minute_str = minute+":";
        }
        if(second >0 && second< 10){
            second_str = "0"+second;
        }else if(second == 0){
            second_str = "00";
        }else{
            second_str = second+"";
        }
        return hour_str+minute_str+second_str;
    }

    /**
     * 判断是否为高配手机
     * @return
     */
    public static boolean isHighPhone(){
        boolean result = false;
        int cpuMax = DeviceInfo.getCPUMaxFreqKHz();
        int cpuNum = DeviceInfo.getNumberOfCPUCores();
        long memory = DeviceInfo.getTotalMemory(SDLibrary.getInstance().getApplication());
        if(cpuMax >= LiveConstant.LIVE_CPU_MAXHZ &&
                cpuNum >= LiveConstant.LIVE_CPU_NUM &&
                memory >= LiveConstant.LIVE_CPU_MAXHZ){
            result = true;
        }
        return result;
    }
}
