package com.fanwe.library.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/8/30.
 */
public class SDRecyclerView extends RecyclerView
{
    public SDRecyclerView(Context context)
    {
        super(context);
        init();
    }

    public SDRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        setLinearVertical();
    }

    public boolean isFirstItemCompletelyVisible()
    {
        boolean result = false;
        int count = getItemCount();
        if (count > 0)
        {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                if (getLinearLayoutManager().findFirstCompletelyVisibleItemPosition() == 0)
                {
                    result = true;
                }
            } else if (layoutManager instanceof GridLayoutManager)
            {
                if (getLinearLayoutManager().findFirstCompletelyVisibleItemPosition() == 0)
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean isLastItemCompletelyVisible()
    {
        boolean result = false;

        int count = getItemCount();
        if (count > 0)
        {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager)
            {
                if (getLinearLayoutManager().findLastCompletelyVisibleItemPosition() == count - 1)
                {
                    result = true;
                }
            } else if (layoutManager instanceof GridLayoutManager)
            {
                if (getLinearLayoutManager().findLastCompletelyVisibleItemPosition() == count - 1)
                {
                    result = true;
                }
            }
        }
        return result;
    }

    public int getItemCount()
    {
        Adapter adapter = getAdapter();
        if (adapter != null)
        {
            return adapter.getItemCount();
        }
        return 0;
    }

    // Linear
    public void setLinearVertical()
    {
        setLinearOrientation(RecyclerView.VERTICAL);
    }

    public void setLinearHorizontal()
    {
        setLinearOrientation(RecyclerView.HORIZONTAL);
    }

    private void setLinearOrientation(int orientation)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        if (orientation == RecyclerView.VERTICAL || orientation == RecyclerView.HORIZONTAL)
        {
            layoutManager.setOrientation(orientation);
            setLayoutManager(layoutManager);
        }
    }

    public LinearLayoutManager getLinearLayoutManager()
    {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager)
        {
            return (LinearLayoutManager) layoutManager;
        }
        return null;
    }

    // Grid
    public void setGridVertical(int spanCount)
    {
        setGridOrientation(RecyclerView.VERTICAL, spanCount);
    }

    public void setGridHorizontal(int spanCount)
    {
        setGridOrientation(RecyclerView.HORIZONTAL, spanCount);
    }

    private void setGridOrientation(int orientation, int spanCount)
    {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        if (orientation == RecyclerView.VERTICAL || orientation == RecyclerView.HORIZONTAL)
        {
            layoutManager.setOrientation(orientation);
            setLayoutManager(layoutManager);
        }
    }

    public GridLayoutManager getGridLayoutManager()
    {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            return (GridLayoutManager) layoutManager;
        }
        return null;
    }

    // scroll
    public void scrollToStart()
    {
        scrollToPosition(0);
    }

    public void scrollToStartDelayed(long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                scrollToStart();
            }
        }, delay);
    }

    public void scrollToEnd()
    {
        int count = getItemCount();
        if (count > 0)
        {
            scrollToPosition(count - 1);
        }
    }

    public void scrollToEndDelayed(long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                scrollToEnd();
            }
        }, delay);
    }

    public void scrollToPositionDelayed(final int position, long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                scrollToPosition(position);
            }
        }, delay);
    }

    public void smoothScrollToPositionDelayed(final int position, long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                smoothScrollToPosition(position);
            }
        }, delay);
    }
}
