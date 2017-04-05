package com.fanwe.live.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.customview.SDGridLinearLayout.ItemClickListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveUserHomeRightAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_user_reviewActModel;
import com.fanwe.live.model.ItemApp_user_reviewModel;
import com.fanwe.live.model.PlayBackData;
import com.fanwe.live.view.LazyScrollView.OnScrollListener;
import com.fanwe.live.view.LiveUserHomeScrollView;
import com.fanwe.live.view.TabLeftImage;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-11 下午3:30:30 类说明
 */
public class LiveUserHomeRightFragment extends LiveUserHomeBaseFragment implements OnScrollListener
{
    public static final String TAG = "LiveUserHomeRightFragment";

    @ViewInject(R.id.tv_count)
    private TextView tv_count;
    @ViewInject(R.id.ll_no_jilu)
    private LinearLayout ll_no_jilu;
    @ViewInject(R.id.gll_info)
    private SDGridLinearLayout gll_info;
    @ViewInject(R.id.ll_loading)
    private LinearLayout ll_loading;
    @ViewInject(R.id.left_tab_sort0)
    private TabLeftImage left_tab_sort0;
    @ViewInject(R.id.left_tab_sort1)
    private TabLeftImage left_tab_sort1;

    private SDSelectViewManager<TabLeftImage> mSelectManager = new SDSelectViewManager<TabLeftImage>();
    private int mSelectTabIndex = 0;
    private LiveUserHomeRightAdapter adapter;
    private List<ItemApp_user_reviewModel> listModel = new ArrayList<ItemApp_user_reviewModel>();

    private App_user_reviewActModel app_user_reviewActModel;

    private LiveUserHomeActivity act;
    private LiveUserHomeScrollView slv;

    private int page = 1;
    private int sort = 0;
    private String to_user_id;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_user_home_right;
    }

    @Override
    protected void init()
    {
        super.init();
        register();
        registerScrollListner();
        getIntentExtra();
        addSortTab();
        bindAdapter();
        requestUser_review(false);
    }

    private void getIntentExtra()
    {
        to_user_id = getActivity().getIntent().getExtras().getString(LiveUserHomeActivity.EXTRA_USER_ID);
    }

    private void registerScrollListner()
    {
        act = (LiveUserHomeActivity) getActivity();
        act.registerScrollListner(this);
        slv = act.getLsv();
    }

    private void register()
    {
        gll_info.setItemClickListener(new ItemClickListener()
        {

            @Override
            public void onItemClick(int position, View view, ViewGroup parent)
            {
                ItemApp_user_reviewModel model = adapter.getItem(position);
                if (model != null)
                {
                    PlayBackData data = new PlayBackData();
                    // TODO  去掉了roomId
//                    data.setRoomId(model.getReviewId());
                    data.setReviewId(model.getReviewId());
                    data.setPlaybackUrl(model.getReviewUrl());
                    AppRuntimeWorker.startPlayback(data, getActivity());
                }
            }
        });
    }

    private void addSortTab()
    {
        int textSizeNormal = SDViewUtil.sp2px(13);
        int textSizeSelected = SDViewUtil.sp2px(14);

        left_tab_sort0.setTextTitle(SDResourcesUtil.getString(R.string.newest));
        left_tab_sort0.getViewConfig(left_tab_sort0.mTvTitle).setTextColorNormalResId(R.color.black).setTextColorSelectedResId(R.color.main_color).setTextSizeNormal(textSizeNormal).setTextSizeSelected(textSizeSelected);
        left_tab_sort0.getViewConfig(left_tab_sort0.mIvLeft).setBackgroundNormalResId(R.drawable.ic_me_jiantou2).setBackgroundSelectedResId(R.drawable.ic_me_jiantou2);

        left_tab_sort1.setTextTitle(SDResourcesUtil.getString(R.string.hotest));
        left_tab_sort1.getViewConfig(left_tab_sort1.mTvTitle).setTextColorNormalResId(R.color.black).setTextColorSelectedResId(R.color.main_color).setTextSizeNormal(textSizeNormal).setTextSizeSelected(textSizeSelected);
        left_tab_sort1.getViewConfig(left_tab_sort1.mIvLeft).setBackgroundNormalResId(R.drawable.ic_me_jiantou2).setBackgroundSelectedResId(R.drawable.ic_me_jiantou2);

        mSelectManager.setListener(new SDSelectManagerListener<TabLeftImage>()
        {

            @Override
            public void onNormal(int index, TabLeftImage item)
            {
            }

            @Override
            public void onSelected(int index, TabLeftImage item)
            {
                switch (index)
                {
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

        mSelectManager.setItems(new TabLeftImage[]
                {left_tab_sort0, left_tab_sort1});
        mSelectManager.performClick(mSelectTabIndex);
    }

    protected void click0()
    {
        left_tab_sort0.mIvLeft.setVisibility(View.INVISIBLE);
        left_tab_sort1.mIvLeft.setVisibility(View.INVISIBLE);

        sort = 0;
        refreshViewer();
    }

    protected void click1()
    {
        left_tab_sort0.mIvLeft.setVisibility(View.INVISIBLE);
        left_tab_sort1.mIvLeft.setVisibility(View.INVISIBLE);

        sort = 1;
        refreshViewer();
    }

    private void bindAdapter()
    {
        gll_info.setColNumber(1);
        adapter = new LiveUserHomeRightAdapter(listModel, getActivity(), app_user_homeActModel);
        gll_info.setAdapter(adapter);
    }

    private void loadMoreViewer()
    {
        if (isVisible())
        {
            if (app_user_reviewActModel != null)
            {
                if (app_user_reviewActModel.getHas_next() == 1)
                {
                    SDViewUtil.show(ll_loading);
                    ll_loading.postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            page++;
                            requestUser_review(true);
                        }
                    }, 1000);
                } else
                {

                }
            } else
            {
                refreshViewer();
            }
        }
    }

    private void refreshViewer()
    {
        page = 1;
        requestUser_review(false);
    }

    private void requestUser_review(final boolean isLoadMore)
    {
        CommonInterface.requestUser_review(page, sort, to_user_id, new AppRequestCallback<App_user_reviewActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.getStatus() == 1)
                {
                    app_user_reviewActModel = actModel;

                    int count = actModel.getReviewList().size();
                    if (page == 1 && count <= 0)
                    {
                        SDViewUtil.show(ll_no_jilu);
                        SDViewUtil.hide(gll_info);
                    } else
                    {
                        if (page == 1 && count > 0)
                        {
                            SDViewBinder.setTextView(tv_count, count + "");
                        }
                        SDViewUtil.hide(ll_no_jilu);
                    }

                    SDViewUtil.updateAdapterByList(listModel, actModel.getReviewList(), adapter, isLoadMore);

                    slv.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //slv.fullScroll(ScrollView.FOCUS_DOWN);
                            slv.scrollBy(0, 1);
                        }
                    }, 500);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                // TODO Auto-generated method stub
                super.onFinish(resp);
                SDViewUtil.hide(ll_loading);
            }

        });
    }

    @Override
    public void onBottom()
    {
        loadMoreViewer();
    }

    @Override
    public void onTop()
    {
        slv.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //slv.fullScroll(ScrollView.FOCUS_DOWN);
                slv.scrollBy(0, 1);
            }
        }, 1000);
    }

    @Override
    public void onScroll()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume()
    {
        super.onResume();
        MobclickAgent.onPageStart("用户主页-直播");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("用户主页-直播");
    }
}
