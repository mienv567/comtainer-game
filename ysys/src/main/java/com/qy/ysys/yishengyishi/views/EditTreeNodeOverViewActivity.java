package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.EditTreeNodeAdapter;
import com.qy.ysys.yishengyishi.model.ModelTreeNodes;
import com.qy.ysys.yishengyishi.utils.UiUtils;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.FragGenealogyTree;
import com.qy.ysys.yishengyishi.views.customviews.FramilyTreeContainer;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;
import com.qy.ysys.yishengyishi.views.customviews.OverViewSpaceItemDecoration;

/**
 * 点击树状图节点后,如果节点有内容,就进入此节目编辑
 */
public class EditTreeNodeOverViewActivity extends BaseTitleActivity {

    private TextView add_menber;
    private RecyclerView recyclerView;
    private ModelTreeNodes traslatTreeNodes;
    private String grade;
    private int position;
    private int index;
    private int sex;

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);
    }

    @Override
    protected boolean shouldAddTitle() {
        return false;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.activity_add_treenode;
    }

    @Override
    public void onClick(View v) {
        if (v == add_menber) {
            Intent intent = new Intent(this, AddTreeMemberActivity.class);
            intent.putExtra("grade", grade);
            intent.putExtra("index",index);
            intent.putExtra("sex", sex);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        grade = getIntent().getStringExtra("grade");
        position = getIntent().getIntExtra("position", 0);
        index = getIntent().getIntExtra("index", 0);
        sex = FramilyTreeContainer.isMale(FramilyTreeContainer.positionTransformIndex(position)) ? 0 : 1;

        add_menber = (TextView) contentView.findViewById(R.id.add_menber);
        add_menber.setOnClickListener(this);
        add_menber.setText("添加" + grade);

        // 写死来传
        traslatTreeNodes = FragGenealogyTree.traslatTreeNodes;
        recyclerView = (RecyclerView) contentView.findViewById(R.id.rcv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new OverViewSpaceItemDecoration(UiUtils.dip2px(20)));
        recyclerView.setLayoutManager(gridLayoutManager);
        EditTreeNodeAdapter editTreeNodeAdapterp = new EditTreeNodeAdapter(traslatTreeNodes.getNodes(), this);
        editTreeNodeAdapterp.setItemClickListener(new EditTreeNodeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                Intent intent = new Intent(EditTreeNodeOverViewActivity.this, EditTreeMemberActivity.class);
                intent.putExtra("node", traslatTreeNodes.getNodes().get(index));
                intent.putExtra("index",index);
                intent.putExtra("grade", grade);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(editTreeNodeAdapterp);


    }
}
