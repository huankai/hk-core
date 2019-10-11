package com.hk.commons.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * {@link BasicResponseHandler} 返回中文会有乱码问题
 *
 * @author kevin
 * @date 2019-6-25 15:33
 * @see BasicResponseHandler
 */
public class UTF8ResponseHandler extends BasicResponseHandler {

    private static final UTF8ResponseHandler UTF_8_RESPONSE_HANDLER = new UTF8ResponseHandler();

    public static UTF8ResponseHandler getInstance() {
        return UTF_8_RESPONSE_HANDLER;
    }

    @Override
    public String handleEntity(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity, Consts.UTF_8);
    }
}
