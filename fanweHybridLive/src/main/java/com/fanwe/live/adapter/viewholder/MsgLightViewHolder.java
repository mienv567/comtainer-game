package com.fanwe.live.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.span.LiveHeartSpan;
import com.fanwe.live.view.HeartLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * 点亮
 */
public class MsgLightViewHolder extends MsgTextViewHolder
{
    public MsgLightViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgLight msg = (CustomMsgLight) customMsg;

        appendUserInfoNotFormat(msg.getSender(), LiveConstant.LiveMsgType.MSG_LIGHT);

        // 内容
        String text = " "+SDResourcesUtil.getString(R.string.live_light);
        int textColor = SDResourcesUtil.getColor(R.color.live_light);
        appendContent(text, textColor);

        // 心
        String imageName = msg.getImageName();
        LiveHeartSpan giftSpan = getDefaultGiftSpan(imageName);
        if (null != HeartLayout.mapNameUrl && HeartLayout.mapNameUrl.size()>0) {
            if(ImageLoader.getInstance().getDiskCache() != null
                    && !TextUtils.isEmpty(HeartLayout.mapNameUrl.get(imageName))){
                File heartImg = ImageLoader.getInstance().getDiskCache().get(HeartLayout.mapNameUrl.get(imageName));
                if (heartImg != null && heartImg.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(heartImg.getAbsolutePath());
                    Drawable drawable = new BitmapDrawable(bitmap);
                    giftSpan = new LiveHeartSpan(drawable);
                }
            }
        }
        sb.appendSpan(giftSpan, "heart");

        setUserInfoClickListener(tv_content);
    }

    private LiveHeartSpan getDefaultGiftSpan(String imageName){
        int id = SDViewUtil.getIdentifierDrawable(imageName);
        if (id == 0) {
            id = R.drawable.heart0;
        }
        return new LiveHeartSpan(getAdapter().getActivity(), id);
    }
}
