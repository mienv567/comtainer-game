package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * 家族主页
 * Created by Administrator on 2016/9/26.
 */

public class App_family_indexActModel extends BaseActModel
{
    private App_family_indexItemActModel family_info;

    public App_family_indexItemActModel getFamily_info() {
        return family_info;
    }

    public void setFamily_info(App_family_indexItemActModel family_info) {
        this.family_info = family_info;
    }
}
