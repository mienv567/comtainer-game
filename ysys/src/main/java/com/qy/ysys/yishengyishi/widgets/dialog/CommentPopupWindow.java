package com.qy.ysys.yishengyishi.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.CommonUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;

/**
 * Created by kevin.liu on 2017/3/23.
 */

public class CommentPopupWindow {

    // 加的popupWindow
    private LiveCommentResult liveCommentResult;
    private View commentView;
    private PopupWindow commentPopup;
    private EditText popup_live_comment_edit;
    private InputMethodManager inputMethodManager;

    public CommentPopupWindow(final Activity activity, LiveCommentResult commentResult) {
        liveCommentResult = commentResult;
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (commentView == null) {
            commentView = activity.getLayoutInflater().inflate(R.layout.layout_editview, null);
        }
        if (commentPopup == null) {
            // 创建一个PopuWidow对象
            commentPopup = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画 commentPopup.setAnimationStyle(R.style.popWindow_animation_in2out);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        commentPopup.setFocusable(true);
        // 设置允许在外点击消失
        commentPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        commentPopup.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        commentPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        popup_live_comment_edit = (EditText) commentView.findViewById(R.id.circleEt);
        commentView.findViewById(R.id.sendIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布评论
                String result = popup_live_comment_edit.getText().toString().trim();
                if (TextUtils.isEmpty(result)) {
                    ToastUtil.showToast("评论内容不能为空...", Toast.LENGTH_SHORT);
                    return;
                }
                if (liveCommentResult != null && result.length() != 0) {
                    //把数据传出去
                    liveCommentResult.onResult(true, result);
                    //关闭popup
                    commentPopup.dismiss();
                }
            }
        });
        //设置popup关闭时要做的操作
        commentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideWindow(popup_live_comment_edit, true);
            }
        });
    }

    /**
     * 显示方法
     *
     * @param view 在制定view上显示
     */
    public void showWindowAtView(View view) {
        // PopupWindow的显示及位置设置
        commentPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        CommonUtils.showSoftInput(view.getContext(), view);
    }

    private boolean hideKeyboard(IBinder windowToken) {
        if (inputMethodManager.isActive(popup_live_comment_edit)) {
            //因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
            commentView.requestFocus();//使其它view获取焦点.这里因为是在fragment下,所以便用了getView(),可以指定任意其它view
            inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    /**
     * 隐藏输入框并清空
     *
     * @param view
     */
    public void hideWindow(View view, boolean clear) {
        hideKeyboard(view.getWindowToken());
        if (clear) {
            popup_live_comment_edit.setText("");
        }
    }

    /**
     * 发送评论回调
     */
    public interface LiveCommentResult {
        void onResult(boolean confirmed, String comment);
    }
}
