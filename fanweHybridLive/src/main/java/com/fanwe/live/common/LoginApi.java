package com.fanwe.live.common;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.fanwe.library.utils.LogUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApi implements Callback {
	private static final int MSG_AUTH_CANCEL = 1;
	private static final int MSG_AUTH_ERROR= 2;
	private static final int MSG_AUTH_COMPLETE = 3;

	private OnLoginListener loginListener;
	private String platform;
	private Context context;
	private Handler handler;

	public LoginApi() {
		handler = new Handler(Looper.getMainLooper(), this);
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setOnLoginListener(OnLoginListener login){
		this.loginListener=login;
	}

	public void login(Context context) {
		this.context = context.getApplicationContext();
		if (platform == null) {
			return;
		}

		//初始化SDK
//		ShareSDK.initSDK(context);
		Platform plat = ShareSDK.getPlatform(platform);
		if (plat == null) {
			return;
		}

		if (plat.isAuthValid()) {
			plat.removeAccount(true);
			return;
		}

		//使用SSO授权，通过客户单授权
		plat.SSOSetting(false);
		plat.setPlatformActionListener(new PlatformActionListener() {
			public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_COMPLETE;
					msg.arg2 = action;
					msg.obj =  new Object[] {plat.getName(), res};
					handler.sendMessage(msg);
				}
			}

			public void onError(Platform plat, int action, Throwable t) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_ERROR;
					msg.arg2 = action;
					msg.obj = t;
					handler.sendMessage(msg);
				}
				t.printStackTrace();
			}

			public void onCancel(Platform plat, int action) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_CANCEL;
					msg.arg2 = action;
					msg.obj = plat;
					handler.sendMessage(msg);
				}
			}
		});
		plat.showUser(null);
	}

	/**处理操作结果*/
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_AUTH_CANCEL: {
				// 取消
				LogUtil.i("授权取消");
				Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
			} break;
			case MSG_AUTH_ERROR: {
				// 失败
				LogUtil.i("授权失败");
				Throwable t = (Throwable) msg.obj;
				String text = "caught error: " + t.getMessage();
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				t.printStackTrace();
			} break;
			case MSG_AUTH_COMPLETE: {
				// 成功
				Object[] objs = (Object[]) msg.obj;
				String plat = (String) objs[0];
				@SuppressWarnings("unchecked")
				HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				LogUtil.i("授权成功");
				if(plat != null){
					logUserInfo(ShareSDK.getPlatform(plat));
				}
				if (loginListener!= null && loginListener.onLogin(plat, res)) {
//					RegisterPage.setOnLoginListener(loginListener);
//					RegisterPage.setPlatform(plat);
//					Intent intent=new Intent(context, RegisterPage.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(intent);
				}
			} break;
		}
		return false;
	}

	private void logUserInfo(Platform platform){
		String gender = "";
		if(platform != null){
			gender = platform.getDb().getUserGender();
			if("m".equals(gender)){
				LogUtil.i("性别 ： 男");
			}else{
				LogUtil.i("性别 ： 女");
			}
			LogUtil.i("头像地址 ：" + platform.getDb().getUserIcon());
			LogUtil.i("userName ：" + platform.getDb().getUserName());
			LogUtil.i("userId ：" + platform.getDb().getUserId());
			LogUtil.i("token ：" + platform.getDb().getToken());
			LogUtil.i("tokenSecret :" + platform.getDb().getTokenSecret());
		}
	}

}
