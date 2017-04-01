package com.fanwe.live.common;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.Api_linkModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.activity.room.LiveCreaterActivity;
import com.fanwe.live.activity.room.LiveLandscapeCreatorActivity;
import com.fanwe.live.activity.room.LivePlaybackActivity;
import com.fanwe.live.activity.room.LivePushCreaterActivity;
import com.fanwe.live.activity.room.LivePushViewerActivity;
import com.fanwe.live.activity.room.LiveVideoActivity;
import com.fanwe.live.activity.room.LiveViewerActivity;
import com.fanwe.live.control.QNSdkControl;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_RegionListActModel;
import com.fanwe.live.model.CreateLiveData;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.PlayBackData;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveNetChecker;

import java.util.List;

public class AppRuntimeWorker {


    /**
     * 是否隐藏所有竞拍功能
     *
     * @return
     */
    public static int getShow_hide_pai_view() {
        int show_hide_pai_view = 0;
        if (getPai_virtual_btn() == 1 && getPai_real_btn() == 1) {
            show_hide_pai_view = 1;
        } else {
            show_hide_pai_view = 0;
        }

        boolean is_show = SDResourcesUtil.getResources().getBoolean(R.bool.show_hide_pai_view);
        if (is_show) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 直播间是否显示虚拟竞拍按钮
     *
     * @return
     */
    public static int getPai_virtual_btn() {
        int pai_virtual_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            pai_virtual_btn = model.getPai_virtual_btn();
        }
        return pai_virtual_btn;
    }

    /**
     * 直播间是否显示实物竞拍按钮
     *
     * @return
     */
    public static int getPai_real_btn() {
        int pai_real_btn = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            pai_real_btn = model.getPai_real_btn();
        }
        return pai_real_btn;
    }

    /**
     * @return true-可编辑美颜,false-开关模式
     */
    public static boolean isBeautyEditMode() {
        boolean result = true;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            if (initActModel.getBeauty_close() == 1) {
                //开关模式
                result = false;
            }
        }
        return result;
    }

    /**
     * 获得推流类型直播间的真实美颜参数值
     *
     * @param progress 0-100
     * @return 0-10
     */
    public static int getRealBeautyProgressPush(int progress) {
        float value = ((float) progress / 100) * 10;
        return (int) value;
    }

    /**
     * 获得美颜百分比0-100
     */
    public static int getBeautyProgress() {
        int result = 0;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getBeauty_android();
            if (isBeautyEditMode()) {
                //可调节模式
                int savedProgress = SDConfig.getInstance().getInt(R.string.config_beauty_progress, 0);
                if (savedProgress > 0) {
                    result = savedProgress;
                }
            }
        }
        return result;
    }

    /**
     * 设置保存美颜百分比0-100
     *
     * @param progress 0-100
     */
    public static void setBeautyProgress(int progress) {
        SDConfig.getInstance().setInt(R.string.config_beauty_progress, progress);
    }

    /**
     * 当为1时,启用脏子过滤;默认0时不过滤
     *
     * @return
     */
    public static int getHas_dirty_words() {
        int result = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            result = model.getHas_dirty_words();
        }
        return result;
    }

    /**
     * 获得当前城市
     *
     * @return
     */
    public static String getCity_name() {
        String cityname = null;

        InitActModel model = InitActModelDao.query();
        if (model != null) {
            cityname = model.getCity();
        }
        return cityname;
    }

    /**
     * 获得钱票字符串
     *
     * @return
     */
    public static String getTicketName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getTicket_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.live_ticket);
        }
        return result;
    }

    /**
     * 获得帐号字符串
     *
     * @return
     */
    public static String getAccountName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getAccount_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.live_account);
        }
        return result;
    }

    /**
     * 获得app短名称字符串
     *
     * @return
     */
    public static String getAppShortName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getShort_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = getAppName();
        }
        return result;
    }

    /**
     * 获得app名称字符串
     *
     * @return
     */
    public static String getAppName() {
        String result = null;
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            result = initActModel.getApp_name();
        }
        if (TextUtils.isEmpty(result)) {
            result = SDResourcesUtil.getString(R.string.app_name);
        }
        return result;
    }

    public static App_RegionListActModel getRegionListActModel() {
        App_RegionListActModel actModel = null;
        int localVersion = SDConfig.getInstance().getInt(R.string.config_region_version, 0);
        if (localVersion != 0) {
            InitActModel initActModel = InitActModelDao.query();
            if (initActModel != null) {
                if (initActModel.getRegion_versions() > localVersion) {
                    //需要升级
                } else {
                    actModel = SDConfig.getInstance().getObject(App_RegionListActModel.class);
                }
            }
        }
        return actModel;
    }

    public static boolean isLogin(Activity activity) {
        boolean result = false;
        UserModel user = UserModelDao.query();
        if (user != null && !TextUtils.isEmpty(user.getUserId())) {
            result = true;
        } else {
            result = false;
            if (activity != null) {
                Intent intent = new Intent(activity, LiveLoginActivity.class);
                activity.startActivity(intent);
            }
        }
        return result;
    }


    public static String getApiUrl(String ctl, String act) {
        if (!TextUtils.isEmpty(ctl) && !TextUtils.isEmpty(act)) {
            InitActModel model = InitActModelDao.query();
            if (model != null) {
                String key = ctl + "_" + act;
                List<Api_linkModel> listApi = model.getApi_link();
                if (listApi != null) {
                    for (Api_linkModel api : listApi) {
                        if (key.equals(api.getCtl_act())) {
                            String url = api.getApi();
                            if (!TextUtils.isEmpty(url)) {
                                return url;
                            }
                        }
                    }
                }
            }
        }
        return ApkConstant.SERVER_URL_API;
    }

    public static String getJavaApiUrl(String ctl, String act) {
        if (!TextUtils.isEmpty(ctl) && !TextUtils.isEmpty(act)) {
            //            InitActModel model = InitActModelDao.query();
            //            if (model != null) {
            //                String key = ctl + "_" + act;
            //                List<Api_linkModel> listApi = model.getApi_link();
            //                if (listApi != null) {
            //                    for (Api_linkModel api : listApi) {
            //                        if (key.equals(api.getCtl_act())) {
            //                            String url = api.getApi();
            //                            if (!TextUtils.isEmpty(url)) {
            //                                return url;
            //                            }
            //                        }
            //                    }
            //                }

            //            if ("live".equals(ctl) && "add_live".equals(act)) {
            //                return "http://192.168.1.173:8081/live-web" + "/" + ctl + "/" + act;
            //            }
            return ApkConstant.SERVER_URL_API + "/" + ctl + "/" + act;
            //            }
        }
        return ApkConstant.SERVER_URL_API;
    }

    public static String getLiveRoleCreater() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_live();
        } else {
            //            return AVRoomManager.ROLE_DEFAULT;

            // 七牛临时
            return "";
        }
    }

    public static String getLiveRoleHighCreater() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_high_live();
        } else {
            //            return AVRoomManager.ROLE_DEFAULT;

            // 七牛临时
            return "";
        }
    }

    public static String getLiveRoleLowCreater() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_low_live();
        } else {
            //            return AVRoomManager.ROLE_DEFAULT;

            // 七牛临时
            return "";
        }
    }

    public static String getLiveRoleViewer() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_normal();
        } else {
            //            return AVRoomManager.ROLE_DEFAULT;

            // 七牛临时
            return "";
        }
    }

    public static String getLiveRoleVideoViewer() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            return model.getSpear_interact();
        } else {
            //            return AVRoomManager.ROLE_DEFAULT;
            // 七牛临时
            return "";
        }
    }

    public static void setUsersig(String usersig) {
        if (usersig == null) {
            usersig = "";
        }
        SDConfig.getInstance().setString(R.string.config_live_usersig, usersig);
    }

    public static String getUsersig() {
        return SDConfig.getInstance().getString(R.string.config_live_usersig, null);
    }

    public static boolean startContext() {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return false;
        }
        String identifier = user.getUserId();
        //        String identifier = user.getMalaId();
        if (TextUtils.isEmpty(identifier)) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.user_id_empty));
            return false;
        }

        String usersig = getUsersig();
        if (TextUtils.isEmpty(usersig)) {
            CommonInterface.requestUsersig(null);
            return false;
        }
        // avsdk 初始化
        //        QavsdkControl.getInstance().getAVContextControl().startContext(LiveConstant.APP_ID_TENCENT_LIVE, LiveConstant.ACCOUNT_TYPE_TENCENT_LIVE, identifier, usersig);

        // 七牛sdk初始化
        QNSdkControl.getInstance().getQNContextControl().startContext(LiveConstant.APP_ID_TENCENT_LIVE, LiveConstant.ACCOUNT_TYPE_TENCENT_LIVE, identifier, usersig);
        return true;
    }

    public static boolean hasRecommendRoom() {
        InitActModel initModel = InitActModelDao.query();
        if (initModel != null && initModel.getRecommend_room() != null) {
            return true;
        }
        return false;
    }

    /**
     * 登出im
     *
     * @param logout
     */
    public static void logoutIm(boolean logout) {
        //        QavsdkControl.getInstance().getAVContextControl().logoutIm(logout);
        QNSdkControl.getInstance().getTimControl().logoutIm();
    }

    public static void stopContext() {
        //        QavsdkControl.getInstance().getAVContextControl().stopContext();
        QNSdkControl.getInstance().getQNContextControl().stopContext();
    }

    public static boolean isContextStarted() {
        //        return QavsdkControl.getInstance().getAVContextControl().isContextStarted();
        return QNSdkControl.getInstance().getQNContextControl().isContextStarted();
    }

    /**
     * 打开主播界面
     *
     * @param activity
     */
    public static void openLiveCreaterActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, com.fanwe.live.activity.room.LiveCreaterActivity.class);
            activity.startActivity(intent);
        }
    }

    /**
     * 启动回放界面
     *
     * @param data
     * @param activity
     */
    public static void startPlayback(final PlayBackData data, final Activity activity) {
        if (activity != null && data != null) {
            new LiveNetChecker().check(activity, new LiveNetChecker.CheckResultListener() {
                @Override
                public void onAccepted() {
                    startPlaybackInside(data, activity);
                }

                @Override
                public void onRejected() {
                }
            });
        }
    }

    public static void joinPullLive(final JoinLiveData data, final Activity activity, final boolean isRecommendRoom) {
        if (activity != null && data != null) {
            new LiveNetChecker().check(activity, new LiveNetChecker.CheckResultListener() {
                @Override
                public void onAccepted() {
                    joinPullLiveInside(data, activity);
                }

                @Override
                public void onRejected() {
                    if (isRecommendRoom) {
                        Intent intent = new Intent(activity, LiveMainActivity.class);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }

    private static void startPlaybackInside(PlayBackData data, Activity activity) {
        Intent intent = new Intent(activity, LivePlaybackActivity.class);
        //        Intent intent = new Intent(activity, PLVideoViewActivity.class);
        intent.putExtra(LivePlaybackActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LivePlaybackActivity.EXTRA_REVIEW_ID, data.getReviewId());
        intent.putExtra(LivePlaybackActivity.EXTRA_PLAYBACK_URL, data.getPlaybackUrl());
        activity.startActivity(intent);
    }

    private static void joinPullLiveInside(JoinLiveData data, Activity activity) {
        Intent intent = new Intent(activity, com.fanwe.live.activity.room.LivePullViewerActivity.class);
        intent.putExtra(com.fanwe.live.activity.room.LivePlaybackActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(com.fanwe.live.activity.room.LiveViewerActivity.EXTRA_LOADING_VIDEO_IMAGE_URL, data.getLoadingVideoImageUrl());
        activity.startActivity(intent);
    }

    /**
     * 创建直播间
     */
    public static void createLive(final CreateLiveData data, final Activity activity) {
        createLive(data, activity, true);
    }

    /**
     * 创建直播间
     */
    public static void createLive(final CreateLiveData data, final Activity activity, final boolean isFinish) {
        if (!isLogin(activity)) {
            return;
        }
        if (activity == null && data == null) {
            return;
        }

        new LiveNetChecker().check(activity, new LiveNetChecker.CheckResultListener() {
            @Override
            public void onAccepted() {
                if (data.getVideoType() == 0) {
                    createLiveInside(data, activity, isFinish);
                } else if (data.getVideoType() == 1) {
                    createPushLiveInside(data, activity);
                }
            }

            @Override
            public void onRejected() {
            }
        });
    }

    /**
     * 创建互动直播间
     */
    private static void createLiveInside(CreateLiveData data, Activity activity, boolean isFinish) {
        if (!isContextStarted()) {
            startContext();
            return;
        }

        ////        avsdk开播逻辑
        //        Intent intent = new Intent(activity, LiveCreaterActivity.class);
        //        intent.putExtra(LiveCreaterActivity.EXTRA_ROOM_ID, data.getRoomId());
        //        intent.putExtra(LiveCreaterActivity.EXTRA_IS_CLOSED_BACK, data.getIsClosedBack());
        //        activity.startActivity(intent);

        //        七牛sdk开播逻辑
        Intent intent = new Intent(activity, data.getIsHorizontal() == 1 ?
                LiveLandscapeCreatorActivity.class : LiveCreaterActivity.class);
        intent.putExtra(LiveCreaterActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LiveCreaterActivity.EXTRA_IS_CLOSED_BACK, data.getIsClosedBack());
        intent.putExtra(LiveActivity.EXTRA_RTC_ROLE, data.getRtcrole());
        intent.putExtra(LiveCreaterActivity.EXTRA_PUBLISHADDR, data.getPush_url());
        // 房间信息
        intent.putExtra(LiveCreaterActivity.EXTRA_ROOM_INFO, data.getVideoActModel());
        intent.putExtra(LiveCreaterActivity.EXTRA_ORIENTATION, data.getIsHorizontal() == 1);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    /**
     * 创建推流直播间
     */
    private static void createPushLiveInside(CreateLiveData data, Activity activity) {
        Intent intent = new Intent(activity, LivePushCreaterActivity.class);
        intent.putExtra(LivePushCreaterActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LivePushCreaterActivity.EXTRA_IS_CLOSED_BACK, data.getIsClosedBack());
        activity.startActivity(intent);
        activity.finish();
    }

    public static void joinRecommendRoom(Activity activity) {
        InitActModel initModel = InitActModelDao.query();
        JoinLiveData data = new JoinLiveData();
        data.setCreaterId(initModel.getRecommend_room().getUserId());
        data.setGroupId(initModel.getRecommend_room().getGroupId());
        data.setLoadingVideoImageUrl(initModel.getRecommend_room().getHeadImage());
        data.setRoomId(initModel.getRecommend_room().getRoomId());
        data.setType(0);
        data.setVideoType(initModel.getRecommend_room().getVideoType());
        if (initModel.getRecommend_room().getVideoType() == 0) {
            AppRuntimeWorker.joinLive(data, activity, true);
        } else {
            AppRuntimeWorker.joinPullLive(data, activity, true);
        }
        activity.finish();
    }

    /**
     * 加入直播间
     *
     * @param data
     * @param activity
     */
    public static void joinLive(final JoinLiveData data, final Activity activity, final boolean isRecommendRoom) {
        if (!isLogin(activity)) {
            return;
        }
        if (activity == null && data == null) {
            return;
        }

        new LiveNetChecker().check(activity, new LiveNetChecker.CheckResultListener() {
            @Override
            public void onAccepted() {
                if (data.getVideoType() == 0) {
                    joinLiveInside(data, activity);
                } else if (data.getVideoType() == 1) {
                    joinPushLiveInside(data, activity);
                }
            }

            @Override
            public void onRejected() {
                if (isRecommendRoom) {
                    Intent intent = new Intent(activity, LiveMainActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    /**
     * 加入推流直播间
     */
    private static void joinPushLiveInside(JoinLiveData data, Activity activity) {
        Intent intent = new Intent(activity, LivePushViewerActivity.class);
        intent.putExtra(LivePushViewerActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LivePushViewerActivity.EXTRA_PRIVATE_KEY, data.getPrivateKey());
        intent.putExtra(LivePushViewerActivity.EXTRA_GROUP_ID, data.getGroupId());
        intent.putExtra(LivePushViewerActivity.EXTRA_CREATER_ID, data.getCreaterId());
        intent.putExtra(LivePushViewerActivity.EXTRA_LIVE_TYPE, data.getType());
        intent.putExtra(LivePushViewerActivity.EXTRA_SEX, data.getSex());
        intent.putExtra(LivePushViewerActivity.EXTRA_CATE_ID, data.getCate_id());
        intent.putExtra(LivePushViewerActivity.EXTRA_CITY, data.getCity());
        intent.putExtra(LivePushViewerActivity.EXTRA_LOADING_VIDEO_IMAGE_URL, data.getLoadingVideoImageUrl());
        activity.startActivity(intent);
    }

    /**
     * 加入互动直播间
     */
    private static void joinLiveInside(JoinLiveData data, Activity activity) {
        if (!isContextStarted()) {
            startContext();
            return;
        }

        UserModel user = UserModelDao.query();
        if (user != null && user.getUserId().equals(data.getCreaterId())) {
            CreateLiveData createLiveData = new CreateLiveData();
            createLiveData.setRoomId(data.getRoomId());

            // 七牛临时,但是接了七牛这个数据还是必须的
            createLiveData.setPush_url(data.getPlay_url());
            createLiveData.setRtcRole(LiveActivity.RTC_ROLE_ANCHOR);
            createLiveData.setVideoType(0);
            createLiveData.setIsClosedBack(1);
            createLive(createLiveData, activity, false);
            return;
        }

        //        if (data.getIs_small_screen() == 1) {
        //            LiveFloatViewerView.tryJoinRoom(data);
        //            return;
        //        }
        Intent intent = new Intent(activity, data.getIsHorizontal() == 1 ?
                LiveVideoActivity.class : LiveViewerActivity.class);
        intent.putExtra(LiveViewerActivity.EXTRA_ROOM_ID, data.getRoomId());
        intent.putExtra(LiveViewerActivity.EXTRA_PRIVATE_KEY, data.getPrivateKey());
        intent.putExtra(LiveViewerActivity.EXTRA_GROUP_ID, data.getGroupId());
        intent.putExtra(LiveViewerActivity.EXTRA_CREATER_ID, data.getCreaterId());
        intent.putExtra(LiveViewerActivity.EXTRA_LIVE_TYPE, data.getType());
        intent.putExtra(LiveViewerActivity.EXTRA_SEX, data.getSex());
        intent.putExtra(LiveViewerActivity.EXTRA_CATE_ID, data.getCate_id());
        intent.putExtra(LiveViewerActivity.EXTRA_CITY, data.getCity());
        intent.putExtra(LiveViewerActivity.EXTRA_LOADING_VIDEO_IMAGE_URL, data.getLoadingVideoImageUrl());
        if (TextUtils.isEmpty(data.getPlay_url())) {
            Log.e("invite", "播放地址为空,不能进入播放");
            return;
        }
        intent.putExtra(LiveViewerActivity.EXTRA_VIDEOPATH, data.getPlay_url());
        //        intent.putExtra(LiveViewerActivity.EXTRA_ORIENTATION, data.getVideoPath());
        //        intent.putExtra(LiveViewerActivity.EXTRA_AUDIOONLY, data.getVideoPath());
        //        intent.putExtra(LiveViewerActivity.EXTRA_SWCODEC, data.getVideoPath());

        intent.putExtra(LiveViewerActivity.EXTRA_ORIENTATION, data.getIsHorizontal() == 1);
        activity.startActivity(intent);
    }

}
