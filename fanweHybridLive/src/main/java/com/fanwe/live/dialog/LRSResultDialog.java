package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LRSResultRecyclerAdapter;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.model.LRSUserModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 狼人杀游戏结束界面
 */
public class LRSResultDialog extends SDDialogBase {

    @ViewInject(R.id.ll_root_view)
    private LinearLayout ll_root_view;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.ll_close)
    private LinearLayout ll_close; //关闭
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;//确定
    @ViewInject(R.id.tv_result_one_zy)
    private TextView tv_result_one_zy;
    @ViewInject(R.id.tv_result_one_result)
    private TextView tv_result_one_result;
    @ViewInject(R.id.list_view_1)
    private SDRecyclerView list_view_1;
    @ViewInject(R.id.tv_result_two_zy)
    private TextView tv_result_two_zy;
    @ViewInject(R.id.tv_result_two_result)
    private TextView tv_result_two_result;
    @ViewInject(R.id.list_view_2)
    private SDRecyclerView list_view_2;
    private LRSResultRecyclerAdapter adapter1;
    private LRSResultRecyclerAdapter adapter2;
    private int mWin; //1 狼人赢了 2 好人赢了

    public LRSResultDialog(Activity activity, int win) {
        super(activity);
        mWin = win;
        init();
    }


    private void init() {
        setContentView(R.layout.dialog_lrs_result);
        setCanceledOnTouchOutside(false);
        paddingLeft(SDViewUtil.dp2px(20));
        paddingRight(SDViewUtil.dp2px(20));
        x.view().inject(this, getContentView());
        ll_root_view.setAlpha(0.7f);
        register();
        bindData();
    }

    private void register() {
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void bindData() {
       if(mWin == 1){
           SDViewBinder.setTextView(tv_title,"狼人胜利");
           SDViewBinder.setTextView(tv_result_one_result,"失败");
           tv_result_one_result.setTextColor(getOwnerActivity().getResources().getColor(R.color.lrs_result_fail));
           SDViewBinder.setTextView(tv_result_two_result,"胜利");
           tv_result_two_result.setTextColor(getOwnerActivity().getResources().getColor(R.color.lrs_result_win));
       }else{
           SDViewBinder.setTextView(tv_title,"村民胜利");
           SDViewBinder.setTextView(tv_result_one_result,"胜利");
           tv_result_one_result.setTextColor(getOwnerActivity().getResources().getColor(R.color.lrs_result_win));
           SDViewBinder.setTextView(tv_result_two_result,"失败");
           tv_result_two_result.setTextColor(getOwnerActivity().getResources().getColor(R.color.lrs_result_fail));
       }
        adapter1 = new LRSResultRecyclerAdapter(getOwnerActivity());
        list_view_1.setAdapter(adapter1);
        adapter2 = new LRSResultRecyclerAdapter(getOwnerActivity());
        list_view_2.setAdapter(adapter2);
        dealUserGamers();
    }

    private void dealUserGamers(){
        List<LRSUserModel> manList = new ArrayList<>();
        List<LRSUserModel> wolfList = new ArrayList<>();
        List<LRSUserModel> userGamers = LRSManager.getInstance().getGameUsers();
        if(userGamers != null && userGamers.size() > 0){
            for(int i = userGamers.size() - 1; i >= 0;i-- ){
                LRSUserModel model = userGamers.get(i);
                if(model.getIdentity() == LRSUserModel.GAME_ROLE_WOLF){
                    wolfList.add(model);
                }else{
                    manList.add(model);
                }
            }
        }
        adapter1.updateData(manList);
        adapter2.updateData(wolfList);
    }

}
