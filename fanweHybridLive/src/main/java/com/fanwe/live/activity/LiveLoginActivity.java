package com.fanwe.live.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.common.CommonOpenLoginSDK;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.EJsWxBackInfo;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.RedPointModel;
import com.fanwe.hybrid.utils.RedPointUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.blocker.SDBlocker;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.LoginApi;
import com.fanwe.live.common.OnLoginListener;
import com.fanwe.live.common.UserInfo;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.httputils.HttpHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.xutils.http.cookie.DbCookieStore;
import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveLoginActivity extends BaseActivity {

    private final static String PLAT_NAME_FACEBOOK = "Facebook";
    private final static String PLAT_NAME_TWITTER = "Twitter";
    private final static String PLAT_NAME_INSTAGRAM = "Instagram";
    private final static String PLAT_NAME_LINE = "Line";

    @ViewInject(R.id.ll_weixin)
    private LinearLayout ll_weixin;
    @ViewInject(R.id.iv_weixin)
    private ImageView iv_weixin;

    @ViewInject(R.id.ll_qq)
    private LinearLayout ll_qq;
    @ViewInject(R.id.iv_qq)
    private ImageView iv_qq;

    @ViewInject(R.id.iv_xinlang)
    private ImageView iv_xinlang;
    @ViewInject(R.id.ll_xinlang)
    private LinearLayout ll_xinlang;

    @ViewInject(R.id.iv_shouji)
    private ImageView iv_shouji;
    @ViewInject(R.id.ll_shouji)
    private LinearLayout ll_shouji;

    @ViewInject(R.id.iv_facebook)
    private ImageView iv_facebook;
    @ViewInject(R.id.iv_twitter)
    private ImageView iv_twitter;
    @ViewInject(R.id.iv_instagram)
    private ImageView iv_instagram;
    @ViewInject(R.id.iv_line)
    private ImageView iv_line;
    @ViewInject(R.id.iv_vkontakte)
    private ImageView iv_vkontakte;
    @ViewInject(R.id.btn_naver)
    private OAuthLoginButton btn_naver;

    @ViewInject(R.id.tv_agreement)
    private TextView tv_agreement;

    @ViewInject(R.id.sv)
    private SurfaceView mSurfaceView;


    @ViewInject(R.id.iv_close_app)
    private View iv_close_app;

    @ViewInject(R.id.tv_forget_pwd)
    private View tv_forget_pwd;

    @ViewInject(R.id.tv_quick_register)
    private View tv_quick_register;

    @ViewInject(R.id.btn_login)
    private View btn_login;

    @ViewInject(R.id.et_user_name)
    private EditText et_user_name;

    @ViewInject(R.id.et_pass_word)
    private EditText et_pass_word;

    private MediaPlayer mMediaPlayer;

    private SDBlocker blocker = new SDBlocker(2000);

    private static final String mVideoPath = "VID_20161031_142732.mp4";

    private static OAuthLogin mOAuthLoginInstance;
    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIsExitApp = true;
        setFullScreen();
        setContentView(R.layout.act_live_login);
        init();
    }

    private void init() {
        register();
        bindDefaultData();
        initLoginIcon();
        //        initMedia();
        initNaver();
        initKakao();
    }

    private void initKakao() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    private void initNaver() {
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.init(this, getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret), getString(R.string.naver_client_name));

        btn_naver.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    private void initMedia() {
        mMediaPlayer = new MediaPlayer();
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置视频流类型
        try {
            AssetFileDescriptor descriptor = getAssets().openFd(mVideoPath);
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    private void register() {
        iv_qq.setOnClickListener(this);
        iv_xinlang.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        iv_shouji.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);

        iv_facebook.setOnClickListener(this);
        iv_twitter.setOnClickListener(this);
        iv_instagram.setOnClickListener(this);
        iv_line.setOnClickListener(this);
        iv_vkontakte.setOnClickListener(this);
        btn_naver.setOnClickListener(this);

        iv_close_app.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        tv_quick_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void bindDefaultData() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_titile = initActModel.getPrivacy_title();
            SDViewBinder.setTextView(tv_agreement, privacy_titile);
        }
    }

    private void initLoginIcon() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            int has_wx_login = model.getHas_wx_login();
            if (has_wx_login == 1) {
                SDViewUtil.show(ll_weixin);
            } else {
                SDViewUtil.hide(ll_weixin);
            }
            int has_qq_login = model.getHas_qq_login();
            if (has_qq_login == 1) {
                SDViewUtil.show(ll_qq);
            } else {
                SDViewUtil.hide(ll_qq);
            }
            int has_sina_login = model.getHas_sina_login();
            if (has_sina_login == 1) {
                SDViewUtil.show(ll_xinlang);
            } else {
                SDViewUtil.hide(ll_xinlang);
            }
            int has_mobile_login = model.getHas_mobile_login();
            if (has_mobile_login == 1) {
                SDViewUtil.show(ll_shouji);
            } else {
                SDViewUtil.hide(ll_shouji);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (blocker.block()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_weixin:
                clickIvWeiXing();
                break;
            case R.id.iv_qq:
                clickIvQQ();
                break;
            case R.id.iv_xinlang:
                clickIvSina();
                break;
            case R.id.iv_shouji:
                clickIvShouJi();
                break;
            case R.id.tv_agreement:
                clickTvAgreement();
                break;
            case R.id.iv_facebook:
                loginForeignPlat(PLAT_NAME_FACEBOOK);
                break;
            case R.id.iv_twitter:
                loginForeignPlat(PLAT_NAME_TWITTER);
                break;
            case R.id.iv_instagram:
                loginForeignPlat(PLAT_NAME_INSTAGRAM);
                break;
            case R.id.iv_line:
                loginForeignPlat(PLAT_NAME_LINE);
                break;
            case R.id.iv_vkontakte:
                clickIvVkontakte();
                break;
            case R.id.btn_naver:
                clickBtnNaver();
                break;
            case R.id.iv_close_app:
                onClickCloseApp();
                break;
            case R.id.tv_forget_pwd:
                onClickForget();
                break;
            case R.id.tv_quick_register:
                onClickQuickRegister();
                break;
            case R.id.btn_login:
                onClickLogin();
                break;
        }
    }


    public void onClickCloseApp() {
        finish();
    }

    public void onClickForget() {
        //防止切换非全屏页面时出现抖动, 放在onPause()里面效果也不太好
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        SDActivityUtil.startActivity(this, LiveSetPasswordActivity.class);
    }

    public void onClickQuickRegister() {
        //防止切换非全屏页面时出现抖动
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        SDActivityUtil.startActivity(this, LiveRegisterActivity.class);
    }

    public void onClickLogin() {
        String userName = et_user_name.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            SDToast.showToast("用户名不能为空！");
            return;
        }
        String pwd = et_pass_word.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            SDToast.showToast("密码不能为空！");
            return;
        } else if (pwd.length() < 8) {
            SDToast.showToast("密码不能少于8位！");
            return;
        }
        SDToast.showToast("用户名：" + userName + ",密码:" + pwd + ",登陆!");
    }


    private void loginForeignPlat(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            public boolean onRegister(UserInfo info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(this);
    }

    private void clickIvVkontakte() {
        VKSdk.login(this, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS, VKScope.MESSAGES, VKScope.DOCS);
    }

    private void clickBtnNaver() {
        mOAuthLoginInstance.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    private void clickTvAgreement() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_link = initActModel.getPrivacy_link();
            if (!TextUtils.isEmpty(privacy_link)) {
                Intent intent = new Intent(LiveLoginActivity.this, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, privacy_link);
                intent.putExtra(AppWebViewActivity.EXTRA_IS_SCALE_TO_SHOW_ALL, false);
                intent.putExtra(AppWebViewActivity.EXTRA_TITLE, "用户隐私政策界面");
                startActivity(intent);
            }
        }
    }

    private void clickIvWeiXing() {
        CommonOpenLoginSDK.loginWx(this);
    }

    private void clickIvQQ() {
        CommonOpenLoginSDK.umQQlogin(this, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                SDToast.showToast(getString(R.string.auth_success));
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                requestQQ(openid, access_token);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                SDToast.showToast(getString(R.string.auth_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                SDToast.showToast(getString(R.string.auth_cancel));
            }
        });
    }

    private void clickIvSina() {
        CommonOpenLoginSDK.umSinalogin(this, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                SDToast.showToast(getString(R.string.auth_success));
                String access_token = data.get("access_token");
                String uid = data.get("uid");
                requestSinaLogin(access_token, uid);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                SDToast.showToast(getString(R.string.auth_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                SDToast.showToast(getString(R.string.auth_cancel));
            }
        });
    }

    private void clickIvShouJi() {
        Intent intent = new Intent(this, LiveMobielRegisterActivity.class);
        startActivity(intent);
    }

    public void onEventMainThread(EStartContextComplete event) {
        if (!LiveUtils.isResultOk(event.result)) {
            LogUtil.e("启动sdk失败:" + event.result);
            Intent intent = new Intent(this, LiveMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            AppRuntimeWorker.joinRecommendRoom(LiveLoginActivity.this);
        }
    }

    public void onEventMainThread(final EJsWxBackInfo event) {
        String json = event.json;
        if (!TextUtils.isEmpty(json)) {
            requestWeiXinLogin(json);
        }
    }

    private void requestWeiXinLogin(String json) {
        String code = JSON.parseObject(json).getString("code");
        if (!TextUtils.isEmpty(code)) {
            CommonInterface.requestWxLogin(code, new AppRequestCallback<App_do_updateActModel>() {
                @Override
                protected void onStart() {
                    super.onStart();
                    iv_weixin.setClickable(false);
                    iv_qq.setClickable(false);
                    iv_xinlang.setClickable(false);
                    iv_shouji.setClickable(false);
                }

                @Override
                protected void onFinish(SDResponse resp) {
                    super.onFinish(resp);
                    iv_weixin.setClickable(true);
                    iv_qq.setClickable(true);
                    iv_xinlang.setClickable(true);
                    iv_shouji.setClickable(true);
                }

                @Override
                protected void onSuccess(SDResponse resp) {
                    handLoginSuccess(actModel, "WEIXIN");
                }
            });
        }
    }

    private void requestQQ(String openid, String access_token) {
        CommonInterface.requestQqLogin(openid, access_token, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                iv_weixin.setClickable(false);
                iv_qq.setClickable(false);
                iv_xinlang.setClickable(false);
                iv_shouji.setClickable(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                iv_weixin.setClickable(true);
                iv_qq.setClickable(true);
                iv_xinlang.setClickable(true);
                iv_shouji.setClickable(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                handLoginSuccess(actModel, "QQ");
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                String msg = resp == null ? "登录遇到未知错误" : resp.getResult();
                SDToast.showToast(msg);
            }
        });
    }

    private void requestSinaLogin(String access_token, String uid) {
        CommonInterface.requestSinaLogin(access_token, uid, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                iv_weixin.setClickable(false);
                iv_qq.setClickable(false);
                iv_xinlang.setClickable(false);
                iv_shouji.setClickable(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                iv_weixin.setClickable(true);
                iv_qq.setClickable(true);
                iv_xinlang.setClickable(true);
                iv_shouji.setClickable(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                handLoginSuccess(actModel, "SINA");
            }
        });
    }

    private void startMainActivity(App_do_updateActModel actModel) {
        UserModel user = actModel.getUser();
        if (user != null) {
            if (UserModel.dealLoginSuccess(user, true)) {
                if (AppRuntimeWorker.hasRecommendRoom()) {
                    AppRuntimeWorker.startContext();
                } else {
                    Intent intent = new Intent(LiveLoginActivity.this, LiveMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                SDToast.showToast(getString(R.string.save_user_info_fail));
            }
        } else {
            SDToast.showToast(getString(R.string.get_user_info_fail));
        }
    }

    /*登录成功接收事件*/
    public void onEventMainThread(EUserLoginSuccess event) {
        if (!AppRuntimeWorker.hasRecommendRoom()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //友盟dialog有bug，Activity销毁时候ConfigDialog设置未空
        Config.dialog = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        Session.getCurrentSession().removeCallback(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登陆界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登陆界面");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                requestVKLogin(res.userId, res.accessToken);
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestVKLogin(String userId, String accessToken) {
        CommonInterface.requestVkLogin(userId, accessToken, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                handLoginSuccess(actModel, "VK");
            }
        });
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(LiveLoginActivity.this);
                requestNaverLogin(accessToken);
            } else {
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(LiveLoginActivity.this);
                Toast.makeText(LiveLoginActivity.this, errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void requestNaverLogin(String accessToken) {
        CommonInterface.requestNaverLogin(accessToken, new AppRequestCallback<App_do_updateActModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    handLoginSuccess(actModel, "NAVER");
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String accessToken = Session.getCurrentSession().getAccessToken();
            requestKakaoLogin(accessToken);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void requestKakaoLogin(String accessToken) {
        CommonInterface.requestKakaoLogin(accessToken, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    handLoginSuccess(actModel, "KAKAO");
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    // 登陆成功之后的跳转方法
    private void handLoginSuccess(App_do_updateActModel actModel, String platform) {
        if (actModel.getStatus() == 1) {
            startMainActivity(actModel);
            if (actModel.getUser() != null) {
                MobclickAgent.onProfileSignIn(platform, actModel.getUser().getUserId());
            }
            CommonInterface.requestLoginStatistic(null);
            CommonInterface.requestRedPoint(new AppRequestCallback<RedPointModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    RedPointUtil.postRedPointEvent(actModel);
                }
            });
            // 将malatv的cookie设置到一生一世的网络请求实现框架中
            // 将登陆成功之后的cookie从malatv的请求实现体中拿出来，置入retrofit中（艹，让我这一顿找，
            // 断点把手都要打断了，验证可以直接去看HttpRequest的sendRequest方法142行）
            List<HttpCookie> cookies = DbCookieStore.INSTANCE.getCookies();
            setUser2YSYS(actModel.getUser());
            setCookie2YSYS(cookies);
            // 将cookie存到本地数据库
            saveCookie();
        }
    }

    private void saveCookie() {
        List<HttpCookie> cookies = DbCookieStore.INSTANCE.getCookies();
        UserModelDao.insertOrUpdateCookie(cookies);
    }

    public static void setUser2YSYS(UserModel actModel) {
        com.qy.ysys.yishengyishi.model.item.UserInfo ysysUser = new com.qy.ysys.yishengyishi.model.item.UserInfo();
        ysysUser.setId(Integer.parseInt(actModel.getUserId()));
        ysysUser.setName(actModel.getNickName());
        AppImpl.setCurrentUser(ysysUser);
    }

    public static void setCookie2YSYS(List<HttpCookie> cookies) {
        try {
            if (cookies != null && cookies.size() > 0) {
                HttpUrl httpUrl = HttpUrl.parse(cookies.get(0).getDomain());
                Cookie.Builder builder = new Cookie.Builder();
                List<Cookie> list = new ArrayList<>();
                for (HttpCookie hc : cookies) {
                    builder.domain(hc.getDomain());
                    builder.expiresAt(Long.MAX_VALUE);
                    builder.name(hc.getName());
                    builder.value(hc.getValue());
                    builder.path(hc.getPath());
                    Cookie cookie = builder.build();
                    list.add(cookie);
                }
                HttpHelper.getInstant().cookieJar.saveFromResponse(httpUrl, list);
            }
        } catch (Exception e) {
            LogUtil.e("设置ysys cookie失败！");
        }
    }
}
