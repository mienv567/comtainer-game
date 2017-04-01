package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.presenters.viewinterface.ITitleViewListener;

/**
 * Created by TonyChen on 2016/12/31.
 */

public class CustomTitleView extends RelativeLayout implements View.OnClickListener, ITitleView {
    private Context mContext;
    private ITitleBarOnClickListener onClickListener;
    private FrameLayout fl_middle_content;

    @Override
    public void setTitleBarOnClickListener(ITitleBarOnClickListener listener) {
        this.onClickListener = listener;
    }

    private TextView tv_title;
    private TextView tv_lefttitle;
    private TextView tv_righttitle;
    private ImageView iv_titlebar_left;
    private ImageView iv_titlebar_right;
    private LinearLayout ll_left;
    private LinearLayout ll_right;


    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        View titleView = View.inflate(context, R.layout.com_titlebar, null);
        fl_middle_content = (FrameLayout) titleView.findViewById(R.id.fl_titlebar_middle_customview);
        tv_title = (TextView) titleView.findViewById(R.id.tv_titlebar_middle_title);
        tv_lefttitle = (TextView) titleView.findViewById(R.id.tv_titlebar_left);
        tv_righttitle = (TextView) titleView.findViewById(R.id.tv_titlebar_right);
        iv_titlebar_left = (ImageView) titleView.findViewById(R.id.iv_titlebar_left);
        iv_titlebar_right = (ImageView) titleView.findViewById(R.id.iv_titlebar_right);
        ll_left = (LinearLayout) titleView.findViewById(R.id.ll_titlebar_left);
        ll_right = (LinearLayout) titleView.findViewById(R.id.ll_titlebar_right);
        ll_left.setOnClickListener(this);
        ll_right.setOnClickListener(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.activity_titlebar_height));
        this.addView(titleView, 0, params);

    }

    @Override
    public void onClick(View v) {
        if (v == ll_left) {
            if (null != onClickListener) {
                onClickListener.onClickLeft();
            }

        } else if (v == ll_right) {
            if (null != onClickListener) {
                onClickListener.onClickRight();
            }
        }


    }

    @Override
    public void setTitleText(String titleText) {
        tv_title.setText(titleText);
    }

    @Override
    public void setLeftImage(int resid) {
        iv_titlebar_left.setVisibility(VISIBLE);
        iv_titlebar_left.setBackgroundResource(resid);
    }

    @Override
    public void setRightImage(int resid) {
        iv_titlebar_right.setVisibility(VISIBLE);
        iv_titlebar_right.setBackgroundResource(resid);
    }


    @Override
    public void setLeftTitle(String leftTitle) {
        tv_lefttitle.setVisibility(VISIBLE);
        tv_lefttitle.setText(leftTitle);
    }

    @Override
    public void setRightTitle(String rightTitle) {
        tv_righttitle.setVisibility(VISIBLE);
        tv_righttitle.setText(rightTitle);
    }

    @Override
    public ViewGroup getTitleBarMiddleContentView() {
        tv_title.setVisibility(GONE);
        return fl_middle_content;
    }

    @Override
    public ViewGroup getTitleBarLeftContentView() {
        return ll_left;
    }

    @Override
    public ViewGroup getTitleBarRightContentView() {
        return ll_right;
    }


}
