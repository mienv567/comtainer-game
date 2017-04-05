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
import com.fanwe.live.common.AliyunOssManage;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_aliyun_stsActModel;
import com.fanwe.live.utils.StorageFileUtils;

import org.xutils.view.annotation.ViewInject;

import java.io.File;

import uk.co.senab.photoview.PhotoViewCrop;

/**
 * Created by Administrator on 2016/7/14.
 */
public class LiveUploadUserImageActivityTest extends BaseTitleActivity
{
    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    @ViewInject(R.id.iv_image)
    private PhotoViewCrop mIv_image;
    @ViewInject(R.id.crop_view)
    private View crop_view;

    private String mStrUrl;
    private File mFileOriginal;
    private ImageFileCompresser mCompresser;

    private App_aliyun_stsActModel app_aliyun_stsActModel;

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
        requestAliyunSts();
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
                showProgressDialog("正在处理图片");
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

        String path = fileCompressed.getPath();

        AliyunOssManage.getInstance().uploadImage(app_aliyun_stsActModel.getBucket(), "test", path);
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
            SDToast.showToast("图片不存在");
            finish();
        }

        mFileOriginal = new File(mStrUrl);
        if (!mFileOriginal.exists())
        {
            SDToast.showToast("图片不存在");
            finish();
        }

        SDViewBinder.setImageView(this,"file://" + mStrUrl, mIv_image);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("上传头像");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("上传");
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

    public void requestAliyunSts()
    {
        CommonInterface.requestAliyunSts(new AppRequestCallback<App_aliyun_stsActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (rootModel.getStatus() == 1)
                {
                    String accessKeyId = actModel.getAccessKeyId();
                    String accessKeySecret = actModel.getAccessKeySecret();
                    String endPoint = actModel.getEndpoint();
                    if (TextUtils.isEmpty(accessKeyId))
                    {
                        SDToast.showToast("accessKeyId为空");
                        return;
                    }
                    if (TextUtils.isEmpty(accessKeySecret))
                    {
                        SDToast.showToast("accessKeySecret为空");
                        return;
                    }
                    if (TextUtils.isEmpty(endPoint))
                    {
                        SDToast.showToast("endPoint为空");
                        return;
                    }
                  String bucket = actModel.getBucket();
                    if (TextUtils.isEmpty(bucket))
                    {
                        SDToast.showToast("bucket为空");
                        return;
                    }

                    app_aliyun_stsActModel = actModel;

                    AliyunOssManage.getInstance().init(getApplicationContext(), accessKeyId, accessKeySecret, endPoint);
                }
            }
        });
    }
}
