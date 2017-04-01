package com.fanwe.live.animator.handler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.live.animator.SDAnimator;

public class SDAnimatorHandler
{
	private View view;
	private SDAnimator model;

	public SDAnimatorHandler(View view)
	{
		super();
		this.view = view;
	}

	public final AnimatorSet parse(SDAnimator model)
	{
		this.model = model;
		return createAnimation(model);
	}

	protected AnimatorSet createAnimation(SDAnimator model)
	{
		AnimatorSet animator = null;
		if (model != null)
		{
			switch (model.getType())
			{
			case SDAnimator.TYPE_ALPHA:
				animator = createAlpha(model);
				break;
			case SDAnimator.TYPE_SCALE:
				animator = createScale(model);
				break;
			case SDAnimator.TYPE_TRANSLATE:
				animator = createTranslate(model);
				break;

			default:
				break;
			}
		}
		return animator;
	}

	protected AnimatorSet createAlpha(SDAnimator model)
	{
		AnimatorSet animatorSet = new AnimatorSet();

		String propertyAlpha = SDAnimationUtil.getPropertyAlpha();
		float start = model.getStart_alpha();
		float end = model.getEnd_alpha();

		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, propertyAlpha, start, end);

		animatorSet.playTogether(alpha);
		long duration = model.getDuration();
		animatorSet.setDuration(duration);
		Interpolator interpolator = createInterpolator(model);
		if (interpolator != null)
		{
			animatorSet.setInterpolator(interpolator);
		}

		return animatorSet;
	}

	protected AnimatorSet createScale(SDAnimator model)
	{
		AnimatorSet animatorSet = new AnimatorSet();

		String propertyScaleX = SDAnimationUtil.getPropertyScaleX();
		String propertyScaleY = SDAnimationUtil.getPropertyScaleY();
		float start = model.getStart_scale();
		float end = model.getEnd_scale();

		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, propertyScaleX, start, end);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, propertyScaleY, start, end);

		animatorSet.playTogether(scaleX, scaleY);
		long duration = model.getDuration();
		animatorSet.setDuration(duration);
		Interpolator interpolator = createInterpolator(model);
		if (interpolator != null)
		{
			animatorSet.setInterpolator(interpolator);
		}

		return animatorSet;
	}

	protected AnimatorSet createTranslate(SDAnimator model)
	{
		AnimatorSet animatorSet = new AnimatorSet();

		String propertyTranslationX = SDAnimationUtil.getPropertyTranslationX();
		String propertyTranslationY = SDAnimationUtil.getPropertyTranslationY();

		float startX = model.getStartPosition().getX();
		float startY = model.getStartPosition().getY();

		float endX = model.getEndPosition().getX();
		float endY = model.getEndPosition().getY();

		ObjectAnimator translationX = ObjectAnimator.ofFloat(view, propertyTranslationX, startX, endX);
		ObjectAnimator translationY = ObjectAnimator.ofFloat(view, propertyTranslationY, startY, endY);

		animatorSet.playTogether(translationX, translationY);

		long duration = model.getDuration();
		animatorSet.setDuration(duration);
		Interpolator interpolator = createInterpolator(model);
		if (interpolator != null)
		{
			animatorSet.setInterpolator(interpolator);
		}

		return animatorSet;
	}

	protected Interpolator createInterpolator(SDAnimator model)
	{
		Interpolator interpolator = null;
		switch (model.getInterpolator())
		{
		case SDAnimator.INTERPOLATOR_DECELERATE:
			interpolator = new DecelerateInterpolator();
			break;
		case SDAnimator.INTERPOLATOR_UNIFORM:

			break;
		case SDAnimator.INTERPOLATOR_ACCELERATE:
			interpolator = new AccelerateInterpolator();
			break;

		default:
			break;
		}
		return interpolator;
	}

}
