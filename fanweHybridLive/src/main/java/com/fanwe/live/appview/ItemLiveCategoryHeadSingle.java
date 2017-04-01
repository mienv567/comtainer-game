package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.CategoryModel;
import com.fanwe.live.model.LiveRoomModel;

import org.xutils.view.annotation.ViewInject;

public class ItemLiveCategoryHeadSingle extends BaseAppView {

    private CategoryModel mCategoryModel;
    @ViewInject(R.id.iv_head_image)
    private ImageView iv_head_image;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;
    @ViewInject(R.id.title_one)
    private TextView title_one;
    @ViewInject(R.id.title_two)
    private TextView title_two;

    public ItemLiveCategoryHeadSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveCategoryHeadSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveCategoryHeadSingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.item_live_category_header_single);
    }


    public void setModel(LiveRoomModel model) {
        mCategoryModel = model.getCategory();
        if(mCategoryModel != null){
            SDViewBinder.setImageView(getActivity(),iv_head_image,mCategoryModel.getCategory_header_url(),R.drawable.ic_default_head);
            SDViewBinder.setTextView(tv_num,mCategoryModel.getVideo_num()+ SDResourcesUtil.getString(R.string.live_num));
            SDViewBinder.setTextView(title_one, mCategoryModel.getCategory_name() + SDResourcesUtil.getString(R.string.topic_live));
            SDViewBinder.setTextView(title_two,mCategoryModel.getCategory_desc());
        }
    }


}
