package com.fanwe.shopping.model;

import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.shopping.model.ShopGoodsDetailModel;


/**
 * Created by Administrator on 2016/8/23.
 */
public class App_shop_goodsActModel extends BaseActModel
{
    private ShopGoodsDetailModel info;

    public ShopGoodsDetailModel getData()
    {
        return info;
    }

    public void setData(ShopGoodsDetailModel info)
    {
        this.info = info;
    }

}
