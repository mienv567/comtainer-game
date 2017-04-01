package com.qy.ysys.yishengyishi.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class SDPullToRefreshGridRecyclerView extends PullToRefreshBase<SDRecyclerView>
{

    public SDPullToRefreshGridRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SDPullToRefreshGridRecyclerView(Context context, Mode mode,
                                           AnimationStyle style)
    {
        super(context, mode, style);
    }

    public SDPullToRefreshGridRecyclerView(Context context, Mode mode)
    {
        super(context, mode);
    }

    public SDPullToRefreshGridRecyclerView(Context context)
    {
        super(context);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection()
    {
        return Orientation.VERTICAL;
    }

    @Override
    protected SDRecyclerView createRefreshableView(Context context, AttributeSet attrs)
    {
        SDRecyclerView listView = new SDRecyclerView(context, attrs);
        listView.setGridVertical(2);
        listView.setId(com.handmark.pulltorefresh.library.R.id.recyclerview);
        return listView;
    }



    @Override
    protected boolean isReadyForPullEnd()
    {
        int count = getRefreshableView().getItemCount();
        if (count <= 0)
        {
            return true;
        } else
        {
            return getRefreshableView().isLastItemCompletelyVisible();
        }
    }

    @Override
    protected boolean isReadyForPullStart()
    {
        int count = getRefreshableView().getItemCount();
        if (count <= 0)
        {
            return true;
        } else
        {
            return getRefreshableView().isFirstItemCompletelyVisible();
        }
    }

}