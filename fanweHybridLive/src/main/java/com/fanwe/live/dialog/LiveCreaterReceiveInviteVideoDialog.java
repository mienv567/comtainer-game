package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;

import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsgInviteVideo;

/**
 * 主播收到连麦邀请窗口
 */
public class LiveCreaterReceiveInviteVideoDialog extends SDDialogConfirm
{

    private CustomMsgInviteVideo msg;
    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public LiveCreaterReceiveInviteVideoDialog(Activity activity, CustomMsgInviteVideo msg)
    {
        super(activity);
        this.msg = msg;

        setTextContent(msg.getSender().getNickName() + SDResourcesUtil.getString(R.string.ask_you_connect_video)).setTextCancel(SDResourcesUtil.getString(R.string.reject)).setTextConfirm(SDResourcesUtil.getString(R.string.accept));
        setmListener(new SDDialogCustomListener()
        {
            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
                if (clickListener != null)
                {
                    clickListener.onClickReject();
                }
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                if (clickListener != null)
                {
                    clickListener.onClickAccept();
                }
            }

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {

            }
        });
    }


    @Override
    public void show()
    {
        startDismissRunnable(10 * 1000);
        super.show();
    }

    public interface ClickListener
    {
        void onClickAccept();

        void onClickReject();
    }


}
