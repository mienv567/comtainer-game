package com.fanwe.library.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class SDKeyboardListener
{

	private boolean isKeyboardShow = false;

	public boolean isKeyboardShow()
	{
		return isKeyboardShow;
	}

	public void listen(final View root, final KeyboardListener listener)
	{
		if (root != null)
		{
			root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{
				@Override
				public void onGlobalLayout()
				{
					Rect rect = new Rect();
					root.getWindowVisibleDisplayFrame(rect);

					int height = root.getHeight();
					int newHeight = rect.bottom - rect.top;

					int invisibleHeight = height - newHeight;
					if (invisibleHeight > 300)
					{
						if (!isKeyboardShow)
						{
							isKeyboardShow = true;

							if (listener != null)
							{
								listener.onVisibilityChanged(isKeyboardShow, invisibleHeight, rect);
							}
						}
					} else
					{
						if (isKeyboardShow)
						{
							isKeyboardShow = false;

							if (listener != null)
							{
								listener.onVisibilityChanged(isKeyboardShow, invisibleHeight, rect);
							}
						}
					}
				}
			});
		}
	}

	public interface KeyboardListener
	{
		/**
		 * 
		 * @param show
		 *            true-键盘显示
		 * @param height
		 *            键盘高度
		 * @param rect
		 *            rootView的边框
		 */
		void onVisibilityChanged(boolean show, int height, Rect rect);
	}

}
