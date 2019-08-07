package com.hk.commons.http;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http 请求
 *
 * @author kevin
 */
public interface HttpExecutor<T, P> {

    /**
     * @param uri    请求的RUI
     * @param params 请求的参数
     * @return 返回Http请求的结果
     * @throws IOException 抛出IO异常
     */
    default T execute(String uri, P params) throws IOException {
        return execute(URI.create(uri), params);
    }

    T execute(URI uri, P params) throws IOException;

    /**
     * 设置返回 处理器
     *
     * @param responseHandler ResponseHandler
     * @return {@link HttpExecutor}
     */
    HttpExecutor<T, P> setResponseHandler(ResponseHandler<T> responseHandler);

    /**
     * 添加请求头
     *
     * @param headers 请求头信息
     * @return {@link HttpExecutor}
     */
    HttpExecutor<T, P> addHeaders(Header... headers);

    /**
     * 生成url
     *
     * @param uri    请求URI
     * @param params 请求参数
     * @return url?params1[key]=params1[value]&params2[key]=params2[value]
     */
    static String generateUri(URI uri, Map<String, Object> params) {
        String urlString = uri.toString();
        StringBuilder s = new StringBuilder();
        s.append(urlString);
        if (CollectionUtils.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<>();
            params.forEach((key, value) -> {
                String convertValue = ConverterUtils.defaultConvert(value, String.class);
                if (StringUtils.isNotEmpty(convertValue)) {
                    nvps.add(new BasicNameValuePair(key, convertValue));
                }
            });
            if (!nvps.isEmpty()) {
                if (!StringUtils.endsWith(urlString, "?")) {
                    s.append("?");
                }
                s.append(URLEncodedUtils.format(nvps, Consts.UTF_8));
            }
        }
        return s.toString();
    }

}
