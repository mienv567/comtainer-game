package com.fanwe.hybrid.map.tencent;

import android.app.Application;
import android.text.TextUtils;

import com.fanwe.library.utils.SDTypeParseUtil;
import com.tencent.lbssearch.object.Location;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentLocationUtils;
import com.tencent.mapsdk.raster.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SDTencentMapManager implements TencentLocationListener
{

    private static SDTencentMapManager mInstance;
    private Application mApplication;

    private TencentLocationManager mLocationManager;

    private TencentLocation mLocation;
    private boolean mStarted = false;

    private Map<TencentLocationListener, TencentLocationListenerInfo> mMapLocationListener = new HashMap<TencentLocationListener, SDTencentMapManager.TencentLocationListenerInfo>();
    private List<TencentLocationListener> mListLocationListenerNeedUnRegister = new ArrayList<TencentLocationListener>();

    private SDTencentMapManager()
    {
    }

    public static SDTencentMapManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (SDTencentMapManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new SDTencentMapManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Application application)
    {
        this.mApplication = application;
        this.mLocationManager = TencentLocationManager.getInstance(mApplication);
    }

    public TencentLocation getLocation()
    {
        return mLocation;
    }

    public String getCurAddress()
    {
        String address = null;
        if (mLocation != null)
        {
            address = mLocation.getAddress();
        }
        return address;
    }

    public String getCity()
    {
        String city = null;
        if (mLocation != null)
        {
            city = mLocation.getCity();
        }
        return city;
    }

    public String getCityShort()
    {
        String city = getCity();
        if (!TextUtils.isEmpty(city))
        {
            if (city.contains("市"))
            {
                city = city.replace("市", "");
            }
        }
        return city;
    }

    public String getDistrict()
    {
        String district = null;
        if (mLocation != null)
        {
            district = mLocation.getDistrict();
        }
        return district;
    }

    public String getProvince()
    {
        if (mLocation != null)
        {
            return mLocation.getProvince();
        } else
        {
            return null;
        }
    }

    public String getDistrictShort()
    {
        String district = getDistrict();
        if (!TextUtils.isEmpty(district))
        {
            if (district.contains("区"))
            {
                district = district.replace("区", "");
            }
        }
        return district;
    }

    public boolean hasLocationSuccess()
    {
        return hasLocationSuccess(mLocation);
    }

    public boolean hasLocationSuccess(TencentLocation location)
    {
        return mLocation != null;
    }

    /**
     * 纬度(ypoint)
     *
     * @return
     */
    public double getLatitude()
    {
        double latitude = 0;
        if (mLocation != null)
        {
            latitude = mLocation.getLatitude();
        }
        return latitude;
    }

    /**
     * 经度(xpoint)
     *
     * @return
     */
    public double getLongitude()
    {
        double longitude = 0;
        if (mLocation != null)
        {
            longitude = mLocation.getLongitude();
        }
        return longitude;
    }

    /**
     * 获得当前位置的LatLng
     *
     * @return
     */
    public LatLng getLatLngCurrent()
    {
        LatLng ll = null;
        double lat = getLatitude();
        double lon = getLongitude();
        if (lat != 0 && lon != 0)
        {
            ll = new LatLng(lat, lon);
        }
        return ll;
    }

    public double getDistance(LatLng llStart, LatLng llEnd)
    {
        if (llStart != null && llEnd != null)
        {
            double latStart = llStart.getLatitude();
            double lngStart = llStart.getLongitude();
            double latEnd = llEnd.getLatitude();
            double lngEnd = llEnd.getLongitude();
            return TencentLocationUtils.distanceBetween(latStart, lngStart, latEnd, lngEnd);
        } else
        {
            return 0;
        }
    }

    public double getDistanceFromMyLocation(LatLng llEnd)
    {
        return getDistance(getLatLngCurrent(), llEnd);
    }

    /**
     * 获得我的位置和传进来的经纬度间的距离
     *
     * @param latitude  纬度(Ypoint)
     * @param longitude 经度(Xpoint)
     * @return
     */
    public double getDistanceFromMyLocation(double latitude, double longitude)
    {
        return getDistance(getLatLngCurrent(), new LatLng(latitude, longitude));
    }

    /**
     * 获得我的位置和传进来的经纬度间的距离
     *
     * @param latitude  纬度(Ypoint)
     * @param longitude 经度(Xpoint)
     * @return
     */
    public double getDistanceFromMyLocation(String latitude, String longitude)
    {
        return getDistance(getLatLngCurrent(), new LatLng(SDTypeParseUtil.getDouble(latitude, 0), SDTypeParseUtil.getDouble(longitude, 0)));
    }

    public Location latLngToLocation(LatLng ll)
    {
        Location location = null;
        if (ll != null)
        {
            location = new Location();
            location.lat = (float) ll.getLatitude();
            location.lng = (float) ll.getLongitude();
        }
        return location;
    }

    public List<LatLng> locationToLatLng(List<Location> listLocation)
    {
        List<LatLng> listLatLng = new ArrayList<LatLng>();
        for (Location location : listLocation)
        {
            listLatLng.add(new LatLng(location.lat, location.lng));
        }
        return listLatLng;
    }

    public LatLng locationToLatLng(Location location)
    {
        LatLng latLng = null;
        if (location != null)
        {
            latLng = new LatLng(location.lat, location.lng);
        }
        return latLng;
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason)
    {
        if (location != null)
        {
            mLocation = location;
        }

        for (TencentLocationListenerInfo item : mMapLocationListener.values())
        {
            item.listener.onLocationChanged(location, error, reason);
            if (!item.locationAllTheTime)
            {
                addToUnRegisterList(item.listener);
            }
        }

        shouldStopLocation();
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc)
    {
        for (TencentLocationListenerInfo item : mMapLocationListener.values())
        {
            item.listener.onStatusUpdate(name, status, desc);
        }
    }

    private void shouldStopLocation()
    {
        for (TencentLocationListener listener : mListLocationListenerNeedUnRegister)
        {
            mMapLocationListener.remove(listener);
        }
        mListLocationListenerNeedUnRegister.clear();

        if (mMapLocationListener.isEmpty())
        {
            stopLocation();
        }
    }

    public static TencentLocationRequest createDefaultLocationRequest()
    {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(3 * 1000);
        request.setAllowCache(false);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);// default
        return request;
    }

    /**
     * 定位最终调用方法
     *
     * @param request
     */
    private void startLocation(TencentLocationRequest request)
    {
        if (!mStarted)
        {
            mStarted = true;
            mLocationManager.requestLocationUpdates(request, this);
        }
    }

    public void startLocation(TencentLocationListener listener)
    {
        startLocation(false, listener);
    }

    public void startLocation(boolean locationAllTheTime, TencentLocationListener listener)
    {
        if (listener != null)
        {
            TencentLocationListenerInfo listenerInfo = new TencentLocationListenerInfo();
            listenerInfo.listener = listener;
            listenerInfo.locationAllTheTime = locationAllTheTime;
            mMapLocationListener.put(listener, listenerInfo);

            TencentLocationRequest request = createDefaultLocationRequest();
            startLocation(request);
        }
    }

    private void stopLocation()
    {
        if (mStarted)
        {
            mLocationManager.removeUpdates(this);
            mStarted = false;
        }
    }

    public boolean isInLocation()
    {
        return mStarted;
    }

    // 监听相关
    private void addToUnRegisterList(TencentLocationListener listener)
    {
        if (listener != null)
        {
            if (!mListLocationListenerNeedUnRegister.contains(listener))
            {
                mListLocationListenerNeedUnRegister.add(listener);
            }
        }
    }

    public void unRegisterLocationListener(TencentLocationListener listener)
    {
        addToUnRegisterList(listener);
    }

    private class TencentLocationListenerInfo
    {
        public TencentLocationListener listener;
        public boolean locationAllTheTime;
    }

}
