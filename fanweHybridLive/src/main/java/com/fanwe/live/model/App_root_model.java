package com.fanwe.live.model;

/**
 * Created by kevin.liu on 2017/2/17.
 */

public class App_root_model
{

    /**
     * code : 0000
     * message :
     * returnObj : {"ad_http":"","monitor_second":5,"refresh_ranking_time":300,"privacy_link":"http: //www.qg8.com//app/privacy","ad_open":0,"ip_info":{},"app_name":"Live","qq_app_api":"1","auto_login":0,"has_qq_login":"1","beauty_android":49,"jr_user_level":"5","spear_live":"LiveHost","share_title":null,"agreement_link":"http: //www.qg8.com//app/agreement","site_url":"","has_get_profit":1,"sina_app_api":"1","ticket_name":"","has_mobile_login":"1","spear_normal":"NormalGuest","wx_app_api":"1","reload_time":60,"bullet_screen_diamond":null,"tipoffTypes":[],"short_name":"Live","privacy_title":"","has_dirty_words":1,"ad_img":"image.host/public/gift/splash_screen2.jpg","has_wx_login":"1","has_sina_login":"1","spear_interact":"InteractUser","selected_app_api":0,"listmsg":[],"beauty_ios":100,"props":[],"h5_url":{}}
     */

    private String code;
    private String message;
    private String returnObj;

    protected int status; // 接口返回状态，1-成功，其他值失败，或者其他业务
    protected String error; // 接口返回的提示信息

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(String returnObj) {
        this.returnObj = returnObj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isOk() {
        return this.status == 1;
    }
}
