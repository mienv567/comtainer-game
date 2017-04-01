package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.SDSlidingButton;

import java.util.List;

/**
 * 用户关注推送
 */
public class UserFollowPushModelAdapter extends SDSimpleAdapter<UserModel>
{
    private UserModel user = UserModelDao.query();

    public UserFollowPushModelAdapter(List<UserModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_follow_push;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final UserModel model)
    {
        ImageView civ_v_icon = ViewHolder.get(R.id.civ_v_icon, convertView);
        SDViewUtil.hide(civ_v_icon);

        ImageView civ_head_image = ViewHolder.get(R.id.civ_head_image, convertView);
        TextView tv_nick_name = ViewHolder.get(R.id.tv_nick_name, convertView);
        ImageView iv_global_male = ViewHolder.get(R.id.iv_global_male, convertView);
        ImageView iv_rank = ViewHolder.get(R.id.iv_rank, convertView);
        SDSlidingButton sl_btn = ViewHolder.get(R.id.sl_btn,convertView);

        SDViewBinder.setImageView(getActivity(), model.getHeadImage(), civ_head_image, R.drawable.ic_default_head);
        SDViewBinder.setTextView(tv_nick_name, model.getNickName());
        if (model.getSex() > 0)
        {
            SDViewUtil.show(iv_global_male);
            iv_global_male.setImageResource(model.getSexResId());
        } else
        {
            SDViewUtil.hide(iv_global_male);
        }
        iv_rank.setImageResource(model.getLevelImageResId());

        if(model.getIsUnpush() == 0){
            sl_btn.setSelected(true);
        }else{
            sl_btn.setSelected(false);
        }

        if (user != null)
        {
            sl_btn.setSelectedChangeListener(new SDSlidingButton.SelectedChangeListener() {
                @Override
                public void onSelectedChange(SDSlidingButton view, boolean selected) {
                    boolean currentSelected = (model.getIsUnpush() == 0);
                    if(currentSelected != selected){
                        CommonInterface.requestChangePush(model.getUserId(), new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if(actModel.isOk()){
                                    if(model.getIsUnpush() == 0){
                                        model.setIsUnpush(1);
                                    }else{
                                        model.setIsUnpush(0);
                                    }
                                }else{
                                    SDToast.showToast(SDResourcesUtil.getString(R.string.operate_fail));
                                }
                            }
                        });
                    }
                }
            });
        }

        if (onItemClickListener != null)
        {
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(model, position);
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener
    {
        void OnItemClick(UserModel user, int position);
    }
}
