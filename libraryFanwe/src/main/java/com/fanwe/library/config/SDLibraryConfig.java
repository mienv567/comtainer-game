package com.fanwe.library.config;

import android.graphics.Color;

public class SDLibraryConfig
{

    private int mainColor;
    private int mainColorPress;

    private int grayPressColor;

    private int strokeColor;
    private int strokeWidth;

    private int cornerRadius;

    private int titleHeight;
    private int titleColor;
    private int titleColorPressed;
    private int titleTextColor;

    public SDLibraryConfig()
    {
        setStrokeWidth(1);
        setCornerRadius(10);
        setGrayPressColor(Color.parseColor("#E5E5E5"));
        setStrokeColor(Color.parseColor("#E5E5E5"));
        setMainColor(Color.parseColor("#FC7507"));
        setMainColorPress(Color.parseColor("#FFCC66"));
        setTitleHeight(80);
        setTitleColor(Color.parseColor("#FC7507"));
        setTitleColorPressed(Color.parseColor("#FFCC66"));
        setTitleTextColor(Color.parseColor("#FFFFFF"));
    }

    public int getTitleTextColor()
    {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor)
    {
        this.titleTextColor = titleTextColor;
    }

    public int getTitleColorPressed()
    {
        return titleColorPressed;
    }

    public void setTitleColorPressed(int titleColorPressed)
    {
        this.titleColorPressed = titleColorPressed;
    }

    public int getTitleColor()
    {
        return titleColor;
    }

    public void setTitleColor(int titleColor)
    {
        this.titleColor = titleColor;
    }

    public int getTitleHeight()
    {
        return titleHeight;
    }

    public void setTitleHeight(int titleHeight)
    {
        this.titleHeight = titleHeight;
    }

    public int getCornerRadius()
    {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius)
    {
        this.cornerRadius = cornerRadius;
    }

    public int getStrokeColor()
    {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor)
    {
        this.strokeColor = strokeColor;
    }

    public int getGrayPressColor()
    {
        return grayPressColor;
    }

    public void setGrayPressColor(int grayPressColor)
    {
        this.grayPressColor = grayPressColor;
    }

    public int getStrokeWidth()
    {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth)
    {
        this.strokeWidth = strokeWidth;
    }

    public int getMainColorPress()
    {
        return mainColorPress;
    }

    public void setMainColorPress(int mainColorPress)
    {
        this.mainColorPress = mainColorPress;
    }

    public int getMainColor()
    {
        return mainColor;
    }

    public void setMainColor(int mainColor)
    {
        this.mainColor = mainColor;
    }

}
