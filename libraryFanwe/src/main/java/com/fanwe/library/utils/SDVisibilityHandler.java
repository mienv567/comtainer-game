package com.fanwe.library.utils;

import android.animation.Animator;
import android.view.View;

import com.fanwe.library.listener.SDVisibilityStateListener;

/**
 * Created by Administrator on 2016/8/4.
 */
public class SDVisibilityHandler
{

    private View view;

    private Animator showAnimator;
    private Animator hideAnimator;

    private boolean isInShowAnimator;
    private boolean isInHideAnimator;

    private boolean hideMode = true;

    private SDVisibilityStateListener visibilityStateListener;

    public void setView(View view)
    {
        this.view = view;
    }

    public void setVisibilityStateListener(SDVisibilityStateListener visibilityStateListener)
    {
        this.visibilityStateListener = visibilityStateListener;
    }

    public void setShowAnimator(Animator showAnimator)
    {
        this.showAnimator = showAnimator;
        if (showAnimator != null)
        {
            showAnimator.addListener(showListener);
        }
    }

    public void setHideAnimator(Animator hideAnimator)
    {
        this.hideAnimator = hideAnimator;
        if (hideAnimator != null)
        {
            hideAnimator.addListener(hideListener);
        }
    }

    //show
    public void show(boolean anim)
    {
        if (anim)
        {
            startShowAnimator();
        } else
        {
            showViewInside();
        }
    }

    private void showViewInside()
    {
        SDViewUtil.show(view);
        notifyVisibleStateListener();
    }

    private void startShowAnimator()
    {
        if (isInShowAnimator)
        {
            return;
        }

        if (showAnimator != null)
        {
            isInShowAnimator = true;
            showAnimator.start();
        }
    }

    // hide
    public void hide(boolean anim)
    {
        if (anim)
        {
            startHideAnimator(true);
        } else
        {
            hideViewInside();
        }
    }

    private void hideViewInside()
    {
        SDViewUtil.hide(view);
        notifyVisibleStateListener();
    }

    private void startHideAnimator(boolean hideMode)
    {
        if (isInHideAnimator)
        {
            return;
        }

        if (hideAnimator != null)
        {
            isInHideAnimator = true;
            this.hideMode = hideMode;
            hideAnimator.start();
        }
    }

    // invisible
    public void invisible(boolean anim)
    {
        if (anim)
        {
            startHideAnimator(false);
        } else
        {
            invisibleViewInside();
        }
    }

    public void invisibleViewInside()
    {
        SDViewUtil.invisible(view);
        notifyVisibleStateListener();
    }

    /**
     * 显示监听
     */
    private Animator.AnimatorListener showListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            isInShowAnimator = true;
            showViewInside();
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            isInShowAnimator = false;
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            isInShowAnimator = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    /**
     * 隐藏监听
     */
    private Animator.AnimatorListener hideListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            isInHideAnimator = true;
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            isInHideAnimator = false;
            if (hideMode)
            {
                hideViewInside();
            } else
            {
                invisibleViewInside();
            }
            SDViewUtil.resetView(view);
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            isInHideAnimator = false;
            if (hideMode)
            {
                hideViewInside();
            } else
            {
                invisibleViewInside();
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    public final void notifyVisibleStateListener()
    {
        if (view != null && visibilityStateListener != null)
        {
            switch (view.getVisibility())
            {
                case View.GONE:
                    visibilityStateListener.onGone(view);
                    break;
                case View.VISIBLE:
                    visibilityStateListener.onVisible(view);
                    break;
                case View.INVISIBLE:
                    visibilityStateListener.onInvisible(view);
                    break;

                default:
                    break;
            }
        }
    }


    public Animator getShowAnimator()
    {
        return showAnimator;
    }


    public Animator getHideAnimator()
    {
        return hideAnimator;
    }

}
