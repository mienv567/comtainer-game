package com.fanwe.live.adapter.viewholder;

import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.span.builder.SDSpannableStringBuilder;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.span.LiveLevelSpan;

/**
 * Created by Administrator on 2016/8/27.
 */
public abstract class MsgViewHolder extends SDRecyclerViewHolder {
    public TextView tv_content;
    protected SDSpannableStringBuilder sb = new SDSpannableStringBuilder();

    protected MsgModel liveMsgModel;
    protected CustomMsg customMsg;
    private boolean isForLrs = false;

    public MsgViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter) {
        super(itemView, adapter);
        tv_content = find(R.id.tv_content);
        if (LiveInformation.getInstance().isCreater()) {
            tv_content.setTextSize(12);
        }
    }

    @Override
    public final void bindData(int position, Object itemModel) {
        liveMsgModel = (MsgModel) itemModel;
        customMsg = liveMsgModel.getCustomMsg();
        sb.clear();
        bindCustomMsg(position, customMsg);
        appendFinish();
    }

    protected abstract void bindCustomMsg(int position, CustomMsg customMsg);

    protected void appendUserInfo(UserModel model) {
        appendUserLevel(model);
        appendUserNickname(model, true, LiveConstant.LiveMsgType.MSG_TEXT);
    }

    protected void appendUserInfo(UserModel model, boolean isForLrs) {
        this.isForLrs = isForLrs;
        if (isForLrs) {
            appendUserNickname(model, true, LiveConstant.LiveMsgType.MSG_TEXT);
        } else {
            appendUserInfo(model);
        }
    }

    protected void appendUserInfoNotFormat(UserModel model) {
        appendUserLevel(model);
        appendUserNickname(model, false, LiveConstant.LiveMsgType.MSG_TEXT);
    }

    protected void appendUserInfoNotFormat(UserModel model, int viewType) {
        appendUserLevel(model);
        appendUserNickname(model, false, viewType);
    }

    /**
     * 添加用户等级
     */
    protected void appendUserLevel(UserModel model) {
        UserModel user = model;
        int levelImageResId = user.getLevelImageResId();
        if (levelImageResId != 0) {
            LiveLevelSpan levelSpan = new LiveLevelSpan(getAdapter().getActivity(), levelImageResId);
            sb.appendSpan(levelSpan, LiveConstant.LEVEL_SPAN_KEY);
        }
    }

    /**
     * 添加用户名
     */
    protected void appendUserNickname(UserModel model, boolean format, int viewType) {
        UserModel user = /*customMsg.getSender()*/model;
        String nickName = " ";
        if (isForLrs) {
            int lrsIndex = LRSManager.getInstance().getUserIndex(model.getUserId());
            if (lrsIndex != -1) {
                nickName += "" + lrsIndex + "号:";
            }
        } else {
            if (format) {
                nickName += user.getNickNameFormat();
            } else {
                nickName += user.getNickName();
            }
        }
        int nickColor = 0;
        if (user.getUserId().equals(LiveInformation.getInstance().getCreaterId())) {
            // 主播
            nickColor = SDResourcesUtil.getColor(R.color.live_username_creater);
        } else {
            if (user.isProUser() || user.isSuperUser()) {
                nickColor = SDResourcesUtil.getColor(R.color.live_username_viewer_high);
            } else {
                nickColor = SDResourcesUtil.getColor(R.color.live_username_viewer);
            }

            switch (viewType) {
                case LiveConstant.LiveMsgType.MSG_LIGHT:
                    nickColor = SDResourcesUtil.getColor(R.color.live_light);
                    break;
                case LiveConstant.LiveMsgType.MSG_SHARE:
                    nickColor = SDResourcesUtil.getColor(R.color.live_share);
                    break;
                case LiveConstant.LiveMsgType.MSG_GIFT_CREATER:
                case LiveConstant.LiveMsgType.MSG_GIFT_VIEWER:
                    nickColor = SDResourcesUtil.getColor(R.color.live_gift);
                    break;
            }
        }
        appendContent(nickName, nickColor);
    }

    protected void setUserInfoClickListener(View view) {
        if (view != null) {
            final UserModel user = customMsg.getSender();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveUserInfoDialog dialog = new LiveUserInfoDialog(getAdapter().getActivity(), user.getUserId());
                    dialog.show();
                }
            });
        }
    }

    /**
     * 添加内容
     *
     * @param content
     * @param textColor
     */
    protected void appendContent(String content, int textColor) {
        if (!TextUtils.isEmpty(content)) {
            if (textColor == 0) {
                textColor = SDResourcesUtil.getColor(R.color.live_msg_text_viewer);
            }
            ForegroundColorSpan textSpan = new ForegroundColorSpan(textColor);
            sb.appendSpan(textSpan, content);
        }
    }

    protected void appendFinish() {
        tv_content.setText(sb);
    }

}
