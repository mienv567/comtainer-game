package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by cheng.yuan on 2017/3/16.
 *
 */

public class GoodsListModel extends BaseActModel {

    private List<GoodsTypeModel> types;

    public List<GoodsTypeModel> getTypes() {
        return types;
    }

    public void setTypes(List<GoodsTypeModel> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoodsListModel{" +
                "types=" + types +
                '}';
    }
}
