package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 狼人杀游戏投票界面
 */
public class LRSVoteDialog extends SDDialogBase {

    @ViewInject(R.id.ll_root_view)
    private LinearLayout ll_root_view;
    @ViewInject(R.id.ll_close)
    private LinearLayout ll_close; //关闭
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;//确定
    @ViewInject(R.id.tv_content)
    private TextView tv_content;//投票内容
    private String mContent;
    public LRSVoteDialog(Activity activity, String content) {
        super(activity);
        mContent = content;
        init();
    }


    private void init() {
        setContentView(R.layout.dialog_lrs_vote);
        setCanceledOnTouchOutside(false);
        paddingLeft(SDViewUtil.dp2px(20));
        paddingRight(SDViewUtil.dp2px(20));
        x.view().inject(this, getContentView());
        ll_root_view.setAlpha(0.7f);
        register();
        bindData();
    }

    private void register() {
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void bindData() {
       if(!TextUtils.isEmpty(mContent)){
           SDViewBinder.setTextView(tv_content,mContent);
       }
    }

}
