package com.hk.core.authentication.shiro.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.shiro.ShiroSecurityContext;

/**
 * Shiro pac4j configuration
 * 
 * @author huangkai
 * @date 2017年12月21日下午12:38:36
 */
@Configuration
public class AuthenticationShiroAutoConfiguration {

	/* ************Core shiro configuation ************ */

	/**
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(value = SecurityContext.class)
	public ShiroSecurityContext securityContext() {
		return new ShiroSecurityContext();
	}

	/* ************Core pac4j configuation ************ */

	// @Autowired
	// private List<Client> clientList;
	//
	// @Bean
	// public Config config() {
	// Config config = new Config(clientList);
	// config.setSessionStore(sessionStore());
	// return config;
	// }
	//
	// @Bean
	// @ConditionalOnMissingBean(value = Clients.class)
	// public Clients clients() {
	// Clients clients = new Clients();
	// clients.setCallbackUrl("/callback");
	// clients.setClients(clientList);
	// return clients;
	// }

	/*
	 * 需要每个应用配置
	 * 
	 * @return
	 * 
	 * @Bean
	 * 
	 * @ConditionalOnMissingBean(value = Client.class) public List<Client>
	 * defaultClient() { Authenticator<UsernamePasswordCredentials> authenticator =
	 * new LocalCachingAuthenticator<>(); Client<UsernamePasswordCredentials,
	 * CommonProfile> client = new FormClient(null, authenticator); return
	 * Lists.newArrayList(client); }
	 */

	// @Bean
	// @ConditionalOnMissingBean(value = SessionStore.class)
	// public SessionStore sessionStore() {
	// return new ShiroSessionStore();
	// }

	/**
	 *
	 * 
	 * @param config
	 * @return
	 */
	// @Bean
	// @ConditionalOnMissingBean(value = SecurityFilter.class)
	// public SecurityFilter securityFilter(Config config) {
	// SecurityFilter filter = new SecurityFilter();
	// filter.setConfig(config);
	// filter.setClients("form");
	// filter.setSecurityLogic(defaultSecurityLogic());
	// return filter;
	// }
	//
	// /**
	// *
	// * @return
	// */
	// @Bean
	// @ConditionalOnMissingBean(value = SecurityLogic.class)
	// public SecurityLogic<Object, J2EContext> defaultSecurityLogic() {
	// return new DefaultSecurityLogic<>();
	// }
	//
	// /**
	// *
	// * @param config
	// * @param callbackLogic
	// * @return
	// */
	// @Bean
	// public CallbackFilter callbackFilter(Config config, CallbackLogic<Object,
	// J2EContext> callbackLogic) {
	// CallbackFilter filter = new CallbackFilter();
	// filter.setConfig(config);
	// filter.setCallbackLogic(callbackLogic);
	// return filter;
	// }
	//
	// /**
	// *
	// * @return
	// */
	// @Bean
	// @ConditionalOnMissingBean(value = CallbackLogic.class)
	// public CallbackLogic<Object, J2EContext> defaultCallbackLogic() {
	// return new DefaultCallbackLogic<>();
	// }

}
