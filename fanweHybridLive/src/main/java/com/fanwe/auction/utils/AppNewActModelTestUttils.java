package com.fanwe.auction.utils;

import com.fanwe.auction.model.App_pai_user_goods_detailActModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataPaiListItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class AppNewActModelTestUttils
{
    public static App_pai_user_goods_detailActModel newActApp_pai_user_goods_detailActModel()
    {
        PaiUserGoodsDetailDataInfoModel info = new PaiUserGoodsDetailDataInfoModel();
        info.setId(1);
        info.setPai_left_time(3600);
        info.setLast_pai_diamonds(10000);
        info.setName("标题标题标题");
        info.setQp_diamonds("1000");
        info.setJj_diamonds("1000");
        info.setBz_diamonds(1000);
        info.setPai_yanshi("5");
        info.setMax_yanshi("5");
        info.setDate_time("0000-00-00 00:00");
        info.setPlace("福建福州方维软件");

        ArrayList<String> imgs=new ArrayList<String>();
        imgs.add("http://image.qiankeep.com/public/attachment/201608/15/13/57b158edab468.jpg");
        imgs.add("http://image.qiankeep.com/public/attachment/201608/15/13/57b158edab468.jpg");
        info.setImgs(imgs);

        PaiUserGoodsDetailDataModel data = new PaiUserGoodsDetailDataModel();
        data.setInfo(info);

        List<PaiUserGoodsDetailDataPaiListItemModel> list = new ArrayList<PaiUserGoodsDetailDataPaiListItemModel>();
        for (int i = 0; i < 3; ++i)
        {
            PaiUserGoodsDetailDataPaiListItemModel item = new PaiUserGoodsDetailDataPaiListItemModel();
            item.setPai_date("00-00 00:00:00");
            item.setUser_name("yhz");
            item.setPai_diamonds("1000");
            list.add(item);
        }
        data.setPai_list(list);

        App_pai_user_goods_detailActModel app_pai_user_goods_detailActModel = new App_pai_user_goods_detailActModel();
        app_pai_user_goods_detailActModel.setData(data);
        return app_pai_user_goods_detailActModel;
    }
}
