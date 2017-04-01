package com.qy.ysys.yishengyishi.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.event.EDeleteFamilyComment;
import com.qy.ysys.yishengyishi.model.item.FamilyCommentItem;

import de.greenrobot.event.EventBus;

/**
 * @author yiw
 * @ClassName: CommentDialog
 * @Description: 评论长按对话框，保护复制和删除
 * @date 2015-12-28 下午3:36:39
 */
public class CommentDialog extends Dialog implements
        View.OnClickListener {

    private Context mContext;
    private FamilyCommentItem mCommentItem;
    private int mCirclePosition;

    public CommentDialog(Context context, FamilyCommentItem commentItem, int circlePosition) {
        super(context, R.style.comment_dialog);
        mContext = context;
        this.mCommentItem = commentItem;
        this.mCirclePosition = circlePosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
        if (mCommentItem != null
                && AppImpl.getCurrentUser().getId() ==
                mCommentItem.getUserId()) {
            deleteTv.setVisibility(View.VISIBLE);
        } else {
            deleteTv.setVisibility(View.GONE);
        }
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.copyTv) {
            if (mCommentItem != null) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(mCommentItem.getComment());
            }
            dismiss();
        } else if (id == R.id.deleteTv) {
            EventBus.getDefault().post(new EDeleteFamilyComment(mCirclePosition, mCommentItem.getId()));
//			if (mPresenter != null && mCommentItem != null) {
//				mPresenter.deleteComment(mCirclePosition, mCommentItem.getId());
//			}
            dismiss();
        }
    }

}
