package com.fanwe.library.listener;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface SDValueListener<T>
{
    void onSuccess(T result);

    void onError(int code, Throwable throwable);
}
