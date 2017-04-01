package com.fanwe.hybrid.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Yuan on 2017/3/26.
 * 邮箱：44004606@qq.com
 */

public class DialogViewHolder {

    private final SparseArray<View> mViews;
    private View mDialogView;

    private DialogViewHolder(Context context, int layoutId) {
        mViews = new SparseArray<>();
        mDialogView = View.inflate(context, layoutId, null);
    }

    public static DialogViewHolder get(Context context, int layoutId) {
        return new DialogViewHolder(context, layoutId);
    }

    public View getConvertView() {
        return mDialogView;
    }

    /**
     * 设置dialog中textView的文字
     */
    public DialogViewHolder setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 设置dialog中View的点击事件
     */
    public DialogViewHolder setOnClick(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置dialog中View为可见
     */
    public DialogViewHolder setViewViSible(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置dialog中View为不可见
     */
    public DialogViewHolder setViewInViSible(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 设置dialog中View为gone
     */
    public DialogViewHolder setViewGone(int viewId) {
        TextView view = getView(viewId);
        view.setVisibility(View.GONE);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mDialogView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
