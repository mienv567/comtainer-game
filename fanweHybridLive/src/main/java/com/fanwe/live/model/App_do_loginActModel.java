package com.fanwe.live.model;


import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by Administrator on 2016/7/6.
 */
public class App_do_loginActModel extends BaseActModel
{
    private UserModel user;
    private int isLack;//是否缺失信息

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

    public int getIsLack()
    {
        return isLack;
    }

    public void setIsLack(int isLack)
    {
        this.isLack = isLack;
    }

}
