package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserSignInItem;
import com.fanwe.hybrid.model.UserSignInModel;
import com.fanwe.hybrid.model.UserSignInReward;
import com.fanwe.hybrid.model.UserSubmitSignModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 签到
 *
 */

public class UserSignInActivity extends BaseTitleActivity
{

    private UserSignInModel mUserSignInModel;
    @ViewInject(R.id.iv_global_male)
    private ImageView iv_global_male;// 性别
    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;// 等级
    @ViewInject(R.id.iv_head)
    private ImageView iv_head;// 头像
    @ViewInject(R.id.iv_level)
    private ImageView iv_level;// 等级
    @ViewInject(R.id.tv_datacycle)
    private TextView tv_datacycle; //循环周期
    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;
    @ViewInject(R.id.btn_sign)
    private TextView btn_sign;
    private String mCycleValue;//周期值
    private boolean mTodaySigned; //判断今天是否已经签到
    private ImageView mTodayImageView; //当日的图片显示
    private int mTodayIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_signin);
        initTitle();
        initUserModel();
        initData();
        initListener();
    }

    private void initListener(){
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTodaySigned) {
                    SDToast.showToast(getString(R.string.today_already_sign));
                    return;
                }
                CommonInterface.submitUserSign(new AppRequestCallback<UserSubmitSignModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (actModel.getStatus() == 1) {
                            mTitle.post(new Runnable() {
                                @Override
                                public void run() {
                                    SDToast.showToast(getString(R.string.already_get_sign_reward)+"："+ getExperienceFormatString(actModel.getReward()));
                                    doSignSuccess();
//                                    UserSignSuccessDialog dialog = new UserSignSuccessDialog(UserSignInActivity.this, actModel);
//                                    dialog.show();
                                }
                            });
                        } else {
                            SDToast.showToast(actModel.getError());
                        }
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

    private void initUserModel(){
        UserModel userModel = UserModelDao.query();
        if(userModel != null){
            SDViewBinder.setImageView(UserSignInActivity.this, userModel.getHeadImage(), iv_head, R.drawable.ic_default_head);
            if (!TextUtils.isEmpty(userModel.getV_icon())) {
                SDViewBinder.setImageView(UserSignInActivity.this, userModel.getV_icon(), iv_level);
                SDViewUtil.show(iv_level);
            } else {
                SDViewUtil.hide(iv_level);
            }
            if (userModel.getSexResId() > 0) {
                SDViewUtil.show(iv_global_male);
                iv_global_male.setImageResource(userModel.getSexResId());
            } else {
                SDViewUtil.hide(iv_global_male);
            }
            iv_rank.setImageResource(userModel.getLevelImageResId());
            SDViewBinder.setTextView(tv_nick_name,userModel.getNickName());
        }
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.everyday_sign));
        mTitle.setBackgroundColor(getResources().getColor(R.color.main_color));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageRight(R.drawable.ic_task_more);
        mTitle.setOnClickListener(this);
    }

    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        if(mUserSignInModel != null){
            Intent intent = new Intent(UserSignInActivity.this, UserSignRuleActivity.class);
            intent.putExtra(UserSignRuleActivity.EXTRA_SIGN_RULE,mUserSignInModel.getSign_word());
            startActivity(intent);
        }
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.main_color);
    }

    private void initData(){
        CommonInterface.requestUserSignData(new AppRequestCallback<UserSignInModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    mUserSignInModel = actModel;
                    List<UserSignInItem> itemList = mUserSignInModel.getList();
                    if (itemList != null && itemList.size() == 7) {
                        for (int i = 0; i < itemList.size(); i++) {
                            initItemData(itemList, i);
                        }
                    }
                    tv_datacycle.setText(mCycleValue);
                }
            }
        });
    }

    private void initItemData(List<UserSignInItem> itemList,int i){
        UserSignInItem item = itemList.get(i);
        int textIdentify = getResources().getIdentifier(String.format("tv_sign_%d",
                i + 1), "id", getApplicationContext().getPackageName());
        TextView text = (TextView) findViewById(textIdentify);
        int imgIdentify = getResources().getIdentifier(String.format("iv_sign_%d",
                i + 1), "id", getApplicationContext().getPackageName());
        ImageView img = (ImageView) findViewById(imgIdentify);
        if(item.getCurrent() == UserSignInItem.IS_TODAY) {
            mTodayIndex = i;
            mTodayImageView = img;
            text.setText(R.string.today);
            if (item.getIs_sign() == UserSignInItem.NOT_SIGN) {
                todayNotSign();
            }else{
                todaySigned();
            }
        }else{
            text.setText(item.getDate());
        }
        judgeShowImg(i, item, img);
        if (i == 0) {
            mCycleValue = item.getDate();
        } else if (i == (itemList.size() - 1)) {
            mCycleValue += "-" + item.getDate();
        }
    }

    private void todaySigned() {
        mTodaySigned = true;
        SDViewBinder.setTextView(btn_sign, getString(R.string.signed));
    }

    private void todayNotSign() {
        mTodaySigned = false;
        SDViewBinder.setTextView(btn_sign, getString(R.string.sign_immediately));
    }

    private void judgeShowImg(int i, UserSignInItem item, ImageView img) {
        switch (i){
            case 2:
                if (item.getIs_sign() == UserSignInItem.NOT_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_small_off));
                } else if (item.getIs_sign() == UserSignInItem.HAS_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_small_on));
                }
                break;
            case 6:
                if (item.getIs_sign() == UserSignInItem.NOT_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_big_off));
                } else if (item.getIs_sign() == UserSignInItem.HAS_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_big_on));
                }
                break;
            default:
                if (item.getIs_sign() == UserSignInItem.NOT_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_sign_off));
                } else if (item.getIs_sign() == UserSignInItem.HAS_SIGN) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_sign_on));
                }
                break;
        }
    }

    private void judgeShowTodayImg() {
        switch (mTodayIndex){
            case 2:
                mTodayImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_small_on));
                break;
            case 6:
                mTodayImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_gift_big_on));
                break;
            default:
                mTodayImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_sign_on));
                break;
        }
    }

    private void doSignSuccess(){
        if(mTodayImageView != null){
            judgeShowTodayImg();
            todaySigned();
//            App.mShowSignRedPoint = false;
//            SDEventManager.post(new ERedPointChange());
        }
    }

}
