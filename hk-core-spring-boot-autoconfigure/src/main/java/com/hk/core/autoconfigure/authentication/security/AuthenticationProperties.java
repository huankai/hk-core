package com.hk.core.autoconfigure.authentication.security;

import com.hk.core.authentication.api.LoginResponseType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * 浏览器登陆处理
     */
    @Data
    public static class BrowserProperties {

        /**
         * Get 登陆页面URL
         */
        private String loginUrl = "/login";

        /**
         * POST 登陆处理URL
         */
        private String loginProcessingUrl = "/login";

        /**
         * Session 失效/过期URL
         */
        private String sessionInvalidUrl = "/login";

        /**
         * 不需要登陆授权的URL
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
         * Spring Security 可查看 : org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer#rememberMeParameter
         */
        private String rememberMeParameter = "remember-me";

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
    static class SMSProperties {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        /**
         * 只支持POST请求
         */
        private boolean postOnly = true;

        /**
         * 是否需要与账号绑定
         */
        private boolean needBindAccount = false;

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
        private int codeExpireIn = 1800;

        /**
         * 验证码请求参数名
         */
        private String codeParameter = "phoneCode";

        /**
         * 请求地址
         */
        private String phoneRequestUri = "/authentication/mobile";
    }

}
