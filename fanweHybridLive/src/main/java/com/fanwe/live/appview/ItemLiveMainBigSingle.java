package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCategoryActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.CategoryModel;
import com.fanwe.live.model.HeadCategoryModel;
import com.fanwe.live.model.JoinLiveData;

public class ItemLiveMainBigSingle extends BaseAppView {

    private ImageView iv_head_image;
    private HeadCategoryModel mHeadModel;
    public ItemLiveMainBigSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveMainBigSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveMainBigSingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.item_live_main_big_single);
        iv_head_image = find(R.id.iv_head_image);

    }


    public void setModel(CategoryModel model) {
        mHeadModel = model.getHead();
        if(mHeadModel != null && !TextUtils.isEmpty(mHeadModel.getRound_url())){
            SDViewBinder.setImageView(getActivity(), iv_head_image, mHeadModel.getRound_url(), R.drawable.layer_bg_main_solid_10dp_corner);
        }else{
            SDViewUtil.hide(this);
        }
    }

    public void onClick(View v) {
        super.onClick(v);
        if(mHeadModel != null){
            switch (mHeadModel.getType()){
                case HeadCategoryModel.GO_TO_ROOM:
                    JoinLiveData data = new JoinLiveData();
                    data.setCreaterId(mHeadModel.getUser_id());
                    data.setGroupId(mHeadModel.getGroup_id());
                    data.setLoadingVideoImageUrl(mHeadModel.getHead_image());
                    data.setRoomId(mHeadModel.getRoom_id());
                    data.setType(1);
                    AppRuntimeWorker.joinLive(data, getActivity(), false);
                    break;
                case HeadCategoryModel.GO_TO_CATEGORY:
                    Intent intent = new Intent(getActivity(), LiveCategoryActivity.class);
                    intent.putExtra(LiveCategoryActivity.EXTRA_CATEGORY_ID,mHeadModel.getCategory_id());
                    getActivity().startActivity(intent);
                    break;
            }
        }
    }

}
