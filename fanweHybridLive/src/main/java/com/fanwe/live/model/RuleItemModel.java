package com.fanwe.live.model;

import com.fanwe.live.utils.SDFormatUtil;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-7 上午11:45:38 类说明
 */
public class RuleItemModel
{
    private int id;
    private String name;
    private double money;
    private long diamonds;

    // add
    private String moneyFormat;
    private boolean isSelected;

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public String getMoneyFormat()
    {
        return moneyFormat;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.moneyFormat = SDFormatUtil.formatMoneyChina(money);
        this.money = money;
    }

    public long getDiamonds()
    {
        return diamonds;
    }

    public void setDiamonds(long diamonds)
    {
        this.diamonds = diamonds;
    }

}
