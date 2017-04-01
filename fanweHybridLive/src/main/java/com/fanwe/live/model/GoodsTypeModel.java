package com.fanwe.live.model;

import java.util.List;

/**
 * Created by cheng.yuan on 2017/3/16.
 */

public class GoodsTypeModel extends BaseGoodsModel {

    /**
     * id : 3
     * name :
     * goods : [{"id":16,"title":"stevesb5","description":"stevesb5stevesb5stevesb5stevesb5","icon":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","iconSmall":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","charmCount":500,"likesCount":600},{"id":16,"title":"stevesb5","description":"stevesb5stevesb5stevesb5stevesb5","icon":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","iconSmall":"http://image.qg8game.com/malaLive/boxZone/68/20170314/8b67073f2f084e4d9de191524fe112b9.JPG","charmCount":500,"likesCount":600}]
     */

    private int typeId;
    private String name;
    private List<GoodsDetailModel> goods;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsDetailModel> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsDetailModel> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "GoodsListModel{" +
                "typeId=" + typeId +
                ", name='" + name + '\'' +
                ", goods=" + goods +
                '}';
    }
}
