package com.fanwe.live;

import com.fanwe.library.utils.SDBase64;
import com.fanwe.library.utils.SDResourcesUtil;

public class LiveConstant {

    public static final int APP_ID_TENCENT_LIVE = Integer.valueOf(SDResourcesUtil.getString(R.string.app_id_tencent_live));
    public static final String ACCOUNT_TYPE_TENCENT_LIVE = SDResourcesUtil.getString(R.string.account_type);

    public static final int VIDEO_VIEW_MAX = 1;

    public static final boolean CAN_MOVE_VIDEO = false;

    public static final String LIVE_PRIVATE_KEY_TAG = SDBase64.decodeToString("8J+UkQ==");

    public static final String LIVE_HOT_CITY = "热门";

    public static final String LEVEL_SPAN_KEY = "level";

    public static final int LIVE_VIEWER_FOLLOW_TIME = 5 * 60 * 1000; //提示关注主播时间（毫秒）

    public static final int LIVE_CPU_NUM = 4; //CPU核心数 - 高配

    public static final int LIVE_CPU_MAXHZ = 1572864;//CPU主频 - 高配 1.5GHZ

    public static final int LIVE_MEMORY = 1945124864;//内存大小 - 高配 2G

    /**
     * 0-正式，1-测试
     */
    public static final int LIVE_ENVIRONMENT = 0;

    /**
     * 用于发送和接收自定义消息的类型判断
     */
    public static final class CustomMsgType {
        public static final int MSG_NONE = -1;
        /**
         * 正常文字聊天消息
         */
        public static final int MSG_TEXT = 0;
        /**
         * 收到发送礼物消息
         */
        public static final int MSG_GIFT = 1;
        /**
         * 收到弹幕消息
         */
        public static final int MSG_POP_MSG = 2;
        /**
         * 主播结束直播
         */
        public static final int MSG_CREATER_QUIT = 3;
        /**
         * 禁言消息
         */
        public static final int MSG_FORBID_SEND_MSG = 4;
        /**
         * 观众进入直播间消息
         */
        public static final int MSG_VIEWER_JOIN = 5;
        /**
         * 观众退出直播间消息
         */
        public static final int MSG_VIEWER_QUIT = 6;
        /**
         * 主播结束直播消息，直播间内的人可收到
         */
        public static final int MSG_END_VIDEO = 7;
        /**
         * 红包消息
         */
        public static final int MSG_RED_ENVELOPE = 8;
        /**
         * 直播消息
         */
        public static final int MSG_LIVE_MSG = 9;
        /**
         * 主播离开
         */
        public static final int MSG_CREATER_LEAVE = 10;
        /**
         * 主播回来
         */
        public static final int MSG_CREATER_COME_BACK = 11;
        /**
         * 点亮
         */
        public static final int MSG_LIGHT = 12;
        /**
         * 发起连麦
         */
        public static final int MSG_INVITE_VIDEO = 13;
        /**
         * 接受连麦
         */
        public static final int MSG_ACCEPT_VIDEO = 14;
        /**
         * 拒绝连麦
         */
        public static final int MSG_REJECT_VIDEO = 15;
        /**
         * 主播结束连麦
         */
        public static final int MSG_CREATER_STOP_VIDEO = 16;
        /**
         * 踢人
         */
        public static final int MSG_STOP_LIVE = 17;
        /**
         * 任何主播的结束，服务端都会推这条消息下来，用于更新列表状态
         */
        public static final int MSG_LIVE_STOPPED = 18;
        /**
         * 私聊文字消息
         */
        public static final int MSG_PRIVATE_TEXT = 20;

        /**
         * 私聊语音消息
         */
        public static final int MSG_PRIVATE_VOICE = 21;
        /**
         * 私聊图片消息
         */
        public static final int MSG_PRIVATE_IMAGE = 22;
        /**
         * 私聊礼物消息
         */
        public static final int MSG_PRIVATE_GIFT = 23;
        /**
         * 竞拍成功
         */
        public static final int MSG_AUCTION_SUCCESS = 25;
        /**
         * 竞拍通知付款，比如第一名超时未付款，通知下一名付款
         */
        public static final int MSG_AUCTION_NOTIFY_PAY = 26;
        /**
         * 流拍
         */
        public static final int MSG_AUCTION_FAIL = 27;

        /**
         * 推送出价信息
         */
        public static final int MSG_AUCTION_OFFER = 28;

        /**
         * 支付成功
         */
        public static final int MSG_AUCTION_PAY_SUCCESS = 29;
        /**
         * 主播发起竞拍成功
         */
        public static final int MSG_AUCTION_CREATE_SUCCESS = 30;
        /**
         * 购物直播商品推送
         */
        public static final int MSG_SHOP_GOODS_PUSH = 31;
        /**
         * 观众分享直播间
         */
        public static final int MSG_SHARE = 32;

        /**
         * 带数字连击点亮
         */
        public static final int MSG_CUSTOM_LIGHT = 33;

        /**
         * 点亮满99星星动画
         */
        public static final int MSG_STAR = 35;

        /**
         * 拉流切换地址
         */
        public static final int MSG_CHANGE_CHANNEL = 36;

        /**
         * 红点相关的消息
         */
        public static final int MSG_RED_POINT = 37;

        /**
         * 狼人杀 操作IM
         */
        public static final int MSG_LRS = 38;

        /**
         * 狼人杀 游戏进程
         */
        public static final int MSG_LRS_PROGRESS = 39;

        /**
         * 全屏tips消息（跑马灯）
         */
        public static final int MSG_TIPS_MSG = 40;

        /**
         * 任务完成或失败弹窗
         */
        public static final int MSG_MISSION_RESULT = 41;

        /**
         * 狼人杀 游戏规则 - 并不会单独通过IM获得 这个是为了显示需要才存在的
         */
        public static final int MSG_LRS_RULE = 999;
        /**
         * 在线人数和在线列表
         */
        public static final int MSG_ONLINE_NUM = 100;
    }

    /**
     * 用于直播间聊天的类型判断
     */
    public static final class LiveMsgType {
        /**
         * 正常文字聊天消息
         */
        public static final int MSG_TEXT = 0;
        /**
         * 观看者加入
         */
        public static final int MSG_VIEWER_JOIN = MSG_TEXT + 1;
        /**
         * 观众收到礼物发送成功消息
         */
        public static final int MSG_GIFT_VIEWER = MSG_VIEWER_JOIN + 1;
        /**
         * 主播收到礼物发送成功消息
         */
        public static final int MSG_GIFT_CREATER = MSG_GIFT_VIEWER + 1;
        /**
         * 禁言消息
         */
        public static final int MSG_FORBID_SEND_MSG = MSG_GIFT_CREATER + 1;
        /**
         * 直播间消息
         */
        public static final int MSG_LIVE_MSG = MSG_FORBID_SEND_MSG + 1;
        /**
         * 红包消息
         */
        public static final int MSG_RED_ENVELOPE = MSG_LIVE_MSG + 1;
        /**
         * 主播离开
         */
        public static final int MSG_CREATER_LEAVE = MSG_RED_ENVELOPE + 1;
        /**
         * 主播回来
         */
        public static final int MSG_CREATER_COME_BACK = MSG_CREATER_LEAVE + 1;
        /**
         * 点亮
         */
        public static final int MSG_LIGHT = MSG_CREATER_COME_BACK + 1;
        /**
         * 弹幕
         */
        public static final int MSG_POP_MSG = MSG_LIGHT + 1;
        /**
         * 高级用户加入
         */
        public static final int MSG_PRO_VIEWER_JOIN = MSG_POP_MSG + 1;
        /**
         * 推送出价信息
         */
        public static final int MSG_AUCTION_OFFER = MSG_PRO_VIEWER_JOIN + 1;
        /**
         * 竞拍成功
         */
        public static final int MSG_AUCTION_SUCCESS = MSG_AUCTION_OFFER + 1;
        /**
         * 支付成功
         */
        public static final int MSG_AUCTION_PAY_SUCCESS = MSG_AUCTION_SUCCESS + 1;
        /**
         * 流拍
         */
        public static final int MSG_AUCTION_FAIL = MSG_AUCTION_PAY_SUCCESS + 1;
        /**
         * 竞拍通知付款，比如第一名超时未付款，通知下一名付款
         */
        public static final int MSG_AUCTION_NOTIFY_PAY = MSG_AUCTION_FAIL + 1;
        /**
         * 主播发起竞拍成功
         */
        public static final int MSG_AUCTION_CREATE_SUCCESS = MSG_AUCTION_NOTIFY_PAY + 1;
        /**
         * 观众分享主播
         */
        public static final int MSG_SHARE = MSG_AUCTION_CREATE_SUCCESS + 1;
        /**
         * 点亮带数字
         */
        public static final int MSG_CUSTOM_LIGHT = MSG_SHARE + 1;
        /**
         * 全房间弹幕
         */
        public static final int MSG_TIP_MSG = MSG_CUSTOM_LIGHT + 1;
        /**
         * 点亮满99飘星星
         */
        public static final int MSG_STAR = MSG_TIP_MSG + 1;
        /**
         * 狼人杀 游戏进程
         */
        public static final int MSG_LRS_PROGRESS = MSG_STAR + 1;

        /**
         * 狼人杀 规则
         */
        public static final int MSG_LRS_RULE = MSG_LRS_PROGRESS + 1;

        /**
         * 在线人数列表
         */
        public static final int MSG_ONLINE_NUM = MSG_LRS_RULE + 1;

        /**
         * 跑马灯
         */
        public static final int MSG_Marquee = MSG_ONLINE_NUM + 1;
    }

    /**
     * 私聊消息类型
     */
    public static final class PrivateMsgType {
        /**
         * 文字消息
         */
        public static final int MSG_TEXT_LEFT = 0;
        public static final int MSG_TEXT_RIGHT = 1;

        //语音
        public static final int MSG_VOICE_LEFT = 2;
        public static final int MSG_VOICE_RIGHT = 3;

        //图片
        public static final int MSG_IMAGE_LEFT = 4;
        public static final int MSG_IMAGE_RIGHT = 5;

        //礼物
        public static final int MSG_GIFT_LEFT = 6;
        public static final int MSG_GIFT_RIGHT = 7;

    }

    public static final class GiftType {
        public static final int NORMAL = 0;

        public static final int GIF = 1;

        public static final int ANIMATOR = 2;
    }

    public static final class GiftAnimatorType {
        public static final String PLANE1 = "plane1";

        public static final String PLANE2 = "plane2";

        public static final String ROCKET1 = "rocket1";

        public static final String FERRARI = "ferrari";

        public static final String LAMBORGHINI = "lamborghini";
    }

}
