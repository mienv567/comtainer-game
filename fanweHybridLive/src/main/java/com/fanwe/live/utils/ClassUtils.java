package com.fanwe.live.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yong.zhang on 2017/3/22 0022.
 */

public class ClassUtils {

    public static Type getType(Class<?> clazz, int index) {
        Type[] types = getType(clazz);
        if (types == null || types.length <= 0) {
            return null;
        }
        if (index < 0 || types.length < index) {
            return null;
        }
        return types[index];
    }

    public static Type[] getType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Type type = clazz.getGenericSuperclass();
        if (type == null) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments();
    }

}
