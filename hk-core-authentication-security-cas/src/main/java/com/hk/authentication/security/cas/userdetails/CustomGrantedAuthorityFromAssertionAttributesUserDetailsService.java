package com.hk.authentication.security.cas.userdetails;

import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.security.SecurityUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Set;


/**
 * @author huangkai
 * @description Cas client 用户信息
 * @date 2019年1月28日 上午10:22:49
 * @since V2.1.2
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
        return new SecurityUserPrincipal(CollectionUtils.getLongValue(attributes, "id"),
                CollectionUtils.getLongValue(attributes, "org_id"),
                CollectionUtils.getStringValue(attributes, "org_name"),
                CollectionUtils.getLongValue(attributes, "dept_id"),
                CollectionUtils.getStringValue(attributes, "dept_name"),
                principal.getName(),
                CollectionUtils.getStringValue(attributes, "real_name"),
                CollectionUtils.getByteValue(attributes, "user_type"),
                CollectionUtils.getStringValue(attributes, "phone"),
                CollectionUtils.getStringValue(attributes, "email"),
                CollectionUtils.getByteValue(attributes, "sex"),
                CollectionUtils.getStringValue(attributes, "icon_path"),
                null, CollectionUtils.getByteValue(attributes, "user_status", ByteConstants.TWO), defaultRoles, defaultPermissions);
    }

}
