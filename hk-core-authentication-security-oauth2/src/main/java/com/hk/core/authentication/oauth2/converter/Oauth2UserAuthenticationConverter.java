package com.hk.core.authentication.oauth2.converter;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.ClientAppInfo;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SecurityUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

/**
 * @author kevin
 * @date 2018-08-01 16:24
 * @see org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter
 */
public class Oauth2UserAuthenticationConverter implements UserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * Oauth2 返回的信息
     *
     * @param map map
     * @return authentication
     */
    @Override
    @SuppressWarnings("unchecked")
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Map<String, Object> m = (Map<String, Object>) map;
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roleList = CollectionUtils.getValue(m, "roleSet", List.class);
            Set<String> roles = new HashSet<>();
            if (null != roleList) {
                roles.addAll(roleList);
                roleList.forEach(role -> {
                    if (!StringUtils.startsWith(role, SecurityUserPrincipal.ROLE_PREFIX)) {
                        role = SecurityUserPrincipal.ROLE_PREFIX + role;
                    }
                    authorities.add(new SimpleGrantedAuthority(role));
                });
            }

            List<String> permissionList = CollectionUtils.getValue(m, "permissionSet", List.class);
            Set<String> permissions = new HashSet<>();
            if (null != permissionList) {
                permissions.addAll(permissionList);
                permissionList.forEach(permissionName -> authorities.add(new SimpleGrantedAuthority(permissionName)));
            }

            UserPrincipal principal = new UserPrincipal(CollectionUtils.getStringValue(m, "userId"),
                    CollectionUtils.getStringValue(m, "account", CollectionUtils.getStringValue(m, USERNAME)),
                    CollectionUtils.getBooleanValue(m, "protectUser", false),
                    CollectionUtils.getStringValue(m, "realName"),
                    CollectionUtils.getByteValue(m, "userType"),
                    CollectionUtils.getStringValue(m, "phone"),
                    CollectionUtils.getStringValue(m, "email"),
                    CollectionUtils.getByteValue(m, "sex"),
                    CollectionUtils.getStringValue(m, "iconPath"), roles, permissions);
            principal.setOrgId(CollectionUtils.getStringValue(m, "orgId"));
            principal.setOrgName(CollectionUtils.getStringValue(m, "orgName"));
            principal.setDeptId(CollectionUtils.getStringValue(m, "deptId"));
            principal.setDeptName(CollectionUtils.getStringValue(m, "deptName"));

            Map<String, Object> clientAppInfo = CollectionUtils.getMapValue(m, "appInfo");
            if (null != clientAppInfo) {
                principal.setAppInfo(new ClientAppInfo(CollectionUtils.getStringValue(clientAppInfo, "appId"),
                        CollectionUtils.getStringValue(clientAppInfo, "appCode"),
                        CollectionUtils.getStringValue(clientAppInfo, "appName"),
                        CollectionUtils.getStringValue(clientAppInfo, "appIcon")));
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

}
