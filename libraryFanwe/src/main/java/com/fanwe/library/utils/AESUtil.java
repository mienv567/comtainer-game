package com.fanwe.library.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * 
 * @ClassName AESUtil
 * @Copyright Fuzhou Daxia Network Technology Co., Ltd. Copyright YYYY-YYYY, All
 *            rights reserved
 * @Description 用于aes加解密
 * @author zhengjun
 * @date 2014年3月6日 下午6:48:44
 * @version V1.0
 */
public class AESUtil
{

	public static final String DEFAULT_KEY = "je98rufj983ufjoa";

	public static String decrypt(String content, String key)
	{
		String result = null;
		byte[] decryptResult = null;
		try
		{
			byte[] contentBytes = Base64.decode(content, 0);
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decryptResult = cipher.doFinal(contentBytes);
			if (decryptResult != null)
			{
				result = new String(decryptResult, "UTF-8");
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}

	public static String decrypt(String content)
	{
		return decrypt(content, DEFAULT_KEY);
	}

	public static String encrypt(String content)
	{
		return encrypt(content, DEFAULT_KEY);
	}

	public static String encrypt(String content, String key)
	{
		byte[] encryptResult = null;
		String result = null;
		try
		{
			byte[] contentBytes = content.getBytes("UTF-8");
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptResult = cipher.doFinal(contentBytes);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (encryptResult != null)
		{
			result = Base64.encodeToString(encryptResult, 0);
		}
		return result;
	}

}
