package com.fanwe.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.event.EGoToHotTab;
import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveCreaterAgreementActivity;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.UserInviteActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserTaskDetailDialog extends SDDialogBase
{

    @ViewInject(R.id.iv_close)
    private ImageView iv_close; //关闭

    @ViewInject(R.id.tv_go_to_work)
    private TextView tv_go_to_work;//去完成

    @ViewInject(R.id.tv_detail)
    private TextView tv_detail;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    private UserTaskItem mItem;
    private Activity mActivity;

    public UserTaskDetailDialog(Activity activity, UserTaskItem item)
    {
        super(activity);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        mActivity = activity;
        mItem = item;
        init();
    }


    private void init()
    {
        setContentView(R.layout.dialog_task_detail);
        setCanceledOnTouchOutside(true);
        paddingLeft(SDViewUtil.dp2px(40));
        paddingRight(SDViewUtil.dp2px(40));
        x.view().inject(this, getContentView());
        register();
        bindData();
    }

    private void register()
    {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_go_to_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWork();
            }
        });
    }

    private void goToWork(){
        if(mItem != null){
            Intent intent ;
            switch (mItem.getTask_to_do()){
                case UserTaskItem.GO_TO_MAIN:
                    intent = new Intent(mActivity, LiveMainActivity.class);
                    mActivity.startActivity(intent);
                    SDEventManager.post(new EGoToHotTab());
                    break;
                case UserTaskItem.GO_TO_PODCAST:
                    final UserModel userModel = UserModelDao.query();
                    if (userModel.getIsAgree() == 1) {
                        intent = new Intent(mActivity, LiveCreateRoomActivity.class);
                        mActivity.startActivity(intent);
                    } else {
                        intent = new Intent(mActivity, LiveCreaterAgreementActivity.class);
                        mActivity.startActivity(intent);
                    }
                    break;
                case UserTaskItem.GO_TO_INVITE:
                    intent = new Intent(mActivity, UserInviteActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case UserTaskItem.GO_TO_RECHARGE:
                    intent = new Intent(mActivity, LiveRechargeActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case UserTaskItem.GO_TO_ME:
                    intent = new Intent(mActivity, LiveUserEditActivity.class);
                    mActivity.startActivity(intent);
                    break;
            }
            dismiss();
        }
    }

    private void bindData()
    {
        String detail = mItem.getTask_detail();
        String title = mItem.getTask_title();
        if(!TextUtils.isEmpty(detail)){
            tv_detail.setText(detail);
        }
        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
    }

}
