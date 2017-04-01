package com.fanwe.hybrid.model;

import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_Init_TabModel;
import com.fanwe.live.model.CategoryModel;
import com.fanwe.live.model.IpInfoModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.custommsg.CustomMsgLiveMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhz
 * @create time 2014-9-16 类说明 初始化init Model
 */
@SuppressWarnings("serial")
public class InitActModel extends BaseActModel
{
    // APP Key
    private String sina_app_key;
    private String sina_app_secret;
    private String sina_bind_url;
    private String qq_app_key;
    private String qq_app_secret;
    private String wx_app_key;
    private String wx_app_secret;

    private int has_sdk_login;
    private int has_sina_login;//是否显示新浪登录
    private int has_wx_login;//是否显示微信登录
    private int has_qq_login;//是否显示QQ登录
    private int has_mobile_login;//是否显示手机登录

    private List<CustomMsgLiveMsg> listmsg;
    private long monitor_second;// 单位秒
    private int bullet_screen_diamond = 1;// 弹幕一次消费的金币
    private int jr_user_level; // 加入房间时,如果用户等级>=值时，有用户进入房间提醒

    private String spear_live;// 角色名，主播
    private String spear_normal;// 角色名，观众
    private String spear_interact;// 角色名，连麦观众
    private String spear_high_live;//角色名，主播 - 高质量
    private String spear_low_live;//角色名，主播 - 低质量
    private List<Api_linkModel> api_link;
    private String privacy_title;//注册协议标题
    private String privacy_link;//注册协议链接
    private String agreement_link;//主播协议链接
    private int beauty_android;
    private int beauty_close;
    private int service_push;
    private int region_versions; // 城市版本
    private String city;
    private IpInfoModel ip_info;
    private String short_name; //钱客
    private String ticket_name; //钱票
    private String account_name; //帐号
    private App_InitH5Model h5_url;

    private int has_dirty_words; // 当为1时,启用脏子过滤;默认0时不过滤

    private int pai_real_btn;//主播真实竞拍按钮
    private int pai_virtual_btn;//主播虚拟竞拍按钮
    private int show_hide_pai_view;//隐藏全部竞拍功能

    private int open_family_module;//是否隐藏我的家族按钮
    private int super_user_level; // 超高级用户等级
    private long refresh_ranking_time;//热度更新时间

    private LiveRoomModel recommend_room;//推荐房间
    private App_Init_TabModel tab_res_url; // 1.5新增 主页tab的icon动态设置
    /**
     * auto_login : 0
     * beauty_ios : 100
     * bullet_screen_diamond : 1
     * classification : [{"category_color":"e7704c","category_cover_url":"./public/attachment/201701/17/18/587dee6a4864e.jpg","category_desc":"大咖、小咖、好玩的、好看的","category_header_url":"./public/attachment/201701/17/18/587dee715a3e7.jpg","category_name":"综艺","category_round_cover_url":"./public/attachment/201701/23/16/5885bdbccfcc6.png","create_time":"2017-01-05 18:22:16","id":2,"is_delete":0,"sort":1},{"category_color":"e3845b","category_cover_url":"./public/attachment/201701/17/18/587dee812a3ed.jpg","category_desc":"一起直播你我的麻辣生活","category_header_url":"./public/attachment/201701/17/18/587dee86ecf0b.jpg","category_name":"播客","category_round_cover_url":"./public/attachment/201701/23/16/5885bdaaecf0d.png","create_time":"2017-01-06 17:23:49","id":4,"is_delete":0,"sort":2},{"category_color":"dd4c59","category_cover_url":"./public/attachment/201701/17/18/587dee9bb1647.jpg","category_desc":"扫尽潮品热货","category_header_url":"./public/attachment/201701/17/18/587deea12999e.jpg","category_name":"热店","category_round_cover_url":"./public/attachment/201701/23/16/5885bd9e0dd1d.png","create_time":"2017-01-09 16:13:50","id":5,"is_delete":0,"sort":3},{"category_color":"5dbd7b","category_cover_url":"./public/attachment/201701/17/18/587deeb26cce9.jpg","category_desc":"绿色户外,健康人生","category_header_url":"./public/attachment/201701/17/18/587deeb963804.jpg","category_name":"户外","category_round_cover_url":"./public/attachment/201701/23/16/5885bd8d49fb7.png","create_time":"2017-01-09 16:14:19","id":6,"is_delete":0,"sort":4},{"category_color":"5a7dd6","category_cover_url":"./public/attachment/201701/17/18/587deeca0ebcf.jpg","category_desc":"享受趣味生活 品味趣人生","category_header_url":"./public/attachment/201701/17/18/587deecfed73f.jpg","category_name":"趣生活","category_round_cover_url":"./public/attachment/201701/23/16/5885bd7aa3058.png","create_time":"2017-01-09 16:14:41","id":7,"is_delete":0,"sort":5},{"category_color":"e74e4e","category_cover_url":"./public/attachment/201701/17/18/587deee03f2f6.jpg","category_desc":"大家一起来 大家一起嗨","category_header_url":"./public/attachment/201701/17/18/587deee5c16d5.jpg","category_name":"活动","category_round_cover_url":"./public/attachment/201701/23/16/5885bd6b4b40e.png","create_time":"2017-01-09 16:15:33","id":8,"is_delete":0,"sort":6},{"category_color":"e7bd4c","category_cover_url":"./public/attachment/201701/17/18/587deef89d4d4.jpg","category_desc":"发现附近的他/她","category_header_url":"./public/attachment/201701/17/18/587deefe8a8af.jpg","category_name":"附近","category_round_cover_url":"./public/attachment/201701/23/16/5885bd5c85e9b.png","create_time":"2017-01-09 16:16:14","id":9,"is_delete":0,"sort":8},{"category_color":"4fc388","category_cover_url":"./public/attachment/201701/17/18/587def0b39f9d.jpg","category_desc":"我的关注","category_header_url":"./public/attachment/201701/17/18/587def2c2599f.jpg","category_name":"关注","category_round_cover_url":"./public/attachment/201701/23/16/5885bd4ea7bfa.png","create_time":"2017-01-09 16:16:41","id":10,"is_delete":0,"sort":9},{"category_color":"e7704c","category_cover_url":"./public/attachment/201701/17/18/587def533e6f5.jpg","category_desc":"首屏","category_header_url":"./public/attachment/201701/17/18/587def5966f8a.jpg","category_name":"首屏","category_round_cover_url":"./public/attachment/201701/23/16/5885bd14f2e4b.png","create_time":"2017-01-17 17:54:52","id":12,"is_delete":0,"sort":10},{"category_color":"904cbf","category_cover_url":"./public/attachment/201701/17/18/587def3d66ede.jpg","category_desc":"潮人聚集地 总有一款适合你","category_header_url":"./public/attachment/201701/17/18/587def432dac7.jpg","category_name":"潮咖","category_round_cover_url":"./public/attachment/201701/23/16/5885bd331783d.png","create_time":"2017-01-17 17:53:49","id":11,"is_delete":0,"sort":11}]
     * has_get_profit : 1
     * jr_user_level : 5
     * listmsg : []
     * monitor_second : 5
     * props : [{"animType":"","diamonds":10,"icon":"http://image.qiankeep.com/public/attachment/201608/27/13/57c124c04012a.png","id":31,"isAnimated":0,"isEffect":0,"isMuch":0,"isRedEnvelope":0,"name":"测试","score":10,"sort":17,"superimposedType":0,"ticket":10},{"animType":"quantou","diamonds":0,"icon":"./public/gift/quantao.png","id":32,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"拳套","score":0,"sort":19,"superimposedType":1,"ticket":0},{"animType":"","diamonds":33,"icon":"./public/gift/a.png","id":9,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"香吻","score":33,"sort":12,"superimposedType":0,"ticket":33},{"animType":"plane2","diamonds":5000,"icon":"http://image.qiankeep.com/public/attachment/201608/12/20/57adc567284cd.png","id":26,"isAnimated":2,"isEffect":1,"isMuch":0,"isRedEnvelope":0,"name":"客机","score":5000,"sort":3,"superimposedType":0,"ticket":5000},{"animType":"lamborghini","diamonds":1200,"icon":"http://image.qiankeep.com/public/attachment/201608/12/20/57adc834ec803.png","id":27,"isAnimated":2,"isEffect":1,"isMuch":0,"isRedEnvelope":0,"name":"兰博基尼","score":1200,"sort":9,"superimposedType":0,"ticket":1200},{"animType":"","diamonds":10,"icon":"./public/gift/c.png","id":5,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"玫瑰花","score":10,"sort":13,"superimposedType":0,"ticket":10},{"animType":"","diamonds":1,"icon":"./public/gift/a4.png","id":11,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"花","score":1,"sort":15,"superimposedType":0,"ticket":1},{"animType":"quantou","diamonds":500,"icon":"./public/gift/yaodai.png","id":33,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"腰带","score":500,"sort":19,"superimposedType":0,"ticket":0},{"animType":"ferrari","diamonds":6666,"icon":"http://image.qiankeep.com/public/attachment/201608/12/20/57adc842ec997.png","id":10,"isAnimated":2,"isEffect":1,"isMuch":0,"isRedEnvelope":0,"name":"法拉利","score":6666,"sort":2,"superimposedType":0,"ticket":6666},{"animType":"","diamonds":1,"icon":"http://image.qiankeep.com/public/attachment/201607/26/12/5796e55e60201.png","id":19,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"表","score":1,"sort":16,"superimposedType":0,"ticket":1},{"animType":"","diamonds":1,"icon":"./public/gift/a7.png","id":1,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"香蕉","score":1,"sort":8,"superimposedType":0,"ticket":1},{"animType":"","diamonds":3,"icon":"./public/gift/a1.png","id":6,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"皮鞭","score":3,"sort":7,"superimposedType":0,"ticket":3},{"animType":"plane1","diamonds":3000,"icon":"http://image.qiankeep.com/public/attachment/201608/12/20/57adc53bf3726.png","id":25,"isAnimated":2,"isEffect":1,"isMuch":0,"isRedEnvelope":0,"name":"轰炸机","score":3000,"sort":4,"superimposedType":0,"ticket":3000},{"animType":"","diamonds":88,"icon":"./public/gift/a2.png","id":7,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"粉钻","score":88,"sort":11,"superimposedType":0,"ticket":199},{"animType":"rocket1","diamonds":9000,"icon":"http://image.qiankeep.com/public/attachment/201608/12/20/57adc5a2ec77e.png","id":28,"isAnimated":2,"isEffect":1,"isMuch":0,"isRedEnvelope":0,"name":"火箭","score":9000,"sort":1,"superimposedType":0,"ticket":9000},{"animType":"","diamonds":1,"icon":"./public/gift/a8.png","id":2,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"黄瓜","score":1,"sort":14,"superimposedType":0,"ticket":1},{"animType":"","diamonds":199,"icon":"./public/gift/a6.png","id":8,"isAnimated":0,"isEffect":1,"isMuch":1,"isRedEnvelope":0,"name":"钻戒","score":199,"sort":5,"superimposedType":0,"ticket":199},{"animType":"","diamonds":999,"icon":"./public/gift/a4.png","id":13,"isAnimated":1,"isEffect":0,"isMuch":0,"isRedEnvelope":0,"name":"烟花","score":999,"sort":6,"superimposedType":0,"ticket":999},{"animType":"","diamonds":500,"icon":"./public/gift/a3.png","id":12,"isAnimated":0,"isEffect":0,"isMuch":0,"isRedEnvelope":1,"name":"红包","score":500,"sort":10,"superimposedType":0,"ticket":200}]
     * qq_app_api : 1
     * refresh_ranking_time : 300
     * selected_app_api : 0
     * sina_app_api : 1
     * tipoffTypes : [{"description":"涉黄","id":1,"isEffect":1}]
     * wx_app_api : 1
     */

    private int auto_login;
    private int beauty_ios;
    private int has_get_profit;
    private int selected_app_api;
    private List<CategoryModel> classification;
    private List<LiveGiftModel> props;
    private List<TipoffTypesBean> tipoffTypes;

    public List<LiveLikeModel> getLikeProps() {
        return likeProps;
    }

    public void setLikeProps(List<LiveLikeModel> likeProps) {
        this.likeProps = likeProps;
    }

    private List<LiveLikeModel> likeProps;

    public App_Init_TabModel getTab_res_url() {
        return tab_res_url;
    }

    public void setTab_res_url(App_Init_TabModel tab_res_url) {
        this.tab_res_url = tab_res_url;
    }

    public int getHas_dirty_words()
    {
        return has_dirty_words;
    }

    public void setHas_dirty_words(int has_dirty_words)
    {
        this.has_dirty_words = has_dirty_words;
    }
    public String getAccount_name()
    {
        return account_name;
    }

    public void setAccount_name(String account_name)
    {
        this.account_name = account_name;
    }

    public String getApp_name()
    {
        return app_name;
    }

    public void setApp_name(String app_name)
    {
        this.app_name = app_name;
    }

    private String app_name; //钱客

    public String getShort_name()
    {
        return short_name;
    }

    public void setShort_name(String short_name)
    {
        this.short_name = short_name;
    }

    public String getTicket_name()
    {
        return ticket_name;
    }

    public void setTicket_name(String ticket_name)
    {
        this.ticket_name = ticket_name;
    }

    public IpInfoModel getIp_info()
    {
        return ip_info;
    }

    public void setIp_info(IpInfoModel ip_info)
    {
        this.ip_info = ip_info;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getRegion_versions()
    {
        return region_versions;
    }

    public void setRegion_versions(int region_versions)
    {
        this.region_versions = region_versions;
    }

    public String getPrivacy_title()
    {
        return privacy_title;
    }

    public void setPrivacy_title(String privacy_title)
    {
        this.privacy_title = privacy_title;
    }

    public String getPrivacy_link()
    {
        return privacy_link;
    }

    public void setPrivacy_link(String privacy_link)
    {
        this.privacy_link = privacy_link;
    }

    public String getAgreement_link()
    {
        return agreement_link;
    }

    public void setAgreement_link(String agreement_link)
    {
        this.agreement_link = agreement_link;
    }

    public int getBeauty_android()
    {
        return beauty_android;
    }

    public void setBeauty_android(int beauty_android)
    {
        this.beauty_android = beauty_android;
    }

    public int getBeauty_close()
    {
        return beauty_close;
    }

    public void setBeauty_close(int beauty_close)
    {
        this.beauty_close = beauty_close;
    }

    public int getService_push()
    {
        return service_push;
    }

    public void setService_push(int service_push)
    {
        this.service_push = service_push;
    }

    public String getSpear_live()
    {
        return spear_live;
    }

    public void setSpear_live(String spear_live)
    {
        this.spear_live = spear_live;
    }

    public String getSpear_normal()
    {
        return spear_normal;
    }

    public void setSpear_normal(String spear_normal)
    {
        this.spear_normal = spear_normal;
    }

    public String getSpear_interact()
    {
        return spear_interact;
    }

    public void setSpear_interact(String spear_interact)
    {
        this.spear_interact = spear_interact;
    }

    public List<Api_linkModel> getApi_link()
    {
        return api_link;
    }

    public void setApi_link(List<Api_linkModel> api_link)
    {
        this.api_link = api_link;
    }

    public String getSina_app_key()
    {
        return sina_app_key;
    }

    public void setSina_app_key(String sina_app_key)
    {
        this.sina_app_key = sina_app_key;
    }

    public String getSina_app_secret()
    {
        return sina_app_secret;
    }

    public void setSina_app_secret(String sina_app_secret)
    {
        this.sina_app_secret = sina_app_secret;
    }

    public String getSina_bind_url()
    {
        return sina_bind_url;
    }

    public void setSina_bind_url(String sina_bind_url)
    {
        this.sina_bind_url = sina_bind_url;
    }

    public String getQq_app_key()
    {
        return qq_app_key;
    }

    public void setQq_app_key(String qq_app_key)
    {
        this.qq_app_key = qq_app_key;
    }

    public String getQq_app_secret()
    {
        return qq_app_secret;
    }

    public void setQq_app_secret(String qq_app_secret)
    {
        this.qq_app_secret = qq_app_secret;
    }

    public String getWx_app_key()
    {
        return wx_app_key;
    }

    public void setWx_app_key(String wx_app_key)
    {
        this.wx_app_key = wx_app_key;
    }

    public String getWx_app_secret()
    {
        return wx_app_secret;
    }

    public void setWx_app_secret(String wx_app_secret)
    {
        this.wx_app_secret = wx_app_secret;
    }

    private int sina_app_api;
    private int qq_app_api;
    private int wx_app_api;
    private int statusbar_hide;
    private String statusbar_color;
    private String topnav_color;
    private String ad_img;
    private String ad_http;
    private int ad_open;
    private int reload_time;
    private String site_url;
    private InitUpgradeModel version;
    private ArrayList<String> top_url;

    public ArrayList<String> getTop_url()
    {
        return top_url;
    }

    public void setTop_url(ArrayList<String> top_url)
    {
        this.top_url = top_url;
    }

    public InitUpgradeModel getVersion()
    {
        return version;
    }

    public void setVersion(InitUpgradeModel version)
    {
        this.version = version;
    }

    public String getSite_url()
    {
        return site_url;
    }

    public void setSite_url(String site_url)
    {
        this.site_url = site_url;
    }

    public int getReload_time()
    {
        return reload_time;
    }

    public void setReload_time(int reload_time)
    {
        this.reload_time = reload_time;
    }

    public int getSina_app_api()
    {
        return sina_app_api;
    }

    public void setSina_app_api(int sina_app_api)
    {
        this.sina_app_api = sina_app_api;
    }

    public int getQq_app_api()
    {
        return qq_app_api;
    }

    public void setQq_app_api(int qq_app_api)
    {
        this.qq_app_api = qq_app_api;
    }

    public int getWx_app_api()
    {
        return wx_app_api;
    }

    public void setWx_app_api(int wx_app_api)
    {
        this.wx_app_api = wx_app_api;
    }

    public int getStatusbar_hide()
    {
        return statusbar_hide;
    }

    public void setStatusbar_hide(int statusbar_hide)
    {
        this.statusbar_hide = statusbar_hide;
    }

    public String getStatusbar_color()
    {
        return statusbar_color;
    }

    public void setStatusbar_color(String statusbar_color)
    {
        this.statusbar_color = statusbar_color;
    }

    public String getTopnav_color()
    {
        return topnav_color;
    }

    public void setTopnav_color(String topnav_color)
    {
        this.topnav_color = topnav_color;
    }

    public String getAd_img()
    {
        return ad_img;
    }

    public void setAd_img(String ad_img)
    {
        this.ad_img = ad_img;
    }

    public String getAd_http()
    {
        return ad_http;
    }

    public void setAd_http(String ad_http)
    {
        this.ad_http = ad_http;
    }

    public int getAd_open()
    {
        return ad_open;
    }

    public void setAd_open(int ad_open)
    {
        this.ad_open = ad_open;
    }

    public List<CustomMsgLiveMsg> getListmsg()
    {
        return listmsg;
    }

    public void setListmsg(List<CustomMsgLiveMsg> listmsg)
    {
        this.listmsg = listmsg;
    }

    public long getMonitor_second()
    {
        return monitor_second;
    }

    public void setMonitor_second(long monitor_second)
    {
        this.monitor_second = monitor_second;
    }

    public int getBullet_screen_diamond()
    {
        return bullet_screen_diamond;
    }

    public void setBullet_screen_diamond(int bullet_screen_diamond)
    {
        this.bullet_screen_diamond = bullet_screen_diamond;
    }

    public int getJr_user_level()
    {
        return jr_user_level;
    }

    public void setJr_user_level(int jr_user_level)
    {
        this.jr_user_level = jr_user_level;
    }

    public int getHas_sdk_login()
    {
        return has_sdk_login;
    }

    public void setHas_sdk_login(int has_sdk_login)
    {
        this.has_sdk_login = has_sdk_login;
    }

    public int getHas_sina_login()
    {
        return has_sina_login;
    }

    public void setHas_sina_login(int has_sina_login)
    {
        this.has_sina_login = has_sina_login;
    }

    public int getHas_wx_login()
    {
        return has_wx_login;
    }

    public void setHas_wx_login(int has_wx_login)
    {
        this.has_wx_login = has_wx_login;
    }

    public int getHas_qq_login()
    {
        return has_qq_login;
    }

    public void setHas_qq_login(int has_qq_login)
    {
        this.has_qq_login = has_qq_login;
    }

    public int getHas_mobile_login()
    {
        return has_mobile_login;
    }

    public void setHas_mobile_login(int has_mobile_login)
    {
        this.has_mobile_login = has_mobile_login;
    }

    public App_InitH5Model getH5_url()
    {
        return h5_url;
    }

    public void setH5_url(App_InitH5Model h5_url)
    {
        this.h5_url = h5_url;
    }

    public int getPai_real_btn()
    {
        return pai_real_btn;
    }

    public void setPai_real_btn(int pai_real_btn)
    {
        this.pai_real_btn = pai_real_btn;
    }

    public int getPai_virtual_btn()
    {
        return pai_virtual_btn;
    }

    public void setPai_virtual_btn(int pai_virtual_btn)
    {
        this.pai_virtual_btn = pai_virtual_btn;
    }

    public int getShow_hide_pai_view()
    {
        return show_hide_pai_view;
    }

    public void setShow_hide_pai_view(int show_hide_pai_view)
    {
        this.show_hide_pai_view = show_hide_pai_view;
    }

    public int getOpen_family_module()
    {
        return open_family_module;
    }

    public void setOpen_family_module(int open_family_module)
    {
        this.open_family_module = open_family_module;
    }

    public int getSuper_user_level() {
        return super_user_level;
    }

    public void setSuper_user_level(int super_user_level) {
        this.super_user_level = super_user_level;
    }

    public String getSpear_high_live() {
        return spear_high_live;
    }

    public void setSpear_high_live(String spear_high_live) {
        this.spear_high_live = spear_high_live;
    }

    public String getSpear_low_live() {
        return spear_low_live;
    }

    public void setSpear_low_live(String spear_low_live) {
        this.spear_low_live = spear_low_live;
    }

    public long getRefresh_ranking_time() {
        return refresh_ranking_time;
    }

    public void setRefresh_ranking_time(long refresh_ranking_time) {
        this.refresh_ranking_time = refresh_ranking_time;
    }

    public LiveRoomModel getRecommend_room() {
        return recommend_room;
    }

    public void setRecommend_room(LiveRoomModel recommend_room) {
        this.recommend_room = recommend_room;
    }

    public int getAuto_login() {
        return auto_login;
    }

    public void setAuto_login(int auto_login) {
        this.auto_login = auto_login;
    }

    public int getBeauty_ios() {
        return beauty_ios;
    }

    public void setBeauty_ios(int beauty_ios) {
        this.beauty_ios = beauty_ios;
    }


    public int getHas_get_profit() {
        return has_get_profit;
    }

    public void setHas_get_profit(int has_get_profit) {
        this.has_get_profit = has_get_profit;
    }


    public int getSelected_app_api() {
        return selected_app_api;
    }

    public void setSelected_app_api(int selected_app_api) {
        this.selected_app_api = selected_app_api;
    }

    public List<CategoryModel> getClassification() {
        return classification;
    }

    public void setClassification(List<CategoryModel> classification) {
        this.classification = classification;
    }

    public List<LiveGiftModel> getProps() {
        return props;
    }

    public void setProps(List<LiveGiftModel> props) {
        this.props = props;
    }

    public List<TipoffTypesBean> getTipoffTypes() {
        return tipoffTypes;
    }

    public void setTipoffTypes(List<TipoffTypesBean> tipoffTypes) {
        this.tipoffTypes = tipoffTypes;
    }


    public static class TipoffTypesBean {
        /**
         * description : 涉黄
         * id : 1
         * isEffect : 1
         */

        private String description;
        private int id;
        private int isEffect;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsEffect() {
            return isEffect;
        }

        public void setIsEffect(int isEffect) {
            this.isEffect = isEffect;
        }
    }
}
