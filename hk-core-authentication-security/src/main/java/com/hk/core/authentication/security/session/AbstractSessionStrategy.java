package com.hk.core.authentication.security.session;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kevin
 * @date 2018-07-27 10:15
 */
public abstract class AbstractSessionStrategy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final String destinationUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Setter
    private boolean createNewSession = false;

    AbstractSessionStrategy(String invalidSessionUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
        this.destinationUrl = invalidSessionUrl;
    }

    protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(createNewSession);
        logger.debug("session失效,跳转到: {}", destinationUrl);
        redirectStrategy.sendRedirect(request, response, destinationUrl);
    }
}
