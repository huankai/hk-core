package com.hk.commons.http.delete;

import com.hk.commons.http.AbstractHttpExecutor;
import com.hk.commons.http.HttpExecutor;
import com.hk.commons.http.UTF8ResponseHandler;
import com.hk.commons.http.utils.HttpUtils;
import org.apache.http.Header;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.util.Map;


/**
 * Http Delete 请求
 *
 * @param <T>
 * @author kevin
 * @see com.hk.commons.http.HttpClientUtils#delete(String, Map, Header...)
 * @see com.hk.commons.http.async.AsyncHttpClientUtils#delete(String, Map, FutureCallback, Header...)
 */
public final class HttpDeleteHttpExecutor extends AbstractHttpExecutor<String> implements HttpExecutor<String, Map<String, Object>> {

    public HttpDeleteHttpExecutor() {
        super(UTF8ResponseHandler.getInstance());
    }

    @Override
    public String execute(String uri, Map<String, Object> params) throws IOException {
        return doExecute(HttpUtils.newHttpDelete(uri, params, getHeaders()));
    }

}
