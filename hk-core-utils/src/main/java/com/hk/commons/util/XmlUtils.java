package com.hk.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Objects;

/**
 * xml 工具类
 *
 * @author kevin
 * @date 2019-5-6 17:26
 * @see JsonUtils
 */
public final class XmlUtils {

    private static XmlMapper xmlMapper;

    private static XmlMapper indentMapper;

    private static XmlMapper getXmlMapper() {
        if (Objects.isNull(xmlMapper)) {
            synchronized (XmlUtils.class) {
                if (Objects.isNull(xmlMapper)) {
                    xmlMapper = new XmlMapper();
                    JsonUtils.configure(xmlMapper);
                }
            }
        }
        return xmlMapper;
    }

    private static XmlMapper getIndentXmlMapper() {
        if (Objects.isNull(indentMapper)) {
            synchronized (XmlUtils.class) {
                if (Objects.isNull(indentMapper)) {
                    indentMapper = new XmlMapper();
                    indentMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    JsonUtils.configure(indentMapper);
                }
            }
        }
        return indentMapper;
    }

    public static String serialize(Object obj) {
        return serialize(obj, false);
    }

    @SneakyThrows(value = {JsonProcessingException.class})
    public static String serialize(Object obj, boolean indent) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return indent ? getIndentXmlMapper().writeValueAsString(obj) : getXmlMapper().writeValueAsString(obj);
    }

    @SneakyThrows(value = {IOException.class})
    public static <T> T deserialize(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return getXmlMapper().readValue(json, clazz);
    }


}
