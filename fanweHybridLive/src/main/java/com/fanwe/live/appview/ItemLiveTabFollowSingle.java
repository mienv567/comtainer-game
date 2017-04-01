package com.fanwe.live.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveRoomModel;

public class ItemLiveTabFollowSingle extends BaseAppView {

    private ImageView iv_head_image;
    //    private ImageView iv_level;
    private TextView tv_vediostate; // 视频当前状态
//    private TextView tv_city;
    private TextView tv_name;
    private LiveRoomModel model;
    private ImageView iv_ringgirl_logo;
    private String logoUrl;

    public ItemLiveTabFollowSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveTabFollowSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveTabFollowSingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();

        setContentView(R.layout.item_live_tab_follow_single);

        iv_head_image = find(R.id.iv_head_image);
        tv_vediostate = find(R.id.tv_vediostate);
        //        iv_level = find(iv_level);
//        tv_city = find(R.id.tv_city);
        tv_name = find(R.id.tv_name);
        iv_ringgirl_logo = find(R.id.iv_ringgirl_logo);
    }

    public LiveRoomModel getModel() {
        return model;
    }

    public void setModel(LiveRoomModel model) {
        this.model = model;
        if (model != null) {
            SDViewUtil.show(this);
            SDViewBinder.setImageView(getActivity(),model.getHeadImage(), iv_head_image);
            //            iv_level.setImageResource(LiveUtils.getLevelImageResId(model.getUserLevel()));
            //直播状态 LiveRoomModel定义的:0-结束;1-正在直播;2-创建中;3-回看
            tv_vediostate.setVisibility(VISIBLE);
            if (model.getLiveStatus() == 0) {
                SDViewBinder.setTextView(tv_vediostate, SDResourcesUtil.getString(R.string.finish));
            } else if (model.getLiveStatus() == 1) {
                SDViewBinder.setTextView(tv_vediostate, SDResourcesUtil.getString(R.string.is_living));
            } else if (model.getLiveStatus() == 2) {
                SDViewBinder.setTextView(tv_vediostate, SDResourcesUtil.getString(R.string.is_creating));
            } else if (model.getLiveStatus() == 3) {
                SDViewBinder.setTextView(tv_vediostate, SDResourcesUtil.getString(R.string.playback));
            } else {
                tv_vediostate.setVisibility(INVISIBLE);
            }
            SDViewBinder.setTextView(tv_name,model.getNickName());
//            SDViewBinder.setTextView(tv_city, model.getCity(), SDResourcesUtil.getString(R.string.live_city_default));
            logoUrl = model.getLogoUrl();
            if (!TextUtils.isEmpty(logoUrl)) {
                iv_ringgirl_logo.setVisibility(View.VISIBLE);
                SDViewBinder.setImageView(getActivity(),logoUrl, iv_ringgirl_logo);
            } else {
                iv_ringgirl_logo.setVisibility(View.GONE);
            }
        } else {
            SDViewUtil.hide(this);
        }
    }

}
