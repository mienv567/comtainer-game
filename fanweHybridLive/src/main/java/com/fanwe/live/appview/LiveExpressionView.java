package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.customview.SDViewPager;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.appview.SDSlidingPlayViewHandler.BindDataListener;
import com.fanwe.live.model.LiveExpressionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 私聊界面，表情布局
 */
public class LiveExpressionView extends BaseAppView implements ILivePrivateChatMoreView
{

    private SDSlidingPlayView spv_content;
    private ImageView iv_delete;

    private SDSlidingPlayViewHandler<LiveExpressionModel> viewHandler;
    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public LiveExpressionView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveExpressionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveExpressionView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_expression);
        spv_content = find(R.id.spv_content);
        iv_delete = find(R.id.iv_delete);

        viewHandler = new SDSlidingPlayViewHandler<LiveExpressionModel>();

        iv_delete.setOnClickListener(this);
        viewHandler.setBindDataListener(new BindDataListener<LiveExpressionModel>()
        {
            @Override
            public void bindData(int position, View convertView, ViewGroup parent, final LiveExpressionModel model)
            {
                ImageView iv_image = ViewHolder.get(R.id.iv_image, convertView);
                iv_image.setImageResource(model.getResId());
                iv_image.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        if (clickListener != null)
                        {
                            clickListener.onClickExpression(model);
                        }
                    }
                });
            }

            @Override
            public int getLayoutId(int position, View convertView, ViewGroup parent)
            {
                return R.layout.item_live_expression;
            }
        });

        spv_content.setNormalImageResId(R.drawable.ic_point_normal_dark);
        spv_content.setSelectedImageResId(R.drawable.ic_point_selected_main_color);
        SDViewUtil.setViewMarginBottom(spv_content.vpg_content, SDViewUtil.dp2px(20));
        List<LiveExpressionModel> listModel = createExpressionModel();
        setData(listModel);
    }

    @Override
    public void onClick(View v)
    {
        if (v == iv_delete)
        {
            clickDelete();
        }
        super.onClick(v);
    }

    private void clickDelete()
    {
        if (clickListener != null)
        {
            clickListener.onClickDelete();
        }
    }

    private List<LiveExpressionModel> createExpressionModel()
    {
        List<LiveExpressionModel> listModel = new ArrayList<LiveExpressionModel>();
        String name = "face";


        for (int i = 1; i <= 1000; i++)
        {
            int resId = SDViewUtil.getIdentifierDrawable(name + i);
            if (resId != 0)
            {
                LiveExpressionModel model = new LiveExpressionModel();
                model.setName(name + i);
                model.setKey("[" + name + i + "]");
                model.setResId(resId);
                listModel.add(model);
            } else
            {
                break;
            }
        }
        return listModel;
    }

    public void setData(List<LiveExpressionModel> listModel)
    {
        viewHandler.bindData(spv_content, listModel, 7, 21, getActivity());
    }

    @Override
    public void setContentMatchParent()
    {
        SDViewUtil.setViewHeightWeightContent(spv_content, 1);
        spv_content.setContentMatchParent();
    }

    @Override
    public void setContentWrapContent()
    {
        SDViewUtil.setViewHeightWrapContent(spv_content);
        spv_content.setContentWrapContent();
    }

    public interface ClickListener
    {
        void onClickDelete();

        void onClickExpression(LiveExpressionModel model);
    }

}
