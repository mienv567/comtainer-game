package com.fanwe.live.activity.room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.auction.appview.room.RoomAuctionBtnView;
import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.dialog.AuctionSucPayDialog;
import com.fanwe.auction.dialog.room.AuctionSucPaySingleton;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.listener.SDNetStateListener;
import com.fanwe.library.listener.SDVisibilityStateListener;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.appview.room.RoomBottomView;
import com.fanwe.live.appview.room.RoomSendGiftView;
import com.fanwe.live.appview.room.RoomViewerBottomView;
import com.fanwe.live.appview.room.RoomViewerFinishView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dialog.LiveCreaterFinishDialog;
import com.fanwe.live.dialog.LiveInviteVideoDialog;
import com.fanwe.live.event.ELRSSpeakPass;
import com.fanwe.live.event.ERoomInfoReady;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.LRSGameModel;
import com.fanwe.live.model.RandomPodcastModel;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.view.CircleProgressBar;
import com.fanwe.live.view.LiveLayoutViewer;
import com.fanwe.live.view.SDVerticalScollView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Random;
import java.util.TreeSet;

import static com.tencent.imsdk.IMMsfCoreProxy.mainHandler;

/**
 * Created by Administrator on 2016/8/4.
 */
public class LiveLayoutViewerActivity extends LiveLayoutActivity implements CircleProgressBar.StrokeListener, CircleProgressBar.OneHundredHitListener {
    /**
     * 加载中的图片链接
     */
    public static final String EXTRA_LOADING_VIDEO_IMAGE_URL = "extra_loading_video_image_url";
    /**
     * 直播的类型，仅用于观众时候需要传入0-热门;1-最新;2-关注(int)
     */
    public static final String EXTRA_LIVE_TYPE = "extra_live_type";
    /**
     * 私密直播的key(String)
     */
    public static final String EXTRA_PRIVATE_KEY = "extra_private_key";
    /**
     * 性别0-全部，1-男，2-女(int)
     */
    public static final String EXTRA_SEX = "extra_sex";
    /**
     * 话题id(int)
     */
    public static final String EXTRA_CATE_ID = "extra_cate_id";
    /**
     * 城市(String)
     */
    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_VIDEOPATH = "videoPath";
    public static final String EXTRA_AUDIOONLY = "audioOnly";
    // 七牛相关
    protected static final int MESSAGE_ID_RECONNECTING = 0x01;
    public HitTask currentTask;
    protected View view_loading_video;
    protected ImageView iv_image_top;
    protected ImageView iv_loading_video;
    protected ImageView iv_image_bottom;
    protected ViewGroup fl_live_send_gift;
    protected View view_left;
    protected RoomSendGiftView roomSendGiftView;
    protected RoomViewerBottomView roomViewerBottomView;
    /**
     * 直播的类型0-热门;1-最新
     */
    protected int type;
    /**
     * 私密直播的key
     */
    protected String strPrivateKey;
    /**
     * 性别0-全部，1-男，2-女
     */
    protected int sex;
    /**
     * 话题id
     */
    protected int cate_id;
    /**
     * 城市
     */
    protected String city;
    /**
     * 是否是滚动切换的房间
     */
    protected boolean isScrollChangeRoom = false;
    /**
     * 是否显示直播结束界面
     */
    protected boolean isNeedShowFinish = false;
    /**
     * 是否正在和主播连麦
     */
    protected boolean isVideoing = false;
    /**
     * 七牛是否正在和主播连麦
     */
    protected boolean mIsConferenceStarted = false;
    protected LiveInviteVideoDialog inviteVideoDialog;
    protected UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            sendShareSuccessMsg(share_media, null);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
        }
    };
    protected TextView userANickName;
    protected TextView userCNickName;
    protected TextView userBNickName;
    protected PLVideoView mVideoView;
    protected String mVideoPath = null; // 视频url地址
    protected boolean mIsLandscape = false;
    protected boolean mIsAudioOnly = false;
    protected boolean mIsSWCodec = true;
    protected PLMediaPlayer.OnInfoListener mOnInfoListener;
    protected PLMediaPlayer.OnErrorListener mOnErrorListener;
    protected PLMediaPlayer.OnCompletionListener mOnCompletionListener;
    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING || mIsActivityPaused) {
                return;
            }
            SDOtherUtil.checkNet(LiveLayoutViewerActivity.this, new SDNetStateListener() {
                @Override
                public void onWifi() {
                    //                    mVideoView.setVideoPath(mVideoPath);
                    mVideoView.start();
                    LogUtil.e("重连onWifi");
                }

                @Override
                public void onNone() {
                    sendReconnectMessage();
                }

                @Override
                public void onMobile() {
                    //                    mVideoView.setVideoPath(mVideoPath);
                    mVideoView.start();
                }
            });

        }
    };
    /**
     * 底部菜单点击监听
     */
    protected RoomViewerBottomView.ClickListener bottomClickListener = new RoomViewerBottomView.ClickListener() {
        @Override
        public void onClickBottomOpenSend(View v) {
            LiveLayoutViewerActivity.this.onClickBottomOpenSend(v);
        }

        @Override
        public void onClickBottomPodcastOrder(View v) {
            LiveLayoutViewerActivity.this.onCLickBottomPodcastOrder(v);
        }

        @Override
        public void onCLickBottomViewerAuctionBtn(View v) {
            LiveLayoutViewerActivity.this.onCLickBottomViewerAuctionBtn();
        }

        @Override
        public void onClickBottomMsg(View v) {
            LiveLayoutViewerActivity.this.onClickBottomMsg(v);
        }

        @Override
        public void onClickBottomShare(View v) {
            LiveLayoutViewerActivity.this.onClickBottomShare(v);
        }

        @Override
        public void onClickBottomInviteVideo(View v) {
            LiveLayoutViewerActivity.this.onClickBottomInviteVideo(v);
        }

        @Override
        public void onClickBottomGift(View v) {
            LiveLayoutViewerActivity.this.onClickBottomGift(v);
        }

        @Override
        public void onClickBottomClose(View v) {
            LiveLayoutViewerActivity.this.onClickBottomClose(v);
        }

        @Override
        public void onClickBottomLrsChannel(View v) {

        }

        @Override
        public void onClickBottomGamePass(View v) {
            //过麦
            if (isVideoing) {
                onClickCloseVideo();
            }
            CommonInterface.requestLRSPassSpeak(getRoomId(), null);
            SDEventManager.post(new ELRSSpeakPass());
        }

        @Override
        public void onClickBottomGameOut(View v) {
            //退出游戏
            LiveCreaterFinishDialog dialog = new LiveCreaterFinishDialog(LiveLayoutViewerActivity.this);
            dialog.setTextGravity(Gravity.CENTER);
            dialog.setTextContent(getString(R.string.if_ensure_out_game));
            dialog.setTextCancel(getString(R.string.no));
            dialog.setTextConfirm(getString(R.string.yes));
            dialog.setmListener(new SDDialogCustom.SDDialogCustomListener() {

                @Override
                public void onDismiss(SDDialogCustom dialog) {
                }

                @Override
                public void onClickConfirm(View v, SDDialogCustom dialog) {
                    CommonInterface.requestLRSOut(getRoomId(), null);
                }

                @Override
                public void onClickCancel(View v, SDDialogCustom dialog) {

                }
            });
            dialog.show();
        }

        @Override
        public void onClickBottomGameOpenClose(View v, boolean isShow) {
            //控制玩家信息以及倒计时的显示
            roomLRSUserView.showOrHide(isShow);
            roomInfoView.showOrHideGameTime(isShow);
        }

        @Override
        public void onClickChannelPublic(View v) {

        }

        @Override
        public void onClickChannelWolf(View v) {

        }

        @Override
        public void onClickChannelProgress(View v) {

        }

        @Override
        public void onClickChannelRule(View v) {

        }
    };
    /**
     * 是否可以绑定过连麦按钮的显示隐藏
     */
    private boolean canBindShowInviteVideoView = false;
    private SDVerticalScollView verticalScollView;
    private int viewerNumber;
    private RoomViewerFinishView roomViewerFinishView;
    private RoomAuctionBtnView roomAuctionBtnView;
    private ViewGroup fl_auction_btn;
    private Runnable strokeProgressTask = new Runnable() {
        @Override
        public void run() {
            circleProgressBar.setStrokeProgressValue(circleProgressBar.getStrokeProgress() - 2);
        }
    };
    private HandlerThread secondThread = new HandlerThread("secondThread");
    private HandlerThread thirdThread = new HandlerThread("thirdThread");
    private Handler secondHandler;
    private Handler thirdHanler;
    private boolean isHiting;
    private boolean shouldRepeat; // 描边是否要自增
    private boolean isScrolling; // 是否在上下滑动切换房间
    /**
     * 默认滚动监听
     */
    protected SDVerticalScollView.ScrollListener defaultScrollListener = new SDVerticalScollView.ScrollListener() {
        @Override
        public void onFinishTop() {
            afterVerticalScroll(true);
            verticalScollView.resetVerticalViews();
        }

        @Override
        public void onFinishCenter() {
            verticalScollView.resetVerticalViews();
        }

        @Override
        public void onFinishBottom() {
            afterVerticalScroll(false);
            verticalScollView.resetVerticalViews();
        }

        @Override
        public void onFinishRight() {
        }

        @Override
        public void onFinishHCenter() {
            SDViewUtil.hide(view_left);
        }

        @Override
        public void onFinishLeft() {
            SDViewUtil.show(view_left);
            MobclickAgent.onEvent(LiveLayoutViewerActivity.this, "live_clean");
        }

        @Override
        public void onVerticalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //            if (mIsConferenceStarted) {
            //                SDToast.showToast("连麦期间不允许切换房间");
            //                return;
            //            }
            LiveLayoutViewerActivity.this.onVerticalScroll(e1, e2, distanceX, distanceY);
            isScrolling = true;
        }

        @Override
        public void onHorizontalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        }
    };
    // 点亮连击计数器描边进度任务
    private Runnable strokeTask = new Runnable() {
        @Override
        public void run() {
            while (shouldRepeat) {
                circleProgressBar.post(strokeProgressTask);
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Random random;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            //            七牛临时
            roomHeartView.getViewHeart().addHeart();
        }
    };
    // 自动点亮任务
    private Runnable addHeartTask = new Runnable() {
        private TreeSet<Integer> set = new TreeSet<>();

        @Override
        public void run() {
            random = new Random(System.currentTimeMillis());
            //            int count = random.nextInt(15) + 5;
            int count = 10;
            while (set.size() < count) {
                set.add(random.nextInt(9999));
            }
            for (Integer i : set) {
                mainHandler.postDelayed(task, i);
            }
            secondHandler.postDelayed(addHeartTask, 10000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(mVideoPath);
        LogUtil.i(mVideoPath);
        mVideoView.start();
        mIsActivityPaused = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        mIsActivityPaused = true;
    }

    /**
     * 点亮一下点亮计数器需要进行的操作
     */
    private void circleProgressBarHit() {
        if (isScrolling) {
            return;
        }

        if (circleProgressBar.getVisibility() != View.VISIBLE) {
            circleProgressBar.setVisibility(View.VISIBLE);
        }
        checkHiting();
        shouldRepeat = false;
        secondHandler.removeCallbacks(strokeTask);
        secondHandler.postDelayed(strokeTask, 800);
        secondHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldRepeat = true;
            }
        }, 200);
        //        thirdHanler.removeCallbacks(currentTask);
        secondHandler.removeCallbacks(addHeartTask);
        secondHandler.postDelayed(addHeartTask, 10000);
        currentTask = new HitTask(1);
        thirdHanler.post(currentTask);
    }

    private void checkHiting() {
        if (isHiting) {
            return;
        }
    }

    @Override
    public void HitOneHundred() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roomStarView.addStar();
            }
        });
        //        roomHeartViewWithNumber.getViewHeart().addHeart("heart0", circleProgressBar.getHitCount() + "");
        roomHeartViewWithNumber.addHeart(circleProgressBar.getHitCount());
        circleProgressBar.reset();
        shouldRepeat = false;
        circleProgressBar.setVisibility(View.INVISIBLE);
        secondHandler.removeCallbacks(strokeTask);
        thirdHanler.removeCallbacks(currentTask);
    }

    @Override
    public void strokeFull() {
        roomHeartViewWithNumber.addHeart(circleProgressBar.getHitCount());
        circleProgressBar.reset();
        shouldRepeat = false;
        circleProgressBar.setVisibility(View.INVISIBLE);
        secondHandler.removeCallbacks(strokeTask);
        thirdHanler.removeCallbacks(currentTask);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String loadingVideoImageUrl = getIntent().getStringExtra(EXTRA_LOADING_VIDEO_IMAGE_URL);
        type = getIntent().getIntExtra(EXTRA_LIVE_TYPE, 0);
        strPrivateKey = getIntent().getStringExtra(EXTRA_PRIVATE_KEY);
        sex = getIntent().getIntExtra(EXTRA_SEX, 0);
        city = getIntent().getStringExtra(EXTRA_CITY);

        view_loading_video = findViewById(R.id.view_loading_video);
        iv_image_top = (ImageView) findViewById(R.id.iv_image_top);
        iv_loading_video = (ImageView) findViewById(R.id.iv_loading_video);
        iv_image_bottom = (ImageView) findViewById(R.id.iv_image_bottom);
        view_left = findViewById(R.id.view_left);
        //        if(view_left != null){
        //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //                view_left.setPadding(0, SDViewUtil.dp2px(24),0,0);
        //            }
        //        }

        // 七牛临时
        //        if (!isEmpty(loadingVideoImageUrl)) {
        //            File file = ImageLoader.getInstance().getDiskCache().get(loadingVideoImageUrl);
        //            if (file != null) {
        //                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //                setLoadingVideoBitmap(bitmap);
        //            } else {
        setLoadingVideoImageUrl(loadingVideoImageUrl);
        //            }
        //        }

        initLayout(getWindow().getDecorView());

        initPlayer();


    }

    /**
     * 直播间都需要常亮,无titile
     */
    @Override
    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void sendReconnectMessage() {
        //        SDToast.showToast("正在重连...");
        LogUtil.e("正在重连...");
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 200);
    }

    /**
     * 初始化七牛播放器相关数据
     */
    protected void initPlayer() {
        mVideoPath = getIntent().getStringExtra(LiveViewerActivity.EXTRA_VIDEOPATH);
        mIsLandscape = getIntent().getBooleanExtra(LiveViewerActivity.EXTRA_ORIENTATION, false);
        mIsAudioOnly = getIntent().getBooleanExtra(LiveViewerActivity.EXTRA_AUDIOONLY, false);
        mIsSWCodec = getIntent().getBooleanExtra(EXTRA_SWCODEC, true);
//        setRequestedOrientation(mIsLandscape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        mVideoView.setAVOptions(options);
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);

        mVideoView.setVideoPath(mVideoPath);

        initVideoListener();

        // Set some listeners
        if (null != mOnInfoListener) {
            mVideoView.setOnInfoListener(mOnInfoListener);
        }
        if (null != mOnCompletionListener) {
            mVideoView.setOnCompletionListener(mOnCompletionListener);
        }
        if (null != mOnErrorListener) {
            mVideoView.setOnErrorListener(mOnErrorListener);
        }
    }

    protected void initVideoListener() {
        mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                LogUtil.e("onInfo: " + what + ", " + extra);
                if (what == PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    hideLoadingVideo();
                }
                return true;
            }
        };

        mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
                boolean isNeedReconnect = false;
                LogUtil.e("Error happened, errorCode = " + errorCode);
                switch (errorCode) {
                    case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                        LogUtil.e("invalid URL!");
                        break;
                    case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                        LogUtil.e("404 resource not found !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                        LogUtil.e("Connection refused !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                        LogUtil.e("Connection timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                        LogUtil.e("Empty playlist !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                        LogUtil.e("Stream disconnected !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                        LogUtil.e("Network IO Error !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                        LogUtil.e("Unauthorized Error !");
                        break;
                    case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                        LogUtil.e("Prepare timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                        LogUtil.e("Read frame timeout !");
                        isNeedReconnect = true;
                        break;
                    case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                        break;
                    default:
                        LogUtil.e("unknown error !");
                        break;
                }
                // Todo pls handle the error status here, reconnect or call finish()
                if (isNeedReconnect) {
                    sendReconnectMessage();
                } else {
                    finish();
                }
                // Return true means the error has been handled
                // If return false, then `onCompletion` will be called
                return true;
            }
        };


        mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                LogUtil.e("Play Completed !");
                SDToast.showToast(getString(R.string.live_is_finish_out_room));
                exitRoom(false);
                //                finish();
            }
        };
    }

    protected void exitRoom(boolean isNeedFinish) {

    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);

        // 七牛相关
        mVideoView = (PLVideoView) view.findViewById(R.id.VideoView);
        userANickName = (TextView) findViewById(R.id.tv_userA);
        userCNickName = (TextView) findViewById(R.id.tv_userC);
        userBNickName = (TextView) findViewById(R.id.tv_userB);

        verticalScollView = (SDVerticalScollView) view.findViewById(R.id.view_vertical_scroll);
        fl_live_send_gift = (ViewGroup) view.findViewById(R.id.fl_live_send_gift);
        fl_live_auction_info = (ViewGroup) view.findViewById(R.id.fl_live_auction_info);
        fl_auction_btn = (ViewGroup) view.findViewById(R.id.fl_auction_btn);
        initSDVerticalScollView(verticalScollView);
        secondThread.start();
        secondHandler = new Handler(secondThread.getLooper());
        thirdThread.start();
        thirdHanler = new Handler(thirdThread.getLooper());
        secondHandler.postDelayed(addHeartTask, 5000);
        circleProgressBar.setStrokeListener(this);
        circleProgressBar.setHitListener(this);

    }

    /**
     * 重写此方法设置监听
     *
     * @param scollView
     */
    protected void initSDVerticalScollView(SDVerticalScollView scollView) {
        if (scollView == null) {
            return;
        }

        this.scollView = scollView;
        scollView.setTopView(iv_image_top);
        scollView.setVerticalView(findViewById(R.id.view_touch_scroll));
        scollView.setBottomView(iv_image_bottom);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_bottom_close);
        scollView.setLeftView(view_left);
        scollView.setHorizontalView(findViewById(R.id.rl_root_layout));

        scollView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addHeart();
            }
        });
        scollView.setOnScrollClickListener(new SDVerticalScollView.OnScrollClickListener() {
            @Override
            public void onClick(MotionEvent e) {
                if (isViewClicked(rl, e)) {
                    onClickBottomClose(rl);
                }
            }
        });
        scollView.setListenerScroll(defaultScrollListener);

        //        // 连麦窗口点击事件
        //        scollView.setGlVideoViewOnClickListener(new GLVideoView.GlVideoViewOnClickListener() {
        //            @Override
        //            public void onClick(int index, GLVideoView glVideoView) {
        //                LiveUserInfoDialog dialog = new LiveUserInfoDialog(LiveLayoutViewerActivity.this, glVideoView.getUserId());
        //                dialog.show();
        //            }
        //        });

    }

    public boolean isViewClicked(View view, MotionEvent e) {
        boolean result = false;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        //        int y = location[1] - getStatusBarHeight();

        //        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            y += SDViewUtil.dp2px(24);
        //        }
        if (((e.getX() >= x) && (e.getX() <= (x + view.getWidth()))) &&
                ((e.getY() >= y) && (e.getY() <= (y + view.getHeight())))) {
            result = true;
        }
        return result;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected void onVerticalScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    protected void afterVerticalScroll(boolean top) {
        if (top) {
            iv_loading_video.setImageDrawable(iv_image_top.getDrawable());
            if (getRoomInfo() != null && getRoomInfo().getPodcast_previous() != null) {
                setRoomId(getRoomInfo().getPodcast_previous().getRoom_id());
            }
        } else {
            iv_loading_video.setImageDrawable(iv_image_bottom.getDrawable());
            if (getRoomInfo() != null && getRoomInfo().getPodcast_next() != null) {
                LogUtil.e("vertical:" + getRoomInfo());
                setRoomId(getRoomInfo().getPodcast_next().getRoom_id());
                afterVerticalScrollRoom(getRoomInfo());


            }
        }

        // 如果切换房间,连击计数器还在应该把它重置并隐藏
        if (null != circleProgressBar && circleProgressBar.getVisibility() == View.VISIBLE) {
            circleProgressBar.reset();
            circleProgressBar.setVisibility(View.INVISIBLE);
        }


        isScrollChangeRoom = true;
    }

    // 房间切换后的回调
    protected void afterVerticalScrollRoom(App_get_videoActModel videoActModel) {
    }


    protected void showViewerLayout() {
        roomInfoView.show();
        roomGiftPlayView.show();
        roomPopMsgView.show();
        roomViewerJoinRoomView.show();
        roomMsgView.show();
        roomHeartView.show();
        roomGiftGifView.show();
        roomViewerBottomView.showClick(true);
        bindShowInviteVideoView();
    }

    protected void invisibleViewerLayout() {
        //滑动切换的时候移除竞拍信息 竞拍排行榜 竞拍锤子 付款按钮
        removeView(roomAuctionInfoView);
        removeView(roomAuctionBtnView);
        removeView(auctionUserRanklistView);
        if (roomViewerBottomView != null) {
            roomViewerBottomView.hideCustomMsgAuction();
        }

        roomInfoView.invisible();
        roomGiftPlayView.invisible();
        roomPopMsgView.invisible();
        roomViewerJoinRoomView.invisible();
        roomMsgView.invisible();
        roomHeartView.invisible();
        roomGiftGifView.invisible();
        roomViewerBottomView.showClick(false);
        roomViewerBottomView.hideInviteVideoView();
    }

    public void addHeart() {
        if (roomHeartView != null) {
            roomHeartView.addHeart();
        }
        circleProgressBarHit();
    }


    /**
     * 返回viewpager布局
     *
     * @return
     */
    protected View getViewInViewPager() {
        return new LiveLayoutViewer(this);
    }


    @Override
    protected void onMsgEndVideo(MsgModel msg) {
        super.onMsgEndVideo(msg);
        //        SDToast.showToast("LiveLayoutViewer接收到onMsgEndVideo");
        isNeedShowFinish = true;
        viewerNumber = msg.getCustomMsgEndVideo().getShow_num();
    }

    public boolean isInvitingVideo() {
        if (inviteVideoDialog != null) {
            return inviteVideoDialog.isShowing();
        } else {
            return false;
        }
    }

    @Override
    protected void addLiveFinish() {
        if (getRoomInfo() != null) {
            removeView(roomViewerFinishView);
            roomViewerFinishView = new RoomViewerFinishView(this);
            int status = getRoomInfo().getStatus();
            if (status == 1) {
                roomViewerFinishView.setHasFollow(roomInfoView.getHasFollow());
            } else if (status == 2) {
                viewerNumber = getRoomInfo().getShow_num();
                roomViewerFinishView.setHasFollow(getRoomInfo().getHas_focus());
            }
            roomViewerFinishView.setViewerNumber(viewerNumber);
            addView(roomViewerFinishView);
        }
    }

    public void setLoadingVideoImageUrl(final String loadingVideoImageUrl) {
        if (loadingVideoImageUrl != null) {
            //            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<Bitmap>() {
            //                @Override
            //                public Bitmap onBackground() {
            //                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(loadingVideoImageUrl);
            //                    return bitmap;
            //                }
            //
            //                @Override
            //                public void onMainThread(Bitmap result) {
            //                    setLoadingVideoBitmap(result);
            //                }
            //            });
            Glide.with(LiveLayoutViewerActivity.this).load(loadingVideoImageUrl).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    setLoadingVideoBitmap(resource);
                }
            });
        }
    }

    public void setLoadingVideoBitmap(Drawable bitmapLoadingVideo) {
        if (iv_loading_video != null) {
            iv_loading_video.setImageDrawable(bitmapLoadingVideo);
        }
    }

    public void setLoadingVideoBitmap(Bitmap bitmapLoadingVideo) {
        if (iv_loading_video != null) {
            iv_loading_video.setImageBitmap(bitmapLoadingVideo);
        }
    }

    protected void showLoadingVideo() {
        if (view_loading_video != null) {
            SDViewUtil.show(view_loading_video);
        }
    }

    protected void hideLoadingVideo() {
        if (view_loading_video != null) {
            SDViewUtil.hide(view_loading_video);
            isScrolling = false;
        }
    }

    protected void bindLoadingImages() {
        if (getRoomInfo() != null) {
            if (getRoomInfo().isOk()) {
                RandomPodcastModel next = getRoomInfo().getPodcast_next();
                if (next != null) {
                    SDViewBinder.setImageView(LiveLayoutViewerActivity.this, next.getHead_image(), iv_image_bottom);
                }
                RandomPodcastModel previous = getRoomInfo().getPodcast_previous();
                if (previous != null) {
                    SDViewBinder.setImageView(LiveLayoutViewerActivity.this, previous.getHead_image(), iv_image_top);
                }
            }
        }
    }

    @Override
    protected SDRequestHandler requestRoomInfo() {
        return CommonInterface.requestRoomInfo(getRoomId(), 0, type, 1, sex, cate_id, city, strPrivateKey, null, new AppRequestCallback<App_get_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                setRoomInfo(actModel);
                SDViewBinder.setTextView(scollView.getTv_user_number_left(), String.valueOf(getRoomInfo().getPodcast().getUserId()));
                if (rootModel.isOk()) {
                    onSuccessRequestRoomInfo(actModel);
                } else {
                    onErrorRequestRoomInfo(actModel);
                }
            }
        });
    }

    @Override
    protected void dealWolfKillGameInfo(LRSGameModel model) {

    }


    @Override
    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {
        super.onSuccessRequestRoomInfo(actModel);
        //在开启竞拍功能的情况下观众进入直播间请求拍卖信息
        if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
            requestPaiUserGetVideo(actModel.getPai_id());
        }

        bindShowInviteVideoView();
        bindLoadingImages();
        SDEventManager.post(new ERoomInfoReady());
    }

    public void setCanBindShowInviteVideoView(boolean canBindShowInviteVideoView) {
        this.canBindShowInviteVideoView = canBindShowInviteVideoView;
    }

    /**
     * 是否显示连麦按钮
     */
    protected void bindShowInviteVideoView() {

        if (getRoomInfo() != null) {
            if (canBindShowInviteVideoView) {
                if (getRoomInfo().getHasLianmai() == 1) {
                    roomViewerBottomView.showInviteVideoView();
                } else {
                    roomViewerBottomView.hideInviteVideoView();
                }

            }
        } else {
            roomViewerBottomView.hideInviteVideoView();
        }
    }

    /**
     * 送礼物
     */
    protected void addLiveSendGift() {
        if (roomSendGiftView == null) {
            roomSendGiftView = new RoomSendGiftView(this);
            roomSendGiftView.setVisibilityStateListener(new SDVisibilityStateListener() {
                @Override
                public void onVisible(View view) {
                    getBottomView().invisible(true);
                    roomMsgView.invisible(true);
                }

                @Override
                public void onGone(View view) {
                    getBottomView().show(true);
                    roomMsgView.show(true);
                    SDViewUtil.removeViewFromParent(roomSendGiftView);
                }

                @Override
                public void onInvisible(View view) {
                    getBottomView().show(true);
                    roomMsgView.show(true);
                    SDViewUtil.removeViewFromParent(roomSendGiftView);
                }
            });
        }
        roomSendGiftView.requestData(this);
        roomSendGiftView.bindUserData();
        replaceView(fl_live_send_gift, roomSendGiftView);
        MobclickAgent.onEvent(LiveLayoutViewerActivity.this, "live_gift");
    }

    @Override
    protected void addLiveBottomMenu() {
        roomViewerBottomView = new RoomViewerBottomView(this);
        roomViewerBottomView.setClickListener(bottomClickListener);
        replaceView(fl_live_bottom_menu, roomViewerBottomView);
    }

    @Override
    protected RoomBottomView getBottomView() {
        return roomViewerBottomView;
    }

    //星店订单
    protected void onCLickBottomPodcastOrder(View v) {
        InitActModel initmodel = InitActModelDao.query();
        if (initmodel != null) {
            App_InitH5Model h5Model = initmodel.getH5_url();
            if (h5Model != null) {
                String url_podcast_order = h5Model.getUrl_podcast_order();
                if (!TextUtils.isEmpty(url_podcast_order)) {
                    Intent intent = new Intent(this, LiveWebViewActivity.class);
                    intent.putExtra(LiveWebViewActivity.EXTRA_URL, url_podcast_order);
                    startActivity(intent);
                } else {
                    SDToast.showToast(getString(R.string.url_empty));
                }
            }
        }
    }

    /*观众付款入口*/
    protected void onCLickBottomViewerAuctionBtn() {
        AuctionSucPayDialog dialog = AuctionSucPaySingleton.getInstance().getAuctionSucPayDialog();
        if (dialog != null) {
            dialog.showBottom();
        }
    }

    /**
     * 点击底部分享菜单
     *
     * @param v
     */
    protected void onClickBottomShare(View v) {
        //                        openShare(new UMShareListener() {
        //                            @Override
        //                            public void onResult(SHARE_MEDIA share_media) {
        //                                sendShareSuccessMsg(share_media, null);
        //                            }
        //
        //                            @Override
        //                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        //                            }
        //
        //                            @Override
        //                            public void onCancel(SHARE_MEDIA share_media) {
        //                            }
        //                        });
        View popupContentView = View.inflate(this, R.layout.view_live_share, null);
        final View share_qq = popupContentView.findViewById(R.id.ll_share_qq);
        final View share_qzoom = popupContentView.findViewById(R.id.ll_share_qzoom);
        final View share_weixin = popupContentView.findViewById(R.id.ll_share_weixin);
        final View share_pengyouquan = popupContentView.findViewById(R.id.ll_share_pengyouquan);
        final View share_xinlang = popupContentView.findViewById(R.id.ll_share_xinlang);

        View.OnClickListener shareViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == share_qq) {
                    openShare(0, umShareListener);
                } else if (v == share_qzoom) {
                    openShare(1, umShareListener);
                } else if (v == share_weixin) {
                    openShare(2, umShareListener);
                } else if (v == share_pengyouquan) {
                    openShare(3, umShareListener);
                } else if (v == share_xinlang) {
                    openShare(4, umShareListener);
                }
                mPopupWindow.dismiss();
            }
        };

        popupContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                    return true;
                }
                return false;
            }
        });

        share_qq.setOnClickListener(shareViewListener);
        share_qzoom.setOnClickListener(shareViewListener);
        share_weixin.setOnClickListener(shareViewListener);
        share_pengyouquan.setOnClickListener(shareViewListener);
        share_xinlang.setOnClickListener(shareViewListener);

        mPopupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, SDViewUtil.dp2px(80), true);
        mPopupWindow.setAnimationStyle(R.style.path_popwindow_anim_enterorout_window);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 点击连麦菜单
     *
     * @param v
     */
    protected void onClickBottomInviteVideo(View v) {
        //子类去实现
        /*if (!mIsConferenceStarted) {
            //后台现未做连麦检查接口, 直接请求连麦接口

            *//*CommonInterface.requestCheckLianmai(getRoomId(), new AppRequestCallback<App_check_lianmaiActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (rootModel.getStatus() == 1) {
                        showInviteVideoDialog();
                    }
                }
            });*//*
            showInviteVideoDialog();
        } else {
            SDDialogMenu dialog = new SDDialogMenu(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setItems(new String[]{getString(R.string.close_invite_video)});
            dialog.setmListener(new SDDialogMenu.SDDialogMenuListener() {
                @Override
                public void onItemClick(View v, int index, SDDialogMenu dialog) {
                    switch (index) {
                        case 0:
                            onClickCloseVideo();
                            break;

                        default:
                            break;
                    }
                }

                @Override
                public void onDismiss(SDDialogMenu dialog) {
                }

                @Override
                public void onCancelClick(View v, SDDialogMenu dialog) {
                }
            });
            dialog.showBottom();
        }*/
    }

    /**
     * 关闭连麦
     */
    protected void onClickCloseVideo() {
        //子类实现
    }

    @Override
    protected void onMsgAcceptVideo(MsgModel msg) {
        super.onMsgAcceptVideo(msg);
        if (!LRSManager.getInstance().mIsGaming) {
            if (isInvitingVideo()) {
                inviteVideoDialog.tv_info.setText(msg.getCustomMsgAcceptVideo().getSender().getNickName() + "接受了你的连麦请求");
            }
        } else {
            SDToast.showToast("现在轮到你发言啦");
        }
    }

    protected void showInviteVideoDialog() {
        dismissInviteVideoDialog();
        inviteVideoDialog = new LiveInviteVideoDialog(this);
        inviteVideoDialog.show();
    }

    public void dismissInviteVideoDialog() {
        if (inviteVideoDialog != null) {
            inviteVideoDialog.dismiss();
        }
    }

    /**
     * 点击送礼物菜单
     *
     * @param v
     */
    protected void onClickBottomGift(View v) {
        addLiveSendGift();
        roomSendGiftView.show(true);
    }

    //拍卖详情
    private void requestPaiUserGetVideo(int pai_id) {
        if (pai_id > 0) {
            AuctionCommonInterface.requestPaiUserGetVideo(pai_id, new AppRequestCallback<App_pai_user_get_videoActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (rootModel.getStatus() == 1) {
                        //设置直播处于竞拍中
                        getRoomInfo().setAuctioning(true);
                        //绑定顶部竞拍信息
                        addLiveAuctionInfo(actModel);
                        //绑定锤子按钮
                        addLiveAuctionBtnView(actModel);
                        if (actModel.getData() != null && actModel.getData().getBuyer() != null) {
                            //绑定排行榜
                            addLiveAuctionRankList(actModel.getData().getBuyer());
                            //判断底部付款按钮是否显示
                            roomViewerBottomView.showHideCustomMsgAuction(actModel.getData().getBuyer());
                        }
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                }
            });
        }
    }

    //关注添加竞拍按钮
    private void addLiveAuctionBtnView(App_pai_user_get_videoActModel app_pai_user_get_videoActModel) {
        roomAuctionBtnView = new RoomAuctionBtnView(this);
        replaceView(fl_auction_btn, roomAuctionBtnView);
        roomAuctionBtnView.bindData(app_pai_user_get_videoActModel);
    }

    @Override
    protected void onMsgAuction(MsgModel msg) {
        super.onMsgAuction(msg);
        if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_CREATE_SUCCESS) {
            CustomMsgAuctionCreateSuccess createSuccess = msg.getCustomMsgAuctionCreateSuccess();
            requestPaiUserGetVideo(createSuccess.getPai_id());
        } else if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_SUCCESS) {
            //竞拍成功移除锤子
            removeView(roomAuctionBtnView);
        } else if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_FAIL) {
            //流拍移除锤子
            removeView(roomAuctionBtnView);
        }
    }

    public class HitTask implements Runnable {
        private int repeatCount;

        public HitTask(int repeatCount) {
            this.repeatCount = repeatCount;
        }

        @Override
        public void run() {
            isHiting = true;
            circleProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    circleProgressBar.setStrokeProgressValue(100);
                    circleProgressBar.hit();
                    isHiting = false;
                }
            });
        }
    }


}
