package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.model.RoomChannelModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/8/8.
 */
public class RoomChannelView extends BaseAppView
{
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private Map<Integer,RoomChannelModel> mChannelMaps = new HashMap<>();
    private LinearLayout mRootLayout;
    private Context mContext;
    public RoomChannelView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public RoomChannelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }

    public RoomChannelView(Context context)
    {
        super(context);
        mContext = context;
    }

    public void setRoomChannelList(List<RoomChannelModel> list){
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size() ; i++){
                TextView tx = new TextView(mContext);
                tx.setId(generateViewId());
                tx.setText(list.get(i).getName());
                tx.setTextColor(mContext.getResources().getColor(R.color.white));
                if(i == 0){
                    tx.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel_selected));
                }else{
                    tx.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel));
                }
                tx.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mRootLayout.getLayoutParams());
                lp.width = LayoutParams.WRAP_CONTENT;
                lp.height = LayoutParams.WRAP_CONTENT;
                lp.setMargins(0, 0, 0, SDViewUtil.dp2px(15));
                tx.setLayoutParams(lp);
                tx.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClickChannel(v);
                    }
                });
                mChannelMaps.put(tx.getId(), list.get(i));
                mRootLayout.addView(tx);
            }
        }
    }

    public String getRTMPUrlById(int id){
        return mChannelMaps.get(id).getRtmp_play_url();
    }

    public void selectChannel(int id){
        for(int i = 0; i< mChannelMaps.size();i++){
            TextView child = (TextView)mRootLayout.getChildAt(i);
            if(id == child.getId()){
                child.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel_selected));
            }else{
                child.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel));
            }
        }
    }

    public void selectChannel(String url){
        for(Integer key : mChannelMaps.keySet()){
            RoomChannelModel model = mChannelMaps.get(key);
            TextView child = (TextView)find(key);
            if(model.getRtmp_play_url().equals(url)){
                child.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel_selected));
            }else{
                child.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_live_channel));
            }
        }
    }

    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }


    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_room_channel;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        mRootLayout = (LinearLayout)find(R.id.channel_root);
    }

    public interface ClickListener
    {
        void onClickChannel(View v);
    }

}
