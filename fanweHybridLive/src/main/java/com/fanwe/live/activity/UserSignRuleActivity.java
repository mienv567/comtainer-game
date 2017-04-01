package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;


public class UserSignRuleActivity extends BaseTitleActivity {
    public static final String EXTRA_SIGN_RULE = "extra_sign_rule";
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    private String mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_rule);
        initTitle();
        initIntent();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.sign_rule));
        mTitle.setOnClickListener(this);
    }

    private void initIntent(){
        Intent intent = getIntent();
        mContent = intent.getStringExtra(EXTRA_SIGN_RULE);
        SDViewBinder.setTextView(tv_content,mContent);
    }
}
