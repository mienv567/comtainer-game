package com.qy.ysys.yishengyishi.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.widgets.MultiImageView;


/**
 * Created by suneee on 2016/8/16.
 */
public class ImageViewHolder extends CircleViewHolder {
    /**
     * 图片
     */
    public MultiImageView multiImageView;
    public TextView tv_pic_number;

    public ImageViewHolder(View itemView) {
        super(itemView, TYPE_IMAGE);
        tv_pic_number = (TextView) itemView.findViewById(R.id.tv_pic_number);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
//        this.tv_pic_number = (TextView) subView.findViewById(R.id.tv_pic_number);
        if (multiImageView != null) {
            this.multiImageView = multiImageView;
            this.multiImageView.setOnMeasuredListener(new MultiImageView.OnMeasuredListener() {
                @Override
                public void onMeasured(int width) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_pic_number.getLayoutParams();
                    if (layoutParams.topMargin < width) {
                        layoutParams.topMargin = width + layoutParams.topMargin;
                        tv_pic_number.setLayoutParams(layoutParams);
                    }
                }
            });
        }
    }
}
