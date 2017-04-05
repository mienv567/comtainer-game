package com.fanwe.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.ELRSChannelChange;
import com.fanwe.live.model.App_pop_msgActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.fanwe.live.view.SDSlidingButton;
import com.fanwe.live.view.SDSlidingButton.SelectedChangeListener;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

public class RoomSendMsgView extends RoomView {
    public RoomSendMsgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomSendMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomSendMsgView(Context context) {
        super(context);
    }

    private SDSlidingButton sl_btn;
    private ImageView iv_pop_msg_handle;
    private EditText et_content;
    private TextView tv_send;

    private int popMsgDiamonds = 1;
    private String strContent;
    private boolean isPopMsg = false;
    private String mGroupId;
    private int mCurrentChannelIndex = -1; //狼人杀的游戏频道下标 0-公共 1-狼人 2-进程（不可发消息） 3-规则（不可发消息）

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_send_msg;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        sl_btn = find(R.id.sl_btn);
        iv_pop_msg_handle = find(R.id.iv_pop_msg_handle);
        et_content = find(R.id.et_content);
        tv_send = find(R.id.tv_send);

        invisible();
        register();
    }

    private void register() {
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            popMsgDiamonds = model.getBullet_screen_diamond();
        }

        initSDSlidingButton();

        et_content.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TANetWorkUtil.isNetworkAvailable(getActivity())) {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.no_internet));
                    return;
                }

                strContent = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(strContent)) {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.please_input_content));
                    return;
                }

                if (strContent.length() > 38) {
                    strContent = strContent.substring(0, 38);
                }
                sendMessage();
            }
        });
    }

    private void initSDSlidingButton() {
        sl_btn.setSelectedChangeListener(new SelectedChangeListener() {
            @Override
            public void onSelectedChange(SDSlidingButton view, boolean selected) {
                if (selected) {
                    iv_pop_msg_handle.setImageResource(R.drawable.bg_live_pop_msg_handle_normal);
                    if (getLiveInfo().isCreater()) {
                        et_content.setHint(SDResourcesUtil.getString(R.string.live_send_msg_hint_normal));
                    } else {
                        et_content.setHint(SDResourcesUtil.getString(R.string.open_big_mouth) + popMsgDiamonds + SDResourcesUtil.getString(R.string.big_mouth_cost));
                    }
                    isPopMsg = true;
                } else {
                    iv_pop_msg_handle.setImageResource(R.drawable.bg_live_pop_msg_handle_normal);
                    et_content.setHint(SDResourcesUtil.getString(R.string.live_send_msg_hint_normal));
                    isPopMsg = false;
                }
            }
        });
    }

    public void setContent(String content) {
        if (content == null) {
            content = "";
        }
        et_content.setText(content);
        et_content.setSelection(et_content.getText().length());
        SDViewUtil.showInputMethod(et_content, 100);
    }

    protected void sendMessage() {
        if (isPopMsg) {
            AppRequestParams params = CommonInterface.requestPopMsgParams(getLiveInfo().getRoomId(), strContent,getLiveInfo().getGroupId());
            AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_msgActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (rootModel.isOk()) {
                        if (!getLiveInfo().isCreater()) {
                            // 扣费
                            final UserModel user = UserModelDao.query();
                            if (user != null) {
                                user.pay(popMsgDiamonds);
                                UserModelDao.insertOrUpdate(user);
                            }
                        }
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    CommonInterface.requestMyUserInfoJava(null);
                }
            });
        } else {
            String groupId = getLiveInfo().getGroupId();
            if (!TextUtils.isEmpty(mGroupId)) {
                groupId = mGroupId;
            }
            if (TextUtils.isEmpty(groupId)) {
                return;
            }

            final CustomMsgText msg = new CustomMsgText();
            msg.setText(strContent);
            if (sl_btn.getVisibility() == View.GONE) {
                UserModel model = UserModelDao.query();
                if (model != null) {//如果是游戏频道 判断当前用户是否为玩家 如果不是则不允许发言
                    if (LRSManager.getInstance().mIsGaming) {
                        if (!LRSManager.getInstance().userIsGamer(model.getUserId())) {
                            SDToast.showToast("非游戏玩家不可以发言");
                            return;
                        }
                    }
                }
                if (mCurrentChannelIndex != 0 && mCurrentChannelIndex != 1) { //如果是游戏频道 判断当前频道是否可以发言
                    SDToast.showToast("当前频道不可以发言");
                    return;
                }
                if (mCurrentChannelIndex == 0) { //游戏公频的发言限制
                    if (LRSManager.getInstance().mIsDark) {
                        SDToast.showToast("现在是天黑时间，不能发言");
                        return;
                    }
                    if (!LRSManager.getInstance().isSelfALive() && !LRSManager.getInstance().mIsSelfLastWords) {
                        SDToast.showToast("您已出局，不能在此频道发言");
                        return;
                    }
                }
            }

            IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
                }

                @Override
                public void onError(int code, String desc) {
                    if (code == 80001) {
                        SDToast.showToast(getResources().getString(R.string.live_dirty));
                    }
                }
            });
        }
        et_content.setText("");
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev) {
        invisible();
        return true;
    }

    @Override
    public boolean onBackPressed() {
        invisible();
        return true;
    }

    @Override
    public void onInvisible(View view) {
        SDViewUtil.hideInputMethod(et_content);
        super.onInvisible(view);
    }

    @Override
    public void onVisible(View view) {
        SDViewUtil.showInputMethod(et_content);
        super.onVisible(view);
    }

    public void onEventMainThread(ELRSChannelChange event) {
        if (event.getIsLiveGroup()) {
            mGroupId = getLiveInfo().getGroupId();
            SDViewUtil.show(sl_btn);
        } else {
            mGroupId = event.getCurrentGroupId();
            isPopMsg = false;
            SDViewUtil.hide(sl_btn);
        }
        mCurrentChannelIndex = event.getChannelIndex();
    }

}
