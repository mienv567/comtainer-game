package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.span.builder.SDSpannableStringBuilder;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewSizeLocker;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveTabHotBannerPagerAdapter;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.model.LiveTopicInfoModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LiveTabHotHeaderView extends BaseAppView
{

    private static final String STRING_MORE = "["+SDResourcesUtil.getString(R.string.more)+"]";
    private static final String STRING_BACK = "[+"+SDResourcesUtil.getString(R.string.recover)+"]";

    private SDSlidingPlayView spv_content;
    private View ll_topic_info;
    private ImageView iv_head;
    private TextView tv_topic;
    private TextView tv_desc;

    private List<LiveBannerModel> listModel = new ArrayList<>();
    private LiveTabHotBannerPagerAdapter adapter;

    private boolean showAllDescMode = false;
    private String strDesc;
    private SDViewSizeLocker sizeLocker;
    private int lockHeight;

    public LiveTabHotHeaderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotHeaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabHotHeaderView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_tab_hot_header);

        spv_content = find(R.id.spv_content);
        ll_topic_info = find(R.id.ll_topic_info);
        iv_head = find(R.id.iv_head);
        tv_topic = find(R.id.tv_topic);
        tv_desc = find(R.id.tv_desc);

        spv_content.setNormalImageResId(R.drawable.ic_point_normal_dark);
        spv_content.setSelectedImageResId(R.drawable.ic_point_selected_main_color);

        adapter = new LiveTabHotBannerPagerAdapter(listModel, getActivity());
        adapter.setItemClicklistener(new SDAdapter.ItemClickListener<LiveBannerModel>()
        {
            @Override
            public void onClick(int position, LiveBannerModel item, View view)
            {

            }
        });
        spv_content.setAdapter(adapter);

        sizeLocker = new SDViewSizeLocker(this);
    }

    public void setBannerClickListener(SDAdapter.ItemClickListener<LiveBannerModel> listener)
    {
        adapter.setItemClicklistener(listener);
    }

    public void setTopicInfoModel(final LiveTopicInfoModel model)
    {
        showAllDescMode = false;
        strDesc = null;
        if (model != null)
        {
            SDViewUtil.show(ll_topic_info);
            SDViewBinder.setImageView(getActivity(),model.getHead_image(), iv_head,R.drawable.ic_default_head);
            SDViewBinder.setTextView(tv_topic, model.getTitle());
            iv_head.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUser_id());
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
                    getActivity().startActivity(intent);
                }
            });
            strDesc = model.getDesc();
            if (TextUtils.isEmpty(strDesc))
            {
                SDViewUtil.hide(tv_desc);
            } else
            {
                SDViewUtil.show(tv_desc);

                postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setShowAllDescMode(false);
                    }
                }, 100);
            }
        } else
        {
            SDViewUtil.hide(ll_topic_info);
        }
    }

    public void setShowAllDescMode(boolean showAllDescMode)
    {
        this.showAllDescMode = showAllDescMode;

        float length = SDViewUtil.measureText(tv_desc, strDesc);
        float maxLength = tv_desc.getWidth() * 2;
        float clickLength = SDViewUtil.measureText(tv_desc, STRING_MORE);
        if (length > maxLength)
        {
            SDSpannableStringBuilder sb = new SDSpannableStringBuilder();
            int color = SDResourcesUtil.getColor(R.color.main_color);
            ForegroundColorSpan span = new ForegroundColorSpan(color);

            if (showAllDescMode)
            {
                sb.append(strDesc);
                sb.appendSpan(span, STRING_BACK);
            } else
            {
                CharSequence ellipsizeDesc = TextUtils.ellipsize(strDesc, tv_desc.getPaint(), maxLength - clickLength, TextUtils.TruncateAt.END);
                sb.append(ellipsizeDesc);
                sb.appendSpan(span, STRING_MORE);
            }
            sb.coverSpan(span, clickableSpan);

            tv_desc.setText(sb);
        } else
        {
            tv_desc.setText(strDesc);
        }
    }

    private ClickableSpan clickableSpan = new ClickableSpan()
    {
        @Override
        public void updateDrawState(TextPaint ds)
        {
        }

        @Override
        public void onClick(View widget)
        {
            setShowAllDescMode(!showAllDescMode);
        }
    };


    public void setListLiveBannerModel(List<LiveBannerModel> listModel)
    {
        loadFirstImage(listModel);
        adapter.updateData(listModel);

        if (!adapter.getData().isEmpty())
        {
            SDViewUtil.show(spv_content);
            spv_content.startPlay(5 * 1000);
        } else
        {
            SDViewUtil.hide(spv_content);
        }
    }

    private void loadFirstImage(List<LiveBannerModel> listModel)
    {
        if (!SDCollectionUtil.isEmpty(listModel))
        {
            LiveBannerModel model = listModel.get(0);
            ImageLoader.getInstance().loadImage(model.getImage(), new SimpleImageLoadingListener()
            {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                {
                    int height = SDViewUtil.getScaleHeight(loadedImage.getWidth(), loadedImage.getHeight(), SDViewUtil.getScreenWidth());
                    if (height > 0)
                    {
                        if (lockHeight <= 0)
                        {
                            lockHeight = height;
                            sizeLocker.lockHeight(lockHeight);
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void onDetachedFromWindow()
    {
        spv_content.stopPlay();
        super.onDetachedFromWindow();
    }
}
