package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.fanwe.live.IMHelper;
import com.fanwe.live.model.custommsg.CustomMsgInviteVideo;
import com.fanwe.live.R;

public class LiveInviteVideoDialog extends LiveBaseDialog
{

	public TextView tv_info;
	private View ll_close;

	public LiveInviteVideoDialog(Activity activity)
	{
		super(activity);
		init();
	}

	private void init()
	{
		setContentView(R.layout.dialog_live_invite_video);

		tv_info = (TextView) getContentView().findViewById(R.id.tv_info);
		ll_close = getContentView().findViewById(R.id.ll_close);
		register();

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
	}

	@Override
	public void show()
	{
		IMHelper.sendMsgC2C(getLiveInfo().getCreaterId(), new CustomMsgInviteVideo(), null);
		super.show();
	}
}
