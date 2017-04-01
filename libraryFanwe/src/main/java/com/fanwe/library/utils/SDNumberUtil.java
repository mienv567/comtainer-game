package com.fanwe.library.utils;

import java.math.BigDecimal;

public class SDNumberUtil
{

    public static double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double miles = dist * 60 * 1.1515 * 1.609344 * 1000;
        return miles;
    }

    // 将角度转换为弧度
    static double deg2rad(double degree)
    {
        return degree / 180 * Math.PI;
    }

    // 将弧度转换为角度
    static double rad2deg(double radian)
    {
        return radian * 180 / Math.PI;
    }

    /**
     * 四舍五入
     *
     * @param value
     * @param scale 保留小数位数
     * @return
     */
    public static double roundHalfUp(double value, int scale)
    {
        if (scale < 0)
        {
            scale = 0;
        }
        BigDecimal bdValue = new BigDecimal(value);
        BigDecimal oneValue = new BigDecimal(1);
        return bdValue.divide(oneValue, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 乘法
     *
     * @param value1
     * @param value2
     * @param scale  保留小数位
     * @return
     */
    public static double multiply(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.multiply(b2).doubleValue();
        result = roundHalfUp(result, scale);
        return result;
    }

    /**
     *
     * @param value1
     * @param value2
     * @param scale 保留位数
     * @param mode 小数的保留模式
     * @return
     */
    public static double multiply(double value1,double value2,int scale,int mode) {

        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.multiply(b2).setScale(scale,BigDecimal.ROUND_FLOOR).doubleValue();
        return result;
    }

    /**
     * 加法
     *
     * @param value1
     * @param value2
     * @param scale  保留小数位
     * @return
     */
    public static double add(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.add(b2).doubleValue();
        result = roundHalfUp(result, scale);
        return result;
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @param scale  保留小数位
     * @return
     */
    public static double subtract(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.subtract(b2).doubleValue();
        result = roundHalfUp(result, scale);
        return result;
    }

    /**
     * 除法
     *
     * @param value1
     * @param value2
     * @param scale  保留小数位
     * @return
     */
    public static double divide(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

}
