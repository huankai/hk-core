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

    @Override
    public String handleEntity(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity, Consts.UTF_8);
    }
}