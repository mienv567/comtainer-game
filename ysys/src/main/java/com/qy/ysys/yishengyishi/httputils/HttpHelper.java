package com.qy.ysys.yishengyishi.httputils;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.qy.ysys.yishengyishi.AppConfig;
import com.qy.ysys.yishengyishi.AppImpl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class HttpHelper {
    private Retrofit mRetrofit;
    private CommonInterface mService;
    public final ClearableCookieJar cookieJar;

    private HttpHelper(Context mContext) {
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();

        mRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConfig.SERVERWEBSITE)
                .client(okHttpClient)
                .build();

        mService = mRetrofit.create(CommonInterface.class);
    }

    private static HttpHelper mInstant = null;

    public static HttpHelper getInstant() {
        synchronized (HttpHelper.class){
            if(mInstant == null){
                mInstant = new HttpHelper(AppImpl.getApplication());
            }
        }
        return mInstant;
    }

    public CommonInterface getService() {
        return mService;
    }


}
