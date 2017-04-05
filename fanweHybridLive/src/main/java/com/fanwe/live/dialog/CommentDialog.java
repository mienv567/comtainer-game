package com.fanwe.live.dialog;

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

import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.item.FamilyCommentItem;

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

    public CommentDialog(Context context, FamilyCommentItem commentItem) {
        super(context, R.style.comment_dialog);
        mContext = context;
        this.mCommentItem = commentItem;
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
                && UserModelDao.query().getUserId().equals(mCommentItem.userId)) {
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
                clipboard.setText(mCommentItem.comment);
            }
            dismiss();
        } else if (id == R.id.deleteTv) {
            //EventBus.getDefault().post(new EDeleteFamilyComment(mCirclePosition, mCommentItem.userId));
//			if (mPresenter != null && mCommentItem != null) {
//				mPresenter.deleteComment(mCirclePosition, mCommentItem.getId());
//			}
            dismiss();
        }
    }

}
