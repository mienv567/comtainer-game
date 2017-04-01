package com.qy.ysys.yishengyishi.views.customviews;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TonyChen on 2017/1/1.
 */

public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    protected abstract View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
