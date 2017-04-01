package com.fanwe.library.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SDLocalCache
{
    private static final String DEFAULT_DIR_NAME = "sdlocalcache";
    private static Map<String, SDLocalCache> mapCacheInstance = new HashMap<>();
    private File cacheDir;

    public SDLocalCache get(Context context)
    {
        return get(context, null);
    }

    public SDLocalCache get(Context context, String dirName)
    {
        if (TextUtils.isEmpty(dirName))
        {
            dirName = DEFAULT_DIR_NAME;
        }

        SDLocalCache cacheInstance = mapCacheInstance.get(dirName);
        if (cacheInstance == null)
        {
            File dir = new File(context.getExternalCacheDir(), dirName);
            cacheInstance = new SDLocalCache(dir);
            mapCacheInstance.put(dirName, cacheInstance);
        }

        return cacheInstance;
    }

    private SDLocalCache(File cacheDir)
    {
        this.cacheDir = cacheDir;
    }

    private String newCacheKey(String key)
    {
        String cacheKey = null;
        if (!TextUtils.isEmpty(key))
        {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private File newCacheFile(String key)
    {
        String cacheKey = newCacheKey(key);
        if (!TextUtils.isEmpty(cacheKey))
        {
            return new File(cacheDir, cacheKey);
        } else
        {
            return null;
        }
    }

    // put
    public void put(String key, String value)
    {
        synchronized (cacheDir)
        {
            File file = newCacheFile(key);
            BufferedWriter out = null;
            try
            {
                out = new BufferedWriter(new FileWriter(file), 1024);
                out.write(value);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (out != null)
                {
                    try
                    {
                        out.flush();
                        out.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
