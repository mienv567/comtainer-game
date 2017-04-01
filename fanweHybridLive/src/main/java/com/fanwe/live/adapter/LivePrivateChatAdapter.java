package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.media.player.SDMediaPlayer;
import com.fanwe.library.span.builder.SDSpannableStringBuilder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant.PrivateMsgType;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.fanwe.live.model.custommsg.CustomMsgPrivateVoice;
import com.fanwe.live.model.custommsg.PrivateMsgModel;

import java.util.List;

//SDRecyclerAdapter
public class LivePrivateChatAdapter extends SDSimpleAdapter<PrivateMsgModel>
{

    private ClickListener clickListener;

    public LivePrivateChatAdapter(List<PrivateMsgModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount()
    {
        return 8;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        int layoutId = 0;
        // 判断消息类型
        switch (getItemViewType(position))
        {
            case PrivateMsgType.MSG_TEXT_LEFT:
                layoutId = R.layout.item_private_chat_text_left;
                break;
            case PrivateMsgType.MSG_TEXT_RIGHT:
                layoutId = R.layout.item_private_chat_text_right;
                break;
            case PrivateMsgType.MSG_VOICE_LEFT:
                layoutId = R.layout.item_private_chat_voice_left;
                break;
            case PrivateMsgType.MSG_VOICE_RIGHT:
                layoutId = R.layout.item_private_chat_voice_right;
                break;
            case PrivateMsgType.MSG_IMAGE_LEFT:
                layoutId = R.layout.item_private_chat_image_left;
                break;
            case PrivateMsgType.MSG_IMAGE_RIGHT:
                layoutId = R.layout.item_private_chat_image_right;
                break;
            case PrivateMsgType.MSG_GIFT_LEFT:
                layoutId = R.layout.item_private_chat_gift_left;
                break;
            case PrivateMsgType.MSG_GIFT_RIGHT:
                layoutId = R.layout.item_private_chat_gift_right;
                break;
            default:
                break;
        }
        return layoutId;
    }

    @Override
    public void bindData(int position, final View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        switch (getItemViewType(position))
        {
            case PrivateMsgType.MSG_TEXT_LEFT:
                bindDataTextLeft(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_TEXT_RIGHT:
                bindDataTextRight(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_VOICE_LEFT:
                bindDataVoiceLeft(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_VOICE_RIGHT:
                bindDataVoiceRight(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_IMAGE_LEFT:
                bindDataImageLeft(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_IMAGE_RIGHT:
                bindDataImageRight(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_GIFT_LEFT:
                bindDataGiftLeft(position, convertView, parent, model);
                break;
            case PrivateMsgType.MSG_GIFT_RIGHT:
                bindDataGiftRight(position, convertView, parent, model);
                break;
            default:
                break;
        }
    }

    /**
     * 礼物消息
     */
    private void bindDataGiftLeft(int position, View convertView, ViewGroup parent, PrivateMsgModel model)
    {
        bindHeadImage(convertView, model);

        ImageView iv_gift = get(R.id.iv_gift, convertView);
        TextView tv_msg = get(R.id.tv_msg, convertView);

        CustomMsgPrivateGift msg = model.getMsg().getCustomMsgPrivateGift();

        // 图片
        String uri = msg.getProp_icon();
        SDViewBinder.setImageView(getActivity(),uri, iv_gift);

        SDViewBinder.setTextView(tv_msg, msg.getTo_msg());
    }

    private void bindDataGiftRight(int position, View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        bindHeadImage(convertView, model);
        ImageView iv_gift = get(R.id.iv_gift, convertView);
        TextView tv_msg = get(R.id.tv_msg, convertView);
        TextView tv_score = get(R.id.tv_score, convertView);

        CustomMsgPrivateGift msg = model.getMsg().getCustomMsgPrivateGift();

        // 图片
        String uri = msg.getProp_icon();
        SDViewBinder.setImageView(getActivity(),uri, iv_gift);

        SDViewBinder.setTextView(tv_msg, msg.getFrom_msg());
        SDViewBinder.setTextView(tv_score, msg.getFrom_score());


        bindStatus(convertView, model);
    }

    /**
     * 图片消息
     */
    private void bindDataImageLeft(int position, View convertView, ViewGroup parent, PrivateMsgModel model)
    {
        bindHeadImage(convertView, model);

        ImageView iv_image = get(R.id.iv_image, convertView);

        // 图片
        String uri = model.getMsg().getCustomMsgPrivateImage().getAvailableUri();
        SDViewBinder.setImageView(getActivity(),uri, iv_image);
    }

    private void bindDataImageRight(int position, View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        bindDataImageLeft(position, convertView, parent, model);
        bindStatus(convertView, model);
    }

    /**
     * 语音消息
     */
    private void bindDataVoiceLeft(int position, View convertView, ViewGroup parent, PrivateMsgModel model)
    {
        bindHeadImage(convertView, model);

        TextView tv_duration = get(R.id.tv_duration, convertView);
        LinearLayout ll_voice = get(R.id.ll_voice, convertView);
        ImageView iv_play_voice = get(R.id.iv_play_voice, convertView);

        final CustomMsgPrivateVoice customMsg = model.getMsg().getCustomMsgPrivateVoice();
        final String path = customMsg.getPath();

        if (SDMediaPlayer.getInstance().isPlayingAudioFile(path))
        {
            SDViewUtil.startAnimationDrawable(iv_play_voice.getDrawable());
        } else
        {
            SDViewUtil.stopAnimationDrawable(iv_play_voice.getDrawable());
        }

        tv_duration.setText(customMsg.getDurationFormat());
        SDViewUtil.setViewWidth(ll_voice, customMsg.getViewWidth());
        ll_voice.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SDMediaPlayer.getInstance().performPlayAudioFile(path);
            }
        });
    }

    private void bindDataVoiceRight(int position, View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        bindDataVoiceLeft(position, convertView, parent, model);
        bindStatus(convertView, model);
    }

    /**
     * 文字消息
     */
    private void bindDataTextLeft(int position, View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        bindHeadImage(convertView, model);

        TextView tv_text = get(R.id.tv_text, convertView);
        LinearLayout ll_content = get(R.id.ll_content, convertView);

        ll_content.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (clickListener != null)
                {
                    clickListener.onLongClick(model, v);
                }
                return false;
            }
        });

        // 文字
        String text = model.getMsg().getCustomMsgPrivateText().getText();
        SDSpannableStringBuilder sb = new SDSpannableStringBuilder();
        sb.append(text);
        tv_text.setText(sb);
    }


    private void bindDataTextRight(int position, View convertView, ViewGroup parent, final PrivateMsgModel model)
    {
        bindDataTextLeft(position, convertView, parent, model);
        bindStatus(convertView, model);
    }

    private void bindHeadImage(View convertView, final PrivateMsgModel model)
    {
        ImageView iv_head_img = get(R.id.iv_head_img, convertView);
        // 头像
        iv_head_img.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (clickListener != null)
                {
                    clickListener.onClickHeadImage(model);
                }
            }
        });
        SDViewBinder.setImageView(getActivity(),model.getMsg().getCustomMsg().getSender().getHeadImage(), iv_head_img,R.drawable.ic_default_head);
    }

    private void bindStatus(View convertView, final PrivateMsgModel model)
    {
        ImageView iv_resend = get(R.id.iv_resend, convertView);
        ProgressBar pb_sending = get(R.id.pb_sending, convertView);

        // 重新发送
        iv_resend.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (clickListener != null)
                {
                    clickListener.onClickResend(model);
                }
            }
        });

        switch (model.getMsg().getStatus())
        {
            case Sending:
                SDViewUtil.show(pb_sending);
                SDViewUtil.hide(iv_resend);
                break;
            case SendSuccess:
                SDViewUtil.hide(iv_resend);
                SDViewUtil.hide(pb_sending);
                break;
            case SendFail:
                SDViewUtil.show(iv_resend);
                SDViewUtil.hide(pb_sending);
                break;
            default:
                SDViewUtil.hide(iv_resend);
                SDViewUtil.hide(pb_sending);
                break;
        }
    }

    public interface ClickListener
    {
        void onClickResend(PrivateMsgModel model);

        void onClickHeadImage(PrivateMsgModel model);

        void onLongClick(PrivateMsgModel model, View v);
    }
}
