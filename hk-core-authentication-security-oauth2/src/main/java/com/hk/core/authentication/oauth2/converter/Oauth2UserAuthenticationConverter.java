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
import java.util.stream.Collectors;

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
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<?> roleList = CollectionUtils.getValue(map, "roles", List.class);
            Set<String> roles = null;
            if (null != roleList) {
                roles = roleList.stream().map(Object::toString).collect(Collectors.toSet());
                roles.forEach(role -> {
                    if (!StringUtils.startsWith(role, SecurityUserPrincipal.ROLE_PREFIX)) {
                        role = SecurityUserPrincipal.ROLE_PREFIX + role;
                    }
                    authorities.add(new SimpleGrantedAuthority(role));
                });
            }

            List<?> permissionList = CollectionUtils.getValue(map, "permissions", List.class);
            Set<String> permissions = null;
            if (null != permissionList) {
                permissions = permissionList.stream().map(Object::toString).collect(Collectors.toSet());
                permissions.forEach(permissionName -> authorities.add(new SimpleGrantedAuthority(permissionName)));
            }

            UserPrincipal principal = new UserPrincipal(CollectionUtils.getLongValue(map, "userId"),
                    CollectionUtils.getStringValue(map, "account", CollectionUtils.getStringValue(map, USERNAME)),
                    CollectionUtils.getStringValue(map, "realName"),
                    CollectionUtils.getByteValue(map, "userType"),
                    CollectionUtils.getStringValue(map, "phone"),
                    CollectionUtils.getStringValue(map, "email"),
                    CollectionUtils.getByteValue(map, "sex"),
                    CollectionUtils.getStringValue(map, "iconPath"), roles, permissions);
            principal.setOrgId(CollectionUtils.getLongValue(map, "orgId"));
            principal.setOrgName(CollectionUtils.getStringValue(map, "orgName"));
            principal.setDeptId(CollectionUtils.getLongValue(map, "deptId"));
            principal.setDeptName(CollectionUtils.getStringValue(map, "deptName"));
            @SuppressWarnings("unchecked")
            Map<String, String> thirdOpenId = (Map<String, String>) CollectionUtils.getMapValue(map, "thirdOpenId");
            principal.setThirdOpenId(thirdOpenId);
            Map<?, ?> clientAppInfo = CollectionUtils.getMapValue(map, "appInfo");
            if (null != clientAppInfo) {
                principal.setAppInfo(new ClientAppInfo(CollectionUtils.getLongValue(clientAppInfo, "appId"),
                        CollectionUtils.getStringValue(clientAppInfo, "appCode"),
                        CollectionUtils.getStringValue(clientAppInfo, "appName"),
                        CollectionUtils.getStringValue(clientAppInfo, "appIcon")));
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

}
