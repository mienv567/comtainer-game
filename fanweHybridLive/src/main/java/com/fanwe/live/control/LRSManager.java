package com.fanwe.live.control;


import android.app.Activity;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LRSBottomDialog;
import com.fanwe.live.event.ELRSOutGame;
import com.fanwe.live.event.ELRSStopPlay;
import com.fanwe.live.model.App_check_lianmaiActModel;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgInviteVideo;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMCallBack;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LRSManager {
    public static final int GAME_STEP_READY = 0; //天黑倒计时
    public static final int GAME_STEP_WOLF_PROPHET = 1;//天黑-狼人、预言家行动
    public static final int GAME_STEP_WITCH = 2;//天黑 - 女巫确定要毒人
    public static final int GAME_STEP_DAYBREAK = 3;//天亮了
    public static final int GAME_STEP_HUNTER = 4;//天亮了 - 猎人带走人
    public static final int GAME_STEP_LAST_WORDS_ONE = 5;//天亮了 - 留遗言 选择是否连麦
    public static final int GAME_STEP_LAST_WORDS_TWO = 6;//天亮了 - 遗言连麦
    public static final int GAME_STEP_SPEAK_ONE_BY_ONE_ONE = 7; //天亮了 - 轮流发言 选择是否连麦
    public static final int GAME_STEP_SPEAK_ONE_BY_ONE_TWO = 8;//天亮了 - 连麦发言
    public static final int GAME_STEP_VOTE = 9;//天亮了 - 投票
    public static final int GAME_STEP_DEAD = 10;//天亮了 - 获得死亡信息 包括天黑死人、猎人杀人、投票被投死
    public static final int GAME_STEP_OUT = 11;//有人退出游戏了
    public static final int GAME_CONTROL_KILL = 1; //狼人杀人
    public static final int GAME_CONTROL_POISON = 2;//女巫毒
    public static final int GAME_CONTROL_CHECK = 3;//先知验人
    public static final int GAME_CONTROL_VOTE = 4;//投票操作
    public static final int GAME_CONTROL_HUNT = 5;//猎人带走人

    public static final int WITCH_POISON = 1;//女巫毒人
    public static final int WITCH_SAVE = 2;//女巫救人
    public String mGameGroupId; //游戏频道
    public String mWolfGroupId;//狼人频道
    static LRSBottomDialog mDialog;
    static Activity mActivity;
    public boolean mIsShowingEnter = false;//主播主动点击参与或者不参与按钮设置为 true
    private List<LRSUserModel> mGameUsers = new ArrayList<>();
    private int mSelfRole = -1;//当前用户的游戏角色  默认-1 0表示观众
    public boolean mIsGaming = false; //判断当前是否在狼人杀游戏中
    public boolean mIsSelfOutGame = false; //判断自己是否已经退出了游戏
    public boolean mIsDark = true;//判断是否为天黑
    public boolean mIsSelfLastWords = false;//判断是否为自己的遗言时间
    public boolean mIsJoinGame = false; //判断当前用户是否报名
    public boolean mIsCreaterOutGame = false;//判断是否是主播点击了关闭游戏 如果是 之后的参与人数不弹框显示
    private MediaPlayer mPlayer;
    private String mUserId;
    private static final LRSManager instance = new LRSManager();

    private LRSManager() {
        initUserId();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public static LRSManager getInstance() {
        return instance;
    }

    private void initUserId() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            mUserId = userModel.getUserId();
        }
    }

    public void reSet() {
        mSelfRole = -1;//重置角色
        mIsGaming = false;
        mIsSelfOutGame = false;
        mIsShowingEnter = false;
        mIsDark = true;
        mIsSelfLastWords = false;
        mIsJoinGame = false;
        mIsCreaterOutGame = false;
        mGameUsers.clear();
        mGameGroupId = "";
        mWolfGroupId = "";
    }

    /**
     * 主播开启游戏
     *
     * @param activity
     */
    public void showPrepareBottomDialog(Activity activity, int roomId) {
        dismiss();
        mDialog = getDialog(activity);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_PREPARE);
        mDialog.setRoomId(roomId);
        mDialog.showBottom();
    }


    /**
     * 报名游戏
     *
     * @param activity
     */
    public void showSignBottomDialog(Activity activity, int roomId, boolean isCreater) {
        reSet();
        dismiss();
        if (isCreater) {
            mIsShowingEnter = false;
        }
        mDialog = getDialog(activity);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_SIGN);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setIsCreater(isCreater);
        mDialog.setRoomId(roomId);
        mDialog.showBottom();
    }

    /**
     * 玩家显示报名过程
     * @param activity
     * @param roomId
     * @param num
     */
    public void showPlayerBottomDialog(Activity activity, int roomId,int num){
        boolean needShow = true;
        if (mDialog != null && mDialog.isShowing()) {
            if((mDialog.getDes() == LRSBottomDialog.SHOW_FOR_SIGN)){
                return;
            }else if((mDialog.getDes() == LRSBottomDialog.SHOW_FOR_PLAYER)){
                needShow = false;
            }
        }
        mDialog = getDialog(activity);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_PLAYER);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setRoomId(roomId);
        mDialog.setPlayerNumber(num);
        if (needShow) {
            mDialog.showBottom();
        }
    }

    /**
     * 主播确定开始游戏
     *
     * @param activity
     */
    public void showEnterBottomDialog(Activity activity, int num, int enterNum, int roomId) {
        boolean needShow = true;
        if (mDialog != null && mDialog.isShowing() && (mDialog.getDes() == LRSBottomDialog.SHOW_FOR_ENTER)) {
            needShow = false;
        }
        mDialog = getDialog(activity);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_ENTER);
        mDialog.setRoomId(roomId);
        mDialog.setEnterNumber(num, enterNum);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        if (needShow) {
            mDialog.showBottom();
        }
    }

    /**
     * 女巫救人 - 5s不操作自动关闭
     *
     * @param activity
     * @param roomId
     */
    public void showWitchBottomDialogForSave(Activity activity, int roomId, CustomMsgLRS msg) {
        dismiss();
        mDialog = getDialog(activity);
        mDialog.setKillUserId(msg.getWolf_kill_user_id());
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_WITCH_SAVE);
        mDialog.setRoomId(roomId);
        mDialog.setMsg(msg);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.showBottom();
    }

    /**
     * 女巫毒人 - 5s不操作自动关闭
     *
     * @param activity
     * @param roomId
     */
    public void showWitchButtomDialogForPoison(Activity activity, int roomId, CustomMsgLRS msg) {
        dismiss();
        mDialog = getDialog(activity);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_WITCH_POISON);
        mDialog.setRoomId(roomId);
        mDialog.setMsg(msg);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.showBottom();
    }

    /**
     * 猎人带走人提示
     *
     * @param activity
     */
    public void showHunterButtomDialog(Activity activity, CustomMsgLRS msg) {
        dismiss();
        mDialog = getDialog(activity);
        mDialog.setDes(LRSBottomDialog.SHOW_FOR_HUNTER);
        mDialog.setMsg(msg);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.showBottom();
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing() {
        boolean result = false;
        if (mDialog != null && mDialog.isShowing()) {
            result = true;
        }
        return result;
    }

    public void setGameUsers(List<LRSUserModel> list) {
        mGameUsers = list;
    }

    public List<LRSUserModel> getGameUsers() {
        return mGameUsers;
    }

    /**
     * 改变用户的生命状态
     *
     * @param userIds
     */
    public void dealDeadUsers(List<String> userIds) {
        if (userIds != null && userIds.size() > 0) {
            if(mGameUsers != null && mGameUsers.size() > 0){
                for (LRSUserModel userModel : mGameUsers) {
                    for (String userId : userIds) {
                        if (userModel.getUser_id().equals(userId)) {
                            userModel.setIs_alive(LRSUserModel.ALIVE_NOT_LIVE);
                        }
                    }
                }
            }
        }
    }

    public void dealDeadUser(String userId){
        if(mGameUsers != null && mGameUsers.size() > 0){
            for (LRSUserModel userModel : mGameUsers) {
                if (userModel.getUser_id().equals(userId)) {
                    userModel.setIs_alive(LRSUserModel.ALIVE_NOT_LIVE);
                }
            }
        }
    }


    /**
     * 获得自己的游戏角色编号
     */
    public int getSelfRole() {
        if (mSelfRole == -1) {
            mSelfRole = LRSUserModel.GAME_ROLE_NONE;
            if (mGameUsers != null && mGameUsers.size() > 0) {
                UserModel model = UserModelDao.query();
                if (model != null) {
                    for (LRSUserModel lModel : mGameUsers) {
                        if (lModel.getUser_id().equals(model.getUserId())) {
                            mSelfRole = lModel.getIdentity();
                            break;
                        }
                    }
                }
            }
        }
        return mSelfRole;
    }

    /**
     * 判断自己是否为活着的状态
     *
     * @return
     */
    public boolean isSelfALive() {
        boolean result = false;
        if (mGameUsers != null && mGameUsers.size() > 0) {
            UserModel model = UserModelDao.query();
            if (model != null) {
                for (LRSUserModel lModel : mGameUsers) {
                    if (lModel.getUser_id().equals(model.getUserId())) {
                        result = lModel.getIs_alive() == LRSUserModel.ALIVE_LIVE;
                        break;
                    }
                }
            }
        }
        return result;
    }


    /**
     * 获得用户的编号
     *
     * @param userId
     * @return
     */
    public int getUserIndex(String userId) {
        int result = -1;
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    result = lModel.getIndex();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 判断用户是否为游戏玩家
     *
     * @param userId
     * @return
     */
    public boolean userIsGamer(String userId) {
        boolean result = false;
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 清理操作显示内容  例如“杀几号”
     */
    public void cleanControlContent() {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.isShowControl()) {
                    lModel.setShowControl(false);
                    lModel.setControlContent(null);
                }
                if (lModel.isSpeaking()) {
                    lModel.setSpeaking(false);
                    lModel.setSpeakTime(0);
                    lModel.setChooseLianMaiTime(0);
                }
            }
        }
    }

    /**
     * 处理连麦的显示  包括遗言和正常发言
     *
     * @param userId
     * @param chooseLianMaiTime
     * @param speakTime
     */
    public void dealSpeak(String userId, int chooseLianMaiTime, int speakTime) {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setSpeaking(true);
                    lModel.setChooseLianMaiTime(chooseLianMaiTime);
                    lModel.setSpeakTime(speakTime);
                    break;
                } else {
                    lModel.setSpeaking(false);
                    lModel.setChooseLianMaiTime(0);
                    lModel.setSpeakTime(0);
                }
            }
        }
    }


    /**
     * 处理狼人投票过程的显示
     */
    public void dealWolfKillProgress(String userId, String killUserId) {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setShowControl(true);
                    lModel.setControlContent("杀" + getUserIndex(killUserId) + "号");
                    break;
                }
            }
        }
    }

    /**
     * 处理女巫毒人过程 仅女巫自己可见
     *
     * @param userId
     * @param poisonUserId
     */
    public void dealWitchPoisonProgress(String userId, String poisonUserId) {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setShowControl(true);
                    lModel.setControlContent("毒" + getUserIndex(poisonUserId) + "号");
                    break;
                }
            }
        }
    }

    /**
     * 处理猎人带走人过程 仅猎人自己可见
     *
     * @param userId
     * @param poisonUserId
     */
    public void dealHunterProgress(String userId, String poisonUserId) {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setShowControl(true);
                    lModel.setControlContent("带走" + getUserIndex(poisonUserId) + "号");
                    break;
                }
            }
        }
    }

    /**
     * 处理投票过程 仅自己可见
     *
     * @param userId
     * @param voteUserId
     */
    public void dealVoteProgress(String userId, String voteUserId) {
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setShowControl(true);
                    lModel.setControlContent("投" + getUserIndex(voteUserId) + "号");
                    break;
                }
            }
        }
    }

    /**
     * 处理用户退出游戏
     * @param userId
     */
    public void dealUserOutGame(String userId){
        if (mGameUsers != null && mGameUsers.size() > 0) {
            for (LRSUserModel lModel : mGameUsers) {
                if (lModel.getUser_id().equals(userId)) {
                    lModel.setIs_offline(LRSUserModel.IS_OFFLINE);
                    if(userId.equals(mUserId)){
                        mIsSelfOutGame = true;
                        SDEventManager.post(new ELRSOutGame(true));
                    }
                    break;
                }
            }
        }
    }

    /**
     * 根据步骤播放游戏音频
     *
     * @param step
     */
    public void playGameMusic(int step, String userId) {

        switch (step) {
            case GAME_STEP_READY:
                mPlayer = MediaPlayer.create(mActivity, R.raw.ready);
                mPlayer.start();
                break;
            case GAME_STEP_WOLF_PROPHET:
                mPlayer = MediaPlayer.create(mActivity, R.raw.wolf_prophet_active);
                mPlayer.start();
                break;
            case GAME_STEP_WITCH:
                mPlayer = MediaPlayer.create(mActivity, R.raw.witch);
                mPlayer.start();
                break;
            case GAME_STEP_HUNTER:
                mPlayer = MediaPlayer.create(mActivity, R.raw.hunter);
                mPlayer.start();
                break;
            case GAME_STEP_DAYBREAK:
                mPlayer = MediaPlayer.create(mActivity, R.raw.daybreak);
                mPlayer.start();
                break;
            case GAME_STEP_VOTE:
                mPlayer = MediaPlayer.create(mActivity, R.raw.vote);
                mPlayer.start();
                break;
            case GAME_STEP_SPEAK_ONE_BY_ONE_ONE:
                if (userId.equals(mUserId)) {
                    mPlayer = MediaPlayer.create(mActivity, R.raw.speak);
                    mPlayer.start();
                }
                break;
            case GAME_STEP_LAST_WORDS_ONE:
                if (userId.equals(mUserId)) {
                    mPlayer = MediaPlayer.create(mActivity, R.raw.last_words);
                    mPlayer.start();
                }
                break;
        }
    }

    public void onEventMainThread(ELRSStopPlay event){
        if(mPlayer != null && mPlayer.isPlaying()){
            mPlayer.stop();
        }
    }

    /**
     * 更新游戏用户
     */
    public void refreshUsersInfo() {

    }

    /**
     * 初始化游戏的聊天区域 公共频道和狼人杀频道
     */
    public void joinGroup() {
        joinGameGroup();
        if (getSelfRole() == LRSUserModel.GAME_ROLE_WOLF) {
            joinWolfGroup();
        }
    }

    /**
     * 退出游戏以及狼人频道
     */
    public void quiteGroup() {
        quiteGameGroup();
        if (getSelfRole() == LRSUserModel.GAME_ROLE_WOLF) {
            quiteWolfGroup();
        }
    }

    private void quiteGameGroup() {
        quitGroup(mGameGroupId, null);
    }

    private void quiteWolfGroup() {
        quitGroup(mWolfGroupId, null);
    }

    private void joinGameGroup() {
        if (TextUtils.isEmpty(mGameGroupId)) {
            return;
        }
        joinGroup(mGameGroupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.w("加入游戏频道失败" + code + " : " + desc);
            }

            @Override
            public void onSuccess() {
                LogUtil.w("加入游戏频道成功");
            }
        });
    }

    private void joinWolfGroup() {
        if (TextUtils.isEmpty(mWolfGroupId)) {
            return;
        }
        joinGroup(mWolfGroupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.w("加入狼人频道失败" + code + " : " + desc);
            }

            @Override
            public void onSuccess() {
                LogUtil.w("加入狼人频道成功");
            }
        });
    }

    /**
     * 加入聊天组
     *
     * @param groupId
     * @param listener
     */
    protected void joinGroup(final String groupId, final TIMCallBack listener) {
        IMHelper.applyJoinGroup(groupId, "申请加入", new TIMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.i("join im success:" + groupId);
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc) {
                LogUtil.i("join im error:" + groupId + "," + code + "," + desc);
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 退出聊天组
     *
     * @param groupId
     * @param listener
     */
    protected void quitGroup(final String groupId, final TIMCallBack listener) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        IMHelper.quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onSuccess() {
                LogUtil.i("quit im success");
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc) {
                LogUtil.i("quit im error");
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }


    /**
     * 进行连麦
     */
    public void doMicSpeak(int roomId, final String createrId) {
        CommonInterface.requestCheckLianmai(roomId, new AppRequestCallback<App_check_lianmaiActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                IMHelper.sendMsgC2C(createrId, new CustomMsgInviteVideo(), null);
            }
        });
    }


    private static LRSBottomDialog getDialog(Activity activity) {
        if (mActivity != activity) {
            mDialog = new LRSBottomDialog(activity);
            mActivity = activity;
        }
        return mDialog;
    }


}
