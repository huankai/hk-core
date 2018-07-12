package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.util.SpringContextHolder;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SpringSecurityContext;
import com.hk.core.web.JsonResult;
import com.hk.core.web.Webs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
public class SecurityWebAutoConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 用户Service
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略不需要认证的url
        web
                .ignoring()
                .antMatchers(HttpMethod.GET, "/login")
                .antMatchers("/api/**", "/resources/**", "/static/**", "/favicon.ico", "/webjars/**");
    }

    /**
     * @return
     */
//    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
//        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
//        filter.setContinueChainBeforeSuccessfulAuthentication(true);
//        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
//            UserPrincipal principal = securityContext.getPrincipal();
//            Map<String, Object> userMap = Maps.newHashMapWithExpectedSize(5);
//            userMap.put("userId", principal.getUserId());
//            userMap.put("useName", principal.getUserName());
//            userMap.put("nickName", principal.getNickName());
//            userMap.put("sex", principal.getSex());
//            userMap.put("iconPath", principal.getIconPath());
//            Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult(JsonResult.Status.SUCCESS, SpringContextHolder.getMessage("login.sucess", null), userMap));
//        });
//        filter.setAuthenticationFailureHandler((request, response, exception) -> Webs.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, JsonResult.failure(exception.getMessage())));
//        return filter;
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.formLogin().disable();
//        http.formLogin()/*.disable()*/;
        http
                /* CSRF Disable*/
                .csrf()
                .disable()

                .anonymous().disable()

                /* Login Config */
                .formLogin()/*.disable()*/
                .loginPage("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    UserPrincipal principal = securityContext.getPrincipal();
                    Map<String, Object> userMap = new HashMap<>(5);
                    userMap.put("userId", principal.getUserId());
                    userMap.put("useName", principal.getUserName());
                    userMap.put("nickName", principal.getNickName());
                    userMap.put("sex", principal.getSex());
                    userMap.put("iconPath", principal.getIconPath());
                    Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult(JsonResult.Status.SUCCESS, SpringContextHolder.getMessage("login.sucess", null), userMap));
                })
                .failureHandler((request, response, exception) -> Webs.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, JsonResult.failure(exception.getMessage())))

                /*Logout Config */
                .and()
                .logout()
//            .logoutUrl("/logout")
                .invalidateHttpSession(true)
//                .addLogoutHandler((request, response, authentication) -> System.out.println("logout handler....0")) logout 处理器
                .logoutSuccessHandler((request, response, authentication) ->
                        Webs.writeJson(response, HttpServletResponse.SC_OK, JsonResult.success(SpringContextHolder.getMessage("logout.success", null))))
//                .deleteCookies("") //删除指定的Cookie
                .permitAll()

//        .and().requestMatchers().antMatchers("/api/**")

                /*任意请求都需要认证*/
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

    }
}
