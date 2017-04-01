package com.fanwe.live.fragment;

import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.auction.adapter.AuctionTabMeItemAdapter;
import com.fanwe.auction.model.AuctionTabMeItemModel;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.ERedPointChange;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveFamilyDetailsActivity;
import com.fanwe.live.activity.LiveFollowActivity;
import com.fanwe.live.activity.LiveMyFocusActivity;
import com.fanwe.live.activity.LiveMySelfContActivity;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.LiveUserHomeReplayActivity;
import com.fanwe.live.activity.LiveUserPhotoActivity;
import com.fanwe.live.activity.LiveUserProfitActivity;
import com.fanwe.live.activity.LiveUserSettingActivity;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.activity.UserCenterAuthentActivity;
import com.fanwe.live.activity.UserCenterAuthentTransferActivity;
import com.fanwe.live.activity.UserInviteActivity;
import com.fanwe.live.activity.UserSignInActivity;
import com.fanwe.live.activity.UserTaskActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveAddNewFamilyDialog;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgRedPoint;
import com.fanwe.live.utils.LiveUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-7-2 上午11:01:04 类说明
 */
public class LiveTabMeFragment extends BaseFragment {

    public static final String TAG = "LiveTabMeFragment";

    //        @ViewInject(R.id.ll_search)
    //        private LinearLayout ll_search;// 搜索

    //        @ViewInject(R.id.tv_use_diamonds)
    //        private TextView tv_use_diamonds; // 送出

    //        @ViewInject(R.id.ll_chat)
    //        private RelativeLayout ll_chat;

    @ViewInject(R.id.tv_total_unreadnum)
    private TextView tv_total_unreadnum;

    @ViewInject(R.id.fl_head)
    private FrameLayout fl_head;

    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id;

    @ViewInject(R.id.ll_user_id)
    private LinearLayout ll_user_id;

    //    我的背景
    @ViewInject(R.id.iv_background)
    private ImageView iv_background;


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

    @ViewInject(R.id.iv_remark)
    private ImageView iv_remark;// 编辑

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
    //
    //    @ViewInject(R.id.rl_my_live)
    //    private RelativeLayout rl_my_live;

    @ViewInject(R.id.tv_video_num)
    private TextView tv_video_num;// 直播

    //    @ViewInject(R.id.tv_video_num_me)
    //    private TextView tv_video_num_me;// 直播

    @ViewInject(R.id.rl_level)
    private RelativeLayout rl_level;

    // 1.2版用户等级图片
    @ViewInject(R.id.iv_level_below)
    private ImageView iv_level_below;
    //
    //        @ViewInject(R.id.tv_level)
    //        private TextView tv_level; // 等级

    @ViewInject(R.id.rl_income)
    private RelativeLayout rl_income; //我的收益

    @ViewInject(R.id.rl_invite)
    private RelativeLayout rl_invite;//邀请有礼

    //    @ViewInject(R.id.tv_income)
    //    private TextView tv_income; // 收益

    @ViewInject(R.id.rl_accout)
    private RelativeLayout rl_accout;// 账户

    @ViewInject(R.id.auction_gll_info)
    private SDGridLinearLayout auction_gll_info;//竞拍Item

    //        @ViewInject(R.id.tv_accout)
    //        private TextView tv_accout;

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

    private UserModel app_userinfoActModel;

    private AuctionTabMeItemAdapter adapter;
    private List<AuctionTabMeItemModel> auction_gll_info_array = new ArrayList<AuctionTabMeItemModel>();
    private int is_authentication;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_tab_me;
    }

    @Override
    protected void init() {
        super.init();
        register();
        bindAuctionAdapter();
    }

    private void register() {
        //                ll_search.setOnClickListener(this);
        //                ll_chat.setOnClickListener(this);
        fl_head.setOnClickListener(this);
        iv_remark.setOnClickListener(this);
        SDViewUtil.show(iv_remark);
        SDViewUtil.show(ll_user_id);
        include_cont_linear.setOnClickListener(this);
        ll_my_focus.setOnClickListener(this);
        ll_my_fans.setOnClickListener(this);
        ll_my_live.setOnClickListener(this);
        //        rl_my_live.setOnClickListener(this);
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

    private void bindAuctionAdapter() {
        auction_gll_info.setColNumber(2);
        adapter = new AuctionTabMeItemAdapter(auction_gll_info_array, getActivity());
        adapter.setItemClickListener(new SDAdapter.ItemClickListener<AuctionTabMeItemModel>() {
            @Override
            public void onClick(int position, AuctionTabMeItemModel item, View view) {
                if (!TextUtils.isEmpty(item.getUrl())) {
                    Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
                    intent.putExtra(LiveWebViewActivity.EXTRA_URL, item.getUrl());
                    startActivity(intent);
                } else {
//                    SDToast.showToast("url为空");
                }
            }
        });
        auction_gll_info.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            request();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        initUnReadNum();
        request();
        super.onResume();
        MobclickAgent.onPageStart("我的界面");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的界面");
    }

    private void request() {
        CommonInterface.requestMyUserInfoJava(new AppRequestCallback<UserModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_userinfoActModel = actModel;
                    bindData(actModel);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    private void bindData(UserModel actModel) {
        UserModel user = actModel;
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUserId())) {
                if (!UserModel.dealLoginSuccess(user, false))//更新用户数据
                {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.save_user_info_fail));
                }
            } else {
//                SDToast.showToast("接口返回user_id为空");
            }

            if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
                setAuctionItemView(user);
            }

            SDViewBinder.setTextView(tv_user_id, user.getUserId());

            //                        String user_diamonds = user.getUseDiamonds() + "";
            //                        SDViewBinder.setTextView(tv_use_diamonds, user_diamonds);

            SDViewBinder.setImageView(getActivity(), user.getHeadImage(), iv_head, R.drawable.ic_default_head);
            //            SDViewBinder.setImageView(getActivity(),user.getHeadImage(), iv_background);
            SDViewBinder.setImageView(getActivity(), iv_background, user.getHeadImage(), new BlurTransformation(getActivity(), 4));


            if (!TextUtils.isEmpty(user.getV_icon())) {
                SDViewBinder.setImageView(getActivity(), user.getV_icon(), iv_level);
                SDViewUtil.show(iv_level);
            } else {
                SDViewUtil.hide(iv_level);
            }

            SDViewBinder.setTextView(tv_nick_name, user.getNickName());
            if (user.getSexResId() > 0) {
                SDViewUtil.show(iv_global_male);
                iv_global_male.setImageResource(user.getSexResId());
            } else {
                SDViewUtil.hide(iv_global_male);
            }
            iv_rank.setImageResource(user.getLevelImageResId());
            iv_level_below.setImageResource(user.getLevelImageResId());

            String focus_count = user.getFocusCount() + "";
            SDViewBinder.setTextView(tv_focus_count, focus_count);

            SDViewBinder.setTextView(tv_fans_count, LiveUtils.getFormatNumber(user.getFansCount()));

            SDViewBinder.setTextView(tv_introduce, user.getSignature(), SDResourcesUtil.getString(R.string.forget_write_signature));

            TextPaint tp = tv_introduce.getPaint();
            if (!TextUtils.isEmpty(user.getV_explain())) {
                SDViewUtil.show(ll_v_explain);
                SDViewBinder.setTextView(tv_v_explain, user.getV_explain());
                tp.setFakeBoldText(true);
            } else {
                SDViewUtil.hide(ll_v_explain);
                tp.setFakeBoldText(false);
            }

            String video_count = user.getVideoCount() + "";
            SDViewBinder.setTextView(tv_video_num, video_count);
            //            SDViewBinder.setTextView(tv_video_num_me, video_count);


            String user_level = user.getUserLevel() + "";
            //            SDViewBinder.setTextView(tv_level, user_level);
            //
            //            SDViewBinder.setTextView(tv_income, LiveUtils.getFormatNumber(user.getUseableTicket()));
            //
            //                        SDViewBinder.setTextView(tv_accout, LiveUtils.getFormatNumber(user.getDiamonds()));

            int v_type = SDTypeParseUtil.getInt(user.getV_type());
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

            is_authentication = user.getIsAuthentication();
            if (is_authentication == 0) {
                tv_v_type.setText(SDResourcesUtil.getString(R.string.not_auth));
            } else if (is_authentication == 1) {
                tv_v_type.setText(SDResourcesUtil.getString(R.string.in_check_not_compile));
            } else if (is_authentication == 2) {
                tv_v_type.setText(SDResourcesUtil.getString(R.string.already_auth));
            } else if (is_authentication == 3) {
                tv_v_type.setText(SDResourcesUtil.getString(R.string.auth_check_not_pass));
            }
            judgeRedPoint();
        }
    }

    private void judgeRedPoint() {
        if (App.mShowSignRedPoint) {
            red_point_sign.setVisibility(View.VISIBLE);
        } else {
            red_point_sign.setVisibility(View.GONE);
        }
        if (App.mShowTaskRedPoint) {
            red_point_task.setVisibility(View.VISIBLE);
        } else {
            red_point_task.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(ERedPointChange event) {
        judgeRedPoint();
    }

    public void onEventMainThread(EImOnNewMessages event) {
        if (LiveConstant.CustomMsgType.MSG_RED_POINT == event.msg.getCustomMsgType()) {
            CustomMsgRedPoint msg1 = event.msg.getCustomMsgRedPoint();
            if(msg1.getHas_task_not_get() == 0){
                App.mShowTaskRedPoint = false;
            }else{
                App.mShowTaskRedPoint = true;
            }
            judgeRedPoint();
        }
    }

    private void setAuctionItemView(UserModel user) {
        InitActModel initmodel = InitActModelDao.query();

        String url_user_order = "";
        String url_user_pai = "";
        String url_podcast_order = "";
        String url_podcast_pai = "";
        String url_podcast_goods = "";
        if (initmodel != null) {
            App_InitH5Model h5Model = initmodel.getH5_url();
            if (h5Model != null) {
                url_user_order = h5Model.getUrl_user_order();
                url_user_pai = h5Model.getUrl_user_pai();
                url_podcast_order = h5Model.getUrl_podcast_order();
                url_podcast_pai = h5Model.getUrl_podcast_pai();
                url_podcast_goods = h5Model.getUrl_podcast_goods();
            }
        }

        auction_gll_info_array.clear();
        if (user.getShowUserOrder() == 1) {
            AuctionTabMeItemModel model = new AuctionTabMeItemModel();
            model.setLeft_text("我的订单");
            model.setRight_text(Integer.toString(user.getUserOrder()) + "个");
            model.setStr_Tag(AuctionTabMeItemModel.TabMeTag.tag1);
            model.setImage_Res(R.drawable.ic_user_order);
            model.setUrl(url_user_order);
            auction_gll_info_array.add(model);
        }

        if (user.getShowUserPai() == 1) {
            AuctionTabMeItemModel model = new AuctionTabMeItemModel();
            model.setRight_text(Integer.toString(user.getUserPai()) + "个");
            model.setLeft_text("我的竞拍");
            model.setImage_Res(R.drawable.ic_user_pai);
            model.setStr_Tag(AuctionTabMeItemModel.TabMeTag.tag2);
            model.setUrl(url_user_pai);
            auction_gll_info_array.add(model);
        }

        if (user.getShowPodcastOrder() == 1) {
            AuctionTabMeItemModel model = new AuctionTabMeItemModel();
            model.setRight_text(Integer.toString(user.getPodcastOrder()) + "个");
            model.setLeft_text("星店订单");
            model.setImage_Res(R.drawable.ic_podcast_order);
            model.setStr_Tag(AuctionTabMeItemModel.TabMeTag.tag3);
            model.setUrl(url_podcast_order);
            auction_gll_info_array.add(model);
        }

        if (user.getShowPodcastPai() == 1) {
            AuctionTabMeItemModel model = new AuctionTabMeItemModel();
            model.setRight_text(Integer.toString(user.getPodcastPai()) + "个");
            model.setLeft_text("竞拍管理");
            model.setStr_Tag(AuctionTabMeItemModel.TabMeTag.tag4);
            model.setImage_Res(R.drawable.ic_podcast_pai);
            model.setUrl(url_podcast_pai);
            auction_gll_info_array.add(model);
        }

        if (user.getShowPodcastGoods() == 1) {
            AuctionTabMeItemModel model = new AuctionTabMeItemModel();
            model.setRight_text(Integer.toString(user.getPodcastGoods()) + "个");
            model.setLeft_text("商品管理");
            model.setStr_Tag(AuctionTabMeItemModel.TabMeTag.tag5);
            model.setImage_Res(R.drawable.ic_podcast_goods);
            model.setUrl(url_podcast_goods);
            auction_gll_info_array.add(model);
        }

        if (auction_gll_info_array.size() == 0) {
            SDViewUtil.hide(auction_gll_info);
        } else {
            //如果基数添加一个白色区域
            if (auction_gll_info_array.size() % 2 != 0) {
                AuctionTabMeItemModel model = new AuctionTabMeItemModel();
                model.setBlankPage(true);
                auction_gll_info_array.add(model);
            }
            SDViewUtil.show(auction_gll_info);
            auction_gll_info.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //            case R.id.ll_search:
            //                clickLLSearch();
            //                MobclickAgent.onEvent(getActivity(), "search");
            //                break;
            //            case R.id.ll_chat:
            //                clickLlChat();
            //                MobclickAgent.onEvent(getActivity(), "message");
            //                break;
            case R.id.fl_head:
                clickFlHead();
                break;
            case R.id.iv_remark:
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
            case R.id.ll_family:
                clickfamily();
                break;
            case R.id.rl_sign:
                clickSign();
                break;
            case R.id.rl_task:
                clickTask();
                break;
            default:
                break;
        }
    }

    /**
     * 签到
     */
    private void clickSign() {
        Intent intent = new Intent(getActivity(), UserSignInActivity.class);
        startActivity(intent);
    }

    /**
     * 任务
     */
    private void clickTask() {
        Intent intent = new Intent(getActivity(), UserTaskActivity.class);
        startActivity(intent);
    }

    /**
     * 我的家族
     */
    private void clickfamily() {
        UserModel dao = UserModelDao.query();
        if (dao.getFamilyId() == 0)
            showFamDialog();
        else {
            //家族详情
            Intent intent = new Intent(getActivity(), LiveFamilyDetailsActivity.class);
            startActivity(intent);
        }
    }

    private void showFamDialog() {
        LiveAddNewFamilyDialog dialog = new LiveAddNewFamilyDialog(getActivity());
        dialog.showCenter();
    }

    /**
     * 设置
     */
    private void clickSetting() {
        Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
        startActivity(intent);
    }

    // 我关注的人
    private void clickLlMyFocus() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            String user_id = user.getUserId();
            if (!TextUtils.isEmpty(user_id)) {
                Intent intent = new Intent(getActivity(), LiveFollowActivity.class);
                intent.putExtra(LiveFollowActivity.EXTRA_USER_ID, user_id);
                startActivity(intent);
            } else {
//                SDToast.showToast("本地user_id为空");
            }
        } else {

        }
    }

    // 我的粉丝
    private void clickLlMyFans() {
        Intent intent = new Intent(getActivity(), LiveMyFocusActivity.class);
        startActivity(intent);
    }

    //认证
    private void clickLlUpgrade() {
        if (app_userinfoActModel != null) {
            //            Intent intent = new Intent(getActivity(), UserCenterAuthentActivity.class);
            //                        startActivity(intent);
            // 1.4新需求,认证前先跳转中转页
            if (is_authentication == 0) {
                Intent intent = new Intent(getActivity(), UserCenterAuthentTransferActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), UserCenterAuthentActivity.class);
                startActivity(intent);
            }
        }
    }

    private void clickRlAccout() {
        Intent intent = new Intent(getActivity(), LiveRechargeActivity.class);
        startActivity(intent);
    }

    //收益
    private void clickRlIncome() {
        Intent intent = new Intent(getActivity(), LiveUserProfitActivity.class);
        startActivity(intent);

    }

    //邀请有礼
    private void clickRlInvite() {
        Intent intent = new Intent(getActivity(), UserInviteActivity.class);
        startActivity(intent);
    }

    //等级
    private void clickRlLevel() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                String url = h5Model.getUrl_my_grades();
                Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
                intent.putExtra(LiveWebViewActivity.EXTRA_TITLE, "等级");
                startActivity(intent);
            }
        }
    }

    // 回放列表
    private void clickRlVideo() {
        Intent intent = new Intent(getActivity(), LiveUserHomeReplayActivity.class);
        startActivity(intent);
    }

    private void clickIncludeContLinear() {
        Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
        startActivity(intent);
    }

    //编辑
    private void clickIvRemark() {
        Intent intent = new Intent(getActivity(), LiveUserEditActivity.class);
        startActivity(intent);
    }

    // 我的头像
    private void clickFlHead() {
        if (app_userinfoActModel != null) {
            UserModel user = app_userinfoActModel;
            if (user != null) {
                Intent intent = new Intent(getActivity(), LiveUserPhotoActivity.class);
                intent.putExtra(LiveUserPhotoActivity.EXTRA_USER_IMG_URL, user.getHeadImage());
                startActivity(intent);
            }
        }
    }

    //聊天
    private void clickLlChat() {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        startActivity(intent);
    }

    // 搜索
    private void clickLLSearch() {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        startActivity(intent);
    }

    private void initUnReadNum() {
        TotalConversationUnreadMessageModel model = IMHelper.getC2CTotalUnreadMessageModel();
        setUnReadNumModel(model);
    }

    public void onEventMainThread(ERefreshMsgUnReaded event) {
        TotalConversationUnreadMessageModel model = event.model;
        setUnReadNumModel(model);
    }

    private void setUnReadNumModel(TotalConversationUnreadMessageModel model) {
        if (tv_total_unreadnum != null) {
            SDViewUtil.hide(tv_total_unreadnum);
            if (model != null && model.getTotalUnreadNum() > 0) {
                SDViewUtil.show(tv_total_unreadnum);
                tv_total_unreadnum.setText(model.getStr_totalUnreadNum());
            }
        }
    }
}
