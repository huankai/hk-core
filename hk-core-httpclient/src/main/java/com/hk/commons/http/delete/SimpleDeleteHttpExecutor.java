package com.hk.commons.http.delete;

import org.apache.http.client.ResponseHandler;


/**
 * Delete
 *
 * @author kevin
 * @date 2017年9月28日上午9:39:20
 */
public class SimpleDeleteHttpExecutor extends AbstractDeleteHttpExecutor<String> {

    public SimpleDeleteHttpExecutor() {
        this(UTF8_HANDLER);
    }

    public SimpleDeleteHttpExecutor(ResponseHandler<String> handler) {
        super(handler);
    }

}
