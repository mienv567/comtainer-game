package com.fanwe.live.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.RedPointModel;
import com.fanwe.hybrid.utils.RedPointUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDSendValidateButton;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.model.App_do_loginActModel;
import com.fanwe.live.model.App_is_user_verifyActModel;
import com.fanwe.live.model.App_send_mobile_verifyActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveMobielRegisterActivity extends BaseTitleActivity {

    @ViewInject(R.id.ll_image_code)
    private LinearLayout ll_image_code;
    @ViewInject(R.id.et_image_code)
    private EditText et_image_code;
    @ViewInject(R.id.iv_image_code)
    private ImageView iv_image_code;

    @ViewInject(R.id.et_mobile)
    private EditText et_mobile;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.btn_send_code)
    private SDSendValidateButton btn_send_code;

    @ViewInject(R.id.tv_login)
    private TextView tv_login;

    private App_do_loginActModel app_do_loginActModel;
    private String strMobile;
    private String strImageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_mobile_register);
        init();
    }

    private void init() {
        register();
        initTitle();
        reqeustIsUserVerify();
        initSDSendValidateButton();
    }

    private void register() {
        tv_login.setOnClickListener(this);
        iv_image_code.setOnClickListener(this);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.mobile_register));
    }


    private void initSDSendValidateButton() {
        btn_send_code.setmListener(new SDSendValidateButton.SDSendValidateButtonListener() {
            @Override
            public void onTick() {
            }

            @Override
            public void onClickSendValidateButton() {
                requestSendCode();
            }
        });
    }

    private void reqeustIsUserVerify() {
        CommonInterface.requestIsUserVerify(new AppRequestCallback<App_is_user_verifyActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    SDViewUtil.show(ll_image_code);
                    SDViewBinder.setImageView(LiveMobielRegisterActivity.this, actModel.getVerify_url(), iv_image_code);
                } else {
                    SDViewUtil.hide(ll_image_code);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }


    private void requestSendCode() {
        strMobile = et_mobile.getText().toString();
        strImageCode = et_image_code.getText().toString();

        if (TextUtils.isEmpty(strMobile)) {
            SDToast.showToast(getString(R.string.please_input_phone_number));
            return;
        }
        if (ll_image_code.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(strImageCode)) {
                SDToast.showToast(getString(R.string.please_input_pic_ver_code));
                return;
            }
        }


        CommonInterface.requestSendMobileVerify(0, strMobile, strImageCode, new AppRequestCallback<App_send_mobile_verifyActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    btn_send_code.setmDisableTime(actModel.getTime());
                    btn_send_code.startTickWork();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_image_code:
                reqeustIsUserVerify();
                break;
            case R.id.tv_login:
                clickTvLogin();
                break;
        }
    }

    private void clickTvLogin() {
        strMobile = et_mobile.getText().toString();
        if (TextUtils.isEmpty(strMobile)) {
            SDToast.showToast(getString(R.string.please_input_phone_number));
            return;
        }
        String code = et_code.getText().toString();
        if (TextUtils.isEmpty(code)) {
            SDToast.showToast(getString(R.string.please_input_ver_code));
            return;
        }

        CommonInterface.requestDoLogin(strMobile, code, new AppRequestCallback<App_do_loginActModel>() {
            @Override
            public void onStart() {
                showProgressDialog(getString(R.string.is_logining));
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    dealSuccess(actModel);
                    if (actModel.getUser() != null) {
                        MobclickAgent.onProfileSignIn("MOBILE", actModel.getUser().getUserId());
                    }
                    CommonInterface.requestLoginStatistic(null);
                    CommonInterface.requestRedPoint(new AppRequestCallback<RedPointModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (rootModel.getStatus() == 1) {
                                RedPointUtil.postRedPointEvent(actModel);
                            }
                        }
                    });
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    private void dealSuccess(App_do_loginActModel actModel) {
        UserModel user = actModel.getUser();
        if (user != null) {
            if (actModel.getIsLack() == 1) {
                Intent intent = new Intent(this, LiveDoUpdateActivity.class);
                intent.putExtra(LiveDoUpdateActivity.EXTRA_USER_MODEL, (Serializable) user);
                startActivity(intent);
            } else {
                if (UserModel.dealLoginSuccess(user, true)) {
                    if (AppRuntimeWorker.hasRecommendRoom()) {
                        AppRuntimeWorker.startContext();
                    } else {
                        Intent intent = new Intent(LiveMobielRegisterActivity.this, LiveMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    SDToast.showToast(getString(R.string.save_user_info_fail));
                }
            }
        } else {
            SDToast.showToast(getString(R.string.get_user_info_fail));
        }
    }

    public void onEventMainThread(EStartContextComplete event) {
        if (!LiveUtils.isResultOk(event.result)) {
            LogUtil.e("启动sdk失败:" + event.result);
            Intent intent = new Intent(this, LiveMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            AppRuntimeWorker.joinRecommendRoom(LiveMobielRegisterActivity.this);
        }
    }

    /*登录成功接收事件*/
    public void onEventMainThread(EUserLoginSuccess event) {
        if (!AppRuntimeWorker.hasRecommendRoom()) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("手机注册界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("手机注册界面");
        MobclickAgent.onPause(this);
    }
}
