package com.hk.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hk.commons.jackson.BigIntegerToStringSerializer;
import com.hk.commons.jackson.LongToStringSerializer;
import com.hk.commons.util.date.DatePattern;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * JSON Utils
 *
 * @author kevin
 * @date 2018-07-17 14:48
 */
public final class JsonUtils {

    public static final String IGNORE_ENTITY_SERIALIZE_FIELD_FILTER_ID = "fieldFilter";

    private static ObjectMapper mapper;

    private static final Module[] modules;

    private static final boolean HIBERNATE_MODULE_ENABLED = ClassUtils.isPresent("org.hibernate.Session", null)
            && ClassUtils.isPresent("com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module", null);

    static {
        List<Module> moduleList = new ArrayList<>();

        JavaTimeModule JAVA_TIME_MODULE = new JavaTimeModule();
        JAVA_TIME_MODULE.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern())));

        JAVA_TIME_MODULE.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern())));

        JAVA_TIME_MODULE.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD.getPattern())));
        JAVA_TIME_MODULE.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD.getPattern())));

        JAVA_TIME_MODULE.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.HH_MM_SS.getPattern())));
        JAVA_TIME_MODULE.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.HH_MM_SS.getPattern())));
        moduleList.add(JAVA_TIME_MODULE);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, LongToStringSerializer.getInstance());
        simpleModule.addSerializer(BigInteger.class, BigIntegerToStringSerializer.getInstance());
//        simpleModule.addDeserializer(Set.class, new Jackson2ArrayOrStringDeserializer());
        moduleList.add(simpleModule);
        moduleList.add(new Jdk8Module());
        /*
            添加 hibernate 使用 getOne 查询 懒加载报错的问题
            @see https://stackoverflow.com/questions/24994440/no-serializer-found-for-class-org-hibernate-proxy-pojo-javassist-javassist
        */
        if (HIBERNATE_MODULE_ENABLED) {
            Hibernate5Module hibernate5Module = new Hibernate5Module();
            hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
            hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
            moduleList.add(hibernate5Module);
        }
        /*
         *   这里使用 toArray 方法转换为数组时，toArray(new Module[0]) 与 toArray(new Module[moduleList.size()])的区别:
         *   转换集合为数组的时候，有两种方式：使用初始化大小的数组（这里指的是初始化大小的时候使用了集合的size()方法）和空数组。
         *   在低版本的 Java 中推荐使用初始化大小的数组，因为使用反射调用去创建一个合适大小的数组相对较慢。
         *   但是在 openJDK 6 之后的高版本中方法被优化了，传入空数组相比传入初始化大小的数组，效果是相同的甚至有时候是更优的。
         *   因为使用 concurrent 或 synchronized 集合时，如果集合进行了收缩，toArray()和size()方法可能会发生数据竞争，此时传入初始化大小的数组是危险的。
         * @see https://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size
         */
        modules = moduleList.toArray(new Module[0]);
    }

    public static Module[] modules() {
        return modules;
    }

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            synchronized (JsonUtils.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                    configure(mapper, AuditField.AUDIT_FIELD_ARRAY);
                }
            }
        }
        return mapper;
    }

    public static void configure(ObjectMapper om, String... exceptFields) {
        Set<String> exceptSet = ArrayUtils.asHashSet(exceptFields);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(IGNORE_ENTITY_SERIALIZE_FIELD_FILTER_ID,
                SimpleBeanPropertyFilter.serializeAllExcept(exceptSet));
        om.setDateFormat(new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getPattern()))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
//                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)// 如果配置为了会输出类名
                .registerModules(modules()) // 注册java 8 日期 module
                .setFilterProvider(filterProvider);
//                .getSerializerProvider().setNullValueSerializer(NullEmptyJsonSerializer.INSTANCE);// 空值处理为空串
    }

    private static ObjectMapper indentMapper;

    private static ObjectMapper getIndentMapper() {
        if (indentMapper == null) {
            synchronized (JsonUtils.class) {
                if (indentMapper == null) {
                    indentMapper = new ObjectMapper();
                    indentMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    configure(indentMapper, AuditField.AUDIT_FIELD_ARRAY);
                }
            }
        }
        return indentMapper;
    }

    /**
     * 将对象序列化为JSON string.
     *
     * @param obj obj
     * @return json str
     */
    public static String serialize(Object obj) {
        return serialize(obj, false);
    }

    /**
     * 将对象序列化为 JSON string
     *
     * @param obj    obj
     * @param indent indent
     * @return json str
     */
    @SneakyThrows(value = {JsonProcessingException.class})
    public static String serialize(Object obj, boolean indent) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return indent ? getIndentMapper().writeValueAsString(obj) : getMapper().writeValueAsString(obj);
    }

    /**
     * 序列化为 字符串，忽略指定属性
     *
     * @param obj               obj
     * @param containsNullValue 是否 包含属性值为null 属性
     * @param ignoreProperties  要忽略的属性名
     * @return json str
     */
    public static String serializeIgnoreProperties(Object obj, boolean containsNullValue, String... ignoreProperties) {
        return serialize(BeanUtils.beanToMap(obj, containsNullValue, ignoreProperties), false);
    }

    /**
     * 序列化到 byte 数组
     *
     * @param obj obj
     * @return byte[]
     */
    @SneakyThrows(value = {JsonProcessingException.class})
    public static byte[] serializeToByte(Object obj, boolean indent) {
        if (null == obj) {
            return null;
        }
        return indent ? getIndentMapper().writeValueAsBytes(obj) : getMapper().writeValueAsBytes(obj);
    }

    /**
     * 序列化到 outputStream
     *
     * @param obj          obj
     * @param outputStream outputStream
     */
    @SneakyThrows(value = {IOException.class})
    public static void serializeToOutputStream(Object obj, OutputStream outputStream) {
        if (Objects.nonNull(obj)) {
            getMapper().writeValue(outputStream, obj);
        }
    }

    /**
     * 序列化为文件
     *
     * @param obj  obj
     * @param file file
     */
    @SneakyThrows(value = {IOException.class})
    public static void serializeToFile(Object obj, File file) {
        if (Objects.nonNull(obj)) {
            getMapper().writeValue(file, obj);
        }
    }

    /**
     * 将json 字符串反序列化为对象
     *
     * @param json  json str
     * @param clazz Class
     * @param <T>   T
     * @return T
     */
    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return getMapper().readValue(json, clazz);
    }

    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(byte[] json, Class<T> clazz) {
        if (ArrayUtils.isEmpty(json)) {
            return null;
        }
        return getMapper().readValue(json, clazz);
    }

    /**
     * 将json 字符串反序列化为对象集合
     *
     * @param <T>   T
     * @param json  json str
     * @param clazz class
     * @return 序列化的List
     */
    public static <T> List<T> deserializeList(String json, Class<T> clazz) {
        return deserialize(json, ArrayList.class, clazz);
    }

    public static <T> List<T> deserializeList(byte[] json, Class<T> clazz) {
        return deserialize(json, List.class, clazz);
    }

    /**
     * 将json 字符串反序列化为对象集合
     *
     * @param <T>   T
     * @param json  json str
     * @param clazz class
     * @return 序列化的List
     */
    public static <T> Set<T> deserializeSet(String json, Class<T> clazz) {
        return deserialize(json, HashSet.class, clazz);
    }

    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(String json, Class<?> rawType, Class<?> parametrized) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = getMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(rawType, parametrized));
    }

    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(byte[] json, Class<?> rawType, Class<?> parametrized) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = getMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(rawType, parametrized));
    }

    @SneakyThrows(value = {IOException.class})
    public static <K, V> Map<K, V> deserializeMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = getMapper();
        MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
        return mapper.readValue(json, mapType);
    }

    /**
     * 反序列化json 字符串到对象 二级泛型: 如：JsonResult<List<SysUser>>
     *
     * @return 序列化的对象
     */
    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(String json, Class<?> rawType, Class<?> parametrized, Class<?> parameterClasses) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = getMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(rawType, type));
    }

}
