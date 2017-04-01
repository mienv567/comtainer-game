package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

public class GoodsHistoryListModel extends BaseActModel {

    private List<GoodsHistoryModel> historys;

    public List<GoodsHistoryModel> getHistorys() {
        return historys;
    }

    public void setHistorys(List<GoodsHistoryModel> historys) {
        this.historys = historys;
    }

    @Override
    public String toString() {
        return "GoodsHistoryListModel{" +
                "historys=" + historys +
                '}';
    }
}
