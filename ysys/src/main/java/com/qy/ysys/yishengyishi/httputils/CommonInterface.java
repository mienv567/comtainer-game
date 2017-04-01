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

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


/**
 * Created by tony.chen on 2017/1/8.
 */

public interface CommonInterface {
    //    verification/send?phone=17086892121&purpose=1
    @POST("verification/send")
    Call<ModelSendMsg> sendMsg(@Query("phone") String phone, @Query("purpose") String purpose); // 发送验证码

    //    http://localhost:8080/1314-web/user/login?phone=17086892121&code=000055
    @POST("user/login")
    Call<ModelLogin> login(@Query("phone") String phone, @Query("code") String code); // 发送验证码

    //    http://localhost:8080/1314-web/user/fill?name=游侠&sex=0&phone=17086892121
    @POST("user/fill")
    Call<ModelFitInfo> fillInfo(@Query("name") String name, @Query("sex") int sex, @Query("phone") String phone); // 完善账号信息

    //    http://localhost:8080/1314-web/family/show?uid=7&type=0
    @POST("family/show")
    Call<ModelShowFamilyTree> showTree(@Query("uid") int uid, @Query("type") int type); // 显示家族树

    //    http://localhost:8080/1314-web/family/createTree?uid=7&type=0
    @POST("family/createTree")
    Call<ModelCreateTreeCallBack> creatTree(@Query("uid") int uid, @Query("type") int type); // 创建家族树

    //    http://localhost:8080/1314-web/family/addNode?name=凤凰女&rank=-1&phone=13115122265&gender=1&parentGender=0&relationshipId=14&isPartner=0&uid=7

    /**
     * @param name
     * @param rank           // 相对于自己的层级关系
     * @param phone
     * @param gender         // 性别
     * @param parentGender   // 父节点的性别
     * @param relationshipId // 对应后端的position
     * @param isPartner      // 暂时写死为0
     * @param uid
     * @return
     */

    @POST("family/addNode")
    Call<ModelAddNodeCallBack> addNode(@Query("name") String name, @Query("rank") int rank,
                                       @Query("phone") String phone, @Query("gender") int gender,
                                       @Query("parentGender") int parentGender,
                                       @Query("relationshipId") int relationshipId,
                                       @Query("isPartner") int isPartner,
                                       @Query("uid") int uid); // 增加节点


    /**
     * 更新节点信息
     *
     * @param name
     * @param type
     * @param phone
     * @param uuid
     * @param uid
     * @return
     */
    //    http://localhost:8080/1314-web/family/updateNode?name=美女&type=0&phone=13789522265&uuid=27e5919b2b64458b9a6d5e825752d06d&uid=7
    @POST("family/updateNode")
    Call<ModelAddNodeCallBack> updateNode(@Query("name") String name, @Query("type") int type,
                                          @Query("phone") String phone,
                                          @Query("uuid") String uuid,
                                          @Query("uid") int uid); // 更新节点信息


    /**
     * 删除节点信息
     *
     * @param type
     * @param uuid
     * @param uid
     * @return
     */
//    http://localhost:8080/1314-web/family/removeNode?type=0&uuid=27e5919b2b64458b9a6d5e825752d06d&uid=7
    @POST("family/removeNode")
    Call<ModelAddNodeCallBack> removeNode(@Query("type") int type,
                                          @Query("uuid") String uuid,
                                          @Query("uid") int uid); // 更新节点信息

    @POST("invatationCode/myCode")
    Call<GetMyInviteCodeModel> getMyInviteCode();

    @POST("invatationCode/inputCode")
    Call<SubmitInviteCodeModel> requestSubmitInviteCode(@Query("invitationCode") String invitationCode);

    @POST("user/getUserInfo")
    Call<UserModel> requestUserInfo();

    @Multipart
    @POST("boxZone/publish")
    Call<BaseModel> publicFamilyZone(@Query("content") String content,
                                     @Query("place") String place,
                                     @Query("happenTime") long happenTime,
                                     @Query("video") String video,
                                     @Query("publishType") int publishType,
                                     @Query("privateUserIds") String privateUserIds,
                                     @Query("isAddToMemoirs") int isAddToMemoirs,
                                     @PartMap Map<String, RequestBody> params);

    @POST("boxZone/publish")
    Call<BaseModel> publicFamilyZone(@Query("content") String content,
                                     @Query("place") String place,
                                     @Query("happenTime") long happenTime,
                                     @Query("video") String video,
                                     @Query("publishType") int publishType,
                                     @Query("privateUserIds") String privateUserIds,
                                     @Query("isAddToMemoirs") int isAddToMemoirs);

    @POST("boxZone/delete")
    Call<BaseModel> deleteFamilyZone(@Query("zoneId") int zoneId);

    @POST("familyZone/praise")
    Call<BaseModel> praiseFamilyZone(@Query("zoneId") int zoneId);

    @POST("familyZone/deletePraise")
    Call<BaseModel> deletePraiseFamilyZone(@Query("praiseId") int praiseId);

    @POST("boxZone/comment")
    Call<BaseModel> publishComment(@Query("zoneId") int zoneId,@Query("replyUserId") int replyUserId,
                                   @Query("comment") String comment);

    @POST("boxZone/deleteComment")
    Call<BaseModel> deleteComment(@Query("commentId") int commentId);

    @POST("familyZone/members")
    Call<FamilyMemberModel> getFamilyMembers();

    @POST("boxZone/queryList")
    Call<FamilyDataListModel> queryFamilyList(@Query("page") int page, @Query("pageSize") int pageSize);


    @POST("familyMemoir/createMemoir")
    Call<BaseModel> createMemoir(@Query("fmName") String fmName,@Query("remark") String remark,
                                 @Query("happenTime") long happenTime,@Query("place") String place,
                                 @Query("fmType") int fmType,@Query("type") int type,
                                 @Query("content") String content);


    @Multipart
    @POST("familyMemoir/uploadMemoir")
    Call<BaseModel> uploadMemoir(@Part("fmId") int fmId,
                                 @Part("isFamilyZone") int isFamilyZone,
                                 @PartMap Map<String,RequestBody> params);

    @POST("familyMemoir/familyMemoirList")
    Call<MemoirListModel> familyMemoirList(@Query("toPage") int toPage, @Query("pageSize") int pageSize,
                                     @Query("fmType") int fmType);

    @POST("familyMemoir/ familyMemoirInfo")
    Call<MemoirInfoModel> familyMemoirInfo(@Query("fmId") int fmId);
}
