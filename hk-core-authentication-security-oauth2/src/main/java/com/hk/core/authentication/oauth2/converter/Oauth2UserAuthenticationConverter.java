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
 * @author: kevin
 * @date 2018-08-01 16:24
 * @see org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter
 */
public class Oauth2UserAuthenticationConverter implements UserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
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
            UserPrincipal principal = new UserPrincipal(CollectionUtils.getValue(m, "userId", String.class),
                    CollectionUtils.getValueOrDefault(m, "account",
                            CollectionUtils.getValue(m, USERNAME, String.class), String.class),
                    CollectionUtils.getValueOrDefault(m, "isProtect", false, Boolean.class),
                    CollectionUtils.getValue(m, "realName", String.class),
                    Byte.valueOf(CollectionUtils.getValueOrDefault(m, "userType", 0, Integer.class).toString()),
                    CollectionUtils.getValue(m, "phone", String.class),
                    CollectionUtils.getValue(m, "email", String.class),
                    Byte.valueOf(CollectionUtils.getValueOrDefault(m, "sex", 0, Integer.class).toString()),
                    CollectionUtils.getValue(m, "iconPath", String.class));

            Map<String, Object> clientAppInfo = CollectionUtils.getValue(m, "clientApp", Map.class);
            if (null != clientAppInfo) {
                principal.setAppInfo(new ClientAppInfo(CollectionUtils.getValue(clientAppInfo, "appId", String.class),
                        CollectionUtils.getValue(clientAppInfo, "appCode", String.class),
                        CollectionUtils.getValue(clientAppInfo, "appName", String.class),
                        CollectionUtils.getValue(clientAppInfo, "appIcon", String.class)));
            }

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roleList = CollectionUtils.getValue(m, "roles", List.class);
            if (null != roleList) {
                Set<String> roleSet = new HashSet<>(roleList);
                principal.setRoleSet(roleSet);
                roleSet.forEach(role -> {
                    if (!StringUtils.startsWith(role, SecurityUserPrincipal.ROLE_PREFIX)) {
                        role = SecurityUserPrincipal.ROLE_PREFIX + role;
                    }
                    authorities.add(new SimpleGrantedAuthority(role));
                });
            }
            List<String> permissionList = CollectionUtils.getValue(m, "permissions", List.class);
            if (null != permissionList) {
                Set<String> permissionSet = new HashSet<>(permissionList);
                principal.setPermissionSet(permissionSet);
                permissionSet.forEach(permissionName -> authorities.add(new SimpleGrantedAuthority(permissionName)));
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

}
