package com.fanwe.live.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EUpLoadImageComplete;
import com.fanwe.live.model.App_uploadImageActModel;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.util.FileUtil;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

import uk.co.senab.photoview.PhotoViewCrop;

/**
 * Created by Administrator on 2016/7/25.
 */
public class LiveUploadImageActivity extends BaseTitleActivity
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
        setContentView(R.layout.act_upload_image);
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
                requestUpload(fileCompressed);
            }

            @Override
            public void onFailure(String msg)
            {
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
                dismissProgressDialog();

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

        CommonInterface.requestUploadImage(fileCompressed, new AppRequestCallback<App_uploadImageActModel>()
        {
            @Override
            public void onStart()
            {
                showProgressDialog(getString(R.string.is_uploading));
            }

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    String path = actModel.getPath();
                    if (!TextUtils.isEmpty(path))
                    {
                        EUpLoadImageComplete event = new EUpLoadImageComplete();
                        event.server_path = path;
                        event.local_path = mStrUrl;
                        SDEventManager.post(event);
                        finish();
                    } else
                    {
                        SDToast.showToast(getString(R.string.pic_path_empty));
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

        SDViewBinder.setImageView(this,"file://" + mStrUrl,mIv_image);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.upload_auth_pic));
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
        File dir = createDirPackageName();
        final String dirPath = dir.getAbsolutePath();
        String filename = File.separator + time + ".jpg";
        SDImageUtil.dealImageCompress(dirPath, filename, bitmap, 100);
        mFileOriginal = new File(dirPath + filename);
        mStrUrl = dirPath + filename;
        mCompresser.compressImageFile(mFileOriginal);
    }

    private File createDirPackageName()
    {
        File dir;
        String dirName = SDPackageUtil.getPackageName();
        if (FileUtil.existsSdcard())
        {
            dir = new File(Environment.getExternalStorageDirectory(), dirName);
        } else
        {
            dir = new File(Environment.getDataDirectory(), dirName);
        }
        return dir;
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
        MobclickAgent.onPageStart("图片上传界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片上传界面");
        MobclickAgent.onPause(this);
    }
}
