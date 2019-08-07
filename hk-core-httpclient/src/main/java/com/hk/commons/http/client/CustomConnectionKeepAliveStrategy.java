package com.hk.commons.http.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/**
 * @author kevin
 * @date 2019-8-7 12:44
 * @see org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

    private static final CustomConnectionKeepAliveStrategy INSTANCE = new CustomConnectionKeepAliveStrategy();

    public static CustomConnectionKeepAliveStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        Args.notNull(response, "HTTP response");
        final HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            final HeaderElement he = it.nextElement();
            final String param = he.getName();
            final String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch (final NumberFormatException ignore) {
                }
            }
        }
        return 60 * 1000;
    }
}
