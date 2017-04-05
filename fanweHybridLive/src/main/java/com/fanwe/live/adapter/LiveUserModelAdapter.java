package com.fanwe.live.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.pop.OpenPushTipsPop;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午2:14:28 类说明
 */
public class LiveUserModelAdapter extends SDSimpleAdapter<UserModel>
{
    private UserModel user = UserModelDao.query();
    private static Handler mShowHandler = new Handler();
    public LiveUserModelAdapter(List<UserModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_focus_follow;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final UserModel model)
    {
        final OpenPushTipsPop pop = new OpenPushTipsPop(mActivity);
        ImageView civ_v_icon = ViewHolder.get(R.id.civ_v_icon, convertView);
        SDViewUtil.hide(civ_v_icon);

        ImageView civ_head_image = ViewHolder.get(R.id.civ_head_image, convertView);
        TextView tv_nick_name = ViewHolder.get(R.id.tv_nick_name, convertView);
        TextView tv_signature = ViewHolder.get(R.id.tv_signature, convertView);
        ImageView iv_global_male = ViewHolder.get(R.id.iv_global_male, convertView);
        ImageView iv_rank = ViewHolder.get(R.id.iv_rank, convertView);

        final RelativeLayout has_follow = ViewHolder.get(R.id.ll_has_follow,convertView);
        final RelativeLayout follow = ViewHolder.get(R.id.ll_follow,convertView);
        View left_click_view = ViewHolder.get(R.id.v_click_left,convertView);
        final View right_click_view = ViewHolder.get(R.id.v_click_right,convertView);
        final ImageView img_push = ViewHolder.get(R.id.img_push,convertView);
        if (user.getUserId().equals(model.getUserId()))
        {
            SDViewUtil.hide(has_follow);
            SDViewUtil.hide(follow);
        }


        SDViewBinder.setImageView(getActivity(),model.getHeadImage(), civ_head_image,R.drawable.ic_default_head);
        SDViewBinder.setTextView(tv_nick_name, model.getNickName());
        SDViewBinder.setTextView(tv_signature, model.getSignature());
        if (model.getSex() > 0)
        {
            SDViewUtil.show(iv_global_male);
            iv_global_male.setImageResource(model.getSexResId());
        } else
        {
            SDViewUtil.hide(iv_global_male);
        }
        iv_rank.setImageResource(model.getLevelImageResId());

        if (model.getRelationship() == 1)
        {
            SDViewUtil.show(has_follow);
            SDViewUtil.hide(follow);
        } else
        {
            SDViewUtil.show(follow);
            SDViewUtil.hide(has_follow);
        }

        if(model.getIsUnpush() == 0){
            img_push.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_push_open));
        }else{
            img_push.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_push_close));
        }

        if (user != null)
        {
            left_click_view.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    followOnClick(model,has_follow,follow,position);
                }
            });
            follow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    followOnClick(model, has_follow, follow, position);
                }
            });
            right_click_view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonInterface.requestChangePush(model.getUserId(), new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (rootModel.isOk()) {
                                if (model.getIsUnpush() == 0) {
                                    model.setIsUnpush(1);
                                    img_push.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_push_close));
                                    pop.showPopTips(SDResourcesUtil.getString(R.string.live_inform_closed), right_click_view);
                                    closePopWindow(pop);
                                } else {
                                    model.setIsUnpush(0);
                                    img_push.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_push_open));
                                    pop.showPopTips(SDResourcesUtil.getString(R.string.live_inform_opened), right_click_view);
                                    closePopWindow(pop);
                                }
                            } else {
                                SDToast.showToast(SDResourcesUtil.getString(R.string.operate_fail));
                            }
                        }
                    });
                }
            });
        }

        if (onItemClickListener != null)
        {
            convertView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemClickListener.OnItemClick(model, position);
                }
            });
        }
    }

    private void closePopWindow(final OpenPushTipsPop pop){
        mShowHandler.removeCallbacksAndMessages(0);
        mShowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        }, 3 * 1000);
    }

    private void followOnClick(UserModel model,final RelativeLayout has_follow,final RelativeLayout follow,final int position){
        CommonInterface.requestFollow(model.getUserId(), model, new AppRequestCallback<App_followActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    if (actModel.getRelationship() == 1) {
                        SDViewUtil.show(has_follow);
                        SDViewUtil.hide(follow);
                    } else {
                        SDViewUtil.show(follow);
                        SDViewUtil.hide(has_follow);
                    }
                    getData().get(position).setRelationship(actModel.getRelationship());
                    updateItem(position);
                } else {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.operate_fail));
                }

            }
        });
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
