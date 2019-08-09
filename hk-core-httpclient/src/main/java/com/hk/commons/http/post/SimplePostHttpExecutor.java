package com.hk.commons.http.post;

import com.hk.commons.http.UTF8ResponseHandler;
import com.hk.commons.http.utils.HttpUtils;
import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * 常规 POST 请求
 *
 * @author kevin
 * @date 2017年9月28日上午9:31:16
 */
public class SimplePostHttpExecutor extends AbstractPostHttpExecutor<String, Map<String, Object>> {

    public SimplePostHttpExecutor() {
        super(UTF8ResponseHandler.getInstance());
    }

    @Override
    public HttpEntity generateEntity(Map<String, Object> params) {
        return HttpUtils.toHttpEntity(params);
    }

}
