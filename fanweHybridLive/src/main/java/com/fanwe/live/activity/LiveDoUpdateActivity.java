package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDSimpleTextAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.dialog.SDDialogMenu;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.SDTabImage;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.event.EUserImageUpLoadComplete;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LiveDoUpdateActivity extends BaseTitleActivity
{
    public static final String EXTRA_USER_MODEL = "extra_user_model";

//    @ViewInject(R.id.tab_male)
//    private SDTabImage tabImageMale;
//    @ViewInject(R.id.tab_right)
//    private SDTabImage tabImageFeMale;

    @ViewInject(R.id.iv_head)
    private ImageView iv_head;
    @ViewInject(R.id.tv_finish)
    private TextView tv_finish;
    @ViewInject(R.id.et_nickname)
    private EditText et_nickname;
//    @ViewInject(R.id.tv_text_limit)
//    private TextView tv_text_limit;

    private SDSelectViewManager<SDTabImage> mSelectManager = new SDSelectViewManager<SDTabImage>();

    private PhotoHandler mPhotoHandler;

    private UserModel user;

    private String user_id;
    private String nick_name;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_do_upadte);
        init();
    }

    private void init()
    {
        getIntentInfo();
        register();
        initTitle();
        initTabImageMale();
        initPhotoHandler();
        bindData();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.mobile_register));
    }

    private void getIntentInfo()
    {
        if (getIntent().hasExtra(EXTRA_USER_MODEL))
        {
            user = (UserModel) getIntent().getExtras().getSerializable(EXTRA_USER_MODEL);
            this.user_id = user.getUserId();
        }
    }

    private void register()
    {
        iv_head.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        et_nickname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
//                String text = et_nickname.getText().toString();
//                if (!TextUtils.isEmpty(text))
//                {
//                    int intLength = text.length();
//                    String strLength = Integer.toString(intLength);
//                    tv_text_limit.setText(strLength);
//                } else
//                {
//                    tv_text_limit.setText("0");
//                }
            }
        });
    }

    private void bindData()
    {
        String user_nick_name = user.getNickName();
        if (!TextUtils.isEmpty(user_nick_name))
        {
            et_nickname.setText(user_nick_name);
        }

        String user_head_image = user.getHeadImage();
        if (!TextUtils.isEmpty(user_head_image))
        {
            SDViewBinder.setImageView(LiveDoUpdateActivity.this,user_head_image, iv_head,R.drawable.ic_default_head);
        }
    }

    private void initTabImageMale()
    {
//        tabImageMale.getViewConfig(tabImageMale.mIv_image).setImageNormalResId(R.drawable.ic_male).setImageSelectedResId(R.drawable.ic_selected_male);
//        tabImageFeMale.getViewConfig(tabImageFeMale.mIv_image).setImageNormalResId(R.drawable.ic_female).setImageSelectedResId(R.drawable.ic_selected_female);

        mSelectManager.setListener(new SDSelectManager.SDSelectManagerListener<SDTabImage>()
        {

            @Override
            public void onNormal(int index, SDTabImage item)
            {
            }

            @Override
            public void onSelected(int index, SDTabImage item)
            {
                switch (index)
                {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    default:
                        break;
                }
            }

        });
//
//        mSelectManager.setItems(new SDTabImage[]
//                {tabImageMale, tabImageFeMale});

        int sex = user.getSex();
        if (sex == 2)
        {
            mSelectManager.performClick(1);
        } else
        {
            mSelectManager.performClick(0);
        }
    }

    private void initPhotoHandler()
    {
        mPhotoHandler = new PhotoHandler(this);
        mPhotoHandler.setListener(new PhotoHandler.PhotoHandlerListener()
        {

            @Override
            public void onResultFromCamera(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onResultFromAlbum(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onFailure(String msg)
            {
                SDToast.showToast(msg);
            }
        });
    }

    private void dealImageFile(File file)
    {
        Intent intent = new Intent(this, LiveUploadUserImageActivity.class);
        intent.putExtra(LiveUploadUserImageActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
        startActivity(intent);
    }

    private void click0()
    {
        sex = "1";
    }

    private void click1()
    {
        sex = "2";
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.iv_head:
                clickIvHead();
                break;
            case R.id.tv_finish:
                clickTvFinish();
                break;
        }
    }

    private void clickIvHead()
    {
        showBotDialog();
    }

    private void showBotDialog()
    {
        String[] arrOption = new String[]
                {getString(R.string.take_photo), getString(R.string.gallery)};

        List<String> listOptions = Arrays.asList(arrOption);

        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(listOptions, mActivity);

        SDDialogMenu dialog = new SDDialogMenu(this);

        dialog.setAdapter(adapter);
        dialog.setmListener(new SDDialogMenu.SDDialogMenuListener()
        {

            @Override
            public void onItemClick(View v, int index, SDDialogMenu dialog)
            {
                switch (index)
                {
                    case 0:
                        mPhotoHandler.getPhotoFromCamera();
                        break;
                    case 1:
                        mPhotoHandler.getPhotoFromAlbum();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }

            @Override
            public void onDismiss(SDDialogMenu dialog)
            {
            }

            @Override
            public void onCancelClick(View v, SDDialogMenu dialog)
            {
            }
        });
        dialog.showBottom();
    }

    private void clickTvFinish()
    {
        requestdoUpdate();
    }

    private void requestdoUpdate()
    {
        nick_name = et_nickname.getText().toString();
        if (TextUtils.isEmpty(nick_name))
        {
            SDToast.showToast(getString(R.string.please_input_nick_name));
            return;
        }

        if (nick_name.trim().length() == 0)
        {
            SDToast.showToast(getString(R.string.nick_name_not_empty));
            return;
        }

        CommonInterface.requestDoUpdate(user_id, nick_name, sex, null, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            public void onStart() {
                showProgressDialog(getString(R.string.is_commiting_data));
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {

                    UserModel user = actModel.getUser();
                    if (user != null) {
                        boolean is_login_success = UserModelDao.insertOrUpdate(user);
                        if (is_login_success) {
                            EUserLoginSuccess event = new EUserLoginSuccess();
                            SDEventManager.post(event);
                            if (AppRuntimeWorker.hasRecommendRoom()) {
                                AppRuntimeWorker.startContext();
                            } else {
                                Intent intent = new Intent(LiveDoUpdateActivity.this, LiveMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            SDToast.showToast(getString(R.string.save_user_info_fail));
                        }
                    } else {
                        SDToast.showToast(getString(R.string.user_info_is_empty));
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    public void onEventMainThread(EStartContextComplete event)
    {
        if (!LiveUtils.isResultOk(event.result))
        {
            LogUtil.e("启动sdk失败:" + event.result);
            Intent intent = new Intent(this, LiveMainActivity.class);
            startActivity(intent);
            finish();
        }else{
            AppRuntimeWorker.joinRecommendRoom(LiveDoUpdateActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHandler.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(EUserImageUpLoadComplete event)
    {
        SDViewBinder.setImageView(LiveDoUpdateActivity.this,iv_head, event.head_image,R.drawable.ic_default_head);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("用户信息完善界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("用户信息完善界面");
        MobclickAgent.onPause(this);
    }
}
