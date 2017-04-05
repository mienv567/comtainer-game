package com.fanwe.hybrid.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.CommonSharePTag;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.RedPointModel;
import com.fanwe.hybrid.umeng.UmengSocialManager;
import com.fanwe.hybrid.utils.RedPointUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.model.Login_test_loginActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.AIDUtil;
import com.fanwe.live.utils.LiveUtils;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-16 下午4:39:42 类说明 启动页
 */
public class InitActivity extends BaseActivity {
    @ViewInject(R.id.rl_root_layout)
    private RelativeLayout rl_root_layout;
    @ViewInject(R.id.iv_splash_second)
    private ImageView iv_splash_second;
    int init_delayed_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.act_init);
        init();
    }

    private void init() {
        UmengSocialManager.init(getApplication());
        SDTencentMapManager.getInstance().startLocation(new TencentLocationListener() {

            @Override
            public void onStatusUpdate(String arg0, int arg1, String arg2) {
            }

            @Override
            public void onLocationChanged(TencentLocation arg0, int arg1, String arg2) {
            }
        });

        final String aid = AIDUtil.getAID(this);
        if (!TextUtils.isEmpty(aid)) {
            if (!AIDUtil.isAIdExist(aid)) {
                CommonInterface.requestActiveStatistic(new AppRequestCallback<BaseActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        AIDUtil.saveAId(aid);
                    }
                });
            }
        }

        int init_delayed_time = getResources().getInteger(R.integer.init_delayed_time);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestInit();
            }
        }, 500);
    }

    private void requestInit() {
        CommonInterface.requestInit(new AppRequestCallback<InitActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (!InitActModelDao.insertOrUpdate(actModel)) {
                    SDToast.showToast(getString(R.string.save_init_fail));
                }

                startLiveMain();
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                startLiveMain();
            }
        });
    }

    private void startLiveMain() {
        //启动本地广告图
        int is_first_open_app = SDConfig.getInstance().getInt(CommonSharePTag.IS_FIRST_OPEN_APP, 0);
        boolean is_open_adv = getResources().getBoolean(R.bool.is_open_adv);
        if (is_first_open_app != 1 && is_open_adv) {
            ArrayList<String> array = new ArrayList<String>();
            Resources res = getResources();
            String[] adv_img_array = res.getStringArray(R.array.adv_img_array);
            for (int i = 0; i < adv_img_array.length; i++) {
                array.add(adv_img_array[i]);
            }
            startInitAdvList(array);
            return;
        }
        //增加网络广告闪屏的展示
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null && !TextUtils.isEmpty(initActModel.getAd_img())) {
            Glide.with(this).load(initActModel.getAd_img()).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    iv_splash_second.setVisibility(View.VISIBLE);
                    iv_splash_second.setBackgroundDrawable(resource);
                    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv_splash_second, "alpha", 0f, 1f);
                    alphaAnimator.setDuration(800).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startLiveMainActivity();
                        }
                    }, 2000);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    startLiveMainActivity();
                }
            });
            return;
        }
        startLiveMainActivity();
    }


    private void startLiveMainActivity() {
        boolean is_open_webview_main = getResources().getBoolean(R.bool.is_open_webview_main);
        if (is_open_webview_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        UserModel user = UserModelDao.query();
        if (user != null) {
            List<HttpCookie> cookies = UserModelDao.queryCookie();
            LiveLoginActivity.setCookie2YSYS(cookies);
            LiveLoginActivity.setUser2YSYS(user);
            CommonInterface.requestLoginStatistic(null);
            CommonInterface.requestRedPoint(new AppRequestCallback<RedPointModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (rootModel.getStatus() == 1) {
                        RedPointUtil.postRedPointEvent(actModel);
                    }
                }
            });
            if (AppRuntimeWorker.hasRecommendRoom()) {
                AppRuntimeWorker.startContext();
            } else {
                Intent intent = new Intent(this, LiveMainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (ApkConstant.AUTO_REGISTER) {
                CommonInterface.requestTestLogin(new AppRequestCallback<Login_test_loginActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        if (rootModel.isOk()) {
                            Intent intent = new Intent(InitActivity.this, LiveMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else {
                Intent intent = new Intent(this, LiveLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void onEventMainThread(EStartContextComplete event) {
        if (!LiveUtils.isResultOk(event.result)) {
            LogUtil.e("启动sdk失败:" + event.result);
            Intent intent = new Intent(this, LiveMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            AppRuntimeWorker.joinRecommendRoom(InitActivity.this);
        }
    }

    private void startInitAdvList(ArrayList<String> array) {
        Intent intent = new Intent(InitActivity.this, InitAdvListActivity.class);
        intent.putStringArrayListExtra(InitAdvListActivity.EXTRA_ARRAY, array);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("闪屏界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("闪屏界面");
        MobclickAgent.onPause(this);
    }
}
