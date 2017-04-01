package com.qy.ysys.yishengyishi.views;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.views.customviews.BaseActivity;
import com.qy.ysys.yishengyishi.views.customviews.FragChat;
import com.qy.ysys.yishengyishi.views.customviews.FragFamily;
import com.qy.ysys.yishengyishi.views.customviews.FragGenealogy;
import com.qy.ysys.yishengyishi.views.customviews.FragMe;
import com.qy.ysys.yishengyishi.views.customviews.FragMemoirs;

public class MainActivity extends BaseActivity {

    private BottomNavigationViewEx bnve;
    private ViewGroup content;

    @Override
    protected boolean shouldAddTitle() {
        return false;
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_main;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        content = (ViewGroup) findViewById(R.id.fl_content);

        bnve = (BottomNavigationViewEx) findViewById(R.id.bnv);
        bnve.enableAnimation(true);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(true);
        bnve.setIconVisibility(true);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R2.id.menu_family:
                        onTabSelect(0);
                        break;
                    case R2.id.menu_chat:
                        onTabSelect(1);
                        break;
                    case R2.id.menu_memoirs:
                        onTabSelect(2);
                        break;
                    case R2.id.menu_genealogy:
                        onTabSelect(3);
                        break;
                    case R2.id.menu_me:
                        onTabSelect(4);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
        bnve.setCurrentItem(0);
    }

    private void onTabSelect(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                transaction.replace(R.id.fl_content, new FragFamily());
                break;
            case 1:
                transaction.replace(R.id.fl_content, new FragChat());
                break;
            case 2:
                transaction.replace(R.id.fl_content, new FragMemoirs());
                break;
            case 3:
                transaction.replace(R.id.fl_content, new FragGenealogy());
                break;
            case 4:
                transaction.replace(R.id.fl_content, new FragMe());
                break;
            default:
                break;
        }
        transaction.commit();

    }

    @Override
    public void onClick(View v) {

    }
}




