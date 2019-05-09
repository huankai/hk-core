package com.hk.oauth2.web.cookie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author huangkai
 * @date 2019-05-08 23:01
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "hk.authentication.cookie")
public class CookieProperties implements Serializable {

    private String name;

    /**
     * Cookie path.
     */
    private String path = StringUtils.EMPTY;

    /**
     * Cookie domain. Specifies the domain within which this cookie should be presented.
     */
    private String domain = StringUtils.EMPTY;

    /**
     * True if sending this cookie should be restricted to a secure protocol, or false if the it can be sent using any protocol.
     */
    private boolean secure = true;

    /**
     * true if this cookie contains the HttpOnly attribute. This means that the cookie should not be accessible to scripting engines, like javascript.
     */
    private boolean httpOnly = true;

    /**
     * maxAge
     */
    private int maxAge = -1;

    /**
     * 当配置为true时，会将 cookie 信息中拼接 user-agent 和 requestIp
     */
    private boolean pinToSession = true;
}
