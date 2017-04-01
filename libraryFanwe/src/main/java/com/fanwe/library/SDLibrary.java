package com.fanwe.library;

import android.app.Application;

import com.fanwe.library.common.SDCookieManager;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.SDObjectCache;

public class SDLibrary
{

    private static SDLibrary instance;
    private Application application;

    private SDLibraryConfig config;

    public SDLibraryConfig getConfig()
    {
        return config;
    }

    public void setConfig(SDLibraryConfig config)
    {
        this.config = config;
    }

    private SDLibrary()
    {
        config = new SDLibraryConfig();
    }

    public Application getApplication()
    {
        return application;
    }

    public void init(Application application)
    {
        this.application = application;
        SDConfig.getInstance().init(application);
        SDCookieManager.getInstance().init(application);
        SDObjectCache.init(application);
    }

    public static SDLibrary getInstance()
    {
        if (instance == null)
        {
            instance = new SDLibrary();
        }
        return instance;
    }

}
