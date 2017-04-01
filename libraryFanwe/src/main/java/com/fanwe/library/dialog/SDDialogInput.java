package com.fanwe.library.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 带标题，输入框，确定按钮和取消按钮的窗口
 *
 * @author js02
 */
public class SDDialogInput extends SDDialogCustom
{

    public EditText et_content;

    private SDDialogInputListener listener;

    public SDDialogInput setListener(SDDialogInputListener listener)
    {
        this.listener = listener;
        return this;
    }

    public SDDialogInput(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void init()
    {
        super.init();
        setmListener(new SDDialogCustomListener()
        {

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
                if (listener != null)
                {
                    listener.onDismiss(SDDialogInput.this);
                }
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                if (listener != null)
                {
                    listener.onClickConfirm(v, et_content.getText().toString(), SDDialogInput.this);
                }
            }

            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
                if (listener != null)
                {
                    listener.onClickCancel(v, SDDialogInput.this);
                }
            }
        });

        setCustomView(R.layout.dialog_input);
        et_content = (EditText) findViewById(R.id.dialog_input_et_content);

        Drawable backgroundDrawable = new SDDrawable().color(Color.WHITE).strokeWidthAll(SDViewUtil.dp2px(1)).cornerAll(config.getCornerRadius());
        et_content.setBackgroundDrawable(backgroundDrawable);
    }

    public SDDialogInput setTextContent(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            et_content.setText("");
        } else
        {
            et_content.setText(text);
        }
        return this;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SDViewUtil.showInputMethod(et_content, 200);
    }

    @Override
    protected void onStop()
    {
        SDViewUtil.hideInputMethod(et_content);
        super.onStop();
    }

    public interface SDDialogInputListener
    {
        void onClickCancel(View v, SDDialogInput dialog);

        void onClickConfirm(View v, String content, SDDialogInput dialog);

        void onDismiss(SDDialogInput dialog);
    }

}
