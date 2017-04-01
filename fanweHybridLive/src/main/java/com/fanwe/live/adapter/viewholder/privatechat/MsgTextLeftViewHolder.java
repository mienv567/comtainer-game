package com.fanwe.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.span.builder.SDSpannableStringBuilder;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.dialog.PrivateChatLongClickMenuDialog;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateText;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgTextLeftViewHolder extends PrivateChatViewHolder
{

    private TextView tv_text;
    private LinearLayout ll_content;
    private SDSpannableStringBuilder sb = new SDSpannableStringBuilder();

    public MsgTextLeftViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);

        tv_text = find(R.id.tv_text);
        ll_content = find(R.id.ll_content);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        final CustomMsgPrivateText msg = (CustomMsgPrivateText) customMsg;

        ll_content.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                final PrivateChatLongClickMenuDialog dialog = new PrivateChatLongClickMenuDialog(getAdapter().getActivity());
                dialog.setItems(SDResourcesUtil.getString(R.string.copy));
                dialog.setItemClickListener(new SDAdapter.ItemClickListener<String>()
                {
                    @Override
                    public void onClick(int position, String item, View view)
                    {
                        SDOtherUtil.copyText(msg.getText());
                        SDToast.showToast(SDResourcesUtil.getString(R.string.already_copy));
                        dialog.dismiss();
                    }
                });
                SDViewUtil.showDialogTopCenter(dialog, v, 10, 0);
                return false;
            }
        });

        // 文字
        sb.clear();
        sb.append(msg.getText());
        tv_text.setText(sb);
    }
}
