package com.hk.commons.http.utils;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Http 工具类
 *
 * @author kevin
 * @date 2019-8-9 11:27
 */
public abstract class HttpUtils {

    public static final List<Header> DEFAULT_HEADER = ArrayUtils.asArrayList(
            new BasicHeader(HttpHeaders.CONTENT_ENCODING, Consts.UTF_8.name()));

    /**
     * 请求时间配置
     * Socket超时 :10 秒
     * 连接超时:    5秒
     * 连接请求超时: 5秒
     */
    public static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(10000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();


    /**
     * 创建 http Get 请求
     *
     * @param uri     　uri
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link HttpGet}
     */
    public static HttpGet newHttpGet(String uri, Map<String, ?> params, Header... headers) {
        HttpGet get = new HttpGet();
        String uri_ = generateUri(uri, params);
        get.setHeaders(headers);
        get.setURI(URI.create(uri_));
        return get;
    }

    /**
     * 创建 http Delete 请求
     *
     * @param uri     　uri
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link HttpDelete}
     */
    public static HttpDelete newHttpDelete(String uri, Map<String, ?> params, Header... headers) {
        HttpDelete httpDelete = new HttpDelete();
        String uri_ = generateUri(uri, params);
        httpDelete.setHeaders(headers);
        httpDelete.setURI(URI.create(uri_));
        return httpDelete;
    }

    /**
     * 创建 http Post 请求
     *
     * @param uri     　uri
     * @param entity  请求参数
     * @param headers 请求头
     * @return {@link HttpPost}
     */
    public static HttpPost newHttpPost(String uri, HttpEntity entity, Header... headers) {
        HttpPost httpPost = new HttpPost();
        httpPost.setEntity(entity);
        httpPost.setHeaders(headers);
        httpPost.setURI(URI.create(uri));
        return httpPost;
    }

    /**
     * 支持文件上传的转换为 请求体
     *
     * @param contentBody 　内容
     * @return {@link HttpEntity}
     */
    public static HttpEntity contentBodyToHttpEntity(Map<String, ContentBody> contentBody) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(Consts.UTF_8);
        if (CollectionUtils.isNotEmpty(contentBody)) {
            for (Map.Entry<String, ContentBody> entry : contentBody.entrySet()) {
                ContentBody value = entry.getValue();
                if (Objects.nonNull(value)) {
                    entityBuilder.addPart(entry.getKey(), value);
                }
            }
        }
        return entityBuilder.build();
    }

    /**
     * uri 请求参数 转换为 请求体
     *
     * @param params 　params
     * @return {@link HttpEntity}
     */
    public static HttpEntity toHttpEntity(Map<String, ?> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(params)) {
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                String value = ConverterUtils.defaultConvert(entry.getValue(), String.class);
                if (StringUtils.isNotEmpty(value)) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        return new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
    }

    /**
     * uri 请求参数 拼装
     *
     * @param params 　params
     * @return 请求 uri 加参数
     */
    public static String generateUri(String uri, Map<String, ?> params) {
        StringBuilder s = new StringBuilder(uri);
        if (CollectionUtils.isNotEmpty(params)) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            params.forEach((key, value) -> {
                String convertValue = ConverterUtils.defaultConvert(value, String.class);
                if (StringUtils.isNotEmpty(convertValue)) {
                    nameValuePairs.add(new BasicNameValuePair(key, convertValue));
                }
            });
            if (!nameValuePairs.isEmpty()) {
                if (!StringUtils.endsWith(uri, "?")) {
                    s.append("?");
                }
                s.append(URLEncodedUtils.format(nameValuePairs, Consts.UTF_8));
            }
        }
        return s.toString();
    }

}
