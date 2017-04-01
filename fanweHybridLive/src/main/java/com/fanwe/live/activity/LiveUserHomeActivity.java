package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabText;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.fragment.LiveUserHomeBaseFragment;
import com.fanwe.live.fragment.LiveUserHomeLeftFragment;
import com.fanwe.live.fragment.LiveUserHomeRightFragment;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_user_homeActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_set_blackActModel;
import com.fanwe.live.utils.LiveUtils;
import com.fanwe.live.view.LazyScrollView.OnScrollListener;
import com.fanwe.live.view.LiveUserHomeScrollView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-11 上午11:52:46 类说明
 */
public class LiveUserHomeActivity extends BaseActivity {
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_USER_IMG_URL = "extra_user_img_url";

    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;
    //
    //    @ViewInject(R.id.tv_nickname)
    //    private TextView tv_nickname;

    @ViewInject(R.id.ll_userinfo)
    private LinearLayout ll_userinfo;

    @ViewInject(R.id.iv_userinfo_bg)
    private ImageView iv_userinfo_bg;

    @ViewInject(R.id.lsv)
    private LiveUserHomeScrollView lsv;

    public LiveUserHomeScrollView getLsv() {
        return lsv;
    }

    @ViewInject(R.id.ll_close)
    private LinearLayout ll_close;


    @ViewInject(R.id.tab_left)
    private SDTabText tab_left;

    @ViewInject(R.id.tab_right)
    private SDTabText tab_right;
    //
    //    @ViewInject(R.id.tv_use_diamonds)
    //    private TextView tv_use_diamonds;// 消费砖石

    @ViewInject(R.id.ll_user_id)
    private LinearLayout ll_user_id;

    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id;

    @ViewInject(R.id.iv_head)
    private ImageView iv_head;// 头像

    @ViewInject(R.id.iv_level)
    private ImageView iv_level;// 等级

    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;// 昵称

    @ViewInject(R.id.iv_global_male)
    private ImageView iv_global_male;

    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;// 等级

    @ViewInject(R.id.ll_my_focus)
    private LinearLayout ll_my_focus;// 关注布局

    @ViewInject(R.id.tv_focus_count)
    private TextView tv_focus_count;// 关注数量

    @ViewInject(R.id.ll_my_fans)
    private LinearLayout ll_my_fans;// 粉丝布局

    @ViewInject(R.id.tv_fans_count)
    private TextView tv_fans_count;// 粉丝数量

    @ViewInject(R.id.tv_introduce)
    private TextView tv_introduce;// 个人简介

    //    @ViewInject(R.id.tv_v_explain)
    //    private TextView tv_v_explain;// 认证信息
    //    @ViewInject(R.id.ll_v_explain)
    //    private LinearLayout ll_v_explain;

    @ViewInject(R.id.ll_follow)
    private LinearLayout ll_follow;// 关注
    @ViewInject(R.id.tv_follow)
    private TextView tv_follow;

    @ViewInject(R.id.ll_letter)
    private LinearLayout ll_letter;// 私信

    @ViewInject(R.id.ll_set_black)
    private LinearLayout ll_set_black;// 拉黑
    @ViewInject(R.id.tv_set_black)
    private TextView tv_set_black;

    //    @ViewInject(R.id.ll_my_live)
    //    private LinearLayout ll_my_live;
    //    @ViewInject(R.id.tv_video_num)
    //    private TextView tv_video_num; // 直播数

    @ViewInject(R.id.ll_function_layout)
    private LinearLayout ll_function_layout;


    private SDSelectViewManager<SDTabText> mSelectManager = new SDSelectViewManager<SDTabText>();

    private int mSelectTabIndex = 0;

    private String user_id;

    private App_user_homeActModel app_user_homeActModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_home);
        x.view().inject(this);
        init();
    }

    @Override
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void init() {
        getIntentExtra();
        register();
        setHideUserIdView();
        requestUser_home(false);
    }

    private void setHideUserIdView() {
        SDViewUtil.hide(ll_user_id);
    }

    private void getIntentExtra() {
        user_id = getIntent().getExtras().getString(EXTRA_USER_ID);
    }

    private void register() {
        if (UserModelDao.query() != null) {
            if (TextUtils.equals(user_id, UserModelDao.query().getUserId())) {
                SDViewUtil.hide(ll_function_layout);
            }
        }
        rl_title.setAlpha(0);
        ll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_back.setClickable(false);
        lsv.setScrollViewListener(new LiveUserHomeScrollView.ScrollViewListener() {
            // y表示当前滑动条的纵坐标
            // oldy表示前一次滑动的纵坐标
            @Override
            public void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy) {
                if (y < 600) {
                    float alpha = ((float) y) / 600;
                    rl_title.setAlpha(alpha);

                    if (y < 300) {
                        ll_back.setClickable(false);
                    } else {
                        ll_back.setClickable(true);
                    }
                }
            }
        });

        ll_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_my_focus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (app_user_homeActModel != null && app_user_homeActModel.getUser() != null) {
                    UserModel userModel = app_user_homeActModel.getUser();
                    Intent intent = new Intent(LiveUserHomeActivity.this, LiveFollowActivity.class);
                    intent.putExtra(LiveMyFocusActivity.EXTRA_USER_ID, userModel.getUserId());
                    LiveUserHomeActivity.this.startActivity(intent);
                }
            }
        });

        ll_my_fans.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (app_user_homeActModel != null && app_user_homeActModel.getUser() != null) {
                    UserModel userModel = app_user_homeActModel.getUser();
                    Intent intent = new Intent(LiveUserHomeActivity.this, LiveMyFocusActivity.class);
                    intent.putExtra(LiveMyFocusActivity.EXTRA_USER_ID, userModel.getUserId());
                    LiveUserHomeActivity.this.startActivity(intent);
                }
            }
        });

        ll_follow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                requestFollow();
            }
        });
        ll_letter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (app_user_homeActModel != null) {
                    UserModel local_user = UserModelDao.query();
                    UserModel to_user = app_user_homeActModel.getUser();
                    if (local_user != null && to_user != null) {
                        Intent intent = new Intent(mActivity, LivePrivateChatActivity.class);
                        intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, to_user.getUserId());
                        mActivity.startActivity(intent);
                    }
                }
            }
        });
        ll_set_black.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                requestSet_black();
            }
        });
        iv_head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (app_user_homeActModel != null && app_user_homeActModel.getUser() != null) {
                    Intent intent = new Intent(LiveUserHomeActivity.this, LiveUserHeadImageActivity.class);
                    intent.putExtra(EXTRA_USER_ID, app_user_homeActModel.getUser().getUserId());
                    intent.putExtra(EXTRA_USER_IMG_URL, app_user_homeActModel.getUser().getHeadImage());
                    LiveUserHomeActivity.this.startActivity(intent);
                }
            }
        });

        //        ll_my_live.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                mSelectManager.setSelected(tab_right, true);
        //            }
        //        });

        addTab();
    }

    private void addTab() {
        tab_left.setTextTitle(getString(R.string.home_page));
        tab_left.getViewConfig(tab_left.mTv_title).setTextColorNormalResId(R.color.gray).setTextColorSelectedResId(R.color.main_color);
        tab_left.setTextSizeTitleSp(15);
        tab_right.setTextTitle(getString(R.string.live));
        tab_right.getViewConfig(tab_right.mTv_title).setTextColorNormalResId(R.color.gray).setTextColorSelectedResId(R.color.main_color);
        tab_right.setTextSizeTitleSp(15);

        mSelectManager.setListener(new SDSelectManagerListener<SDTabText>() {

            @Override
            public void onNormal(int index, SDTabText item) {
            }

            @Override
            public void onSelected(int index, SDTabText item) {
                switch (index) {
                    case 0:
                        click0();

                        break;
                    case 1:
                        click1();

                        break;
                    default:
                        break;
                }
            }

        });

        mSelectManager.setItems(new SDTabText[]
                {tab_left, tab_right});
    }

    /**
     * 主页
     */
    protected void click0() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeLeftFragment.class, b);
        }
    }

    /**
     * 直播
     */
    protected void click1() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeRightFragment.class, b);
        }
    }

    private void requestFollow() {
        CommonInterface.requestFollow(user_id, app_user_homeActModel.getUser(), new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    // 已关注则刷新接口
                    setIsFollow(actModel.getRelationship());
                    requestUser_home(true);
                }
            }
        });
    }

    private void requestSet_black() {

        CommonInterface.requestSet_black(user_id, app_user_homeActModel.getUser(), new AppRequestCallback<User_set_blackActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    // 已拉黑则刷新接口
                    setIsSet_black(actModel.getRelationship());
                    requestUser_home(true);
                }
            }
        });
    }

    // 设置个人信息关注按钮
    private void setIsFollow(int relationShip) {
        if (relationShip == 1) {
            tv_follow.setText(R.string.already_follow);
        } else {
            tv_follow.setText(R.string.follow);
        }
    }

    // 设置个人信息拉黑按钮
    private void setIsSet_black(int relationShip) {
        if (relationShip == 2) {
            // 拉黑则默认无法关注
            // ll_follow.setClickable(false);
            tv_set_black.setText(R.string.relive_black);
        } else {
            // ll_follow.setClickable(true);
            tv_set_black.setText(R.string.pull_black);
        }
    }

    // isRefresh是刷新就不切换Fragment
    private void requestUser_home(final boolean isRefresh) {
        CommonInterface.requestUser_home(user_id, new AppRequestCallback<App_user_homeActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_user_homeActModel = actModel;

                    if (!isRefresh) {
                        mSelectManager.performClick(mSelectTabIndex);
                    }

                    setIsFollow(actModel.getUser().getRelationship());
                    setIsSet_black(actModel.getUser().getRelationship());
                    bindData(actModel);
                }
            }
        });
    }

    private void bindData(App_user_homeActModel actModel) {
        UserModel user = actModel.getUser();
        if (user != null) {
            long use_diamonds = user.getUseDiamonds();
            String str_use_diamonds = String.valueOf(use_diamonds);
            //            SDViewBinder.setTextView(tv_use_diamonds, str_use_diamonds);
            SDViewBinder.setImageView(this, user.getHeadImage(), iv_head, R.drawable.ic_default_head);
            //            SDViewBinder.setImageView(this,user.getHeadImage(), iv_userinfo_bg);
            SDViewBinder.setImageView(this, iv_userinfo_bg, user.getHeadImage(), new BlurTransformation(this, 4));

            if (!TextUtils.isEmpty(user.getV_icon())) {
                SDViewBinder.setImageView(this, user.getV_icon(), iv_level);
            } else {
                iv_level.setVisibility(View.GONE);
            }

            //            SDViewBinder.setTextView(tv_nickname, user.getNickName());
            SDViewBinder.setTextView(tv_nick_name, user.getNickName());

            if (user.getSexResId() > 0) {
                SDViewUtil.show(iv_global_male);
                iv_global_male.setImageResource(user.getSexResId());
            } else {
                SDViewUtil.hide(iv_global_male);
            }

            iv_rank.setImageResource(user.getLevelImageResId());


            long focus_count = user.getFocusCount();
            String str_focus_count = String.valueOf(focus_count);
            SDViewBinder.setTextView(tv_focus_count, str_focus_count);

            long fans_count = user.getFansCount();
            String str_fans_count = String.valueOf(fans_count);
            SDViewBinder.setTextView(tv_fans_count, LiveUtils.getFormatNumber(fans_count));

            //            SDViewBinder.setTextView(tv_video_num, user.getVideoCount() + "");
            SDViewBinder.setTextView(tv_introduce, user.getSignature(), getString(R.string.forget_write_signature));

            //            if (!TextUtils.isEmpty(user.getV_explain())) {
            //                SDViewUtil.show(ll_v_explain);
            //                SDViewBinder.setTextView(tv_v_explain, user.getV_explain());
            //            } else {
            //                ll_v_explain.setVisibility(View.GONE);
            //            }
        }

    }

    public void registerScrollListner(OnScrollListener o) {
        lsv.getView();
        lsv.setOnScrollListener(o);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUser_home(true);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
