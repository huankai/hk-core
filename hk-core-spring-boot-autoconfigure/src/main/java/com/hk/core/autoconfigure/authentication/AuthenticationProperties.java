package com.hk.core.autoconfigure.authentication;

import com.hk.core.authentication.api.PermitMatcher;
import com.hk.core.autoconfigure.authentication.security.oauth2.Oauth2ClientAutoConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kevin
 * @date 2018-07-26 16:59
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "hk.authentication")
public class AuthenticationProperties {

    /**
     * 手机发送短信验证码
     */
    @NestedConfigurationProperty
    private SMSProperties sms = new SMSProperties();

    /**
     * oauth2 认证参数配置
     */
    @NestedConfigurationProperty
    private Oauth2Properties oauth2 = new Oauth2Properties();

    /**
     * 表单登陆参数配置
     */
    @NestedConfigurationProperty
    private LoginProperties login = new LoginProperties();

//    /**
//     * 图片验证码
//     */
//    @NestedConfigurationProperty
//    private ImageCodeProperties imageCode = new ImageCodeProperties();

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

    /**
     * oauth2 登陆属性
     */
    @Data
    public static class Oauth2Properties {

        /**
         * oauth2 client 登陆默认失败地址
         *
         * @see com.hk.core.autoconfigure.exception.Oauth2ErrorController
         * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#defaultFailureUrl
         * @see Oauth2ClientAutoConfiguration#beanPostProcessor()
         */
        private String oauth2FailureUrl = "/oauth2-error";

        /**
         * 是否使用 https
         */
        private boolean forceHttps = false;
    }

    /* ******************************************************************* */

    /**
     * 登陆处理
     */
    @Data
    public static class LoginProperties {

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
        private String loginProcessingUrl = getLoginUrl();

        /**
         * 登陆失败url
         */
        private String failureUrl = getLoginUrl();

        /**
         * 登陆成功后的地址
         */
        private String loginSuccessUrl = "/";

        /**
         * 退出地址
         */
        private String logoutUrl = "/logout";

        /**
         * 退出成功后的请求地址
         */
        private String logoutSuccessUrl = getLoginUrl();

        /**
         * Session 失效/过期URL
         */
        private String sessionInvalidUrl = "/login";

        /**
         * <p>
         * 不需要登陆授权的URL
         * </p>
         */
        private Set<PermitMatcher> permitMatchers = new HashSet<>();

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
         *
         * @see com.hk.core.authentication.security.savedrequest.GateWayHttpSessionRequestCache#gateWayUrl
         */
        private String gateWayHost;

//        private RememberMeProperties rememberMe = new RememberMeProperties();
    }

//    @Data
//    static class RememberMeProperties {
//
//        /**
//         * 是否开始RememberMe 功能
//         */
//        private boolean enabledRememberMe = false;
//
//        /**
//         * 记住我时间:单位 : 秒
//         * 默认为一周
//         */
//        private int rememberMeSeconds = 3600 * 24 * 7;
//
//        /**
//         * RememberMe Parameter
//         *
//         * @see org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#rememberMeParameter
//         */
//        private String rememberMeParameter = "remember-me";
//
//        /**
//         * RememberMe Parameter
//         *
//         * @see org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#rememberMeCookieName
//         */
//        private String rememberMeCookieName = "remember-me";
//
//        private boolean useSecureCookie = true;
//
//    }

//    @Data
//    static class ImageCodeProperties {
//
//        private boolean enabled = false;
//
//        private int width = 67;
//
//        private int height = 23;
//
//        /**
//         * 生成的验证码长度
//         */
//        private byte codeLength = 4;
//
//        /**
//         * 验证码过期时间,单位:秒
//         */
//        private int codeExpireIn;
//
//        private String codeParameter = "code";
//
//    }


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
        private int codeLength = 4;

        /**
         * 验证码过期时间,单位:秒
         */
        private int codeExpireIn = 180;

        /**
         * 验证码请求参数名
         */
        private String codeParameter = "phoneCode";

        /**
         * 登录短信发送地址
         */
        private String sendUri = "/phone/sender";

        /**
         * 手机号登陆请求地址
         */
        private String phoneLoginUri = "/phone/login";

        /**
         * oauth2 认证服务器 手机号登陆返回 access_token 信息 的 clientId
         */
        private String clientId;

        /**
         * oauth2 认证服务器 手机号登陆返回 access_token 信息 的 clientSecret
         */
        private String clientSecret;
    }

}
