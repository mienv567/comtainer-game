package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.R;

public class LiveSendLikeView extends BaseAppView {

    public LiveSendLikeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveSendLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveSendLikeView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_live_send_like;
    }

    @Override
    protected void init() {
        super.init();
    }
}
