package com.fanwe.library.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.fanwe.library.utils.AESUtil;
import com.fanwe.library.utils.SDJsonUtil;

public class SDConfig
{
    private static SDConfig mConfig;
    private Application mApplication;
    private Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private String mStrFilename;

    private SDConfig()
    {

    }

    public static SDConfig getInstance()
    {
        if (mConfig == null)
        {
            mConfig = new SDConfig();
        }
        return mConfig;
    }

    public SDConfig init(Application application)
    {
        this.mApplication = application;
        loadConfig(null);
        return this;
    }

    public void loadConfig(String fileName)
    {
        try
        {
            if (TextUtils.isEmpty(fileName))
            {
                fileName = mApplication.getPackageName();
            }

            if (!fileName.equals(mStrFilename))
            {
                this.mStrFilename = fileName;

                mSharedPreferences = mApplication.getSharedPreferences(mStrFilename, Context.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public String getFileName()
    {
        return mStrFilename;
    }

    public void setBoolean(String key, boolean value)
    {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void setFloat(String key, float value)
    {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void setInt(String key, int value)
    {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setLong(String key, long value)
    {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void setString(String key, String value)
    {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setDouble(String key, double value)
    {
        setString(key, String.valueOf(value));
    }

    public void setByte(String key, byte[] value)
    {
        setString(key, String.valueOf(value));
    }

    public void setShort(String key, short value)
    {
        setString(key, String.valueOf(value));
    }

    public void setString(int resID, String value)
    {
        setString(this.mApplication.getString(resID), value);
    }

    public void setInt(int resID, int value)
    {
        setInt(this.mApplication.getString(resID), value);
    }

    public void setBoolean(int resID, boolean value)
    {
        setBoolean(this.mApplication.getString(resID), value);
    }

    public void setByte(int resID, byte[] value)
    {
        setByte(this.mApplication.getString(resID), value);
    }

    public void setShort(int resID, short value)
    {
        setShort(this.mApplication.getString(resID), value);
    }

    public void setLong(int resID, long value)
    {
        setLong(this.mApplication.getString(resID), value);
    }

    public void setFloat(int resID, float value)
    {
        setFloat(this.mApplication.getString(resID), value);
    }

    public void setDouble(int resID, double value)
    {
        setDouble(this.mApplication.getString(resID), value);
    }

    public String getString(String key, String defaultValue)
    {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue)
    {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, Boolean defaultValue)
    {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public byte[] getByte(String key, byte[] defaultValue)
    {
        try
        {
            return getString(key, "").getBytes();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public short getShort(String key, Short defaultValue)
    {
        try
        {
            return Short.valueOf(getString(key, ""));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public long getLong(String key, Long defaultValue)
    {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key, Float defaultValue)
    {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public double getDouble(String key, Double defaultValue)
    {
        try
        {
            return Double.valueOf(getString(key, ""));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public String getString(int resID, String defaultValue)
    {
        return getString(this.mApplication.getString(resID), defaultValue);
    }

    public int getInt(int resID, int defaultValue)
    {
        return getInt(this.mApplication.getString(resID), defaultValue);
    }

    public boolean getBoolean(int resID, Boolean defaultValue)
    {
        return getBoolean(this.mApplication.getString(resID), defaultValue);
    }

    public byte[] getByte(int resID, byte[] defaultValue)
    {
        return getByte(this.mApplication.getString(resID), defaultValue);
    }

    public short getShort(int resID, Short defaultValue)
    {
        return getShort(this.mApplication.getString(resID), defaultValue);
    }

    public long getLong(int resID, Long defaultValue)
    {
        return getLong(this.mApplication.getString(resID), defaultValue);
    }

    public float getFloat(int resID, Float defaultValue)
    {
        return getFloat(this.mApplication.getString(resID), defaultValue);
    }

    public double getDouble(int resID, Double defaultValue)
    {
        return getDouble(this.mApplication.getString(resID), defaultValue);
    }

    public void removeObject(Class<?> clazz)
    {
        if (clazz != null)
        {
            String key = clazz.getName();
            remove(key);
        }
    }

    public void setObject(Object obj)
    {
        setObject(obj, false);
    }

    public <T> T getObject(Class<T> clazz)
    {
        return getObject(clazz, false);
    }

    public void setObject(Object obj, boolean encrypt)
    {
        if (obj != null)
        {
            String key = obj.getClass().getName();
            String value = SDJsonUtil.object2Json(obj);
            if (encrypt)
            {
                value = AESUtil.encrypt(value);
            }
            setString(key, value);
        }
    }

    public <T> T getObject(Class<T> clazz, boolean decrypt)
    {
        T obj = null;
        if (clazz != null)
        {
            String key = clazz.getName();
            String value = getString(key, null);
            if (value != null)
            {
                if (decrypt)
                {
                    value = AESUtil.decrypt(value);
                }
                obj = SDJsonUtil.json2Object(value, clazz);
            }
        }
        return obj;
    }

    public void remove(String key)
    {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void remove(String... keys)
    {
        for (String key : keys)
        {
            remove(key);
        }
    }

    public void clear()
    {
        mEditor.clear();
        mEditor.commit();
    }
}
