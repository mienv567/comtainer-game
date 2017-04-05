package com.fanwe.live.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.model.UserModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-3 上午10:54:04 类说明
 */
public class LiveContTotalFragment extends LiveContBaseFragment
{
    public static final String TAG = "LiveContTotalFragment";
    public static final String EXTRA_USER_ID = "extra_user_id";

    @ViewInject(R.id.ll_do_not_contribute)
    private LinearLayout ll_do_not_contribute;

    private View headview;
    private TextView tv_num;

    protected String user_id = "";

    @Override
    protected void init()
    {
        getIntentExtra();
        super.init();
    }

    private void getIntentExtra()
    {
        if (getActivity().getIntent().hasExtra(EXTRA_USER_ID))
        {
            user_id = getActivity().getIntent().getExtras().getString(EXTRA_USER_ID);
        } else
        {
            this.user_id = "";
        }
    }

    @Override
    protected void register()
    {
        super.register();

        headview = getActivity().getLayoutInflater().inflate(R.layout.include_live_cont_total_top, null);
        tv_num = (TextView) headview.findViewById(R.id.tv_num);

        headview.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (app_ContActModel != null)
                {
                    UserModel user = app_ContActModel.getUserInfo();
                    if (user != null)
                    {
                        String userid = user.getUserId();
                        if (!TextUtils.isEmpty(userid))
                        {
                            Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUserId());
                            startActivity(intent);
                        } else
                        {
//                            SDToast.showToast("userid为空");
                        }
                    }
                }
            }
        });

        list.getRefreshableView().addHeaderView(headview);
        SDViewUtil.hide(headview);
    }

    @Override
    protected void requestCont(final boolean isLoadMore)
    {
        super.requestCont(isLoadMore);

        CommonInterface.requestCont(0, user_id, page, new AppRequestCallback<App_ContActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.getStatus() == 1)
                {
                    app_ContActModel = actModel;
                    bindheadData(actModel);

                }
            }

            private void bindheadData(App_ContActModel actModel)
            {
                List<App_ContModel> list_act = actModel.getCuserList();
                if (page == 1 && list_act != null && list_act.size() <= 0)
                {
                    SDViewUtil.show(ll_do_not_contribute);
                    SDViewUtil.hide(headview);
                } else
                {
                    SDViewUtil.show(headview);
                    SDViewUtil.hide(ll_do_not_contribute);
                }


                if (actModel != null)
                {
                    SDViewBinder.setTextView(tv_num, actModel.getTotalNum() + "");
                }

                SDViewUtil.updateAdapterByList(listModel, actModel.getCuserList(), adapter, isLoadMore);
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                list.onRefreshComplete();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("总贡献排行界面");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("总贡献排行界面");
    }
}
