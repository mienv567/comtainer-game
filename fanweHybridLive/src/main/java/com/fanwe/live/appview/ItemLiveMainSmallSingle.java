package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCategoryActivity;
import com.fanwe.live.model.CategoryModel;

public class ItemLiveMainSmallSingle extends BaseAppView {

    private ImageView iv_head_image;
    private TextView tv_category;
    private CategoryModel mCategoryModel;
    public ItemLiveMainSmallSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveMainSmallSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveMainSmallSingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.item_live_main_small_single);
        iv_head_image = find(R.id.iv_head_image);
        tv_category = find(R.id.tv_category);
    }


    public void setModel(CategoryModel model) {
        mCategoryModel = model;
        if(mCategoryModel == null){
            SDViewUtil.invisible(this);
        }else{
            if(!TextUtils.isEmpty(mCategoryModel.getCategory_round_cover_url())) {
                SDViewBinder.setImageView(getActivity(), iv_head_image, mCategoryModel.getCategory_round_cover_url(), R.drawable.layer_bg_main_solid_10dp_corner);
            }
            SDViewBinder.setTextView(tv_category, mCategoryModel.getCategory_name());
            try {
                tv_category.setBackgroundColor(Color.parseColor("#" + mCategoryModel.getCategory_color()));
            }catch(Exception e){
                LogUtil.e("颜色码不对");
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(mCategoryModel != null){
            Intent intent = new Intent(getActivity(), LiveCategoryActivity.class);
            intent.putExtra(LiveCategoryActivity.EXTRA_CATEGORY_ID,mCategoryModel.getCategory_id());
            getActivity().startActivity(intent);
        }
    }
}
