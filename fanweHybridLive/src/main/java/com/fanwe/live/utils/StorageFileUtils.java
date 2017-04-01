package com.fanwe.live.utils;

import android.os.Environment;

import com.fanwe.library.utils.SDPackageUtil;

import org.xutils.common.util.FileUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/9/18.
 */
public class StorageFileUtils
{
    public static File createDirPackageName()
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

    public static String CROP_IMAGEFlODER = "cropimage";

    public static File createCropImageDir()
    {
        File crop_file;
        String dirName = SDPackageUtil.getPackageName();
        if (FileUtil.existsSdcard())
        {
            File dir_frist = new File(Environment.getExternalStorageDirectory(), dirName);
            if (!dir_frist.exists())
            {
                dir_frist.mkdir();
            }

            String crop_dir = dir_frist + File.separator + CROP_IMAGEFlODER;
            crop_file = new File(crop_dir);
            if (!crop_file.exists())
            {
                crop_file.mkdir();
            }
        } else
        {
            File dir_frist = new File(Environment.getDataDirectory(), dirName);
            if (!dir_frist.exists())
            {
                dir_frist.mkdir();
            }

            String crop_dir = dir_frist + File.separator + CROP_IMAGEFlODER;
            crop_file = new File(crop_dir);
            if (!crop_file.exists())
            {
                crop_file.mkdir();
            }
        }
        return crop_file;
    }
}
