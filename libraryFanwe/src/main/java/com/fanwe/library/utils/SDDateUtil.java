package com.fanwe.library.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDDateUtil
{
    public static final long MILLISECONDS_DAY = 1000 * 60 * 60 * 24;
    public static final long MILLISECONDS_HOUR = 1000 * 60 * 60;
    public static final long MILLISECONDS_MINUTES = 1000 * 60;
    public static final long MILLISECONDS_SECOND = 1000;

    public static long getDuringDay(long mss)
    {
        return mss / MILLISECONDS_DAY;
    }

    public static long getDuringHours(long mss)
    {
        return (mss % MILLISECONDS_DAY) / MILLISECONDS_HOUR;
    }

    public static long getDuringMinutes(long mss)
    {
        return (mss % MILLISECONDS_HOUR) / MILLISECONDS_MINUTES;
    }

    public static long getDuringSeconds(long mss)
    {
        return (mss % MILLISECONDS_MINUTES) / MILLISECONDS_SECOND;
    }

    public static long getTotalMinutes(long mss)
    {
        return mss / MILLISECONDS_MINUTES;
    }

    public static String getTotalMinutesFormat(long mss)
    {
        long value = SDDateUtil.getTotalMinutes(mss);
        return formatDuringValue(value);
    }

    public static String getDuringSecondsFormat(long mss)
    {
        long value = SDDateUtil.getDuringSeconds(mss);
        return formatDuringValue(value);
    }

    public static String getDuringMinutesFormat(long mss)
    {
        long value = SDDateUtil.getDuringMinutes(mss);
        return formatDuringValue(value);
    }

    private static String formatDuringValue(long value)
    {
        String result = null;
        if (value < 10)
        {
            result = "0" + value;
        } else
        {
            result = String.valueOf(value);
        }
        return result;
    }

    /**
     * yyyy-MM-dd HH:mm:ss转毫秒
     *
     * @param stringLong
     * @return
     */
    public static long yyyyMMddHHmmss2Mil(String stringLong)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date date = formatter.parse(stringLong);
            long mil = date.getTime();
            return mil;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date yyyyMMdd2Mil(String yyyyMMdd)
    {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try
        {
            date = format.parse(yyyyMMdd);
        } catch (ParseException e)
        {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    /**
     * 毫秒转yyyy-MM-dd HH:mm:ss
     *
     * @param mil
     * @return
     */
    public static String mil2yyyyMMddHHmmss(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转yyyy-MM-dd HH:mm:ss
     *
     * @param mil
     * @return
     */
    public static String mil2yyyyMMddHHmm(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转HH
     *
     * @param mil
     * @return
     */
    public static String mil2HH(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转MM-dd HH:mm:ss
     *
     * @param mil
     * @return
     */
    public static String mil2MMddHHmmss(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转 yyyy-MM-dd
     *
     * @param mil
     * @return
     */
    public static String mil2yyyyMMdd(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转 MM-dd
     *
     * @param mil
     * @return
     */
    public static String mil2MMdd(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转HH:mm:ss
     *
     * @param mil
     * @return
     */
    public static String mil2HHmmss(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 毫秒转HH:mm
     *
     * @param mil
     * @return
     */
    public static String mil2HHmm(long mil)
    {
        Date date = new Date(mil);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 返回当前时间的yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNow_yyyyMMddHHmmss()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * @return 返回当前时间的yyyy-MM-dd字符串
     */
    public static String getNow_yyyyMMdd()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * @return 返回当前时间的HH:mm:ss字符串
     */
    public static String getNow_HHmmss()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 返回指定时间的yyyy-MM-dd字符串
     *
     * @param date 指定时间
     * @return "yyyy-MM-dd"
     */
    public static String getYYmmddFromDate(Date date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getYYmmddhhmmssFromDate(Date date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String formatDuringFrom(long timestamp)
    {
        long current = System.currentTimeMillis();
        long timeSpan = current - timestamp;

        if (timeSpan <= 0 || timeSpan < MILLISECONDS_MINUTES)
        {
            return "刚刚";
        } else if (timeSpan < MILLISECONDS_HOUR)
        {
            long min = getDuringMinutes(timeSpan);
            return min + "分钟前";
        } else if (timeSpan < MILLISECONDS_DAY)
        {
            try
            {
                String hhmmCurrent = mil2HHmm(current);
                int hhCurrent = Integer.valueOf(hhmmCurrent.substring(0, 2));

                String hhmm = mil2HHmm(timestamp);
                int hh = Integer.valueOf(hhmm.substring(0, 2));

                if (hhCurrent > hh)
                {
                    if (hh <= 0)
                    {
                        return "半夜" + hhmm;
                    } else if (hh < 6)
                    {
                        return "凌晨" + hhmm;
                    } else if (hh < 9)
                    {
                        return "早上" + hhmm;
                    } else if (hh < 12)
                    {
                        return "上午" + hhmm;
                    } else if (hh == 12)
                    {
                        return "中午" + hhmm;
                    } else if (hh < 18)
                    {
                        return "下午" + hhmm;
                    } else
                    {
                        return "晚上" + hhmm;
                    }
                } else
                {
                    return "昨天" + hhmm;
                }
            } catch (Exception e)
            {
                return getDuringHours(timestamp) + "小时前";
            }
        } else
        {
            long day = getDuringDay(timeSpan);
            if (day <= 1)
            {
                return "昨天" + mil2HHmm(timestamp);
            } else if (day <= 2)
            {
                return "前天" + mil2HHmm(timestamp);
            } else
            {
                return mil2MMddHHmmss(timestamp);
            }
        }

    }
}