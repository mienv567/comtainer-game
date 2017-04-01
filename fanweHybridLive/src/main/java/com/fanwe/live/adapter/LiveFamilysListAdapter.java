package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.model.App_family_listItemActModel;

import java.util.List;


/**
 * Created by Administrator on 2016/9/24.
 */

public class LiveFamilysListAdapter extends SDSimpleAdapter<App_family_listItemActModel>
{
    private ItemClickListener<App_family_listItemActModel> clickJoinListener;

    public void setClickJoinListener(ItemClickListener<App_family_listItemActModel> clickJoinListener) {
        this.clickJoinListener = clickJoinListener;
    }

    public LiveFamilysListAdapter(List<App_family_listItemActModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_familys_list;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final App_family_listItemActModel model)
    {
        ImageView civ_head_image= ViewHolder.get(R.id.civ_head_image, convertView);//家族头像
        TextView txv_fam_nick = ViewHolder.get(R.id.txv_fam_nick, convertView);//家族名称
        TextView txv_fam_name = ViewHolder.get(R.id.txv_fam_name, convertView);//族长
        TextView txv_fam_num = ViewHolder.get(R.id.txv_fam_num, convertView);//人数
        final TextView txv_add_fam = ViewHolder.get(R.id.txv_add_fam, convertView);//加入家族/申请中

        SDViewBinder.setImageView(getActivity(),model.getFamily_logo(), civ_head_image);
        SDViewBinder.setTextView(txv_fam_nick, model.getFamily_name());
        SDViewBinder.setTextView(txv_fam_name, model.getNick_name());
        SDViewBinder.setTextView(txv_fam_num, model.getUser_count() + "");

        if (model.getIs_apply() == 1)//是否已经提交申请，1：已提交、0：未提交
        {
            SDViewBinder.setTextView(txv_add_fam, "申请中");
            txv_add_fam.setBackgroundResource(R.drawable.bg_grey_rectangle_radius);
            txv_add_fam.setEnabled(false);
        }else
        {
            SDViewBinder.setTextView(txv_add_fam, "加入家族");
            txv_add_fam.setBackgroundResource(R.drawable.bg_orange_rectangle_radius);
            txv_add_fam.setEnabled(true);
        }

        /**
         * 加入家族
         */
        txv_add_fam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (clickJoinListener != null)
                {
                    clickJoinListener.onClick(position, model, view);

                    SDViewBinder.setTextView(txv_add_fam, "申请中");
                    txv_add_fam.setBackgroundResource(R.drawable.bg_grey_rectangle_radius);
                    txv_add_fam.setEnabled(false);
                }
            }
        });
    }
}
