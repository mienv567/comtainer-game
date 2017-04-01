package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.App_user_red_envelopeModel;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-28 下午3:23:32 类说明
 */
public class LiveRedEnvelopeAdapter extends SDSimpleAdapter<App_user_red_envelopeModel>
{

    public LiveRedEnvelopeAdapter(List<App_user_red_envelopeModel> listModel, Activity activity)
    {
        super(listModel, activity);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        return R.layout.item_live_red_envelope;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, App_user_red_envelopeModel model)
    {
        ImageView iv_head = get(R.id.iv_head, convertView);
        TextView tv_nick_name = get(R.id.tv_nick_name, convertView);
        TextView tv_diamonds = get(R.id.tv_diamonds, convertView);
        ImageView iv_sex = get(R.id.iv_sex, convertView);
        LinearLayout ll_best = get(R.id.ll_best, convertView);

        SDViewBinder.setImageView(getActivity(),model.getHead_image(), iv_head,R.drawable.ic_default_head);
        SDViewBinder.setTextView(tv_nick_name, model.getNick_name());
        SDViewBinder.setTextView(tv_diamonds, model.getDiamonds() + "");

        if (model.getSex() == 2)
        {
            iv_sex.setImageResource(R.drawable.ic_global_female);
        } else if (model.getSex() == 1)
        {
            iv_sex.setImageResource(R.drawable.ic_global_male);
        } else
        {
            SDViewUtil.hide(iv_sex);
        }

        if (position > 0)
        {
            // 隐藏手气最佳
            SDViewUtil.hide(ll_best);
        } else
        {
            SDViewUtil.show(ll_best);
        }
    }

}
