package com.fanwe.live.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by yong.zhang on 2017/3/22 0022.
 */
public class LiveTaskDetailDialog extends PopupWindow {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.tv_content)
    private TextView tvContent;

    @ViewInject(R.id.tv_action)
    private TextView tvAction;

    private View.OnClickListener mListner;

    public LiveTaskDetailDialog(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_live_task_detail, null);
        setContentView(layout);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvContent = (TextView) layout.findViewById(R.id.tv_content);
        tvAction = (TextView) layout.findViewById(R.id.tv_action);
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListner != null) {
                    mListner.onClick(v);
                }
            }
        });
    }

    public void showLeft(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        DisplayMetrics dm = v.getResources().getDisplayMetrics();
        /*showAtLocation(v, Gravity.RIGHT | Gravity.TOP,
                (int) (dm.widthPixels - location[0] + 20 * dm.density), location[1]);*/
        showAtLocation(v, Gravity.RIGHT | Gravity.CENTER_VERTICAL,//竖直方向改为屏幕居中显示
                (int) (dm.widthPixels - location[0] + 20 * dm.density), 0);
    }

    public void setContent(String msg) {
        tvContent.setText(msg);
    }

    public void setAction(String action) {
        tvAction.setText(action);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setOnClickListner(View.OnClickListener listner) {
        this.mListner = listner;
    }
}
