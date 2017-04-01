package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.live.R;
import com.fanwe.live.activity.info.LiveInfo;
import com.fanwe.live.event.EExitRoomComplete;

public class LiveBaseDialog extends SDDialogBase
{
	private LiveInfo liveInfo;

	public LiveBaseDialog(Activity activity)
	{
		super(activity, R.style.dialogBase);
	}

	public LiveInfo getLiveInfo()
	{
		if (liveInfo == null)
		{
			if (getOwnerActivity() instanceof LiveInfo)
			{
				liveInfo = (LiveInfo) getOwnerActivity();
			}
		}
		return liveInfo;
	}

	public void onEventMainThread(EExitRoomComplete event)
	{
		dismiss();
	}

}
