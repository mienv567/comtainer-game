package com.fanwe.shopping.common;

import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.shopping.model.App_shop_goodsActModel;
import com.fanwe.shopping.model.App_shop_mystoreActModel;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ShoppingCommonInterface
{
    public static AppRequestParams getAppRequestParams()
    {
        AppRequestParams params = new AppRequestParams();
        params.put("itype", "sdk");
        return params;
    }
    /**
     * 我的小店
     *
     * @param user_id   主播用户ID
     * @param listener
     */
    public static void requestShopMystore(int user_id, AppRequestCallback<App_shop_mystoreActModel> listener)
    {
        AppRequestParams params = getAppRequestParams();
        params.putCtl("shop");
        params.putAct("mystore");
        params.put("podcast_user_id", user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 新增购物商品
     *
     * @param user_id        主播用户ID
     * @param name              商品名称
     * @param imgs              图片（JSON数据）
     * @param price             商品价钱
     * @param url               商品详情URL地址
     * @param description       商品描述
     * @param kd_cost           快递费用
     * @param listener
     */
    public static void requestShopAddGoods(String user_id,String name,String imgs,float price,float kd_cost,String url,String description, AppRequestCallback<App_shop_goodsActModel> listener)
    {
        AppRequestParams params = getAppRequestParams();
        params.putCtl("shop");
        params.putAct("add_goods");
        params.put("podcast_id", user_id);
        params.put("name", name);
        params.put("imgs", imgs);
        params.put("price", price);
        params.put("url", url);
        params.put("description", description);
        params.put("kd_cost", kd_cost);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 修改购物商品
     *
     * @param id        	     ID
     * @param user_id        主播用户ID
     * @param name              商品名称
     * @param imgs              图片（JSON数据）
     * @param price             商品价钱
     * @param url               商品详情URL地址
     * @param description       商品描述
     * @param kd_cost           快递费用
     * @param listener
     */
    public static void requestShopEditGoods(String id,String user_id,String name,String imgs,float price,float kd_cost,String url,String description, AppRequestCallback<App_shop_goodsActModel> listener)
    {
        AppRequestParams params = getAppRequestParams();
        params.putCtl("shop");
        params.putAct("edit_goods");
        params.put("id", id);
        params.put("podcast_id", user_id);
        params.put("name", name);
        params.put("imgs", imgs);
        params.put("price", price);
        params.put("url", url);
        params.put("description", description);
        params.put("kd_cost", kd_cost);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除购物商品
     *
     * @param id        	     ID
     * @param listener
     */
    public static void requestShopDelGoods(int id, AppRequestCallback<BaseActModel> listener)
    {
        AppRequestParams params = getAppRequestParams();
        params.putCtl("shop");
        params.putAct("del_goods");
        params.put("id", id);
        AppHttpUtil.getInstance().post(params, listener);
    }
}
