package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.ModelShowFamilyTree;
import com.qy.ysys.yishengyishi.model.ModelTreeNodes;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.views.AddTreeMemberActivity;
import com.qy.ysys.yishengyishi.views.EditTreeMemberActivity;
import com.qy.ysys.yishengyishi.views.EditTreeNodeOverViewActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragGenealogyTree extends BaseTitleFragment {

    private int uid;
    private int sex;
    private FramilyTreeContainer tree;
    public static List<ModelTreeNodes> treeNodes;
    public static ModelTreeNodes traslatTreeNodes;
    public static boolean HASCREATEDTREE = false; // 是否已经创建过树

    @Override
    protected void initTitleBar(ITitleView titleView) {

    }

    @Override
    protected boolean shouldShowTitle() {
        return false;
    }

    @Override
    protected View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_genealogy_tree, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tree = (FramilyTreeContainer) contentView.findViewById(R.id.ftc);
        tree.setItemOnClickLister(new FramilyTreeContainer.ItemOnClickLister() {

            @Override
            public void onClickIntemView(View v, int index) {
                int position = FramilyTreeContainer.mapArr[index];
                ModelTreeNodes modelTreeNodes = traverse(position);
                String grade = FramilyTreeContainer.names[index];
                if (null != modelTreeNodes) {
                    traslatTreeNodes = modelTreeNodes;
                    // 自己,父亲,祖父,祖母,母亲只会有一个,如果点击直接进入修改内容节目
                    if (index == 9 || index == 3 || index == 0 || index == 1 || index == 4) {
                        Intent intent = new Intent(getActivity(), EditTreeMemberActivity.class);
                        intent.putExtra("node", traslatTreeNodes.getNodes().get(0));
                        intent.putExtra("position", position);
                        intent.putExtra("index", index);
                        getActivity().startActivity(intent);
                    } else {
                        // 如果该辈分可能有多个人,进入概览界面让用户选择要编辑那一个
                        Intent intent = new Intent(getActivity(), EditTreeNodeOverViewActivity.class);
                        intent.putExtra("grade", grade);
                        intent.putExtra("index", index);
                        intent.putExtra("position", position);
                        getActivity().startActivity(intent);
                    }
                } else {
                    // 该节点尚无任何人员信息,直接进入添加节点信息的界面
                    traslatTreeNodes = null;
                    Intent intent = new Intent(getActivity(), AddTreeMemberActivity.class);
                    intent.putExtra("grade", grade);
                    intent.putExtra("position", position);
                    intent.putExtra("index", index);
                    getActivity().startActivity(intent);
                }
            }
        });

        uid = (int) SPUtils.getParam("uid", 0);
        sex = (int) SPUtils.getParam("sex", 0);

    }


    private void refresh() {
        RequestInterface.showTree(uid, 0 ,new Callback<ModelShowFamilyTree>() {
            @Override
            public void onResponse(Call<ModelShowFamilyTree> call, Response<ModelShowFamilyTree> response) {
                Log.i("onResponse", response.body().getCode());
                if ("0000".equals(response.body().getCode())) {
                    treeNodes = response.body().getReturnObj();
                    bindData(treeNodes);
                }
            }

            @Override
            public void onFailure(Call<ModelShowFamilyTree> call, Throwable t) {

            }

        });
    }

    /**
     * 把获取到的数据绑定的视图上
     *
     * @param treeNodes
     */
    private void bindData(List<ModelTreeNodes> treeNodes) {
        if (null == treeNodes) {
            HASCREATEDTREE = false;
            return;
        }
        HASCREATEDTREE = true;
        int count = treeNodes.size();
        for (int i = 0; i < count; i++) {
            ModelTreeNodes modelTreeNodes = treeNodes.get(i);
            int position = modelTreeNodes.getPosition();
            int index = FramilyTreeContainer.positionTransformIndex(position);
            Log.i("position", "position=" + position + "对应的index=" + index);
            if (index >= 0) {
                tree.light(index);
            }
        }
    }

    public ModelTreeNodes traverse(int position) {
        if (null == treeNodes) {
            return null;
        }
        for (int i = 0; i < treeNodes.size(); i++) {
            ModelTreeNodes currentTreeNode = treeNodes.get(i);
            if (position == currentTreeNode.getPosition()) {
                return currentTreeNode;
            }
        }
        return null;
    }
}