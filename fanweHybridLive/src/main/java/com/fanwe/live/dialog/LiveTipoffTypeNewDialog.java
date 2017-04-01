package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-26 下午6:16:49 类说明
 */
public class LiveTipoffTypeNewDialog extends LiveBaseDialog {
    @ViewInject(R.id.btn_cancel)
    private TextView btn_cancel;

    @ViewInject(R.id.btn_confim)
    private TextView btn_confim;
    @ViewInject(R.id.rl_root_layout)
    private RelativeLayout rl_root_layout;

    private String to_user_id;

    public LiveTipoffTypeNewDialog(Activity activity, String to_user_id) {
        super(activity);
        this.to_user_id = to_user_id;
        init();

    }

    private void init() {
        setContentView(R.layout.dialog_tipoff_type_new);
        setCanceledOnTouchOutside(true);
        x.view().inject(this, getContentView());
        register();
        getContentView().setAlpha(0.78f);
        SDViewUtil.setViewHeight(rl_root_layout, SDViewUtil.dp2px(120));
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
    }

    private void register() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_confim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestTipoff();
            }
        });
    }


    private void requestTipoff() {
        CommonInterface.requestTipoff(getLiveInfo().getRoomId(), to_user_id, 0, "", new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.already_get_tipoff_msg));
                    dismiss();
                }
            }
        });
    }

}
