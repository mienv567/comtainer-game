package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

public class UserCenterAuthentTransferActivity extends BaseTitleActivity {
    @ViewInject(R.id.ll_transfer)
    private LinearLayout ll_transfer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center_authent_transfer);
        initTitle();
        ll_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCenterAuthentTransferActivity.this, UserCenterAuthentActivity.class);
                startActivity(intent);
                UserCenterAuthentTransferActivity.this.finish();
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.mala_auth));
    }
}
