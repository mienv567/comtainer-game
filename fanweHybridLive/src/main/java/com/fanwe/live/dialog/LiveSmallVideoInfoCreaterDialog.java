package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EOnClickSmallVideo;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgCreaterStopVideo;

public class LiveSmallVideoInfoCreaterDialog extends LiveBaseDialog
{

	private ImageView iv_head;
	private TextView tv_nickname;
	private View ll_close;
	private View view_close_video;

	private EOnClickSmallVideo event;

	public LiveSmallVideoInfoCreaterDialog(Activity activity, EOnClickSmallVideo event)
	{
		super(activity);
		this.event = event;
		init();
	}

	private void init()
	{
		setContentView(R.layout.dialog_live_small_video_info_creater);

		iv_head = (ImageView) getContentView().findViewById(R.id.iv_head);
		tv_nickname = (TextView) getContentView().findViewById(R.id.tv_nickname);
		ll_close = getContentView().findViewById(R.id.ll_close);
		view_close_video = getContentView().findViewById(R.id.view_close_video);

		register();
		requestData();
	}

	private void register()
	{
		ll_close.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		view_close_video.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				IMHelper.sendMsgC2C(event.identifier, new CustomMsgCreaterStopVideo(), null);
//				QavsdkControl.getInstance().setRemoteHasVideo(false, event.identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
				dismiss();
			}
		});
	}


	private void requestData()
	{
		CommonInterface.requestUserInfoJava(getLiveInfo().getRoomId(), event.identifier, new AppRequestCallback<UserModel>()
		{
			@Override
			protected void onSuccess(SDResponse resp)
			{
				if (rootModel.getStatus() == 1)
				{
					bindData(actModel);
				}
			}
		});
	}

	protected void bindData(UserModel actModel)
	{
		SDViewBinder.setImageView(getContext(),actModel.getHeadImage(), iv_head,R.drawable.ic_default_head);
		SDViewBinder.setTextView(tv_nickname, actModel.getNickName());
	}

}
