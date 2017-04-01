package com.fanwe.live.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class LogUtils {

    private LogUtils() {
    }

    public static void logI(String msg) {
        StringBuilder tag = new StringBuilder("zy_");
        StackTraceElement[] array = Thread.currentThread().getStackTrace();
        if (array != null && array.length > 4) {
            StackTraceElement element = array[3];
            if (element != null) {
                tag.append(element.getClassName());
                tag.append("@");
                tag.append(element.getMethodName());
                tag.append("@");
                tag.append(element.getLineNumber());
            }
        }
        Log.i(tag.toString(), String.valueOf(msg));
        System.out.print(String.valueOf(msg));
    }

    public static void logE(Throwable e) {
        logE(Log.getStackTraceString(e));
    }

    private static boolean isApkDebugable() {
        try {
            Class ActivityThread = Class.forName("android.app.ActivityThread");
            Method currentApplication = ActivityThread.getDeclaredMethod("currentApplication");
            Application app = (Application) currentApplication.invoke(ActivityThread);
            ApplicationInfo info = app.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            Log.e("zy_log", Log.getStackTraceString(e));
        }
        return false;
    }

    private static void logE(String msg) {
        if (!isApkDebugable()) {
            return;
        }

        logI(msg);

        if (TextUtils.isEmpty(msg)) {
            return;
        }
        File log = new File("/mnt/sdcard/zy_log.txt");
        if (!mkdir(log)) {
            return;
        }
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(log, log.exists());
            output.write(msg.getBytes());
            output.flush();
        } catch (Throwable e) {
            logI(Log.getStackTraceString(e));
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Throwable e) {
                    logI(Log.getStackTraceString(e));
                }
            }
        }
    }

    private static boolean mkdir(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && !delete(file)) {
            return false;
        }
        File dir = file.getParentFile();
        if (dir.exists() && !dir.isDirectory() && !delete(dir)) {
            return false;
        }
        if (!dir.exists() && !dir.mkdirs()) {
            return false;
        }
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        return true;
    }

    private static boolean delete(final File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] child = file.listFiles();
            if (child == null || child.length <= 0) {
                return file.delete();
            }
            for (File item : child) {
                if (!delete(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printV(View v) {
        if (v == null) {
            return;
        }
        if (v instanceof ViewGroup) {
            ViewGroup layout = (ViewGroup) v;
            for (int index = 0; index < layout.getChildCount(); index++) {
                printV(layout.getChildAt(index));
            }
        }
        ViewGroup.LayoutParams params = v.getLayoutParams();
        LogUtils.logI(v + " -- " + params.width + "::" + params.height);
    }
}
