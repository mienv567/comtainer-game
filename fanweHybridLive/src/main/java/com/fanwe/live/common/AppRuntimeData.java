package com.fanwe.live.common;

import com.fanwe.live.model.UserModel;

/**
 * 用于保存app运行期间需要频繁用到的数据。
 * 此类的setter只应该在数据持久化到本地和初始化Application的时候调用赋值，其他地方不应该调用setter方法赋值
 * 更新此类的某个数据后应该将数据持久化到本地，比如数据库操作或者SharePreference等
 */
public class AppRuntimeData
{
    private static AppRuntimeData instance;

    //data
    private UserModel user;

    public static AppRuntimeData getInstance()
    {
        if (instance == null)
        {
            instance = new AppRuntimeData();
        }
        return instance;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }
}
