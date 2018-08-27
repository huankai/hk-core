package com.hk.core.authentication.oauth2.converter;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.UserDetailClientService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

/**
 * 本地用户转换
 *
 * @author: kevin
 * @date 2018-08-21 13:07
 */
public class LocalUserAuthenticationConverter implements UserAuthenticationConverter {

    private final UserDetailClientService userDetailClientService;

    public LocalUserAuthenticationConverter(UserDetailClientService userDetailClientService) {
        this.userDetailClientService = userDetailClientService;
    }

    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Map<String, Object> m = new HashMap<>(map);
            String principal = CollectionUtils.getValue(m, USERNAME, String.class);
            UserPrincipal user = UserPrincipal.class.cast(userDetailClientService.loadUserByUsername(principal));
            user.setAppInfo(userDetailClientService.getClientInfoById(CollectionUtils.getValue(m, "client_id", String.class)));
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(user.getPermissionSet())) {
                user.getPermissionSet().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
            }
            return new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
        }
        return null;
    }
}
