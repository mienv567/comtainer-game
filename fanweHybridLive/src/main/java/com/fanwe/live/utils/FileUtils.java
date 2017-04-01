package com.fanwe.live.utils;


import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void createDir(String dirPath, boolean nomedia) {
        ensureDirExists(dirPath);
        if (nomedia) {
            File nomediafile = new File(dirPath + "/.nomedia");
            try {
                nomediafile.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public static void ensureDirExists(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }
}
