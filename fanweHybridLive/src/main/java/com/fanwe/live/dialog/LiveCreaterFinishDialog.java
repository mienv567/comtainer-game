package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * 主播退出时的提示
 *
 * @author js02
 */
public class LiveCreaterFinishDialog extends SDDialogCustom
{

    public TextView tv_content;

    public LiveCreaterFinishDialog(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void init()
    {
        super.init();
        paddingLeft(DEFAULT_PADDING_LEFT_RIGHT * 2).paddingRight(DEFAULT_PADDING_LEFT_RIGHT * 2);

        setCustomView(R.layout.dialog_live_creater_finish);
        tv_content = (TextView) findViewById(R.id.dialog_confirm_tv_content);
        SDViewUtil.hide(tv_title);
    }

    public LiveCreaterFinishDialog setTextGravity(int gravity)
    {
        if (tv_content.getLayoutParams() instanceof LinearLayout.LayoutParams)
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_content.getLayoutParams();
            if (params.gravity != gravity)
            {
                params.gravity = gravity;
                tv_content.setLayoutParams(params);
            }
        }
        return this;
    }

    public LiveCreaterFinishDialog setTextContent(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            tv_content.setVisibility(View.GONE);
        } else
        {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(text);
        }
        return this;
    }
}
