package com.hk.core.autoconfigure.authentication.security.oauth2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author: kevin
 * @date 2018-07-16 10:42
 */
//@Order(5)
//@Configuration
//@ConditionalOnClass(ResourceServerConfigurerAdapter.class)
//@EnableResourceServer
//@EnableConfigurationProperties(AuthenticationProperties.class)
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

//    @Autowired
//    private AuthenticationProperties authenticationProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/login","/user").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
//        AuthenticationProperties.BrowserProperties browser = authenticationProperties.getBrowser();
//        Set<String> permitAllMatchers = browser.getPermitAllMatchers();
//        permitAllMatchers.add(browser.getLoginUrl());
//        http
////                /* CSRF Disable*/
//                .csrf().disable() // 跨站请求
////
//////                .httpBasic()
////
//////                .and()  如果启用了 httpBasic 认证，也启用了 formLogin认证，会使用formLogin认证
////
//                .formLogin()
////                .loginPage(browser.getLoginUrl()) //  自定义登陆页，默认为 /login GET请求
//                .loginProcessingUrl(browser.getLoginProcessingUrl()) // 登陆的处理请求，默认为 /login POST请求
//                .and()
//                .authorizeRequests()
//                .antMatchers(permitAllMatchers.toArray(new String[]{})).permitAll() //不需要登陆
//                .anyRequest().authenticated();  // 任意请求都需要登陆
    }
}
