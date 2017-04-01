package com.fanwe.live.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.fanwe.library.utils.LogUtil;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 广告id获取帮助类
 */
public class AIDUtil {
	
	private static final String AID_KEY = "cztaid";
	private static final String AID_VERSION_KEY = "cztaid_version";
	public static final String ROOT_DIR = "malatv";
	public static final String ACTIVE = "_ai.dat";
	private static String mAID;
	/**
	 * 返回市场。  如果获取失败返回""
	 * @param context
	 * @return
	 */
	public static String getAID(Context context){
		return getAID(context, "");
	}
	/**
	 * 返回市场。  如果获取失败返回defaultChannel
	 * @param context
	 * @param defaultChannel
	 * @return
	 */
	public static String getAID(Context context, String defaultChannel) {
		//内存中获取
		if(!TextUtils.isEmpty(mAID)){
			return mAID;
		}
		//sp中获取
		mAID = getChannelBySharedPreferences(context);
		if(!TextUtils.isEmpty(mAID)){
			return mAID;
		}
		//从apk中获取
		mAID = getChannelFromApk(context, AID_KEY);
		if(!TextUtils.isEmpty(mAID)){
			//保存sp中备用
			saveChannelBySharedPreferences(context, mAID);
			return mAID;
		}
		//全部获取失败
		return defaultChannel;
    }
	/**
	 * 从apk中获取版本信息
	 * @param context
	 * @param channelKey
	 * @return
	 */
	private static String getChannelFromApk(Context context, String channelKey) {
		//从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split != null && split.length >= 2) {
        	channel = ret.substring(split[0].length() + 1);
        }
        return channel;
	}
	/**
	 * 本地保存channel & 对应版本号
	 * @param context
	 * @param channel
	 */
	private static void saveChannelBySharedPreferences(Context context, String channel){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(AID_KEY, channel);
		editor.putInt(AID_VERSION_KEY, getVersionCode(context));
		editor.commit();
	}
	/**
	 * 从sp中获取channel
	 * @param context
	 * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
	 */
	private static String getChannelBySharedPreferences(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int currentVersionCode = getVersionCode(context);
		if(currentVersionCode == -1){
			//获取错误
			return "";
		}
		int versionCodeSaved = sp.getInt(AID_VERSION_KEY, -1);
		if(versionCodeSaved == -1){
			//本地没有存储的channel对应的版本号
			//第一次使用  或者 原先存储版本号异常
			return "";
		}
		if(currentVersionCode != versionCodeSaved){
			return "";
		}
		return sp.getString(AID_KEY, "");
	}
	/**
	 * 从包信息中获取版本号
	 * @param context
	 * @return
	 */
	private static int getVersionCode(Context context){
		try{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}catch(NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static String ACTIVE_FILE_PATH = getRootDir() + File.separator + ACTIVE;

	public static String getRootDir() {
		return Environment.getExternalStorageDirectory().getPath() + File.separator
				+ ROOT_DIR;
	}

	public static boolean isAIdExist(String aId){
		boolean result = false;
		String aIds = readFileSdcardFile(ACTIVE_FILE_PATH);
		if(!TextUtils.isEmpty(aIds)) {
			String[] aArr = aIds.split("@");
			if (aArr != null && aArr.length > 0) {
				for (String aId_ : aArr) {
					if (aId_.equals(aId)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public static void saveAId(String aId){
		String gameIds = readFileSdcardFile(ACTIVE_FILE_PATH);
		if(!TextUtils.isEmpty(gameIds)){
			if(!isAIdExist(aId)){
				gameIds += "@" + aId;
			}
		}else{
			gameIds = aId;
		}
		writeFileSdcardFile(ACTIVE_FILE_PATH, gameIds);
	}

	private static void writeFileSdcardFile(String fileName,String write_str){
		try{

			FileOutputStream fout = new FileOutputStream(fileName);
			byte [] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}catch(Exception e){
			LogUtil.e("writeFileSdcardFile() error : " + e);
		}
	}

	private static String readFileSdcardFile(String fileName){
		String res="";
		try{
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte [] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();
		} catch(Exception e){
			LogUtil.e("readFileSdcardFile(String fileName) error : " + e);
		}
		return res;
	}
}
