package com.fanwe.live.appview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.listener.SDItemClickListener;
import com.fanwe.library.model.ImageModel;
import com.fanwe.library.utils.LocalImageFinder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDAppView;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;

/**
 * 本地图库加载view
 * <p/>
 * Created by Administrator on 2016/9/6.
 */
public class SDSelectImageView extends SDAppView
{
    private SDRecyclerView recyclerView;
    private SDRecyclerAdapter<ImageModel> adapter;

    private SDSelectManager<ImageModel> selectManager;
    private LocalImageFinder localImageFinder;

    private EnumSelectMode selectMode;
    /**
     * 是否显示勾选按钮
     */
    private boolean showSelected;

    private SDSelectManager.SDSelectManagerListener<ImageModel> selectListener;
    private SDItemClickListener<ImageModel> clickImageListener;
    private SDItemClickListener<ImageModel> clickSelectedListener;


    public SDSelectImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public SDSelectImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDSelectImageView(Context context)
    {
        super(context);
        init();
    }

    public void setShowSelected(boolean showSelected)
    {
        this.showSelected = showSelected;
        if (adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    public void setSelectListener(SDSelectManager.SDSelectManagerListener<ImageModel> selectListener)
    {
        this.selectListener = selectListener;
    }

    public void setClickImageListener(SDItemClickListener<ImageModel> clickImageListener)
    {
        this.clickImageListener = clickImageListener;
    }

    public void setClickSelectedListener(SDItemClickListener<ImageModel> clickSelectedListener)
    {
        this.clickSelectedListener = clickSelectedListener;
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_sd_select_image);
        recyclerView = find(R.id.recyclerView);

        recyclerView.setGridVertical(3);

        setShowSelected(true);
        setSelectMode(EnumSelectMode.single);
        setAdapter(new DefaultAdapter(getActivity()));
    }

    public void setSelectMode(EnumSelectMode selectMode)
    {
        this.selectMode = selectMode;
        initSelectManager();
    }

    private void initSelectManager()
    {
        if (selectManager == null)
        {
            selectManager = new SDSelectManager<>();
            selectManager.setListener(new SDSelectManager.SDSelectManagerListener<ImageModel>()
            {
                @Override
                public void onNormal(int index, ImageModel item)
                {
                    item.setSelected(false);
                    adapter.updateData(index);
                    if (selectListener != null)
                    {
                        selectListener.onNormal(index, item);
                    }
                }

                @Override
                public void onSelected(int index, ImageModel item)
                {
                    item.setSelected(true);
                    adapter.updateData(index);
                    if (selectListener != null)
                    {
                        selectListener.onSelected(index, item);
                    }
                }
            });
        }

        if (selectMode == EnumSelectMode.multi)
        {
            selectManager.setMode(SDSelectManager.Mode.MULTI);
        } else
        {
            selectManager.setMode(SDSelectManager.Mode.SINGLE);
        }
    }

    public SDSelectManager<ImageModel> getSelectManager()
    {
        return selectManager;
    }

    public SDRecyclerView getRecyclerView()
    {
        return recyclerView;
    }

    public void setAdapter(SDRecyclerAdapter<ImageModel> adapter)
    {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public SDRecyclerAdapter<ImageModel> getAdapter()
    {
        return adapter;
    }

    public void loadImage()
    {
        if (localImageFinder == null)
        {
            localImageFinder = new LocalImageFinder((FragmentActivity) getActivity());
        }

        if (adapter != null)
        {
            localImageFinder.getLocalImage(loadListener);
        }
    }

    private LocalImageFinder.LocalImageFinderListener loadListener = new LocalImageFinder.LocalImageFinderListener()
    {
        @Override
        public void onResult(List<ImageModel> listImage)
        {
            if (adapter != null)
            {
                selectManager.setItems(listImage);
                adapter.updateData(listImage);
            }
        }
    };

    private class DefaultAdapter extends SDSimpleRecyclerAdapter<ImageModel>
    {
        public DefaultAdapter(Activity activity)
        {
            super(activity);
        }

        @Override
        public int getLayoutId(ViewGroup parent, int viewType)
        {
            return R.layout.item_sd_select_image;
        }

        @Override
        protected void initViewHolder(SDRecyclerViewHolder holder, int viewType, ViewGroup parent)
        {
            ImageView iv_image = holder.get(R.id.iv_image);

            int itemWidth = (SDViewUtil.getScreenWidth() / 3);
            SDViewUtil.setViewWidth(holder.itemView, itemWidth);
            SDViewUtil.setViewHeight(holder.itemView, itemWidth);

            int imageWidth = itemWidth - SDViewUtil.dp2px(4);
            SDViewUtil.setViewWidth(iv_image, imageWidth);
            SDViewUtil.setViewHeight(iv_image, imageWidth);
        }

        @Override
        public void bindData(SDRecyclerViewHolder holder, final int position, ImageModel model)
        {
            ImageView iv_image = holder.get(R.id.iv_image);
            ImageView iv_selected = holder.get(R.id.iv_selected);

            SDViewBinder.setImageView(getActivity(),ImageDownloader.Scheme.FILE.wrap(model.getUri()), iv_image);

            if (showSelected)
            {
                SDViewUtil.show(iv_selected);
                if (model.isSelected())
                {
                    iv_selected.setImageResource(R.drawable.ic_sd_select_image_selected);
                } else
                {
                    iv_selected.setImageResource(R.drawable.ic_sd_select_image_normal);
                }
            } else
            {
                SDViewUtil.hide(iv_selected);
            }

            iv_selected.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (clickSelectedListener != null)
                    {
                        boolean result = clickSelectedListener.onClick(position, getItemData(position), v);
                        if (result)
                        {
                            return;
                        }
                    }
                    selectManager.performClick(position);
                }
            });

            iv_image.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (clickImageListener != null)
                    {
                        clickImageListener.onClick(position, getItemData(position), v);
                    }
                }
            });
        }
    }

    public enum EnumSelectMode
    {
        /**
         * 单选
         */
        single,

        /**
         * 多选
         */
        multi;
    }

}
