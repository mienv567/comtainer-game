package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;

import org.xutils.view.annotation.ViewInject;

/**
 * 用户等级顶部展示View
 * 作者：tracy.lee on 2017/1/13 0013 14:50
 */
public class UserLevelTopView extends BaseAppView{

    @ViewInject(R.id.tv_down_experience)
    private TextView tv_down_experience;
    @ViewInject(R.id.tv_up_experience)
    private TextView tv_up_experience;
    @ViewInject(R.id.tv_current_experience)
    private TextView tv_current_experience;
    @ViewInject(R.id.tv_current_level)
    private TextView tv_current_level;
    @ViewInject(R.id.iv_bg_level)
    private ImageView iv_bg_level;
    public UserLevelTopView(Context context) {
        super(context);
        init();
    }

    public UserLevelTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserLevelTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.view_user_level_top);
        UserModel userModel = UserModelDao.query();
        if(userModel != null){
            SDViewBinder.setTextView(tv_down_experience,userModel.getDownSocre()+"");
            SDViewBinder.setTextView(tv_up_experience,userModel.getUpScore()+"");
            SDViewBinder.setTextView(tv_current_experience,"EXP:"+userModel.getCurrentScore());
            SDViewBinder.setTextView(tv_current_level,"LV"+userModel.getUserLevel());
            iv_bg_level.setImageDrawable(
                    getResources().getDrawable(LiveUtils.getLevelBackgroudImageResId(userModel.getCurrentScore(),
                            userModel.getUpScore(),userModel.getDownSocre())));
        }
    }


}
