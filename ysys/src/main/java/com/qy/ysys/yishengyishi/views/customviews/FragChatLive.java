package com.qy.ysys.yishengyishi.views.customviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.R;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragChatLive extends BaseTitleFragment {
    @Override
    protected void initTitleBar(ITitleView titleView) {

    }

    @Override
    protected boolean shouldShowTitle() {
        return false;
    }
    @Override
    protected View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat_live, null);

    }
}
