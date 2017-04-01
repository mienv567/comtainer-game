package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class App_AuthentActModel extends BaseActModel
{
    private String title;
    private String investor_send_info;

    private UserModel user;

    private List<App_AuthentItemModel> authent_list;

    public List<App_AuthentItemModel> getAuthent_list() {
        return authent_list;
    }

    public void setAuthent_list(List<App_AuthentItemModel> authent_list) {
        this.authent_list = authent_list;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

    public String getInvestor_send_info()
    {
        return investor_send_info;
    }

    public void setInvestor_send_info(String investor_send_info)
    {
        this.investor_send_info = investor_send_info;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
