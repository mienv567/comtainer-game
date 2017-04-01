package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * @author ���� E-mail:
 * @version ����ʱ�䣺2016-5-13 ����8:28:59 ��˵��
 */
public class LiveViewerListAdapter extends SDSimpleAdapter<UserModel>
{

	public LiveViewerListAdapter(List<UserModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_live_user;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void bindData(final int position, View convertView, ViewGroup parent, final UserModel model)
	{
		ImageView im = ViewHolder.get(R.id.iv_pic, convertView);
		ImageView iv_level = ViewHolder.get(R.id.iv_level, convertView);

		if (!TextUtils.isEmpty(model.getV_icon()))
		{
			SDViewUtil.show(iv_level);
			SDViewBinder.setImageView(getActivity(),model.getV_icon(), iv_level);
		} else
		{
			SDViewUtil.hide(iv_level);
		}

		SDViewBinder.setImageView(getActivity(),model.getHeadImage(), im,R.drawable.ic_default_head);
		// convertView.setOnClickListener(new View.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// clickHeadImage(model.getUserId());
		// }
		// });
	}

	protected void clickHeadImage(String to_userid)
	{
		// 显示用户信息窗口
		LiveUserInfoDialog dialog = new LiveUserInfoDialog(mActivity, to_userid);
		dialog.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss(DialogInterface dialog)
			{

			}
		});
		dialog.showCenter();
	}

}
