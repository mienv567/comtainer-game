package com.qy.ysys.yishengyishi.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.FragEditGroupRecAdapter;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupEditActivity extends BaseTitleActivity {

    private RecyclerView rcyParent;
    private RecyclerView rcyMenber;
    private List<UserInfo> mParents;
    private List<UserInfo> mMenbers;
    private View tv_dissolve;

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("我家");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                ChatGroupEditActivity.this.finish();
            }

            @Override
            public void onClickRight() {
            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_chatgroup_edit;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_dissolve) {
            finish();
        }
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rcyParent = (RecyclerView) contentView.findViewById(R.id.rcv_groupedit_parent);
        rcyParent.setLayoutManager(linearLayoutManager);
        mParents = new ArrayList<>();
        mParents.add(new UserInfo("你爸", 1));
        FragEditGroupRecAdapter parentAdapter = new FragEditGroupRecAdapter(this, mParents);
        rcyParent.setAdapter(parentAdapter);

        LinearLayoutManager linearLayoutManagerForMenber = new LinearLayoutManager(this);
        linearLayoutManagerForMenber.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcyMenber = (RecyclerView) contentView.findViewById(R.id.rcv_groupedit_menber);
        rcyMenber.setLayoutManager(linearLayoutManagerForMenber);
        mMenbers = new ArrayList<>();
        mMenbers.add(new UserInfo("儿子", 1));
        mMenbers.add(new UserInfo("女儿", 2));
        mMenbers.add(new UserInfo("add", 2));
        mMenbers.add(new UserInfo("minus", 2));
        FragEditGroupRecAdapter memberAdapter = new FragEditGroupRecAdapter(this, mMenbers);
        rcyMenber.setAdapter(memberAdapter);

        tv_dissolve = contentView.findViewById(R.id.tv_dissolve);
        tv_dissolve.setOnClickListener(this);
    }
}
