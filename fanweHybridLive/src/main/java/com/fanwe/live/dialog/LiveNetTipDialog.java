package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;

/**
 * Created by Administrator on 2016/7/11.
 */
public class LiveNetTipDialog extends SDDialogConfirm
{
    public LiveNetTipDialog(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void init()
    {
        super.init();
        setTextContent(SDResourcesUtil.getString(R.string.current_network_is_not_wifi_if_continue)).setTextCancel(SDResourcesUtil.getString(R.string.no)).setTextConfirm(SDResourcesUtil.getString(R.string.yes)).show();
    }
}
