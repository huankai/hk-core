package com.hk.core.authentication.oauth2.matcher;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.PermitMatcher;
import com.hk.core.authentication.oauth2.utils.AccessTokenUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 非 access_token 请求
 *
 * @author kevin
 * @date 2018-08-27 13:52
 */
public class NoPermitMatcher implements RequestMatcher {

//    private final List<RequestMatcher> requestMatchers;

    public NoPermitMatcher(Set<PermitMatcher> permitMatchers) {
//        this.requestMatchers = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(permitMatchers)) {
//            RequestMatcher matcher;
//            for (var permitMatcher : permitMatchers) {
//                String[] uris = permitMatcher.getUris();
//                if (ArrayUtils.isNotEmpty(uris)) {
//                    for (var item : uris) {
//                        matcher = new AntPathRequestMatcher(item,
//                                permitMatcher.getMethod() == null ? null : permitMatcher.getMethod().name());
//                        requestMatchers.add(matcher);
//                    }
//
//                }
//            }
//        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return StringUtils.isEmpty(AccessTokenUtils.getAccessToken(request));
    }
}
