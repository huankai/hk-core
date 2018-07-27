package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.sms.SmsCodeSender;
import com.hk.core.authentication.security.SpringSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;
import java.util.Set;

/**
 * 安全配置
 *
 * @author: kevin
 * @date 2017年12月18日下午5:26:46
 */
@Order(5)
@Configuration
@EnableWebSecurity
@ConditionalOnClass(value = {SpringSecurityContext.class})
@AutoConfigureAfter(SecurityAuthenticationAutoConfiguration.class)
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityWebAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationProperties properties;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    /**
     * 用户Service
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private DataSource dataSource;

    public SecurityWebAutoConfiguration(AuthenticationProperties authenticationProperties) {
        this.properties = authenticationProperties;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 如果是支持短信验证，请配置此bean
     */
    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationProperties.BrowserProperties browser = properties.getBrowser();
        Set<String> permitAllMatchers = browser.getPermitAllMatchers();
        permitAllMatchers.add(browser.getLoginUrl());
        http
                .apply(new SmsAuthenticationSecurityConfiguration(properties.getSms(), successHandler, failureHandler, userDetailsService, smsCodeSender)) // 短信验证码配置
                .and()
                .apply(new ImageAuthenticationSecurityConfiguration(properties.getImageCode(), failureHandler, browser.getLoginProcessingUrl())) // 图片验证码配置
                .and()
                .apply(new RememberMeAuthenticationSecurityConfiguration(browser.getRememberMe(), successHandler, userDetailsService, tokenRepository())) // 图片验证码配置
                .and()
//                /* CSRF Disable*/
                .csrf().disable() // 跨站请求
//
////                .httpBasic()
//
////                .and()  如果启用了 httpBasic 认证，也启用了 formLogin认证，会使用formLogin认证
//
                .formLogin()
//                .loginPage(browser.getLoginUrl()) //  自定义登陆页，默认为 /login GET请求
                .loginProcessingUrl(browser.getLoginProcessingUrl()) // 登陆的处理请求，默认为 /login POST请求
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .and()

                .sessionManagement() //Session 管理
                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(browser.getMaximumSessions())
                .maxSessionsPreventsLogin(browser.isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()

                .and()
                .authorizeRequests()
                .antMatchers(permitAllMatchers.toArray(new String[]{})).permitAll() //不需要登陆
                .anyRequest().authenticated()  // 任意请求都需要登陆
//
                .and()
                .anonymous().disable(); // 禁用匿名用户
    }

    /**
     * 不定义没有password grant_type
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @ConditionalOnMissingBean(PersistentTokenRepository.class)
    public JdbcTokenRepositoryImpl tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false); //启动时是否创建表,因为已创建过,所以不需要创建了,
        //创建表的语句请查看 org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl.CREATE_TABLE_SQL
        return tokenRepository;
    }
}
