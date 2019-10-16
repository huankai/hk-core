package com.hk.commons.http.get;

import com.hk.commons.http.AbstractHttpExecutor;
import com.hk.commons.http.HttpExecutor;
import com.hk.commons.http.UTF8ResponseHandler;
import com.hk.commons.http.utils.HttpUtils;
import org.apache.http.Header;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.util.Map;


/**
 * Http Get 请求
 *
 * @author kevin
 * @see com.hk.commons.http.HttpClientUtils#get(String, Map, Header...)
 * @see com.hk.commons.http.async.AsyncHttpClientUtils#get(String, Map, FutureCallback, Header...)
 */
public final class HttpGetHttpExecutor extends AbstractHttpExecutor<String> implements HttpExecutor<String, Map<String, Object>> {

    public HttpGetHttpExecutor() {
        super(UTF8ResponseHandler.getInstance());
    }

    @Override
    public String execute(String uri, Map<String, Object> params) throws IOException {
        return doExecute(HttpUtils.newHttpGet(uri, params, getHeaders()));
    }
}
