package com.fanwe.library.listener;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface SDActivityLifeCircleListener
{
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
