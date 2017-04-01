package com.fanwe.library.utils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SDViewBinder
{

	public static boolean mCanLoadImageFromUrl = true;

	public static void setRatingBar(RatingBar ratingBar, float rating)
	{
		ratingBar.setRating(rating);
	}

	public static void setTextView(TextView textView, CharSequence content, CharSequence emptyTip)
	{
		if (!TextUtils.isEmpty(content))
		{
			textView.setText(content);
		} else
		{
			if (!TextUtils.isEmpty(emptyTip))
			{
				textView.setText(emptyTip);
			} else
			{
				textView.setText("");
			}
		}
	}

	public static void setTextView(TextView textView, CharSequence content)
	{
		setTextView(textView, content, null);
	}

	public static void setTextView(EditText editText, CharSequence content)
	{
		if (!TextUtils.isEmpty(content))
		{
			editText.setText(content);
		} else
		{
			editText.setText("");
		}
	}

	public static void setTextViewHtml(TextView textView, String contentHtml)
	{
		setTextViewHtml(textView, contentHtml, null);
	}

	public static void setTextViewHtml(TextView textView, String contentHtml, String emptyTip)
	{
		CharSequence content = contentHtml;
		if (!TextUtils.isEmpty(contentHtml))
		{
			content = Html.fromHtml(contentHtml);
		}
		setTextView(textView, content, emptyTip);
	}

	public static void setTextViewsVisibility(TextView textView, CharSequence content)
	{
		if (TextUtils.isEmpty(content))
		{
			SDViewUtil.hide(textView);
		} else
		{
			textView.setText(content);
			SDViewUtil.show(textView);
		}
	}

	public static void setImageViewsVisibility(ImageView imageView, int resId)
	{
		if (resId <= 0)
		{
			SDViewUtil.hide(imageView);
		} else
		{
			imageView.setImageResource(resId);
			SDViewUtil.show(imageView);
		}
	}

	public static boolean setViewsVisibility(View view, boolean visible)
	{
		if (visible)
		{
			SDViewUtil.show(view);
		} else
		{
			SDViewUtil.hide(view);
		}
		return visible;
	}

	public static boolean setViewsVisibility(View view, int visible)
	{
		if (visible == 1)
		{
			SDViewUtil.show(view);
			return true;
		} else
		{
			SDViewUtil.hide(view);
			return false;
		}
	}

	public static void setImageViewResource(ImageView imageView, int resId, boolean setZeroResId)
	{
		if (resId == 0)
		{
			if (setZeroResId)
			{
				imageView.setImageResource(resId);
			} else
			{

			}
		} else
		{
			imageView.setImageResource(resId);
		}
	}

	public static void setImageView(Context context,ImageView imageView, String url)
	{
		setImageView(context, url, imageView, false);
	}

	public static void setImageView(Context context,String uri, ImageView imageView)
	{
		setImageView(context,uri,imageView,false);
	}

	public static void setImageView(Context context,String uri, ImageView imageView,boolean gif){
		setImageView(context,uri,imageView,gif,false);
	}

	public static void setImageView(Context context,String uri, ImageView imageView,boolean gif,boolean firstFrame){
		if(gif){
			if(firstFrame){
				Glide.with(context).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate().into(imageView);
			}else{
				Glide.with(context).load(uri).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).dontAnimate().into(imageView);
			}
		}else{
			Glide.with(context).load(uri).dontAnimate()
					.into(imageView);
		}
	}

	public static void setImageView(Context context,String url,ImageView imageView,int resourceId){
		Glide.with(context).load(url).dontAnimate().placeholder(resourceId)
				.into(imageView);
	}

	public static void setImageView(Context context,ImageView imageView,String url,int resourceId){
		setImageView(context, url, imageView, resourceId);
	}

	public static void setImageView(Context context,ImageView imageView, String url,Transformation transformation){
		Glide.with(context).load(url).dontAnimate().bitmapTransform(transformation)
				.into(imageView);
	}

	public static void setRoundImageView(Context context,ImageView imageView,String url,int resourceId){
		Glide.with(context).load(url).
				bitmapTransform(new RoundedCornersTransformation(context, SDViewUtil.dp2px(15),0,RoundedCornersTransformation.CornerType.ALL)).
				placeholder(resourceId).
				dontAnimate().
				into(imageView);
	}


}
