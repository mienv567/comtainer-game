package com.fanwe.live.appview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.hybrid.utils.SDImageUtil;
import com.fanwe.library.utils.ImageFileCompresser;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.utils.StorageFileUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoViewCrop;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveCropImageView extends BaseAppView
{
    private PhotoViewCrop mIv_image;
    private View crop_view;

    private ImageFileCompresser mCompresser;

    private ImageFileCompresser.ImageFileCompresserListener imageFileCompresserListener;

    public void setImageFileCompresserListener(ImageFileCompresser.ImageFileCompresserListener l)
    {
        this.imageFileCompresserListener = l;
        if (l != null)
        {
            mCompresser.setmListener(imageFileCompresserListener);
        }
    }

    public LiveCropImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveCropImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveCropImageView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_live_crop_image;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        register();
        initCropViewSize();
        initImageFileCompresser();
    }

    private void register()
    {
        mIv_image = find(R.id.iv_image);
        crop_view = find(R.id.crop_view);
    }

    private void initCropViewSize()
    {
        int width = (int) (SDViewUtil.getScreenWidth() * 0.7);
        SDViewUtil.setViewHeight(crop_view, width);
        SDViewUtil.setViewWidth(crop_view, width);
        mIv_image.setCropView(crop_view);
    }

    private void initImageFileCompresser()
    {
        mCompresser = new ImageFileCompresser();
        mCompresser.setmMaxLength(300 * 300);
    }

    public void loadUrl(String url)
    {
        SDViewBinder.setImageView(getActivity(),url, mIv_image);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        mCompresser.deleteCompressedImageFile();
        super.onDetachedFromWindow();
    }

    public void clickUpload()
    {
        long time = System.currentTimeMillis();
        File dir = StorageFileUtils.createDirPackageName();
        String dirPath = dir.getAbsolutePath();
        String filename = File.separator + time + ".jpg";

        Bitmap bitmap = mIv_image.getCropViewBitmap();
        SDImageUtil.dealImageCompress(dirPath, filename, bitmap, 100);
        File file = new File(dirPath + filename);
        mCompresser.compressImageFile(file);
        bitmap.recycle();
    }
}
