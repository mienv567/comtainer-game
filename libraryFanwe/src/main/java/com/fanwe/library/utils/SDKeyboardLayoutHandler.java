package com.fanwe.library.utils;

import android.graphics.Rect;
import android.view.View;

import com.fanwe.library.utils.SDKeyboardListener;
import com.fanwe.library.utils.SDKeyboardListener.KeyboardListener;

public class SDKeyboardLayoutHandler
{

	private SDKeyboardListener keyboardListener = new SDKeyboardListener();

	public void handle(final View root, final KeyboardLayoutListener listener)
	{
		if (root != null)
		{
			keyboardListener.listen(root, new KeyboardListener()
			{

				@Override
				public void onVisibilityChanged(boolean show, int height, Rect rect)
				{
					boolean scroll = true;
					if (show)
					{
						if (listener != null)
						{
							scroll = !listener.onVisibilityChanged(show, height);
						}

						if (scroll)
						{
							root.scrollBy(0, height);
						}
					} else
					{
						if (listener != null)
						{
							scroll = !listener.onVisibilityChanged(show, height);
						}

						if (scroll)
						{
							root.scrollTo(0, 0);
							root.setVisibility(View.INVISIBLE);
							root.setVisibility(View.VISIBLE);
						}
					}
				}
			});
		}
	}

	public void handle(final View root, final View target, final KeyboardLayoutListener listener)
	{
		if (root != null && target != null)
		{
			keyboardListener.listen(root, new KeyboardListener()
			{

				@Override
				public void onVisibilityChanged(boolean show, int height, Rect rect)
				{
					boolean scroll = true;
					if (show)
					{
						if (listener != null)
						{
							scroll = !listener.onVisibilityChanged(show, height);
						}

						if (scroll)
						{
							int[] location = new int[2];
							target.getLocationOnScreen(location);

							int oldY = location[1];
							int newY = rect.bottom - target.getHeight();

							int srollHeight = oldY - newY;
							if (srollHeight > 0)
							{
								root.scrollBy(0, srollHeight);
							}
						}
					} else
					{
						if (listener != null)
						{
							scroll = !listener.onVisibilityChanged(show, height);
						}

						if (scroll)
						{
							root.scrollTo(0, 0);
							root.setVisibility(View.INVISIBLE);
							root.setVisibility(View.VISIBLE);
						}
					}
				}
			});

		}
	}

	public interface KeyboardLayoutListener
	{
		/**
		 * 键盘状态变化监听
		 * 
		 * @param show
		 *            true:键盘
		 * @param height
		 *            键盘高度
		 * @return true:消耗掉事件，SDKeyboardLayoutHandler将不处理需要滚动的逻辑
		 */
		boolean onVisibilityChanged(boolean show, int height);
	}

}
