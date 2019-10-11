package com.hk.core.authentication.api;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.io.Serializable;

/**
 * @author huangkai
 * @date 2019-06-15 21:40
 */
@Data
public class PermitMatcher implements Serializable {

    /**
     * HTTP Method
     */
    private HttpMethod method;

    /**
     * URI
     */
    private String[] uris;

    /**
     * 需要的角色
     */
    private String[] roles;

    /**
     * 需要的权限
     */
    private String[] permissions;
}
