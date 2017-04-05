package com.fanwe.live.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveCategoryRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.CategoryDetailModel;
import com.fanwe.live.model.CategoryModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.pop.CategoryMorePop;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LiveCategoryActivity extends BaseActivity {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.iv_plus)
    private ImageView iv_plus;
    @ViewInject(R.id.iv_bg)
    private ImageView iv_bg;
    @ViewInject(R.id.list_view)
    private SDRecyclerView list_view;
    @ViewInject(R.id.rl_title)
    private RelativeLayout rl_title;
    @ViewInject(R.id.fl_bg)
    private FrameLayout fl_bg;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_main_title)
    private TextView tv_main_title;
    @ViewInject(R.id.tv_sub_title)
    private TextView tv_sub_title;
    private LiveCategoryRecyclerAdapter mAdapter;
    private List<List<LiveRoomModel>> mList = new ArrayList<>();
    private int mTotalDY = 0;
    private String mCategoryId;
    private CategoryDetailModel mCategoryDetailModel;
    private CategoryMorePop mPopWindow;
    private long mPopTime = 0;

    @Override
    protected int onCreateContentView() {
        return R.layout.activity_live_category;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        init();
        initIntent();
        initListener();
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        mCategoryId = intent.getStringExtra(EXTRA_CATEGORY_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        if (!TextUtils.isEmpty(mCategoryId)) {
            CommonInterface.requestCategory(mCategoryId, new AppRequestCallback<CategoryDetailModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (rootModel.isOk()) {
                        mCategoryDetailModel = actModel;
                        bindData();
                    }
                }
            });
        }
    }

    private void bindData() {
        if (mCategoryDetailModel != null) {
            CategoryModel model = null;
            InitActModel actModel = InitActModelDao.query();
            List<CategoryModel> modelList = actModel.getClassification();
            for (CategoryModel categoryModel : modelList) {
                if (mCategoryId.equals(categoryModel.getCategory_id())) {
                    model = categoryModel;
                    break;
                }
            }
            if (model != null) {
                model.setVideo_num(mCategoryDetailModel.getLiveNum());
                mPopWindow.setCategoryId(model.getCategory_id());
                mPopWindow.setCategoryName(model.getCategory_name());
                bindViewData(model);
                updateAdapter(model);
            }
        }
    }

    private void bindViewData(CategoryModel model) {
        SDViewBinder.setTextView(tv_title, model.getCategory_name());
        SDViewBinder.setTextView(tv_main_title, model.getCategory_name());
        SDViewBinder.setTextView(tv_sub_title, model.getCategory_desc());
        try {
            tv_main_title.setBackgroundColor(Color.parseColor("#" + model.getCategory_color()));
            tv_sub_title.setBackgroundColor(Color.parseColor("#" + model.getCategory_color()));
        } catch (Exception e) {
            LogUtil.e("颜色码不对");
        }
        SDViewBinder.setImageView(LiveCategoryActivity.this, iv_bg, model.getCategory_cover_url(), R.color.bg_main);
    }

    private void updateAdapter(CategoryModel model) {
        mList.clear();
        List<LiveRoomModel> listOne = new ArrayList<>();
        LiveRoomModel liveRoomModel = new LiveRoomModel();
        liveRoomModel.setCategory(model);
        listOne.add(liveRoomModel);
        mList.add(listOne);
        mList.addAll(SDCollectionUtil.splitList(mCategoryDetailModel.getLiveList(), 2));
        mAdapter.updateData(mList);
    }

    private void init() {
        mAdapter = new LiveCategoryRecyclerAdapter(this);
        list_view.setLinearVertical();
        list_view.setAdapter(mAdapter);
        mPopWindow = new CategoryMorePop(LiveCategoryActivity.this);
    }


    private void initListener() {
        list_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalDY += dy;
                judgeTitle();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopWindow.isShowing() && (System.currentTimeMillis() - mPopTime >= 300)) {
                    mPopWindow.showPopTips(iv_plus, (int) getResources().getDimension(R.dimen.category_more_pop_top), SDViewUtil.dp2px(22));
                    ObjectAnimator.ofFloat(iv_plus, "rotation", 0F, 45F).setDuration(300).start();//90度旋转
                } else {
                    mPopWindow.dismiss();
                }
            }
        });

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopTime = System.currentTimeMillis();
                ObjectAnimator.ofFloat(iv_plus, "rotation", 45F, 0F).setDuration(300).start();//90度旋转
            }
        });
    }

    private void judgeTitle() {
        float alpha = ((float) mTotalDY) / SDViewUtil.dp2px(300);
        if (alpha < 0.1) {
            alpha = 0;
        }
        if (alpha >= 1) {
            rl_title.setAlpha(1);
        } else {
            rl_title.setAlpha(0);
        }
        fl_bg.setAlpha(alpha);
    }

}
