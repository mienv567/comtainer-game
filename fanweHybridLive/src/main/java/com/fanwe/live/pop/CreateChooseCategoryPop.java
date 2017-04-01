package com.fanwe.live.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.CategoryRecyclerAdapter;
import com.fanwe.live.model.CategoryNameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题点击更多pop
 */
public class CreateChooseCategoryPop extends PopupWindow {

    private View contentView;
    private Activity mActivity;
    private CategoryRecyclerAdapter mAdapter;
    private SDRecyclerView live_view;
    private List<CategoryNameModel> mList = new ArrayList<>();

    public CreateChooseCategoryPop(Activity context) {
        mActivity = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = layoutInflater.inflate(R.layout.pop_create_category, null);
        setContentView(contentView);
        live_view = (SDRecyclerView)contentView.findViewById(R.id.live_view);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        this.setBackgroundDrawable(dw);

        setOutsideTouchable(true);
        setFocusable(false);
        initAdapter();
    }

    private void initAdapter(){
        mAdapter = new CategoryRecyclerAdapter(mActivity);
        live_view.setAdapter(mAdapter);
    }

    public void setList(List<CategoryNameModel> list){
        mList = list;
        mAdapter.updateData(mList);
    }

    public void showPopTips(View parent,int top,int left) {
        if(isShowing()) {
            dismiss();
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        showAtLocation(parent, Gravity.BOTTOM, location[0] - SDViewUtil.getScreenWidth() / 2 + this.getWidth() / 2 - left,
                SDViewUtil.getScreenHeight() / 2 + top);
    }

    public void showPopTips(View parent) {
        if(isShowing()) {
            dismiss();
        }
        SDViewUtil.showPopTopAndLeft(this, parent, 0, SDViewUtil.dp2px(42));
    }


}
