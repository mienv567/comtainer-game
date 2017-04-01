package com.qy.ysys.yishengyishi.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.ModelAddNodeCallBack;
import com.qy.ysys.yishengyishi.model.ModelTreeNode;
import com.qy.ysys.yishengyishi.utils.SPUtils;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 编辑某一个人的内容
 */

public class EditTreeMemberActivity extends BaseTitleActivity {

    private View save;
    private View cancle;
    private ModelTreeNode node;

    private int mPosition;
    private View convertView;
    public EditText et_username;
    public EditText et_phone;
    private View ll_male;
    private View ll_female;
    private EditText et_female_name;
    private EditText et_female_phone;
    private EditText et_male_name;
    private EditText et_male_phone;
    private String grade;
    private String userName;
    private String phoneNumber;
    private TextView female_title;
    private TextView male_title;
    private View male_del;
    private View female_del;
    private int uid;


    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("家谱");
        titleView.setRightImage(R.mipmap.ic_dir);
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.item_edit_treemember;
    }

    @Override
    public void onClick(View v) {
        if (v == cancle) {
            finish();
        } else if (v == save) {
            saveChange();

        } else if (v == male_del) {
            delMember();
        } else if (v == female_del) {
            delMember();
        }
    }

    /**
     * 删除节点
     */
    private void delMember() {
        if (null == node) {
            return;
        }
        RequestInterface.removeNode(0, node.getUuid(), uid,new Callback<ModelAddNodeCallBack>() {
            @Override
            public void onResponse(Call<ModelAddNodeCallBack> call, Response<ModelAddNodeCallBack> response) {
                if (null != response.body()) {
                    if ("0000".equals(response.body().getCode())) {
                        ToastUtil.showToast("节点删除成功!");
                        EditTreeMemberActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelAddNodeCallBack> call, Throwable t) {
                Log.e("removeNode", "" + t.getMessage());
                EditTreeMemberActivity.this.finish();
            }
        });
    }

    /**
     * 保存修改
     */
    private void saveChange() {
        if (node.getGender() == 0) {
            userName = et_male_name.getText().toString();
            phoneNumber = et_male_phone.getText().toString();

        } else {
            userName = et_female_name.getText().toString();
            phoneNumber = et_female_phone.getText().toString();
        }

        // 内容没修改就不需要更新了
        if (node.getName().equals(userName) && node.getPhone().equals(phoneNumber)) {
            return;
        }

        RequestInterface.updateNode(userName, 0, phoneNumber, node.getUuid(), uid, new Callback<ModelAddNodeCallBack>() {
            @Override
            public void onResponse(Call<ModelAddNodeCallBack> call, Response<ModelAddNodeCallBack> response) {
                if (null != response.body()) {
                    if ("0000".equals(response.body().getCode())) {
                        ToastUtil.showToast("保存修改成功");
                        EditTreeMemberActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelAddNodeCallBack> call, Throwable t) {
                Log.e("updateNode", t.getMessage());
            }
        });
    }

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);
//        username = getIntent().getStringExtra("username");
//        phonenum = getIntent().getStringExtra("phontnum");
//        sex1 = getIntent().getIntExtra("sex", 0);

//        mPosition = FragGenealogyTree.traslatTreeNodes.getPosition();
        // 遍历内容并显示
//        mNodes = FragGenealogyTree.traslatTreeNodes.getNodes();

        node = getIntent().getParcelableExtra("node");
        grade = getIntent().getStringExtra("grade");
        uid = (int) SPUtils.getParam("uid", 0);


    }

    @Override
    protected void initView(final View contentView) {
        super.initView(contentView);
        save = contentView.findViewById(R.id.tv_save);
        cancle = contentView.findViewById(R.id.tv_cancle);

        save.setOnClickListener(this);
        cancle.setOnClickListener(this);

        ll_male = contentView.findViewById(R.id.ll_male);
        ll_female = contentView.findViewById(R.id.ll_female);

        et_female_name = (EditText) contentView.findViewById(R.id.et_female_name);
        et_female_phone = (EditText) contentView.findViewById(R.id.et_female_phone);
        female_title = (TextView) contentView.findViewById(R.id.tv_female_title);
        female_del = contentView.findViewById(R.id.tv_female_del);
        female_del.setOnClickListener(this);

        et_male_name = (EditText) contentView.findViewById(R.id.et_male_name);
        et_male_phone = (EditText) contentView.findViewById(R.id.et_male_phone);
        male_title = (TextView) contentView.findViewById(R.id.tv_male_title);
        male_del = contentView.findViewById(R.id.tv_male_del);
        male_del.setOnClickListener(this);


        if (node.getGender() == 0) {
            ll_female.setVisibility(View.GONE);
            ll_male.setVisibility(View.VISIBLE);
            et_male_name.setText(node.getName());
            et_male_phone.setText(node.getPhone());
            male_title.setText(grade);


        } else {
            ll_male.setVisibility(View.GONE);
            ll_female.setVisibility(View.VISIBLE);
            et_female_name.setText(node.getName());
            et_female_phone.setText(node.getPhone());
            female_title.setText(grade);
        }
    }


//        mListView = (ListView) contentView.findViewById(R.id.lv_edit_treemember);
//        mListView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return mNodes.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return null;
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return 0;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                ViewHolder viewHolder;
//
//                if (convertView != null) {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                } else {
//                    viewHolder = new ViewHolder();
//                    if (FramilyTreeContainer.isMale(FramilyTreeContainer.positionTransformIndex(mPosition))) {
//                        convertView = View.inflate(EditTreeMemberActivity.this, R.layout.item_edit_treemember_male, null);
//                    } else {
//                        convertView = View.inflate(EditTreeMemberActivity.this, R.layout.item_edit_treemember_female, null);
//                    }
//                    viewHolder.et_username = (EditText) convertView.findViewById(R.id.et_name);
//                    viewHolder.et_phone = (EditText) convertView.findViewById(R.id.et_phone);
//                    convertView.setTag(viewHolder);
//
//                }
//
//                // 绑定数据
//                viewHolder.et_username.setText(mNodes.get(position).getName());
//                viewHolder.et_phone.setText(mNodes.get(position).getPhone());
//                return convertView;
//            }
//        });
//
//    }
//
//
//    public static class ViewHolder {
//        public EditText et_username;
//        public EditText et_phone;
//    }
}
