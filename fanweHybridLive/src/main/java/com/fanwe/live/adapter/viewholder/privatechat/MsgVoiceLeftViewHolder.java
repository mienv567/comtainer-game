package com.fanwe.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.media.player.SDMediaPlayer;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateVoice;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgVoiceLeftViewHolder extends PrivateChatViewHolder
{
    private TextView tv_duration;
    private LinearLayout ll_voice;
    private ImageView iv_play_voice;

    public MsgVoiceLeftViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
        tv_duration = find(R.id.tv_duration);
        ll_voice = find(R.id.ll_voice);
        iv_play_voice = find(R.id.iv_play_voice);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        final CustomMsgPrivateVoice msg = (CustomMsgPrivateVoice) customMsg;
        final String path = msg.getPath();

        if (SDMediaPlayer.getInstance().isPlayingAudioFile(path))
        {
            SDViewUtil.startAnimationDrawable(iv_play_voice.getDrawable());
        } else
        {
            SDViewUtil.stopAnimationDrawable(iv_play_voice.getDrawable());
        }

        tv_duration.setText(msg.getDurationFormat());
        SDViewUtil.setViewWidth(ll_voice, msg.getViewWidth());
        ll_voice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SDMediaPlayer.getInstance().performPlayAudioFile(path);
            }
        });
    }
}
