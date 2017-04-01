package com.fanwe.hybrid.common;

import android.app.Activity;
import android.app.Dialog;

import com.fanwe.hybrid.app.App;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-3-21 上午10:40:36 类说明
 */
public class CommonOpenLoginSDK
{
    private static UMShareAPI shareAPI;

    public static UMShareAPI getShareAPI()
    {
        return shareAPI;
    }

    /**
     * 点击微信登录，先获取个人资料
     */
    public static void loginWx(final Activity activity)
    {
        SDDialogProgress dialog = new SDDialogProgress(activity);
        dialog.setTextMsg(activity.getString(R.string.start_weixin));
        umLogin(activity, dialog, SHARE_MEDIA.WEIXIN, new UMAuthListener()
        {

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data)
            {
                SDToast.showToast(activity.getString(R.string.auth_success));
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t)
            {
                SDToast.showToast(activity.getString(R.string.auth_fail));
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action)
            {
                SDToast.showToast(activity.getString(R.string.auth_cancel));
            }
        });
    }

    public static void umSinalogin(Activity activity, UMAuthListener listener)
    {
        SDDialogProgress dialog = new SDDialogProgress(activity);
        dialog.setTextMsg(activity.getString(R.string.start_sina));
        umLogin(activity, dialog, SHARE_MEDIA.SINA, listener);
    }

    public static void umQQlogin(Activity activity, UMAuthListener listener)
    {
        SDDialogProgress dialog = new SDDialogProgress(activity);
        dialog.setTextMsg(activity.getString(R.string.start_QQ));
        umLogin(activity, dialog, SHARE_MEDIA.QQ, listener);
    }

    public static void umLogin(Activity activity, Dialog dialog, SHARE_MEDIA platform, UMAuthListener listener)
    {
        if (activity == null || listener == null || platform == null)
        {
            return;
        }

        if (shareAPI == null)
        {
            shareAPI = UMShareAPI.get(App.getApplication().getApplicationContext());
        }

        if (!shareAPI.isInstall(activity, platform))
        {
            if (platform == SHARE_MEDIA.SINA)
            {
                SDToast.showToast(activity.getString(R.string.not_install_sina));
            } else if (platform == SHARE_MEDIA.WEIXIN)
            {
                SDToast.showToast(activity.getString(R.string.not_install_weixin));
            } else if (platform == SHARE_MEDIA.QQ)
            {
                SDToast.showToast(activity.getString(R.string.not_install_QQ));
            }
            return;
        }

        Config.dialog = dialog;
        shareAPI.doOauthVerify(activity, platform, listener);
    }
}
