package com.fanwe.shopping.model;

import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.shopping.model.ShopMystoreListItemModel;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class App_shop_mystoreActModel extends BaseActModel
{
    private List<ShopMystoreListItemModel> list;

    public List<ShopMystoreListItemModel> getList() {
        return list;
    }

    public void setList(List<ShopMystoreListItemModel> list) {
        this.list = list;
    }
}
