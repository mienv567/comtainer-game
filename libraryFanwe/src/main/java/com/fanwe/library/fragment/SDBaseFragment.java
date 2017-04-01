package com.fanwe.library.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.listener.SDDispatchKeyEventListener;
import com.fanwe.library.listener.SDDispatchTouchEventListener;
import com.fanwe.library.listener.SDVisibilityStateListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

import java.util.List;

public abstract class SDBaseFragment extends Fragment implements SDEventObserver, OnClickListener, SDDispatchTouchEventListener,
        SDDispatchKeyEventListener
{

    private SDFragmentManager fragmentManager;
    private boolean isRemovedFromViewPager = false;
    private SDVisibilityStateListener visibilityStateListener;

    public void setVisibilityStateListener(SDVisibilityStateListener visibilityStateListener)
    {
        this.visibilityStateListener = visibilityStateListener;
    }

    public boolean isRemovedFromViewPager()
    {
        return isRemovedFromViewPager;
    }

    public void setIsRemovedFromViewPager(boolean isRemovedFromViewPager)
    {
        this.isRemovedFromViewPager = isRemovedFromViewPager;
    }

    public SDBaseFragment()
    {
        ensureArgumentsNotNull();
    }

    private void ensureArgumentsNotNull()
    {
        if (getArguments() == null)
        {
            super.setArguments(new Bundle());
        }
    }

    @Override
    public void setArguments(Bundle args)
    {
        ensureArgumentsNotNull();
        if (args != null)
        {
            getArguments().putAll(args);
        }
    }

    public SDFragmentManager getSDFragmentManager()
    {
        if (fragmentManager == null)
        {
            fragmentManager = new SDFragmentManager(getChildFragmentManager());
        }
        return fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        SDEventManager.register(this);
        SDBaseActivity activity = getSDBaseActivity();
        if (activity != null)
        {
            activity.getDispatchTouchEventListenerManager().add(this);
            activity.getDispatchKeyEventListenerManager().add(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View contentView = onCreateContentView(inflater, container, savedInstanceState);
        if (contentView == null)
        {
            int layoutId = onCreateContentView();
            if (layoutId != 0)
            {
                View layoutView = inflater.inflate(layoutId, container, false);
                contentView = addTitleView(layoutView);
            }
        }
        return contentView;
    }

    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return null;
    }

    /**
     * 此方法用于被重写返回fragment布局id
     *
     * @return
     */
    protected abstract int onCreateContentView();

    /**
     * 为contentView添加titleView
     *
     * @param contentView
     * @return
     */
    private View addTitleView(View contentView)
    {
        View viewFinal = contentView;

        View titleView = createTitleView();
        if (titleView != null)
        {
            LinearLayout linAll = new LinearLayout(getActivity());
            linAll.setOrientation(LinearLayout.VERTICAL);
            linAll.addView(titleView, createTitleViewLayoutParams());
            linAll.addView(contentView, createContentViewLayoutParams());
            viewFinal = linAll;
        }
        return viewFinal;
    }

    private View createTitleView()
    {
        View view = onCreateTitleView();
        if (view == null)
        {
            int resId = onCreateTitleViewResId();
            if (resId != 0)
            {
                view = LayoutInflater.from(getActivity()).inflate(resId, null);
            }
        }
        return view;
    }

    protected View onCreateTitleView()
    {
        return null;
    }

    protected int onCreateTitleViewResId()
    {
        return 0;
    }

    protected LinearLayout.LayoutParams createTitleViewLayoutParams()
    {
        return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    protected LinearLayout.LayoutParams createContentViewLayoutParams()
    {
        return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public SDBaseActivity getSDBaseActivity()
    {
        SDBaseActivity sdBaseActivity = null;
        if (getActivity() instanceof SDBaseActivity)
        {
            sdBaseActivity = (SDBaseActivity) getActivity();
        }
        return sdBaseActivity;
    }

    public boolean isEmpty(CharSequence content)
    {
        return TextUtils.isEmpty(content);
    }

    public boolean isEmpty(List<?> list)
    {
        return SDCollectionUtil.isEmpty(list);
    }

    @Override
    public void onDestroy()
    {
        SDEventManager.unregister(this);
        SDBaseActivity activity = getSDBaseActivity();
        if (activity != null)
        {
            activity.getDispatchTouchEventListenerManager().remove(this);
            activity.getDispatchKeyEventListenerManager().remove(this);
        }
        super.onDestroy();
    }

    /**
     * 调用此方法会触发onRefreshData()方法
     */
    public final void refreshData()
    {
        if (this.isAdded())
        {
            onRefreshData();
        }
    }

    /**
     * 调用refreshData()方法后触发此方法
     */
    protected void onRefreshData()
    {

    }

    public void hideFragmentView()
    {
        SDViewUtil.hide(getView());
        notifyVisibleState();
    }

    public void showFragmentView()
    {
        SDViewUtil.show(getView());
        notifyVisibleState();
    }

    public void invisibleFragmentView()
    {
        SDViewUtil.invisible(getView());
        notifyVisibleState();
    }

    public void notifyVisibleState()
    {
        View view = getView();
        if (view != null && visibilityStateListener != null)
        {
            int visibility = view.getVisibility();
            if (visibility == View.VISIBLE)
            {
                visibilityStateListener.onVisible(view);
            } else if (visibility == View.INVISIBLE)
            {
                visibilityStateListener.onInvisible(view);
            } else if (visibility == View.GONE)
            {
                visibilityStateListener.onGone(view);
            }
        }
    }

    public boolean toggleFragmentView(List<?> list)
    {
        if (list != null && !list.isEmpty())
        {
            showFragmentView();
            return true;
        } else
        {
            hideFragmentView();
            return false;
        }
    }

    public boolean toggleFragmentView(Object obj)
    {
        if (obj != null)
        {
            showFragmentView();
            return true;
        } else
        {
            hideFragmentView();
            return false;
        }
    }

    public boolean toggleFragmentView(String content)
    {
        if (!TextUtils.isEmpty(content))
        {
            showFragmentView();
            return true;
        } else
        {
            hideFragmentView();
            return false;
        }
    }

    public boolean toggleFragmentView(int show)
    {
        if (show == 1)
        {
            showFragmentView();
            return true;
        } else
        {
            hideFragmentView();
            return false;
        }
    }

    public boolean toggleFragmentView(boolean show)
    {
        if (show)
        {
            showFragmentView();
            return true;
        } else
        {
            hideFragmentView();
            return false;
        }
    }

    public View findViewById(int id)
    {
        View view = null;
        if (getActivity() != null)
        {
            view = getActivity().findViewById(id);
        }
        return view;
    }

    public void remove()
    {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    public boolean onBackPressed()
    {
        return false;
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public boolean dispatchTouchEventActivity(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean dispatchKeyEventActivity(KeyEvent event)
    {
        switch (event.getAction())
        {
            case KeyEvent.ACTION_DOWN:
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                {
                    return onBackPressed();
                }
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onEvent(SDBaseEvent event)
    {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event)
    {

    }

    @Override
    public void onEventBackgroundThread(SDBaseEvent event)
    {

    }

    @Override
    public void onEventAsync(SDBaseEvent event)
    {

    }
}
