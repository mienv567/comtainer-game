package com.fanwe.live.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.util.helper.StoryProtocol;
import com.kakao.util.helper.TalkProtocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yuan on 2017/3/11.
 * 邮箱：44004606@qq.com
 */

public class KakaoLoginButton extends FrameLayout {

    public KakaoLoginButton(Context context) {
        super(context);
    }

    public KakaoLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KakaoLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            public void onClick(View view) {
                List authTypes = getAuthTypes();
                if(authTypes.size() == 1) {
                    Session.getCurrentSession().open((AuthType)authTypes.get(0), (Activity)getContext());
                } else {
                    onClickLoginButton(authTypes);
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<AuthType> getAuthTypes() {
        ArrayList availableAuthTypes = new ArrayList();
        if(TalkProtocol.existCapriLoginActivityInTalk(this.getContext(), Session.getCurrentSession().isProjectLogin())) {
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            availableAuthTypes.add(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN);
        }

        if(StoryProtocol.existCapriLoginActivityInStory(this.getContext(), Session.getCurrentSession().isProjectLogin())) {
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }

        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        AuthType[] selectedAuthTypes = Session.getCurrentSession().getAuthTypes();
        availableAuthTypes.retainAll(Arrays.asList(selectedAuthTypes));
        if(availableAuthTypes.size() == 0) {
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }

        return availableAuthTypes;
    }

    @SuppressWarnings("unchecked")
    private void onClickLoginButton(List<AuthType> authTypes) {
        final ArrayList itemList = new ArrayList();
        if(authTypes.contains(AuthType.KAKAO_TALK)) {
            itemList.add(new Item(com.kakao.usermgmt.R.string.com_kakao_kakaotalk_account, com.kakao.usermgmt.R.drawable.kakaotalk_icon, com.kakao.usermgmt.R.string.com_kakao_kakaotalk_account_tts, AuthType.KAKAO_TALK));
        }

        if(authTypes.contains(AuthType.KAKAO_STORY)) {
            itemList.add(new Item(com.kakao.usermgmt.R.string.com_kakao_kakaostory_account, com.kakao.usermgmt.R.drawable.kakaostory_icon, com.kakao.usermgmt.R.string.com_kakao_kakaostory_account_tts, AuthType.KAKAO_STORY));
        }

        if(authTypes.contains(AuthType.KAKAO_ACCOUNT)) {
            itemList.add(new Item(com.kakao.usermgmt.R.string.com_kakao_other_kakaoaccount, com.kakao.usermgmt.R.drawable.kakaoaccount_icon, com.kakao.usermgmt.R.string.com_kakao_other_kakaoaccount_tts, AuthType.KAKAO_ACCOUNT));
        }

        itemList.add(new Item(com.kakao.usermgmt.R.string.com_kakao_account_cancel, 0, com.kakao.usermgmt.R.string.com_kakao_account_cancel_tts, null));
        final Item[] items = (Item[])itemList.toArray(new Item[itemList.size()]);
        ArrayAdapter adapter = new ArrayAdapter(
                getContext(), android.R.layout.select_dialog_item,
                android.R.id.text1, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setText(items[position].textId);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(15.0F);
                tv.setGravity(Gravity.CENTER);
                tv.setContentDescription(this.getContext().getString(items[position].contentDescId));
                if(position == itemList.size() - 1) {
                    tv.setBackgroundResource(com.kakao.usermgmt.R.drawable.kakao_cancel_button_background);
                } else {
                    tv.setBackgroundResource(com.kakao.usermgmt.R.drawable.kakao_account_button_background);
                }

                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
                int dp5 = (int)(5.0F * KakaoLoginButton.this.getResources().getDisplayMetrics().density + 0.5F);
                tv.setCompoundDrawablePadding(dp5);
                return v;
            }
        };
        (new AlertDialog.Builder(this.getContext())).setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                AuthType authType = items[position].authType;
                if(authType != null) {
                    Session.getCurrentSession().open(authType, (Activity)KakaoLoginButton.this.getContext());
                }

                dialog.dismiss();
            }
        }).create().show();
    }

    private static class Item {
        public final int textId;
        public final int icon;
        public final int contentDescId;
        public final AuthType authType;

        public Item(int textId, Integer icon, int contentDescId, AuthType authType) {
            this.textId = textId;
            this.icon = icon;
            this.contentDescId = contentDescId;
            this.authType = authType;
        }
    }
}
