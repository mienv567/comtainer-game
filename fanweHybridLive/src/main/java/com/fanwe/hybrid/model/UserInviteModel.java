package com.fanwe.hybrid.model;

import com.fanwe.live.model.RoomShareModel;

/**
 * 用户邀请
 */
public class UserInviteModel extends BaseActModel{
    private int has_invited_num;//已邀请人数
    private String invite_code;//我的邀请码
    private String bind_invite_code;//我已经绑定的邀请码
    private String bind_invite_user_id;//邀请我的好友id
    private String bind_invite_user_name;//邀请我的好友昵称
    private RoomShareModel share; // 分享内容
    public String getBind_invite_code() {
        return bind_invite_code;
    }

    public void setBind_invite_code(String bind_invite_code) {
        this.bind_invite_code = bind_invite_code;
    }

    public String getBind_invite_user_id() {
        return bind_invite_user_id;
    }

    public void setBind_invite_user_id(String bind_invite_user_id) {
        this.bind_invite_user_id = bind_invite_user_id;
    }

    public String getBind_invite_user_name() {
        return bind_invite_user_name;
    }

    public void setBind_invite_user_name(String bind_invite_user_name) {
        this.bind_invite_user_name = bind_invite_user_name;
    }

    public int getHas_invited_num() {
        return has_invited_num;
    }

    public void setHas_invited_num(int has_invited_num) {
        this.has_invited_num = has_invited_num;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public RoomShareModel getShare() {
        return share;
    }

    public void setShare(RoomShareModel share) {
        this.share = share;
    }
}
