package com.fanwe.live.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveCreaterAgreementActivity;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

/**
 * 主题点击更多pop
 */
public class CategoryMorePop extends PopupWindow implements  View.OnClickListener{

    private View contentView;
    private LinearLayout ll_create;
    private LinearLayout ll_search;
    private LinearLayout ll_message;
    private LinearLayout ll_me;
    private LinearLayout ll_recharge;
    private Activity mActivity;
    private String mCategoryId;
    private String mCategoryName;
    public CategoryMorePop(Activity context) {
        mActivity = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = layoutInflater.inflate(R.layout.pop_category_more, null);
        setContentView(contentView);
        ll_create = (LinearLayout)contentView.findViewById(R.id.ll_create);
        ll_search = (LinearLayout)contentView.findViewById(R.id.ll_search);
        ll_message = (LinearLayout)contentView.findViewById(R.id.ll_message);
        ll_me = (LinearLayout)contentView.findViewById(R.id.ll_me);
        ll_recharge = (LinearLayout)contentView.findViewById(R.id.ll_recharge);
        ll_create.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        this.setBackgroundDrawable(dw);

        setOutsideTouchable(true);
        setFocusable(false);
    }


    public void showPopTips(View parent,int top,int left) {
        if(isShowing()) {
            dismiss();
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);

        showAtLocation(parent, Gravity.BOTTOM, location[0] - SDViewUtil.getScreenWidth() / 2 + this.getWidth() / 2 - left,
                SDViewUtil.getScreenHeight() / 2 +  top);
    }

    public void showPopTips(View parent) {
        if(isShowing()) {
            dismiss();
        }
        SDViewUtil.showPopTopAndLeft(this, parent, 0, SDViewUtil.dp2px(42));
    }

    public void setCategoryId(String categoryId){
        mCategoryId = categoryId;
    }

    public void setCategoryName(String categoryName){
        mCategoryName = categoryName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_create:
                if (AppRuntimeWorker.isLogin(mActivity)) {
                    final UserModel userModel = UserModelDao.query();
                    if (userModel.getIsAgree() == 1) {
                        Intent intent = new Intent(mActivity, LiveCreateRoomActivity.class);
                        intent.putExtra(LiveCreateRoomActivity.EXTRA_CATEGORY_ID,mCategoryId);
                        intent.putExtra(LiveCreateRoomActivity.EXTRA_CATEGORY_NAME,mCategoryName);
                        mActivity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mActivity, LiveCreaterAgreementActivity.class);
                        mActivity.startActivity(intent);
                    }
                }
                break;
            case R.id.ll_search:
                clickSearch();
                MobclickAgent.onEvent(mActivity,"search");
                break;
            case R.id.ll_message:
                clickChatList();
                MobclickAgent.onEvent(mActivity, "message");
                break;
            case R.id.ll_me:
                UserModel model = UserModelDao.query();
                Intent intent = new Intent(mActivity, LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUserId());
                mActivity.startActivity(intent);
                break;
            case R.id.ll_recharge:
                clickRlAccout();
                MobclickAgent.onEvent(mActivity, "me_account");
                break;
        }
        if(isShowing()) {
            dismiss();
        }
    }

    /**
     * 搜索
     */
    private void clickSearch()
    {
        Intent intent = new Intent(mActivity, LiveSearchUserActivity.class);
        mActivity.startActivity(intent);
    }


    /**
     * 私聊列表
     */
    private void clickChatList()
    {
        Intent intent = new Intent(mActivity, LiveChatC2CActivity.class);
        mActivity.startActivity(intent);
    }

    private void clickRlAccout() {
        Intent intent = new Intent(mActivity, LiveRechargeActivity.class);
        mActivity.startActivity(intent);
    }
}
