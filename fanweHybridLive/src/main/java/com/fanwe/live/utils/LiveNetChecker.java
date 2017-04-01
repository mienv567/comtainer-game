package com.fanwe.live.utils;

import android.app.Activity;
import android.view.View;

import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.listener.SDNetStateListener;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.dialog.LiveNetTipDialog;

/**
 * Created by L on 2016/8/28.
 */
public class LiveNetChecker
{
    public void check(final Activity activity, final CheckResultListener listener)
    {
        SDOtherUtil.checkNet(activity, new SDNetStateListener()
        {
            @Override
            public void onWifi()
            {
                if (listener != null)
                {
                    listener.onAccepted();
                }
            }

            @Override
            public void onNone()
            {
                SDToast.showToast("无网络");
            }

            @Override
            public void onMobile()
            {
                new LiveNetTipDialog(activity).setmListener(new SDDialogCustom.SDDialogCustomListener()
                {
                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog)
                    {
                        if (listener != null)
                        {
                            listener.onRejected();
                        }
                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog)
                    {
                        if (listener != null)
                        {
                            listener.onAccepted();
                        }
                    }

                    @Override
                    public void onDismiss(SDDialogCustom dialog)
                    {
                    }
                }).show();
            }
        });
    }


    public interface CheckResultListener
    {
        void onAccepted();

        void onRejected();
    }

}
