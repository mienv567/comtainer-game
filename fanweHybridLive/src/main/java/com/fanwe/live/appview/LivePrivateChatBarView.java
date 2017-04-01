package com.fanwe.live.appview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.span.view.SDSpannableEdittext;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * 私聊界面底部操作栏布局
 */
public class LivePrivateChatBarView extends BaseAppView implements TextWatcher
{
    public ImageView iv_keyboard;
//    public ImageView iv_voice;

    public SDSpannableEdittext et_content;
    public TextView tv_record;

    public ImageView iv_expression_on;
    public ImageView iv_expression_off;

    public TextView tv_send;
    public ImageView iv_more;

    private boolean voiceModeEnable;

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public LivePrivateChatBarView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LivePrivateChatBarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LivePrivateChatBarView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_private_chat_bar);

        iv_keyboard = find(R.id.iv_keyboard);
//        iv_voice = find(R.id.iv_voice);
        et_content = find(R.id.et_content);
        tv_record = find(R.id.tv_record);
        iv_expression_on = find(R.id.iv_expression_on);
        iv_expression_off = find(R.id.iv_expression_off);
        tv_send = find(R.id.tv_send);
        iv_more = find(R.id.iv_more);

        iv_keyboard.setOnClickListener(this);
//        iv_voice.setOnClickListener(this);

        iv_expression_on.setOnClickListener(this);
        iv_expression_off.setOnClickListener(this);

        iv_more.setOnClickListener(this);
        tv_send.setOnClickListener(this);

        et_content.addTextChangedListener(this);
        et_content.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    et_content.append(" ");
                    return true;
                }
                return false;
            }
        });
        et_content.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                showInputMode();
                if (clickListener != null)
                {
                    return clickListener.onTouchEditText();
                }
                return false;
            }
        });

        setVoiceModeEnable(true);
    }

    public void setVoiceModeEnable(boolean voiceModeEnable)
    {
        this.voiceModeEnable = voiceModeEnable;

        showNormalMode();
        if (!voiceModeEnable)
        {
//            SDViewUtil.hide(iv_voice);
            SDViewUtil.hide(iv_keyboard);
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == iv_keyboard)
        {
            showInputMode();
            if (clickListener != null)
            {
                clickListener.onClickKeyboard();
            }
        }
//        else if (v == iv_voice)
//        {
//            showVoiceMode();
//            if (clickListener != null)
//            {
//                clickListener.onClickVoice();
//            }
//        }
        else if (v == iv_expression_off)
        {
            showExpressionMode();
            if (clickListener != null)
            {
                clickListener.onClickExpressionOff();
            }
        } else if (v == iv_expression_on)
        {
            showInputMode();
            if (clickListener != null)
            {
                clickListener.onClickExpressionOn();
            }
        } else if (v == iv_more)
        {
            showMoreMode();
            if (clickListener != null)
            {
                clickListener.onClickMore();
            }
        } else if (v == tv_send)
        {
            if (clickListener != null)
            {
                clickListener.onClickSend(getInputContent());
            }
        }
    }

    /**
     * 显示输入键盘
     */
    public void showInputMethod()
    {
        SDViewUtil.showInputMethod(et_content);
    }

    /**
     * 隐藏输入键盘
     */
    public void hideInputMethod()
    {
        SDViewUtil.hideInputMethod(et_content);
    }

    public void showNormalMode()
    {
        showFirstVoice();
        showSecondInput();
        showThirdExpressionOff();
        showFourByContent();
        hideInputMethod();
    }

    /**
     * 语音输入模式
     */
    private void showVoiceMode()
    {
        showFirstKeyboard();
        showSecondVoice();
        showThirdExpressionOff();
        showFourMore();
        hideInputMethod();
    }

    /**
     * 文字输入模式
     */
    private void showInputMode()
    {
        showFirstVoice();
        showSecondInput();
        showThirdExpressionOff();
        showFourByContent();
        showInputMethod();
    }

    /**
     * 更多模式
     */
    private void showMoreMode()
    {
        showFirstVoice();
        showSecondInput();
        showThirdExpressionOff();
        showFourMore();
    }

    /**
     * 显示表情模式
     */
    public void showExpressionMode()
    {
        showFirstVoice();
        showSecondInput();
        showThirdExpressionOn();
        showFourByContent();
    }

    protected void showFirstVoice()
    {
        if (voiceModeEnable)
        {
//            SDViewUtil.show(iv_voice);
            SDViewUtil.hide(iv_keyboard);
        }
    }

    protected void showFirstKeyboard()
    {
        if (voiceModeEnable)
        {
            SDViewUtil.show(iv_keyboard);
//            SDViewUtil.hide(iv_voice);
        }
    }

    private void showSecondVoice()
    {
        SDViewUtil.show(tv_record);
        SDViewUtil.hide(et_content);
    }

    private void showSecondInput()
    {
        SDViewUtil.show(et_content);
        SDViewUtil.hide(tv_record);
    }

    protected void showThirdExpressionOff()
    {
        SDViewUtil.show(iv_expression_off);
        SDViewUtil.hide(iv_expression_on);
    }

    protected void showThirdExpressionOn()
    {
        SDViewUtil.show(iv_expression_on);
        SDViewUtil.hide(iv_expression_off);
    }

    protected void showFourMore()
    {
        SDViewUtil.show(iv_more);
        SDViewUtil.invisible(tv_send);
    }

    protected void showFourSend()
    {
        SDViewUtil.show(tv_send);
        SDViewUtil.invisible(iv_more);
    }

    protected void showFourByContent()
    {
        if (getInputContent().length() > 0)
        {
            showFourSend();
        } else
        {
            showFourMore();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        showFourByContent();
    }

    public String getInputContent()
    {
        return et_content.getText().toString();
    }


    public interface ClickListener
    {
        void onClickKeyboard();

        void onClickVoice();

        void onClickExpressionOff();

        void onClickExpressionOn();

        void onClickMore();

        void onClickSend(String content);

        boolean onTouchEditText();
    }
}
