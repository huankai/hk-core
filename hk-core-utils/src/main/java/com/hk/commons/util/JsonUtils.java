package com.hk.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hk.commons.util.date.DatePattern;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    public static final String HANDLER = "handler";

    public static final String HIBERNATE_LAZY_INITIALIZER = "hibernateLazyInitializer";

    private static ObjectMapper mapper;

    private static final Module[] modules;

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
        moduleList.add(new Jdk8Module());

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
        Set<String> exceptSet = ArrayUtils.asHashSet(HANDLER, HIBERNATE_LAZY_INITIALIZER);
        CollectionUtils.addAllNotNull(exceptSet, exceptFields);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        /* 忽略实体中的Hibernate getOne查询返回的 "handler", "hibernateLazyInitializer" 字段 */
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
    public static byte[] serializeToByte(Object obj) {
        if (null == obj) {
            return null;
        }
        return getMapper().writeValueAsBytes(obj);
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
