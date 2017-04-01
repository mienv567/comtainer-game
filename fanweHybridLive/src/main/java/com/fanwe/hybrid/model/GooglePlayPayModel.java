package com.fanwe.hybrid.model;

/**
 * Created by kevin.liu on 2017/3/10.
 */

public class GooglePlayPayModel {
    /**
     * 商品在google play后台管理页面上配置的
     */
    private String sku;
    /**
     * 用来做验证的参数，由我们决定，传给Google play APP，在返回结果中会原封不动的返给我们
     */
    private String payLoad;

    /*
     *  调用的google play的API版本，此处写死为3
     */
    public int getApiVersion() {
        return 3;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    /*
     * 商品类型，写死为托管的装备类型（非订阅类型）
     */
    public String getType() {
        return "inapp";
    }
}
