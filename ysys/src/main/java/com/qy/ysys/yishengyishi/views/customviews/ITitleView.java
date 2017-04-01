package com.qy.ysys.yishengyishi.views.customviews;

import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by tony.chen on 2017/1/4.
 */

public interface ITitleView {
    void setTitleText(String titleText);

    void setLeftImage(int resid);

    void setRightImage(int resid);

    void setLeftTitle(String leftTitle);

    void setRightTitle(String rightTitle);

    ViewGroup getTitleBarMiddleContentView();

    ViewGroup getTitleBarLeftContentView();

    ViewGroup getTitleBarRightContentView();

    void setTitleBarOnClickListener(ITitleBarOnClickListener listener);

}
