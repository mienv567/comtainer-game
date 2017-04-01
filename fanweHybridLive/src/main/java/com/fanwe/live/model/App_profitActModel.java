package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by shibx on 2016/7/19.
 */
public class App_profitActModel extends BaseActModel{

    private String ticket;//钱票总数
    private String useable_ticket;//可兑换钱票
    private String money;//可提现人名币
    private int subscribe;//是否关注公众号
    private int binding_wx;//是否绑定微信
    private int mobile_exist;//是否绑定手机
    private String subscription;//微信公众号名称

    private int show_pai_ticket;//是否显示拍卖收入
    private String pai_ticket;//拍卖总收入
    private String pai_wait_ticket;//拍卖收入-待结算
    private int show_goods_ticket;//是否显示销售收入
    private String goods_ticket;//销售总收入
    private String goods_wait_ticket;//销售收入-待结算
    private String month_return;//当月收益 （人民币）
    private String total_return;//总收益 （人民币）
    public int getShow_pai_ticket() {
        return show_pai_ticket;
    }

    public void setShow_pai_ticket(int show_pai_ticket) {
        this.show_pai_ticket = show_pai_ticket;
    }

    public String getPai_ticket() {
        return pai_ticket;
    }

    public void setPai_ticket(String pai_ticket) {
        this.pai_ticket = pai_ticket;
    }

    public String getPai_wait_ticket() {
        return pai_wait_ticket;
    }

    public void setPai_wait_ticket(String pai_wait_ticket) {
        this.pai_wait_ticket = pai_wait_ticket;
    }

    public int getShow_goods_ticket() {
        return show_goods_ticket;
    }

    public void setShow_goods_ticket(int show_goods_ticket) {
        this.show_goods_ticket = show_goods_ticket;
    }

    public String getGoods_ticket() {
        return goods_ticket;
    }

    public void setGoods_ticket(String goods_ticket) {
        this.goods_ticket = goods_ticket;
    }

    public String getGoods_wait_ticket() {
        return goods_wait_ticket;
    }

    public void setGoods_wait_ticket(String goods_wait_ticket) {
        this.goods_wait_ticket = goods_wait_ticket;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getUseable_ticket() {
        return useable_ticket;
    }

    public void setUseable_ticket(String useable_ticket) {
        this.useable_ticket = useable_ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getBinding_wx() {
        return binding_wx;
    }

    public void setBinding_wx(int binding_wx) {
        this.binding_wx = binding_wx;
    }

    public int getMobile_exist() {
        return mobile_exist;
    }

    public void setMobile_exist(int mobile_exist) {
        this.mobile_exist = mobile_exist;
    }

    public String getMonth_return() {
        return month_return;
    }

    public void setMonth_return(String month_return) {
        this.month_return = month_return;
    }

    public String getTotal_return() {
        return total_return;
    }

    public void setTotal_return(String total_return) {
        this.total_return = total_return;
    }
}
