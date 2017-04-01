package com.fanwe.live.wxapi;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.event.EJsWxBackInfo;
import com.sunday.eventbus.SDEventManager;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends WXCallbackActivity
{

    // 应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp)
    {
        int type = resp.getType();
        if (type == ConstantsAPI.COMMAND_SENDAUTH)
        {
            //授权回调
            SendAuth.Resp sar = (SendAuth.Resp) resp;
            String code = sar.code;

            Map<String, String> map = new HashMap<String, String>();
            map.put("login_sdk_type", "wxlogin");
            map.put("err_code", String.valueOf(resp.errCode));

            switch (resp.errCode)
            {
                case BaseResp.ErrCode.ERR_OK:
                    map.put("code", code);
                    break;
                default:
                    map.put("code", "");
                    break;
            }
            String json = JSON.toJSONString(map);

            EJsWxBackInfo event = new EJsWxBackInfo();
            event.json = json;
            SDEventManager.post(event);
            finish();
        } else
        {
            super.onResp(resp);
        }
    }

}
