package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.SettingsSecurityActModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by shibx on 2016/7/12.
 */
public class LiveAccountCenterActivity extends BaseTitleActivity
{
    @ViewInject(R.id.tv_band_mobilephone)
    private TextView tv_band_mobilephone;
//
//    @ViewInject(R.id.iv_account_safe)
//    private ImageView iv_account_safe;

//    @ViewInject(R.id.tv_safe_grade)
//    private TextView tv_safe_grade;

    @ViewInject(R.id.ll_account_bind_mobilephone)
    private LinearLayout ll_account_bind_mobilephone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_account_center);
        init();
    }

    private void init()
    {
        initTitle();
    }

    @Override
    protected void onResume()
    {
        requestData();
        super.onResume();
        MobclickAgent.onPageStart("账号与安全界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("账号与安全界面");
        MobclickAgent.onPause(this);
    }

    private void requestData()
    {
        CommonInterface.requestAccountAndSafe(new AppRequestCallback<SettingsSecurityActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.isOk())
                {
                    bindData(actModel);
                }
            }
        });
    }

    private void bindData(final SettingsSecurityActModel model)
    {
        if (1 == model.getIs_security())
        {
//            setViewAttribute(model.getMobile(), R.drawable.account_safe_guard, "安全等级：高");
            tv_band_mobilephone.setTextColor(getResources().getColor(R.color.gray));
            tv_band_mobilephone.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_rect_live_msg_normal));
            tv_band_mobilephone.setText(R.string.unbind);
            ll_account_bind_mobilephone.setOnClickListener(null);
        } else
        {
            tv_band_mobilephone.setTextColor(getResources().getColor(R.color.white));
            tv_band_mobilephone.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_rect_live_msg_selected));
//            tv_band_mobilephone.setBackground(getResources().getDrawable(R.id.));
            tv_band_mobilephone.setText(R.string.bind);
//            setViewAttribute("未绑定", R.drawable.account_safe_guard_dark, "安全等级：低");
            ll_account_bind_mobilephone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(LiveAccountCenterActivity.this, LiveMobileBindActivity.class));
                }
            });
        }
    }

    private void setViewAttribute(CharSequence content, int resId, CharSequence grade)
    {
//        SDViewBinder.setTextView(tv_band_mobilephone, content);
//        iv_account_safe.setImageResource(resId);
//        SDViewBinder.setTextView(tv_safe_grade, grade);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.account_security));
        mTitle.setOnClickListener(this);
    }

}
