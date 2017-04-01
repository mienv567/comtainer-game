package com.fanwe.live.appview.room;

import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveCreaterActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_del_videoActModel;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.utils.LiveUtils;

import java.math.BigDecimal;
import java.util.List;

public class RoomCreaterFinishView extends RoomView
{

    public RoomCreaterFinishView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomCreaterFinishView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomCreaterFinishView(Context context)
    {
        super(context);
    }
    private ViewGroup ll_root_view;
    private ImageView iv_head_image;
    private TextView tv_viewer_number;
    private TextView tv_money;
    private TextView tv_ticket;
    private TextView tv_share;
    private TextView tv_back_home;
    private TextView tv_delete_video;
    private TextView tv_fans;
    private TextView tv_share_count;
    private TextView tv_show_time;
    private String headImgUrl;
    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_finish_creater;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        ll_root_view = find(R.id.ll_root_view);
        if(ll_root_view != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ll_root_view.setPadding(0, SDViewUtil.dp2px(24),0,0);
            }
        }
        iv_head_image = find(R.id.iv_head_image);
        tv_viewer_number = find(R.id.tv_viewer_number);
        tv_money = find(R.id.tv_money);
        TextPaint tp = tv_money.getPaint();
        tp.setFakeBoldText(true);
        tv_ticket = find(R.id.tv_ticket);
        tv_share = find(R.id.tv_share);
        tv_back_home = find(R.id.tv_back_home);
        tv_delete_video = find(R.id.tv_delete_video);
        tv_fans = find(R.id.tv_fans);
        tv_share_count = find(R.id.tv_share_count);
        tv_show_time = find(R.id.tv_show_time);
        tv_share.setOnClickListener(this);
        tv_back_home.setOnClickListener(this);
        tv_delete_video.setOnClickListener(this);
        requestEndVideo();
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tv_share)
        {
            clickShare();
        } else if (v == tv_back_home)
        {
            clickBackHome();
        } else if (v == tv_delete_video)
        {
            clickDeleteVideo();
        }
    }

    protected void clickBackHome()
    {
        getActivity().finish();
    }

    protected void clickShare()
    {
        getLiveInfo().openShare(null);
    }

    protected void clickDeleteVideo()
    {
        CommonInterface.requestDeleteVideo(getLiveInfo().getRoomId(), new AppRequestCallback<App_del_videoActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    getActivity().finish();
                }
            }
        });
    }

    private void requestEndVideo()
    {
        String urlJson = null;
        List<String> listRecord = getLiveInfo().getListRecord();
        if (listRecord != null && listRecord.size() > 0)
        {
            urlJson = SDJsonUtil.object2Json(listRecord);
        }

        CommonInterface.requestEndVideo(getLiveInfo().getRoomId(), urlJson, new AppRequestCallback<App_end_videoActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    SDViewBinder.setImageView(getActivity(), headImgUrl, iv_head_image,R.drawable.ic_default_head);
                    SDViewBinder.setTextView(tv_money, String.valueOf(floatFormat(actModel.getAdd_rmb())));
                    SDViewBinder.setTextView(tv_viewer_number, String.valueOf(actModel.getWatch_number()));
                    SDViewBinder.setTextView(tv_ticket, String.valueOf(actModel.getVote_number()));
                    SDViewBinder.setTextView(tv_fans, String.valueOf(actModel.getAdd_funs()));
                    SDViewBinder.setTextView(tv_share_count, String.valueOf(actModel.getAdd_shared()));
                    SDViewBinder.setTextView(tv_show_time, LiveUtils.formatShowTime(actModel.getTime_len()));
                    if(getActivity() instanceof LiveCreaterActivity){
                        ((LiveCreaterActivity)getActivity()).setCreaterTime(actModel.getTime_len());
                    }
                    if (actModel.getHas_delvideo() == 1) {
                        SDViewUtil.show(tv_delete_video);
                    } else {
                        SDViewUtil.invisible(tv_delete_video);
                    }
                }
            }
        });
    }

    /**
     * 四舍五入保留小数两位
     * @param f
     * @return
     */
    private float floatFormat(float f){
        BigDecimal b   =   new   BigDecimal(f);
        return  b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    public boolean onBackPressed()
    {
        getActivity().finish();
        return true;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
