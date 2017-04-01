package com.fanwe.live.utils;


import android.content.Context;
import android.os.Build;

import com.fanwe.library.utils.LogUtil;

public class DeviceUtil {
    public static  String OS_VERSION = Build.MANUFACTURER+"-"+ Build.MODEL+";"+ Build.VERSION.RELEASE;

    public static String getOsVersion(){
        if (OS_VERSION.length() > 32) {
           return OS_VERSION.substring(0, 32);
        }
        return OS_VERSION;
    }

    public static String getDeviceUuid(Context context){
        DeviceUuidFactory factory = new DeviceUuidFactory(context);
        return (factory.getDeviceUuid() + "").replaceAll("-","");
    }

    public static String getDeviceUuidIncludeEmulator(Context context){
        if(isEmulator()){
            return getEmulatorDeviceUuid(context);
        }
        return getDeviceUuid(context);
    }

    public static String getEmulatorDeviceUuid(Context context){
        String emulator = "emulator";
        return emulator + getDeviceUuid(context).substring(emulator.length());
    }

    public static boolean isEmulator(){
        boolean result = false;
        String serial = "unknown";
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception e) {
            LogUtil.e("isEmulator error : " + e.getMessage().toString());
        }
        if("unknown".equals(serial)){
            result = true;
        }
        return result;
    }
}
