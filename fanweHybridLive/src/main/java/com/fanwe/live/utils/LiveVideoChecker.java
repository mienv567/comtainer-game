package com.fanwe.live.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.looper.impl.SDWaitRunner;
import com.fanwe.library.span.utils.SDPatternUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.Video_check_statusActModel;

import java.util.List;

/**
 * Created by L on 2016/8/28.
 */
public class LiveVideoChecker
{
    private Activity activity;
    private int roomId;
    private String strPrivateKey;

    public LiveVideoChecker(Activity activity)
    {
        this.activity = activity;
    }

    public void check(int roomId)
    {
        if (roomId > 0)
        {
            check(roomId, null);
        }
    }

    public void check(String copyContent)
    {
        if (!TextUtils.isEmpty(copyContent))
        {
            check(0, copyContent);
        }
    }

    private void check(int roomId, String copyContent)
    {
        if (activity == null)
        {
            return;
        }

        String tag = LiveConstant.LIVE_PRIVATE_KEY_TAG;
        if (copyContent != null)
        {
            List<Integer> listPosition = SDPatternUtil.findPosition(copyContent, tag);
            if (listPosition != null && listPosition.size() == 2)
            {
                String key = copyContent.substring(copyContent.indexOf(tag) + tag.length(), copyContent.lastIndexOf(tag));
                if (!TextUtils.isEmpty(key))
                {
                    this.strPrivateKey = key;
                    SDOtherUtil.copyText("");
                }
            }
        }

        if (LiveInformation.getInstance().isCreater())
        {
            return;
        }

        this.roomId = roomId;

        new SDWaitRunner().run(new Runnable()
        {

            @Override
            public void run()
            {
                tryCheck();
            }
        }).condition(new SDWaitRunner.RunnableCondition()
        {

            @Override
            public boolean canRun()
            {
                return AppRuntimeWorker.isContextStarted();
            }
        }).startWait();
    }

    protected void tryCheck()
    {
        CommonInterface.requestCheckVideoStatus(roomId, strPrivateKey, new AppRequestCallback<Video_check_statusActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    showCheckDialog(actModel);
                }
            }
        });
    }

    private void showCheckDialog(final Video_check_statusActModel actModel)
    {
        SDDialogConfirm dialog = new SDDialogConfirm(activity);
        dialog.setTextContent(actModel.getContent()).setTextCancel("取消");
        if (actModel.getLive_in() == 1)
        {
            dialog.setTextConfirm("立即查看");
        } else
        {
            dialog.setTextConfirm("立即前往");
        }
        dialog.setmListener(new SDDialogCustom.SDDialogCustomListener()
        {
            @Override
            public void onClickCancel(View v, SDDialogCustom dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogCustom dialog)
            {
                clickConfirm(actModel);
            }

            @Override
            public void onDismiss(SDDialogCustom dialog)
            {
            }
        }).show();
    }

    protected void clickConfirm(Video_check_statusActModel actModel)
    {
        if (actModel.getLive_in() == 1)
        {
            // TODO 跳到直播页面
            JoinLiveData data = new JoinLiveData();
            data.setRoomId(actModel.getRoom_id());
            data.setGroupId(actModel.getGroup_id());
            data.setCreaterId(actModel.getUser_id());
            data.setLoadingVideoImageUrl(actModel.getHead_image());
            data.setPrivateKey(strPrivateKey);
            AppRuntimeWorker.joinLive(data, activity,false);
        } else
        {
            // TODO 跳到用户首页
            Intent intent = new Intent(activity, LiveUserHomeActivity.class);
            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, actModel.getUser_id());
            activity.startActivity(intent);
        }
    }


}
