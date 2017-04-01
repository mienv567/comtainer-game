package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

public class App_get_videoActModel extends BaseActModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int room_id; // 直播间id
    private String userId; // 主播id
    private String groupId; // 聊天组id
    private String cont_url; // 贡献榜地址
    private int live_in; // 0-结束；1-正在直播；2-创建中；3-回放
    private int viewer_num; // 观众数量
    private int hasLianmai; // 1-显示连麦
    private int online_status = -1; // 1-主播在线；0-主播离开


    private String title; // 话题
    private UserModel podcast;
    private RoomShareModel share; //分享信息
    private int isManage;

    private RandomPodcastModel podcast_previous;
    private RandomPodcastModel podcast_next;

    private int is_private; // 1-私密直播
    private String private_share;
    private String share_type; // 分享类型
    private String play_url; // 当live_in=3时，回放视频地址，当video_type=1时，为观看直播的拉流地址
    private String push_rtmp; // 推流地址
    private String play_rtmp; // 拉流地址
    private String play_mp4;
    private String play_flv;
    private String play_hls;
    private int video_type; // 0-互动直播；1-直播
    private String pushUrl; // video_type=1时，主播的推流地址
    private int has_video_control; // 当live_in=3时，回放是否显示视频控制操作

    private int has_focus; // status-2的时候返回是否已关注
    private int show_num; // status-2的时候返回观看人数
    private int is_del_vod; // 1-回放视频已删除

    private int pai_id;//当前直播间存在的竞拍ID
    private boolean isAuctioning;//是否处于竞拍中
    private int cate_id; // 话题id
    private RoomResourceModel resource; // 新增业务:房间对应活动的相关资源存储类
    private List<RoomChannelModel> stream_url_list; // 拉流对应的频道实体集合
    private int type; //1代表服务端切换，0代表客户端切换  - 只有在拉流房间用到
    private int score;//热度积分
    private String ranking; //热度名称

    private int isHorizontal;  //是否横屏 1：是  0：否

    public int getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(int isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    //    private LRSGameModel wolf;//狼人杀游戏信息
    public RoomResourceModel getResource() {
        return resource;
    }

    public void setResource(RoomResourceModel resource) {
        this.resource = resource;
    }

    public List<RoomChannelModel> getStream_url_list() {
        return stream_url_list;
    }

    public void setStream_url_list(List<RoomChannelModel> stream_url_list) {
        this.stream_url_list = stream_url_list;
    }

    /**
     * 是否是推流拉流的直播模式
     *
     * @return
     */
    public boolean isRtmpMode() {
        return video_type == 1;
    }

    public String getPush_rtmp() {
        return push_rtmp;
    }

    public void setPush_rtmp(String push_rtmp) {
        this.push_rtmp = push_rtmp;
    }

    public String getPlay_rtmp() {
        return play_rtmp;
    }

    public void setPlay_rtmp(String play_rtmp) {
        this.play_rtmp = play_rtmp;
    }

    public String getPlay_mp4() {
        return play_mp4;
    }

    public void setPlay_mp4(String play_mp4) {
        this.play_mp4 = play_mp4;
    }

    public String getPlay_flv() {
        return play_flv;
    }

    public void setPlay_flv(String play_flv) {
        this.play_flv = play_flv;
    }

    public String getPlay_hls() {
        return play_hls;
    }

    public void setPlay_hls(String play_hls) {
        this.play_hls = play_hls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAuctioning() {
        return isAuctioning;
    }

    public void setAuctioning(boolean auctioning) {
        isAuctioning = auctioning;
    }

    public boolean isVideoStoped() {
        return status == 2;
    }

    public int getHas_video_control() {
        return has_video_control;
    }

    public void setHas_video_control(int has_video_control) {
        this.has_video_control = has_video_control;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public int getVideo_type() {
        return video_type;
    }

    public void setVideo_type(int video_type) {
        this.video_type = video_type;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public int getIs_del_vod() {
        return is_del_vod;
    }

    public void setIs_del_vod(int is_del_vod) {
        this.is_del_vod = is_del_vod;
    }


    public RandomPodcastModel getPodcast_previous() {
        return podcast_previous;
    }

    public void setPodcast_previous(RandomPodcastModel podcast_previous) {
        this.podcast_previous = podcast_previous;
    }

    public int getHas_focus() {
        return has_focus;
    }

    public void setHas_focus(int has_focus) {
        this.has_focus = has_focus;
    }

    public int getShow_num() {
        return show_num;
    }

    public void setShow_num(int show_num) {
        this.show_num = show_num;
    }

    public RandomPodcastModel getPodcast_next() {
        return podcast_next;
    }

    public void setPodcast_next(RandomPodcastModel podcast_next) {
        this.podcast_next = podcast_next;
    }

    public int getRoomId() {
        return room_id;
    }

    public void setRoomId(int roomId) {
        this.room_id = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCont_url() {
        return cont_url;
    }

    public void setCont_url(String cont_url) {
        this.cont_url = cont_url;
    }

    public int getLive_in() {
        return live_in;
    }

    public void setLive_in(int live_in) {
        this.live_in = live_in;
    }

    public UserModel getPodcast() {
        return podcast;
    }

    public void setPodcast(UserModel podcast) {
        this.podcast = podcast;
    }

    public RoomShareModel getShare() {
        return share;
    }

    public void setShare(RoomShareModel share) {
        this.share = share;
    }

    public int getViewer_num() {
        return viewer_num;
    }

    public void setViewer_num(int viewer_num) {
        this.viewer_num = viewer_num;
    }

    public int getHasLianmai() {
        return hasLianmai;
    }

    public void setHasLianmai(int has_lianmai) {
        this.hasLianmai = has_lianmai;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public String getPrivate_share() {
        return private_share;
    }

    public void setPrivate_share(String private_share) {
        this.private_share = private_share;
    }

    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public int getPai_id() {
        return pai_id;
    }

    public void setPai_id(int pai_id) {
        this.pai_id = pai_id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getIsManage() {
        return isManage;
    }

    public void setIsManage(int isManage) {
        this.isManage = isManage;
    }
    //    public LRSGameModel getWolf() {
    //        return wolf;
    //    }
    //
    //    public void setWolf(LRSGameModel wolf) {
    //        this.wolf = wolf;
    //    }
}
