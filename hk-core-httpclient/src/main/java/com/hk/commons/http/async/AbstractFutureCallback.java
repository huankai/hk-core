package com.hk.commons.http.async;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kevin
 * @date 2019-8-9 13:04
 */
public abstract class AbstractFutureCallback implements FutureCallback<HttpResponse> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    @SneakyThrows
    public void completed(HttpResponse response) {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (null != entity) {
            handleEntity(entity);
        }
    }

    public abstract void handleEntity(HttpEntity entity) throws IOException;

    @Override
    public void failed(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @Override
    public void cancelled() {
        //Not　Used.
    }
}
