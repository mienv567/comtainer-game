package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-11 下午5:18:00 类说明
 */
@SuppressWarnings("serial")
public class App_user_homeActModel extends BaseActModel
{

    private List<UserModel> cuser_list;//贡献前3名

    public List<UserModel> getCuser_list()
    {
        return cuser_list;
    }

    public void setCuser_list(List<UserModel> cuser_list)
    {
        this.cuser_list = cuser_list;
    }

    private UserModel user;

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

}
