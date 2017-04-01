package com.fanwe.library.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDCollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class SDGestureTouchView extends View
{
	private static final int TIME_PER_CIRCLE_ANIMATING = 300;
	private List<SDGestureItemView> mListItems = new ArrayList<SDGestureItemView>();
	private List<SDGestureItemView> mListItemsTouched = new ArrayList<SDGestureItemView>();
	private List<SDGestureItemView> mListItemsAnimation = new ArrayList<SDGestureItemView>();

	private Canvas mCanvas;
	private Canvas mCanvasFinger;
	private Bitmap mBitmap;
	private Bitmap mBitmapFinger;
	private Paint mPaint;
	private boolean mIsAllItemAdded = false;
	private SDGestureTouchViewListener mListener;
	private StringBuilder mSbPassword = new StringBuilder();

	private int mColorTouched = Color.parseColor("#04739D");
	private int mColorError = Color.RED;

	private int mCurX;
	private int mCurY;

	private float mProgressX;
	private float mProgressY;

	private int mAnimationLoopTime = 1;
	private int mAnimationIndexLast = -1;
	private long mAnimationStartTime = 0;

	private boolean mIsInAnimation = false;

	public int getmAnimationLoopTime()
	{
		return mAnimationLoopTime;
	}

	public void setmAnimationLoopTime(int animationLoopTime)
	{
		if (animationLoopTime <= 0)
		{
			animationLoopTime = 1;
		}
		this.mAnimationLoopTime = animationLoopTime;
	}

	public boolean ismIsInAnimation()
	{
		return mIsInAnimation;
	}

	public SDGestureTouchViewListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDGestureTouchViewListener mListener)
	{
		this.mListener = mListener;
	}

	public SDGestureTouchView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SDGestureTouchView(Context context)
	{
		this(context, null);
	}

	private void init()
	{
		initPaint();
		initCanvas(720, 1280);
	}

	private void initCanvas(int width, int height)
	{
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // 设置位图的宽高
		mBitmapFinger = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // 设置位图的宽高

		mCanvas = new Canvas();
		mCanvas.setBitmap(mBitmap);

		mCanvasFinger = new Canvas();
		mCanvasFinger.setBitmap(mBitmapFinger);
	}

	private void initPaint()
	{
		Paint paint = new Paint(Paint.DITHER_FLAG);// 创建一个画笔
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth(10);// 笔宽5像素
		paint.setColor(mColorTouched);// 设置颜色
		paint.setAntiAlias(true);// 不显示锯齿
		mPaint = paint;
	}

	public void setItems(List<SDGestureItemView> listItems)
	{
		if (listItems != null && listItems.size() > 0)
		{
			this.mListItems = listItems;
			reset();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		initCanvas(getWidth(), getHeight());
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if (mIsInAnimation)
		{
			return false;
		}
		switch (e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if (mListener != null)
			{
				mListener.onTouchDown();
			}
			reset();
			mCurX = (int) e.getX();
			mCurY = (int) e.getY();
			findTouchedItem(mCurX, mCurY);
			break;
		case MotionEvent.ACTION_MOVE:
			mCurX = (int) e.getX();
			mCurY = (int) e.getY();
			findTouchedItem(mCurX, mCurY);
			drawFingerLine(mCurX, mCurY);
			break;
		case MotionEvent.ACTION_UP:
			clearFingerLine();
			if (mListener != null)
			{
				if (mSbPassword.length() > 0)
				{
					mListener.onTouchFinish(mSbPassword.toString(), mListItemsTouched.size());
				}
				mListener.onTouchUp();
			}
			break;

		default:
			break;
		}
		return true;
	}

	private SDGestureItemView getLastTouchedItem()
	{
		if (mListItemsTouched.size() > 0)
		{
			return mListItemsTouched.get(mListItemsTouched.size() - 1);
		}
		return null;
	}

	private void drawFingerLine(int x, int y)
	{
		SDGestureItemView lastItem = getLastTouchedItem();
		if (lastItem != null)
		{
			mCanvasFinger.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			if (!mIsAllItemAdded)
			{
				mCanvasFinger.drawLine(lastItem.getMiddleX(), lastItem.getMiddleY(), x, y, mPaint);
				invalidate();
			}
		}
	}

	private void clearFingerLine()
	{
		mCanvasFinger.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
	}

	private void clearItemLines()
	{
		mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
	}

	public void reset()
	{
		stopAnimation();
		clearItemLines();
		clearFingerLine();
		for (SDGestureItemView item : mListItems)
		{
			item.setHighLightState(false);
		}
		mListItemsTouched.clear();
		resetPassword();
		mPaint.setColor(mColorTouched);
	}

	private void resetPassword()
	{
		mSbPassword = new StringBuilder();
	}

	private void appendPassword(String index)
	{
		if (!TextUtils.isEmpty(index))
		{
			if (mSbPassword.length() > 0)
			{
				mSbPassword.append(",");
			}
			mSbPassword.append(index);
		}
	}

	private SDGestureItemView findTouchedItem(int x, int y)
	{
		SDGestureItemView touchedItem = null;
		for (SDGestureItemView model : mListItems)
		{
			if (model.isTouchedView(x, y))
			{
				touchedItem = model;
				break;
			}
		}
		addTouchedItem(touchedItem);
		return touchedItem;
	}

	private void addTouchedItem(SDGestureItemView item)
	{
		if (item != null)
		{
			if (!mListItemsTouched.contains(item))
			{
				int index = mListItems.indexOf(item);
				appendPassword(String.valueOf(index));
				item.setHighLightState(true);
				mListItemsTouched.add(item);
				if (mListItems.size() == mListItemsTouched.size())
				{
					mIsAllItemAdded = true;
				} else
				{
					mIsAllItemAdded = false;
				}
				drawItemLineNew();
				if (mListener != null)
				{
					mListener.onTouchItem(item, index);
				}
			}
		}
	}

	private void drawItemLineNew()
	{
		SDGestureItemView itemCuruent = SDCollectionUtil.getLast(mListItemsTouched, 0);
		SDGestureItemView itemPre = SDCollectionUtil.getLast(mListItemsTouched, 1);
		if (itemCuruent != null && itemPre != null)
		{
			int startX = itemPre.getMiddleX();
			int startY = itemPre.getMiddleY();
			int stopX = itemCuruent.getMiddleX();
			int stopY = itemCuruent.getMiddleY();
			mPaint.setColor(mColorTouched);
			Log.i(getClass().getName(), startX + "," + startY + "|" + stopX + "," + stopY);
			mCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
		}
	}

	private void drawItemLines(int color, List<SDGestureItemView> listItems)
	{
		if (listItems != null)
		{
			int touchedSize = listItems.size();
			if (touchedSize > 1)
			{
				clearItemLines();
				for (int i = 0; i < touchedSize; i++)
				{
					SDGestureItemView itemCuruent = listItems.get(i);
					itemCuruent.setHighLightState(true);
					if (i > 0)
					{
						SDGestureItemView itemPre = listItems.get(i - 1);
						int startX = itemPre.getMiddleX();
						int startY = itemPre.getMiddleY();

						int stopX = itemCuruent.getMiddleX();
						int stopY = itemCuruent.getMiddleY();
						mPaint.setColor(color);
						mCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
					}
				}
			}
		}
	}

	public void drawErrorLine()
	{
		drawErrorLine(mColorError);
	}

	public void drawErrorLine(int color)
	{
		drawItemLines(color, mListItemsTouched);
	}

	public String getPassword()
	{
		return mSbPassword.toString();
	}

	public void drawLine(String password, boolean anim, int loopTime)
	{
		if (!TextUtils.isEmpty(password) && password.contains(","))
		{
			String[] indexs = password.split(",");
			if (indexs != null && indexs.length > 0)
			{
				int[] intPassword = new int[indexs.length];
				for (int i = 0; i < indexs.length; i++)
				{
					intPassword[i] = Integer.valueOf(indexs[i]);
				}
				drawLine(intPassword, anim, loopTime);
			}
		}
	}

	public void drawLine(int[] password, boolean anim, int loopTime)
	{
		List<SDGestureItemView> listItems = getItemsFromIntArray(password);
		if (listItems != null && listItems.size() > 1)
		{
			mListItemsAnimation.clear();
			mListItemsAnimation.addAll(listItems);

			if (anim)
			{
				setmAnimationLoopTime(loopTime);
				startAnimation();
			} else
			{
				drawItemLines(mColorTouched, listItems);
			}
		}
	}

	public void startAnimation()
	{
		if (mListItemsAnimation.size() > 1)
		{
			stopAnimation();
			reset();
			mIsInAnimation = true;
			mPaint.setColor(mColorTouched);
			mAnimationStartTime = System.currentTimeMillis();
			invalidate();
		}
	}

	public void stopAnimation()
	{
		mIsInAnimation = false;
		mAnimationIndexLast = -1;
	}

	private List<SDGestureItemView> getItemsFromIntArray(int[] password)
	{
		List<SDGestureItemView> listItems = null;
		if (password != null && password.length > 0)
		{
			listItems = new ArrayList<SDGestureItemView>();
			SDGestureItemView item = null;
			for (int i = 0; i < password.length; i++)
			{
				try
				{
					int index = password[i];
					item = mListItems.get(index);
				} catch (Exception e)
				{
					return null;
				}
				listItems.add(item);
			}

		}
		return listItems;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.drawBitmap(mBitmapFinger, 0, 0, null);

		drawLineAnimate();
	}

	private void drawLineAnimate()
	{
		List<SDGestureItemView> listItems = mListItemsAnimation;
		int animationSize = listItems.size();
		if (animationSize > 1 && mIsInAnimation)
		{
			// ----------------------------
			long timePassed = System.currentTimeMillis() - mAnimationStartTime;
			long timePassedPerCircle = timePassed % TIME_PER_CIRCLE_ANIMATING;

			int curAnimationIndex = (int) (timePassed / TIME_PER_CIRCLE_ANIMATING);
			if (curAnimationIndex >= animationSize - 1)
			{
				SDGestureItemView itemLast = SDCollectionUtil.getLast(listItems, 0);
				SDGestureItemView itemLastSecond = SDCollectionUtil.getLast(listItems, 1);
				if (itemLast != null && itemLastSecond != null)
				{
					itemLast.setHighLightState(true);
					mCanvas.drawLine(itemLastSecond.getMiddleX(), itemLastSecond.getMiddleY(), itemLast.getMiddleX(), itemLast.getMiddleY(), mPaint);
				}
				mAnimationLoopTime--; // 循环次数减1
				if (mAnimationLoopTime <= 0)
				{
					stopAnimation();
				} else
				{
					SDHandlerManager.getMainHandler().postDelayed(new Runnable()
					{

						@Override
						public void run()
						{
							startAnimation();
						}
					}, TIME_PER_CIRCLE_ANIMATING);
				}
				return;
			}

			// -----------------------------

			SDGestureItemView itemCurrent = listItems.get(curAnimationIndex);
			float startX = itemCurrent.getMiddleX();
			float startY = itemCurrent.getMiddleY();

			SDGestureItemView itemNext = listItems.get(curAnimationIndex + 1);
			float stopX = itemNext.getMiddleX();
			float stopY = itemNext.getMiddleY();

			float deltaX = stopX - startX;
			float deltaY = stopY - startY;

			float percent = (float) timePassedPerCircle / (float) TIME_PER_CIRCLE_ANIMATING;
			if (mAnimationIndexLast != curAnimationIndex) // 说明切换到下一个点了
			{
				mAnimationIndexLast = curAnimationIndex;
				itemCurrent.setHighLightState(true);

				SDGestureItemView itemPre = SDCollectionUtil.get(listItems, curAnimationIndex - 1);
				if (itemPre != null)
				{
					float preX = itemPre.getMiddleX();
					float preY = itemPre.getMiddleY();
					mCanvas.drawLine(preX, preY, startX, startY, mPaint);
				}
			}

			mProgressX = (deltaX * percent) + startX;
			mProgressY = (deltaY * percent) + startY;

			mCanvas.drawLine(startX, startY, mProgressX, mProgressY, mPaint);

			invalidate();
		} else
		{
			stopAnimation();
		}

	}

	public interface SDGestureTouchViewListener
	{
		public void onTouchItem(View v, int index);

		public void onTouchFinish(String password, int size);

		public void onTouchDown();

		public void onTouchUp();
	}
}
