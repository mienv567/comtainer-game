package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.utils.LiveUtils;
import com.fanwe.live.R;
import com.fanwe.live.model.App_ContModel;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-28 下午3:23:32 类说明
 */
public class LiveContAdapter extends SDSimpleAdapter<App_ContModel>
{

    public LiveContAdapter(List<App_ContModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (position)
        {
            case 0:
                return position;
            case 1:
                return position;
            case 2:
                return position;
            default:
                return 3;
        }

    }

    @Override
    public int getViewTypeCount()
    {
        return 4;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        int layout;
        if (position == 0)
        {
            layout = R.layout.item_cont_first;
        } else if (position == 1)
        {
            layout = R.layout.item_cont_second;
        } else if (position == 2)
        {
            layout = R.layout.item_cont_third;
        } else
        {
            layout = R.layout.item_cont;
        }
        return layout;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, App_ContModel model)
    {
        switch (getItemViewType(position))
        {
            case 0:
                bindFirstData(position, convertView, parent, model);
                break;
            case 1:
                bindSecondData(position, convertView, parent, model);
                break;
            case 2:
                bindThirdData(position, convertView, parent, model);
                break;
            default:
                binddefaultData(position, convertView, parent, model);
                break;
        }
    }

    private void bindFirstData(int position, View convertView, ViewGroup parent, App_ContModel model)
    {
        ImageView civ_head_first = get(R.id.civ_head_first, convertView);
        ImageView civ_level_first = get(R.id.civ_level_first, convertView);
        TextView tv_nickname_first = get(R.id.tv_nickname_first, convertView);
        ImageView iv_global_male_first = get(R.id.iv_global_male_first, convertView);
        ImageView iv_rank_first = get(R.id.iv_rank_first, convertView);
        TextView tv_num_first = get(R.id.tv_num_first, convertView);

        SDViewBinder.setImageView(getActivity(),model.getHead_image(), civ_head_first,R.drawable.ic_default_head);

        if (TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.hide(civ_level_first);
        } else
        {
            SDViewUtil.show(civ_level_first);
            SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_level_first);
        }

        SDViewBinder.setTextView(tv_nickname_first, model.getNick_name(), SDResourcesUtil.getString(R.string.not_set_nick_name));
        if (model.getSex() == 1)
        {
            iv_global_male_first.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2)
        {
            iv_global_male_first.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.hide(iv_global_male_first);
        }
        iv_rank_first.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tv_num_first, model.getTotal_diamonds() + "");
    }

    private void bindSecondData(int position, View convertView, ViewGroup parent, App_ContModel model)
    {
        ImageView civ_head_second = get(R.id.civ_head_second, convertView);
        ImageView civ_level_second = get(R.id.civ_level_second, convertView);
        TextView tv_nickname_second = get(R.id.tv_nickname_second, convertView);
        ImageView iv_global_male_second = get(R.id.iv_global_male_second, convertView);
        ImageView iv_rank_second = get(R.id.iv_rank_second, convertView);
        TextView tv_num_second = get(R.id.tv_num_second, convertView);

        SDViewBinder.setImageView(getActivity(),model.getHead_image(), civ_head_second,R.drawable.ic_default_head);

        if (TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.hide(civ_level_second);
        } else
        {
            SDViewUtil.show(civ_level_second);
            SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_level_second);
        }

        SDViewBinder.setTextView(tv_nickname_second, model.getNick_name(), SDResourcesUtil.getString(R.string.not_set_nick_name));
        if (model.getSex() == 1)
        {
            iv_global_male_second.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2)
        {
            iv_global_male_second.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.hide(iv_global_male_second);
        }
        iv_rank_second.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tv_num_second, model.getTotal_diamonds() + "");
    }

    private void bindThirdData(int position, View convertView, ViewGroup parent, App_ContModel model)
    {
        ImageView civ_head_third = get(R.id.civ_head_third, convertView);
        ImageView civ_level_third = get(R.id.civ_level_third, convertView);
        TextView tv_nickname_third = get(R.id.tv_nickname_third, convertView);
        ImageView iv_global_male_third = get(R.id.iv_global_male_third, convertView);
        ImageView iv_rank_third = get(R.id.iv_rank_third, convertView);
        TextView tv_num_third = get(R.id.tv_num_third, convertView);

        SDViewBinder.setImageView(getActivity(),model.getHead_image(), civ_head_third,R.drawable.ic_default_head);

        if (TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.hide(civ_level_third);
        } else
        {
            SDViewUtil.show(civ_level_third);
            SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_level_third);
        }

        SDViewBinder.setTextView(tv_nickname_third, model.getNick_name(), SDResourcesUtil.getString(R.string.not_set_nick_name));
        if (model.getSex() == 1)
        {
            iv_global_male_third.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2)
        {
            iv_global_male_third.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.hide(iv_global_male_third);
        }
        iv_rank_third.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tv_num_third, model.getTotal_diamonds() + "");

    }

    private void binddefaultData(int position, View convertView, ViewGroup parent, App_ContModel model)
    {
        TextView tv_position_other = get(R.id.tv_position_other, convertView);
        ImageView civ_head_other = get(R.id.civ_head_other, convertView);
        ImageView civ_level_other = get(R.id.civ_level_other, convertView);
        TextView tv_nickname_other = get(R.id.tv_nickname_other, convertView);
        ImageView iv_global_male_other = get(R.id.iv_global_male_other, convertView);
        ImageView iv_rank_other = get(R.id.iv_rank_other, convertView);
        TextView tv_num_other = get(R.id.tv_num_other, convertView);

        int number = position + 1;
        SDViewBinder.setTextView(tv_position_other, number + "");
        SDViewBinder.setImageView(getActivity(),model.getHead_image(), civ_head_other,R.drawable.ic_default_head);

        if (TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.hide(civ_level_other);
        } else
        {
            SDViewUtil.show(civ_level_other);
            SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_level_other);
        }

        SDViewBinder.setTextView(tv_nickname_other, model.getNick_name(), SDResourcesUtil.getString(R.string.not_set_nick_name));
        if (model.getSex() == 1)
        {
            iv_global_male_other.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2)
        {
            iv_global_male_other.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.hide(iv_global_male_other);
        }
        iv_rank_other.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tv_num_other, model.getTotal_diamonds() + "");
    }
}
