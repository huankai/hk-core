package com.hk.core.authentication.api.method.support;

import com.hk.commons.util.ClassUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * 使用 spring mvc controller方法中的参数加入当前登陆用户信息
 * </p>
 * 使用姿势:
 * <pre>
 * \@RequestMapping(path = "xxx")
 * public JsonResult methodName(@LoginUser() UserPrincipal principal,其他参数...){
 *
 * }
 * </pre>
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ClassUtils.isAssignable(UserPrincipal.class, methodParameter.getParameterType())
                && methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return SecurityContextUtils.getPrincipal();
    }
}
