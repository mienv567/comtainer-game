package com.fanwe.library.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 带环形进度条，和信息提示的窗口
 *
 * @author js02
 */
public class SDDialogProgress extends SDDialogBase
{

    private TextView tv_msg;
    private ProgressBar pb_progress;

    public SDDialogProgress setTextMsg(String msg)
    {
        if (TextUtils.isEmpty(msg))
        {
            SDViewUtil.hide(tv_msg);
        } else
        {
            SDViewUtil.show(tv_msg);
            tv_msg.setText(msg);
        }
        return this;
    }

    public SDDialogProgress(Activity activity)
    {
        super(activity);
        init();
    }

    public SDDialogProgress()
    {
        super();
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_progress, SDViewUtil.getLayoutParamsViewGroupWW());
        tv_msg = (TextView) findViewById(R.id.dialog_progress_tv_msg);
        pb_progress = (ProgressBar) findViewById(R.id.dialog_progress_pb_progress);

        pb_progress.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.rotate_progress_white));

        SDDrawable drawable = new SDDrawable().color(Color.parseColor("#55000000")).cornerAll(SDViewUtil.dp2px(5));
        SDViewUtil.setBackgroundDrawable(getContentView(), drawable);

        setCancelable(true);
    }
}
