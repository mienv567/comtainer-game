package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_family_indexActModel;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

/**
 * 家族详情
 * Created by Administrator on 2016/9/27.
 */

public class LiveFamilyDetailsActivity extends BaseTitleActivity
{
    @ViewInject(R.id.iv_head_img)
    private CircleImageView iv_head_img;//家族头像
    @ViewInject(R.id.txv_edt_head)
    private TextView txv_edt_head;//编辑头像提示
    @ViewInject(R.id.et_fam_name)
    private EditText et_fam_name;//编辑家族名称
    @ViewInject(R.id.txv_fam_name)
    private TextView txv_fam_name;//家族名臣
    @ViewInject(R.id.et_fam_decl)
    private EditText et_fam_decl;//家族宣言
    @ViewInject(R.id.txv_sure)
    private TextView txv_sure;//家族成员
    @ViewInject(R.id.ll_family)
    private LinearLayout ll_family;
    @ViewInject(R.id.txv_edt)
    private TextView txv_edt;//编辑资料
    @ViewInject(R.id.txv_manage)
    private TextView txv_manage;//成员管理

    private String family_logo;//家族头像
    private String family_name;//家族名称
    private String family_decl;//家族宣言

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_family_data_update_edit);
        initView();
    }

    private void initView()
    {
        initTitle();

        UserModel dao = UserModelDao.query();
        requestFamilyIndex(dao.getFamilyId());

        txv_edt_head.setVisibility(View.GONE);
        et_fam_name.setVisibility(View.GONE);
        txv_fam_name.setVisibility(View.VISIBLE);
        et_fam_decl.setFocusable(false);

        if (dao.getFamilyChieftain() == 1)//是否家族长；0：否、1：是
        {
            ll_family.setVisibility(View.VISIBLE);
            txv_sure.setVisibility(View.GONE);
        }else
        {
            txv_sure.setText("家庭成员");
        }

        txv_sure.setOnClickListener(this);
        txv_edt.setOnClickListener(this);
        txv_manage.setOnClickListener(this);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("家族详情");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
        mTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.txv_sure:
                Intent intentSure = new Intent(this, LiveFamilyMembersListActivity.class);
                startActivity(intentSure);
                break;
            case R.id.txv_edt:
                Intent intent = new Intent(this, LiveFamilyUpdateEditActivity.class);
                intent.putExtra(LiveFamilyUpdateEditActivity.EXTRA_UPDATE,LiveFamilyUpdateEditActivity.EXTRA_UPDATE_DATA);
                intent.putExtra(LiveFamilyUpdateEditActivity.EXTRA_FAMILY_LOGO,family_logo);
                intent.putExtra(LiveFamilyUpdateEditActivity.EXTRA_FAMILY_NAME,family_name);
                intent.putExtra(LiveFamilyUpdateEditActivity.EXTRA_FAMILY_DECL,family_decl);
                startActivity(intent);
                finish();
                break;
            case R.id.txv_manage:
                Intent intentMem = new Intent(this, LiveFamilyMembersListActivity.class);
                startActivity(intentMem);
                break;
        }
    }

    /**
     * 获取家族主页数据
     * @param jid
     */
    private void requestFamilyIndex(int jid)
    {
        CommonInterface.requestFamilyIndex(jid, new AppRequestCallback<App_family_indexActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.getStatus() == 1)
                {
                    family_logo = actModel.getFamily_info().getFamily_logo();
                    family_name = actModel.getFamily_info().getFamily_name();
                    family_decl = actModel.getFamily_info().getFamily_manifesto();
                    SDViewBinder.setImageView(LiveFamilyDetailsActivity.this,family_logo, iv_head_img);
                    SDViewBinder.setTextView(txv_fam_name,family_name);
                    SDViewBinder.setTextView(et_fam_decl,family_decl);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
            }
        });
    }
}
