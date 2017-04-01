package com.qy.ysys.yishengyishi.httputils;

import com.qy.ysys.yishengyishi.model.BaseModel;
import com.qy.ysys.yishengyishi.model.FamilyMemberModel;
import com.qy.ysys.yishengyishi.model.GetMyInviteCodeModel;
import com.qy.ysys.yishengyishi.model.MemoirInfoModel;
import com.qy.ysys.yishengyishi.model.MemoirListModel;
import com.qy.ysys.yishengyishi.model.ModelAddNodeCallBack;
import com.qy.ysys.yishengyishi.model.ModelCreateTreeCallBack;
import com.qy.ysys.yishengyishi.model.ModelFitInfo;
import com.qy.ysys.yishengyishi.model.ModelLogin;
import com.qy.ysys.yishengyishi.model.ModelSendMsg;
import com.qy.ysys.yishengyishi.model.ModelShowFamilyTree;
import com.qy.ysys.yishengyishi.model.SubmitInviteCodeModel;
import com.qy.ysys.yishengyishi.model.UserModel;
import com.qy.ysys.yishengyishi.model.FamilyDataListModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 作者：tracy.lee on 2017/1/19 0019 10:41
 */
public class RequestInterface {
    public final static int PAGE_SIZE = 10;
    public static final int MEMOIR_FM_TYPE_DEFAULT = 0;//默认分类
    public static final int MEMOIR_TYPE_GALLERY = 1; //相册
    public static final int MEMOIR_IS_FAMILY_ZONE = 0;//不同步到朋友圈

    private static CommonInterface getHttpService() {
        return HttpHelper.getInstant().getService();
    }

    /**
     * 请求手机登录
     *
     * @param phone
     * @param verifyCode
     * @param callback
     */
    public static void requestMobileLogin(String phone, String verifyCode, Callback<ModelLogin> callback) {
        Call<ModelLogin> callLogin = getHttpService().login(phone, verifyCode);
        callLogin.enqueue(callback);
    }

    /**
     * 创建树成员
     *
     * @param uid
     * @param type
     * @param callback
     */
    public static void addTreeMember(int uid, int type, Callback<ModelCreateTreeCallBack> callback) {
        Call<ModelCreateTreeCallBack> modelCreateTreeCallBackCall = getHttpService().creatTree(uid, 0);
        modelCreateTreeCallBackCall.enqueue(callback);
    }

    /**
     * 添加节点
     *
     * @param name
     * @param rank
     * @param phone
     * @param gender
     * @param parentGender
     * @param relationshipId
     * @param isPartner
     * @param uid
     * @param callback
     */
    public static void addNode(String name, int rank, String phone, int gender, int parentGender, int relationshipId, int isPartner,
                               int uid, Callback<ModelAddNodeCallBack> callback) {
        Call<ModelAddNodeCallBack> call = getHttpService().addNode(name, rank, phone, gender, parentGender, relationshipId,
                isPartner, uid);
        call.enqueue(callback);
    }

    /**
     * 删除节点
     *
     * @param type
     * @param uuid
     * @param uid
     * @param callback
     */
    public static void removeNode(int type, String uuid, int uid, Callback<ModelAddNodeCallBack> callback) {
        Call<ModelAddNodeCallBack> modelAddNodeCallBackCall = getHttpService().removeNode(type, uuid, uid);
        modelAddNodeCallBackCall.enqueue(callback);
    }

    /**
     * 修改节点
     *
     * @param name
     * @param type
     * @param phone
     * @param uuid
     * @param uid
     * @param callback
     */
    public static void updateNode(String name, int type, String phone, String uuid, int uid, Callback<ModelAddNodeCallBack> callback) {
        Call<ModelAddNodeCallBack> modelAddNodeCallBackCall = getHttpService().updateNode(name, type, phone, uuid, uid);
        modelAddNodeCallBackCall.enqueue(callback);
    }

    /**
     * 提交邀请码
     *
     * @param inviteCode
     * @param callback
     */
    public static void submitInviteCode(String inviteCode, Callback<SubmitInviteCodeModel> callback) {
        Call<SubmitInviteCodeModel> callSubmit = getHttpService().requestSubmitInviteCode(inviteCode);
        callSubmit.enqueue(callback);
    }

    /**
     * 获得我的邀请码
     *
     * @param callback
     */
    public static void getMyInviteCode(Callback<GetMyInviteCodeModel> callback) {
        Call<GetMyInviteCodeModel> call = getHttpService().getMyInviteCode();
        call.enqueue(callback);
    }

    /**
     * 发送短信
     *
     * @param phone
     * @param purpose
     * @param callback
     */
    public static void sendPhoneMsg(String phone, String purpose, Callback<ModelSendMsg> callback) {
        Call<ModelSendMsg> call = getHttpService().sendMsg(phone, purpose);
        call.enqueue(callback);
    }

    /**
     * 补充个性信息
     *
     * @param name
     * @param sex
     * @param phone
     * @param callback
     */
    public static void fillInfo(String name, int sex, String phone, Callback<ModelFitInfo> callback) {
        Call<ModelFitInfo> call = getHttpService().fillInfo(name, sex, phone);
        call.enqueue(callback);
    }

    /**
     * 显示家族树
     *
     * @param uid
     * @param type
     * @param callback
     */
    public static void showTree(int uid, int type, Callback<ModelShowFamilyTree> callback) {
        Call<ModelShowFamilyTree> call = getHttpService().showTree(uid, type);
        call.enqueue(callback);
    }

    /**
     * 获得用户信息
     *
     * @param callback
     */
    public static void getUserInfo(Callback<UserModel> callback) {
        Call<UserModel> call = getHttpService().requestUserInfo();
        call.enqueue(callback);
    }

    /**
     * 上传图片到朋友圈
     *
     * @param content
     * @param place
     * @param happenTime
     * @param video
     * @param publishType
     * @param privateUserIds
     * @param isAddToMemoirs
     * @param filePaths
     * @param callback
     */
    public static void publicFamilyZonePhoto(String content, String place, long happenTime, String video,
                                             int publishType, String privateUserIds, int isAddToMemoirs,
                                             ArrayList<String> filePaths, Callback<BaseModel> callback) {
        Map<String, RequestBody> bodyMap = new HashMap<>();
        Call<BaseModel> call;
        if (filePaths != null && filePaths.size() > 0) {
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                bodyMap.put("images" + "\";filename=\"" + file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
            call = getHttpService().publicFamilyZone(content, place, happenTime, video, publishType, privateUserIds, isAddToMemoirs, bodyMap);
        } else {
            call = getHttpService().publicFamilyZone(content, place, happenTime, video, publishType, privateUserIds, isAddToMemoirs);
        }
        call.enqueue(callback);
    }

    /**
     * 获得家族圈列表
     *
     * @param page
     * @param pageSize
     * @param callback
     */
    public static void queryFamilyList(int page, int pageSize, Callback<FamilyDataListModel> callback) {
        Call<FamilyDataListModel> call = getHttpService().queryFamilyList(page, pageSize);
        call.enqueue(callback);
    }

    /**
     * 获得家庭成员
     *
     * @param callback
     */
    public static void getFamilyMembers(Callback<FamilyMemberModel> callback) {
        Call<FamilyMemberModel> call = getHttpService().getFamilyMembers();
        call.enqueue(callback);
    }

    /**
     * 点赞
     *
     * @param zoneId
     * @param callback
     */
    public static void praiseFamilyZone(int zoneId, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().praiseFamilyZone(zoneId);
        call.enqueue(callback);
    }

    /**
     * 删除点赞
     *
     * @param praiseId
     * @param callback
     */
    public static void deletePraiseFamilyZone(int praiseId, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().deletePraiseFamilyZone(praiseId);
        call.enqueue(callback);
    }

    /**
     * 发评论
     *
     * @param zoneId
     * @param replyUserId
     * @param comment
     * @param callback
     */
    public static void publishComment(int zoneId, int replyUserId, String comment, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().publishComment(zoneId, replyUserId, comment);
        call.enqueue(callback);
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @param callback
     */
    public static void deleteComment(int commentId, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().deleteComment(commentId);
        call.enqueue(callback);
    }

    /**
     * 删除朋友圈记录
     *
     * @param zoneId
     * @param callback
     */
    public static void deleteFamilyZone(int zoneId, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().deleteFamilyZone(zoneId);
        call.enqueue(callback);
    }

    /**
     * 创建回忆录
     *
     * @param fmName
     * @param remark
     * @param happenTime
     * @param place
     * @param fmType
     * @param type
     * @param content
     * @param callback
     */
    public static void createMemoir(String fmName, String remark, long happenTime, String place,
                                    int fmType, int type, String content, Callback<BaseModel> callback) {
        Call<BaseModel> call = getHttpService().createMemoir(fmName, remark, happenTime, place,
                fmType, type, content);
        call.enqueue(callback);
    }

    /**
     * 上传回忆录图片
     *
     * @param fmId
     * @param isFamilyZone
     * @param filePaths
     * @param callback
     */
    public static void uploadMemoir(int fmId, int isFamilyZone, ArrayList<String> filePaths, Callback<BaseModel> callback) {
        Map<String, RequestBody> bodyMap = new HashMap<>();
        if (filePaths != null && filePaths.size() > 0) {
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                bodyMap.put("images" + "\";filename=\"" + file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }
        Call<BaseModel> call = getHttpService().uploadMemoir(fmId, isFamilyZone, bodyMap);
        call.enqueue(callback);
    }

    /**
     * 查询回忆录列表
     *
     * @param page
     * @param pageSize
     * @param fmType
     * @param callback
     */
    public static void queryFamilyMemoirList(int page, int pageSize, int fmType, Callback<MemoirListModel> callback) {
        Call<MemoirListModel> call = getHttpService().familyMemoirList(page, pageSize, fmType);
        call.enqueue(callback);
    }

    /**
     * 查询回忆录详情
     *
     * @param fmId
     * @param callback
     */
    public static void queryFamilyMemoirInfo(int fmId, Callback<MemoirInfoModel> callback) {
        Call<MemoirInfoModel> call = getHttpService().familyMemoirInfo(fmId);
        call.enqueue(callback);
    }

}
