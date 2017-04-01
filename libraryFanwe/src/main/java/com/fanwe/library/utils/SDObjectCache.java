package com.fanwe.library.utils;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体内存缓存工具类
 */
public class SDObjectCache
{
    private static final String DEFAULT_CACHE_DIR = "objcache";

    private static Object lock = new Object();
    private static Map<String, Object> mapCache = new HashMap<>();
    private static File cacheDir;

    public static <T> T get(Class<T> clazz)
    {
        synchronized (lock)
        {
            T model = null;
            if (clazz != null)
            {
                String key = clazz.getName();
                Object object = mapCache.get(key);
                if (object != null)
                {
                    model = (T) object;
                }
            }
            return model;
        }
    }

    public static <T> void put(T model)
    {
        synchronized (lock)
        {
            if (model != null)
            {
                Class<?> clazz = model.getClass();
                String key = clazz.getName();
                mapCache.put(key, model);
            }
        }
    }

    public static <T> void remove(Class<T> clazz)
    {
        synchronized (lock)
        {
            if (clazz != null)
            {
                String key = clazz.getName();
                if (mapCache.containsKey(key))
                {
                    mapCache.remove(key);
                }
            }
        }
    }

    public static void clear()
    {
        synchronized (lock)
        {
            mapCache.clear();
        }
    }

    // disk
    public synchronized static void init(Context context)
    {
        if (cacheDir == null)
        {
            cacheDir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
            if (!cacheDir.exists())
            {
                cacheDir.mkdirs();
            }
        }
    }

    private static File newCacheFile(Class<?> clazz)
    {
        File file = null;
        if (clazz != null)
        {
            String key = clazz.getName();
            file = new File(cacheDir, key);
        }
        return file;
    }

    public static <T> boolean putDisk(T model)
    {
        synchronized (lock)
        {
            if (model != null)
            {
                long startTime = System.currentTimeMillis();
                File file = newCacheFile(model.getClass());
                if (file != null)
                {
                    String value = SDJsonUtil.object2Json(model);
                    SDFileUtil.writeToFile(value, file.getAbsolutePath());
                    LogUtil.i("putDisk time:" + (System.currentTimeMillis() - startTime) + "," + model.getClass().getName());
                    return true;
                }
            }
            return false;
        }
    }

    public static <T> T getDisk(Class<T> clazz)
    {
        synchronized (lock)
        {
            long startTime = System.currentTimeMillis();
            File file = newCacheFile(clazz);
            if (file != null && file.exists())
            {
                String content = SDFileUtil.readFromFile(file.getAbsolutePath());
                T t = SDJsonUtil.json2Object(content, clazz);
                LogUtil.i("getDisk time:" + (System.currentTimeMillis() - startTime) + "," + clazz.getName());
                return t;
            } else
            {
                return null;
            }
        }
    }

    public static <T> void removeDisk(Class<T> clazz)
    {
        synchronized (lock)
        {
            File file = newCacheFile(clazz);
            if (file != null && file.exists())
            {
                file.delete();
            }
        }
    }

}
