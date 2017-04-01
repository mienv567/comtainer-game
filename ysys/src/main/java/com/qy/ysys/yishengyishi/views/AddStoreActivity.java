package com.qy.ysys.yishengyishi.views;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.event.ENeedRefreshMemoirs;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.BaseModel;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoreActivity extends BaseTitleActivity {

    @BindView(R2.id.tv_submit)
    TextView mSubmit;
    @BindView(R2.id.et_title)
    TextView mTitle;
    @BindView(R2.id.et_description)
    TextView mDescription;

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("创建相册");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onClickRight() {

            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_addstore;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        initListener();
    }

    private void initListener(){
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText()+"".replace(" ", "");
                String description = mDescription.getText()+"".replace(" ", "");
                if(TextUtils.isEmpty(title)){
                    ToastUtil.showToast("标题不能为空！");
                    return;
                }else if(TextUtils.isEmpty(description)){
                    ToastUtil.showToast("描述不能为空！");
                    return;
                }else{
                    RequestInterface.createMemoir(title, description, System.currentTimeMillis(), "长沙", RequestInterface.MEMOIR_FM_TYPE_DEFAULT,
                            RequestInterface.MEMOIR_TYPE_GALLERY, null, new Callback<BaseModel>() {
                                @Override
                                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                                    if(response.body().isSuccess()){
                                        ToastUtil.showToast("创建成功!");
                                        EventBus.getDefault().post(new ENeedRefreshMemoirs());
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseModel> call, Throwable t) {
                                    ToastUtil.showToast("创建失败!");
                                    finish();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}
