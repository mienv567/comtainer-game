package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.live.R;

/**
 * 私聊界面，更多里面的布局
 */
public class LivePrivateChatMoreView extends BaseAppView implements ILivePrivateChatMoreView
{
    private ImageView iv_gift;
    private ImageView iv_camera;
    private ImageView iv_photo;

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public LivePrivateChatMoreView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LivePrivateChatMoreView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LivePrivateChatMoreView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();

        setContentView(R.layout.view_live_private_chat_more);

        iv_gift = find(R.id.iv_gift);
        iv_camera = find(R.id.iv_camera);
        iv_photo = find(R.id.iv_photo);

        iv_gift.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        iv_photo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == iv_gift)
        {
            if (clickListener != null)
            {
                clickListener.onClickGift();
            }
        } else if (v == iv_camera)
        {
            if (clickListener != null)
            {
                clickListener.onClickCamera();
            }
        } else if (v == iv_photo)
        {
            if (clickListener != null)
            {
                clickListener.onClickPhoto();
            }
        }
    }

    @Override
    public void setContentMatchParent()
    {

    }

    @Override
    public void setContentWrapContent()
    {

    }

    public interface ClickListener
    {
        void onClickGift();

        void onClickPhoto();

        void onClickCamera();
    }
}

