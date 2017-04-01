package com.fanwe.hybrid.umeng;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.live.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

import java.util.ArrayList;

public class UmengSocialManager {
    public static final String SHARE = "com.umeng.share";
    public static final String LOGIN = "com.umeng.login";

    public static void init(Application app) {
        InitActModel model = InitActModelDao.query();
        String wxAppKey = null;
        String wxAppSecret = null;

        String qqAppKey = null;
        String qqAppSecret = null;

        String sinaAppKey = null;
        String sinaAppSecret = null;

        if (model != null) {
            if (!TextUtils.isEmpty(model.getWx_app_key()) && !TextUtils.isEmpty(model.getWx_app_secret())) {
                wxAppKey = model.getWx_app_key();
                wxAppSecret = model.getWx_app_secret();
            } else {
                wxAppKey = app.getResources().getString(R.string.wx_app_id);
                wxAppSecret = app.getResources().getString(R.string.wx_app_secret);
            }

            if (!TextUtils.isEmpty(model.getQq_app_key()) && !TextUtils.isEmpty(model.getQq_app_secret())) {
                qqAppKey = model.getQq_app_key();
                qqAppSecret = model.getQq_app_secret();

            } else {
                qqAppKey = app.getResources().getString(R.string.qq_app_id);
                qqAppSecret = app.getResources().getString(R.string.qq_app_key);
            }

            if (!TextUtils.isEmpty(model.getSina_app_key()) && !TextUtils.isEmpty(model.getSina_app_secret())) {
                sinaAppKey = model.getSina_app_key();
                sinaAppSecret = model.getSina_app_secret();
            } else {
                sinaAppKey = app.getResources().getString(R.string.sina_app_key);
                sinaAppSecret = app.getResources().getString(R.string.sina_app_secret);
            }
        } else {
            // /////////////////////////////////////////微信
            wxAppKey = app.getResources().getString(R.string.wx_app_id);
            wxAppSecret = app.getResources().getString(R.string.wx_app_secret);

            qqAppKey = app.getResources().getString(R.string.qq_app_id);
            qqAppSecret = app.getResources().getString(R.string.qq_app_key);

            sinaAppKey = app.getResources().getString(R.string.sina_app_key);
            sinaAppSecret = app.getResources().getString(R.string.sina_app_secret);
        }

        PlatformConfig.setWeixin(wxAppKey, wxAppSecret);
        // 微信 appid appsecret
        PlatformConfig.setQQZone(qqAppKey, qqAppSecret);
        // QQ和Qzone appid appkey
        PlatformConfig.setSinaWeibo(sinaAppKey, sinaAppSecret);
        // 新浪微博 appkey appsecret
    }

    private static SHARE_MEDIA[] getPlatform(Activity activity) {
        ArrayList<SHARE_MEDIA> list = new ArrayList<SHARE_MEDIA>();

        if (isWeixinEnable()) {
            list.add(SHARE_MEDIA.WEIXIN);
            list.add(SHARE_MEDIA.WEIXIN_CIRCLE);
        }

        if (isQQEnable()) {
            list.add(SHARE_MEDIA.QQ);
            list.add(SHARE_MEDIA.QZONE);
        }

        if (isSinaEnable()) {
            list.add(SHARE_MEDIA.SINA);
        }

        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[list.size()];
        list.toArray(displaylist);
        return displaylist;
    }

    public static boolean isWeixinEnable() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getWx_app_api() == 1;
        } else {
            return false;
        }
    }

    public static boolean isQQEnable() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getQq_app_api() == 1;
        } else {
            return false;
        }
    }

    public static boolean isSinaEnable() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSina_app_api() == 1;
        } else {
            return false;
        }
    }

    public static void shareWeixin(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        if (isWeixinEnable()) {
            shareAction(title, content, imageUrl, clickUrl, activity, listener).setPlatform(SHARE_MEDIA.WEIXIN).share();
        }
    }

    public static void shareWeixinCircle(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        if (isWeixinEnable()) {
            shareAction(title, content, imageUrl, clickUrl, activity, listener).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
        }
    }

    public static void shareQQ(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        if (isQQEnable()) {
            shareAction(title, content, imageUrl, clickUrl, activity, listener).setPlatform(SHARE_MEDIA.QQ).share();
        }
    }

    public static void shareQzone(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        if (isQQEnable()) {
            shareAction(title, content, imageUrl, clickUrl, activity, listener).setPlatform(SHARE_MEDIA.QZONE).share();
        }
    }

    public static void shareSina(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        if (isSinaEnable()) {
            shareAction(title, content, imageUrl, clickUrl, activity, listener).setPlatform(SHARE_MEDIA.SINA).share();
        }
    }

    /**
     * 打开分享面板
     */
    public static void openShare(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        shareAction(title, content, imageUrl, clickUrl, activity, listener).open();
    }

    /**
     *  新增自定义的分享面板使用的分享方法
     * @param shareMedia
     * @param title
     * @param content
     * @param imageUrl
     * @param clickUrl
     * @param activity
     * @param listener
     */
    public static void openShare(int shareMedia, String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        ShareAction share = new ShareAction(activity);

        if (TextUtils.isEmpty(title)) {
            title = activity.getString(R.string.title_is_empty);
        }
        if (TextUtils.isEmpty(content)) {
            content = activity.getString(R.string.content_is_empty);
        }
        switch (shareMedia) {
            case 0:
                share.setPlatform(SHARE_MEDIA.QQ);
                break;
            case 1:
                share.setPlatform(SHARE_MEDIA.QZONE);
                break;
            case 2:
                share.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 3:
                share.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 4:
                share.setPlatform(SHARE_MEDIA.SINA);
                break;
        }
        Object media = null;
        if (!TextUtils.isEmpty(imageUrl)) {
            media = new UMImage(activity, imageUrl);
        }
        if (media instanceof UMImage) {
            share.withMedia((UMImage) media);
        } else if (media instanceof UMusic) {
            share.withMedia((UMusic) media);
        } else if (media instanceof UMVideo) {
            share.withMedia((UMVideo) media);
        }
        share.withTitle(title).withText(content).withTargetUrl(clickUrl).setCallback(listener)
                .share();

    }

    private static ShareAction shareAction(String title, String content, String imageUrl, String clickUrl, Activity activity, UMShareListener listener) {
        SHARE_MEDIA[] displayList = getPlatform(activity);
        return shareAction(title, content, imageUrl, clickUrl, activity, listener, displayList);
    }

    private static ShareAction shareAction(String title, String content, String imageUrl, String clickUrl, Activity activity,
                                           UMShareListener listener, SHARE_MEDIA... displayList) {
        Object media = null;
        if (!TextUtils.isEmpty(imageUrl)) {
            media = new UMImage(activity, imageUrl);
        }
        return shareAction(title, content, media, clickUrl, activity, listener, displayList);
    }

    private static ShareAction shareAction(String title, String content, Object media, String clickUrl, Activity activity, UMShareListener listener,
                                           SHARE_MEDIA... displayList) {
        ShareAction share = new ShareAction(activity);

        if (TextUtils.isEmpty(title)) {
            title = activity.getString(R.string.title_is_empty);
        }
        if (TextUtils.isEmpty(content)) {
            content = activity.getString(R.string.content_is_empty);
        }

        setShowLoadDialog(activity);

        share.setDisplayList(displayList).withTitle(title).withText(content).withTargetUrl(clickUrl).setListenerList(listener);

        if (media instanceof UMImage) {
            share.withMedia((UMImage) media);
        } else if (media instanceof UMusic) {
            share.withMedia((UMusic) media);
        } else if (media instanceof UMVideo) {
            share.withMedia((UMVideo) media);
        }

        return share;
    }

    public static void setShowLoadDialog(Activity activity) {
        setShowLoadDialog(activity.getString(R.string.is_sharing), activity);
    }

    public static void setShowLoadDialog(String text, Activity activity) {
        SDDialogProgress dialog = new SDDialogProgress(activity);
        if (!TextUtils.isEmpty(text)) {
            dialog.setTextMsg(text);
        } else {
            dialog.setTextMsg(activity.getString(R.string.is_sharing));
        }
        Config.dialog = dialog;
    }

    public static String getPlatformString(SHARE_MEDIA media) {
        String result = "";
        if (media != null) {
            switch (media) {
                case WEIXIN:
                    result = "微信";
                    break;
                case WEIXIN_CIRCLE:
                    result = "朋友圈";
                    break;
                case QQ:
                    result = "QQ";
                    break;
                case SINA:
                    result = "新浪微博";
                    break;
                case QZONE:
                    result = "QQ空间";
                    break;
                default:
                    break;
            }
        }
        return result;
    }


}