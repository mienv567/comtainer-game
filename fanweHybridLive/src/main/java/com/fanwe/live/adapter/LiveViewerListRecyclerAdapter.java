package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.info.LiveInfo;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * 直播间观众列表
 */
public class LiveViewerListRecyclerAdapter extends SDSimpleRecyclerAdapter<UserModel> {

    public LiveViewerListRecyclerAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_user;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, final UserModel model) {
        ImageView iv_pic = holder.get(R.id.iv_pic);
        ImageView iv_level = holder.get(R.id.iv_level);

        SDViewBinder.setImageView(getActivity(), model.getThumbHeadImage(), iv_pic, R.drawable.ic_default_head);

        if (!TextUtils.isEmpty(model.getV_icon())) {
            SDViewUtil.show(iv_level);
            SDViewBinder.setImageView(getActivity(), model.getV_icon(), iv_level);
        } else {
            SDViewUtil.hide(iv_level);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(getActivity(), "live_other_head");
                int isManage = ((LiveInfo) getActivity()).getRoomInfo().getIsManage();
                LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), model.getUserId(), isManage);
                dialog.show();
            }
        });
    }
}
