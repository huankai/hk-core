package com.hk.commons.http.post;

import com.hk.commons.http.AbstractHttpExecutor;
import com.hk.commons.http.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;

/**
 * Post 请求
 *
 * @param <T>
 * @param <P>
 * @author kevin
 */
public abstract class AbstractPostHttpExecutor<T, P> extends AbstractHttpExecutor<T> {

    public AbstractPostHttpExecutor(ResponseHandler<T> responseHandler) {
        super(responseHandler);
    }

    /**
     * 执行请求
     *
     * @param uri    请求 Uri
     * @param params 请求参数
     * @return 响应结果
     */
    public T execute(String uri, P params) {
        return doExecute(HttpUtils.newHttpPost(uri, generateEntity(params), getHeaders()));
    }

    /**
     * 生成请求体，Post 请求体不相同，如表单、json 、附件 等
     *
     * @param params params
     * @return {@link HttpEntity}
     */
    public abstract HttpEntity generateEntity(P params);

}
