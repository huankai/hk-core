package com.hk.core.authentication.security.expression;

import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * @author kevin
 * @date 2018-09-14 11:05
 */
public class AdminAccessPermissionEvaluator implements PermissionEvaluator {

    /**
     * 普通的 targetDomainObject 判断
     *
     * @param authentication     authentication
     * @param targetDomainObject targetDomainObject
     * @param permission         permission
     * @return boolean
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.hasPermission(String.valueOf(permission));
    }

    /**
     * ACL访问控制
     *
     * @param authentication authentication
     * @param targetId       targetId
     * @param targetType     targetType
     * @param permission     permission
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
