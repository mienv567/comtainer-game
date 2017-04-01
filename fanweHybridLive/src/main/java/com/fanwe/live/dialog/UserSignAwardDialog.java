package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserSignInReward;
import com.fanwe.hybrid.model.UserSubmitSignModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.flyco.dialog.widget.base.BaseDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 签到成功提示
 *
 */
public class UserSignAwardDialog extends BaseDialog<UserSignAwardDialog>
{
    @ViewInject(R.id.iv_close)
    private ImageView iv_close; //关闭

    @ViewInject(R.id.tv_get)
    private TextView tv_get; //领取

    private UserSubmitSignModel mUserSubmitSignModel;
    private View contentView;
    private Activity mActivity;

    public UserSignAwardDialog(Activity activity, UserSubmitSignModel model)
    {
        super(activity);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        getWindow().setDimAmount(0);
        mActivity = activity;
        mUserSubmitSignModel = model;
    }

    @Override
    public View onCreateView() {
        widthScale(0.9f);
//        showAnim(new Swing());
        View inflate = View.inflate(mContext, R.layout.dialog_user_sign_award, null);
        contentView = inflate;
        init();
        return inflate;
    }

    protected void init()
    {
        setCanceledOnTouchOutside(true);
        x.view().inject(this, getContentView());
//        getContentView().setAlpha(0.75f);
        initData();
        register();
    }

    public View getContentView()
    {
        return contentView;
    }

    private void register()
    {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonInterface.submitUserSign(new AppRequestCallback<UserSubmitSignModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.getStatus() == 1) {
                            iv_close.post(new Runnable() {
                                @Override
                                public void run() {
                                    SDToast.showToast(SDResourcesUtil.getString(R.string.already_get_sign_reward) + getExperienceFormatString(actModel.getReward()));
                                }
                            });
                        } else {
                            SDToast.showToast(actModel.getError());
                        }
                        dismiss();
                    }
                });
            }
        });
    }

    private String getExperienceFormatString(List<UserSignInReward> list){
        String result = "";
        if(list != null && list.size() > 0){
            for(UserSignInReward item : list){
                result += item.getDesc()+"*"+item.getNum() + ",";
            }
        }
        if(!TextUtils.isEmpty(result)){
            result = result.substring(0,result.lastIndexOf(","));
        }
        return result;
    }

    private void initData(){
        if(mUserSubmitSignModel != null){
            List<UserSignInReward> list = mUserSubmitSignModel.getReward();
            if(list != null && list.size() > 0){
                for(int i = 0 ; i< list.size() ; i++){
                    if( i >= 4 ){
                        break;
                    }
                    UserSignInReward reward = list.get(i);
                    int layoutIdentify = mActivity.getResources().getIdentifier(String.format("ll_sign_%d",
                            i + 1), "id", mActivity.getApplicationContext().getPackageName());
                    LinearLayout linearLayout = (LinearLayout) contentView.findViewById(layoutIdentify);
                    int textIdentify = mActivity.getResources().getIdentifier(String.format("tv_sign_%d",
                            i + 1), "id", mActivity.getApplicationContext().getPackageName());
                    TextView text = (TextView) contentView.findViewById(textIdentify);
                    text.setText(reward.getDesc() + "x" + reward.getNum());
                    int imgIdentify = mActivity.getResources().getIdentifier(String.format("img_sign_%d",
                            i + 1), "id", mActivity.getApplicationContext().getPackageName());
                    ImageView img = (ImageView) contentView.findViewById(imgIdentify);
                    SDViewBinder.setImageView(mActivity,reward.getUrl(),img,R.drawable.ic_default_head);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void setUiBeforShow() {

    }
}
