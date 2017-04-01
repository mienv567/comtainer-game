package com.fanwe.hybrid.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.text.TextUtils;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.dialog.CustomDialog;
import com.fanwe.hybrid.dialog.CustomDialog.OnConfirmListener;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.InitUpgradeModel;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.live.R;

import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 更新服务
 * 
 * @author yhz
 */
public class AppUpgradeService extends Service
{
	public static final String EXTRA_SERVICE_START_TYPE = "extra_service_start_type";

	private static final int DEFAULT_START_TYPE = 0;

	private int mStartType = DEFAULT_START_TYPE; // 0代表启动app时候程序自己检测，1代表用户手动检测版本

	public static final int mNotificationId = 100;

	private String mDownloadUrl;

	private NotificationManager mNotificationManager;

	private Notification mNotification;

	private PendingIntent mPendingIntent;

	private int mServerVersion = 0;

	private String mFileName;

	private boolean isForceUpgrade = false;

	private static AppUpgradeProgressListener mAppUpgradeProgressListener;
	private Notification.Builder builder;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// mDownloadUrl =
		// "http://gdown.baidu.com/data/wisegame/90f1773d78335827/baidushoujizhushou_16786881.apk";
		initIntentData(intent);
		testUpgrade();
		return super.onStartCommand(intent, flags, startId);
	}

	private void initIntentData(Intent intent)
	{
		mStartType = intent.getIntExtra(EXTRA_SERVICE_START_TYPE, DEFAULT_START_TYPE);
	}

	private void testUpgrade()
	{
		stopSelf();

		final InitActModel initActModel = InitActModelDao.query();
		if (initActModel != null && initActModel.getVersion() != null)
		{
			InitUpgradeModel model = initActModel.getVersion();
			if (model == null)
			{
				SDToast.showToast(getString(R.string.check_new_version_fail));
			} else
			{
				if (isUpGrade(model)) // 有新版本
				{
					showDialogUpgrade(model);
				} else
				{
					if (mStartType == 1) // 用户手动检测版本
					{
						SDToast.showToast(getString(R.string.current_version_is_newest));
					}
				}
			}
		}
	}

	private boolean isUpGrade(InitUpgradeModel model)
	{
		PackageInfo info = SDPackageUtil.getCurrentPackageInfo();
		int curVersion = info.versionCode;
		if (!TextUtils.isEmpty(model.getServerVersion()) && !TextUtils.isEmpty(model.getHasfile()) && !TextUtils.isEmpty(model.getFilename()))
		{
			initDownInfo(model);
			boolean hasfile = SDTypeParseUtil.getInt(model.getHasfile(), 0) == 1 ? true : false;
			if (curVersion < mServerVersion && hasfile)
			{
				SDToast.showToast(getString(R.string.find_new_version));
				return true;
			}
		}
		return false;
	}

	private void initDownInfo(InitUpgradeModel model)
	{
		mDownloadUrl = model.getFilename();
		mServerVersion = SDTypeParseUtil.getInt(model.getServerVersion(), 0);
		mFileName = App.getApplication().getString(R.string.app_name) + "_" + mServerVersion + ".apk";
	}

	private void showDialogUpgrade(final InitUpgradeModel model)
	{
		if (!TextUtils.isEmpty(model.getForced_upgrade()))
		{
			isForceUpgrade = "1".equals(model.getForced_upgrade()) ? true : false;
		}

		if (isForceUpgrade)
		{
			Dialog dialog = CustomDialog.alert(SDActivityManager.getInstance().getLastActivity(), getString(R.string.version_update_content) + model.getAndroid_upgrade(), getString(R.string.confirm), new OnConfirmListener()
			{
				@Override
				public void onConfirmListener()
				{
					startDownload();
				}
			});
			dialog.setCancelable(false);
		} else
		{
			CustomDialog.confirm(SDActivityManager.getInstance().getLastActivity(), getString(R.string.version_update_content) + model.getAndroid_upgrade(), getString(R.string.confirm), getString(R.string.cancel), new OnConfirmListener()
			{

				@Override
				public void onConfirmListener()
				{
					startDownload();
				}
			}, null);
		}
	}
	// 从API22 换成23 的话 这个方法改了一下
	private void initNotification()
	{
		Intent completingIntent = new Intent();
		completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		completingIntent.setClass(getApplication().getApplicationContext(), AppUpgradeService.class);
		mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//自定义样式 原来就注释了，我没动
//		mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.service_download_view);
//		mNotification.contentView.setTextViewText(R.id.upgradeService_tv_appname, mFileName);
//		mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载中");
//		mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "0%");

		if(builder==null) {
			builder = new Notification.Builder(this);
		}
//		builder.setContentInfo("补充内容");
		builder.setContentText("0%"+mFileName+getString(R.string.is_downloading));
		builder.setContentTitle(getString(R.string.downloading));
		builder.setSmallIcon(R.drawable.icon);
		builder.setTicker(mFileName + getString(R.string.is_downloading));
		builder.setAutoCancel(true);
		builder.setWhen(System.currentTimeMillis());
		mNotification = builder.getNotification();

		mNotificationManager.cancel(mNotificationId);
		mNotificationManager.notify(mNotificationId, mNotification);

	}

	private void startDownload()
	{
		File dir = FileUtil.getCacheDir("");
		if (dir == null)
		{
			return;
		}

		String path = dir.getAbsolutePath() + File.separator + mFileName;

		RequestParams params = new RequestParams(mDownloadUrl);
		params.setSaveFilePath(path);
		params.setAutoRename(false);
		params.setAutoResume(false);

		x.http().get(params, new Callback.ProgressCallback<File>()
		{

			@Override
			public void onCancelled(CancelledException arg0)
			{
			}

			@Override
			public void onError(Throwable arg0, boolean arg1)
			{
				SDToast.showToast(getString(R.string.download_fail));
			}

			@Override
			public void onFinished()
			{

			}

			@Override
			public void onSuccess(File file)
			{
				if (file != null)
				{
					dealDownloadSuccess(file.getAbsolutePath());
				} else
				{
					SDToast.showToast(getString(R.string.download_fail));
				}
			}

			@Override
			public void onLoading(long total, long current, boolean isDownloading)
			{

				int progress = (int) ((current * 100) / (total));
				if (mAppUpgradeProgressListener != null)
				{
					mAppUpgradeProgressListener.onProgress(progress);
				}
				//自定义样式
//				mNotification.contentView.setTextViewText(R.id.upgradeService_tv, progress + "%");
				//系统样式（通用）
//				mNotification.setLatestEventInfo(AppUpgradeService.this, getString(R.string.downloading),
//						progress+"%"+mFileName+getString(R.string.is_downloading), mPendingIntent);
				if(builder==null) {
					builder = new Notification.Builder(AppUpgradeService.this);
				}
				builder.setContentText(progress+"%"+mFileName+getString(R.string.is_downloading));
				builder.setContentTitle(getString(R.string.downloading));
				builder.setSmallIcon(R.drawable.ic_logo);
//				builder.setAutoCancel(true);
				builder.setWhen(System.currentTimeMillis());
				builder.setContentIntent(mPendingIntent);
				mNotification = builder.getNotification();

				mNotificationManager.notify(mNotificationId, mNotification);

			}

			@Override
			public void onStarted()
			{
				initNotification();
			}

			@Override
			public void onWaiting()
			{
			}

		});

	}

	public void dealDownloadSuccess(String filePath)
	{
		if (!TextUtils.isEmpty(filePath))
		{
			mNotification.defaults = Notification.DEFAULT_SOUND;
			mNotification.contentIntent = mPendingIntent;
			//自定义样式
//			mNotification.contentView.setTextViewText(R.id.upgradeService_tv_status, "下载完成");
//			mNotification.contentView.setTextViewText(R.id.upgradeService_tv, "100%");
			//系统样式（通用）
//			mNotification.setLatestEventInfo(AppUpgradeService.this, getString(R.string.download_success),
//					"100%"+mFileName, mPendingIntent);

			if(builder==null) {
				builder = new Notification.Builder(AppUpgradeService.this);
			}
			builder.setContentText("100%"+mFileName);
			builder.setContentTitle(getString(R.string.download_success));
			builder.setWhen(System.currentTimeMillis());
			builder.setContentIntent(mPendingIntent);
			mNotification = builder.getNotification();


			mNotificationManager.notify(mNotificationId, mNotification);
			mNotificationManager.cancel(mNotificationId);
			SDPackageUtil.installApkPackage(filePath);
			SDToast.showToast(getString(R.string.download_success));
		}
	}

	public static void setAppUpgradeProgressListener(AppUpgradeProgressListener appUpgradeProgressListener)
	{
		mAppUpgradeProgressListener = appUpgradeProgressListener;
	}

	public static interface AppUpgradeProgressListener
	{
		void onProgress(int progress);
	}

}