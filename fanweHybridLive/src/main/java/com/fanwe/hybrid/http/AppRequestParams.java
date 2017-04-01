package com.fanwe.hybrid.http;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.constant.Constant.DeviceType;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.common.AppRuntimeWorker;

/**
 * Created by Administrator on 2016/3/30.
 */
public class AppRequestParams extends SDRequestParams
{

    private boolean isNeedShowActInfo = true;

    public static final class RequestDataType
    {
        public static final int BASE64 = 0;
        public static final int AES = 4;
    }

    public static final class ResponseDataType
    {
        public static final int BASE64 = 0;
        public static final int JSON = 1;
        public static final int AES = 4;
    }

    public AppRequestParams()
    {
        super();
        setUrl(ApkConstant.SERVER_URL_API);

//        put("screen_width", SDViewUtil.getScreenWidth());
//        put("screen_height", SDViewUtil.getScreenHeight());
        put("sdkType", DeviceType.DEVICE_ANDROID);
        put("sdkVersionName", SDPackageUtil.getVersionName());
        put("sdkVersion", SDPackageUtil.getVersionCode());
//        if (AppRuntimeWorker.getShow_hide_pai_view() == 1)
//        {
//            //开启竞拍请求接口要传
//            put("itype", "shop");
//        }
    }

    public boolean isNeedShowActInfo()
    {
        return isNeedShowActInfo;
    }

    public void setNeedShowActInfo(boolean isNeedShowActInfo)
    {
        this.isNeedShowActInfo = isNeedShowActInfo;
    }

}
