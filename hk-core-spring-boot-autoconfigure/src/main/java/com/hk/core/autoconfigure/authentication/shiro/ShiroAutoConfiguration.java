package com.hk.core.autoconfigure.authentication.shiro;

import org.apache.shiro.SecurityUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro Auto configuration
 *
 * @author: sjq-278
 * @date: 2018-12-17 15:23
 */
@Configuration
@ConditionalOnClass(value = {SecurityUtils.class})
public class ShiroAutoConfiguration {


}
