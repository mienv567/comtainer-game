package com.fanwe.live.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.dialog.SDDialogMenu.SDDialogMenuListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveAdminActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.activity.info.LiveInfo;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_forbid_send_msgActModel;
import com.fanwe.live.model.App_set_adminActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgLiveMsg;
import com.fanwe.live.pop.OpenPushTipsPop;
import com.fanwe.live.utils.LiveUtils;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-25 下午3:12:49 类说明
 */
public class LiveUserInfoDialog extends SDDialogBase {
    public static final int NO_TALKING_TIME = 120;// 禁言秒数

    @ViewInject(R.id.ll_root_view)
    private LinearLayout ll_root_view;
    @ViewInject(R.id.btn_tipoff)
    private TextView btn_tipoff;// 举报
    private LinearLayout ll_tipoff;// 举报layout
    @ViewInject(R.id.iv_tipoff)
    private ImageView iv_tipoff;
    @ViewInject(R.id.tv_follow_l)
    private TextView tv_follow_l; // 1.4调整布局后的关注
    @ViewInject(R.id.ll_follow)
    private LinearLayout ll_follow;
    private LinearLayout ll_focus;
    private ImageView img_push;
    private View division;
    @ViewInject(R.id.ll_close)
    private View ll_close;// 关闭

    @ViewInject(R.id.iv_head)
    private ImageView iv_head;// 头像

    @ViewInject(R.id.iv_level)
    private ImageView iv_level;//认证图标

    @ViewInject(R.id.iv_pic)
    private ImageView iv_pic;// 贡献最多

    @ViewInject(R.id.ll_pic)
    private LinearLayout ll_pic;// 贡献最多

    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;// 用户名
    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id; // 麻辣号
    @ViewInject(R.id.iv_global_male)
    private ImageView iv_global_male;// 性别

    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;// 等级

    @ViewInject(R.id.tv_number)
    private TextView tv_number;// 帐号

    @ViewInject(R.id.tv_city)
    private TextView tv_city;// 所在城市

    @ViewInject(R.id.ll_v_explain)
    private LinearLayout ll_v_explain;// 认证等级

    //    @ViewInject(R.id.tv_level_name)
    //    private TextView tv_level_name;// 认证等级

    @ViewInject(R.id.tv_introduce)
    private TextView tv_introduce;// 个人简介

    @ViewInject(R.id.tv_follow)
    private TextView tv_follow;// 关注人数

    @ViewInject(R.id.tv_fans)
    private TextView tv_fans;// 粉丝人数

    @ViewInject(R.id.tv_brick)
    private TextView tv_brick;// 黄砖数

    @ViewInject(R.id.tv_ticket)
    private TextView tv_ticket;// 钱票数

    @ViewInject(R.id.ll_btn)
    private LinearLayout ll_btn;// 按钮组

    //    @ViewInject(R.id.btn_follow)
    //    private Button btn_follow;

    @ViewInject(R.id.ll_letter)
    private LinearLayout ll_letter;
    //    @ViewInject(R.id.btn_reply)
    //    private Button btn_reply;

    @ViewInject(R.id.ll_main_page)
    private LinearLayout ll_main_page;
    private String to_user_id;
    private int isManage = 0;

    private String db_user_id;

    private String identifierCreater_id;

    private String group_id;

    private UserModel app_userinfoActModel;
    private LiveInfo liveInfo;
    OpenPushTipsPop mPopWindow;
    private static Handler mShowHandler = new Handler();

    public LiveUserInfoDialog(Activity activity, String to_user_id) {
        super(activity);
        init_id(to_user_id);
        init();
    }

    public LiveUserInfoDialog(Activity activity, String to_user_id, int isManage) {
        super(activity);
        init_id(to_user_id);
        this.isManage = isManage;
        init();
    }

    private void init_id(String user_id) {
        this.to_user_id = user_id;
        UserModel user = UserModelDao.query();
        if (user != null) {
            this.db_user_id = user.getUserId();
        } else {
            //            SDToast.showToast("未找到本地用户信息");
        }
        identifierCreater_id = getLiveInfo().getCreaterId();
        if (TextUtils.isEmpty(identifierCreater_id)) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.anchor_id_empty));
        }
        group_id = getLiveInfo().getGroupId();
        if (TextUtils.isEmpty(group_id)) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.im_room_id_empty));
        }
    }

    public LiveInfo getLiveInfo() {
        if (liveInfo == null) {
            if (getOwnerActivity() instanceof LiveInfo) {
                liveInfo = (LiveInfo) getOwnerActivity();
            }
        }
        return liveInfo;
    }

    private void init() {
        // 点击头像id和本地登录ID一样
        if (!TextUtils.isEmpty(to_user_id) && to_user_id.equals(db_user_id)) {
            // 点击头像id和主播ID一样
            if (to_user_id.equals(identifierCreater_id)) { // 主播自己看自己账号信息
                setContentView(R.layout.dialog_user_info_page_two);
                ll_focus = (LinearLayout) findViewById(R.id.ll_focus);
                SDViewUtil.hide(ll_focus);
            } else { //
                setContentView(R.layout.dialog_user_info_page_three);
                ll_focus = (LinearLayout) findViewById(R.id.ll_focus);
                SDViewUtil.hide(ll_focus);
            }
        } else { // 观众看其他人的信息
            setContentView(R.layout.dialog_user_info_page_one);
            ll_focus = (LinearLayout) findViewById(R.id.ll_focus);
            //            SDViewUtil.show(ll_focus);
        }
        ll_tipoff = (LinearLayout) findViewById(R.id.ll_tipoff);
        img_push = (ImageView) findViewById(R.id.img_push);
        division = findViewById(R.id.division);
        mPopWindow = new OpenPushTipsPop(getOwnerActivity());
        setCanceledOnTouchOutside(true);
        paddingLeft(SDViewUtil.dp2px(40));
        paddingRight(SDViewUtil.dp2px(40));
        x.view().inject(this, getContentView());
        ll_root_view.setAlpha(0.7f);
        register();
        requestUserInfo();
    }

    private void register() {
        iv_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startMainPage();
            }
        });

        if (null != ll_tipoff) {
            ll_tipoff.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (app_userinfoActModel != null) {
                        //                        if ((app_userinfoActModel.getCanTipoff()) == 1) {
                        //                            showTipoff_typeDialog();
                        //                        } else if (app_userinfoActModel.getIsManage()!=0) {
                        //                            showManageDialog(app_userinfoActModel.getIsManage(), app_userinfoActModel.getIsManage());
                        //                        }

                        /*if ((app_userinfoActModel.getCanTipoff()) == 0) {
                            showTipoff_typeDialog();
                        } else if (app_userinfoActModel.getIsManage() != 1) {
                            showManageDialog(2, 1);
                        }*/
                        if (db_user_id.equals(identifierCreater_id) && !to_user_id.equals(db_user_id)) {
                            showManageDialog(2, app_userinfoActModel.getIsManage());
                        } else if (isManage == 1 && !to_user_id.equals(identifierCreater_id)) {
                            showManageDialog(1, app_userinfoActModel.getIsManage());
                        } else {
                            showTipoff_typeDialog();
                        }
                    }
                }

                private void showManageDialog(int show_admin, int has_admin) {
                    if (show_admin == 1) {
                        LiveUserManageDialog dialog = new LiveUserManageDialog(getOwnerActivity(), LiveUserInfoDialog.this, LiveUserManageDialog.TYPE_ONE, has_admin);
                        dialog.showBottom();
                        //                        open_show_admin_1();
                    } else if (show_admin == 2) {
                        LiveUserManageDialog dialog = new LiveUserManageDialog(getOwnerActivity(), LiveUserInfoDialog.this, LiveUserManageDialog.TYPE_TWO, has_admin);
                        dialog.showBottom();
                        //                        open_show_admin_2(has_admin);
                    }
                }
            });
        }

        ll_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_follow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestFollow();
            }
        });

        if (null != img_push) {
            img_push.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPush();
                }
            });
        }

        ll_letter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LivePrivateChatDialog dialog = new LivePrivateChatDialog(getOwnerActivity(), to_user_id);
                dialog.showBottom();
                dismiss();
            }
        });

        //        btn_reply.setOnClickListener(new View.OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //                if (app_userinfoActModel != null) {
        //                    UserModel user = app_userinfoActModel.getUser();
        //                    if (user != null) {
        //                        String at = "@" + user.getNickName() + "　";
        //                        getLiveInfo().openSendMsg(at);
        //                        dismiss();
        //                    }
        //                }
        //            }
        //        });

        ll_main_page.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startMainPage();
            }
        });
    }

    private void startMainPage() {
        if (app_userinfoActModel != null) {
            UserModel user = app_userinfoActModel;
            if (user != null) {
                Intent intent = new Intent(getOwnerActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUserId());
                getOwnerActivity().startActivity(intent);
                dismiss();
            }
        }
    }

    private void bindData(UserModel to_user) {
        //        UserModel to_user = actModel.getUser();
        if (to_user != null) {
            if (TextUtils.isEmpty(to_user.getUserId())) {
                dealErrorInfo();
            } else if (!to_user.getUserId().equals(to_user_id)) {
                dealErrorInfo();
            }


            if (!TextUtils.isEmpty(to_user.getV_explain())) {
                SDViewUtil.show(ll_v_explain);
                //                SDViewBinder.setTextView(tv_level_name, to_user.getV_explain());
            } else {
                SDViewUtil.hide(ll_v_explain);
            }

            SDViewBinder.setImageView(getContext(), to_user.getHeadImage(), iv_head, R.drawable.ic_default_head);

            if (!TextUtils.isEmpty(to_user.getV_icon())) {
                SDViewBinder.setImageView(getContext(), to_user.getV_icon(), iv_level);
            }

            String nickName = to_user.getNickName();

            SDViewBinder.setTextView(tv_nick_name, nickName, SDResourcesUtil.getString(R.string.not_set_nick_name));

            if (to_user.getSexResId() > 0) {
                iv_global_male.setImageResource(to_user.getSexResId());
            } else {
                SDViewUtil.hide(iv_global_male);
            }

            iv_rank.setImageResource(to_user.getLevelImageResId());

            SDViewBinder.setTextView(tv_number, to_user.getUserId());
            SDViewBinder.setTextView(tv_user_id, to_user.getUserId());
            SDViewBinder.setTextView(tv_city, to_user.getCity(), SDResourcesUtil.getString(R.string.live_city_default));

            // SDViewBinder.setTextView(tv_level_name, model.getIdentifier());

            SDViewBinder.setTextView(tv_introduce, to_user.getSignature(), SDResourcesUtil.getString(R.string.forget_write_signature));

            SDViewBinder.setTextView(tv_follow, to_user.getFocusCount() + "");

            SDViewBinder.setTextView(tv_fans, LiveUtils.getFormatNumber(to_user.getFansCount()));

            SDViewBinder.setTextView(tv_brick, LiveUtils.getFormatNumber(to_user.getUseDiamonds()));

            SDViewBinder.setTextView(tv_ticket, LiveUtils.getFormatNumber(to_user.getTicket()));
            setPush(to_user.getIsUnpush());
        }

        // TODO 钱票贡献第一名的用户信息暂时干掉
        // iv_pic
        //        UserModel model = actModel.getCuser();
        //        if (model != null) {
        //            if (!TextUtils.isEmpty(model.getHeadImage())) {
        //                SDViewBinder.setImageView(getContext(), model.getHeadImage(), iv_pic,R.drawable.ic_default_head);
        //                SDViewUtil.show(iv_pic);
        //            } else {
        //                SDViewUtil.hide(iv_pic);
        //            }
        //        } else {
        //            SDViewUtil.hide(iv_pic);
        //        }

        //        setBtnFollow(actModel.getRelationship());
        setTvFollow(to_user.getIsFocus());

        if (db_user_id.equals(identifierCreater_id)) {//主播
            if (to_user_id.equals(db_user_id)) {
                hideTipOff();
            } else {
                showManage();
            }
        } else if (isManage == 1) {//管理员
            if (to_user_id.equals(identifierCreater_id)) { // 管理员查看主播信息
                showTipOff();
            } else { //管理员查看其它成员信息
                showManage();
            }
        } else {//其它观众
            showTipOff();
        }/*

        if (to_user.getCanTipoff() == 1) {
            showTipOff();
        } else if (to_user.getIsManage() == 1 || to_user.getIsManage() == 2) {
            showManage();
        } else {
            hideTipOff();
        }*/

    }

    /**
     * 显示举报按钮
     */
    private void showTipOff() {
        SDViewUtil.show(ll_tipoff);
        SDViewBinder.setTextView(btn_tipoff, SDResourcesUtil.getString(R.string.tipoff));
        iv_tipoff.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_user_dialog_tipoff));
    }

    /**
     * 显示管理按钮
     */
    private void showManage() {
        SDViewUtil.show(ll_tipoff);
        iv_tipoff.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_user_dialog_manage));
        SDViewBinder.setTextView(btn_tipoff, SDResourcesUtil.getString(R.string.manage));
    }

    /**
     * 自己查看自己的资料时, 举报/管理按钮都不显示
     */
    private void hideTipOff() {
        SDViewUtil.hide(ll_tipoff);
        SDViewBinder.setTextView(btn_tipoff, "");
        ll_tipoff.setOnClickListener(null);
    }

    //    // 设置个人信息关注按钮
    //    private void setBtnFollow(int has_focus) {
    //        if (has_focus == 1) {
    //            btn_follow.setClickable(false);
    //            btn_follow.setText("已关注");
    //            btn_follow.setTextColor(SDResourcesUtil.getColor(R.color.text_gray));
    //        } else {
    //            btn_follow.setText("关注");
    //            btn_follow.setTextColor(SDResourcesUtil.getColor(R.color.main_color_second));
    //        }
    //    }

    // 设置个人信息关注按钮
    private void setTvFollow(int has_focus) {
        if (has_focus == 1) {
            ll_follow.setClickable(false);
            tv_follow_l.setText(SDResourcesUtil.getString(R.string.already_follow));
            //            img_push.setVisibility(View.VISIBLE);
            //            division.setVisibility(View.VISIBLE);
            //                        tv_focus.setTextColor(SDResourcesUtil.getColor(R.color.text_gray));
        } else {
            ll_follow.setClickable(true);
            tv_follow_l.setText(SDResourcesUtil.getString(R.string.follow));
            //            img_push.setVisibility(View.GONE);
            //            division.setVisibility(View.GONE);
            //            tv_focus.setTextColor(SDResourcesUtil.getColor(R.color.main_color_second));
        }
    }

    //设置推送开关初始化
    private void setPush(int isUnPush) {
        if (isUnPush == 0) {
            img_push.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_open_push_white));
        } else {
            img_push.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_close_push_white));
        }
    }

    // 弹出管理员权限底部框
    private void open_show_admin_1() {
        String[] arrOption = new String[]
                {SDResourcesUtil.getString(R.string.tipoff), SDResourcesUtil.getString(R.string.can_not_say)};
        List<String> listOptions = Arrays.asList(arrOption);

        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(listOptions, getOwnerActivity());

        SDDialogMenu dialog = new SDDialogMenu(getOwnerActivity());

        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenuListener() {

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0: // 举报
                        showTipoff_typeDialog();
                        break;
                    case 1: // 禁言/取消禁言
                        requestforbid_send_msg(NO_TALKING_TIME);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onDismiss(SDDialogMenu dialog) {
            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog) {
            }
        });
        dialog.showBottom();
    }

    // 弹出主播权限底部框
    private void open_show_admin_2(int has_admin) {
        String[] arrOption;
        if (has_admin == 1) {
            arrOption = new String[]
                    {SDResourcesUtil.getString(R.string.cancel_manager), SDResourcesUtil.getString(R.string.manager_list), SDResourcesUtil.getString(R.string.can_not_say)};
        } else {
            arrOption = new String[]
                    {SDResourcesUtil.getString(R.string.setting_admin), SDResourcesUtil.getString(R.string.manager_list), SDResourcesUtil.getString(R.string.can_not_say)};
        }

        List<String> listOptions = Arrays.asList(arrOption);

        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(listOptions, getOwnerActivity());

        SDDialogMenu dialog = new SDDialogMenu(getOwnerActivity());

        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenuListener() {

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog) {
                switch (index) {
                    case 0: // 设置管理员或者取消
                        requestset_admin();
                        break;
                    case 1: // 管理员列表
                        Intent intent = new Intent(getOwnerActivity(), LiveAdminActivity.class);
                        getOwnerActivity().startActivity(intent);
                        break;
                    case 2:// 禁言/取消禁言
                        requestforbid_send_msg(NO_TALKING_TIME);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onDismiss(SDDialogMenu dialog) {
            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog) {
            }
        });
        dialog.showBottom();
    }

    public void showTipoff_typeDialog() {
        LiveTipoffTypeDialog dialog = new LiveTipoffTypeDialog(getOwnerActivity(), to_user_id);
        dialog.showBottom();
    }

    private void requestUserInfo() {
        CommonInterface.requestUserInfoJava(getLiveInfo().getRoomId(), to_user_id, new AppRequestCallback<UserModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    app_userinfoActModel = actModel;
                    bindData(actModel);
                }
            }
        });
    }

    // 关注某人
    private void requestFollow() {
        CommonInterface.requestFollow(to_user_id, app_userinfoActModel, new AppRequestCallback<App_followActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    //                    setBtnFollow(actModel.getRelationship());
                    setTvFollow(actModel.getRelationship());
                }
            }
        });
    }

    private void requestPush() {
        CommonInterface.requestChangePush(to_user_id, new AppRequestCallback<BaseActModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk()) {
                    if (app_userinfoActModel.getIsUnpush() == 0) {
                        app_userinfoActModel.setIsUnpush(1);
                        img_push.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_close_push_white));
                        mPopWindow.showPopTips(SDResourcesUtil.getString(R.string.live_inform_closed), img_push, SDViewUtil.dp2px(100), SDViewUtil.dp2px(42));
                        closePopWindow();
                    } else {
                        app_userinfoActModel.setIsUnpush(0);
                        img_push.setImageDrawable(getOwnerActivity().getResources().getDrawable(R.drawable.ic_open_push_white));
                        mPopWindow.showPopTips(SDResourcesUtil.getString(R.string.live_inform_opened), img_push, SDViewUtil.dp2px(100), SDViewUtil.dp2px(42));
                        closePopWindow();
                    }
                } else {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.operate_fail));
                }
            }
        });
    }

    private void closePopWindow() {
        mShowHandler.removeCallbacksAndMessages(0);
        mShowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                }
            }
        }, 3 * 1000);
    }

    // 设置管理员
    public void requestset_admin() {
        CommonInterface.requestSet_admin(to_user_id, Math.abs(app_userinfoActModel.getIsManage() - 1), new AppRequestCallback<App_set_adminActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    final CustomMsgLiveMsg liveMsg = new CustomMsgLiveMsg();
                    String nickName = app_userinfoActModel.getNickName();

                    if (actModel.getIsManage() == 1) {
                        app_userinfoActModel.setIsManage(1);
                        SDToast.showToast(SDResourcesUtil.getString(R.string.setting_admin_success));
                        liveMsg.setDesc(nickName + " " + SDResourcesUtil.getString(R.string.be_set_manager));
                    } else {
                        app_userinfoActModel.setIsManage(0);
                        SDToast.showToast(SDResourcesUtil.getString(R.string.cancel_admin_success));
                        liveMsg.setDesc(nickName + " " + SDResourcesUtil.getString(R.string.be_cancel_manager));
                    }
                    IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), liveMsg, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            IMHelper.postMsgLocal(liveMsg, getLiveInfo().getGroupId());
                        }
                    });
                }
            }
        });
    }

    // 禁言
    public void requestforbid_send_msg(int second) {
        CommonInterface.requestForbidSendMsg(group_id, getLiveInfo().getRoomId(), to_user_id, second, new AppRequestCallback<App_forbid_send_msgActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                String nickName = app_userinfoActModel.getNickName();
                if (rootModel.getStatus() == 1) {
                    final CustomMsgLiveMsg liveMsg = new CustomMsgLiveMsg();
                    liveMsg.setDesc(nickName + " " + SDResourcesUtil.getString(R.string.is_forbidden_send_msg));
                    IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), liveMsg, new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            IMHelper.postMsgLocal(liveMsg, getLiveInfo().getGroupId());
                        }
                    });
                } else {
                    SDToast.showToast(nickName + " " + actModel.getError());
                }
            }
        });
    }

    private void dealErrorInfo() {
        SDToast.showToast(SDResourcesUtil.getString(R.string.data_exception));
        iv_head.setOnClickListener(null);
        ll_tipoff.setOnClickListener(null);
        if (null != ll_follow) {
            ll_follow.setOnClickListener(null);
        }
        //        btn_follow.setOnClickListener(null);
        ll_letter.setOnClickListener(null);
        ll_main_page.setOnClickListener(null);
        //        btn_reply.setOnClickListener(null);
    }
}
