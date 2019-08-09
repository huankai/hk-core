package com.hk.commons.http.async;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author kevin
 * @date 2019-8-8 20:09
 */
public class LoggerFutureCallback extends AbstractFutureCallback {

    @Override
    public void handleEntity(HttpEntity entity) throws IOException {
        String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        log.info("responseEntity :{}", result);
    }

}
