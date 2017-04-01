package com.fanwe.library.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.event.EKeyboardVisibilityChange;
import com.fanwe.library.event.EOnActivityResult;
import com.fanwe.library.event.EOnBackground;
import com.fanwe.library.event.EOnResumeFromBackground;
import com.fanwe.library.listener.SDActivityLifeCircleListener;
import com.fanwe.library.listener.SDDispatchKeyEventListener;
import com.fanwe.library.listener.SDDispatchTouchEventListener;
import com.fanwe.library.listener.SDIterateListener;
import com.fanwe.library.listener.SDSizeListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDListenerManager;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.SDWindowSizeListener;
import com.fanwe.library.view.SDAppView;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

import java.util.Iterator;
import java.util.List;

public class SDBaseActivity extends FragmentActivity implements SDEventObserver, OnClickListener {

    private FrameLayout flRootLayout;
    private SDFragmentManager fragmentManager;
    protected SDBaseActivity mActivity;
    private static boolean isBackground = false;
    private long backgroundTime;
    private SDWindowSizeListener windowSizeListener = new SDWindowSizeListener();
    private SDDialogProgress progressDialog;
    private SDListenerManager<SDActivityLifeCircleListener> activityLifeCircleListenerManager = new SDListenerManager<>();
    private SDListenerManager<SDDispatchTouchEventListener> dispatchTouchEventListenerManager = new SDListenerManager<>();
    private SDListenerManager<SDDispatchKeyEventListener> dispatchKeyEventListenerManager = new SDListenerManager<>();

    public static boolean isBackground() {
        return isBackground;
    }

    public long getBackgroundTime() {
        return backgroundTime;
    }

    //SDListenerManager
    public SDListenerManager<SDActivityLifeCircleListener> getActivityLifeCircleListenerManager() {
        return activityLifeCircleListenerManager;
    }

    public SDListenerManager<SDDispatchTouchEventListener> getDispatchTouchEventListenerManager() {
        return dispatchTouchEventListenerManager;
    }

    public SDListenerManager<SDDispatchKeyEventListener> getDispatchKeyEventListenerManager() {
        return dispatchKeyEventListenerManager;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        initSystemBar();

        flRootLayout = (FrameLayout) findViewById(android.R.id.content);
        SDActivityManager.getInstance().onCreate(this);
        SDEventManager.register(this);
        afterOnCreater(savedInstanceState);

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onCreate(savedInstanceState);
                return false;
            }
        });
    }

    /**
     * 初始化系统状态栏的颜色
     */
    protected void initSystemBar() {

    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void afterOnCreater(Bundle savedInstanceState) {
        initWindowSizeListener();
        baseInit();

        int layoutId = onCreateContentView();
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        init(savedInstanceState);
    }

    /**
     * 返回布局activity布局id，基类调用的顺序：onCreateContentView()->setContentView()->init()
     *
     * @return
     */
    protected int onCreateContentView() {
        return 0;
    }

    /**
     * 重写此方法初始化，如果没有重写onCreateContentView()方法，则要手动调用setContentView()设置activity布局;
     *
     * @param savedInstanceState
     */
    protected void init(Bundle savedInstanceState) {

    }

    /**
     * 此方法一般用于初始化一些基类需要初始化的操作
     */
    protected void baseInit() {

    }

    private void initWindowSizeListener() {
        windowSizeListener.listen(this, new SDSizeListener<View>() {
            @Override
            public void onWidthChanged(int newWidth, int oldWidth, int differ, View view) {
            }

            @Override
            public void onHeightChanged(int newHeight, int oldHeight, int differ, View view) {
                if (oldHeight > 0 && newHeight > 0) {
                    int absDiffer = Math.abs(differ);
                    if (absDiffer > 400) {
                        if (differ > 0) {
                            //键盘收起
                            onKeyboardVisibilityChange(false, absDiffer);
                        } else {
                            // 键盘弹出
                            onKeyboardVisibilityChange(true, absDiffer);
                        }
                    }
                }
            }
        });
    }

    protected void onKeyboardVisibilityChange(boolean visible, int height) {
        EKeyboardVisibilityChange event = new EKeyboardVisibilityChange();
        event.visible = visible;
        event.height = height;
        SDEventManager.post(event);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V find(int id) {
        View view = findViewById(id);
        return (V) view;
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = getLayoutInflater().inflate(layoutResID, null);
        setContentView(contentView);
    }

    /**
     * 设置窗口特征:常亮、无title、方向等
     */
    protected void initWindow() {

    }

    @Override
    public void setContentView(View view) {
        View contentView = addTitleView(view);
        contentView.setFitsSystemWindows(true);
        super.setContentView(contentView);
    }

    /**
     * 为contentView添加titleView
     *
     * @param contentView
     * @return
     */
    private View addTitleView(View contentView) {
        View viewFinal = contentView;

        View titleView = createTitleView();
        if (titleView != null) {
            LinearLayout linAll = new LinearLayout(this);
            linAll.setOrientation(LinearLayout.VERTICAL);
            linAll.addView(titleView, createTitleViewLayoutParams());
            linAll.addView(contentView, createContentViewLayoutParams());
            viewFinal = linAll;
        }
        return viewFinal;
    }

    private View createTitleView() {
        View view = onCreateTitleView();
        if (view == null) {
            int resId = onCreateTitleViewResId();
            if (resId != 0) {
                view = LayoutInflater.from(this).inflate(resId, null);
            }
        }
        return view;
    }

    protected View onCreateTitleView() {
        return null;
    }

    protected int onCreateTitleViewResId() {
        return 0;
    }

    protected LinearLayout.LayoutParams createTitleViewLayoutParams() {
        return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    protected LinearLayout.LayoutParams createContentViewLayoutParams() {
        return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public SDFragmentManager getSDFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = new SDFragmentManager(getSupportFragmentManager());
        }
        return fragmentManager;
    }

    public boolean isEmpty(CharSequence content) {
        return TextUtils.isEmpty(content);
    }

    public boolean isEmpty(List<?> list) {
        return SDCollectionUtil.isEmpty(list);
    }

    @Override
    protected void onStart() {
        super.onStart();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onStart();
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onRestart();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        SDActivityManager.getInstance().onResume(this);
        if (isBackground) {
            isBackground = false;
            onResumeFromBackground();
            SDEventManager.post(new EOnResumeFromBackground());
            backgroundTime = 0;
        }
        super.onResume();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onResume();
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onPause();
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        if (!isBackground) {
            if (SDPackageUtil.isBackground()) {
                isBackground = true;
                backgroundTime = System.currentTimeMillis();
                onBackground();
                SDEventManager.post(new EOnBackground());
            }
        }
        super.onStop();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onStop();
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        SDActivityManager.getInstance().onDestroy(this);
        SDEventManager.unregister(this);
        super.onDestroy();

        activityLifeCircleListenerManager.foreach(new SDIterateListener<SDActivityLifeCircleListener>() {
            @Override
            public boolean next(int i, SDActivityLifeCircleListener item, Iterator<SDActivityLifeCircleListener> it) {
                item.onDestroy();
                return false;
            }
        });
    }


    @Override
    public void finish() {
        SDActivityManager.getInstance().onDestroy(this);
        super.finish();
    }

    /**
     * 进入后台时候回调
     */
    protected void onBackground() {

    }

    /**
     * 从后台回到前台回调
     */
    protected void onResumeFromBackground() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            if (outState != null) {
                outState.remove("android:support:fragments");
            }
        } catch (Exception e) {
            onSaveInstanceStateException(e);
        }
    }

    protected void onSaveInstanceStateException(Exception e) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            onRestoreInstanceStateException(e);
        }
    }

    protected void onRestoreInstanceStateException(Exception e) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        postOnActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void postOnActivityResult(int requestCode, int resultCode, Intent data) {
        EOnActivityResult event = new EOnActivityResult();
        event.activity = this;
        event.data = data;
        event.requestCode = requestCode;
        event.resultCode = resultCode;
        SDEventManager.post(event);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        boolean notifyResult = dispatchTouchEventListenerManager.foreachReverse(new SDIterateListener<SDDispatchTouchEventListener>() {
            @Override
            public boolean next(int i, SDDispatchTouchEventListener item, Iterator<SDDispatchTouchEventListener> it) {
                return item.dispatchTouchEventActivity(ev);
            }
        });

        if (notifyResult) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        boolean notifyResult = dispatchKeyEventListenerManager.foreachReverse(new SDIterateListener<SDDispatchKeyEventListener>() {
            @Override
            public boolean next(int i, SDDispatchKeyEventListener item, Iterator<SDDispatchKeyEventListener> it) {
                return item.dispatchKeyEventActivity(event);
            }
        });

        if (notifyResult) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new SDDialogProgress(this);
        }
        progressDialog.setTextMsg(msg);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public SDDialogProgress getProgressDialog() {
        return progressDialog;
    }

    public void removeView(View view) {
        SDViewUtil.removeViewFromParent(view);
    }

    public void replaceView(ViewGroup parent, View child) {
        SDViewUtil.replaceView(parent, child);
    }


    public void replaceView(ViewGroup parent, View child, ViewGroup.LayoutParams params) {
        SDViewUtil.replaceView(parent, child, params);
    }

    public void addView(View view) {
        SDViewUtil.addView(flRootLayout, view);
    }

    public void addView(View view, FrameLayout.LayoutParams params) {
        SDViewUtil.addView(flRootLayout, view, params);
    }

    public void registerAppView(SDAppView view) {
        if (view != null) {
            dispatchKeyEventListenerManager.add(view);
            dispatchTouchEventListenerManager.add(view);
            activityLifeCircleListenerManager.add(view);
        }
    }

    public void unregisterAppView(SDAppView view) {
        if (view != null) {
            dispatchKeyEventListenerManager.remove(view);
            dispatchTouchEventListenerManager.remove(view);
            activityLifeCircleListenerManager.remove(view);
        }
    }

    public void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onEvent(SDBaseEvent event) {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {

    }

    @Override
    public void onEventBackgroundThread(SDBaseEvent event) {

    }

    @Override
    public void onEventAsync(SDBaseEvent event) {

    }

    @Override
    public void onClick(View v) {

    }
}
