package com.qy.ysys.yishengyishi.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.ModelCreateTreeCallBack;
import com.qy.ysys.yishengyishi.model.ModelAddNodeCallBack;
import com.qy.ysys.yishengyishi.utils.HandlerManager;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.FragGenealogyTree;
import com.qy.ysys.yishengyishi.views.customviews.FramilyTreeContainer;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTreeMemberActivity extends BaseTitleActivity {
    private View save;
    private View cancle;
    private EditText et_malename;
    private EditText et_malephone;
    private EditText et_femalename;
    private EditText et_femalephone;

    private String grade;
    private int index;
    private int uid;
    private int sex;
    private View ll_male;
    private View ll_female;
    private TextView tv_female_title;
    private TextView tv_male_title;

    private String userName;
    private String phoneNum;


    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("家谱");
        titleView.setRightImage(R.mipmap.ic_dir);
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_add_treemember;
    }

    @Override
    public void onClick(View v) {
        if (v == cancle) {
            finish();
        } else if (v == save) {
            if (0 == sex) {
                userName = et_malename.getText().toString();
                phoneNum = et_malephone.getText().toString();

            } else {
                userName = et_femalename.getText().toString();
                phoneNum = et_femalephone.getText().toString();
            }
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToast("请填入用户名");
                return;
            }


            if (!FragGenealogyTree.HASCREATEDTREE) { // 没有创建树,先创建
                RequestInterface.addTreeMember(uid, 0, new Callback<ModelCreateTreeCallBack>() {
                    @Override
                    public void onResponse(Call<ModelCreateTreeCallBack> call, Response<ModelCreateTreeCallBack> response) {
                        if ("0000".equals(response.code())) {
                            // 正常放回了就代表创建树成功了
                            ToastUtil.showToast("creatTree success");
                            FragGenealogyTree.HASCREATEDTREE = true;
                            HandlerManager.getMainHamdler().post(addMember);

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelCreateTreeCallBack> call, Throwable t) {

                    }
                });
            } else {
                HandlerManager.getMainHamdler().post(addMember);

            }
        }
    }

    private Runnable addMember = new Runnable() {
        @Override
        public void run() {
            RequestInterface.addNode(userName, FramilyTreeContainer.getRank(index)
                    , phoneNum, FramilyTreeContainer.getGender(index)
                    , FramilyTreeContainer.getParentGender(index)
                    , FramilyTreeContainer.indexTransformPosition(index)
                    , 0, uid, new Callback<ModelAddNodeCallBack>() {
                @Override
                public void onResponse(Call<ModelAddNodeCallBack> call, Response<ModelAddNodeCallBack> response) {
                    if (null == response.body()) {
                        ToastUtil.showToast("response返回为空");
                        return;
                    }
                    if ("0000".equals(response.body().getCode())) { // 正常返回说明创建节点成功了
                        AddTreeMemberActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<ModelAddNodeCallBack> call, Throwable t) {
                    Log.e("addNode", t.getMessage());
                }

            });
        }
    };


    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);

        grade = getIntent().getStringExtra("grade");
        index = getIntent().getIntExtra("index", 0);
        sex = FramilyTreeContainer.isMale(index) ? 0 : 1;
        uid = (int) SPUtils.getParam("uid", 0);

    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        save = contentView.findViewById(R.id.tv_save);
        save.setOnClickListener(this);

        cancle = contentView.findViewById(R.id.tv_cancle);
        cancle.setOnClickListener(this);

        ll_male = contentView.findViewById(R.id.ll_male);
        ll_female = contentView.findViewById(R.id.ll_female);

        if (0 == sex) {
            ll_male.setVisibility(View.VISIBLE);
            ll_female.setVisibility(View.GONE);

            et_malename = (EditText) contentView.findViewById(R.id.et_malename);
            et_malephone = (EditText) contentView.findViewById(R.id.et_malephone);
            tv_male_title = (TextView) contentView.findViewById(R.id.tv_ll_male_title);
            tv_male_title.setText(grade);


        } else {
            ll_female.setVisibility(View.VISIBLE);
            ll_male.setVisibility(View.GONE);

            et_femalename = (EditText) contentView.findViewById(R.id.et_femalename);
            et_femalephone = (EditText) contentView.findViewById(R.id.et_femalephone);
            tv_female_title = (TextView) contentView.findViewById(R.id.tv_ll_female_title);
            tv_female_title.setText(grade);
        }


    }
}
