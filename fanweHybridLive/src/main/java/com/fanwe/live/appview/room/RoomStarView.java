package com.fanwe.live.appview.room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.EMsgStarLocal;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgStar;
import com.fanwe.live.utils.UiUtils;
import com.sunday.eventbus.SDEventManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * 直播间顶部view
 */
public class RoomStarView extends RoomLooperMainView<CustomMsgStar> {
    HashMap<ImageView, Coordinates> coordinates = new HashMap<ImageView, Coordinates>();
    private  RelativeLayout rl_star_container;
    private static final long DURATION_LOOPER = 400;
    public RoomStarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomStarView(Context context) {
        super(context);
    }

    @Override
    protected void looperWork(LinkedList<CustomMsgStar> queue) {
        CustomMsgStar msg = queue.poll();
        if (msg != null) {
            startStar();
        }
    }

    /**
     * 默认事件拦截间隔
     */
    private static final long DURATION_INTERCEPT = 100;


    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_room_star;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void addStar(){
        sendStarMsg();
    }

    private void sendStarMsg(){
//        final CustomMsgStar msg = new CustomMsgStar();
//        IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {
//
//            @Override
//            public void onSuccess(TIMMessage timMessage) {
//                IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//            }
//        });
        CommonInterface.requestLight(getLiveInfo().getGroupId(), getLiveInfo().getRoomId(), 2, new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                EMsgStarLocal event = new EMsgStarLocal();
                event.setmCustomMsgStar(new CustomMsgStar());
                SDEventManager.post(event);
            }
        });
    }

    public void startStar() {
        rl_star_container = (RelativeLayout) findViewById(R.id.rl_star);
        int[] starDrawables = new int[]{
                R.drawable.star_blue, R.drawable.star_green, R.drawable.star_purple, R.drawable.star_red, R.drawable.star_yellow};

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UiUtils.dip2px(App.getApplication().getApplicationContext(), 13), UiUtils.dip2px(App.getApplication().getApplicationContext(), 13));
        Random randomStar = new Random();
        if (this.getChildCount() == 1) {
            for (int i = 0; i < starDrawables.length; i++) {
                ImageView star = new ImageView(getContext());
                int x = randomStar.nextInt(this.getWidth() - 2 * UiUtils.dip2px(App.getApplication().getApplicationContext(), 18)) + UiUtils.dip2px(App.getApplication().getApplicationContext(), 18);
                int y = randomStar.nextInt(this.getHeight()/2 - 2 * UiUtils.dip2px(App.getApplication().getApplicationContext(), 18)) + UiUtils.dip2px(App.getApplication().getApplicationContext(), 18);
                coordinates.put(star, new Coordinates(x, y));
                star.setTranslationX(x);
                star.setTranslationY(y);
                star.setBackgroundDrawable(this.getResources().getDrawable(starDrawables[i]));
                rl_star_container.addView(star, params);
            }
        }
        for (final ImageView imageView : coordinates.keySet()) {
            Coordinates coordinate = coordinates.get(imageView);
            PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", coordinate.getX(), 50);
            PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", coordinate.getY(), 50);
            PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("alpha", 1.3f,0.6f,1.0f,0.6f,0.3F,0.8f,0.5f,0.3F,0.5f);
            PropertyValuesHolder p4 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f,1.1f,0.9f,0.7f,0.5f,0.3f);
            PropertyValuesHolder p5 = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f,1.1f,0.9f,0.7f,0.5f,0.3f);
//            PropertyValuesHolder p6 = PropertyValuesHolder.ofFloat( "Rotation", 0f, 360f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, p1, p2, p3, p4, p5);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    rl_star_container.removeView(imageView);

                }
            });
            animator.setDuration(1500).start();
        }
    }

    /**
     * 星星坐标方位
     */
    class Coordinates {
        private int x;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    @Override
    protected void startLooper(long period) {
        super.startLooper(DURATION_LOOPER);
    }

    public void onEventMainThread(EMsgStarLocal event){
        CustomMsgStar msg = event.getmCustomMsgStar();
        offerModel(msg);
    }

    public void onEventMainThread(EImOnNewMessages event) {
        try {
            String peer = event.msg.getConversationPeer();
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (LiveConstant.CustomMsgType.MSG_STAR == event.msg.getCustomMsgType()) {
                    if(getLiveInfo().getCreaterId().equals(getCurrentUserId())) {
                        CustomMsgStar msg = event.msg.getCustomMsgStar();
                        offerModel(msg);
                    }
                }
            }
        } catch (Exception e) {
            SDToast.showToast(e.toString());
        }
    }

    private String getCurrentUserId(){
        UserModel user = UserModelDao.query();
        if(user != null){
            return user.getUserId();
        }
        return "";
    }

}
