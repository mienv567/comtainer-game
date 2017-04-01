package com.fanwe.library.listener;

import android.view.View;

public interface SDVisibilityStateListener
{
	void onVisible(View view);

	void onGone(View view);

	void onInvisible(View view);
}
