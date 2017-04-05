package com.fanwe.live.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.utils.SDImageUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.ImageFileCompresser;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EUserImageUpLoadComplete;
import com.fanwe.live.model.App_upload_avaterActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.StorageFileUtils;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.io.File;

import uk.co.senab.photoview.PhotoViewCrop;

/**
 * Created by Administrator on 2016/7/14.
 */
public class LiveUploadUserImageActivity extends BaseTitleActivity
{
    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    @ViewInject(R.id.iv_image)
    private PhotoViewCrop mIv_image;
    @ViewInject(R.id.crop_view)
    private View crop_view;

    private String mStrUrl;
    private File mFileOriginal;
    private ImageFileCompresser mCompresser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_upload_user_head);
        init();
    }

    private void init()
    {
        initTitle();
        initCropView();
        initImageView();
        initImageFileCompresser();
        getIntentData();
    }

    private void initCropView()
    {
        SDViewUtil.show(crop_view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (SDViewUtil.getScreenWidth() * 0.8), (int) (SDViewUtil.getScreenWidth() * 0.8));
        crop_view.setLayoutParams(lp);
        mIv_image.setCropView(crop_view);
    }

    private void initImageFileCompresser()
    {
        mCompresser = new ImageFileCompresser();
        mCompresser.setmListener(new ImageFileCompresser.ImageFileCompresserListener()
        {

            @Override
            public void onSuccess(File fileCompressed)
            {
                dismissProgressDialog();
                requestUpload(fileCompressed);
            }

            @Override
            public void onFailure(String msg)
            {
                dismissProgressDialog();
                if (!TextUtils.isEmpty(msg))
                {
                    SDToast.showToast(msg);
                }
            }

            @Override
            public void onStart()
            {
                showProgressDialog(getString(R.string.is_dispose_pic));
            }

            @Override
            public void onFinish()
            {

            }
        });
    }

    protected void requestUpload(File fileCompressed)
    {
        if (fileCompressed == null)
        {
            return;
        }

        if (!fileCompressed.exists())
        {
            return;
        }

        CommonInterface.requestUploadAvatar(fileCompressed, new AppRequestCallback<UserModel>()
        {
            @Override
            public void onStart()
            {
                showProgressDialog(getString(R.string.is_uploading));
            }

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (rootModel.getStatus() == 1)
                {
                    UserModel user = actModel;
                    if (user != null)
                    {
                        if (!TextUtils.isEmpty(user.getHeadImage()))
                        {
                            EUserImageUpLoadComplete event = new EUserImageUpLoadComplete();
                            event.head_image = user.getHeadImage();
                            SDEventManager.post(event);
                            finish();
                        } else
                        {
                            SDToast.showToast(getString(R.string.pic_path_empty));
                        }
                    } else
                    {
                        SDToast.showToast(getString(R.string.user_empty));
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast(getString(R.string.upload_fail));
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    private void initImageView()
    {
        mIv_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void getIntentData()
    {
        mStrUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        if (isEmpty(mStrUrl))
        {
            SDToast.showToast(getString(R.string.pic_not_exist));
            finish();
        }

        mFileOriginal = new File(mStrUrl);
        if (!mFileOriginal.exists())
        {
            SDToast.showToast(getString(R.string.pic_not_exist));
            finish();
        }

        SDViewBinder.setImageView(this,"file://" + mStrUrl, mIv_image);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.upload_head_portrait));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot(getString(R.string.upload));
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {
        clickUpload();
    }

    private void clickUpload()
    {
        long time = System.currentTimeMillis();
        Bitmap bitmap = mIv_image.getCropViewBitmap();
        File dir = StorageFileUtils.createDirPackageName();
        final String dirPath = dir.getAbsolutePath();
        String filename = File.separator + time + ".jpg";
        SDImageUtil.dealImageCompress(dirPath, filename, bitmap, 100);
        mFileOriginal = new File(dirPath + filename);
        mCompresser.compressImageFile(mFileOriginal);
    }


    @Override
    protected void onDestroy()
    {
        mCompresser.deleteCompressedImageFile();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("用户图片上传界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("用户图片上传界面");
        MobclickAgent.onPause(this);
    }
}
