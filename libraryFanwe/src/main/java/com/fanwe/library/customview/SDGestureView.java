package com.fanwe.library.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewUtil;

public class SDGestureView extends FrameLayout
{

	private SDGestureTouchView mTouchView;

	public SDGestureTouchView getmTouchView()
	{
		return mTouchView;
	}

	public SDGestureView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SDGestureView(Context context)
	{
		this(context, null);
	}

	private void init()
	{
		mTouchView = new SDGestureTouchView(getContext());
		createDefaultItems();
	}

	private void createDefaultItems()
	{
		View viewPassword = SDViewUtil.inflate(R.layout.view_password, null);
		setPasswordView(viewPassword);
		SDGestureItemView item0 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item0);
		SDGestureItemView item1 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item1);
		SDGestureItemView item2 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item2);
		SDGestureItemView item3 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item3);
		SDGestureItemView item4 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item4);
		SDGestureItemView item5 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item5);
		SDGestureItemView item6 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item6);
		SDGestureItemView item7 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item7);
		SDGestureItemView item8 = (SDGestureItemView) viewPassword.findViewById(R.id.view_password_item8);

		List<SDGestureItemView> listItems = new ArrayList<SDGestureItemView>();
		listItems.add(item0);
		listItems.add(item1);
		listItems.add(item2);
		listItems.add(item3);
		listItems.add(item4);
		listItems.add(item5);
		listItems.add(item6);
		listItems.add(item7);
		listItems.add(item8);
		mTouchView.setItems(listItems);
	}

	public void setPasswordView(View view)
	{
		this.removeAllViews();
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(view, params);
		this.addView(mTouchView, params);
	}

}
