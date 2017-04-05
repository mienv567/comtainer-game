package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
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

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午2:14:28 类说明
 */
public class LiveSerchUserModelAdapter extends SDSimpleAdapter<UserModel>
{
    private UserModel user = UserModelDao.query();

    public LiveSerchUserModelAdapter(List<UserModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_search_focus_follow;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final UserModel model)
    {
        ImageView civ_v_icon = ViewHolder.get(R.id.civ_v_icon, convertView);
        SDViewUtil.hide(civ_v_icon);

        ImageView civ_head_image = ViewHolder.get(R.id.civ_head_image, convertView);
        TextView tv_nick_name = ViewHolder.get(R.id.tv_nick_name, convertView);
        TextView tv_signature = ViewHolder.get(R.id.tv_signature, convertView);
//        ImageView iv_global_male = ViewHolder.get(R.id.iv_global_male, convertView);
//        ImageView iv_rank = ViewHolder.get(R.id.iv_rank, convertView);

        final ImageView iv_focus = ViewHolder.get(R.id.cb_focus, convertView);

        if (user.getUserId().equals(model.getUserId()))
        {
            SDViewUtil.hide(iv_focus);
        } else
        {
            SDViewUtil.show(iv_focus);
        }


        SDViewBinder.setImageView(getActivity(),model.getHeadImage(), civ_head_image,R.drawable.ic_default_head);
        SDViewBinder.setTextView(tv_nick_name, model.getNickName());
        SDViewBinder.setTextView(tv_signature, model.getSignature());
//        if (model.getSex() > 0)
//        {
//            SDViewUtil.show(iv_global_male);
//            iv_global_male.setImageResource(model.getSexResId());
//        } else
//        {
//            SDViewUtil.hide(iv_global_male);
//        }
//        iv_rank.setImageResource(model.getLevelImageResId());

        if (model.getFollowId() > 0)
        {
            iv_focus.setSelected(true);
        } else
        {
            iv_focus.setSelected(false);
        }

        if (user != null)
        {
            iv_focus.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    clickIvFocus(model, iv_focus,position);
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

    private void clickIvFocus(UserModel model, final ImageView iv_focus, final int position)
    {
        // 本地默认选中和不选中
//        if (iv_focus.isSelected())
//        {
//            iv_focus.setSelected(false);
//        } else
//        {
//            iv_focus.setSelected(true);
//        }

        CommonInterface.requestFollow(model.getUserId(), model, new AppRequestCallback<App_followActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.isOk())
                {
                    if (actModel.getRelationship() == 1)
                    {
                        iv_focus.setSelected(true);
                    } else
                    {
                        iv_focus.setSelected(false);
                    }
                    getData().get(position).setFollowId(actModel.getRelationship());
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
