package com.hk.commons.util;

import org.springframework.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author kevin
 * @date 2018-04-19 09:00
 */
public abstract class ClassUtils extends org.springframework.util.ClassUtils {

    /**
     * 获取类的泛型类
     *
     * @param genType genType
     * @param index   指定索引的泛型类
     * @return 泛型类
     */
    public static Class<?> getGenericTypeByType(ParameterizedType genType, int index) {
        var params = genType.getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return null;
        }
        var res = params[index];
        if (res instanceof Class) {
            return (Class<?>) res;
        }
        if (res instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) res).getRawType();
        }
        return null;
    }

    /**
     * 获取一个类的泛型类型,如果未获取到返回 null
     *
     * @param clazz 要获取的类
     * @param index 泛型索引
     * @return 泛型
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericType(Class<?> clazz, int index) {
        var arrays = new ArrayList<>();
        arrays.add(clazz.getGenericSuperclass());
        arrays.addAll(Arrays.asList(clazz.getGenericInterfaces()));
        return (Class<T>) arrays.stream()
                .filter(Objects::nonNull)
                .map(type -> {
                    if (clazz != Object.class && !(type instanceof ParameterizedType)) {
                        return getGenericType(clazz.getSuperclass(), index);
                    }
                    return getGenericTypeByType(((ParameterizedType) type), index);
                })
                .filter(Objects::nonNull)
                .filter(res -> res != Object.class)
                .findFirst()
                .orElse(null);
    }

}
