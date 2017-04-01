package com.fanwe.hybrid.model;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.utils.JsonUtil;

@SuppressWarnings("serial")
public class PaySdkModel extends BaseActModel {
    private String pay_sdk_type;

    private Map<String, Object> config;

    public String getPay_sdk_type() {
        return pay_sdk_type;
    }

    public void setPay_sdk_type(String pay_sdk_type) {
        this.pay_sdk_type = pay_sdk_type;
    }

    public MalipayModel getMalipay() {
        return JsonUtil.map2Object(config, MalipayModel.class);
    }

    public WxappModel getWxapp() {
        return JsonUtil.map2Object(config, WxappModel.class);
    }

    public BfupwapModel getBfupwapModel() {
        String s_config = JSON.toJSONString(config);
        return JSON.parseObject(s_config, BfupwapModel.class);
    }

    public BfappModel getBfappModel() {
        String s_config = JSON.toJSONString(config);
        return JSON.parseObject(s_config, BfappModel.class);
    }

    public GooglePlayPayModel getGoogleModel() {
        String s_config = JSON.toJSONString(config);
        return JSON.parseObject(s_config, GooglePlayPayModel.class);
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

}
