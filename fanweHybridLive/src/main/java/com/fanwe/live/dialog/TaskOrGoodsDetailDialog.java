package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class TaskOrGoodsDetailDialog extends LiveBaseDialog {

    public static final int TYPE_TASK = 0X01;
    public static final int TYPE_GOODS = 0X02;

    @ViewInject(R.id.tv_dialog_title)
    TextView tv_dialog_title;
    @ViewInject(R.id.iv_close_dialog)
    ImageView iv_close_dialog;
    @ViewInject(R.id.tv_task_desc)
    TextView tv_task_desc;
    @ViewInject(R.id.tv_complete_task)
    TextView tv_complete_task;

    private OnConfirmClickListener mConfirmClickListener;

    public TaskOrGoodsDetailDialog(Activity activity) {
        super(activity);
        init();
    }

    public void setConfirmClickListener(OnConfirmClickListener confirmClickListener) {
        mConfirmClickListener = confirmClickListener;
    }

    private void init() {
        setContentView(R.layout.dialog_live_rask_detail);
        setCanceledOnTouchOutside(true);
        paddingLeft(SDViewUtil.dp2px(40));
        paddingRight(SDViewUtil.dp2px(40));
        x.view().inject(this, getContentView());
        register();
    }

    private void register() {
        iv_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_complete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onConfirm();
                }
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        tv_dialog_title.setText(title);
    }

    public void setContent(String content) {
        tv_task_desc.setText(content);
    }

    public void setConfirmString(String text) {
        tv_complete_task.setText(text);
    }

    public interface OnConfirmClickListener {
        void onConfirm();
    }
}
