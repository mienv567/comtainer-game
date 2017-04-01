package com.fanwe.live.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.activity.FansRankActivity;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.activity.LiveVideoCreateActivity;
import com.fanwe.live.activity.PlayersRankActivity;
import com.fanwe.live.adapter.LiveMainRecyclerAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.CategoryDetailModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.BonusPoolView;
import com.fanwe.live.view.SDProgressPullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 主界面fragment
 */
public class LiveMainFragment extends BaseFragment {

    @ViewInject(R.id.ptrsv_content)
    private SDProgressPullToRefreshScrollView ptrsv_content;
    @ViewInject(R.id.list_view)
    private SDRecyclerView list_view;
    @ViewInject(R.id.iv_search)
    private ImageView iv_search;
    @ViewInject(R.id.iv_start_live)
    private ImageView iv_start_live;
    @ViewInject(R.id.tv_pool)
    private TextView tv_pool;
    @ViewInject(R.id.tv_player_rank)
    private TextView tv_player_rank;
    @ViewInject(R.id.tv_fans_rank)
    private TextView tv_fans_rank;
    @ViewInject(R.id.tv_news)
    private TextView tv_news;

    @ViewInject(R.id.tv_money)
    private BonusPoolView tv_money;
    private int mMoney = 70000000;

    @ViewInject(R.id.convenientBanner)
    private ConvenientBanner convenientBanner;

    private LiveMainRecyclerAdapter mAdapter;
    private List<LiveRoomModel> mList = new ArrayList<>();

    public LiveMainFragment() {
        // Required empty public constructor
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_main;
    }

    @Override
    protected void init() {
        super.init();
        mAdapter = new LiveMainRecyclerAdapter(getActivity());
        list_view.setGridVertical(2);
        list_view.setAdapter(mAdapter);
        initListener();
        initPullToRefresh();
        requestData();
        requestMoney();
    }

    private void requestMoney() {
        tv_money.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMoney += new Random().nextInt(10000);
                //                tv_money.setText(mMoney + "");
                requestMoney();
            }
        }, 10000);
    }

    private void requestData() {
        CommonInterface.requestCategory("1", new AppRequestCallback<CategoryDetailModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    //                    for (int i = 0; i < 10; i++) {
                    List<LiveRoomModel> liveList = actModel.getLiveList();
                    if (liveList != null) {
                        mList.clear();
                        mList.addAll(liveList);
                        mAdapter.updateData(mList);
                    }
                    //                    }
                }
                ptrsv_content.onRefreshComplete();
            }
        });


        String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
                "http://img2.3lian.com/2014/f2/37/d/40.jpg",
                "http://d.3987.com/sqmy_131219/001.jpg",
                "http://img2.3lian.com/2014/f2/37/d/39.jpg",
                "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
                "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
                "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
        };
        List datas = Arrays.asList(images);
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, datas);
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        //                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
        //设置指示器的方向
        //                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
        //        convenientBanner.setManualPageable(false);//设置不能手动影响
    }


    private void initPullToRefresh() {
        ptrsv_content.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptrsv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void initListener() {
        iv_search.setOnClickListener(this);
        iv_start_live.setOnClickListener(this);
        tv_pool.setOnClickListener(this);
        tv_player_rank.setOnClickListener(this);
        tv_fans_rank.setOnClickListener(this);
        tv_news.setOnClickListener(this);
    }

    private void startLive() {
        if (AppRuntimeWorker.isLogin(getActivity())) {
            final UserModel userModel = UserModelDao.query();
            if (userModel.getIsAgree() == 1) {
                Intent intent = new Intent(getContext(), LiveVideoCreateActivity.class);
                intent.putExtra(LiveCreateRoomActivity.EXTRA_CATEGORY_ID, "2");
                intent.putExtra(LiveCreateRoomActivity.EXTRA_CATEGORY_NAME, "综艺");
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), LiveVideoCreateActivity.class);
                getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), LiveSearchUserActivity.class));
                break;
            case R.id.iv_start_live:
                startLive();
                break;
            case R.id.tv_pool:
                break;
            case R.id.tv_player_rank:
                startActivity(new Intent(getActivity(), PlayersRankActivity.class));
                break;
            case R.id.tv_fans_rank:
                startActivity(new Intent(getActivity(), FansRankActivity.class));
                break;
            case R.id.tv_news:
                break;
        }
    }

    class ImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            //            imageView.setImageResource(data);
            SDViewBinder.setImageView(getActivity(), imageView, data, R.drawable.ic_logo);
        }
    }


}
