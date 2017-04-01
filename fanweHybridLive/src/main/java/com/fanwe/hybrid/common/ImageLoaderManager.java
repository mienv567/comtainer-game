package com.fanwe.hybrid.common;

import android.graphics.Bitmap;

import com.fanwe.hybrid.app.App;
import com.fanwe.live.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageLoaderManager
{

    public static void initImageLoader()
    {
        if (!ImageLoader.getInstance().isInited())
        {
            ImageLoaderConfiguration config = getConfigurationDefault();
            ImageLoader.getInstance().init(config);
        }
    }

    private static ImageLoaderConfiguration getConfigurationDefault()
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(App.getApplication())
                .memoryCacheSize(3 * 1024 * 1024)
                .diskCacheSize(30 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(getOptionsDefault()).build();
        return config;
    }

    private static DisplayImageOptions getOptionsDefault()
    {
        DisplayImageOptions options = getBuilderDefault().build();
        return options;
    }

    public static DisplayImageOptions.Builder getBuilderDefault()
    {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_image_loading)
                .showImageForEmptyUri(R.drawable.nopic)
                .showImageOnFail(R.drawable.nopic)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true);
        return builder;
    }
//
//    public static DisplayImageOptions getOptionsNoHeadImg()
//    {
//        DisplayImageOptions options = getBuilderDefault()
//                .showImageOnLoading(R.drawable.ic_default_head)
//                .showImageForEmptyUri(R.drawable.ic_default_head)
//                .showImageOnFail(R.drawable.ic_default_head)
//                .resetViewBeforeLoading(false).build();
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsNoCache()
//    {
//        DisplayImageOptions options = getBuilderDefault()
//                .cacheOnDisk(false)
//                .cacheInMemory(false).build();
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsNopicSmall()
//    {
//        DisplayImageOptions options = getBuilderDefault()
//                .showImageForEmptyUri(R.drawable.nopic_expression)
//                .showImageOnFail(R.drawable.nopic_expression).build();
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsNoCacheNoResetViewBeforeLoading()
//    {
//        DisplayImageOptions options = getBuilderDefault()
//                .cacheOnDisk(false)
//                .cacheInMemory(false)
//                .resetViewBeforeLoading(false).build();
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsNoResetViewBeforeLoading()
//    {
//        DisplayImageOptions options = getBuilderDefault().showImageOnLoading(0).resetViewBeforeLoading(false).build();
//        return options;
//    }
//
//    public static boolean isCacheExistOnDisk(String url)
//    {
//        if (!TextUtils.isEmpty(url))
//        {
//            File file = ImageLoader.getInstance().getDiskCache().get(url);
//            if (file != null && file.exists()) // 缓存存在
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean isCacheExistInMemory(String url)
//    {
//        if (!TextUtils.isEmpty(url))
//        {
//            Bitmap bmp = ImageLoader.getInstance().getMemoryCache().get(url);
//            if (bmp != null) // 缓存存在
//            {
//                return true;
//            }
//        }
//        return false;
//    }

}
