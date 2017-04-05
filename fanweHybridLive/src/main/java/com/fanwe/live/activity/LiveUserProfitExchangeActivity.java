package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDNumberUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveUserProfitExchangeAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ExchangeRuleActModel;
import com.fanwe.live.model.App_doExchangeActModel;
import com.fanwe.live.model.ExchangeModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2016/7/19.
 */
public class LiveUserProfitExchangeActivity extends BaseTitleActivity implements TextWatcher{

    @ViewInject(R.id.tv_useable_ticket)
    private TextView tv_useable_ticket;
    @ViewInject(R.id.lv_exchange_rule)
    private ListView lv_exchange_rule;
    @ViewInject(R.id.tv_exchange_rate)
    private TextView tv_exchange_rate;
    @ViewInject(R.id.et_money)
    private EditText et_money;
    @ViewInject(R.id.tv_money_to_diamonds)
    private TextView tv_money_to_diamonds;
    @ViewInject(R.id.tv_unit_and_equals)
    private TextView tv_unit_and_equals;
    @ViewInject(R.id.tv_do_exchange)
    private TextView tv_do_exchange;
    @ViewInject(R.id.ll_other_ticket_exchange)
    private LinearLayout ll_other_ticket_exchange;

    private List<ExchangeModel> mList;
    private LiveUserProfitExchangeAdapter mAdapter;

    private int mTicket;
    private static final int ANOTHER_TICKET_ID = 0;

    private double rate;//其他钱票 兑换 比例
    private int min_ticket;//其他钱票的兑换下限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_profit_exchange);
        init();
    }

    private void init() {
        initTitle();
        mList = new ArrayList<>();
        et_money.addTextChangedListener(this);
        et_money.setHint(getString(R.string.input)+AppRuntimeWorker.getTicketName()+getString(R.string.num));
        tv_do_exchange.setOnClickListener(this);
        mAdapter = new LiveUserProfitExchangeAdapter(mList,this);
        lv_exchange_rule.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new SDAdapter.ItemClickListener<ExchangeModel>() {
            @Override
            public void onClick(int position, ExchangeModel item, View view) {
                if(! (mTicket < item.getTicket())) {
                    requestDoExchange(item.getId(), item.getTicket());
                } else {
                    SDToast.showToast(AppRuntimeWorker.getTicketName() + getString(R.string.not_enough));
                }
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.conversion));
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestExchageRule();
        MobclickAgent.onPageStart("兑换界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("兑换界面");
        MobclickAgent.onPause(this);
    }

    private void requestDoExchange(int id, int ticket) {
        CommonInterface.requestDoExchange(id, ticket, new AppRequestCallback<App_doExchangeActModel>() {

            @Override
            protected void onStart() {
                showProgressDialog(getString(R.string.is_conversion));
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if(rootModel.isOk()) {
                    mTicket = actModel.getUseable_ticket();
                    tv_useable_ticket.setText(String.valueOf(actModel.getUseable_ticket()));

                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                dismissProgressDialog();
            }
        });
    }
    private void requestExchageRule() {
        CommonInterface.requestExchangeRule(new AppRequestCallback<App_ExchangeRuleActModel>() {
            @Override
            protected void onStart() {
                showProgressDialog(getString(R.string.getting)+"...");
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if(rootModel.isOk()) {
                    mTicket = actModel.getUseable_ticket();
                    tv_useable_ticket.setText(String.valueOf(actModel.getUseable_ticket()));
                    mList = actModel.getExchange_rules();
                    mAdapter.updateData(mList);
                    if(!TextUtils.isEmpty(actModel.getRatio()) && Double.valueOf(actModel.getRatio()) != 0) {
                        rate = Double.valueOf(actModel.getRatio());
                        tv_exchange_rate.setText(getString(R.string.conversion_rate)+"："+ actModel.getRatio() + "("+getString(R.string.conversion_result_only_get_int)+")");
                    } else {
                        tv_exchange_rate.setVisibility(View.GONE);
                        ll_other_ticket_exchange.setVisibility(View.GONE);
                    }

                    min_ticket = actModel.getMin_ticket();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_do_exchange :
                int ticket = Integer.valueOf(et_money.getText().toString());
                requestDoExchange(ANOTHER_TICKET_ID,ticket);
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String result = et_money.getText().toString();
        if(!TextUtils.isEmpty(result)) {
            SDViewUtil.show(tv_unit_and_equals);
            tv_money_to_diamonds.setText(getDiamondsByRate(result));
            if(Integer.parseInt(result) < min_ticket) {
                // enable = false;
                SDViewUtil.setTextViewColorResId(tv_do_exchange,R.color.text_gray);
                tv_do_exchange.setEnabled(false);
            }else {
                //enable = true;
                SDViewUtil.setTextViewColorResId(tv_do_exchange,R.color.main_color);
                tv_do_exchange.setEnabled(true);
            }
        } else {
            SDViewUtil.invisible(tv_unit_and_equals);
            tv_money_to_diamonds.setText("0");
        }
    }

    private String getDiamondsByRate(String ticketStr) {
        double ticket = Double.valueOf(ticketStr);
        int diamonds = (int) (SDNumberUtil.multiply(ticket,rate,0,BigDecimal.ROUND_FLOOR));
        return String.valueOf(diamonds);
    }
}
