package com.hk.core.authentication.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.ClientAppInfo;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SecurityUserPrincipal;

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
            UserPrincipal principal = new UserPrincipal((String) map.get("userId"),
                    (String) map.get("account"),
                    (Boolean) map.get("isProtect"),
                    (String) map.get("realName"),
                    Byte.valueOf(map.get("userType").toString()),
                    (String) map.get("phone"),
                    (String) map.get("email"), Byte.valueOf(map.get("sex").toString()),
                    (String) map.get("iconPath"));

            Map<String, String> clientAppInfo = (Map<String, String>) map.get("clientApp");
            if (null != clientAppInfo) {
                principal.setAppInfo(new ClientAppInfo(clientAppInfo.get("appId"), clientAppInfo.get("appCode"),
                        clientAppInfo.get("appName"), clientAppInfo.get("appIcon")));
            }

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roleList = (List<String>) map.get("roles");
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
            List<String> permissionList = (List<String>) map.get("permissions");
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
