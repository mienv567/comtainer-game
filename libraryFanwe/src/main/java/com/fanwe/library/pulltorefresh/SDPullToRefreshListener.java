package com.fanwe.library.pulltorefresh;

import android.view.View;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface SDPullToRefreshListener<T extends View>
{

    void onPullDownToRefresh(T view);

    void onPullUpToRefresh(T view);

}
