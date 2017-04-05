package com.fanwe.live.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.UserInviteModel;
import com.fanwe.hybrid.umeng.UmengSocialManager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveLayoutActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.UserInviteDialog;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.RoomShareModel;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;

public class UserInviteActivity extends BaseActivity {

    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.copy_content)
    private TextView copy_content;
    @ViewInject(R.id.invite_code)
    private TextView invite_code;
    @ViewInject(R.id.ll_share_weixin)
    private LinearLayout ll_share_weixin;
    @ViewInject(R.id.ll_share_xinlang)
    private LinearLayout ll_share_xinlang;
    @ViewInject(R.id.ll_share_pengyouquan)
    private LinearLayout ll_share_pengyouquan;
    @ViewInject(R.id.ll_share_qq)
    private LinearLayout ll_share_qq;
    @ViewInject(R.id.ll_share_qzoom)
    private LinearLayout ll_share_qzoom;
    @ViewInject(R.id.iv_invite)
    private ImageView iv_invite;
    @ViewInject(R.id.iv_invite_record)
    private ImageView iv_invite_record;
    protected PopupWindow mPopupWindow;
    private UserInviteDialog mDialog;
    private UserInviteModel mUserInviteModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SDViewUtil.setStatusBarTintResource(this, R.color.transparent);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_user_invite);
        initListener();
        initData();
//        initPullToRefresh();
    }

    private void initData(){
        CommonInterface.requestUserInvite(new AppRequestCallback<UserInviteModel>() {

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.getStatus() == 1) {
                    mUserInviteModel = actModel;
                    invite_code.setText(actModel.getInvite_code());
                    if(TextUtils.isEmpty(actModel.getBind_invite_code())){
                        SDViewUtil.show(iv_invite);
                    }else{
                        SDViewUtil.invisible(iv_invite);
                    }
                    iv_invite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog = new UserInviteDialog(UserInviteActivity.this, actModel.getInvite_code());
                            mDialog.show();
                            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (mDialog.mBindSuccess) {
                                        SDViewUtil.invisible(iv_invite);
                                    }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
//                lv_content.onRefreshComplete();
                super.onFinish(resp);
            }
        });
    }

//    private void initPullToRefresh()
//    {
//        lv_content.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
//                initData();
//            }
//        });
//    }


    private void initListener(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        copy_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = invite_code.getText() + "".trim();
                if(!TextUtils.isEmpty(code)) {
                    // 得到剪贴板管理器
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(code);
                    SDToast.showToast(getString(R.string.already_copy)+"：" + code);
                }
            }
        });

//        invite_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputCode = input_invite_code.getText()+"".trim().replaceAll(" ", "");
//                String code = invite_code.getText() + "".trim().replaceAll(" ","");
//                if(TextUtils.isEmpty(inputCode)){
//                    SDToast.showToast("请输入邀请码");
//                }else if(inputCode.equals(code)) {
//                    SDToast.showToast("不能使用自己的邀请码");
//                }else{
//                    CommonInterface.submitUserInvite(inputCode, new AppRequestCallback<UserInviteModel>() {
//                        @Override
//                        protected void onSuccess(SDResponse sdResponse) {
//                            if (rootModel.getStatus() == 1) {
//                                SDViewUtil.hide(invite_no_bind);
//                                SDViewUtil.show(invite_bind);
//                                bind_user.setText(String.format(getResources().getString(R.string.invete_bined_user), actModel.getBind_invite_user_name(), actModel.getBind_invite_user_id()));
//                                bind_code.setText(String.format(getResources().getString(R.string.invete_bind_code), actModel.getBind_invite_code()));
//                            }else{
//                                SDToast.showToast(actModel.getError());
//                            }
//                        }
//                    });
//                }
//            }
//        });
        ll_share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare(2, umShareListener);
            }
        });
        ll_share_xinlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare(4, umShareListener);
            }
        });
        ll_share_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare(3, umShareListener);
            }
        });
        ll_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare(0, umShareListener);
            }
        });
        ll_share_qzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShare(1, umShareListener);
            }
        });
        iv_invite_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInviteActivity.this, UserInviteRecordActivity.class);
                if (mUserInviteModel != null) {
                    intent.putExtra(UserInviteRecordActivity.EXTRA_INVITE_USER_NUM, mUserInviteModel.getHas_invited_num());
                }
                startActivity(intent);
            }
        });
    }

    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        toInviteRule();
    }


    protected UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            SDToast.showToast(getString(R.string.share_to) + LiveLayoutActivity.convertEnumToString(share_media) +getString(R.string.success));
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
        }
    };

    // 新增自定义面板的分享
    public void openShare(int shareMedia,final UMShareListener listener) {
        if (mUserInviteModel == null) {
            return;
        }
        final RoomShareModel shareModel = mUserInviteModel.getShare();
        if (shareModel == null) {
            return;
        }
        UmengSocialManager.openShare(shareMedia, shareModel.getShareTitle(), shareModel.getShareContent(), shareModel.getShareImageUrl(),
                shareModel.getShareUrl(), this, new UMShareListener() {

                    @Override
                    public void onResult(SHARE_MEDIA media) {
                        String shareKey = shareModel.getShareKey();
                        CommonInterface.requestShareComplete(media.toString(), shareKey, null);
                        if (listener != null) {
                            listener.onResult(media);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA media, Throwable throwable) {
                        if (listener != null) {
                            listener.onError(media, throwable);
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA media) {
                        if (listener != null) {
                            listener.onCancel(media);
                        }
                    }
                });
    }

    private void toInviteRule(){
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                String url = h5Model.getUrl_invite_rule();
                Intent intent = new Intent(UserInviteActivity.this, LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
                startActivity(intent);
            }
        }
    }
}
