package com.fanwe.live.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.live.R;

import java.util.ArrayList;

/**
 * Created by yong.zhang on 2017/3/15 0015.
 */
public class OrderPanel {

    private ArrayList<Object> mData = new ArrayList<>();

    private View mRootView;

    private int mRole;

    public OrderPanel(Context context, int role) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.panel_live_h_order, null);
        ListView lvOrder = (ListView) mRootView.findViewById(R.id.lv_order);
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        mData.add(new Object());
        lvOrder.setAdapter(mAdapter);
        this.mRole = role;
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
                        .inflate(R.layout.item_live_h_order, null);
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
}
