package com.fanwe.live.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserSubmitSignModel;
import com.fanwe.hybrid.service.AppUpgradeService;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.MenuMeView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.UserSignAwardDialog;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.ELiveMainOpenLeft;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.fragment.BoxBarFragment;
import com.fanwe.live.fragment.LiveChatRoomFragment;
import com.fanwe.live.fragment.LiveGuideFragment;
import com.fanwe.live.fragment.LiveMainFragment;
import com.fanwe.live.fragment.LiveMineFragment;
import com.fanwe.live.fragment.LiveTabLiveFragment;
import com.fanwe.live.fragment.LiveTabMeFragment;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;
import com.fanwe.live.utils.LiveVideoChecker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

public class LiveMainActivity extends BaseActivity {

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer_layout;
    @ViewInject(R.id.iv_bg_sign)
    private ImageView iv_bg_sign;
    @ViewInject(R.id.v_bg_sign)
    private View v_bg_sign;

    @ViewInject(R.id.rg_tab_activity_main)
    private RadioGroup rg_tab_activity_main;

    @ViewInject(R.id.rb_home)
    private RadioButton rb_home;
    @ViewInject(R.id.rb_guide)
    private RadioButton rb_guide;
    @ViewInject(R.id.rb_chat)
    private RadioButton rb_chat;
    @ViewInject(R.id.rb_box_bar)
    private RadioButton rb_box_bar;
    @ViewInject(R.id.rb_mine)
    private RadioButton rb_mine;
    private View.OnClickListener clickListener;


    //    @ViewInject(R.id.menu_me)
    private MenuMeView menu_me;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTrasaction;
    private LiveMainFragment mMainFragment;
    private SlidingMenu mMenu;
    private BoxBarFragment mLiveFragFamily;
    private LiveGuideFragment mGuideFragment;
    private LiveChatRoomFragment mChatRoomFragment;
    private LiveMineFragment mMineFragment;

    @Override
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mIsExitApp = true;
        checkUpdate();
        checkSign();
        AppRuntimeWorker.startContext();
        CommonInterface.requestUser_apns(null);
        CommonInterface.requestMyUserInfoJava(null);

        LiveVideoChecker checker = new LiveVideoChecker(this);
        CharSequence copyContent = SDOtherUtil.pasteText();
        checker.check(String.valueOf(copyContent));
        initFragment();
        initMenu();
        initListener();
    }

    private void initListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckChange(v.getId());
            }
        };
        rb_home.setOnClickListener(clickListener);
        rb_guide.setOnClickListener(clickListener);
        rb_chat.setOnClickListener(clickListener);
        rb_box_bar.setOnClickListener(clickListener);
        rb_mine.setOnClickListener(clickListener);
    }


    private void doCheckChange(int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                if (mMainFragment == null) {
                    mMainFragment = new LiveMainFragment();
                }
                transact(mMainFragment);
                break;
            case R.id.rb_box_bar:
                if (mLiveFragFamily == null) {
                    mLiveFragFamily = new BoxBarFragment();
                }
                transact(mLiveFragFamily);
                break;
            case R.id.rb_mine:
                //onEventMainThread(new ELiveMainOpenLeft());
                if (mMineFragment == null) {
                    mMineFragment = new LiveMineFragment();
                }
                transact(mMineFragment);
                break;
            case R.id.rb_chat:
                if (mChatRoomFragment == null) {
                    mChatRoomFragment = new LiveChatRoomFragment();
                }
                transact(mChatRoomFragment);
                break;
            case R.id.rb_guide:
                if (mGuideFragment == null) {
                    mGuideFragment = new LiveGuideFragment();
                }
                transact(mGuideFragment);
                break;
            default:
                SDToast.showToast("哈哈哈哈哈");
        }
    }

    private void checkSign() {
        CommonInterface.requestAppSign(new AppRequestCallback<UserSubmitSignModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    UserSignAwardDialog dialog = new UserSignAwardDialog(LiveMainActivity.this, actModel);
                    dialog.show();
                    SDViewUtil.show(iv_bg_sign);
                    SDViewUtil.show(v_bg_sign);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            SDViewUtil.hide(iv_bg_sign);
                            SDViewUtil.hide(v_bg_sign);
                        }
                    });
                }
            }
        });
    }

    private void initMenu() {
        menu_me = new MenuMeView(this);
        // configure the SlidingMenu
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        // 设置滑动菜单视图的宽度
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
        mMenu.setOffsetFadeDegree(0.4f);
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        mMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        mMenu.setMenu(menu_me);
    }

    private void request() {
        CommonInterface.requestMyUserInfoJava(new AppRequestCallback<UserModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    UserModel user = actModel;
//                    mMainFragment.setUserModel(user);
                    menu_me.setUserModel(user);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mMainFragment = new LiveMainFragment();
        transact(mMainFragment);
    }

    private void transact(Fragment fragment) {
        if (fragment != null) {
            mFragmentTrasaction = mFragmentManager.beginTransaction();
            mFragmentTrasaction.replace(R.id.fl_main_content, fragment);
            mFragmentTrasaction.commit();
        }
    }

    public void onEventMainThread(EStartContextComplete event) {
        if (!LiveUtils.isResultOk(event.result)) {
            LogUtil.e("启动sdk失败:" + event.result);
        }
    }

    private void checkUpdate() {
        Intent updateIntent = new Intent(this, AppUpgradeService.class);
        startService(updateIntent);
    }


    protected void clickTabLive() {
        getSDFragmentManager().toggle(R.id.fl_main_content, null, LiveTabLiveFragment.class);
    }

    protected void clickTabMe() {
        getSDFragmentManager().toggle(R.id.fl_main_content, null, LiveTabMeFragment.class);
    }

    private void clickTabCreateLive() {
        if (AppRuntimeWorker.isLogin(this)) {
            final UserModel userModel = UserModelDao.query();
            if (userModel.getIsAgree() == 1) {
                Intent intent = new Intent(this, LiveCreateRoomActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LiveCreaterAgreementActivity.class);
                startActivity(intent);
            }
        }
    }

    public void onEventMainThread(ELiveMainOpenLeft event) {
        if (mMenu != null) {
            mMenu.showMenu();
        }
    }

    /**
     * 异地登录
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event) {
        SDDialogConfirm dialog = new SDDialogConfirm(this);
        dialog.setTextContent(getString(R.string.account_login_other_mobile));
        dialog.setTextCancel(getString(R.string.exit));
        dialog.setTextConfirm(getString(R.string.relogin));
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

            @Override
            public void onDismiss(SDDialogCustom dialog) {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog) {
                AppRuntimeWorker.startContext();
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog) {
                App.getApplication().logout(true);
            }
        });
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        request();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }
}
