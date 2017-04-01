package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserInviteModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 输入邀请码
 */
public class UserInviteDialog extends SDDialogBase {

    @ViewInject(R.id.lr_root_view)
    private RelativeLayout lr_root_view;
    @ViewInject(R.id.iv_close)
    private ImageView iv_close; //关闭
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;//确定
    @ViewInject(R.id.et_input_invite)
    private EditText et_input_invite;
    private String mSelfCode;
    public boolean mBindSuccess;
    public UserInviteDialog(Activity activity, String selfCode) {
        super(activity);
        getWindow().setDimAmount(0);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        mSelfCode = selfCode;
        init();
    }


    private void init() {
        setContentView(R.layout.dialog_invite);
        setCanceledOnTouchOutside(false);
        paddingLeft(SDViewUtil.dp2px(20));
        paddingRight(SDViewUtil.dp2px(20));
        x.view().inject(this, getContentView());
        lr_root_view.setAlpha(0.9f);
        register();
    }

    private void register() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCode = et_input_invite.getText()+"".trim().replaceAll(" ", "");
                if(TextUtils.isEmpty(inputCode)){
                    SDToast.showToast(SDResourcesUtil.getString(R.string.please_input_invite_code));
                }else if(inputCode.equals(mSelfCode)) {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.can_use_self_invite_code));
                }else{
                    CommonInterface.submitUserInvite(inputCode, new AppRequestCallback<UserInviteModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (actModel.getStatus() == 1) {
                                SDToast.showToast(SDResourcesUtil.getString(R.string.bind_success));
                                mBindSuccess = true;
                            } else {
                                SDToast.showToast(actModel.getError());
                                mBindSuccess = false;
                            }
                            dismiss();
                        }
                    });
                }
            }
        });
    }



}
