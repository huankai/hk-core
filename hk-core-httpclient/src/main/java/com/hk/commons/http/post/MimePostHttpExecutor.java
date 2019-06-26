package com.hk.commons.http.post;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * 支持文件上传的POST请求
 *
 * @author kevin
 * @date 2017年10月12日下午4:58:33
 */
public class MimePostHttpExecutor extends AbstractPostHttpExecutor<String, Map<String, ContentBody>> {

    public MimePostHttpExecutor() {
        super(UTF8_HANDLER);
    }

    public MimePostHttpExecutor(ResponseHandler<String> responseHandler) {
        super(responseHandler);
    }

    @Override
    public HttpEntity generateEntity(Map<String, ContentBody> params) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Consts.UTF_8);
        for (Entry<String, ContentBody> entry : params.entrySet()) {
            ContentBody value = entry.getValue();
            if (Objects.nonNull(value)) {
                entityBuilder.addPart(entry.getKey(), value);
            }
        }
        return entityBuilder.build();
    }

}
