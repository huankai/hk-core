package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.LoginResponseType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: kevin
 * @date 2018-07-26 16:59
 */
@Data
@ConfigurationProperties(prefix = "hk.authentication")
public class AuthenticationProperties {

    /**
     * 手机发送短信验证码
     */
    private SMSProperties sms = new SMSProperties();

    /**
     * 浏览器参数配置
     */
    private BrowserProperties browser = new BrowserProperties();

    /**
     * 图片验证码
     */
    private ImageCodeProperties imageCode = new ImageCodeProperties();

    /**
     * 登陆返回的类型
     */
    private LoginResponseType responseType = LoginResponseType.JSON;


    /**
     * 默认失败页面
     *
     * @see com.hk.core.autoconfigure.exception.DefaultErrorController
     * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#defaultFailureUrl
     */
    private String defaultFailureUrl = "/error";

    /**
     * 是否请求重定向
     *
     * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#forwardToDestination
     */
    private boolean forwardToDestination = false;

    /**
     * 是否可以创建session
     *
     * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#allowSessionCreation
     */
    private boolean allowSessionCreation = true;
    /* ******************************************************************* */

    /**
     * 浏览器登陆处理
     */
    @Data
    public static class BrowserProperties {

        /**
         * Get 登陆页面URL
         */
        private String loginUrl = "/login";

        /**
         * 登陆的用户名参数名
         */
        private String usernameParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

        /**
         * 登陆的密码参数名
         */
        private String passwordParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

        /**
         * POST 登陆处理URL
         */
        private String loginProcessingUrl = "/login";

        /**
         * Session 失效/过期URL
         */
        private String sessionInvalidUrl = "/login";

        /**
         * 退出地址
         */
        private String logoutUrl = "/logout";

        /**
         * 退出成功后的请求地址
         */
        private String logoutSuccessUrl = "/";

        /**
         * <p>
         * 不需要登陆授权的URL
         * </p>
         * <p>
         * 集合中每个元素以英文状态下的逗号(,) 分隔 ：格式为 Request Method , url , authority <br/>
         * ---------------------------------------- <br/>
         * 如果长度为1: 表示 Url <br/>
         * 如果长度为2,表示 请求方法 + url <br/>
         * 如果长度为 3表示 请求方法 + url + 权限 <br/>
         * 如果长度为 大于3 仅表示为 url <br/>
         * ----------------------------------------
         * </p>
         *
         * <p>
         * such as : <br/>
         * /login <--------->  HTTP请求 /login 可任意访问 <br/>
         * GET,/login <--------->  HTTP GET 请求 /login 可任意访问 <br/>
         * GET,/user, user <--------->  HTTP GET 请求 /user 需要 user 权限能才访问 <br/>
         * /user/** <--------->  HTTP 请求 /user/** 可任意访问 <br/>
         * /user,/abc,/def,/xxx <--------->  HTTP 请求 /user 、/abc、/def、/xxx 可任意访问 <br/>
         * </p>
         */
        private Set<String> permitAllMatchers = new HashSet<>();

        /**
         * 同一个用户在系统中的最大session数，默认1
         */
        private int maximumSessions = 1;

        /**
         * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
         */
        private boolean maxSessionsPreventsLogin = false;

        /**
         * 使用gateWay设置地址
         * @see com.hk.core.authentication.security.savedrequest.GateWayHttpSessionRequestCache
         */
        private String gateWayHost;

        private RememberMeProperties rememberMe = new RememberMeProperties();
    }

    @Data
    static class RememberMeProperties {

        /**
         * 是否开始RememberMe 功能
         */
        private boolean enabledRememberMe = false;

        /**
         * 记住我时间:单位 : 秒
         * 默认为一周
         */
        private int rememberMeSeconds = 3600 * 24 * 7;

        /**
         * RememberMe Parameter
         *
         * @see org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#rememberMeParameter
         */
        private String rememberMeParameter = "remember-me";

        /**
         * RememberMe Parameter
         *
         * @see org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#rememberMeCookieName
         */
        private String rememberMeCookieName = "remember-me";

        private boolean useSecureCookie = true;

    }

    @Data
    static class ImageCodeProperties {

        private boolean enabled = false;

        private int width = 67;

        private int height = 23;

        /**
         * 生成的验证码长度
         */
        private byte codeLength = 4;

        /**
         * 验证码过期时间,单位:秒
         */
        private int codeExpireIn;

        private String codeParameter = "code";

    }


    /**
     * 短信验证码配置
     */
    @Data
    public static class SMSProperties {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        /**
         * 只支持POST请求
         */
        private boolean postOnly = true;

        /**
         * 手机号参数名
         */
        private String phoneParameter = "phone";

        /**
         * 生成的验证码长度
         */
        private byte codeLength = 4;

        /**
         * 验证码过期时间,单位:秒
         */
        private int codeExpireIn = 180;

        /**
         * 验证码请求参数名
         */
        private String codeParameter = "phoneCode";

        /**
         * 手机号登陆请求地址
         */
        private String phoneLoginUri = "/mobile/login";
    }

}
