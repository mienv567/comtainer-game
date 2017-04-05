package com.fanwe.live.common;

import android.text.TextUtils;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestCallbackWrapper;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.AppAddVideoModel;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.RedPointModel;
import com.fanwe.hybrid.model.UserInviteModel;
import com.fanwe.hybrid.model.UserSignInModel;
import com.fanwe.hybrid.model.UserSubmitSignModel;
import com.fanwe.hybrid.model.UserTaskModel;
import com.fanwe.hybrid.umeng.UmengPushManager;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.ERequestFollowSuccess;
import com.fanwe.live.model.AppRoomHotScoreActModel;
import com.fanwe.live.model.AppRoomHotScoreMonitorActModel;
import com.fanwe.live.model.AppTaskModel;
import com.fanwe.live.model.App_AuthentActModel;
import com.fanwe.live.model.App_BaseInfoActModel;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.App_ExchangeRuleActModel;
import com.fanwe.live.model.App_GainRecordActModel;
import com.fanwe.live.model.App_ProfitBindingActModel;
import com.fanwe.live.model.App_RegionListActModel;
import com.fanwe.live.model.App_aliyun_stsActModel;
import com.fanwe.live.model.App_check_lianmaiActModel;
import com.fanwe.live.model.App_del_videoActModel;
import com.fanwe.live.model.App_doExchangeActModel;
import com.fanwe.live.model.App_do_loginActModel;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.model.App_family_createActModel;
import com.fanwe.live.model.App_family_indexActModel;
import com.fanwe.live.model.App_family_listActModel;
import com.fanwe.live.model.App_family_user_confirmActModel;
import com.fanwe.live.model.App_family_user_r_user_listActModel;
import com.fanwe.live.model.App_family_user_user_listActModel;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_forbid_send_msgActModel;
import com.fanwe.live.model.App_get_review_ActModel;
import com.fanwe.live.model.App_get_tokenActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_google_play_pay_ActModel;
import com.fanwe.live.model.App_is_user_verifyActModel;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.live.model.App_my_follow_ActModel;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.App_profitActModel;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.App_red_envelopeActModel;
import com.fanwe.live.model.App_send_mobile_verifyActModel;
import com.fanwe.live.model.App_set_adminActModel;
import com.fanwe.live.model.App_shareActModel;
import com.fanwe.live.model.App_start_lianmaiActModel;
import com.fanwe.live.model.App_stop_lianmaiActModel;
import com.fanwe.live.model.App_tipoff_typeActModel;
import com.fanwe.live.model.App_uploadImageActModel;
import com.fanwe.live.model.App_userEditActModel;
import com.fanwe.live.model.App_user_adminActModel;
import com.fanwe.live.model.App_user_homeActModel;
import com.fanwe.live.model.App_user_red_envelopeActModel;
import com.fanwe.live.model.App_user_reviewActModel;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.App_usersigActModel;
import com.fanwe.live.model.App_video_cstatusActModel;
import com.fanwe.live.model.App_viewerActModel;
import com.fanwe.live.model.CategoryDetailModel;
import com.fanwe.live.model.CategoryListModel;
import com.fanwe.live.model.CategoryNameListModel;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.GoodsHistoryListModel;
import com.fanwe.live.model.GoodsListModel;
import com.fanwe.live.model.IndexSearch_areaActModel;
import com.fanwe.live.model.Index_focus_videoActModel;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.Index_topicActModel;
import com.fanwe.live.model.LiveTopicModel;
import com.fanwe.live.model.Login_test_loginActModel;
import com.fanwe.live.model.Music_downurlActModel;
import com.fanwe.live.model.Music_searchActModel;
import com.fanwe.live.model.Music_user_musicActModel;
import com.fanwe.live.model.PlayerListModel;
import com.fanwe.live.model.RankModel;
import com.fanwe.live.model.SettingsSecurityActModel;
import com.fanwe.live.model.Settings_black_listActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_friendsActModel;
import com.fanwe.live.model.User_set_blackActModel;
import com.fanwe.live.model.Video_add_commitGroupId;
import com.fanwe.live.model.Video_add_videoActModel;
import com.fanwe.live.model.Video_check_statusActModel;
import com.fanwe.live.model.Video_private_room_friendsActModel;
import com.fanwe.live.model.item.FamilyDataItem;
import com.fanwe.live.utils.AIDUtil;
import com.fanwe.live.utils.DeviceUtil;
import com.sunday.eventbus.SDEventManager;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CommonInterface {

    /**
     * 观众-获得参与的竞拍列表
     *
     * @param listener
     */
    public static void requestTestLogin(AppRequestCallback<Login_test_loginActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("test_login");
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<Login_test_loginActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    UserModel userModel = actModel.getUser_info();
                    if (userModel != null) {
                        UserModel.dealLoginSuccess(userModel, true);
                    }
                }
            }
        });
    }

    public static void requestLogout(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("logout");
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<BaseActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                LogUtil.i("logout success");
            }
        });
    }

    /**
     * 初始化接口
     *
     * @param listener
     */
    public static void requestInit(AppRequestCallback<InitActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("init");
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求当前用户的usersig
     */
    public static void requestUsersig(AppRequestCallback<App_usersigActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("userSig");
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<App_usersigActModel>(listener) {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    String usersig = actModel.getUsersig();
                    AppRuntimeWorker.setUsersig(usersig);
                    AppRuntimeWorker.startContext();
                }
            }
        });
    }

    /**
     * 获得礼物列表
     *
     * @param listener
     */
    public static void requestGift(int cateId, AppRequestCallback<App_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("prop");
        params.putAct("getProps");
        params.put("cate_id", cateId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求当前用户的信息
     *
     * @param listener
     */
    @Deprecated
    public static void requestMyUserInfo(final AppRequestCallback<App_userinfoActModel> listener) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }

        requestUserInfo(null, null, new AppRequestCallbackWrapper<App_userinfoActModel>(listener) {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    UserModel user = actModel.getUser();
                    UserModelDao.insertOrUpdate(user);
                }
            }
        });
    }


    /**
     * 请求当前用户的信息
     * 搞了个新的接口取代原来的，因为不知道这个接口可靠不可靠，因此没有全局替换掉
     * 注：2017-3-7：已经全局替换掉了
     *
     * @param listener
     */
    public static void requestMyUserInfoJava(final AppRequestCallback<UserModel> listener) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }

        requestUserInfoJava(null, null, new AppRequestCallbackWrapper<UserModel>(listener) {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.getStatus() == 1) {
                    UserModel user = actModel;
                    UserModelDao.insertOrUpdate(user);
                }
            }
        });
    }

    /**
     * 获得用户信息
     *
     * @param liveId     聊天室id
     * @param to_user_id 操作对象id
     * @param listener
     */
    @Deprecated
    public static void requestUserInfo(Integer liveId, String to_user_id, AppRequestCallback<App_userinfoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("userInfo");
        params.put("liveId", liveId);
        params.put("toUserId", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获得用户信息
     *
     * @param liveId     房间id
     * @param to_user_id 操作对象id
     * @param listener
     */
    public static void requestUserInfoJava(Integer liveId, String to_user_id, AppRequestCallback<UserModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("userInfo");
        params.put("liveId", liveId);
        params.put("toUserId", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 弹幕
     *
     * @param roomId  直播间id
     * @param msg     消息内容
     * @param groupId
     * @return
     */
    public static AppRequestParams requestPopMsgParams(int roomId, String msg, String groupId) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("prop");
        params.putAct("popMSg");
        params.put("roomId", roomId);
        params.put("groupId", groupId);
        params.put("message", msg);
        return params;
    }

    /**
     * 送礼物
     *
     * @param prop_id 礼物id
     * @param num     礼物数量
     * @param is_plus 是否叠加
     * @param room_id 直播间id
     * @return
     */
    public static AppRequestParams requestSendGiftParams(int prop_id, int num, int is_plus, int room_id) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("pop_prop");
        params.put("prop_id", prop_id);
        params.put("num", num);
        params.put("is_plus", is_plus);
        params.put("room_id", room_id);
        return params;
    }

    /**
     * 获得直播间观众列表 方法已经过期，现在是使用IM在直播间里由服务器发送在线的观众列表
     *
     * @param group_id 聊天室id
     * @param page     当前分页
     * @param listener
     */
    @Deprecated
    public static void requestViewerList(String group_id, int page, int watch_number, AppRequestCallback<App_viewerActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("room");
        params.putAct("viewerList");
        params.put("watch_number", watch_number);
        params.put("groupId", group_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 禁言
     *
     * @param groupId  聊天室id
     * @param roomId   房间id
     * @param toUserId 操作对象id
     * @param second   禁言时间，单位为秒; 为0时表示取消禁言
     */
    public static void requestForbidSendMsg(String groupId, int roomId, String toUserId, int second, AppRequestCallback<App_forbid_send_msgActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("forbidSendMsg");
        params.put("groupId", groupId);
        params.put("roomId", roomId);
        params.put("toUserId", toUserId);
        params.put("second", second);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 关注/取消关注
     *
     * @param to_user_id 用户id，被拉黑或被关注的人
     * @param userModel  1 关注;2 拉黑;3 取消关注;4 删除黑名单
     * @param listener
     */
    public static void requestFollow(final String to_user_id, UserModel userModel, final AppRequestCallback<App_followActModel> listener) {
        int operate;
        if (userModel.getRelationship() == 1) {
            operate = 3;
        } else {
            operate = 1;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("relationship");
        params.putAct("edit");
        params.put("otherId", to_user_id);
        params.put("operate", operate);
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<App_followActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    ERequestFollowSuccess event = new ERequestFollowSuccess();
                    event.userId = to_user_id;
                    event.actModel = actModel;
                    SDEventManager.post(event);
                }
            }
        });
    }

    /**
     * 关注/取消关注
     *
     * @param to_user_id 用户id，被拉黑或被关注的人
     * @param listener
     */
    public static void requestFollow(final String to_user_id, final AppRequestCallback<App_followActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("relationship");
        params.putAct("edit");
        params.put("otherId", to_user_id);
        params.put("operate", 1);
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<App_followActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    ERequestFollowSuccess event = new ERequestFollowSuccess();
                    event.userId = to_user_id;
                    event.actModel = actModel;
                    SDEventManager.post(event);
                }
            }
        });
    }

    /**
     * 直播间心跳接口
     *
     * @param roomId    直播间Id
     * @param guestId   正在连麦的人id，没人连麦传null
     * @param missionId 任务id，未进行任务传null
     * @param listener  回调监听
     */
    public static void requestMonitor(int roomId, String guestId, String missionId, AppRequestCallback<App_monitorActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("monitor");
        params.put("roomId", roomId);
        params.put("guestId", guestId);
        params.put("missionId", missionId);
        AppHttpUtil.getInstance().post(params, listener);

    }

    public static void requestEndVideo(int room_id, String video_url, AppRequestCallback<App_end_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("end_live");
        params.put("roomId", room_id);
        //        params.put("video_url", video_url);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除回放视频
     *
     * @param room_id
     * @param listener
     */
    public static void requestDeleteVideo(int room_id, AppRequestCallback<App_del_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("del_video");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);

    }

    /**
     * 获取连麦Token
     *
     * @param roomId 房间id
     */
    public static void requestToken(int roomId, AppRequestCallback<App_get_tokenActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("lianmai");
        params.putAct("lianmai");
        params.put("roomId", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求直播间信息
     *
     * @param room_id     直播间id
     * @param is_vod      0:观看直播;1:点播
     * @param type        0:热门;1:最新;2:关注 [当room_id=0时有效，随机返回一个type类型下的直播]
     * @param has_scroll  1-可以滚动
     * @param sex         性别0-不限，1-男，2-女
     * @param cate_id     话题id
     * @param city        城市
     * @param private_key 私密直播的时候的口令
     * @param reviewUrl   回播地址
     * @param listener
     */
    public static SDRequestHandler requestRoomInfo(int room_id, int is_vod, int type, int has_scroll, int sex, int cate_id, String city, String private_key, String reviewUrl, AppRequestCallback<App_get_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("get_live");
        params.put("roomId", room_id);
        params.put("is_vod", is_vod);
        params.put("type", type);
        params.put("hasScroll", has_scroll);
        params.put("sex", sex);
        params.put("cate_id", cate_id);
        params.put("city", city);
        params.put("privateKey", private_key);
        params.put("reviewUrl", reviewUrl);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取直播间
     *
     * @param roomId     房间id
     * @param hasScroll  是否滚动切换房间
     * @param privateKey 私密密钥（私密直播间需要）
     */
    public static SDRequestHandler requestRoomInfo(int roomId, int hasScroll, String privateKey, AppRequestCallback<App_get_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("get_live");
        params.put("roomId", roomId);
        params.put("hasScroll", hasScroll);
        params.put("privateKey", privateKey);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param room_id   直播间id
     * @param group_id  聊天组id
     * @param channelid 旁路直播频道id
     * @param status    1-成功，0-失败，2-主播离开， 3:主播回来
     * @param listener
     */
    public static void requestStartLive(int room_id, String group_id, String channelid, int status, AppRequestCallback<App_video_cstatusActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("video_cstatus");
        params.put("room_id", room_id);
        if (group_id != null) {
            params.put("group_id", group_id);
        }
        if (channelid != null) {
            params.put("channelid", channelid);
        }
        params.put("status", status);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param type   分享类型（WEIXIN,WEIXIN_CIRCLE,QQ,QZONE,EMAIL,SMS,SINA）
     * @param roomId 房间id
     */
    public static void requestShareComplete(String type, String roomId, AppRequestCallback<App_shareActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("share");
        params.put("type", type);
        params.put("roomId", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestCheckLianmai(int room_id, AppRequestCallback<App_check_lianmaiActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("check_lianmai");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestStartLianmai(int room_id, String to_user_id, AppRequestCallback<App_start_lianmaiActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("start_lianmai");
        params.put("room_id", room_id);
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestStopLianmai(int room_id, String to_user_id, AppRequestCallback<App_stop_lianmaiActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("stop_lianmai");
        params.put("room_id", room_id);
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户主页
     *
     * @param to_user_id 被查看的用户id
     * @param listener
     */
    public static void requestUser_home(String to_user_id, AppRequestCallback<App_user_homeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("userHome");
        params.put("toUserId", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置黑名单
     *
     * @param to_user_id 被拉黑的用户id
     * @param userModel  1 关注;2 拉黑;3 取消关注;4 删除黑名单
     * @param listener
     */
    public static void requestSet_black(String to_user_id, final UserModel userModel, AppRequestCallback<User_set_blackActModel> listener) {
        int operate;
        if (userModel.getRelationship() == 2) {
            operate = 4;
        } else {
            operate = 2;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("relationship");
        params.putAct("edit");
        params.put("operate", operate);
        params.put("otherId", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 黑名单列表
     *
     * @param listener
     */
    public static void requestBlackList(int page, AppRequestCallback<Settings_black_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("black_list");
        params.put("p", page);

        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestAccountAndSafe(AppRequestCallback<SettingsSecurityActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("security");

        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户粉丝
     *
     * @param page       当前页数
     * @param to_user_id 被查看的用户id(该ID不传则查看自己)
     * @param listener
     */
    public static void requestMy_focus(int page, String to_user_id, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("relationship");
        params.putAct("get");
        params.put("mode", 3);

        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("to_user_id", to_user_id);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 直播回看列表
     *
     * @param page       当前页数
     * @param sort       排序类型; 0:最新;1:最热 \
     * @param to_user_id (查看自己则不传) 被查看的用户id
     * @param listener
     */
    public static void requestUser_review(int page, int sort, String to_user_id, AppRequestCallback<App_user_reviewActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("getReviewList");
        params.put("sort", sort);
        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("podcastId", to_user_id);
        }
        params.put("p", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 管理员列表
     *
     * @param listener
     */
    public static void requestUser_admin(AppRequestCallback<App_user_adminActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("room");
        params.putAct("getManagers");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置/取消管理员
     *
     * @param managerId 被设置的用户id
     * @param mode      1添加管理员,0取消管理员
     */
    public static void requestSet_admin(String managerId, int mode, AppRequestCallback<App_set_adminActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("room");
        params.putAct("edit");
        params.put("managerId", managerId);
        params.put("mode", mode);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户充值界面
     *
     * @param listener
     */
    public static void requestRecharge(AppRequestCallback<App_rechargeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("pay");
        params.putAct("recharge");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置/取消管理员
     *
     * @param pay_id   支付id，不能为空
     * @param rule_id  支付项目id
     * @param money    充值金额
     * @param listener 注：money跟rule_id 2个必须有一个值
     */
    public static void requestPay(int pay_id, int rule_id, double money, AppRequestCallback<App_payActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("pay");
        params.putAct("pay");
        params.put("pay_id", pay_id);// 支付id，不能为空

        if (rule_id > 0) {
            params.put("rule_id", rule_id);// 支付项目id
        } else if (money > 0) {
            params.put("money", money);// 充值金额；
        }
        // 注：money跟rule_id 2个必须有一个值
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 抢红包
     *
     * @param user_prop_id 红包id
     * @param listener
     */
    public static void requestRed_envelope(int user_prop_id, AppRequestCallback<App_red_envelopeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("red_envelope");
        params.put("user_prop_id", user_prop_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 查看手气
     *
     * @param user_prop_id 红包id
     * @param listener
     */
    public static void requestUser_red_envelope(int user_prop_id, AppRequestCallback<App_user_red_envelopeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("user_red_envelope");
        params.put("user_prop_id", user_prop_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 举报类型列表
     *
     * @param listener
     */
    public static void requestTipoff_type(AppRequestCallback<App_tipoff_typeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("tipoff");
        params.putAct("getTipoffTypes");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 举报用户
     *
     * @param to_user_id 被举报的用户id
     * @param type       类型
     * @param listener
     */
    public static void requestTipoff(int room_id, String to_user_id, long type, String remark, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("tipoff");
        params.putAct("tipoff");
        params.put("toUserId", to_user_id);
        params.put("tipoffId", type);
        params.put("room_id", room_id);
        params.put("remark", remark);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 贡献榜
     *
     * @param room_id  如果有值，则取：本场直播贡献榜排行
     * @param user_id  取某个用户的：总贡献榜排行
     * @param p        取第几页数据;从1开始， 不传或传0;则取前50位排行
     * @param listener
     */
    public static void requestCont(int room_id, String user_id, int p, AppRequestCallback<App_ContActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("contributionList");
        if (room_id > 0) {
            params.put("room_id", room_id);
        } else if (!TextUtils.isEmpty(user_id)) {
            params.put("toUserId", user_id);
        }
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 音乐搜索
     *
     * @param page     第几页
     * @param keyword
     * @param listener
     */
    public static void requestMusic_search(int page, String keyword, AppRequestCallback<Music_searchActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("search");
        params.put("p", page);
        params.put("keyword", keyword);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户音乐列表
     *
     * @param page     第几页
     * @param listener
     */
    public static void requestMusic_user_music(int page, AppRequestCallback<Music_user_musicActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("user_music");
        params.put("p", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 添加音乐
     *
     * @param audio_link  音乐下载地址
     * @param lrc_link    歌词下载地址
     * @param audio_name  歌曲名
     * @param artist_name 演唱者
     * @param listener
     */
    public static void requestMusic_add_music(int audio_id, String audio_name, String audio_link, String lrc_link, String lrc_content, String artist_name, long time_len,
                                              AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("add_music");
        params.put("audio_id", audio_id);
        params.put("audio_name", audio_name);
        params.put("audio_link", audio_link);
        params.put("lrc_link", lrc_link);
        params.put("lrc_content", lrc_content);
        params.put("artist_name", artist_name);
        params.put("time_len", time_len);
        params.setNeedShowActInfo(false);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除音乐
     *
     * @param audio_id 音乐ID
     * @param listener
     */
    public static void requestMusic_del_music(int audio_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("del_music");
        params.put("audio_id", audio_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 音乐下载地址
     *
     * @param audio_id 音乐ID
     * @param listener
     */
    public static void requestMusic_downurl(int audio_id, AppRequestCallback<Music_downurlActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("downurl");
        params.put("audio_id", audio_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 友盟推送id提交
     *
     * @param listener
     */
    public static void requestUser_apns(AppRequestCallback<BaseActModel> listener) {
        String regId = UmengPushManager.getPushAgent().getRegistrationId();
        if (!TextUtils.isEmpty(regId)) {
            LogUtil.i("regId:" + regId);
            AppRequestParams params = new AppRequestParams();
            params.putCtl("user");
            params.putAct("apns");
            params.put("apns_code", regId);
            AppHttpUtil.getInstance().post(params, listener);
        }
    }

    /**
     * 用户的关注列表
     *
     * @param page       当前页数
     * @param to_user_id 被查看的用户id(该ID不传则查看自己)
     * @param listener
     */
    public static void requestUser_follow(int page, String to_user_id, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_follow");
        params.put("p", page);

        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("to_user_id", to_user_id);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求房间号
     *
     * @param title
     * @param cate_id
     * @param city
     * @param province
     * @param share_type
     * @param location_switch
     * @param category_id
     * @param is_private
     * @param listener
     */
    public static void requestAddVideo(String title, int cate_id, String city, String province, String share_type, int location_switch, int is_private, String category_id, int isHorizontal, AppRequestCallback<Video_add_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //        params.putCtl("video");
        //        params.putCtl("qiniu_video");
        params.putCtl("live");
        params.putAct("add_live");
        params.put("title", title);
        //        params.put("cateId", cate_id);
        //        params.put("city", city);
        //        params.put("province", province);
        params.put("shareType", share_type);
        params.put("locationSwitch", location_switch);
        //        params.put("isPrivate", is_private);
        //        category_id = "2";
        //        params.put("categoryId", category_id);
        //TODO 临时加的topicId
        //        params.put("topicId", category_id);
        params.put("isHorizontal", isHorizontal);


        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 请求房间号
     *
     * @param title
     * @param cate_id
     * @param city
     * @param province
     * @param share_type
     * @param location_switch
     * @param is_private
     * @param listener
     */
    public static void requestAddVideo(String title, int cate_id, String city, String province, String share_type, int location_switch, int is_private, AppRequestCallback<AppAddVideoModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //        params.putCtl("video");
        //        params.putCtl("qiniu_video");
        params.putCtl("live");
        params.putAct("add_live");
        params.put("title", title);
        params.put("cateId", cate_id);
        params.put("city", city);
        params.put("province", province);
        params.put("shareType", share_type);
        params.put("locationSwitch", location_switch);
        params.put("isPrivate", is_private);
        String category_id = "10086";
        params.put("categoryId", category_id);
        //TODO 临时加的topicId
        params.put("topicId", category_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void setGroupId(String group_id, AppRequestCallback<Video_add_commitGroupId> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("qiniu_video");
        params.putAct("set_groupid");
        params.put("group_id", group_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 搜索用户
     *
     * @param page     当前页数
     * @param listener
     */
    public static SDRequestHandler requestSearchUser(int page, String keyword, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("search");
        params.put("p", page);
        params.put("keyword", keyword);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除视频
     *
     * @param room_id  房间id
     * @param listener
     */
    public static void requestDelVideo(int room_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("del_video_history");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 检查直播间状态
     *
     * @param room_id     直播间id
     * @param private_key 私密直播的口令
     * @param listener
     */
    public static void requestCheckVideoStatus(int room_id, String private_key, AppRequestCallback<Video_check_statusActModel> listener) {
        if (room_id == 0 && TextUtils.isEmpty(private_key)) {
            return;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("check_status");
        if (room_id != 0) {
            params.put("room_id", room_id);
        }
        if (private_key != null) {
            params.put("private_key", private_key);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 我关注的所有用户
     *
     * @param listener
     */
    public static void requestMyFollow(AppRequestCallback<App_my_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("my_follow");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求好友列表
     *
     * @param room_id  直播间id
     * @param p        分页
     * @param listener
     */
    public static void requestFriends(int room_id, int p, AppRequestCallback<User_friendsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("friends");
        params.put("room_id", room_id);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密房间用户列表
     *
     * @param room_id  直播间id
     * @param p        分页
     * @param listener
     */
    public static void requestPrivateRoomFriends(int room_id, int p, AppRequestCallback<Video_private_room_friendsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_room_friends");
        params.put("room_id", room_id);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密直播加人
     *
     * @param room_id  直播间id
     * @param user_ids
     * @param listener
     */
    public static void requestPrivatePushUser(int room_id, String user_ids, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_push_user");
        params.put("room_id", room_id);
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密直播踢人
     *
     * @param room_id  直播间id
     * @param user_ids
     * @param listener
     */
    public static void requestPrivateDropUser(int room_id, String user_ids, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_drop_user");
        params.put("room_id", room_id);
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 最新直播列表
     *
     * @param listener
     */
    public static void requestNewVideo(int p, AppRequestCallback<Index_new_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //        params.putCtl("index");
        params.putCtl("qiniu_index");
        params.putAct("new_video");
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 关注直播列表
     *
     * @param listener
     */
    public static void requestFocusVideo(AppRequestCallback<Index_focus_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //        params.putCtl("index");
        params.putCtl("qiniu_index");
        params.putAct("focus_video");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 首页接口
     *
     * @param p        分页
     * @param sex      性别 0:全部, 1-男，2-女
     * @param cate_id  话题id
     * @param city     城市(空为:热门)
     * @param listener
     */
    public static void requestIndex(int p, int sex, int cate_id, String city, AppRequestCallback<Index_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("qiniu_index");
        //        params.putCtl("index");
        params.putAct("index");
        params.put("p", p);
        params.put("sex", sex);
        params.put("cate_id", cate_id);
        if (!TextUtils.isEmpty(city)) {
            params.put("city", city);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 热门根据性别，区域搜索直播列表
     *
     * @param sex
     * @param listener
     */
    public static void requestIndexSearch_area(int sex, AppRequestCallback<IndexSearch_areaActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("search_area");
        params.put("sex", sex);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 话题列表
     *
     * @param title    关键字
     * @param p        分页
     * @param listener
     */
    public static SDRequestHandler requestToptic(String title, int p, AppRequestCallback<Index_topicActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("search_video_cate");
        if (title != null) {
            params.put("title", title);
        }
        params.put("p", p);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 个人头像上传
     *
     * @param listener
     */
    public static void requestUploadAvatar(File file, AppRequestCallback<UserModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("updateUser");
        params.putFile("headImageFile", file);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 发送验证码
     *
     * @param mobile     手机
     * @param image_code 图片验证码
     * @param type       是否是绑定手机发送的验证码
     * @param listener
     */
    public static void requestSendMobileVerify(int type, String mobile, String image_code, AppRequestCallback<App_send_mobile_verifyActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //        String url = params.getUrl() + "/login/send_mobile_verify";
        //        params.setUrl(url);
        params.putCtl("login");
        params.putAct("send_mobile_verify");
        params.put("mobile", mobile);
        if (!TextUtils.isEmpty(image_code)) {
            params.put("image_code", image_code);
        }
        if (type == 1) {
            params.put("wx_binding", type);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /***
     * 手机绑定
     *
     * @param mobile
     * @param verify_code
     * @param listener
     */
    public static void requestMobileBind(String mobile, String verify_code, AppRequestCallback<App_ProfitBindingActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("mobile_binding");
        params.put("mobile", mobile);
        params.put("verify_code", verify_code);

        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 是否使用验证
     *
     * @param listener
     */
    public static void requestIsUserVerify(AppRequestCallback<App_is_user_verifyActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("is_user_verify");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播同意协议
     *
     * @param listener
     */
    public static void requestAgree(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("agreeRules");
        params.put("isAgree", 1);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 登录-手机
     *
     * @param listener
     */
    public static void requestDoLogin(String mobile, String verify_coder, AppRequestCallback<App_do_loginActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("do_login");
        params.put("mobile", mobile);
        params.put("verifyCode", verify_coder);
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 登录-补充信息
     *
     * @param listener
     */
    public static void requestDoUpdate(String user_id, String nick_name, String sex, File file, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("do_update");
        //        params.putCtl("userCenter");
        //        params.putAct("updateUser");
        params.put("id", user_id);
        params.put("nick_name", nick_name);
        params.put("sex", sex);
        if (file != null) {
            params.putFile("file", file);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 微信登录
     *
     * @param listener
     */
    public static void requestWxLogin(String code, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("wx_login");
        params.put("code", code);
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * QQ登录
     *
     * @param listener
     */
    public static void requestQqLogin(String openid, String access_token, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("qq_login");
        params.put("openId", openid);
        params.put("accessToken", access_token);
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 新浪登录
     *
     * @param listener
     */
    public static void requestSinaLogin(String access_token, String uid, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("sina_login");
        params.put("access_token", access_token);
        params.put("sina_id", uid);
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestNaverLogin(String accessToken, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("thPlatLogin/6");
        params.put("accessToken", accessToken);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestKakaoLogin(String accessToken, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("thPlatLogin/4");
        params.put("accessToken", accessToken);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestVkLogin(String openId, String accessToken, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("thPlatLogin/10");
        params.put("openId", openId);
        params.put("accessToken", accessToken);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 编辑-用户信息（生日、情感状态、家乡、职业）
     *
     * @param listener
     */
    public static void requestUserEditInfo(AppRequestCallback<App_userEditActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("user_edit");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 更新用户信息
     *
     * @param map
     * @param listener
     */
    public static void requestCommitUserInfo(Map<String, String> map, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("userCenter");
        params.putAct("updateUserForAndroid");

        for (String key : map.keySet()) {
            if (!TextUtils.isEmpty(key)) {
                params.put(key, map.get(key));
            }
        }

        //        params.putFile("file",null);

        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 编辑-获取地区
     */
    public static void requestRegionList(AppRequestCallback<App_RegionListActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("region_list");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置推送
     *
     * @param is_remind 接收推送消息 0-不接收，1-接收
     * @param listener
     */
    public static void requestSet_push(final int is_remind, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("set_push");
        params.setNeedShowActInfo(false);
        params.put("type", 1);
        params.put("is_remind", is_remind);
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<BaseActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    UserModel user = UserModelDao.query();
                    user.setIsRemind(is_remind);
                    UserModelDao.insertOrUpdate(user);
                }
            }
        });
    }

    /**
     * 会员中心-收益页面
     *
     * @param listener
     */
    public static void requestProfit(AppRequestCallback<App_profitActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("profit");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-兑换
     *
     * @param listener
     */
    public static void requestExchangeRule(AppRequestCallback<App_ExchangeRuleActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("exchange");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-领取记录
     *
     * @param listener
     */
    public static void requestGainRecord(AppRequestCallback<App_GainRecordActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("extract_record");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-兑换
     *
     * @param rule_id  兑换规则id
     * @param ticket   兑现的钱票
     * @param listener
     */
    public static void requestDoExchange(int rule_id, int ticket, AppRequestCallback<App_doExchangeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("do_exchange");
        params.put("rule_id", rule_id);
        params.put("ticket", ticket);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-绑定微信
     *
     * @param code     微信授权回调字段
     * @param listener
     */
    public static void requestBindingWz(String code, AppRequestCallback<App_ProfitBindingActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("update_wxopenid");
        params.put("code", code);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 发送礼物给某人
     *
     * @param prop_id    礼物id
     * @param num        数量
     * @param to_user_id 对方id
     * @param listener
     */
    public static void requestSendGiftPrivate(int prop_id, int num, String to_user_id, AppRequestCallback<Deal_send_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("prop");
        params.putAct("sendProps");
        params.put("propId", prop_id);
        params.put("num", num);
        params.put("toUserId", to_user_id);
        // TODO 多加的参数，必传
        params.put("message", to_user_id);
        params.put("groupId", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 发送礼物给某人JAVA接口
     *
     * @param prop_id    礼物id
     * @param num        数量
     * @param to_user_id 对方id
     * @param listener
     */
    public static void requestSendGiftPrivateJava(int prop_id, int num, String to_user_id, int roomId, String groupId, AppRequestCallback<Deal_send_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("prop");
        params.putAct("sendProps");
        params.put("propId", prop_id);
        params.put("num", num);
        params.put("toUserId", to_user_id);
        // TODO 多加的参数，必传
        params.put("groupId", groupId);
        params.put("roomId", roomId);
        params.put("message", "不是说message可以不传吗！");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交认证
     *
     * @param authentication_type     认证类型
     * @param authentication_name     认证名称
     * @param mobile                  联系方式
     * @param identify_number         身份证号码
     * @param identify_hold_image     手持身份证正面
     * @param identify_positive_image 身份证正面
     * @param identify_nagative_image 身份证反面
     * @param listener
     */
    public static void requestAttestation(String authentication_type, String authentication_name, String mobile, String identify_number, String identify_hold_image, String identify_positive_image, String identify_nagative_image, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("attestation");
        params.put("authentication_type", authentication_type);
        params.put("authentication_name", authentication_name);
        params.put("contact", mobile);
        params.put("identify_number", identify_number);
        params.put("identify_hold_image", identify_hold_image);
        params.put("identify_positive_image", identify_positive_image);
        params.put("identify_nagative_image", identify_nagative_image);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 认证初始化
     *
     * @param listener
     */
    public static void requestAuthent(AppRequestCallback<App_AuthentActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("authent");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 认证图片上传
     *
     * @param file     图片
     * @param listener
     */
    public static void requestUploadImage(File file, AppRequestCallback<App_uploadImageActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("avatar");
        params.putAct("uploadImage");
        params.putFile("file", file);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 50、获得用户基本信息
     *
     * @param user_ids
     * @param listener
     */
    public static void requestBaseInfo(String user_ids, AppRequestCallback<App_BaseInfoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("baseinfo");
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 通过话题标题获得话题信息
     *
     * @param title
     * @param listener
     */
    public static void requestTopicDesc(String title, AppRequestCallback<LiveTopicModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("exact_search_video_cate");
        params.put("video_cate_name", title);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 观众进入直播间第一次点亮请求
     *
     * @param room_id
     * @param listener
     */
    public static void requestLike(int room_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("like");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 更新用户状态为在线
     *
     * @param listener
     */
    public static void requestStateChangeLogin(AppRequestCallback<BaseActModel> listener) {
        requestStateChange("Login", listener);
    }

    /**
     * 更新用户状态为离开
     *
     * @param listener
     */
    public static void requestStateChangeLogout(AppRequestCallback<BaseActModel> listener) {
        requestStateChange("Logout", listener);
    }

    /**
     * 请求变更状态接口（用来统计用户在线时长）
     *
     * @param action
     * @param listener
     */
    private static void requestStateChange(String action, AppRequestCallback<BaseActModel> listener) {
        if (AppRuntimeWorker.isLogin(null)) {
            AppRequestParams params = new AppRequestParams();
            params.putCtl("user");
            params.putAct("state_change");
            params.put("action", action);
            AppHttpUtil.getInstance().post(params, listener);
        }
    }

    /**
     * 家族主页
     *
     * @param family_id 家族ID
     * @param listener
     */
    public static void requestFamilyIndex(int family_id, AppRequestCallback<App_family_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("index");
        params.put("family_id", family_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 创建家族接口
     *
     * @param family_logo      家族LOGO
     * @param family_name      家族名称
     * @param family_manifesto 家族宣言
     * @param family_notice    家族公告
     * @param listener
     */
    public static void requestFamilyCreate(String family_logo, String family_name, String family_manifesto, String family_notice, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("create");
        params.put("family_logo", family_logo);
        params.put("family_name", family_name);
        params.put("family_manifesto", family_manifesto);
        params.put("family_notice", family_notice);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 修改家族信息接口
     *
     * @param family_id        家族ID
     * @param family_logo      家族LOGO
     * @param family_manifesto 家族宣言
     * @param family_notice    家族公告
     * @param listener
     */
    public static void requestFamilyUpdate(int family_id, String family_logo, String family_manifesto, String family_notice, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("save");
        params.put("family_id", family_id);
        params.put("family_logo", family_logo);
        params.put("family_manifesto", family_manifesto);
        params.put("family_notice", family_notice);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员列表接口
     *
     * @param family_id
     * @param page
     * @param listener
     */
    public static void requestFamilyMembersList(int family_id, int page, AppRequestCallback<App_family_user_user_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_list");
        params.put("family_id", family_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员申请列表接口
     *
     * @param family_id
     * @param page
     * @param listener
     */
    public static void requestFamilyMembersApplyList(int family_id, int page, AppRequestCallback<App_family_user_r_user_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("r_user_list");
        params.put("family_id", family_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入家族列表
     *
     * @param family_id
     * @param family_name
     * @param page
     * @param listener
     */
    public static SDRequestHandler requestApplyJoinFamilyList(int family_id, String family_name, int page, AppRequestCallback<App_family_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("family_list");
        params.put("family_id", family_id);
        params.put("family_name", family_name);
        params.put("page", page);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入家族接口
     *
     * @param family_id
     * @param listener
     */
    public static void requestApplyJoinFamily(int family_id, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_join");
        params.put("family_id", family_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员移除接口（家族创始人）
     *
     * @param r_user_id
     * @param listener
     */
    public static void requestDelFamilyMember(int r_user_id, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_del");
        params.put("r_user_id", r_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 成员申请审核(家族长权限)
     *
     * @param r_user_id 要审核的成员ID
     * @param is_agree  是否同意 （1：同意，2：拒绝）
     * @param listener
     */
    public static void requestFamilyMemberConfirm(int r_user_id, int is_agree, AppRequestCallback<App_family_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("confirm");
        params.put("r_user_id", r_user_id);
        params.put("is_agree", is_agree);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * OSS上传图片获取参数
     *
     * @param listener
     */
    public static void requestAliyunSts(AppRequestCallback<App_aliyun_stsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("aliyun_sts");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 登陆统计
     *
     * @param listener
     */
    public static void requestLoginStatistic(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("statistics");
        params.putAct("login");
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 激活统计
     *
     * @param listener
     */
    public static void requestActiveStatistic(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("statistics");
        params.putAct("activity");
        params.put("aId", AIDUtil.getAID(App.getApplication()));
        params.put("sn", DeviceUtil.getDeviceUuidIncludeEmulator(App.getApplication()));
        params.put("osVersion", DeviceUtil.getOsVersion());
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 点亮接口
     *
     * @param group_id
     * @param room_id
     * @param type     1：普通点亮 2：点亮99次
     * @param listener
     */
    public static void requestLight(String group_id, int room_id, int type, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("light");
        params.put("group_id", group_id);
        params.put("video_id", room_id);
        params.put("type", type);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获得房间热度信息
     *
     * @param roomId
     * @param listener
     */
    public static void requestHotScore(int roomId, AppRequestCallback<AppRoomHotScoreActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("get_video_progress_bar");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 定时获取热度
     *
     * @param roomId
     * @param listener
     */
    public static void requestHotScoreMonitor(int roomId, AppRequestCallback<AppRoomHotScoreMonitorActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("get_video_score");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取用户邀请信息
     *
     * @param listener
     */
    public static void requestUserInvite(AppRequestCallback<UserInviteModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_invite");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交邀请码
     *
     * @param inviteId
     * @param listener
     */
    public static void submitUserInvite(String inviteId, AppRequestCallback<UserInviteModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_bind_invite");
        params.put("invite_id", inviteId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取用户签到信息
     *
     * @param listener
     */
    public static void requestUserSignData(AppRequestCallback<UserSignInModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("get_sign_data");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交签到
     *
     * @param listener
     */
    public static void submitUserSign(AppRequestCallback<UserSubmitSignModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("sign");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获得红点 - 目前只能判断是否签到
     *
     * @param listener
     */
    public static void requestRedPoint(AppRequestCallback<RedPointModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("red_point");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 批量关注
     *
     * @param userIds
     * @param listener
     */
    public static void requestMutilFollow(String userIds, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("muti_follow");
        params.put("user_ids", userIds);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 修改推送状态
     *
     * @param listener
     */
    public static void requestChangePush(String userId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("unpush");
        params.put("unpush_user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求任务列表
     *
     * @param isCompleted //0-没完成的任务列表 1-已经完成的任务列表（只有主线任务）
     * @param listener
     */
    public static void requestMyTask(int isCompleted, AppRequestCallback<UserTaskModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("get_tasks");
        params.put("is_completed", isCompleted);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 领取任务得到的奖励
     *
     * @param taskId
     * @param listener
     */
    public static void requestGetTaskReward(String taskId, int taskType, AppRequestCallback<UserSubmitSignModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("get_task_reward");
        params.put("task_id", taskId);
        params.put("task_type", taskType);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播发起狼人杀游戏
     *
     * @param roomId
     */
    public static void requestLRSPrepare(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("wolf_start");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入游戏
     *
     * @param roomId
     * @param listener
     */
    public static void requestLRSSign(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("join_game");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播开始狼人杀游戏
     *
     * @param roomId
     * @param listener
     */
    public static void requestLRSEnter(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("begin_game");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 狼人杀人接口
     *
     * @param roomId
     * @param userId
     * @param listener
     */
    public static void requestLRSWolfKill(int roomId, String userId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("wolf_kill");
        params.put("video_id", roomId);
        params.put("kill_user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 女巫毒或者救人
     *
     * @param roomId
     * @param type     1：毒人；2：救人
     * @param userId   被毒或者被救的人的id
     * @param listener
     */
    public static void requestLRSWitch(int roomId, int type, String userId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("witch");
        params.put("video_id", roomId);
        params.put("type", type);
        params.put("user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 女巫活动结束接口
     *
     * @param roomId
     */
    public static void requestLRSWItchFinish(int roomId) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("finish_witch");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, null);
    }

    /**
     * 猎人带人接口
     *
     * @param roomId
     * @param type     1:不杀人; 2:杀人
     * @param userId
     * @param listener
     */
    public static void requestLRSHunter(int roomId, int type, String userId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("hunter");
        params.put("video_id", roomId);
        params.put("type", type);
        params.put("kill_user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 用户跳过发言
     *
     * @param roomId
     * @param listener
     */
    public static void requestLRSPassSpeak(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("pass");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户投票
     *
     * @param roomId
     * @param userId
     * @param listener
     */
    public static void requestLRSVote(int roomId, String userId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("vote");
        params.put("video_id", roomId);
        params.put("kill_user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 退出游戏
     *
     * @param roomId
     * @param listener
     */
    public static void requestLRSOut(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("out");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 在游戏开始之前退出游戏 - 参与玩家 非主播
     *
     * @param roomId
     * @param listener
     */
    public static void requestLRSOutBeforeStartGame(int roomId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("leave");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 在游戏开始之前关闭游戏 - 主播
     *
     * @param roomId
     */
    public static void requestLRSFinish(int roomId) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("finish");
        params.put("video_id", roomId);
        AppHttpUtil.getInstance().post(params, null);
    }

    /**
     * 分类名称
     *
     * @param listener
     */
    public static void requestCategoryNameList(AppRequestCallback<CategoryNameListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("category_name_list");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 直播分类列表
     *
     * @param listener
     */
    public static void requestCategoryList(AppRequestCallback<CategoryListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("category_list");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取分类详情
     *
     * @param categoryId
     * @param listener
     */
    public static void requestCategory(String categoryId, AppRequestCallback<CategoryDetailModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("liveList");
        params.put("categoryId", categoryId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 进入APP提醒签到接口
     *
     * @param listener
     */
    public static void requestAppSign(AppRequestCallback<UserSubmitSignModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("app_sign");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求回放接口
     *
     * @param mReviewId
     * @param listener
     * @return
     */
    public static SDRequestHandler requestReview(String mReviewId, AppRequestCallback<App_get_review_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("getReview");
        params.put("reviewId", mReviewId);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 进行谷歌play的服务端公钥验证
     *
     * @param purchaseData
     * @param signature
     * @param listener
     */
    public static void requestGooglePlayPay(String purchaseData, String signature, AppRequestCallback<App_google_play_pay_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("pay");
        params.putAct("google");
        params.put("purchaseData", purchaseData);
        params.put("signature", signature);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取商品列表
     */
    public static void requestGoodsList(AppRequestCallback<GoodsListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("goods");
        params.putAct("list");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 购买商品
     *
     * @param goodsId 商品id
     */
    public static void requestGoodsBuy(int roomId, int goodsId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("goods");
        params.putAct("buy");
        params.put("roomId", roomId);
        params.put("goodsId", goodsId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取购买历史记录
     *
     * @param buyerId  买家Id
     * @param page     页数
     * @param pageSize 每页显示数据条数
     */
    public static void requestGoodsHistory(String buyerId, int page, int pageSize, AppRequestCallback<GoodsHistoryListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("goods");
        params.putAct("history");
        params.put("buyerId", buyerId);
        params.put("page", page);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取当前主播的当前任务信息
     */
    public static void requestCurrentTask(String missionId, String hosterId, AppRequestCallback<AppTaskModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("mission");
        params.putAct("getMission");
        params.put("missionId", missionId);
        params.put("hosterId", hosterId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestReceiveTask(String missionId, AppRequestCallback<String> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("mission");
        params.putAct("receiveMission");
        params.put("missionId", missionId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestThumbsUp(String toUserId, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("thumbsUp");
        params.putAct("thumbsUp");
        params.put("toUserId", toUserId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 魅力值排行榜
     */
    public static void requestCharmList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("charmList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 点赞排行榜
     */
    public static void requestLikesList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("likesList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 场外任务值排行
     */
    public static void requestOutMissionList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("outMissionList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 场内任务值排行
     */
    public static void requestInsideMissionList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("insideMissionList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 粉丝贡献排行榜
     */
    public static void requestContributionList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("controbutionList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 粉丝贡献排行榜
     */
    public static void requestMedalList(int toPage, int pageSize, AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("medalList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 直播间内-粉丝贡献排行榜
     *
     * @param userId   当前房间主播id
     * @param dateType 排行榜类型：0-日榜，1-周榜，2-月榜，3-总榜
     */
    public static void requestRoomControbutionList(int toPage, int pageSize, String userId, int dateType,
                                                   AppRequestCallback<RankModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rankingList");
        params.putAct("roomControbutionList");
        params.put("toPage", toPage);
        params.put("pageSize", pageSize);
        params.put("userId", userId);
        params.put("dateType", dateType);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播列表
     */
    public static void requestPlayerList(AppRequestCallback<PlayerListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("guide");
        params.putAct("playerList");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 箱吧 - 动态列表
     *
     * @param page     当前页 默认值1
     * @param pageSize 每页显示记录条数
     */
    public static void queryFamilyList(int page, int pageSize, AppRequestCallback<List<FamilyDataItem>> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("boxZone");
        params.putAct("queryList");
        params.put("page", page);
        params.put("pageSize", pageSize);
        AppHttpUtil.getInstance().post(params, listener);
    }
}
