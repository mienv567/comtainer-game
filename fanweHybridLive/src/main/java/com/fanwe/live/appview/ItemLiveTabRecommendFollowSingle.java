package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.UserModel;

public class ItemLiveTabRecommendFollowSingle extends BaseAppView {

    private ImageView iv_head_image;
    private ImageView selected_img;
    private TextView tv_nick_name;
    private UserModel model;

    public ItemLiveTabRecommendFollowSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveTabRecommendFollowSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveTabRecommendFollowSingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();

        setContentView(R.layout.item_live_tab_recommend_follow_single);

        iv_head_image = find(R.id.iv_head_image);
        selected_img = find(R.id.selected_img);
        tv_nick_name = find(R.id.tv_nick_name);
    }

    public UserModel getModel() {
        return model;
    }

    public void setModel(UserModel model) {
        this.model = model;
        if (model != null) {
            SDViewUtil.show(this);
            if(model.isSelected()){
                selected_img.setImageDrawable(getResources().getDrawable(R.drawable.recommend_follow_selected));
            }else{
                selected_img.setImageDrawable(getResources().getDrawable(R.drawable.recommend_follow_normal));
            }
            SDViewBinder.setImageView(getActivity(), model.getHeadImage(), iv_head_image);
            SDViewBinder.setTextView(tv_nick_name,model.getNickName());
        } else {
            SDViewUtil.hide(this);
        }
    }

}
