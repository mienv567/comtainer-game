package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.webview.CustomWebView;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by L on 2016/7/9.
 */
public class LiveCreaterAgreementActivity extends BaseTitleActivity {

    private CustomWebView webView;
    private TextView tv_agree;

    private String mTopic;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_creater_agreement;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        webView = find(R.id.webview);
        tv_agree = find(R.id.tv_agree);

        mTitle.setMiddleTextTop(getString(R.string.anchor_agreement));
        getExtraData(getIntent());
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel == null) {
            SDToast.showToast(getString(R.string.not_find_init_params));
            finish();
            return;
        }

        final String url = initActModel.getAgreement_link();
        if (TextUtils.isEmpty(url)) {
            SDToast.showToast(getString(R.string.anchor_agreement_address_empty));
            finish();
            return;
        }

        webView.get(url);

        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAgree();
            }
        });
    }

    private void getExtraData(Intent extraIntent) {
        Bundle bundle = extraIntent.getExtras();
        if (bundle != null) {
            mTopic = bundle.getString(LiveCreateRoomActivity.EXTRA_TITLE);
        }
    }

    private void clickAgree() {
        CommonInterface.requestAgree(new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    UserModel user = UserModelDao.query();
                    if (user != null) {
                        user.setIsAgree(1);
                        UserModelDao.insertOrUpdate(user);

                        Intent intent = new Intent(LiveCreaterAgreementActivity.this, LiveCreateRoomActivity.class);
                        intent.putExtra(LiveCreateRoomActivity.EXTRA_TITLE, mTopic);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("开播协议界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("开播协议界面");
        MobclickAgent.onPause(this);
    }
}
