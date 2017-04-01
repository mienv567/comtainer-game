package com.fanwe.live.activity;

import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;
import com.qy.ysys.yishengyishi.views.customviews.FragFamily;

/**
 * Created by kevin.liu on 2017/3/17.
 */
public class FamilyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_family);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new FragFamily()).commit();
    }

}
