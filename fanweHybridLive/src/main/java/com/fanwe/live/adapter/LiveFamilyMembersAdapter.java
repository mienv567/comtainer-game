package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * 家族成员adapter
 * Created by Administrator on 2016/9/28.
 */

public class LiveFamilyMembersAdapter extends SDSimpleAdapter<UserModel>
{
    private ItemClickListener<UserModel> clickDelListener;

    public void setClickDelListener(ItemClickListener<UserModel> clickDelListener) {
        this.clickDelListener = clickDelListener;
    }

    public LiveFamilyMembersAdapter(List<UserModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_family_members;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final UserModel model)
    {
        ImageView iv_head_img = get(R.id.civ_head_image, convertView);
        ImageView civ_v_icon = get(R.id.civ_v_icon, convertView);
        TextView tv_nick_name = get(R.id.tv_nick_name, convertView);
        ImageView iv_global_male = get(R.id.iv_global_male, convertView);
        ImageView iv_rank = get(R.id.iv_rank, convertView);
        TextView tv_signature = get(R.id.tv_signature,convertView);
        TextView txv_del = get(R.id.txv_del,convertView);//踢出家族

        SDViewBinder.setImageView(getActivity(),model.getHeadImage(), iv_head_img);
        SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_v_icon);
        if (model.getSex() > 0)
        {
            SDViewUtil.show(iv_global_male);
            iv_global_male.setImageResource(model.getSexResId());
        } else
        {
            SDViewUtil.hide(iv_global_male);
        }
        iv_rank.setImageResource(model.getLevelImageResId());
        tv_nick_name.setText(model.getNickName());
        tv_signature.setText(model.getSignature());
        if (model.getFamilyChieftain() == 1)//是否家族长；0：否、1：是
            txv_del.setVisibility(View.GONE);
        else
            txv_del.setVisibility(View.VISIBLE);

        /**
         * 踢出家族
         */
        txv_del.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (clickDelListener != null)
                {
                    clickDelListener.onClick(position, model, view);
                }
            }
        });
    }
}
