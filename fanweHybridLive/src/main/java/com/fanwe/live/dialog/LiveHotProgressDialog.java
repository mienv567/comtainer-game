package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.ERequestHotSuccess;
import com.fanwe.live.model.AppRoomHotScoreActModel;
import com.fanwe.live.model.AppRoomHotScoreDataActModel;
import com.fanwe.live.model.AppRoomHotScoreProgressActModel;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.NumberFormat;

public class LiveHotProgressDialog extends SDDialogBase
{

    @ViewInject(R.id.ll_close)
    private LinearLayout ll_close; //关闭
    @ViewInject(R.id.prev_value)
    private TextView prev_value;// 上一级的值
    @ViewInject(R.id.current_name)
    private TextView current_name; //当前热度名称
    @ViewInject(R.id.score_value)
    private TextView score_value;//当前热度值
    @ViewInject(R.id.next_value)
    private TextView next_value;//下级的值
    @ViewInject(R.id.person_precent_progress)
    private ProgressBar person_precent_progress;//人气进度条
    @ViewInject(R.id.person_precent)
    private TextView person_precent;//人气值
    @ViewInject(R.id.mala_precent_progress)
    private ProgressBar mala_precent_progress;//麻辣进度条
    @ViewInject(R.id.mala_precent)
    private TextView mala_precent;//麻辣值
    @ViewInject(R.id.share_precent_progress)
    private ProgressBar share_precent_progress;//分享进度条
    @ViewInject(R.id.share_precent)
    private TextView share_precent;//分享值
    @ViewInject(R.id.light_precent_progress)
    private ProgressBar light_precent_progress;//点亮进度条
    @ViewInject(R.id.light_precent)
    private TextView light_precent;//点亮值
    @ViewInject(R.id.level_precent_progress)
    private ProgressBar level_precent_progress;//等级进度条
    @ViewInject(R.id.level_precent)
    private TextView level_precent;//等级值
    @ViewInject(R.id.hot_precent_progress)
    private ProgressBar hot_precent_progress;//热度进度条
    @ViewInject(R.id.hot_precent)
    private TextView hot_precent;//热度值


    private int mRoomId;

    public LiveHotProgressDialog(Activity activity, int roomId)
    {
        super(activity);
        mRoomId = roomId;
        init();
    }


    private void init()
    {
        setContentView(R.layout.dialog_hot_progress);
        setCanceledOnTouchOutside(true);
        paddingLeft(SDViewUtil.dp2px(40));
        paddingRight(SDViewUtil.dp2px(40));
        x.view().inject(this, getContentView());
        register();
        requestData();
    }

    private void register()
    {
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void requestData(){
        CommonInterface.requestHotScore(mRoomId, new AppRequestCallback<AppRoomHotScoreActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                bindData(actModel);
            }
        });
    }

    private void bindData(AppRoomHotScoreActModel actModel)
    {
        if(actModel != null && actModel.getData() != null){
            AppRoomHotScoreDataActModel model = actModel.getData();
            SDViewBinder.setTextView(prev_value,model.getPrev()+"");
            SDViewBinder.setTextView(current_name,model.getCurrent_name());
            SDViewBinder.setTextView(score_value,model.getTotal_score() + "");
            SDViewBinder.setTextView(next_value,model.getNext() + "");
            AppRoomHotScoreProgressActModel progressModel = model.getProgress_bar();
            if(progressModel != null){
                person_precent_progress.setMax(100);
                person_precent_progress.setProgress(progressModel.getPerson_precent());
                SDViewBinder.setTextView(person_precent, progressModel.getPerson_precent() + "%");
                mala_precent_progress.setMax(100);
                mala_precent_progress.setProgress(progressModel.getMala_precent());
                SDViewBinder.setTextView(mala_precent, progressModel.getMala_precent() + "%");
                share_precent_progress.setMax(100);
                share_precent_progress.setProgress(progressModel.getShare_precent());
                SDViewBinder.setTextView(share_precent, progressModel.getShare_precent() + "%");
                light_precent_progress.setMax(100);
                light_precent_progress.setProgress(progressModel.getLight_precent());
                SDViewBinder.setTextView(light_precent, progressModel.getLight_precent() + "%");
                level_precent_progress.setMax(100);
                level_precent_progress.setProgress(progressModel.getLevel_precent());
                SDViewBinder.setTextView(level_precent, progressModel.getLevel_precent() + "%");
                hot_precent_progress.setMax(100);
                int hotProgress = Integer.parseInt(getPercentString(progressModel.getHot_gift(),progressModel.getHot_gift_max()));
                hot_precent_progress.setProgress(hotProgress);
                SDViewBinder.setTextView(hot_precent,progressModel.getHot_gift()+"");
            }
            ERequestHotSuccess event = new ERequestHotSuccess();
            event.score = model.getTotal_score();
            event.scoreName = model.getCurrent_name();
            SDEventManager.post(event);
        }
    }

    private String getPercentString(int num,int max){
        String result = "";
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        return result = numberFormat.format((float) num / (float) max * 100);
    }

}
