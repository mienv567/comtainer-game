package com.fanwe.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveAdminActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-26 下午6:16:49 类说明
 */
public class LiveUserManageDialog extends LiveBaseDialog
{

    public final static int TYPE_ONE = 0;
    public final static int TYPE_TWO = 1;
    @ViewInject(R.id.rl_root_layout)
    private LinearLayout rl_root_layout;
    @ViewInject(R.id.tv_tip_off)
    private TextView tv_tip_off;
    @ViewInject(R.id.tv_setting_manager)
    private TextView tv_setting_manager;
    @ViewInject(R.id.tv_manager_list)
    private TextView tv_manager_list;
    @ViewInject(R.id.tv_forbid_speak)
    private TextView tv_forbid_speak;
    @ViewInject(R.id.tv_cancel)
    private TextView tv_cancel;
    private LiveUserInfoDialog mDialog;
    public LiveUserManageDialog(Activity activity, LiveUserInfoDialog dialog,int type,int has_admin)
    {
        super(activity);
        mDialog = dialog;
        init(type, has_admin);
    }

    private void init(int type,int has_admin) {
        setContentView(R.layout.dialog_user_manage);
        setCanceledOnTouchOutside(true);
        x.view().inject(this, getContentView());
        register();
        getContentView().setAlpha(0.78f);
        showViews(type,has_admin);
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
    }

    private void showViews(int type,int has_admin){
        SDViewUtil.show(tv_cancel);
        if(type == TYPE_ONE){
            SDViewUtil.setViewHeight(rl_root_layout, SDViewUtil.dp2px(110));
            SDViewUtil.show(tv_tip_off);
            SDViewUtil.show(tv_forbid_speak);
            SDViewUtil.hide(tv_setting_manager);
            SDViewUtil.hide(tv_manager_list);
        }else if(type == TYPE_TWO){
            SDViewUtil.setViewHeight(rl_root_layout, SDViewUtil.dp2px(160));
            SDViewUtil.hide(tv_tip_off);
            SDViewUtil.show(tv_forbid_speak);
            SDViewUtil.show(tv_setting_manager);
            SDViewUtil.show(tv_manager_list);
            if(has_admin == 1){
                SDViewBinder.setTextView(tv_setting_manager, SDResourcesUtil.getString(R.string.cancel_admin));
            }else{
                SDViewBinder.setTextView(tv_setting_manager,SDResourcesUtil.getString(R.string.setting_admin));
            }
        }
    }

    private void register()
    {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                dismiss();
            }
        });
        tv_forbid_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                dismiss();
                if(mDialog != null){
                    mDialog.requestforbid_send_msg(LiveUserInfoDialog.NO_TALKING_TIME);
                }
            }
        });
        tv_tip_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                dismiss();
                if(mDialog != null){
                    mDialog.showTipoff_typeDialog();
                }
            }
        });
        tv_setting_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                dismiss();
                if(mDialog != null){
                    mDialog.requestset_admin();
                }
            }
        });
        tv_manager_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                dismiss();
                Intent intent = new Intent(getOwnerActivity(), LiveAdminActivity.class);
                getOwnerActivity().startActivity(intent);
            }
        });
    }

}
