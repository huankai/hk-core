package com.hk.authentication.security.cas.userdetails;

import java.util.Map;
import java.util.Set;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.security.SecurityUserPrincipal;

import lombok.RequiredArgsConstructor;


/**
 * 
 *
 * @description Cas client 用户信息
 * @since V2.1.2
 * @author huangkai
 * @date 2019年1月28日 上午10:22:49
 */
@RequiredArgsConstructor
public class CustomGrantedAuthorityFromAssertionAttributesUserDetailsService
        extends AbstractCasAssertionUserDetailsService {

    private final Set<String> defaultRoles;

    private final Set<String> defaultPermissions;

    @Override
    protected UserDetails loadUserDetails(Assertion assertion) {
        AttributePrincipal principal = assertion.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        SecurityUserPrincipal userPrincipal = new SecurityUserPrincipal(CollectionUtils.getStringValue(attributes, "id"),
                principal.getName(),
                CollectionUtils.getBooleanValue(attributes, "protect_user", false),
                CollectionUtils.getStringValue(attributes, "real_name"),
                CollectionUtils.getByteValue(attributes, "user_type"),
                CollectionUtils.getStringValue(attributes, "phone"),
                CollectionUtils.getStringValue(attributes, "email"),
                CollectionUtils.getByteValue(attributes, "sex"),
                CollectionUtils.getStringValue(attributes, "icon_path"),
                null, CollectionUtils.getByteValue(attributes, "user_status", ByteConstants.TWO));
        userPrincipal.setOrgId(CollectionUtils.getStringValue(attributes, "org_id"));
        userPrincipal.setOrgName(CollectionUtils.getStringValue(attributes, "org_name"));
        userPrincipal.setDeptId(CollectionUtils.getStringValue(attributes, "dept_id"));
        userPrincipal.setDeptName(CollectionUtils.getStringValue(attributes, "dept_name"));
        if (CollectionUtils.isNotEmpty(defaultRoles)) {
            userPrincipal.setRoleSet(defaultRoles);
        }
        if (CollectionUtils.isNotEmpty(defaultPermissions)) {
            userPrincipal.setPermissionSet(defaultPermissions);
        }
        return userPrincipal;
    }

}
