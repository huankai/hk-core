package com.hk.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 集合工具类
 *
 * @author kevin
 * @date 2017年9月1日下午1:31:18
 */
public abstract class CollectionUtils extends org.springframework.util.CollectionUtils {

    /**
     * 是否为一个空集合
     *
     * @param coll coll
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 长度是否不为0
     *
     * @param iterable iterable
     * @return true or false
     */
    public static boolean isNotEmpty(Iterable<?> iterable) {
        return !isEmpty(iterable);
    }

    /**
     * 长度是否为0
     *
     * @param iterable iterable
     * @return true or false
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return size(iterable) == 0;
    }

    /**
     * 转换为 String 数组
     *
     * @param it it
     * @return String[]
     */
    public static String[] toArray(Iterable<String> it) {
        if (null == it) {
            return new String[0];
        }
        return StreamSupport.stream(it.spliterator(), false).toArray(String[]::new);
    }

    /**
     * 判断iterable元素长度
     *
     * @param iterable iterable
     * @return size
     */
    public static long size(Iterable<?> iterable) {
        if (iterable == null) {
            return 0;
        }
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        }
        return StreamSupport.stream(iterable.spliterator(), false).count();
    }

    /**
     * iterable 转换为集合
     *
     * @param iterable iterable
     * @return {@link ArrayList}
     */
    public static <T> Collection<T> toCollection(Iterable<T> iterable) {
        return toCollection(iterable, 10);
    }

    /**
     * iterable 转换为集合
     *
     * @param iterable      iterable
     * @param estimatedSize estimatedSize
     * @return {@link ArrayList}
     */
    public static <T> Collection<T> toCollection(Iterable<T> iterable, int estimatedSize) {
        return toList(iterable, estimatedSize);
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        return toList(iterable, 10);
    }

    public static <T> List<T> toList(Iterable<T> iterable, int estimatedSize) {
        if (iterable == null) {
            return new ArrayList<>(estimatedSize);
        }
        if (iterable instanceof List) {
            return (List<T>) iterable;
        }
        List<T> list = new ArrayList<>(estimatedSize);
        iterable.forEach(list::add);
        return list;
    }

    /**
     * 是否为一个空Map
     *
     * @param map map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 如果 list2中的元素不能空，添加list2的元素添加到 list1中
     *
     * @param list1 list1
     * @param list2 list2
     * @return boolean
     */
    public static <T> boolean addAll(Collection<T> list1, Collection<T> list2) {
        return isNotEmpty(list2) && list1.addAll(list2);
    }

    /**
     * 是否包含指定元素
     *
     * @param it      it
     * @param element element
     * @return boolean
     */
    public static boolean contains(Iterable<?> it, Object element) {
        if (it == null) {
            return false;
        }
        if (it instanceof Collection) {
            return ((Collection<?>) it).contains(element);
        }
        return StreamSupport.stream(it.spliterator(), false).anyMatch(item -> ObjectUtils.nullSafeEquals(item, element));
    }

    /**
     * 集合中是否包含任意的元素
     *
     * @param coll     集合
     * @param elements 元素
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> boolean containsAny(Collection<T> coll, T... elements) {
        return isNotEmpty(coll)
                && ArrayUtils.isNotEmpty(elements)
                && containsAny(coll, Arrays.asList(elements));
    }

    /**
     * Map根据Value 从小到大排序
     *
     * @param result result
     * @return Map
     */
    public static <V extends Comparable<? super V>> Map<String, V> sortMapByValue(Map<String, V> result) {
        return sortMapByValue(result, false);
    }

    /**
     * <pre>
     * Map根据Value 排序
     * </pre>
     *
     * @param result   result
     * @param reversed 是否反转
     * @return Map
     */
    public static <V extends Comparable<? super V>> Map<String, V> sortMapByValue(Map<String, V> result,
                                                                                  boolean reversed) {
        if (isEmpty(result)) {
            return Collections.emptyMap();
        }
        Map<String, V> finalMap = new LinkedHashMap<>();
        result.entrySet().stream()
                .sorted(reversed ? Map.Entry.<String, V>comparingByValue().reversed()
                        : Map.Entry.comparingByValue())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        return finalMap;
    }

    /**
     * 获取 Map key 的值，转换为 String 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static String getStringValue(Map<?, ?> map, Object key) {
        return getValue(map, key, String.class);
    }

    /**
     * 获取 Map key 的值，转换为 String 类型，如果 值为 Null， 返回默认值
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static String getStringValue(Map<?, ?> map, Object key, String defaultValue) {
        return getValueOrDefault(map, key, defaultValue, String.class);
    }

    /**
     * 获取 Map key 的值，转换为 Boolean 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Boolean getBooleanValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Boolean.class);
    }

    /**
     * 获取 Map key 的值，转换为 Boolean 类型，如果 值为 Null， 返回默认值
     *
     * @param map          map
     * @param key          key
     * @param defaultValue defaultValue
     * @return defaultValue
     */
    public static Boolean getBooleanValue(Map<?, ?> map, Object key, Boolean defaultValue) {
        return getValueOrDefault(map, key, defaultValue, Boolean.class);
    }

    /**
     * 获取 Map key 的值，转换为 Long 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Long getLongValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Long.class);
    }

    /**
     * 获取 Map key 的值，转换为 Long 类型，如果 值为 Null， 返回默认值
     *
     * @param map map
     * @param key key
     * @return defaultValue defaultValue
     */
    public static Long getLongValue(Map<?, ?> map, Object key, Long defaultValue) {
        return getValueOrDefault(map, key, defaultValue, Long.class);
    }

    /**
     * 获取 Map key 的值，转换为 Integer 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Integer getIntegerValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Integer.class);
    }

    /**
     * 获取 Map key 的值，转换为 Integer 类型，如果 值为 Null， 返回默认值
     *
     * @param map          map
     * @param key          key
     * @param defaultValue defaultValue
     * @return value
     */
    public static Integer getIntegerValue(Map<?, ?> map, Object key, Integer defaultValue) {
        return getValueOrDefault(map, key, defaultValue, Integer.class);
    }

    /**
     * 获取 Map key 的值，转换为 Byte 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Byte getByteValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Byte.class);
    }

    /**
     * 获取 Map key 的值，转换为 Byte 类型，如果 值为 Null， 返回默认值
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Byte getByteValue(Map<?, ?> map, Object key, Byte defaultValue) {
        return getValueOrDefault(map, key, defaultValue, Byte.class);
    }

    /**
     * 获取 Map key 的值，转换为 Short 类型
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Short getShortValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Short.class);
    }

    /**
     * 获取 Map key 的值，转换为 Short 类型，如果 值为 Null， 返回默认值
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static Short getByteValue(Map<?, ?> map, Object key, Short defaultValue) {
        return getValueOrDefault(map, key, defaultValue, Short.class);
    }

    /**
     * 获取Map key 的 value值,如果不存在，返回 null
     *
     * @param map map
     * @param key key
     * @return Map
     */
    public static Map<?, ?> getMapValue(Map<?, ?> map, Object key) {
        return getValue(map, key, Map.class);
    }

    /**
     * 获取Map key 的 value值,如果不存在，返回 null
     *
     * @param map map
     * @param key key
     * @return T
     */
    public static <T> T getValue(Map<?, ?> map, Object key, Class<T> clazz) {
        return getValueOrDefault(map, key, null, clazz);
    }

    /**
     * 获取Map key 的 value值，如果不存在，返回 defaultValue
     *
     * @param map map
     * @param key key
     * @return value
     */
    public static <T> T getValueOrDefault(Map<?, ?> map, Object key, T defaultValue, Class<T> clazz) {
        if (isEmpty(map)) {
            return defaultValue;
        }
        var value = map.get(key);
        return null == value ? defaultValue : ConverterUtils.defaultConvert(value, clazz);
    }

    /**
     * Map根据Key 排序
     *
     * @param result result
     * @return Map
     */
    public static <K extends Comparable<? super K>> Map<K, Object> sortMapByKey(Map<K, Object> result) {
        return sortMapByKey(result, false);
    }

    /**
     * <pre>
     * Map根据Key 排序
     * </pre>
     *
     * @param result   result
     * @param reversed 是否反转
     * @return Map
     */
    public static <K extends Comparable<? super K>> Map<K, Object> sortMapByKey(Map<K, Object> result,
                                                                                boolean reversed) {
        if (isEmpty(result)) {
            return new LinkedHashMap<>(0);
        }
        return result.entrySet().stream()
                .sorted(reversed ? Map.Entry.<K, Object>comparingByKey().reversed()
                        : Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * 过滤为空的可变数组元素，添加到指定集合
     *
     * @param coll coll
     * @param args args
     * @return Map
     */
    @SafeVarargs
    public static <T> void addAllNotNull(Collection<T> coll, final T... args) {
        addAllNull(coll, false, args);
    }

    /**
     * 添加元素到集合
     *
     * @param coll             指定集合
     * @param containNullValue 是否将空值元素也添加到集合中
     * @param args             是添加到集合中的元素
     */
    @SafeVarargs
    public static <T> void addAllNull(Collection<T> coll, boolean containNullValue, final T... args) {
        if (ArrayUtils.isNotEmpty(args)) {
            for (var t : args) {
                if (containNullValue || Objects.nonNull(t)) {
                    coll.add(t);
                }
            }
        }
    }

    /**
     * 获取集合第一个元素
     *
     * @param coll coll
     * @return Map
     */
    public static <T> Optional<T> getFirstOrDefault(Collection<T> coll) {
        return Optional.ofNullable(isEmpty(coll) ? null : coll.iterator().next());
    }

    /**
     * 多个Map的值合并成一个map
     *
     * @param values values
     * @return Map
     */
    @SafeVarargs
    public static Map<String, Integer> addOrMergeIntegerValues(Map<String, Integer>... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }
        Map<String, Integer> result = new HashMap<>();
        for (var map : values) {
            for (var entry : map.entrySet()) {
                var value = result.get(entry.getKey());
                if (null != value) {
                    value = value + entry.getValue();
                } else {
                    value = entry.getValue();
                }
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * 多个Map的值合并成一个map
     *
     * @param values values
     * @return Map
     */
    @SafeVarargs
    public static Map<String, Double> addOrMergeDoubleValues(Map<String, Double>... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }
        Map<String, Double> result = new HashMap<>();
        for (var map : values) {
            for (var entry : map.entrySet()) {
                var value = result.get(entry.getKey());
                if (null != value) {
                    value = value + entry.getValue();
                } else {
                    value = entry.getValue();
                }
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * 多个Map的值合并成一个map
     *
     * @param values values
     * @return Map
     */
    @SafeVarargs
    public static Map<String, Long> addOrMergeLongValues(Map<String, Long>... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }
        Map<String, Long> result = new HashMap<>();
        for (var map : values) {
            for (var entry : map.entrySet()) {
                var value = result.get(entry.getKey());
                if (null != value) {
                    value = value + entry.getValue();
                } else {
                    value = entry.getValue();
                }
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * 多个Map的值合并成一个map
     *
     * @param values values
     * @return Map
     */
    @SafeVarargs
    public static Map<String, BigDecimal> addOrMergeBigDecimalValues(Map<String, BigDecimal>... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }
        Map<String, BigDecimal> result = new HashMap<>();
        for (var map : values) {
            for (var entry : map.entrySet()) {
                var value = result.get(entry.getKey());
                if (null != value) {
                    value = value.add(entry.getValue());
                } else {
                    value = entry.getValue();
                }
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * 多个Map的值合并成一个map
     *
     * @param values values
     * @return Map
     */
    @SafeVarargs
    public static Map<String, BigInteger> addOrMergeBigIntegerValues(Map<String, BigInteger>... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }
        Map<String, BigInteger> result = new HashMap<>();
        for (var map : values) {
            for (var entry : map.entrySet()) {
                var value = result.get(entry.getKey());
                if (null != value) {
                    value = value.add(entry.getValue());
                } else {
                    value = entry.getValue();
                }
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }
}
