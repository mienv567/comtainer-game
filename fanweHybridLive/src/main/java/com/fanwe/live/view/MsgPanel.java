package com.fanwe.live.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import java.util.ArrayList;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */
public class MsgPanel {

    private ArrayList<Object> mData = new ArrayList<>();

    private View mRootView;

    private FrameLayout mLayoutContainer;

    public MsgPanel(FrameLayout layout) {
        mLayoutContainer = layout;
        mRootView = LayoutInflater.from(mLayoutContainer.getContext())
                .inflate(R.layout.panel_live_h_msg, null);
        ListView lv_msg = (ListView) mRootView.findViewById(R.id.lv_msg);
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        lv_msg.setAdapter(mAdapter);
    }

    public View getRootView() {
        return mRootView;
    }

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoder hoder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_live_h_msg, null);
                hoder = new ViewHoder();
                hoder.tvAnchor = (TextView) convertView.findViewById(R.id.tv_anchor);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHoder) convertView.getTag();
            }
            return convertView;
        }
    };

    public boolean isShown() {
        return mRootView.isShown();
    }

    private static class ViewHoder {
        public TextView tvAnchor;
    }

    public void show() {
        SDViewUtil.addView(mLayoutContainer, mRootView);
    }

    public void hide() {
        mLayoutContainer.removeView(mRootView);
    }

    public void toggle() {
        if (isShown()) {
            hide();
        } else {
            show();
        }
    }
}
