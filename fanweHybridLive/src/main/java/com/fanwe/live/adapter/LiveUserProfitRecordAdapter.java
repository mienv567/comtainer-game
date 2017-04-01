package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.GainRecordModel;

import java.util.List;

/**
 * Created by shibx on 2016/7/19.
 */
public class LiveUserProfitRecordAdapter extends SDSimpleAdapter<GainRecordModel>{

    public LiveUserProfitRecordAdapter(List<GainRecordModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_live_profit_record;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, GainRecordModel model) {
        TextView tv_money = get(R.id.tv_money,convertView);
        TextView tv_time = get(R.id.tv_time,convertView);
        TextView tv_state = get(R.id.tv_state,convertView);
        int is_pay = model.getIs_pay();
        tv_money.setText(model.getMoney() + SDResourcesUtil.getString(R.string.money_unit));
        tv_state.setText(getRecordState(is_pay));
        tv_time.setText(getRecordTime(is_pay,model));
    }

    private String getRecordState(int is_pay) {
        if(is_pay ==3) {
            return SDResourcesUtil.getString(R.string.withdraw_success);
        }
        return SDResourcesUtil.getString(R.string.withdrawing);
    }

    private String getRecordTime(int is_pay,GainRecordModel model) {
        if(is_pay ==3) {
            return model.getPay_time();
        }
        return model.getCreate_time();
    }
}
