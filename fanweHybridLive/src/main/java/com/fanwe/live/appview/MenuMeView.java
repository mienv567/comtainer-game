package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveFollowActivity;
import com.fanwe.live.activity.LiveMyFocusActivity;
import com.fanwe.live.activity.LiveMySelfContActivity;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.LiveUserHomeReplayActivity;
import com.fanwe.live.activity.LiveUserPhotoActivity;
import com.fanwe.live.activity.LiveUserProfitActivity;
import com.fanwe.live.activity.LiveUserSettingActivity;
import com.fanwe.live.activity.UserCenterAuthentActivity;
import com.fanwe.live.activity.UserCenterAuthentTransferActivity;
import com.fanwe.live.activity.UserInviteActivity;
import com.fanwe.live.activity.UserLevelActivity;
import com.fanwe.live.activity.UserSignInActivity;
import com.fanwe.live.activity.UserTaskActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

public class MenuMeView extends BaseAppView {
    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id;
    @ViewInject(R.id.fl_head)
    private FrameLayout fl_head;
    @ViewInject(R.id.ll_user_id)
    private LinearLayout ll_user_id;
    @ViewInject(R.id.rl_fl_task)
    private RelativeLayout rl_fl_task;
    @ViewInject(R.id.iv_fl_task_arrow)
    private ImageView iv_fl_task_arrow;
    @ViewInject(R.id.ll_fl_task)
    private LinearLayout ll_fl_task;
    @ViewInject(R.id.iv_head)
    private ImageView iv_head;// 头像
    @ViewInject(R.id.iv_level)
    private ImageView iv_level;// 等级
    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name; // 昵称
    @ViewInject(R.id.iv_global_male)
    private ImageView iv_global_male;// 性别
    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;// 等级
    @ViewInject(R.id.rl_remark)
    private RelativeLayout rl_remark;// 编辑
    @ViewInject(R.id.ll_my_focus)
    private LinearLayout ll_my_focus;
    @ViewInject(R.id.tv_focus_count)
    private TextView tv_focus_count;// 关注
    @ViewInject(R.id.ll_my_fans)
    private LinearLayout ll_my_fans;
    @ViewInject(R.id.tv_fans_count)
    private TextView tv_fans_count; // 粉丝
    @ViewInject(R.id.tv_introduce)
    private TextView tv_introduce;// 签名
    @ViewInject(R.id.tv_v_explain)
    private TextView tv_v_explain;
    @ViewInject(R.id.ll_v_explain)
    private LinearLayout ll_v_explain; // 认证
    @ViewInject(R.id.tv_anchor)
    private TextView tv_anchor;
    @ViewInject(R.id.ll_my_live)
    private LinearLayout ll_my_live;
    @ViewInject(R.id.tv_video_num)
    private TextView tv_video_num;// 直播
    @ViewInject(R.id.rl_level)
    private RelativeLayout rl_level;
    @ViewInject(R.id.iv_level_below)
    private ImageView iv_level_below;
    @ViewInject(R.id.rl_income)
    private RelativeLayout rl_income; //我的收益
    @ViewInject(R.id.rl_invite)
    private RelativeLayout rl_invite;//邀请有礼
    @ViewInject(R.id.rl_accout)
    private RelativeLayout rl_accout;// 账户
    @ViewInject(R.id.include_cont_linear)
    private View include_cont_linear;
    @ViewInject(R.id.ll_upgrade)
    private RelativeLayout ll_upgrade;
    @ViewInject(R.id.tv_v_type)
    private TextView tv_v_type;
    @ViewInject(R.id.ll_family)
    private RelativeLayout ll_family;
    @ViewInject(R.id.ll_setting)
    private RelativeLayout ll_setting;
    @ViewInject(R.id.rl_task)
    private RelativeLayout rl_task;
    @ViewInject(R.id.rl_sign)
    private RelativeLayout rl_sign;
    @ViewInject(R.id.red_point_sign)
    private View red_point_sign;
    @ViewInject(R.id.red_point_task)
    private View red_point_task;
    private UserModel mUserModel;
    private int is_authentication;
    private boolean mIsOpenFLAndTask;
    public MenuMeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MenuMeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuMeView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.menu_me);
        register();
    }

    private void register() {
        fl_head.setOnClickListener(this);
        rl_remark.setOnClickListener(this);
        rl_fl_task.setOnClickListener(this);
        SDViewUtil.show(ll_user_id);
        include_cont_linear.setOnClickListener(this);
        ll_my_focus.setOnClickListener(this);
        ll_my_fans.setOnClickListener(this);
        ll_my_live.setOnClickListener(this);
        rl_level.setOnClickListener(this);
        rl_income.setOnClickListener(this);
        rl_invite.setOnClickListener(this);
        rl_accout.setOnClickListener(this);
        ll_upgrade.setOnClickListener(this);
        ll_family.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        rl_sign.setOnClickListener(this);
        rl_task.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fl_head:
                clickFlHead();
                break;
            case R.id.rl_remark:
                clickIvRemark();
                break;
            case R.id.ll_my_focus:
                clickLlMyFocus();
                MobclickAgent.onEvent(getActivity(), "me_follow");
                break;
            case R.id.ll_my_fans:
                clickLlMyFans();
                MobclickAgent.onEvent(getActivity(), "me_fans");
                break;
            case R.id.include_cont_linear:
                clickIncludeContLinear();
                MobclickAgent.onEvent(getActivity(), "me_cont");
                break;
            case R.id.ll_my_live:
                clickRlVideo();
                MobclickAgent.onEvent(getActivity(), "me_zhibo");
                break;
            //            case R.id.rl_my_live:
            //                clickRlVideo();
            //                MobclickAgent.onEvent(getActivity(), "me_zhibo");
            //                break;
            case R.id.rl_level:
                clickRlLevel();
                MobclickAgent.onEvent(getActivity(), "me_level");
                break;
            case R.id.rl_income:
                clickRlIncome();
                MobclickAgent.onEvent(getActivity(), "me_income");
                break;
            case R.id.rl_invite:
                clickRlInvite();
                MobclickAgent.onEvent(getActivity(), "me_invite");
                break;
            case R.id.rl_accout:
                clickRlAccout();
                MobclickAgent.onEvent(getActivity(), "me_account");
                break;
            case R.id.ll_upgrade:
                clickLlUpgrade();
                MobclickAgent.onEvent(getActivity(), "me_auth");
                break;
            case R.id.ll_setting:
                clickSetting();
                MobclickAgent.onEvent(getActivity(), "me_setting");
                break;
            case R.id.rl_sign:
                clickSign();
                break;
            case R.id.rl_task:
                clickTask();
                break;
            case R.id.rl_fl_task:
                if(mIsOpenFLAndTask){
                    mIsOpenFLAndTask = false;
                    iv_fl_task_arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_gray));
                    SDViewUtil.hide(ll_fl_task);
                }else{
                    mIsOpenFLAndTask = true;
                    iv_fl_task_arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_gray));
                    SDViewUtil.show(ll_fl_task);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 任务
     */
    private void clickTask() {
        Intent intent = new Intent(getActivity(), UserTaskActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 签到
     */
    private void clickSign() {
        Intent intent = new Intent(getActivity(), UserSignInActivity.class);
        getActivity().startActivity(intent);
    }


    /**
     * 设置
     */
    private void clickSetting() {
        Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
        getActivity().startActivity(intent);
    }


    // 我关注的人
    private void clickLlMyFocus() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            String user_id = user.getUserId();
            if (!TextUtils.isEmpty(user_id)) {
                Intent intent = new Intent(getActivity(), LiveFollowActivity.class);
                intent.putExtra(LiveFollowActivity.EXTRA_USER_ID, user_id);
                getActivity().startActivity(intent);
            } else {
//                SDToast.showToast("本地user_id为空");
            }
        } else {

        }
    }

    // 我的粉丝
    private void clickLlMyFans() {
        Intent intent = new Intent(getActivity(), LiveMyFocusActivity.class);
        getActivity().startActivity(intent);
    }

    //认证
    private void clickLlUpgrade() {
        if (mUserModel != null) {
            //            Intent intent = new Intent(getActivity(), UserCenterAuthentActivity.class);
            //                        startActivity(intent);
            // 1.4新需求,认证前先跳转中转页
            if (is_authentication == 0) {
                Intent intent = new Intent(getActivity(), UserCenterAuthentTransferActivity.class);
                getActivity().startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), UserCenterAuthentActivity.class);
                getActivity().startActivity(intent);
            }
        }
    }

    private void clickRlAccout() {
        Intent intent = new Intent(getActivity(), LiveRechargeActivity.class);
        getActivity().startActivity(intent);
    }

    //收益
    private void clickRlIncome() {
        Intent intent = new Intent(getActivity(), LiveUserProfitActivity.class);
        getActivity().startActivity(intent);
    }

    //邀请有礼
    private void clickRlInvite() {
        Intent intent = new Intent(getActivity(), UserInviteActivity.class);
        getActivity().startActivity(intent);
    }

    //等级
    private void clickRlLevel() {
//        InitActModel initActModel = InitActModelDao.query();
//        if (initActModel != null) {
//            App_InitH5Model h5Model = initActModel.getH5_url();
//            if (h5Model != null) {
//                String url = h5Model.getUrl_my_grades();
//                Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
//                intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
//                intent.putExtra(LiveWebViewActivity.EXTRA_TITLE, "等级");
//                getActivity().startActivity(intent);
//            }
//        }
        getActivity().startActivity(new Intent(getActivity(), UserLevelActivity.class));
    }

    // 回放列表
    private void clickRlVideo() {
        Intent intent = new Intent(getActivity(), LiveUserHomeReplayActivity.class);
        getActivity().startActivity(intent);
    }

    private void clickIncludeContLinear() {
        Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
        getActivity().startActivity(intent);
    }

    //编辑
    private void clickIvRemark() {
        Intent intent = new Intent(getActivity(), LiveUserEditActivity.class);
        getActivity().startActivity(intent);
    }

    // 我的头像
    private void clickFlHead() {
        if (mUserModel != null) {
            Intent intent = new Intent(getActivity(), LiveUserPhotoActivity.class);
            intent.putExtra(LiveUserPhotoActivity.EXTRA_USER_IMG_URL, mUserModel.getHeadImage());
            getActivity().startActivity(intent);
        }
    }

    public void setUserModel(UserModel userModel) {
        mUserModel = userModel;
        if (mUserModel != null) {
            if (!TextUtils.isEmpty(mUserModel.getUserId())) {
                if (!UserModel.dealLoginSuccess(mUserModel, false))//更新用户数据
                {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.save_user_info_fail));
                }
            } else {
//                SDToast.showToast("接口返回user_id为空");
            }


            SDViewBinder.setTextView(tv_user_id, mUserModel.getUserId());

            //                        String user_diamonds = user.getUseDiamonds() + "";
            //                        SDViewBinder.setTextView(tv_use_diamonds, user_diamonds);

            SDViewBinder.setImageView(getActivity(), mUserModel.getHeadImage(), iv_head, R.drawable.ic_default_head);


            if (!TextUtils.isEmpty(mUserModel.getV_icon())) {
                SDViewBinder.setImageView(getActivity(), mUserModel.getV_icon(), iv_level);
                SDViewUtil.show(iv_level);
            } else {
                SDViewUtil.hide(iv_level);
            }

            SDViewBinder.setTextView(tv_nick_name, mUserModel.getNickName());
            if (mUserModel.getSexResId() > 0) {
                SDViewUtil.show(iv_global_male);
                iv_global_male.setImageResource(mUserModel.getSexResId());
            } else {
                SDViewUtil.hide(iv_global_male);
            }
            iv_rank.setImageResource(mUserModel.getLevelImageResId());
            iv_level_below.setImageResource(mUserModel.getLevelImageResId());

            String focus_count = mUserModel.getFocusCount() + "";
            SDViewBinder.setTextView(tv_focus_count, focus_count);

            SDViewBinder.setTextView(tv_fans_count, LiveUtils.getFormatNumber(mUserModel.getFansCount()));

            SDViewBinder.setTextView(tv_introduce, mUserModel.getSignature(), SDResourcesUtil.getString(R.string.forget_write_signature));

            TextPaint tp = tv_introduce.getPaint();
            if (!TextUtils.isEmpty(mUserModel.getV_explain())) {
                SDViewUtil.show(ll_v_explain);
                SDViewBinder.setTextView(tv_v_explain, mUserModel.getV_explain());
                tp.setFakeBoldText(true);
            } else {
                SDViewUtil.hide(ll_v_explain);
                tp.setFakeBoldText(false);
            }

            String video_count = mUserModel.getVideoCount() + "";
            SDViewBinder.setTextView(tv_video_num, video_count);
            //            SDViewBinder.setTextView(tv_video_num_me, video_count);


            String user_level = mUserModel.getUserLevel() + "";
            //            SDViewBinder.setTextView(tv_level, user_level);
            //
            //            SDViewBinder.setTextView(tv_income, LiveUtils.getFormatNumber(user.getUseableTicket()));
            //
            //                        SDViewBinder.setTextView(tv_accout, LiveUtils.getFormatNumber(user.getDiamonds()));

            int v_type = SDTypeParseUtil.getInt(mUserModel.getV_type());
            if (v_type == 0) {
                SDViewUtil.show(ll_upgrade);
            } else if (v_type == 1) {
                SDViewUtil.hide(ll_upgrade);
            } else if (v_type == 2) {
                SDViewUtil.hide(ll_upgrade);
            }

            String anchor = SDResourcesUtil.getString(R.string.live_account_authentication);
            anchor = anchor + SDResourcesUtil.getString(R.string.auth);
            SDViewBinder.setTextView(tv_anchor, anchor);

            is_authentication = mUserModel.getIsAuthentication();
            if (is_authentication == 0) {
                tv_v_type.setText(R.string.not_auth);
            } else if (is_authentication == 1) {
                tv_v_type.setText(R.string.in_check_not_compile);
            } else if (is_authentication == 2) {
                tv_v_type.setText(R.string.already_auth);
            } else if (is_authentication == 3) {
                tv_v_type.setText(R.string.auth_check_not_pass);
            }
        }
    }


}
