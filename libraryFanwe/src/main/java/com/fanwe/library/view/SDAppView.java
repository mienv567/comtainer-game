package com.fanwe.library.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.event.EOnActivityResult;
import com.fanwe.library.listener.SDActivityLifeCircleListener;
import com.fanwe.library.listener.SDDispatchKeyEventListener;
import com.fanwe.library.listener.SDDispatchTouchEventListener;
import com.fanwe.library.listener.SDVisibilityStateListener;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.SDVisibilityHandler;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

import de.greenrobot.event.EventBus;

/**
 * 如果手动的new对象的话Context必须传入Activity对象
 *
 * @author Administrator
 * @date 2016-6-29 上午11:27:25
 */
public class SDAppView extends LinearLayout implements View.OnClickListener, SDEventObserver, SDActivityLifeCircleListener, SDVisibilityStateListener, SDDispatchKeyEventListener, SDDispatchTouchEventListener
{

    private Activity activity;
    private SDBaseActivity sdBaseActivity;
    private SDVisibilityHandler visibilityHandler;
    private SDVisibilityStateListener visibilityStateListener;
    protected  boolean mEventBySelf = false;
    private boolean isRegisterEventBus = false;
    private boolean interceptTouchEvent = false;

    public SDAppView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        baseInit();
    }

    public SDAppView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        baseInit();
    }

    public SDAppView(Context context)
    {
        super(context);
        baseInit();
    }

    public void setInterceptTouchEvent(boolean interceptTouchEvent)
    {
        this.interceptTouchEvent = interceptTouchEvent;
    }

    public void setVisibilityStateListener(SDVisibilityStateListener visibilityStateListener)
    {
        this.visibilityStateListener = visibilityStateListener;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public Activity getActivity()
    {
        return activity;
    }

    private void baseInit()
    {
        if (getContext() instanceof Activity)
        {
            activity = (Activity) getContext();
        }

        int layoutId = onCreateContentView();
        if (layoutId != 0)
        {
            setContentView(layoutId);
        }

        visibilityHandler = new SDVisibilityHandler();
        visibilityHandler.setView(this);
        visibilityHandler.setVisibilityStateListener(this);

        setShowAnimator(SDAnimationUtil.alphaIn(this));
        setHideAnimator(SDAnimationUtil.alphaOut(this));

        baseConstructorInit();
    }

    /**
     * 基类构造方法调用的初始化方法，如果重写此方法初始化需要注意：
     * 1.子类不应该在此方法内访问子类定义属性时候直接new的属性，如：private String value = "value"，否则value的值将为null
     * 2.子类应该在此方法内初始化属性
     */
    protected void baseConstructorInit()
    {

    }

    /**
     * 重写此方法返回布局id
     *
     * @return
     */
    protected int onCreateContentView()
    {
        return 0;
    }

    public void setShowAnimator(Animator animator)
    {
        visibilityHandler.setShowAnimator(animator);
    }

    public void setHideAnimator(Animator animator)
    {
        visibilityHandler.setHideAnimator(animator);
    }

    public void setContentView(int layoutId)
    {
        LayoutInflater.from(getContext()).inflate(layoutId, this, true);
    }

    public <V extends View> V find(int id)
    {
        View view = findViewById(id);
        return (V) view;
    }

    /**
     * 把自己移除掉
     */
    public void remove()
    {
        SDViewUtil.removeViewFromParent(this);
    }

    public void removeView(View view)
    {
        SDViewUtil.removeViewFromParent(view);
    }

    public void replaceView(ViewGroup parent, View child)
    {
        SDViewUtil.replaceView(parent, child);
    }

    public void replaceView(ViewGroup parent, View child, ViewGroup.LayoutParams params)
    {
        SDViewUtil.replaceView(parent, child, params);
    }

    /**
     * 为了统一规范，子类可以重写此方法初始化，并在合适时间调用
     */
    protected void init()
    {

    }

    // show
    public void show()
    {
        visibilityHandler.show(false);
    }

    public void show(boolean anim)
    {
        visibilityHandler.show(anim);
    }

    // hide
    public void hide()
    {
        visibilityHandler.hide(false);
    }

    public void hide(boolean anim)
    {
        visibilityHandler.hide(anim);
    }

    // invisible
    public void invisible()
    {
        visibilityHandler.invisible(false);
    }

    public void invisible(boolean anim)
    {
        visibilityHandler.invisible(anim);
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    }

    @Override
    public void onStart()
    {
    }

    @Override
    public void onRestart()
    {
    }

    @Override
    public void onResume()
    {
    }

    @Override
    public void onPause()
    {
    }

    @Override
    public void onStop()
    {
    }

    @Override
    public void onDestroy()
    {
    }

    @Override
    public void onClick(View v)
    {

    }

    public void onEventMainThread(EOnActivityResult event)
    {
    }

    public boolean isVisible()
    {
        return View.VISIBLE == getVisibility();
    }

    @Override
    public void onVisible(View view)
    {
        if (visibilityStateListener != null)
        {
            visibilityStateListener.onVisible(view);
        }
    }

    @Override
    public void onGone(View view)
    {
        if (visibilityStateListener != null)
        {
            visibilityStateListener.onGone(view);
        }
    }

    @Override
    public void onInvisible(View view)
    {
        if (visibilityStateListener != null)
        {
            visibilityStateListener.onInvisible(view);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (interceptTouchEvent)
        {
            super.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEventActivity(MotionEvent ev)
    {
        if (isVisible())
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if (SDViewUtil.isTouchView(this, ev))
                    {
                        if (onTouchDownInside(ev))
                        {
                            return true;
                        }
                    } else
                    {
                        if (onTouchDownOutside(ev))
                        {
                            return true;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        return false;
    }

    protected boolean onTouchDownOutside(MotionEvent ev)
    {
        return false;
    }

    protected boolean onTouchDownInside(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean dispatchKeyEventActivity(KeyEvent event)
    {
        if (isVisible())
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
        }
        return false;
    }

    public boolean onBackPressed()
    {
        return false;
    }

    public void registerEventBus()
    {
        if (!isRegisterEventBus)
        {
            EventBus.getDefault().register(this);
            isRegisterEventBus = true;
        }
    }

    public void unregisterEventBus()
    {
        EventBus.getDefault().unregister(this);
        isRegisterEventBus = false;
    }

    @Override
    protected void onAttachedToWindow()
    {
        if(!mEventBySelf && !EventBus.getDefault().isRegistered(this)) {
            SDEventManager.register(this);
        }
        if (activity instanceof SDBaseActivity)
        {
            sdBaseActivity = (SDBaseActivity) activity;
            sdBaseActivity.registerAppView(this);
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        if(!mEventBySelf && EventBus.getDefault().isRegistered(this)) {
            unregisterEventBus();
        }
        if (sdBaseActivity != null)
        {
            sdBaseActivity.unregisterAppView(this);
        }
        super.onDetachedFromWindow();
    }

    public void showProgressDialog(String msg)
    {
        if (sdBaseActivity != null)
        {
            sdBaseActivity.showProgressDialog(msg);
        }
    }

    public void dismissProgressDialog()
    {
        if (sdBaseActivity != null)
        {
            sdBaseActivity.dismissProgressDialog();
        }
    }

    public SDDialogProgress getProgressDialog()
    {
        if (sdBaseActivity != null)
        {
            return sdBaseActivity.getProgressDialog();
        }
        return null;
    }

}
