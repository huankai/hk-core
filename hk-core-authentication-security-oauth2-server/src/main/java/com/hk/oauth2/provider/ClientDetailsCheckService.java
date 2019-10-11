package com.hk.oauth2.provider;

import com.hk.oauth2.exception.Oauth2ClientStatusException;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 客户端应用状态检查
 *
 * @author huangkai
 * @date 2019-06-16 08:17
 */
public interface ClientDetailsCheckService extends ClientDetailsService {

    /**
     * 判断当前应用是否可启用状态
     *
     * @param clientId clientId
     * @return true or false，如果为 false ,抛出 {@link Oauth2ClientStatusException} 异常
     * @throws Oauth2ClientStatusException Oauth2ClientStatusException
     */
    void check(String clientId) throws Oauth2ClientStatusException;
}
