package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UnReadView extends BaseAppView {

    private TextView tv_unread_number;
    private String showType;
    private String peer=null;
//    public final static String SHOW_TYPE_TOTAL = "show_type_total";    //总未读数
//    public final static String SHOW_TYPE_ONE = "show_type_one";        //某个人的未读数

    public UnReadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UnReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnReadView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_unread;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        tv_unread_number = find(R.id.tv_unread_number);
    }

    public void onEventMainThread(ERefreshMsgUnReaded event) {
        setUnreadMessageModel(event.model);
    }

    public void setUnreadMessageModel(TotalConversationUnreadMessageModel model) {
        if (tv_unread_number != null) {
            SDViewUtil.hide(tv_unread_number);
            if (peer==null){//默认获取总未读数
                if (model != null && model.getTotalUnreadNum() > 0) {
                    showAndSetUnreadNumber(model.getStr_totalUnreadNum());
                }
            }else {//获取某个人的未读数
                if (model != null && model.geUnreadNum(peer) > 0) {
                    showAndSetUnreadNumber(Long.toString(model.geUnreadNum(peer)));
                }
            }
        }
    }

    private void showAndSetUnreadNumber(String unreadNum) {
        SDViewUtil.show(tv_unread_number);
        tv_unread_number.setText(unreadNum);
    }

    /**
     * 默认获取总未读数；传入该用户的peer获取这个用户的未读数
     * @param peer  该用户的peer
     */
    public void setOneUnreadNumber(String peer) {
        this.peer = peer;
    }
}
