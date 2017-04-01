package com.fanwe.live.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.common.SDSelectManager.Mode;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;
import com.fanwe.library.span.model.MatcherInfo;
import com.fanwe.library.span.utils.SDPatternUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.ECreateCategoryChoose;
import com.fanwe.live.event.ECreateLiveSuccess;
import com.fanwe.live.model.CategoryNameListModel;
import com.fanwe.live.model.CreateLiveData;
import com.fanwe.live.model.LiveTopicModel;
import com.fanwe.live.model.Video_add_commitGroupId;
import com.fanwe.live.model.Video_add_videoActModel;
import com.fanwe.live.pop.CreateChooseCategoryPop;
import com.fanwe.live.pop.LiveCreateRoomShareTipsPop;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class LiveCreateRoomActivity extends BaseActivity implements TextWatcher, TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.iv_close)
    private ImageView iv_close;
    @ViewInject(R.id.ll_position_switch)
    private View ll_position_switch;
    @ViewInject(R.id.tv_start_show)
    private TextView tv_start_show;
    @ViewInject(R.id.tv_private_state)
    private TextView tv_private_state;
    @ViewInject(R.id.ll_add_topic)
    private LinearLayout ll_add_topic;
    @ViewInject(R.id.ll_private_show)
    private LinearLayout ll_private_show;
    @ViewInject(R.id.iv_private_lock)
    private ImageView iv_private_lock;
    @ViewInject(R.id.ll_share_layout)
    private LinearLayout ll_share_layout;
    @ViewInject(R.id.et_content_topic)
    private EditText mEditText;
    @ViewInject(R.id.tv_position_text)
    private TextView tv_position_text;
    @ViewInject(R.id.iv_position_icon)
    private ImageView iv_position_icon;

    @ViewInject(R.id.iv_share_weibo)
    private ImageView iv_share_weibo;
    @ViewInject(R.id.iv_share_timeline)
    private ImageView iv_share_timeline;
    @ViewInject(R.id.iv_share_wechat)
    private ImageView iv_share_wechat;
    @ViewInject(R.id.iv_share_qq)
    private ImageView iv_share_qq;
    @ViewInject(R.id.iv_share_qqzone)
    private ImageView iv_share_qqzone;
    @ViewInject(R.id.topic_desc)
    private TextView tv_topic_desc;
    @ViewInject(R.id.ll_category)
    private LinearLayout ll_category;
    @ViewInject(R.id.iv_category)
    private ImageView iv_category;
    @ViewInject(R.id.tv_category)
    private TextView tv_category;
    @ViewInject(R.id.cb_orientation)
    private CheckBox cb_orientation;
    @ViewInject(R.id.tv_orientation)
    private TextView tv_orientation;
    private SDSelectManager<ImageView> mManagerSelect;

    private LiveCreateRoomShareTipsPop mPopTips;

    private int isPrivate = 0;
    private int isLocate = 1;

    private String mTopic = "";
    private int mCateId;

    private ShareTypeEnum shareTypeEnum = ShareTypeEnum.NONE;

    private PopTipsRunnable mPopRunnable;
    private boolean isInAddVideo = false;
    private CreateChooseCategoryPop mPopWindow;
    private long mPopTime = 0;
    /**
     * 话题(String)
     */
    public static String EXTRA_TITLE = "extra_title";
    /**
     * 话题ID(int)
     */
    public static String EXTRA_CATE_ID = "extra_cate_id";
    /**
     * 开播提醒(String)
     */
    public static String EXTRA_DESC = "extra_desc";

    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private String mCategoryId;
    private String mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_create_room);
        getExtraData(getIntent());
        initView();
        showLocation();
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.bg_main);
    }

    /**
     * 获取activity传递的数据
     *
     * @param extraIntent
     */
    private void getExtraData(Intent extraIntent) {
        Bundle bundle = extraIntent.getExtras();
        if (bundle != null) {
            mTopic = bundle.getString(EXTRA_TITLE);
            mCateId = bundle.getInt(EXTRA_CATE_ID);
            mCategoryName = bundle.getString(EXTRA_CATEGORY_NAME);
            mCategoryId = bundle.getString(EXTRA_CATEGORY_ID);
            String desc = bundle.getString(EXTRA_DESC);
            if (!TextUtils.isEmpty(mTopic)) {
                mEditText.setText(mTopic);
                mEditText.setSelection(mEditText.getText().toString().length());
            }
            judgeShowTopicDesc(desc);
        }
    }

    /**
     * 判断是否显示活动开播提示
     */
    private void judgeShowTopicDesc(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            tv_topic_desc.setText(desc);
            tv_topic_desc.setVisibility(View.VISIBLE);
        } else {
            tv_topic_desc.setVisibility(View.GONE);
        }
    }

    private void initView() {
//        tv_start_show.setTextColor(SDDrawable.getStateListColor(SDResourcesUtil.getColor(R.color.main_color), SDResourcesUtil.getColor(R.color.white)));
        mPopRunnable = new PopTipsRunnable();
        iv_close.setOnClickListener(this);
        ll_position_switch.setOnClickListener(this);
        tv_start_show.setOnClickListener(this);
        ll_add_topic.setOnClickListener(this);
        ll_private_show.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (getString(R.string.give_live_title).equals(mEditText.getText() + "")) {
                    mEditText.setText("");
                }
            }
        });
        iv_share_weibo.setOnClickListener(this);
        iv_share_timeline.setOnClickListener(this);
        iv_share_wechat.setOnClickListener(this);
        iv_share_qq.setOnClickListener(this);
        iv_share_qqzone.setOnClickListener(this);
        ll_category.setOnClickListener(this);
        mManagerSelect = new SDSelectManager<>();
        mPopTips = new LiveCreateRoomShareTipsPop(mActivity);
        mManagerSelect.setMode(Mode.SINGLE);
        mManagerSelect.setListener(mManagerListener);
        mManagerSelect.setItems(new ImageView[]{iv_share_weibo, iv_share_timeline, iv_share_wechat, iv_share_qq, iv_share_qqzone});
        initCategoryPop();

        cb_orientation.setOnCheckedChangeListener(this);
    }

    private void initCategoryPop() {
        if (!TextUtils.isEmpty(mCategoryName)) {
            SDViewBinder.setTextView(tv_category, mCategoryName);
        } else {
            SDViewBinder.setTextView(tv_category, getString(R.string.please_choose));
        }
        mPopWindow = new CreateChooseCategoryPop(LiveCreateRoomActivity.this);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopTime = System.currentTimeMillis();
            }
        });
        requestCategoryNameList();
    }

    private void requestCategoryNameList() {
        CommonInterface.requestCategoryNameList(new AppRequestCallback<CategoryNameListModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    mPopWindow.setList(actModel.getCategory_name_list());
                }
            }
        });
    }

    private void showLocation() {
        String city = getCity();
        if (city == null || "".equals(city)) {
            setPositionSwitch();
        }
        tv_position_text.setText(getCity());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_position_switch:
                setPositionSwitch();
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.ll_add_topic:
                startTopicActivity();
                break;
            case R.id.ll_private_show:
                setShowPrivate();
                break;
            case R.id.tv_start_show:
                requestCreatetLive();
                break;
            case R.id.iv_share_weibo:
                mManagerSelect.performClick(iv_share_weibo);
                break;
            case R.id.iv_share_timeline:
                mManagerSelect.performClick(iv_share_timeline);
                break;
            case R.id.iv_share_wechat:
                mManagerSelect.performClick(iv_share_wechat);
                break;
            case R.id.iv_share_qq:
                mManagerSelect.performClick(iv_share_qq);
                break;
            case R.id.iv_share_qqzone:
                mManagerSelect.performClick(iv_share_qqzone);
                break;
            case R.id.ll_category:
                showCategoryPop();
                break;
            default:
                break;
        }
    }

    private void showCategoryPop() {
        if (!mPopWindow.isShowing() && (System.currentTimeMillis() - mPopTime >= 300)) {
            mPopWindow.showPopTips(iv_category, (int) getResources().getDimension(R.dimen.create_room_category_pop_top), SDViewUtil.dp2px(25));
        } else {
            mPopWindow.dismiss();
        }
    }

    public void onEventMainThread(ECreateCategoryChoose event) {
        mCategoryId = event.getmCategoryId();
        mCategoryName = event.getmCategoryName();
        SDViewBinder.setTextView(tv_category, mCategoryName);
        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

    /**
     * 请求创建直播间
     * 1. 拿到房间id
     * 1. 请求创建im群组
     * 1. 提交im群组给服务器
     * 1. 跳转到直播界面
     */
    private void requestCreatetLive() {
        if (isInAddVideo) {
            return;
        }
        isInAddVideo = true;
        Log.i("invite", "---requestCreatetLive---");
        int orientation = cb_orientation.isChecked() ? 1 : 0;
        CommonInterface.requestAddVideo(getTopic(), getCateId(), getCity(), getProvince(), getShareType(), isLocate, isPrivate, mCategoryId, orientation, new AppRequestCallback<Video_add_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    Log.i("invite", "getroomid success");
//                    TIMGroupManager.getInstance().createAVChatroomGroup(actModel.getRoomId() + "", new TIMValueCallBack<String>() {
//
//                        @Override
//                        public void onSuccess(String groupId) {
//                            LogUtil.i("create im success");
//                            group_id = groupId;
//                            Log.i("invite", "group_id=" + group_id);
//                            setGroupidAndCreateLive(groupId, actModel.getRoomId(), actModel.getVideoType(), actModel.getPush_url());
//                        }
//
//                        @Override
//                        public void onError(int code, String desc) {
//                            LogUtil.i("create im error");
//                            Log.i("invite", "get group_id error");
//                        }
//                    });

                    CreateLiveData data = new CreateLiveData();
                    data.setRoomId(actModel.getRoomId());
                    data.setVideoType(actModel.getVideo_type());
                    data.setPush_url(actModel.getPushUrl());
                    data.setIsHorizontal(cb_orientation.isChecked() ? 1 : 0);
                    // 因为java后台接口合并
                    data.setVideoActModel(actModel);

                    data.setRtcRole(LiveActivity.RTC_ROLE_ANCHOR);
                    if (TextUtils.isEmpty(actModel.getGroupId())) {
                        Log.i("invite", "开房但是无法获取到群组id");
                        return;
                    }
                    data.setGroup_id(actModel.getGroupId());
                    Log.i("invite", "开房群组id=" + actModel.getGroupId());
                    AppRuntimeWorker.createLive(data, LiveCreateRoomActivity.this);
                    //LiveCreateRoomActivity.this.finish();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
//                SDToast.showToast("请求房间id失败,httpCode=" + resp.getHttpCode() + " result=" + resp.getResult());
                Log.i("invite", "add_video errorclass = " + resp.getThrowable().getClass().getSimpleName());
                SDToast.showToast(
                        getString(R.string.create_room_error));
                dismissProgressDialog();
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                isInAddVideo = false;
                super.onFinish(resp);
            }
        });
    }

    /**
     * 向服务器发送群组id,并进入直播间
     *
     * @param group_id
     * @param room_id
     * @param video_type
     * @param push_url
     */
    private void setGroupidAndCreateLive(final String group_id, final int room_id, final int video_type, final String push_url) {
        Log.i("invite", "setGroupidAndCreateLive----");
        CommonInterface.setGroupId(group_id, new AppRequestCallback<Video_add_commitGroupId>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                Log.i("invite", "setGroupId success----");
                CreateLiveData data = new CreateLiveData();
                data.setRoomId(room_id);
                data.setVideoType(video_type);
                data.setPush_url(push_url);
                data.setRtcRole(LiveActivity.RTC_ROLE_ANCHOR);
                data.setGroup_id(group_id);
                AppRuntimeWorker.createLive(data, LiveCreateRoomActivity.this);
                LiveCreateRoomActivity.this.finish();
            }

            @Override
            protected void onError(SDResponse resp) {
                Log.i("invite", "setGroupId error----");
                super.onError(resp);
            }

        });

    }

    private String getTopic() {
        String str = mEditText.getText().toString().trim();
        if (getString(R.string.give_live_title).equals(str)) {
            return "";
        }
        return str;
    }

    private int getCateId() {
        if (TextUtils.equals(getTopic(), mTopic)) {
            return mCateId;
        }
        return 0;
    }

    private String getCity() {
        if (isLocate == 1) {
            return SDTencentMapManager.getInstance().getCity() != null ? SDTencentMapManager.getInstance().getCity() : "";
        }
        return null;
    }

    private String getProvince() {
        if (isLocate == 1) {
            return SDTencentMapManager.getInstance().getProvince() != null ? SDTencentMapManager.getInstance().getProvince() : "";
        }
        return null;
    }

    /**
     * 获取分享类型
     *
     * @return
     */
    private String getShareType() {
        if (isPrivate == 0) {
            switch (shareTypeEnum) {
                case WEIBO_ON:
                    return "sina";
                case TIMELINE_ON:
                    return "weixin_circle";
                case WECHAT_ON:
                    return "weixin";
                case QQ_ON:
                    return "qq";
                case QQZONE_ON:
                    return "qzone";
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * 设置定位开关，并改变相应图片
     */
    private void setPositionSwitch() {
        if (isLocate == 1) {
            isLocate = 0;
            iv_position_icon.setImageResource(R.drawable.create_room_position_close);
            tv_position_text.setText(R.string.open_location);
        } else {
            isLocate = 1;
            iv_position_icon.setImageResource(R.drawable.create_room_position_open);
            tv_position_text.setText(getCity());
        }
    }

    /**
     * 设置私密直播，并改变相应图片
     */
    private void setShowPrivate() {
        if (isPrivate == 0) {
            isPrivate = 1;
            SDViewUtil.setTextViewColorResId(tv_private_state, R.color.white);
            SDViewUtil.invisible(ll_share_layout);
            iv_private_lock.setImageResource(R.drawable.create_room_lock_off);
        } else {
            isPrivate = 0;
            SDViewUtil.setTextViewColorResId(tv_private_state, R.color.textview_gray);
            SDViewUtil.show(ll_share_layout);
            mManagerSelect.clearSelected();
            iv_private_lock.setImageResource(R.drawable.create_room_lock_on);
        }
    }

    private SDSelectManagerListener<ImageView> mManagerListener = new SDSelectManagerListener<ImageView>() {

        @Override
        public void onNormal(int index, ImageView item) {
            changeShareImage(item);

        }

        @Override
        public void onSelected(int index, ImageView item) {
            changeShareImage(item);
        }
    };

    /**
     * 更换分享状态改变的图片
     *
     * @param view
     */
    private void changeShareImage(ImageView view) {
        shareTypeEnum = ShareTypeEnum.NONE;
        if (mManagerSelect.isSelected(view)) {
            change2PressImage(view);
            showShareTipsPop(view);
            SDHandlerManager.getMainHandler().removeCallbacks(mPopRunnable);
            SDHandlerManager.getMainHandler().postDelayed(mPopRunnable, 1500);

        } else {
            change2NormalImage(view);
            mPopTips.dismiss();
        }
    }

    private void showShareTipsPop(View view) {
        switch (shareTypeEnum) {
            case WEIBO_ON:
                mPopTips.showPopTips(getString(R.string.sina_share_already_open), view);
                break;
            case TIMELINE_ON:
                mPopTips.showPopTips(getString(R.string.cricle_share_already_open), view);
                break;
            case WECHAT_ON:
                mPopTips.showPopTips(getString(R.string.weixin_share_already_open), view);
                break;
            case QQ_ON:
                mPopTips.showPopTips(getString(R.string.QQ_share_already_open), view);
                break;
            case QQZONE_ON:
                mPopTips.showPopTips(getString(R.string.QZone_share_already_open), view);
                break;
            default:
                break;
        }
    }

    private void change2PressImage(View view) {
        switch (view.getId()) {
            case R.id.iv_share_weibo:
                shareTypeEnum = ShareTypeEnum.WEIBO_ON;
                iv_share_weibo.setImageResource(R.drawable.create_room_weibo_on);
                break;
            case R.id.iv_share_timeline:
                shareTypeEnum = ShareTypeEnum.TIMELINE_ON;
                iv_share_timeline.setImageResource(R.drawable.create_room_moments_on);
                break;
            case R.id.iv_share_wechat:
                shareTypeEnum = ShareTypeEnum.WECHAT_ON;
                iv_share_wechat.setImageResource(R.drawable.create_room_wechat_on);
                break;
            case R.id.iv_share_qq:
                shareTypeEnum = ShareTypeEnum.QQ_ON;
                iv_share_qq.setImageResource(R.drawable.create_room_qq_on);
                break;
            case R.id.iv_share_qqzone:
                shareTypeEnum = ShareTypeEnum.QQZONE_ON;
                iv_share_qqzone.setImageResource(R.drawable.create_room_qqzone_on);
                break;
            default:
                break;
        }
    }

    private void change2NormalImage(View view) {
        switch (view.getId()) {
            case R.id.iv_share_weibo:
                iv_share_weibo.setImageResource(R.drawable.create_room_weibo_off);
                break;
            case R.id.iv_share_timeline:
                iv_share_timeline.setImageResource(R.drawable.create_room_moments_off);
                break;
            case R.id.iv_share_wechat:
                iv_share_wechat.setImageResource(R.drawable.create_room_wechat_off);
                break;
            case R.id.iv_share_qq:
                iv_share_qq.setImageResource(R.drawable.create_room_qq_off);
                break;
            case R.id.iv_share_qqzone:
                iv_share_qqzone.setImageResource(R.drawable.create_room_qqzone_off);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            tv_orientation.setText(R.string.orientation_h);
        } else {
            tv_orientation.setText(R.string.orientation_v);
        }
    }

    private class PopTipsRunnable implements Runnable {
        @Override
        public void run() {
            if (mPopTips.isShowing()) {
                mPopTips.dismiss();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        List<MatcherInfo> listMatch = SDPatternUtil.findMatcherInfo("#[^#]+#", s.toString());
        if (!listMatch.isEmpty()) {
            for (MatcherInfo info : listMatch) {
                ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.main_color));
                s.setSpan(span, info.getStart(), info.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && before == 0 && count == 1) {
            if (s.subSequence(start, start + 1).toString().equals("#")) {
                // "#"不能直接输入到输入框内，跳转至话题界面
                mEditText.setText(s.subSequence(0, start).toString() + s.subSequence(start + 1, s.length()).toString());
                mEditText.setSelection(start);
                startTopicActivity();
            }
        }
//当输入的内容符合#xxx#规则的时候请求服务器获取话题开播提示，否则隐藏提示
        if (s.length() > 0) {
            String topicTitle = s.toString();
            if (topicTitle.matches("#.+#.*")) {
                topicTitle = topicTitle.substring(topicTitle.indexOf("#") + 1, topicTitle.lastIndexOf("#"));
                CommonInterface.requestTopicDesc(topicTitle, new AppRequestCallback<LiveTopicModel>() {

                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        String desc = actModel.getDesc();
                        if (!TextUtils.isEmpty(desc)) {
                            judgeShowTopicDesc(desc);
                        } else {
                            tv_topic_desc.setText("");
                            tv_topic_desc.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                tv_topic_desc.setText("");
                tv_topic_desc.setVisibility(View.GONE);
            }
        }
    }


    private void startTopicActivity() {
        Intent intent = new Intent(mActivity, LiveCreateRoomTopicActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getExtraData(intent);
    }

    public void onEventMainThread(ECreateLiveSuccess event) {
        finish();
    }

    /**
     * 分享方式枚举
     */
    private enum ShareTypeEnum {
        WEIBO_ON, TIMELINE_ON, WECHAT_ON, QQ_ON, QQZONE_ON, NONE;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        SDViewUtil.hideInputMethod(v);
        if (event == null) {
            return true;
        }
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    @Override
    protected void onDestroy() {

        if (mPopTips != null) {
            SDHandlerManager.getMainHandler().removeCallbacks(mPopRunnable);
            mPopTips = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("创建房间界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("创建房间界面");
        MobclickAgent.onPause(this);
    }
}
