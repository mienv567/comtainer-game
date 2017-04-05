package com.fanwe.live.appview.room;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_followActModel;

public class RoomViewerFinishView extends RoomView
{

    public RoomViewerFinishView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomViewerFinishView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomViewerFinishView(Context context)
    {
        super(context);
    }

    /**
     * 观看人数 (int)
     */
    public static final String EXTRA_VIEWER_NUMBER = "extra_viewer_number";

    /**
     * 是否已关注1-已关注，0-未关注 (int)
     */
    public static final String EXTRA_HAS_FOLLOW = "extra_has_follow";
    private ViewGroup ll_root_view;
    private TextView tv_viewer_number;
    private TextView tv_follow;
    private TextView tv_back_home;
    private TextView tv_follow_tip;
    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_finish_viewer;
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
        tv_viewer_number = find(R.id.tv_viewer_number);
        tv_follow = find(R.id.tv_follow);
        tv_back_home = find(R.id.tv_back_home);
        tv_follow_tip = find(R.id.tv_follow_tip);
        tv_follow.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickFollow();
            }
        });
        tv_back_home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickBackHome();
            }
        });
    }

    public void setViewerNumber(int number)
    {
        if (number < 0)
        {
            number = 0;
        }
        tv_viewer_number.setText(String.valueOf(number));
    }

    public void setHasFollow(int hasFollow)
    {
        if (hasFollow == 1)
        {
            hideFollow();
        } else
        {
            showFollow();
        }
    }

    private void showFollow(){
        SDViewBinder.setTextView(tv_follow, SDResourcesUtil.getString(R.string.follow));
        SDViewUtil.show(tv_follow);
        SDViewUtil.show(tv_follow_tip);
        setHomeTextViewMargin(20);
    }

    private void hideFollow(){
        SDViewUtil.hide(tv_follow);
        SDViewUtil.hide(tv_follow_tip);
        setHomeTextViewMargin(40);
    }

    private void showFollowSuccess(){
        SDViewBinder.setTextView(tv_follow, SDResourcesUtil.getString(R.string.already_follow));
        SDViewUtil.show(tv_follow);
        SDViewUtil.show(tv_follow_tip);
        setHomeTextViewMargin(20);
    }

    private void setHomeTextViewMargin(int marginTop){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(SDViewUtil.dp2px(80), SDViewUtil.dp2px(marginTop), SDViewUtil.dp2px(80), 0);
        tv_back_home.setLayoutParams(lp);
    }

    protected void clickFollow()
    {
        if(SDResourcesUtil.getString(R.string.follow).equals(tv_follow.getText()+"")) {
            CommonInterface.requestFollow(getLiveInfo().getCreaterId(),  new AppRequestCallback<App_followActModel>() {

                @Override
                protected void onSuccess(SDResponse resp) {
                    if (rootModel.getStatus() == 1) {
//                        setHasFollow(actModel.getRelationship());
                        showFollowSuccess();
                    }
                }
            });
        }
    }

    protected void clickBackHome()
    {
        getActivity().finish();
    }


    @Override
    public boolean onBackPressed()
    {
//        Intent intent = new Intent(getActivity(), LiveMainActivity.class);
//        getActivity().startActivity(intent);
        getActivity().finish();
        return true;
    }
}
