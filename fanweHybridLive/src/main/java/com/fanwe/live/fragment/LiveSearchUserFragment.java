package com.fanwe.live.fragment;

import android.text.TextUtils;

import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSerchUserModelAdapter;
import com.fanwe.live.appview.LiveSearchUserView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by shibx on 2016/7/25.
 */
public class LiveSearchUserFragment extends LiveBaseSearchFragment {

    @ViewInject(R.id.view_user)
    private LiveSearchUserView view_user;

    private SDRequestHandler mHandler = null;

    private LiveSerchUserModelAdapter.OnItemClickListener mListener;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_search_user;
    }

    @Override
    protected void init() {
        super.init();
        view_user.setOnItemClickListener(mListener);
    }

    public void setOnItemClickListener(LiveSerchUserModelAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void search(String keyWord) {
        if(!TextUtils.isEmpty(keyWord)) {
            if(TextUtils.equals(keyWord,view_user.keyword)) {
//                SDToast.showToast("查找相同字符串");
                return ;
            }
            if(mHandler != null) {
                mHandler.cancel();
            }
            mHandler = view_user.search(keyWord);
        }else {
//            SDToast.showToast("查找空字符串");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索-用户搜索");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索-用户搜索");
    }
}
