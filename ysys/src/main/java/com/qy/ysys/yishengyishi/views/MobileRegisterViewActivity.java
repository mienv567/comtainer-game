package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.AppConfig;
import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.ModelLogin;
import com.qy.ysys.yishengyishi.model.ModelSendMsg;
import com.qy.ysys.yishengyishi.presenters.RegisterHelper;
import com.qy.ysys.yishengyishi.presenters.viewinterface.IRegisterView;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileRegisterViewActivity extends BaseActivity implements IRegisterView {

    private TextView tv_login;
    private RegisterHelper registerHelper;
    private TextView tv_send;
    private EditText et_phone;
    private EditText et_verifycode;

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);
        registerHelper = new RegisterHelper(this, this);
//        int uid = (int) SPUtils.getParam("uid", 0);
//
//        if (uid != 0) {
//            Intent intent = new Intent(MobileRegisterViewActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_mobiel_register;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tv_login = (TextView) contentView.findViewById(R.id.tv_loginin);
        tv_login.setOnClickListener(this);

        tv_send = (TextView) contentView.findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);

        et_phone = (EditText) contentView.findViewById(R.id.et_phone);
        et_verifycode = (EditText) contentView.findViewById(R.id.et_verifycode);
    }

    public void clickTvAgreement(View view) {
        clickTvAgreement();
    }

    @Override
    public void clickTvAgreement() {
        registerHelper.clickTvAgreement();
    }

    @Override
    public void loginin() {
//        registerHelper.loginIn();

//        // 方便调试
//        SPUtils.setParam("uid",16);
//        SPUtils.setParam("sex",0);
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
//        finish();

        final String photeNum = et_phone.getText().toString();
        String verifyCode = et_verifycode.getText().toString();

        if (TextUtils.isEmpty(photeNum) || TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast("手机号或者验证码不能为空!");
            return;
        }
        RequestInterface.requestMobileLogin(photeNum, verifyCode, new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                Log.i("onResponse", response.body().getCode());
                if(response.body().getReturnObj() == null){
                    ToastUtil.showToast("响应内容为空，请联系管理员");
                    return;
                }
                SPUtils.getParam(AppConfig.LOGINTOKE, response.body().getReturnObj().getPhone());
                int uid = response.body().getReturnObj().getId();
                SPUtils.setParam("uid", uid);
                AppImpl.setCurrentUser(response.body().getReturnObj());
                if (TextUtils.isEmpty(response.body().getReturnObj().getName())) {
                    Intent intent = new Intent(MobileRegisterViewActivity.this, RegisterFillActivity.class);
                    intent.putExtra("phonenumber", photeNum);
                    MobileRegisterViewActivity.this.startActivity(intent);
                    MobileRegisterViewActivity.this.finish();
                } else {
                    startActivity(new Intent(MobileRegisterViewActivity.this, MainActivity.class));
                }

            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerHelper.onDestory();
    }

    @Override
    public void onClick(View v) {
        if (v == tv_login) {
            loginin();
        }
        if (v == tv_send) {
            String photoNum = et_phone.getText().toString();
            if (TextUtils.isEmpty(photoNum)) {
                et_phone.requestFocus();
                ToastUtil.showToast("请填写手机号");
                return;
            }
//            tv_send.setClickable(false);
            et_verifycode.setFocusable(true);
            et_verifycode.requestFocus();
            RequestInterface.sendPhoneMsg(photoNum, "1", new Callback<ModelSendMsg>() {
                @Override
                public void onResponse(Call<ModelSendMsg> call, Response<ModelSendMsg> response) {
                    ModelSendMsg body = response.body();
                    Log.d("onResponse", body.getCode());
                    tv_send.setBackgroundColor(Color.parseColor("#44000000"));
                    ToastUtil.showToast("短信发送成功,请注意查收");
                }

                @Override
                public void onFailure(Call<ModelSendMsg> call, Throwable t) {
                    Log.d("onResponse", t.getMessage());
                }
            });
        }
    }
}
