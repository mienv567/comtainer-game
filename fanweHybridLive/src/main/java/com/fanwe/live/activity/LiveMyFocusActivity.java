package com.fanwe.live.activity;

import android.os.Bundle;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午1:58:48 类说明
 */
public class LiveMyFocusActivity extends LiveFocusFollowBaseActivity {
    public static final String TAG = "LiveMyFocusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.fans));
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
    }

    @Override
    protected void request(final boolean isLoadMore) {
        CommonInterface.requestMy_focus(page, to_user_id, new AppRequestCallback<App_focus_follow_ActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    List<UserModel> relationshipList = actModel.getRelationshipList();
                    app_my_focusActModel = actModel;
                    SDViewUtil.updateAdapterByList(listModel, relationshipList, adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                list.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("粉丝列表界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("粉丝列表界面");
        MobclickAgent.onPause(this);
    }
}
