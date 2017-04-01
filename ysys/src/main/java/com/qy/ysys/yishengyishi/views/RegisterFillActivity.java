package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.AppConfig;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.ModelFitInfo;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFillActivity extends BaseActivity {

    private TextView tv_next;
    private EditText et_username;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFeMale;
    private int sex = 0;
    private String phoneNum;

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_register_fill;
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phonenumber");
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        et_username = (EditText) findViewById(R.id.et_username);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFeMale = (RadioButton) findViewById(R.id.radioFemale);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioMale.getId()) {
                    sex = 0;
                } else if (checkedId == radioFeMale.getId()) {
                    sex = 1;
                }
                SPUtils.setParam("sex", sex);
            }
        });

        tv_next = (TextView) findViewById(R.id.tv_nextsetup);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                if(TextUtils.isEmpty(username)){
                    ToastUtil.showToast("请填入你的姓名");
                    et_username.requestFocus();
                    return;
                }
                RequestInterface.fillInfo(username,sex, phoneNum,new Callback<ModelFitInfo>() {
                    @Override
                    public void onResponse(Call<ModelFitInfo> call, Response<ModelFitInfo> response) {
                        Log.i("onResponse", response.body().getCode());
                        SPUtils.setParam(AppConfig.USERINFO, et_username.getText());
                        Intent intent = new Intent(RegisterFillActivity.this, FingdingRelativeActivity.class);
                        startActivity(intent);
                        RegisterFillActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<ModelFitInfo> call, Throwable t) {
                        Log.e("onFailure", t.getMessage().toString());
                    }

                });

//                // 调试
//                SPUtils.setParam(AppConfig.USERINFO, et_username.getText());
//                SPUtils.setParam("uid",16);
//                Intent intent = new Intent(RegisterFillActivity.this, FingdingRelativeActivity.class);
//                startActivity(intent);
//                RegisterFillActivity.this.finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
