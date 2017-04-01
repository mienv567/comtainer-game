package com.fanwe.shopping.model.custommsg;

import com.fanwe.live.LiveConstant;
import com.fanwe.shopping.model.ShopGoodsDetailModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsg;

/**
 * Created by Administrator on 2016/9/20.
 */
public class CustomMsgShopPush extends CustomMsg
{
    private int room_id; //房间ID
    private int post_id;// 主播ID
    private String desc;//消息
    private UserModel user;
    private ShopGoodsDetailModel goods;

    public CustomMsgShopPush()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_SHOP_GOODS_PUSH);
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ShopGoodsDetailModel getGoods() {
        return goods;
    }

    public void setGoods(ShopGoodsDetailModel goods) {
        this.goods = goods;
    }
}
