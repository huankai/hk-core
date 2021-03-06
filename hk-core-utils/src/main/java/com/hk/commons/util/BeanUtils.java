package com.hk.commons.util;

import com.hk.commons.cglib.BeanConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 与Bean相关的工具类
 *
 * @author kevin
 * @date 2017年9月12日上午10:01:39
 */
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * Map To Bean
     *
     * @param map   map
     * @param clazz clazz
     * @param <T>   T
     * @return T
     */
    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        var beanWrapper = BeanWrapperUtils.createBeanWrapper(clazz);
        for (var entry : map.entrySet()) {
            if (beanWrapper.isWritableProperty(entry.getKey())) {
                beanWrapper.setPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        return clazz.cast(beanWrapper.getWrappedInstance());
    }

    /**
     * Bean to Map
     *
     * @param obj              obj
     * @param ignoreProperties ignoreProperties
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object obj, String... ignoreProperties) {
        return beanToMap(obj, false, ignoreProperties);
    }

    public static Map<String, Object> beanToMapIgnoreEntityProperties(Object obj, String... ignoreProperties) {
        var ignorePropertySet = ArrayUtils.asHashSet(AuditField.AUDIT_FIELD_ARRAY);
        ignorePropertySet.add("new");
        ignorePropertySet.add("class");
        ignorePropertySet.add("hibernateLazyInitializer");
        CollectionUtils.addAllNotNull(ignorePropertySet, ignoreProperties);
        return beanToMap(obj, false, ignorePropertySet.toArray(new String[0]));
    }

    /**
     * Bean to Map
     *
     * @param obj               obj
     * @param containsNullValue containsNullValue
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object obj, boolean containsNullValue, String... ignoreProperties) {
        Map<String, Object> result = new HashMap<>();
        if (null != obj) {
            BeanWrapper beanWrapper = BeanWrapperUtils.createBeanWrapper(obj);
            for (var descriptor : beanWrapper.getPropertyDescriptors()) {
                var name = descriptor.getName();
                var value = beanWrapper.getPropertyValue(name);
                if ((value != null || containsNullValue) && ArrayUtils.noContains(ignoreProperties, name)) {
                    result.put(name, value);
                }
            }
        }
        return result;
    }

    /**
     * 使用此方法进行拷贝时，类型必须一样，基本类型也必须一样，否则拷贝不成功。如 int -> Integer 不会拷贝成功
     *
     * @param source 源对象
     * @param target 拷贝后的对象
     */
    public static void copierCopyNotNullProperties(Object source, Object target) {
        var beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
        beanCopier.copy(source, target, new BeanConverter(target));
    }

    /**
     * 复制不为空的属性
     *
     * @param source source
     * @param target target
     */
    public static void copyNotNullProperties(Object source, Object target) {
        var targetDescriptors = getPropertyDescriptors(target.getClass());
        for (var targetDescriptor : targetDescriptors) {
            var writeMethod = targetDescriptor.getWriteMethod();
            if (writeMethod != null) {
                var sourcePd = getPropertyDescriptor(source.getClass(), targetDescriptor.getName());
                if (null != sourcePd) {
                    var readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            var value = readMethod.invoke(source);
                            if (ObjectUtils.isNotEmpty(value)) {
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetDescriptor.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

}
