package com.hk.core.authentication.security.config;

import com.google.common.collect.Maps;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.web.JsonResult;
import com.hk.core.web.Webs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 安全配置
 *
 * @author kally
 * @date 2017年12月18日下午5:26:46
 */
@Order(5)
@Configuration
@EnableWebSecurity
public class SecurityWebAutoConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 用户Service
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityContext securityContext;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略不需要认证的url
        web
                .ignoring()
                .antMatchers(HttpMethod.GET, "/login")
                .antMatchers("/api/**", "/resources/**", "/static/**", "/favicon.ico", "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            /* CSRF Disable*/
                .csrf()
                .disable()

            /* Login Config */
                .formLogin()
//            .loginPage("/login") l
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    UserPrincipal principal = securityContext.getPrincipal();
                    Map<String, Object> userMap = Maps.newHashMapWithExpectedSize(5);
                    userMap.put("userId", principal.getUserId());
                    userMap.put("useName", principal.getUserName());
                    userMap.put("nickName", principal.getNickName());
                    userMap.put("sex", principal.getSex());
                    userMap.put("iconPath", principal.getIconPath());
                    Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult(JsonResult.Status.SUCCESS, "登陆成功", userMap));
                })
                .failureHandler((request, response, exception) -> Webs.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, JsonResult.failure(exception.getMessage())))

            /*Logout Config */
                .and()
                .logout()
//            .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessHandler((request, response, authentication) -> Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.success("退出成功")))
                .permitAll()

            /*任意请求都需要认证*/
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

    }
}
